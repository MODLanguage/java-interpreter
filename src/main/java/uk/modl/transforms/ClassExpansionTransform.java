/*
 * MIT License
 *
 * Copyright (c) 2020 NUM Technology Ltd
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package uk.modl.transforms;

import io.vavr.collection.HashMap;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import uk.modl.ancestry.Ancestry;
import uk.modl.ancestry.Parent;
import uk.modl.model.*;
import uk.modl.utils.SupertypeInference;
import uk.modl.utils.Util;

import java.util.Objects;

@RequiredArgsConstructor
public class ClassExpansionTransform {

    private final ExpandedClassCache cache = new ExpandedClassCache();

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param ctx TransformationContext
     * @param s   Structure
     * @return the result of function application
     */
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
        return s;
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
                    return convertPairToNull(ctx, pair, expClass);
                case "arr":
                    return convertPairToArray(ctx, pair, expClass);
                case "num":
                    return convertPairToNumber(ctx, pair, expClass);
                case "str":
                    return convertPairToString(ctx, pair, expClass);
                case "bool":
                    return convertPairToBoolean(ctx, pair, expClass);
                default:
                    throw new RuntimeException("Invalid superclass: " + supertype);
            }
        }
        return pair;
    }

    private Pair convertPairToMap(final TransformationContext ctx, final Pair pair, final ExpandedClass expClass) {
        @NonNull final PairValue pairValue = pair.getValue();

        if (pairValue instanceof Array) {
            return getPairFromArray(ctx, pair, expClass, (Array) pairValue);
        } else if (pairValue instanceof Map) {
            return getPairFromMap(ctx, pair, expClass, (Map) pairValue);
        } else if (pairValue instanceof Primitive) {
            final Pair structure = getPairFromPrimitive(ctx, pair, expClass, pairValue);
            if (structure != null) return structure;
        }
        return pair.with(ctx.getAncestry(), expClass.name, pairValue);
    }

    private Pair getPairFromPrimitive(final TransformationContext ctx, final Pair pair, final ExpandedClass expClass, final @NonNull PairValue pairValue) {
        final Ancestry ancestry = ctx.getAncestry();

        if (expClass.hasSingleValueAssign()) {
            final Structure s = expClass.toMapUsingAssign(ctx, pair, cache, Array.of(ancestry, pair, Vector.of((ArrayItem) pairValue)));

            final Structure structure = apply(ctx, s);
            if (structure instanceof ValueItem) {
                return pair.with(ancestry, expClass.name, (PairValue) structure);
            } else {
                throw new RuntimeException("Cannot store this item in a Pair: " + structure.toString());
            }
        } else if (expClass.assigns.isEmpty()) {
            final Map map = expClass.toMapFromMap(ctx, Map.of(ancestry, pair, Vector.of(pair.with(ancestry, "value", pairValue))));

            final Structure structure = apply(ctx, map);
            return pair.with(ancestry, expClass.name, (PairValue) structure);
        }
        return null;
    }

    private Pair getPairFromMap(final TransformationContext ctx, final Pair pair, final ExpandedClass expClass, final Map pairValue) {
        final Map map = expClass.toMapFromMap(ctx, pairValue);
        final Structure structure = apply(ctx, map);
        if (structure instanceof ValueItem) {
            return pair.with(ctx.getAncestry(), expClass.name, (PairValue) structure);
        } else {
            throw new RuntimeException("Cannot store this item in a Pair: " + structure.toString());
        }
    }

    private Pair getPairFromArray(final TransformationContext ctx, final Pair pair, final ExpandedClass expClass, final Array pairValue) {
        final Structure s = expClass.toMapUsingAssign(ctx, pair, cache, pairValue);

        final Structure structure = apply(ctx, s);
        if (structure instanceof ValueItem) {
            return pair.with(ctx.getAncestry(), expClass.name, (PairValue) structure);
        } else {
            throw new RuntimeException("Cannot store this item in a Pair: " + structure.toString());
        }
    }

    private Pair convertPairToNull(final TransformationContext ctx, final Pair pair, final ExpandedClass expClass) {
        return pair.with(ctx.getAncestry(), expClass.name, NullPrimitive.instance);
    }

    private Pair convertPairToArray(final TransformationContext ctx, final Pair pair, final ExpandedClass expClass) {
        @NonNull final PairValue pairValue = pair.getValue();

        if (pairValue instanceof Array) {

            Array array;
            if (expClass.assigns.nonEmpty()) {
                array = expClass.toArrayUsingAssign(ctx, pair, cache, (Array) pairValue);
            } else {
                @NonNull final Vector<ArrayItem> valuesToAssign = ((Array) pairValue).getArrayItems();

                final int assignListLength = valuesToAssign
                        .size();

                array = expClass.assigns.find(l -> l.size() == assignListLength)
                        .map(assignList -> {
                            final Array arr = Array.of(ctx.getAncestry(), pair, Vector.empty());
                            Vector<ArrayItem> items = Vector.empty();
                            for (int i = 0; i < assignListLength; i++) {
                                final String maybeWildcardKey = assignList.get(i);
                                final String key;

                                if (maybeWildcardKey.endsWith("*")) {
                                    key = StringUtils.removeEnd(maybeWildcardKey, "*");
                                } else {
                                    key = maybeWildcardKey;
                                }


                                final Pair p = Pair.of(ctx.getAncestry(), pair, key, (PairValue) valuesToAssign.get(i));
                                final Structure structure = expandToClass(ctx, p);
                                if (structure instanceof Pair) {
                                    @NonNull final PairValue value = ((Pair) structure).getValue();
                                    if (value instanceof Map) {
                                        final Map map = Map.of(ctx.getAncestry(), pair, ((Map) value).getMapItems()
                                                .appendAll(expClass.pairs));
                                        items = items.append(map);
                                    } else if (value instanceof Array) {
                                        final Option<StarClassTransform.ClassInstruction> maybeClass = ctx.getClassByNameOrId(key);
                                        if (maybeClass.isDefined()) {
                                            final StarClassTransform.ClassInstruction classs = maybeClass.get();
                                            final ExpandedClass expandedClass = cache.getExpandedClass(ctx, classs, classs.getSuperclass());

                                            final Structure expandedObject = expandedClass.toMapUsingAssign(ctx, pair, cache, (Structure) value);
                                            items = items.append((ArrayItem) expandedObject);
                                        } else {
                                            items = items.appendAll(((Array) value).getArrayItems());
                                        }
                                    }
                                }
                            }
                            return arr.with(ctx.getAncestry(), items);
                        })
                        .getOrElse(() -> (Array) pairValue);
            }

            final Structure structure = apply(ctx, array);

            if (structure instanceof ValueItem) {
                return pair.with(ctx.getAncestry(), expClass.name, (PairValue) structure);
            } else {
                throw new RuntimeException("Cannot store this item in a Pair: " + structure.toString());
            }
        } else if (pairValue instanceof Primitive) {
            return pair.with(ctx.getAncestry(), expClass.name, Array.of(ctx.getAncestry(), pair, Vector.of((ArrayItem) pairValue)));
        } else if (pairValue instanceof Map) {
            throw new RuntimeException("Cannot convert map to array: " + pairValue);
        }

        return pair.with(ctx.getAncestry(), expClass.name, pairValue);

    }

    private Pair convertPairToNumber(final TransformationContext ctx, final Pair pair, final ExpandedClass expClass) {
        try {
            if (pair.getValue() instanceof NullPrimitive) {
                throw new RuntimeException("Superclass of \"" + expClass.id + "\" is num - cannot assign value \"" + pair.getValue()
                        .toString() + "\"");
            }

            if (pair.getValue() instanceof StringPrimitive) {
                throw new RuntimeException("Superclass of \"" + expClass.id + "\" is num - cannot assign value \"" + pair.getValue()
                        .toString() + "\"");
            }

            return pair.with(ctx.getAncestry(), expClass.name, NumberPrimitive.of(ctx.getAncestry(), pair, NumberUtils.createNumber(pair.getValue()
                    .toString())
                    .toString()));
        } catch (final NumberFormatException e) {
            throw new RuntimeException("Superclass of \"" + expClass.id + "\" is num - cannot assign value \"" + pair.getValue()
                    .toString() + "\"");
        }
    }

    private Pair convertPairToString(final TransformationContext ctx, final Pair pair, final ExpandedClass expClass) {
        @NonNull final PairValue pairValue = pair.getValue();

        if (pairValue instanceof NullPrimitive) {
            throw new RuntimeException("Cannot convert null value to string.");
        }

        if (pairValue instanceof TruePrimitive) {
            return pair.with(ctx.getAncestry(), expClass.name, StringPrimitive.of(ctx.getAncestry(), pair, "true"));
        }

        if (pairValue instanceof FalsePrimitive) {
            return pair.with(ctx.getAncestry(), expClass.name, StringPrimitive.of(ctx.getAncestry(), pair, "false"));
        }

        return pair.with(ctx.getAncestry(), expClass.name, StringPrimitive.of(ctx.getAncestry(), pair, pair.getValue()
                .toString()));
    }

    private Pair convertPairToBoolean(final TransformationContext ctx, final Pair pair, final ExpandedClass expClass) {
        @NonNull final PairValue value = pair.getValue();
        if (Util.truthy(value)) {
            return pair.with(ctx.getAncestry(), expClass.name, TruePrimitive.instance);
        }
        return pair.with(ctx.getAncestry(), expClass.name, FalsePrimitive.instance);
    }

    private Array processArray(final TransformationContext ctx, final Array array) {
        return array.with(ctx.getAncestry(), array.getArrayItems()
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
        return map.with(ctx.getAncestry(), map.getMapItems()
                .map(mi -> processMapItem(ctx, mi)));
    }

    private MapItem processMapItem(final TransformationContext ctx, final MapItem mapItem) {
        if (mapItem instanceof Pair) {
            return (MapItem) processPair(ctx, (Pair) mapItem);
        }
        return mapItem;
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

        public Structure toMapUsingAssign(final TransformationContext ctx, final Parent parent, final ExpandedClassCache cache, final Structure value) {
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

                                final Array arr = Array.of(ctx.getAncestry(), parent, Vector.empty());

                                for (int i = 0; i < assignListLength; i++) {
                                    final ArrayItem item = valuesToAssign.get(i);

                                    final Structure s = expandedClass.toMapUsingAssign(ctx, arr, cache, (Structure) item);
                                    arrayItems = arrayItems.append((ArrayItem) s);
                                }
                                arrayItems = arrayItems.appendAll(pairs);
                                return arr.with(ctx.getAncestry(), arrayItems);
                            }
                        }

                        Vector<MapItem> mapItems = Vector.empty();
                        final Map map = Map.of(ctx.getAncestry(), null, mapItems);

                        for (int i = 0; i < assignListLength; i++) {

                            // Might need to expand wildcard assign lists
                            final Vector<String> keys = (assignList.size() == assignListLength) ? assignList : extendAssignList(assignListLength, assignList);

                            final String key = keys.get(i);
                            final ArrayItem item = valuesToAssign.get(i);

                            if (item instanceof Pair) {
                                mapItems = mapItems.append((MapItem) item);
                            } else if (item instanceof PairValue) {
                                mapItems = mapItems.append(Pair.of(ctx.getAncestry(), map, key, (PairValue) item));
                            } else if (item instanceof ArrayConditional) {
                                mapItems = mapItems.append(Pair.of(ctx.getAncestry(), map, key, (PairValue) ((ArrayConditional) item).getResult()
                                        .get(0)));
                            }
                        }


                        mapItems = mapItems.appendAll(pairs);
                        return map.with(ctx.getAncestry(), mapItems);
                    })
                    .getOrElseThrow(() -> new RuntimeException("No key list of the correct length in class " + id + " - looking for one of length " + assignListLength));
        }

        public Array toArrayUsingAssign(final TransformationContext ctx, final Parent parent, final ExpandedClassCache cache, final Array value) {

            final Vector<ArrayItem> valuesToAssign = value.getArrayItems();

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

                                final Array arr = Array.of(ctx.getAncestry(), parent, Vector.empty());

                                for (int i = 0; i < assignListLength; i++) {
                                    final ArrayItem item = valuesToAssign.get(i);

                                    final Structure s = expandedClass.toMapUsingAssign(ctx, arr, cache, (Structure) item);
                                    arrayItems = arrayItems.append((ArrayItem) s);
                                }
                                arrayItems = arrayItems.appendAll(pairs);
                                return arr.with(ctx.getAncestry(), arrayItems);
                            }
                        }

                        final Array arr = Array.of(ctx.getAncestry(), parent, Vector.empty());

                        for (int i = 0; i < assignListLength; i++) {
                            final Option<StarClassTransform.ClassInstruction> maybeClassByNameOrId = ctx.getClassByNameOrId(StringUtils.removeEnd(assignList.get(i), "*"));
                            if (maybeClassByNameOrId
                                    .isDefined() && maybeClassByNameOrId.get()
                                    .getAssign()
                                    .nonEmpty()) {
                                final StarClassTransform.ClassInstruction classs = maybeClassByNameOrId.get();
                                final ExpandedClass expandedClass = cache.getExpandedClass(ctx, classs, classs.getSuperclass());

                                final ArrayItem item = valuesToAssign.get(i);

                                final Structure s = expandedClass.toMapUsingAssign(ctx, arr, cache, (Structure) item);
                                arrayItems = arrayItems.append((ArrayItem) s);
                            }
                        }
                        arrayItems = arrayItems.appendAll(pairs);
                        return arr.with(ctx.getAncestry(), arrayItems);
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

        public Map toMapFromMap(final TransformationContext ctx, final Map map) {
            return map.with(ctx.getAncestry(), map.getMapItems()
                    .appendAll(pairs));
        }

        public boolean hasSingleValueAssign() {
            return assigns.count(v -> v.size() == 1) > 0;
        }

    }

}
