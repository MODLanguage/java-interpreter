package uk.modl.transforms;

import io.vavr.Function2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import uk.modl.model.*;
import uk.modl.parser.errors.InterpreterError;
import uk.modl.utils.SupertypeInference;
import uk.modl.utils.Util;

import java.util.Objects;

@RequiredArgsConstructor
public class ClassExpansionTransform implements Function2<TransformationContext, Structure, Structure> {

    private final io.vavr.collection.Map<String, ExpandedClass> cache = HashMap.empty();

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param s argument 1
     * @return the result of function application
     */
    @Override
    public Structure apply(final TransformationContext ctx, final Structure s) {
        if (s instanceof Map) {
            return processMap(ctx, (Map) s);
        }
        if (s instanceof Array) {
            return processArray(ctx, (Array) s);
        }
        if (s instanceof Pair) {
            return processPair(ctx, (Pair) s);
        }
        if (s instanceof TopLevelConditional) {
            return processTopLevelConditional((TopLevelConditional) s);
        }
        return s;
    }

    private TopLevelConditional processTopLevelConditional(final TopLevelConditional topLevelConditional) {
        return topLevelConditional;
    }

    private Structure processPair(final TransformationContext ctx, final Pair pair) {
        return expandToClass(ctx, pair);
    }

    private Structure expandToClass(final TransformationContext ctx, final Pair pair) {
        final Option<StarClassTransform.ClassInstruction> maybeCi = ctx.getClassByNameOrId(pair.getKey());
        if (maybeCi.isDefined()) {
            final StarClassTransform.ClassInstruction ci = maybeCi.get();

            final String supertype = SupertypeInference.inferType(ctx, ci, pair.getValue());

            final ExpandedClass expClass;
            final Option<ExpandedClass> maybeExpandedClass = cache.get(ci.getId());
            if (maybeExpandedClass.isDefined()) {
                expClass = maybeExpandedClass.get();
            } else {
                expClass = ExpandedClass.of(ctx, ci, supertype);
                cache.put(ci.getId(), expClass);
            }


            switch (supertype) {
                case "map":
                    return convertPairToMap(ctx, pair, expClass);
                case "null":
                    return convertPairToNull(expClass);
                case "arr":
                    return convertPairToArray(ctx, pair, expClass);
                case "num":
                    return convertPairToNumber(pair, expClass);
                case "str":
                    return convertPairToString(pair, expClass);
                case "bool":
                    return convertPairToBoolean(pair, expClass);
                default:
                    throw new InterpreterError("Unknown Supertype: " + supertype);
            }
        }
        return pair;
    }

    private Pair convertPairToMap(final TransformationContext ctx, final Pair pair, final ExpandedClass expClass) {
        @NonNull final PairValue pairValue = pair.getValue();

        if (pairValue instanceof Array) {
            final Map map = expClass.toMapUsingAssign((Array) pairValue);

            final Structure structure = apply(ctx, map);
            if (structure instanceof ValueItem) {
                return new Pair(expClass.name, (PairValue) structure);
            } else {
                throw new InterpreterError("Cannot store this item in a Pair: " + structure.toString());
            }
        } else if (pairValue instanceof Map) {
            final Map map = expClass.toMapFromMap((Map) pairValue);
            final Structure structure = apply(ctx, map);
            if (structure instanceof ValueItem) {
                return new Pair(expClass.name, (PairValue) structure);
            } else {
                throw new InterpreterError("Cannot store this item in a Pair: " + structure.toString());
            }
        } else if (pairValue instanceof Primitive) {
            if (expClass.hasSingleValueAssign()) {
                final Map map = expClass.toMapUsingAssign(new Array(Vector.of((ArrayItem) pairValue)));

                final Structure structure = apply(ctx, map);
                if (structure instanceof ValueItem) {
                    return new Pair(expClass.name, (PairValue) structure);
                } else {
                    throw new InterpreterError("Cannot store this item in a Pair: " + structure.toString());
                }
            } else if (expClass.assigns.isEmpty()) {
                final Map map = expClass.toMapFromMap(new Map(Vector.of(new Pair("value", pairValue))));

                final Structure structure = apply(ctx, map);
                return new Pair(expClass.name, (PairValue) structure);
            }
        }
        return new Pair(expClass.name, pairValue);
    }

    private Pair convertPairToNull(final ExpandedClass expClass) {
        return new Pair(expClass.name, NullPrimitive.instance);
    }

    private Pair convertPairToArray(final TransformationContext ctx, final Pair pair, final ExpandedClass expClass) {
        @NonNull final PairValue pairValue = pair.getValue();

        if (pairValue instanceof Array) {

            @NonNull final Vector<ArrayItem> valuesToAssign = ((Array) pairValue).getArrayItems();

            final int assignListLength = valuesToAssign
                    .size();

            final Array array = expClass.assigns.find(l -> l.size() == assignListLength)
                    .map(assignList -> {
                        Vector<ArrayItem> items = Vector.empty();
                        for (int i = 0; i < assignListLength; i++) {
                            final Pair p = new Pair(assignList.get(i), (PairValue) valuesToAssign.get(i));
                            final Structure structure = expandToClass(ctx, p);
                            if (structure instanceof Pair) {
                                @NonNull final PairValue value = ((Pair) structure).getValue();
                                if (value instanceof Map) {
                                    final Map map = new Map(((Map) value).getMapItems()
                                            .appendAll(expClass.pairs));
                                    items = items.append(map);
                                }
                            }
                        }
                        return new Array(items);
                    })
                    .getOrElse(() -> (Array) pairValue);


            final Structure structure = apply(ctx, array);
            if (structure instanceof ValueItem) {
                return new Pair(expClass.name, (PairValue) structure);
            } else {
                throw new InterpreterError("Cannot store this item in a Pair: " + structure.toString());
            }
        } else if (pairValue instanceof Primitive) {
            return new Pair(expClass.name, new Array(Vector.of((ArrayItem) pairValue)));
        }

        return new Pair(expClass.name, pairValue);

    }

    private Pair convertPairToNumber(final Pair pair, final ExpandedClass expClass) {
        return new Pair(expClass.name, new NumberPrimitive(NumberUtils.createNumber(pair.getValue()
                .toString())
                .toString()));
    }

    private Pair convertPairToString(final Pair pair, final ExpandedClass expClass) {
        @NonNull final PairValue pairValue = pair.getValue();

        if (pairValue instanceof TruePrimitive) {
            return new Pair(expClass.name, new StringPrimitive("true"));
        }

        if (pairValue instanceof FalsePrimitive) {
            return new Pair(expClass.name, new StringPrimitive("false"));
        }

        return new Pair(expClass.name, new StringPrimitive(pair.getValue()
                .toString()));
    }

    private Pair convertPairToBoolean(final Pair pair, final ExpandedClass expClass) {
        @NonNull final PairValue value = pair.getValue();
        if (Util.truthy(value)) {
            return new Pair(expClass.name, TruePrimitive.instance);
        }
        return new Pair(expClass.name, FalsePrimitive.instance);
    }

    private Array processArray(final TransformationContext ctx, final Array array) {
        return new Array(array.getArrayItems()
                .map(ai -> processArrayItem(ctx, ai)));
    }

    private ArrayItem processArrayItem(final TransformationContext ctx, final ArrayItem arrayItem) {
        if (arrayItem instanceof Map) {
            return processMap(ctx, (Map) arrayItem);
        }
        if (arrayItem instanceof Pair) {
            return (ArrayItem) processPair(ctx, (Pair) arrayItem);
        }
        if (arrayItem instanceof Array) {
            return processArray(ctx, (Array) arrayItem);
        }
        if (arrayItem instanceof Primitive) {
            return processPrimitive((Primitive) arrayItem);
        }
        return arrayItem;
    }

    private Primitive processPrimitive(final Primitive primitive) {
        return primitive;
    }

    private Map processMap(final TransformationContext ctx, final Map map) {
        return new Map(map.getMapItems()
                .map(mi -> processMapItem(ctx, mi)));
    }

    private MapItem processMapItem(final TransformationContext ctx, final MapItem mapItem) {
        if (mapItem instanceof Pair) {
            return (MapItem) processPair(ctx, (Pair) mapItem);
        }
        if (mapItem instanceof MapConditional) {
            return processMapConditional((MapConditional) mapItem);
        }
        return mapItem;
    }

    private MapConditional processMapConditional(final MapConditional mapConditional) {
        return mapConditional;
    }

    /**
     * Class that includes all inherited assigns, pairs, supertype, and defaults the name to the id if not present
     */
    @ToString
    public static class ExpandedClass {


        private final String id;

        private final String name;

        private final String superclass;

        private final Vector<Vector<String>> assigns;

        private final Vector<Pair> pairs;

        public ExpandedClass(final String id, final String name, final String superclass, final Vector<Vector<String>> assigns, final Vector<Pair> pairs) {
            this.id = id;
            this.name = name;
            this.superclass = superclass;
            this.assigns = assigns;
            this.pairs = pairs;
        }

        public static ExpandedClass of(final TransformationContext ctx, final StarClassTransform.ClassInstruction ci, final String supertype) {

            final String id = ci.getId();

            final String name = ci.getNameOrId();
            final Vector<Vector<String>> assigns = getAllAssigns(ctx, ci);
            final Vector<Pair> pairs = getAllPairs(ctx, ci);

            return new ExpandedClass(id, name, supertype, assigns, pairs);
        }

        private static Vector<Pair> getAllPairs(final TransformationContext ctx, final StarClassTransform.ClassInstruction ci) {
            final String superclass = ci.getSuperclass();
            final Vector<Pair> pairs = ci.getPairs();
            return pairs.appendAll(ctx.getClassByNameOrId(superclass)
                    .map(superCi -> getAllPairs(ctx, superCi))
                    .getOrElse(Vector.empty()));
        }

        private static Vector<Vector<String>> getAllAssigns(final TransformationContext ctx, final StarClassTransform.ClassInstruction ci) {

            // Map to strings - they are keys for Pair objects
            final Vector<Vector<String>> assign = ci.getAssign()
                    .map(vectorOfArrayItems -> ((Array) vectorOfArrayItems).getArrayItems()
                            .map(Objects::toString));

            final String superclass = ci.getSuperclass();

            return assign.appendAll(ctx.getClassByNameOrId(superclass)
                    .map(superCi -> getAllAssigns(ctx, superCi))
                    .getOrElse(Vector.empty()));
        }

        public Map toMapUsingAssign(final Array value) {
            @NonNull final Vector<ArrayItem> valuesToAssign = value.getArrayItems();

            final int assignListLength = valuesToAssign
                    .size();

            // If there's a value ending with * in the list then we have a match
            return assigns.find(l -> l.size() == assignListLength || l.count(s -> s.endsWith("*")) > 0)
                    .map(assignList -> {
                        Vector<MapItem> items = Vector.empty();
                        for (int i = 0; i < assignListLength; i++) {

                            // Might need to expand wildcard assign lists
                            final Vector<String> keys = (assignList.size() == assignListLength) ? assignList : extendAssignList(assignListLength, assignList);

                            final String key = keys.get(i);
                            final ArrayItem item = valuesToAssign.get(i);
                            if (item instanceof PairValue) {
                                items = items.append(new Pair(key, (PairValue) item));
                            } else if (item instanceof ArrayConditional) {
                                items = items.append(new Pair(key, (PairValue) ((ArrayConditional) item).getResult()
                                        .get(0)));
                            }
                        }
                        items = items.appendAll(pairs);
                        return new Map(items);
                    })
                    .getOrElseThrow(() -> new InterpreterError("No assign list of length " + assignListLength + " in " + assigns));
        }

        private Vector<String> extendAssignList(final int len, final Vector<String> list) {

            // Only one of the keys should be a wildcard, so continuing on the assumption that it is true
            return list.find(s -> s.endsWith("*"))
                    .map(wild -> StringUtils.removeEnd(wild, "*"))
                    .map(s -> {
                        final int numberOfNonWildcardKeys = list.size() - 1;
                        final int numberOfAdditionalKeys = len - numberOfNonWildcardKeys;

                        Vector<String> result = Vector.empty();
                        for (int i = 0; i < list.size(); i++) {
                            final String key = list.get(i);
                            if (key.endsWith("*")) {
                                result = result.appendAll(Vector.fill(numberOfAdditionalKeys, s));
                            } else {
                                result = result.append(key);
                            }
                        }
                        return result;
                    })
                    .getOrElse(list);
        }

        public Map toMapFromMap(final Map pairValue) {
            return new Map(pairValue.getMapItems()
                    .appendAll(pairs));
        }

        public boolean hasSingleValueAssign() {
            return assigns.count(v -> v.size() == 1) > 0;
        }

    }

}
