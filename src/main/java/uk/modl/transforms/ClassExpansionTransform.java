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
import uk.modl.utils.SupertypeInference;
import uk.modl.utils.Util;

import java.util.Objects;

@RequiredArgsConstructor
public class ClassExpansionTransform implements Function2<TransformationContext, Structure, Structure> {

    private final ExpandedClassCache cache = new ExpandedClassCache();

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

            final ExpandedClass expClass = cache.getExpandedClass(ctx, ci, supertype);

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
                    throw new RuntimeException("Invalid superclass: " + supertype);
            }
        }
        return pair;
    }

    private Pair convertPairToMap(final TransformationContext ctx, final Pair pair, final ExpandedClass expClass) {
        @NonNull final PairValue pairValue = pair.getValue();

        if (pairValue instanceof Array) {
            return getPairFromArray(ctx, expClass, (Array) pairValue);
        } else if (pairValue instanceof Map) {
            return getPairFromMap(ctx, expClass, (Map) pairValue);
        } else if (pairValue instanceof Primitive) {
            final Pair structure = getPairFromPrimitive(ctx, expClass, pairValue);
            if (structure != null) return structure;
        }
        return pair.with(expClass.name, pairValue);
    }

    private Pair getPairFromPrimitive(final TransformationContext ctx, final ExpandedClass expClass, final @NonNull PairValue pairValue) {
        if (expClass.hasSingleValueAssign()) {
            final Structure s = expClass.toStructureUsingAssign(ctx, cache, Array.of(Vector.of((ArrayItem) pairValue)));

            final Structure structure = apply(ctx, s);
            if (structure instanceof ValueItem) {
                return Pair.of(expClass.name, (PairValue) structure);
            } else {
                throw new RuntimeException("Cannot store this item in a Pair: " + structure.toString());
            }
        } else if (expClass.assigns.isEmpty()) {
            final Map map = expClass.toMapFromMap(Map.of(Vector.of(Pair.of("value", pairValue))));

            final Structure structure = apply(ctx, map);
            return Pair.of(expClass.name, (PairValue) structure);
        }
        return null;
    }

    private Pair getPairFromMap(final TransformationContext ctx, final ExpandedClass expClass, final Map pairValue) {
        final Map map = expClass.toMapFromMap(pairValue);
        final Structure structure = apply(ctx, map);
        if (structure instanceof ValueItem) {
            return Pair.of(expClass.name, (PairValue) structure);
        } else {
            throw new RuntimeException("Cannot store this item in a Pair: " + structure.toString());
        }
    }

    private Pair getPairFromArray(final TransformationContext ctx, final ExpandedClass expClass, final Array pairValue) {
        final Structure s = expClass.toStructureUsingAssign(ctx, cache, pairValue);

        final Structure structure = apply(ctx, s);
        if (structure instanceof ValueItem) {
            return Pair.of(expClass.name, (PairValue) structure);
        } else {
            throw new RuntimeException("Cannot store this item in a Pair: " + structure.toString());
        }
    }

    private Pair convertPairToNull(final ExpandedClass expClass) {
        return Pair.of(expClass.name, NullPrimitive.instance);
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
                            final String maybeWildcardKey = assignList.get(i);
                            final String key;

                            if (maybeWildcardKey.endsWith("*")) {
                                key = StringUtils.removeEnd(maybeWildcardKey, "*");
                            } else {
                                key = maybeWildcardKey;
                            }

                            final Pair p = Pair.of(key, (PairValue) valuesToAssign.get(i));
                            final Structure structure = expandToClass(ctx, p);
                            if (structure instanceof Pair) {
                                @NonNull final PairValue value = ((Pair) structure).getValue();
                                if (value instanceof Map) {
                                    final Map map = Map.of(((Map) value).getMapItems()
                                            .appendAll(expClass.pairs));
                                    items = items.append(map);
                                } else if (value instanceof Array) {
                                    final Option<StarClassTransform.ClassInstruction> maybeClass = ctx.getClassByNameOrId(key);
                                    if (maybeClass.isDefined()) {
                                        final StarClassTransform.ClassInstruction classs = maybeClass.get();
                                        final ExpandedClass expandedClass = cache.getExpandedClass(ctx, classs, classs.getSuperclass());

                                        final Structure expandedObject = expandedClass.toStructureUsingAssign(ctx, cache, (Structure) value);
                                        items = items.append((ArrayItem) expandedObject);
                                    } else {
                                        items = items.appendAll(((Array) value).getArrayItems());
                                    }
                                }
                            }
                        }
                        return Array.of(items);
                    })
                    .getOrElse(() -> (Array) pairValue);


            final Structure structure = apply(ctx, array);
            if (structure instanceof ValueItem) {
                return Pair.of(expClass.name, (PairValue) structure);
            } else {
                throw new RuntimeException("Cannot store this item in a Pair: " + structure.toString());
            }
        } else if (pairValue instanceof Primitive) {
            return Pair.of(expClass.name, Array.of(Vector.of((ArrayItem) pairValue)));
        } else if (pairValue instanceof Map) {
            throw new RuntimeException("Cannot convert map to array: " + pairValue);
        }

        return Pair.of(expClass.name, pairValue);

    }

    private Pair convertPairToNumber(final Pair pair, final ExpandedClass expClass) {
        try {
            if (pair.getValue() instanceof NullPrimitive) {
                throw new RuntimeException("Superclass of \"" + expClass.id + "\" is num - cannot assign value \"" + pair.getValue()
                        .toString() + "\"");
            }

            if (pair.getValue() instanceof StringPrimitive) {
                throw new RuntimeException("Superclass of \"" + expClass.id + "\" is num - cannot assign value \"" + pair.getValue()
                        .toString() + "\"");
            }

            return pair.with(expClass.name, NumberPrimitive.of(NumberUtils.createNumber(pair.getValue()
                    .toString())
                    .toString()));
        } catch (final NumberFormatException e) {
            throw new RuntimeException("Superclass of \"" + expClass.id + "\" is num - cannot assign value \"" + pair.getValue()
                    .toString() + "\"");
        }
    }

    private Pair convertPairToString(final Pair pair, final ExpandedClass expClass) {
        @NonNull final PairValue pairValue = pair.getValue();

        if (pairValue instanceof NullPrimitive) {
            throw new RuntimeException("Cannot convert null value to string.");
        }

        if (pairValue instanceof TruePrimitive) {
            return pair.with(expClass.name, StringPrimitive.of("true"));
        }

        if (pairValue instanceof FalsePrimitive) {
            return pair.with(expClass.name, StringPrimitive.of("false"));
        }

        return pair.with(expClass.name, StringPrimitive.of(pair.getValue()
                .toString()));
    }

    private Pair convertPairToBoolean(final Pair pair, final ExpandedClass expClass) {
        @NonNull final PairValue value = pair.getValue();
        if (Util.truthy(value)) {
            return pair.with(expClass.name, TruePrimitive.instance);
        }
        return pair.with(expClass.name, FalsePrimitive.instance);
    }

    private Array processArray(final TransformationContext ctx, final Array array) {
        return array.with(array.getArrayItems()
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
        return map.with(map.getMapItems()
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

    private static class ExpandedClassCache {

        private final io.vavr.collection.Map<String, ExpandedClass> cache = HashMap.empty();

        public ExpandedClass getExpandedClass(final TransformationContext ctx, final StarClassTransform.ClassInstruction ci, final String supertype) {
            final ExpandedClass expClass;
            final Option<ExpandedClass> maybeExpandedClass = cache.get(ci.getId());
            if (maybeExpandedClass.isDefined()) {
                expClass = maybeExpandedClass.get();
            } else {
                expClass = ExpandedClass.of(ctx, ci, supertype);
                cache.put(ci.getId(), expClass);
            }
            return expClass;
        }

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
            final io.vavr.collection.Map<String, Pair> pairs = getAllPairs(ctx, ci);

            return new ExpandedClass(id, name, supertype, assigns, pairs.values()
                    .toVector());
        }

        private static io.vavr.collection.Map<String, Pair> getAllPairs(final TransformationContext ctx, final StarClassTransform.ClassInstruction ci) {
            final String superclass = ci.getSuperclass();
            final io.vavr.collection.Map<String, Pair> pairs = ci.getPairs();

            // Append pairs from superclasses if they are not already in the set of pairs. I.e. Pairs from superclasses do not override pairs from subclasses.

            final io.vavr.collection.Map<String, Pair> pairsFromSuperclasses = ctx.getClassByNameOrId(superclass)
                    .map(superCi -> getAllPairs(ctx, superCi))
                    .getOrElse(HashMap.empty());

            return pairs.merge(pairsFromSuperclasses);
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

        public Structure toStructureUsingAssign(final TransformationContext ctx, final ExpandedClassCache cache, final Structure value) {
            Vector<ArrayItem> tmpValuesToAssign = Vector.empty();
            if (value instanceof Array) {
                tmpValuesToAssign = ((Array) value).getArrayItems();
                if (assigns.isEmpty()) {
                    final Vector<String> strings = tmpValuesToAssign.map(Object::toString)
                            .intersperse(", ");
                    final String arrayStr = strings.foldLeft("[", (l, r) -> l + r) + "]";
                    throw new RuntimeException("Cannot convert array to map: " + arrayStr);
                }
            } else if (value instanceof Map) {
                tmpValuesToAssign = ((Map) value).getMapItems()
                        .map(mi -> (ArrayItem) mi);
            }

            final Vector<ArrayItem> valuesToAssign = tmpValuesToAssign;

            final int assignListLength = valuesToAssign
                    .size();

            // If there's a value ending with * in the list then we have a match
            return assigns.find(l -> l.size() == assignListLength || l.count(s -> s.endsWith("*")) > 0)
                    .map(assignList -> {
                        Vector<ArrayItem> arrayItems = Vector.empty();


                        if (assignList.length() == 1 && assignList.get(0)
                                .endsWith("*")) {
                            final Option<StarClassTransform.ClassInstruction> maybeClassByNameOrId = ctx.getClassByNameOrId(StringUtils.removeEnd(assignList.get(0), "*"));

                            if (maybeClassByNameOrId
                                    .isDefined() && maybeClassByNameOrId.get()
                                    .getAssign()
                                    .nonEmpty()) {
                                final StarClassTransform.ClassInstruction classs = maybeClassByNameOrId.get();
                                final ExpandedClass expandedClass = cache.getExpandedClass(ctx, classs, classs.getSuperclass());

                                for (int i = 0; i < assignListLength; i++) {
                                    final ArrayItem item = valuesToAssign.get(i);

                                    final Structure s = expandedClass.toStructureUsingAssign(ctx, cache, (Structure) item);
                                    arrayItems = arrayItems.append((ArrayItem) s);
                                }
                                arrayItems = arrayItems.appendAll(pairs);
                                return Array.of(arrayItems);
                            }
                        }

                        Vector<MapItem> mapItems = Vector.empty();

                        for (int i = 0; i < assignListLength; i++) {

                            // Might need to expand wildcard assign lists
                            final Vector<String> keys = (assignList.size() == assignListLength) ? assignList : extendAssignList(assignListLength, assignList);

                            final String key = keys.get(i);
                            final ArrayItem item = valuesToAssign.get(i);

                            if (item instanceof Pair) {
                                mapItems = mapItems.append((MapItem) item);
                            } else if (item instanceof PairValue) {
                                mapItems = mapItems.append(Pair.of(key, (PairValue) item));
                            } else if (item instanceof ArrayConditional) {
                                mapItems = mapItems.append(Pair.of(key, (PairValue) ((ArrayConditional) item).getResult()
                                        .get(0)));
                            }
                        }


                        mapItems = mapItems.appendAll(pairs);
                        return Map.of(mapItems);
                    })
                    .getOrElseThrow(() -> new RuntimeException("No key list of the correct length in class " + id + " - looking for one of length " + assignListLength));
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

        public Map toMapFromMap(final Map map) {
            return map.with(map.getMapItems()
                    .appendAll(pairs));
        }

        public boolean hasSingleValueAssign() {
            return assigns.count(v -> v.size() == 1) > 0;
        }

    }

}
