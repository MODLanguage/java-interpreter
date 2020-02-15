package uk.modl.transforms;

import io.vavr.Function1;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import uk.modl.model.*;
import uk.modl.parser.errors.InterpreterError;
import uk.modl.visitor.ModlVisitorBase;

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
        // TODO: currently a no-op
        final ReferencesMutator referencesMutator = new ReferencesMutator();

        return referencesMutator.apply(modl);// Replace references with actual values
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

        public Modl apply(final Modl modl) {
            modl.visit(this);
            resolve();
            return replace(modl);
        }

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

        public Modl replace(final Modl modl) {
            if (pairKeysWithReferences.size() == 0) {
                // No references so nothing to do
                return modl;
            }

            final List<Structure> list = List.ofAll(modl.structures.map(this::replace));
            return new Modl(list);
        }

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

        private Structure replace(final Pair p) {
            return pairKeysWithReferences.get(p.key)
                    .getOrElse(p);
        }

        private Structure replace(final uk.modl.model.Map map) {
            return map;
        }

        private Structure replace(final uk.modl.model.Array arr) {
            return arr;
        }

        private Structure replace(final uk.modl.model.TopLevelConditional tlc) {
            return tlc;
        }

        public void resolve() {
            pairKeysWithReferences = HashMap.ofEntries(
                    pairKeysWithReferences.map(tuple2 -> {
                        final Matcher matcher = referencePattern.matcher(tuple2._2.value.toString());

                        Tuple2<String, Pair> result = tuple2;

                        while (matcher.find()) {
                            final PairValue pairValue = result._2.value;
                            final String reference = matcher.group();
                            if (reference.startsWith("%*")) {
                                // These are handled by the InstructionTransform
                            } else if (reference.contains(".")) {
                                // TODO: process complex references
                            } else {
                                final String refKey = StringUtils.removeEnd(StringUtils.removeStart(reference, "%"), "%");

                                if (StringUtils.isNumeric(refKey)) {
                                    final int index = Integer.parseInt(refKey);
                                    if (index >= 0 && index < objectIndex.arrayItems.size()) {
                                        // Handle numeric references using the index
                                        final Pair newPair = new Pair(tuple2._1, (PairValue) objectIndex.arrayItems.get(index));
                                        result = tuple2.update2(newPair);
                                    }
                                } else {
                                    final PairValue pv = pairs.get(refKey)
                                            .map(p -> {
                                                if (pairValue instanceof StringPrimitive) {
                                                    final String newValue = ((StringPrimitive) pairValue).value.replace(reference, p.value.toString());
                                                    return new StringPrimitive(newValue);
                                                }
                                                return p.value;
                                            })
                                            .getOrElse(tuple2._2);

                                    final Pair newPair = new Pair(tuple2._1, pv);
                                    result = tuple2.update2(newPair);
                                }
                            }
                        }
                        return result;
                    }));
        }
    }
}
