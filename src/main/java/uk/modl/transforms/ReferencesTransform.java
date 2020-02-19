package uk.modl.transforms;

import io.vavr.*;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import uk.modl.model.*;
import uk.modl.parser.errors.InterpreterError;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class ReferencesTransform implements Function1<Structure, Structure> {
    private static Pattern referencePattern = Pattern.compile("((%\\w+)(\\.\\w*<`?\\w*`?,`\\w*`>)+|(%` ?[\\w-]+`[\\w.<>,]*%?)|(%\\*?[\\w]+(\\.%?\\w*<?[\\w,]*>?)*%?))");

    /**
     * The context for this invocation of the interpreter
     */
    @NonNull
    private TransformationContext ctx;

    /**
     * Possible targets of references
     */
    private Map<String, Pair> pairs = HashMap.empty();
    /**
     * Pair keys with a reference in the Pair value
     */
    private Map<String, Pair> pairKeysWithReferences = HashMap.empty();
    /**
     * The Modl Object Index
     */
    private Array objectIndex;

    /**
     * Determine the ReferenceType for a reference String
     *
     * @return the ReferenceType
     */
    private static ReferenceType refToRefType(final String s) {
        if (s.startsWith("%*")) {
            return ReferenceType.INSTRUCTION_REF;
        }
        if (s.contains(".")) {
            return ReferenceType.COMPLEX_REF;
        }
        final String refKey = stripLeadingAndTrailingPercents(s);
        if (StringUtils.isNumeric(refKey)) {
            return ReferenceType.OBJECT_INDEX_REF;
        }
        return ReferenceType.SIMPLE_REF;
    }

    /**
     * Convert %var% to var for use in pair lookups
     *
     * @param ref a String with a leading % and/or trailing %
     * @return a String with leading and trailing %'s removed
     */
    private static String stripLeadingAndTrailingPercents(final String ref) {
        return StringUtils.removeEnd(StringUtils.removeStart(ref, "%"), "%");
    }

    /**
     * Capture pairs as potential targets of references, the Object Index if there is one, and pairs that contain
     * references since we'll need to process those.
     *
     * @param pair a Pair
     */
    private void accept(final Pair pair) {
        if (pair.key.equals("?")) {

            // Capture the Object Index
            if (pair.value instanceof Array) {
                objectIndex = (Array) pair.value;
            } else {
                throw new InterpreterError("Object index is not an Array");
            }
        } else {
            // Keep a map of pairs indexed by their key...
            pairs = pairs.put(pair.key, pair);

            // ...and by their key without the underscore prefix if there is one.
            if (pair.key.startsWith("_")) {
                pairs = pairs.put(pair.key.substring(1), pair);
            }

            // References will only be in StringPrimitives
            if (pair.value instanceof StringPrimitive) {
                final StringPrimitive prim = (StringPrimitive) pair.value;

                // Keep track of pairs with references in their value
                if (prim.value.contains("%")) {
                    pairKeysWithReferences = pairKeysWithReferences.put(pair.key, pair);

                    // Also track them without the underscore prefix if there is one
                    if (pair.key.startsWith("_")) {
                        pairKeysWithReferences = pairKeysWithReferences.put(pair.key.substring(1), pair);
                    }
                }
            }
        }
        resolve();
    }

    /**
     * Replace references in a Modl object
     *
     * @param modl the Modl object
     * @return the updated copy of the Modl object
     */
    private Modl replace(final Modl modl) {
        final Vector<Structure> list = Vector.ofAll(modl.structures.map(this::replace));
        return new Modl(list);
    }

    /**
     * Dispatch `replace()` based on the Structure type
     *
     * @param structure a Structure
     * @return an updated Structure
     */
    private Structure replace(final Structure structure) {
        if (structure instanceof Pair) {
            return replace((Pair) structure);
        } else if (structure instanceof uk.modl.model.Map) {
            return replace((uk.modl.model.Map) structure);
        } else if (structure instanceof uk.modl.model.Array) {
            return replace((uk.modl.model.Array) structure);
        } else if (structure instanceof uk.modl.model.TopLevelConditional) {
            return replace((uk.modl.model.TopLevelConditional) structure);
        }
        return structure;
    }

    /**
     * Handle references in a Map
     *
     * @param map the Map
     * @return a possibly new Map
     */
    private uk.modl.model.Map replace(final uk.modl.model.Map map) {
        final Vector<MapItem> mapItems = Vector.ofAll(map.mapItems.map(mi -> {
            if (mi instanceof Pair) {
                return replace((Pair) mi);
            } else if (mi instanceof MapConditional) {
                return replace((MapConditional) mi);
            } else {
                throw new InterpreterError("Unknown MapItem type: " + mi.getClass());
            }
        }));

        // Was it updated?
        if (!mapItems.eq(map.mapItems)) {
            return new uk.modl.model.Map(mapItems);
        }
        // Return the existing map if there were no changes
        return map;
    }

    /**
     * Replace the elements of an Array if necessary
     *
     * @param arr the Array that might contain references
     * @return a possibly updated Array
     */
    private Array replace(final uk.modl.model.Array arr) {
        final Vector<ArrayItem> arrayItems = Vector.ofAll(arr.arrayItems.map(ai -> {
            if (ai instanceof Pair) {
                return replace((Pair) ai);
            } else if (ai instanceof uk.modl.model.Map) {
                return replace((uk.modl.model.Map) ai);
            } else if (ai instanceof Array) {
                return replace((Array) ai);
            } else if (ai instanceof Primitive) {
                return ai;// Primitives are handled as Pair values
            } else if (ai instanceof ArrayConditional) {
                return replace((ArrayConditional) ai);
            } else {
                throw new InterpreterError("Unknown ArrayItem type: " + ai.getClass());
            }
        }));

        // Was it updated?
        if (!arrayItems.eq(arr.arrayItems)) {
            return new uk.modl.model.Array(arrayItems);
        }
        // Return the existing Array if there were no changes
        return arr;
    }

    /**
     * Replace if necessary
     *
     * @param conditional an ArrayConditional
     * @return an ArrayConditional
     */
    private ArrayConditional replace(final ArrayConditional conditional) {

        final Vector<ConditionTest> newConditionTests = conditional.tests.map(this::replace);

        final Vector<ArrayConditionalReturn> arrayConditionalReturns = conditional.returns.map(ret -> {
            final Vector<ArrayItem> arrayItems = ret.items.map(ai -> {
                if (ai instanceof ArrayConditional) {
                    return replace((ArrayConditional) ai);
                } else if (ai instanceof uk.modl.model.Map) {
                    return replace((uk.modl.model.Map) ai);
                } else if (ai instanceof Pair) {
                    return replace((Pair) ai);
                } else if (ai instanceof Array) {
                    return replace((Array) ai);
                } else if (ai instanceof Primitive) {
                    return (Primitive) replace((Primitive) ai);
                } else {
                    throw new InterpreterError("Unknown ArrayItem type: " + ai.getClass());
                }
            });

            if (!ret.items.equals(arrayItems)) {
                return new ArrayConditionalReturn(arrayItems);
            }
            return ret;
        });

        if (!newConditionTests.equals(conditional.tests) || !arrayConditionalReturns.equals(conditional.returns)) {
            return new ArrayConditional(newConditionTests, arrayConditionalReturns);
        }
        return conditional;
    }

    /**
     * Replace if necessary
     *
     * @param test a ConditionTest
     * @return a ConditionTest
     */
    private ConditionTest replace(final ConditionTest test) {
        final Vector<Tuple2<ConditionOrConditionGroupInterface, String>> newConditions = test.conditions.map(cond -> {
            if (cond._1 instanceof Condition) {
                final Condition condition = (Condition) cond._1;
                return cond.update1(replace(condition));
            } else if (cond._1 instanceof ConditionGroup) {
                final ConditionGroup cg = (ConditionGroup) cond._1;
                return cond.update1(replace(cg));
            }
            return cond;
        });

        if (!newConditions.equals(test.conditions)) {
            return new ConditionTest(newConditions);
        }
        return test;
    }

    /**
     * Replace if necessary
     *
     * @param cg a ConditionGroup
     * @return a ConditionGroup
     */
    private ConditionGroup replace(final ConditionGroup cg) {
        final Vector<Tuple2<ConditionTest, String>> tests = cg.subConditionList.map(tuple -> tuple.update1(replace(tuple._1)));
        if (!tests.equals(cg.subConditionList)) {
            return new ConditionGroup(tests);
        }
        return cg;
    }

    /**
     * Replace if necessary
     *
     * @param condition a Condition
     * @return a Condition
     */
    private Condition replace(final Condition condition) {
        final String lhs = condition.lhs;
        final String newLhs = (lhs == null) ?
                null :
                (lhs.contains("%")) ?
                        replace(lhs) :
                        pairs.containsKey(lhs) ?
                                pairs.get(lhs)
                                        .get().value.toString() :
                                lhs;
        final Vector<ValueItem> values = condition.values;
        final Vector<ValueItem> valueItems = values.map(this::replace);

        if (!valueItems.equals(values) || !(lhs != null && lhs.equals(newLhs))) {
            return new Condition(newLhs, condition.op, valueItems);
        }
        return condition;
    }

    /**
     * Replace if necessary
     *
     * @param v a ValueConditional
     * @return a ValueConditional
     */
    private ValueConditional replace(final ValueConditional v) {
        final Vector<ConditionTest> tests = v.tests.map(this::replace);
        final Vector<ValueConditionalReturn> returns = v.returns.map(this::replace);
        if (!tests.equals(v.tests) || !returns.equals(v.returns)) {
            return new ValueConditional(tests, returns);
        }
        return v;
    }

    /**
     * Replace if necessary
     *
     * @param ret a ValueConditionalReturn
     * @return a ValueConditionalReturn
     */
    private ValueConditionalReturn replace(final ValueConditionalReturn ret) {
        final Vector<ValueItem> items = ret.items.map(this::replace);
        if (!items.equals(ret.items)) {
            return new ValueConditionalReturn(items);
        }
        return ret;
    }

    /**
     * Replace if necessary
     *
     * @param v a ValueItem
     * @return a ValueItem
     */
    private ValueItem replace(final ValueItem v) {
        if (v instanceof ValueConditional) {
            return replace((ValueConditional) v);
        } else if (v instanceof uk.modl.model.Map) {
            return replace((uk.modl.model.Map) v);
        } else if (v instanceof Pair) {
            return replace((Pair) v);
        } else if (v instanceof Array) {
            return replace((Array) v);
        } else if (v instanceof StringPrimitive) {
            final String s = replace(((StringPrimitive) v).value);
            if (!s.equals(((StringPrimitive) v).value)) {
                return new StringPrimitive(s);
            }
        }
        return v;
    }

    /**
     * Replace if necessary
     *
     * @param s a String
     * @return a String
     */
    private String replace(final String s) {
        final Map<ReferenceType, Vector<String>> groupedByType = getReferenceGroups(s);

        // Process the OBJECT_INDEX_REF entries
        String result = groupedByType.get(ReferenceType.OBJECT_INDEX_REF)
                .map(this::indexToReferencedObject)
                .map(replaceAllObjectIndexRefsInString(s))
                .getOrElse(s);

        // Process simple String references, e.g. %val% etc.
        result = groupedByType.get(ReferenceType.SIMPLE_REF)
                .map(this::keyToReferencedObject)
                .map(replaceAllSimpleRefsInString(result))
                .getOrElse(result);
        return result;
    }

    /**
     * Replace if necessary
     *
     * @param tlc a TopLevelConditional
     * @return a TopLevelConditional
     */
    private Structure replace(final TopLevelConditional tlc) {
        final Vector<ConditionTest> tests = tlc.tests.map(this::replace);
        final Vector<TopLevelConditionalReturn> returns = tlc.returns.map(this::replace);
        if (!tests.equals(tlc.tests) || !returns.equals(tlc.returns)) {
            return new TopLevelConditional(tests, returns);
        }
        return tlc;
    }

    /**
     * Replace if necessary
     *
     * @param tlcr a TopLevelConditionalReturn
     * @return a TopLevelConditionalReturn
     */
    private TopLevelConditionalReturn replace(final TopLevelConditionalReturn tlcr) {
        final Vector<Structure> structures = tlcr.structures.map(this::replace);
        if (!structures.equals(tlcr.structures)) {
            return new TopLevelConditionalReturn(structures);
        }
        return tlcr;
    }

    /**
     * Replace if necessary
     *
     * @param mi a MapConditional
     * @return a MapConditional
     */
    private MapConditional replace(final MapConditional mi) {
        final Vector<ConditionTest> tests = mi.tests.map(this::replace);
        final Vector<MapConditionalReturn> returns = mi.returns.map(this::replace);
        if (!tests.equals(mi.tests) || !returns.equals(mi.returns)) {
            return new MapConditional(tests, returns);
        }
        return mi;
    }

    /**
     * Replace if necessary
     *
     * @param mcr a MapConditionalReturn
     * @return a MapConditionalReturn
     */
    private MapConditionalReturn replace(final MapConditionalReturn mcr) {
        final Vector<MapItem> mapItems = mcr.items.map(this::replace);
        if (!mapItems.equals(mcr.items)) {
            return new MapConditionalReturn(mapItems);
        }
        return mcr;
    }

    /**
     * Replace if necessary
     *
     * @param mi a MapItem
     * @return a MapItem
     */
    private MapItem replace(final MapItem mi) {
        if (mi instanceof MapConditional) {
            return replace((MapConditional) mi);
        } else if (mi instanceof Pair) {
            return replace((Pair) mi);
        }
        return mi;
    }

    /**
     * Replace a pair with a new value that has its references resolved.
     *
     * @param p a Pair with references
     * @return a Pair with the references resolved
     */
    private Pair replace(final Pair p) {
        accept(p);
        resolve();
        if (p.value instanceof ValueConditional) {
            final ValueConditional vc = replace((ValueConditional) p.value);
            return new Pair(p.key, vc);
        } else {
            return pairKeysWithReferences.get(p.key)
                    .getOrElse(p);
        }
    }

    /**
     * Update the pairKeysWithReferences to new values with the references replaced by actual values
     */
    private void resolve() {
        pairKeysWithReferences = HashMap.ofEntries(
                pairKeysWithReferences.map(tuple2 -> {

                    final Map<ReferenceType, Vector<String>> groupedByType = getReferenceGroups(tuple2._2.value.toString());

                    // Process the OBJECT_INDEX_REF entries
                    Tuple2<String, Pair> result = groupedByType.get(ReferenceType.OBJECT_INDEX_REF)
                            .map(this::indexToReferencedObject)
                            .map(replaceAllObjectIndexRefs(tuple2))
                            .getOrElse(tuple2);

                    // Process simple String references, e.g. %val% etc.
                    result = groupedByType.get(ReferenceType.SIMPLE_REF)
                            .map(this::keyToReferencedObject)
                            .map(replaceAllSimpleRefs(result))
                            .getOrElse(result);


                    // Handle %* references
                    result = groupedByType.get(ReferenceType.INSTRUCTION_REF)
                            .map(this::instructionToReferencedItems)
                            .map(replaceInstructionReference(result))
                            .getOrElse(result);

                    // TODO: process complex references

                    return result;
                }));
    }

    private Function<Array, Tuple2<String, Pair>> replaceInstructionReference(final Tuple2<String, Pair> result) {
        return items -> Tuple.of(result._1, new Pair(result._1, items));
    }

    private Array instructionToReferencedItems(final Vector<String> instructionRef) {
        final Vector<ArrayItem> list = instructionRef.flatMap(ir -> {
            if ("%*load".equals(ir)) {
                return ctx.getFilesLoaded()
                        .map(f -> (ArrayItem) new StringPrimitive(f));
            } else if ("%*class".equals(ir)) {
                return ctx.getClasses()
                        .map(this::classInstructionToArrayItem);
            } else if ("%*method".equals(ir)) {
                return ctx.getMethods()
                        .map(this::methodInstructionToArrayItem);
            }
            return Vector.empty();
        });
        return new Array(list);
    }

    /**
     * Convert a StarMethodTransform.MethodInstruction to an ArrayItem
     *
     * @param m a StarMethodTransform.MethodInstruction
     * @return an ArrayItem
     */
    private ArrayItem methodInstructionToArrayItem(final StarMethodTransform.MethodInstruction m) {
        Vector<MapItem> mthdItems = Vector.empty();
        final Pair transformPair = new Pair("transform", new StringPrimitive(m.transform));
        if (m.name != null) {
            final Pair namePair = new Pair("name", new StringPrimitive(m.name));
            mthdItems = mthdItems.append(namePair);
        }
        mthdItems = mthdItems.append(transformPair);


        final MapItem mthdMap = new Pair(m.id, new uk.modl.model.Map(mthdItems));
        final Vector<MapItem> mthd = Vector.of(mthdMap);
        return new uk.modl.model.Map(mthd);
    }

    /**
     * Convert a StarClassTransform.ClassInstruction to an ArrayItem
     *
     * @param ci StarClassTransform.ClassInstruction
     * @return an ArrayItem
     */
    private ArrayItem classInstructionToArrayItem(final StarClassTransform.ClassInstruction ci) {

        Vector<MapItem> clssItems = Vector.empty();

        if (ci.name != null) {
            final Pair p = new Pair("name", new StringPrimitive(ci.name));
            clssItems = clssItems.append(p);
        }

        if (ci.superclass != null) {
            final Pair p = new Pair("superclass", new StringPrimitive(ci.superclass));
            clssItems = clssItems.append(p);
        }

        if (ci.assign != null) {
            final Pair p = new Pair("assign", ci.assign);
            clssItems = clssItems.append(p);
        }

        if (ci.pairs != null) {
            clssItems = clssItems.appendAll(ci.pairs);
        }

        final MapItem clssMap = new Pair(ci.id, new uk.modl.model.Map(clssItems));
        final Vector<MapItem> clss = Vector.of(clssMap);
        return new uk.modl.model.Map(clss);
    }

    /**
     * Find references in a String
     *
     * @param stringWithRefs a String
     * @return a Map of references by ReferenceType
     */
    private Map<ReferenceType, Vector<String>> getReferenceGroups(final String stringWithRefs) {
        final Matcher matcher = referencePattern.matcher(stringWithRefs);

        // Gather the match groups into a list of references
        Vector<String> groups = Vector.empty();
        while (matcher.find()) {
            groups = groups.append(matcher.group());
        }

        // Partition the references by reference type
        return groups.groupBy(ReferencesTransform::refToRefType);
    }

    /**
     * Return a function to fold a list of replacement values into a value with references, i.e. replace references with their actual values.
     *
     * @param tuple2 a Tuple2 of a String key and a Pair with references
     * @return a function to convert the Pair in the supplied tuple to a Pair with references replaced
     */
    private Function<Vector<Tuple4<String, String, Integer, Option<ArrayItem>>>, Tuple2<String, Pair>> replaceAllObjectIndexRefs(final Tuple2<String, Pair> tuple2) {
        return refTuples -> refTuples.foldLeft(tuple2, replaceObjectIndexRefInArrayItem());
    }

    /**
     * Return a function to fold a list of replacement values into a value with references, i.e. replace references with their actual values.
     *
     * @param s a String with references
     * @return a function to convert the String to a String with references replaced
     */
    private Function<Vector<Tuple4<String, String, Integer, Option<ArrayItem>>>, String> replaceAllObjectIndexRefsInString(final String s) {
        return refTuples -> refTuples.foldLeft(s, replaceObjectIndexRefInString());
    }

    /**
     * Return a function to fold a list of replacement values into a value with references, i.e. replace references with their actual values.
     *
     * @param tuple2 a Tuple2 of a String key and a Pair with references
     * @return a function to convert the Pair in the supplied tuple to a Pair with references replaced
     */
    private Function<Vector<Tuple3<String, String, Option<Pair>>>, Tuple2<String, Pair>> replaceAllSimpleRefs(final Tuple2<String, Pair> tuple2) {
        return refTuples -> refTuples.foldLeft(tuple2, replaceSimpleRefInPair());
    }

    /**
     * Return a function to fold a list of replacement values into a value with references, i.e. replace references with their actual values.
     *
     * @param s a String with references
     * @return a function to convert the String to a String with references replaced
     */
    private Function<Vector<Tuple3<String, String, Option<Pair>>>, String> replaceAllSimpleRefsInString(final String s) {
        return refTuples -> refTuples.foldLeft(s, replaceSimpleRefInString());
    }

    /**
     * Map an index to the object that it references
     *
     * @param list a Vector of String indexes of the form `%1%` or `%1`
     * @return a tuple of the original reference, %-stripped reference, integer value, and possible new value, e.g. ("%1%","1", 1, Option(ArrayItem))
     */
    private Vector<Tuple4<String, String, Integer, Option<ArrayItem>>> indexToReferencedObject(final Vector<String> list) {
        return list.map(s -> Tuple.of(s, stripLeadingAndTrailingPercents(s)))// E.g. ("%1%","1")
                .map(t -> t.append(Integer.parseInt(t._2)))// E.g. ("%1%","1", 1)
                .map(t -> {
                    if (t._3 >= 0 && t._3 < objectIndex.arrayItems.size()) {
                        return t.append(Option.of(objectIndex.arrayItems.get(t._3)));
                    }
                    return t.append(Option.none());
                });// E.g. ("%1%","1", 1, Option(ArrayItem))
    }

    /**
     * Map a simple key to the Pair that it references
     *
     * @param list a Vector of String indexes of the form `%var%` or `%var`
     * @return a tuple of the original reference, %-stripped reference, and possible new value, e.g. ("%var%","var", Option(Pair))
     */
    private Vector<Tuple3<String, String, Option<Pair>>> keyToReferencedObject(final Vector<String> list) {
        return list.map(s -> Tuple.of(s, stripLeadingAndTrailingPercents(s)))// E.g. ("%var%","var")
                .map(t -> t.append(this.pairs.get(t._2)));// E.g. ("%var%","var", Option(Pair))
    }

    /**
     * Update the Pair in the `curr` tuple by replacing the reference if it matches the index in the `next` tuple
     *
     * @return a tuple with an updated Pair
     */
    private BiFunction<Tuple2<String, Pair>, Tuple4<String, String, Integer, Option<ArrayItem>>, Tuple2<String, Pair>> replaceObjectIndexRefInArrayItem() {
        return (curr, next) -> {
            if (next._4.isDefined()) {
                // If the item containing the reference is a StringPrimitive then do String substitution
                if (next._4.get() instanceof StringPrimitive) {
                    final String s = ((StringPrimitive) curr._2.value).value;
                    final String r = objectIndex.arrayItems.get(next._3)
                            .toString();
                    return curr.update2(new Pair(curr._1, new StringPrimitive(s.replace(next._1, r))));
                }
                // Otherwise replace the whole thing
                return curr.update2(new Pair(curr._1, (PairValue) objectIndex.arrayItems.get(next._3)));
            }
            return curr;
        };
    }

    /**
     * Update the `curr` value by replacing the reference if it matches the index in the `next` tuple
     *
     * @return a tuple with an updated Pair
     */
    private BiFunction<String, Tuple4<String, String, Integer, Option<ArrayItem>>, String> replaceObjectIndexRefInString() {
        return (curr, next) -> {
            if (next._4.isDefined()) {
                // If the item containing the reference is a StringPrimitive then do String substitution
                if (next._4.get() instanceof StringPrimitive) {
                    final String r = objectIndex.arrayItems.get(next._3)
                            .toString();
                    return curr.replace(next._1, r);
                }
            }
            return curr;
        };
    }

    /**
     * Update the Pair in the `curr` tuple by replacing the reference if it matches the index in the `next` tuple
     *
     * @return a tuple with an updated Pair
     */
    private BiFunction<Tuple2<String, Pair>, Tuple3<String, String, Option<Pair>>, Tuple2<String, Pair>> replaceSimpleRefInPair() {
        return (curr, next) -> {
            if (next._3.isDefined()) {
                // If the item containing the reference is a StringPrimitive then do String substitution
                if (next._3.get().value instanceof StringPrimitive) {
                    final String s = ((StringPrimitive) curr._2.value).value;
                    final String r = ((StringPrimitive) next._3.get().value).value;
                    return curr.update2(new Pair(curr._1, new StringPrimitive(s.replace(next._1, r))));
                }
                // Otherwise replace the whole thing
                return curr.update2(next._3.get());
            }
            return curr;
        };
    }

    /**
     * Update `curr` String by replacing the reference if it matches the index in the `next` tuple
     *
     * @return an updated String
     */
    private BiFunction<String, Tuple3<String, String, Option<Pair>>, String> replaceSimpleRefInString() {
        return (curr, next) -> {
            if (next._3.isDefined()) {
                // If the item containing the reference is a StringPrimitive then do String substitution
                if (next._3.get().value instanceof StringPrimitive) {
                    final String r = ((StringPrimitive) next._3.get().value).value;
                    return curr.replace(next._1, r);
                }
            }
            return curr;
        };
    }

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param structure argument 1
     * @return the result of function application
     */
    @Override
    public Structure apply(final Structure structure) {
        return replace(structure);
    }

    public void seCtx(final TransformationContext ctx) {
        this.ctx = ctx;
    }

    private enum ReferenceType {
        INSTRUCTION_REF, COMPLEX_REF, OBJECT_INDEX_REF, SIMPLE_REF
    }
}
