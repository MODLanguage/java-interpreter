package uk.modl.transforms;

import io.vavr.*;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import uk.modl.model.*;
import uk.modl.parser.errors.InterpreterError;
import uk.modl.visitor.ModlVisitorBase;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReferencesTransform implements Function1<Modl, Modl> {
    /**
     * Applies this function to one argument and returns the result.
     *
     * @param modl argument 1
     * @return the result of function application
     */
    @Override
    public Modl apply(final Modl modl) {
        return new ReferencesMutator().apply(modl);// Replace references with actual values
    }

    private enum ReferenceType {
        INSTRUCTION_REF, COMPLEX_REF, OBJECT_INDEX_REF, SIMPLE_REF
    }

    /**
     * Build a new copy of the Modl object with some pairs replaced
     */
    @RequiredArgsConstructor
    private static class ReferencesMutator extends ModlVisitorBase {
        private static Pattern referencePattern = Pattern.compile("((%\\w+)(\\.\\w*<`?\\w*`?,`\\w*`>)+|(%` ?[\\w-]+`[\\w.<>,]*%?)|(%\\*?[\\w]+(\\.%?\\w*<?[\\w,]*>?)*%?))");
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
         * Mutate a Modl object into a new Modl object by replacing references
         *
         * @param modl the current Modl object
         * @return an updated copy of the supplied Modl object
         */
        public Modl apply(final Modl modl) {
            modl.visit(this);
            resolve();
            return replace(modl);
        }

        /**
         * Capture pairs as potential targets of references, the Object Index if there is one, and pairs that contain
         * references since we'll need to process those.
         *
         * @param pair a Pair
         */
        @Override
        public void accept(final Pair pair) {
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
        }

        /**
         * Replace references in a Modl object
         *
         * @param modl the Modl object
         * @return the updated copy of the Modl object
         */
        public Modl replace(final Modl modl) {
            if (pairKeysWithReferences.size() == 0) {
                // No references so nothing to do
                return modl;
            }

            final List<Structure> list = List.ofAll(modl.structures.map(this::replace));
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
            final List<MapItem> mapItems = List.ofAll(map.mapItems.map(mi -> {
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
            final List<ArrayItem> arrayItems = List.ofAll(arr.arrayItems.map(ai -> {
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

        private ArrayConditional replace(final ArrayConditional conditional) {
            // TODO
            conditional.tests.map(test -> {
                test.conditions.map(cond -> {
                    if (cond._1 instanceof Condition) {
                        final String lhs = ((Condition) cond._1).lhs;// TODO
                        final String newLhs = replace(lhs);
                        final List<ValueItem> values = ((Condition) cond._1).values;// TODO
                    } else if (cond._1 instanceof ConditionGroup) {

                    }
                });
            });// TODO

            conditional.returns.map(ret -> {

            });// TODO

            // TODO return new Conditional if there are changes
            return conditional;
        }

        private String replace(final String s) {
            // TODO
            return s;
        }

        private Structure replace(final uk.modl.model.TopLevelConditional tlc) {
            // TODO
            return tlc;
        }

        private MapConditional replace(final MapConditional mi) {
            // TODO
            return mi;
        }

        /**
         * Replace a pair with a new value that has its references resolved.
         *
         * @param p a Pair with references
         * @return a Pair with the references resolved
         */
        private Pair replace(final Pair p) {
            return pairKeysWithReferences.get(p.key)
                    .getOrElse(p);
        }

        /**
         * Update the pairKeysWithReferences to new values with the references replaced by actual values
         */
        public void resolve() {
            pairKeysWithReferences = HashMap.ofEntries(
                    pairKeysWithReferences.map(tuple2 -> {
                        final String stringWithRefs = tuple2._2.value.toString();

                        final Matcher matcher = referencePattern.matcher(stringWithRefs);

                        // Gather the match groups into a list of references
                        List<String> groups = List.empty();
                        while (matcher.find()) {
                            groups = groups.append(matcher.group());
                        }

                        // Partition the references by reference type
                        final Map<ReferenceType, List<String>> groupedByType = groups.groupBy(ReferencesMutator::refToRefType);

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


                        // TODO: Handle %* references
                        // TODO: process complex references

                        return result;
                    }));
        }

        /**
         * Return a function to fold a list of replacement values into a value with references, i.e. replace references with their actual values.
         *
         * @param tuple2 a Tuple2 of a String key and a Pair with references
         * @return a function to convert the Pair in the supplied tuple to a Pair with references replaced
         */
        private Function<List<Tuple4<String, String, Integer, Option<ArrayItem>>>, Tuple2<String, Pair>> replaceAllObjectIndexRefs(final Tuple2<String, Pair> tuple2) {
            return refTuples -> refTuples.foldLeft(tuple2, replaceObjectIndexRefInArrayItem());
        }

        /**
         * Return a function to fold a list of replacement values into a value with references, i.e. replace references with their actual values.
         *
         * @param tuple2 a Tuple2 of a String key and a Pair with references
         * @return a function to convert the Pair in the supplied tuple to a Pair with references replaced
         */
        private Function<List<Tuple3<String, String, Option<Pair>>>, Tuple2<String, Pair>> replaceAllSimpleRefs(final Tuple2<String, Pair> tuple2) {
            return refTuples -> refTuples.foldLeft(tuple2, replaceSimpleRefInPair());
        }

        /**
         * Map an index to the object that it references
         *
         * @param list a List of String indexes of the form `%1%` or `%1`
         * @return a tuple of the original reference, %-stripped reference, integer value, and possible new value, e.g. ("%1%","1", 1, Option(ArrayItem))
         */
        private List<Tuple4<String, String, Integer, Option<ArrayItem>>> indexToReferencedObject(final List<String> list) {
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
         * @param list a List of String indexes of the form `%var%` or `%var`
         * @return a tuple of the original reference, %-stripped reference, and possible new value, e.g. ("%var%","var", Option(Pair))
         */
        private List<Tuple3<String, String, Option<Pair>>> keyToReferencedObject(final List<String> list) {
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
    }
}
