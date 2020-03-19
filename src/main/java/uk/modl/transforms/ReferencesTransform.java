package uk.modl.transforms;

import io.vavr.Function1;
import io.vavr.Tuple;
import io.vavr.Tuple3;
import io.vavr.Tuple4;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import uk.modl.model.*;
import uk.modl.parser.errors.InterpreterError;
import uk.modl.utils.Util;

import java.net.IDN;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class ReferencesTransform implements Function1<Pair, Pair> {
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
                objectIndex = new Array(Vector.of((ArrayItem) pair.value));
            }
        } else {
            // Keep a map of pairs indexed by their key...
            pairs = pairs.put(pair.key, pair);

            // ...and by their key without the underscore prefix if there is one.
            if (pair.key.startsWith("_")) {
                pairs = pairs.put(pair.key.substring(1), pair);
            }
        }
    }


    /**
     * Replace if necessary
     *
     * @param condition a Condition
     * @return a Condition
     */
    public Condition apply(final Condition condition) {
        final ValueItem lhs = condition.lhs;

        ValueItem newLhs = lhs;
        Operator op = condition.op;
        Vector<ValueItem> values = condition.values;

        if (lhs instanceof StringPrimitive) {
            final String value = ((StringPrimitive) lhs).value;

            if (op == null) {
                newLhs = values.get(0);
                newLhs = apply(newLhs);
                if (!(newLhs instanceof TruePrimitive || newLhs instanceof FalsePrimitive)) {
                    final String key = newLhs.toString();
                    if (pairs.containsKey(key)) {
                        newLhs = (ValueItem) pairs.get(key)
                                .get().value;
                        values = Vector.of(newLhs);
                        if (newLhs instanceof FalsePrimitive) {
                            newLhs = TruePrimitive.instance;// Force a false match result
                        }
                    } else {
                        newLhs = FalsePrimitive.instance;
                    }
                }
                op = EqualsOperator.instance;
            }

            if (value != null) {
                if (value.contains("%")) {
                    newLhs = apply(newLhs);
                } else {
                    if (pairs.containsKey(value)) {
                        newLhs = (ValueItem) pairs.get(value)
                                .get().value;
                    }
                }
            }
        }

        if (!(lhs != null && lhs.equals(newLhs))) {
            return new Condition(newLhs, op, values, condition.shouldNegate);
        }
        return condition;
    }

    /**
     * Replace if necessary
     *
     * @param vi a ValueItem
     * @return a ValueItem
     */
    public ValueItem apply(final ValueItem vi) {
        if (vi instanceof StringPrimitive) {
            final String s = ((StringPrimitive) vi).value;
            final Map<ReferenceType, Vector<String>> groupedByType = getReferenceGroups(s);

            // Process the OBJECT_INDEX_REF entries
            ValueItem result = groupedByType.get(ReferenceType.OBJECT_INDEX_REF)
                    .map(this::indexToReferencedObject)
                    .map(replaceAllObjectIndexRefsInValueItem(vi))
                    .getOrElse(vi);

            // Process simple String references, e.g. %val% etc.
            result = groupedByType.get(ReferenceType.SIMPLE_REF)
                    .map(this::keyToReferencedObject)
                    .map(replaceAllSimpleRefsInValueItem(result))
                    .getOrElse(result);

            // Process complex references
            result = groupedByType.get(ReferenceType.COMPLEX_REF)
                    .map(refList -> refList.map(this::complexRefToValueItem))
                    .map(x -> x.get(0))
                    .getOrElse(result);

            return result;
        }
        return vi;
    }

    private ValueItem complexRefToValueItem(final String ref) {
        final String[] refList = ref.split("\\.");
        Vector<Tuple3<String, String, Option<Pair>>> referencedObject = keyToReferencedObject(Vector.of(refList[0]));

        final Vector<ValueItem> valueItems = referencedObject.flatMap(t -> {
            if (t._3.isDefined()) {
                return t._3;
            }
            if (StringUtils.isNumeric(t._2)) {
                return Option.of((ValueItem) objectIndex.arrayItems.get(Integer.parseInt(t._2)));
            } else {
                return Option.of(new StringPrimitive(t._2));
            }
        })
                .map(pair -> followNestedRef(pair, refList, 1));

        if (valueItems.size() > 0) {
            // TODO
            return valueItems.get(0);
        } else {
            throw new InterpreterError("UNHANDLED CODE PATH");
        }
    }

    private ValueItem followNestedRef(final ValueItem vi, final String[] refList, final int refIndex) {
        if (refIndex == refList.length) {
            return vi;
        }

        final String ref = refList[refIndex];
        if (StringUtils.isNumeric(ref)) {
            final int refNum = Integer.parseInt(ref);
            if (vi instanceof Array) {
                final Array arr = (Array) vi;
                final ValueItem valueItem = (ValueItem) arr.arrayItems.get(refNum);

                return followNestedRef(valueItem, refList, refIndex + 1);

            } else if (vi instanceof Pair && ((Pair) vi).value instanceof Array) {
                final ValueItem valueItem = (ValueItem) ((Array) ((Pair) vi).value).arrayItems.get(refNum);
                return followNestedRef(valueItem, refList, refIndex + 1);
            }
        } else {
            // If we have a Map then try to get a Pair from within it and recurse.
            if (vi instanceof uk.modl.model.Map) {
                final Option<MapItem> matchingMapItem = ((uk.modl.model.Map) vi).mapItems.find(mapItem -> {
                    // Check whether the reference key needs de-referencing
                    if (ref.contains("%")) {
                        String actualRef = pairs.get(stripLeadingAndTrailingPercents(ref))
                                .map(pair -> pair.value.toString())
                                .getOrElse(ref);

                        // Now try to de-reference this value as well
                        /*
                        actualRef = pairs.get(actualRef)
                                .map(p -> p.value.toString())
                                .getOrElse(actualRef);

                         */

                        return mapItem instanceof Pair && ((Pair) mapItem).key.equals(actualRef);
                    } else {
                        return mapItem instanceof Pair && ((Pair) mapItem).key.equals(ref);
                    }

                });
                if (matchingMapItem.isDefined()) {
                    return followNestedRef((ValueItem) matchingMapItem.get(), refList, refIndex);
                } else {
                    throw new InterpreterError("No entry '" + ref + "' in Map '" + vi + "'");
                }
            }
            // If we have a Pair then take the pair value and recurse if possible
            final int skipRefIndexesForPathElementsWithReferences = (ref.contains("%")) ? refIndex + 1 : refIndex;
            if (vi instanceof Pair) {
                final PairValue value = ((Pair) vi).value;

                if (!(value instanceof Primitive)) {
                    if (((Pair) vi).key.equals(ref)) {
                        return followNestedRef((ValueItem) value, refList, refIndex + 1);
                    } else {
                        // Use the current refIndex since we haven't yet consumed it.
                        return followNestedRef((ValueItem) value, refList, refIndex);
                    }
                }
                if (((Pair) vi).key.equals(ref)) {
                    return (ValueItem) value;
                }
                // Handle methods and trailing values
                final String valueStr = handleMethodsAndTrailingPathComponents(refList, skipRefIndexesForPathElementsWithReferences, value.toString());
                return new StringPrimitive(valueStr);
            }
            if (vi instanceof StringPrimitive) {
                // Handle methods and trailing values
                final String valueStr = handleMethodsAndTrailingPathComponents(refList, skipRefIndexesForPathElementsWithReferences, vi.toString());
                return new StringPrimitive(valueStr);
            }
        }
        return vi;
    }

    private String handleMethodsAndTrailingPathComponents(final String[] refList, final int refIndex, String valueStr) {
        for (int i = refIndex; i < refList.length; i++) {
            final String pathComponent = refList[i];
            if (pathComponent.length() == 1) {
                switch (pathComponent) {
                    case "p":
                        valueStr = replacePunycode(Util.unquote(valueStr));
                        break;
                    case "u":
                        valueStr = Util.unquote(valueStr)
                                .toUpperCase();
                        break;
                    case "d":
                        valueStr = Util.unquote(valueStr)
                                .toLowerCase();
                        break;
                    case "i":
                        valueStr = WordUtils.capitalize(Util.unquote(valueStr));
                        break;
                    case "s":
                        valueStr = StringUtils.capitalize(Util.unquote(valueStr));
                        break;
                    case "e":
                        try {
                            valueStr = URLEncoder.encode(valueStr, StandardCharsets.UTF_8.toString());
                        } catch (final Exception e) {
                            throw new InterpreterError("Error processing URL encoding instruction: " + e.getMessage());
                        }
                        break;
                    default:
                        break;
                }
            } else if (pathComponent.startsWith("r<")) {
                valueStr = Util.replacer(pathComponent, valueStr);
            } else if (pathComponent.startsWith("t<")) {
                valueStr = Util.trimmer(pathComponent, valueStr);
            } else {
                valueStr += "." + pathComponent;
            }
        }
        return valueStr;
    }

    private String replacePunycode(final String s) {
        // Prefix it with xn-- (the letters xn and two dashes) and decode using punycode / IDN library. Replace the full part (including graves) with the decoded value.
        if (s == null) {
            return null;
        }

        return IDN.toUnicode("xn--" + s);
    }

    /**
     * Replace a pair with a new value that has its references resolved.
     *
     * @param p a Pair with references
     * @return a Pair with the references resolved
     */
    public Pair apply(final Pair p) {
        if (p == null) {
            return null;
        }

        if (p.value instanceof ValueConditional) {
            return p;
        } else {
            final Pair resolved = resolve(p);
            accept(resolved);

            return pairs.get(p.key)
                    .getOrElse(p);
        }
    }

    /**
     * Update the pairKeysWithReferences to new values with the references replaced by actual values
     */
    private Pair resolve(final Pair p) {
        if (p.value instanceof StringPrimitive) {
            final Map<ReferenceType, Vector<String>> groupedByType = getReferenceGroups(p.value.toString());

            // Process the OBJECT_INDEX_REF entries
            Pair result = groupedByType.get(ReferenceType.OBJECT_INDEX_REF)
                    .map(this::indexToReferencedObject)
                    .map(replaceAllObjectIndexRefs(p))
                    .getOrElse(p);

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

            // Process complex references
            final Pair finalResult = result;
            result = groupedByType.get(ReferenceType.COMPLEX_REF)
                    .map(refList -> complexRefToReferencedItems(finalResult, refList))
                    .map(replaceAllSimpleRefs(result))
                    .getOrElse(result);

            return result;
        }
        return p;
    }

    private Vector<Tuple3<String, String, Option<Pair>>> complexRefToReferencedItems(final Pair p, final Vector<String> refList) {
        return refList.map(ref -> Tuple.of(ref, p.key, Option.of(new Pair(p.key, complexRefToValueItem(ref)))));
    }

    private Function<Array, Pair> replaceInstructionReference(final Pair p) {
        return items -> new Pair(p.key, items);
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
            final Pair p = new Pair("assign", new Array(ci.assign));
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
     * @param p a Pair with references
     * @return a function to convert the Pair in the supplied tuple to a Pair with references replaced
     */
    private Function<Vector<Tuple4<String, String, Integer, Option<ArrayItem>>>, Pair> replaceAllObjectIndexRefs(final Pair p) {
        return refTuples -> refTuples.foldLeft(p, replaceObjectIndexRefInArrayItem());
    }

    /**
     * Return a function to fold a list of replacement values into a value with references, i.e. replace references with their actual values.
     *
     * @param vi a ValueItem with references
     * @return a function to convert the String to a String with references replaced
     */
    private Function<Vector<Tuple4<String, String, Integer, Option<ArrayItem>>>, ValueItem> replaceAllObjectIndexRefsInValueItem(final ValueItem vi) {
        return refTuples -> refTuples.foldLeft(vi, replaceObjectIndexRefInValueItem());
    }

    /**
     * Return a function to fold a list of replacement values into a value with references, i.e. replace references with their actual values.
     *
     * @param p a Pair with references
     * @return a function to convert the Pair in the supplied tuple to a Pair with references replaced
     */
    private Function<Vector<Tuple3<String, String, Option<Pair>>>, Pair> replaceAllSimpleRefs(final Pair p) {
        return refTuples -> refTuples.foldLeft(p, replaceSimpleRefInPair());
    }

    /**
     * Return a function to fold a list of replacement values into a value with references, i.e. replace references with their actual values.
     *
     * @param vi a ValueItem with references
     * @return a function to convert the String to a String with references replaced
     */
    private Function<Vector<Tuple3<String, String, Option<Pair>>>, ValueItem> replaceAllSimpleRefsInValueItem(final ValueItem vi) {
        return refTuples -> refTuples.foldLeft(vi, replaceSimpleRefInValueItem());
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
    private BiFunction<Pair, Tuple4<String, String, Integer, Option<ArrayItem>>, Pair> replaceObjectIndexRefInArrayItem() {
        return (curr, next) -> {
            if (next._4.isDefined()) {
                // If the item containing the reference is a StringPrimitive then do String substitution
                if (next._4.get() instanceof StringPrimitive) {
                    final String s = ((StringPrimitive) curr.value).value;
                    final String r = objectIndex.arrayItems.get(next._3)
                            .toString();
                    return new Pair(curr.key, new StringPrimitive(s.replace(next._1, r)));
                }
                // Otherwise replace the whole thing
                return new Pair(curr.key, (PairValue) objectIndex.arrayItems.get(next._3));
            }
            return curr;
        };
    }

    /**
     * Update the `curr` value by replacing the reference if it matches the index in the `next` tuple
     *
     * @return a tuple with an updated Pair
     */
    private BiFunction<ValueItem, Tuple4<String, String, Integer, Option<ArrayItem>>, ValueItem> replaceObjectIndexRefInValueItem() {
        return (curr, next) -> {
            if (next._4.isDefined()) {
                // If the item containing the reference is a StringPrimitive then do String substitution
                if (next._4.get() instanceof StringPrimitive && curr instanceof StringPrimitive) {
                    final String r = objectIndex.arrayItems.get(next._3)
                            .toString();
                    return new StringPrimitive(((StringPrimitive) curr).value.replace(next._1, r));
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
    private BiFunction<Pair, Tuple3<String, String, Option<Pair>>, Pair> replaceSimpleRefInPair() {
        return (curr, next) -> {
            if (next._3.isDefined()) {
                // If the item containing the reference is a StringPrimitive then do String substitution
                final Pair p = next._3.get();
                if (p.value instanceof StringPrimitive) {
                    final String s = ((StringPrimitive) curr.value).value;
                    final String r = ((StringPrimitive) p.value).value;
                    return new Pair(curr.key, new StringPrimitive(s.replace(next._1, r)));
                } else if (p.value instanceof NumberPrimitive) {
                    final String s = ((StringPrimitive) curr.value).value;
                    final String r = ((NumberPrimitive) p.value).value;
                    return new Pair(curr.key, new NumberPrimitive(s.replace(next._1, r)));
                }
                // Otherwise replace the whole thing
                return new Pair(curr.key, p.value);
            }
            return curr;
        };
    }

    /**
     * Update `curr` String by replacing the reference if it matches the index in the `next` tuple
     *
     * @return an updated String
     */
    private BiFunction<ValueItem, Tuple3<String, String, Option<Pair>>, ValueItem> replaceSimpleRefInValueItem() {
        return (curr, next) -> {
            if (next._3.isDefined()) {
                // If the item containing the reference is a StringPrimitive then do String substitution
                if (next._3.get().value instanceof StringPrimitive && curr instanceof StringPrimitive) {
                    final String r = ((StringPrimitive) next._3.get().value).value;
                    return new StringPrimitive(((StringPrimitive) curr).value.replace(next._1, r));
                } else {
                    return (ValueItem) next._3.get().value;
                }
            }
            return curr;
        };
    }

    public void seCtx(final TransformationContext ctx) {
        this.ctx = ctx;
    }

    private enum ReferenceType {
        INSTRUCTION_REF, COMPLEX_REF, OBJECT_INDEX_REF, SIMPLE_REF
    }
}
