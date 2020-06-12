package uk.modl.transforms;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.Tuple4;
import io.vavr.collection.Map;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import uk.modl.ancestry.Child;
import uk.modl.model.*;
import uk.modl.parser.errors.DeepReferenceException;
import uk.modl.utils.Util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReferencesTransform {

    private static final Pattern referencePattern = Pattern.compile("(((\\\\%|~%|%)\\w+)(\\.\\w*<`?\\w*`?,`\\w*`>)+|((\\\\%|~%|%)` ?[\\w-]+`[\\w.<>,]*%?)|((\\\\%|~%|%)\\*?[\\w]+(\\.%?\\w*<?[\\w,]*>?)*%?)|(%`[ %\\w-]+`(\\.\\w+)+)|(%`.+`))");


    private final MethodsTransform methodsTransform;

    public ReferencesTransform() {
        this.methodsTransform = new MethodsTransform();
    }

    /**
     * Determine the ReferenceType for a reference String
     *
     * @return the ReferenceType
     */
    private static ReferenceType refToRefType(final String s) {
        final String refKey = stripLeadingAndTrailingPercents(s);
        if (s.contains(".")) {
            return ReferenceType.COMPLEX_REF;
        } else if (StringUtils.isNumeric(refKey)) {
            return ReferenceType.OBJECT_INDEX_REF;
        } else if (s.startsWith("%`")) {
            return ReferenceType.LITERAL_REF;
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
        // Not the same as StringUtils.unwrap()
        return StringUtils.removeEnd(StringUtils.removeStart(ref, "%"), "%");
    }

    /**
     * Capture pairs as potential targets of references, the Object Index if there is one, and pairs that contain
     * references since we'll need to process those.
     *
     * @param pair a Pair
     */
    private TransformationContext accept(final TransformationContext ctx, final Pair pair) {
        if (pair.getKey()
                .equals("?")) {

            // Capture the Object Index
            final Vector<ArrayItem> objectIndex;

            if (pair.getValue() instanceof Array) {
                objectIndex = ((Array) pair.getValue()).getArrayItems();
            } else {
                final ArrayItem value = (ArrayItem) pair.getValue();
                objectIndex = Vector.of(value);
            }
            return ctx.withObjectIndex(objectIndex);
        }
        return ctx;
    }


    /**
     * Replace if necessary
     *
     * @param ctx       TransformationContext
     * @param condition a Condition
     * @return a Condition
     */
    public Condition apply(final TransformationContext ctx, final Condition condition) {

        Primitive newLhs = condition.getLhs();
        Operator op = condition.getOp();
        Vector<ValueItem> values = condition.getValues();

        final String value = newLhs.toString();

        if (value != null) {
            if (value.contains("%")) {
                final ValueItem applyResult = apply(ctx, newLhs);
                if (applyResult instanceof StringPrimitive) {
                    newLhs = (StringPrimitive) applyResult;
                } else {
                    newLhs = StringPrimitive.of(ctx.getAncestry(), condition, applyResult.toString());
                }
            } else {
                final Pair pair = findPairFromAncestry(ctx, condition, value);

                if (pair != null) {
                    if (pair.getValue() instanceof StringPrimitive) {
                        newLhs = (StringPrimitive) pair.getValue();
                    } else {
                        newLhs = StringPrimitive.of(ctx.getAncestry(), condition, pair.getValue()
                                .toString());
                    }
                }
            }
        }

        if (!condition.getLhs()
                .equals(newLhs)) {

            return condition.with(ctx.getAncestry(), newLhs, op, values, condition.isShouldNegate());
        }
        return condition;
    }

    private Pair findPairFromAncestry(final TransformationContext ctx, final Child child, final String value) {
        final String hiddenKey = (value.startsWith("_")) ? value : "_" + value;
        final String unhiddenKey = (value.startsWith("_")) ? value.substring(1) : value;

        return ctx.getAncestry()
                .findByKey(child, hiddenKey)
                .orElse(() -> ctx.getAncestry()
                        .findByKey(child, unhiddenKey))
                .getOrElse((Pair) null);
    }

    /**
     * Replace if necessary
     *
     * @param ctx TransformationContext
     * @param vi  a ValueItem
     * @return a ValueItem
     */
    public ValueItem apply(final TransformationContext ctx, final ValueItem vi) {
        if (vi instanceof StringPrimitive) {
            final String s = ((StringPrimitive) vi).getValue();
            if (s != null) {
                final Map<ReferenceType, Vector<String>> groupedByType = getReferenceGroups(s);

                // Process the OBJECT_INDEX_REF entries
                ValueItem result = groupedByType.get(ReferenceType.OBJECT_INDEX_REF)
                        .map(i -> indexToReferencedObject(ctx, i))
                        .map(replaceAllObjectIndexRefsInValueItem(ctx, vi))
                        .getOrElse(vi);

                // Process simple String references, e.g. %val% etc.
                result = groupedByType.get(ReferenceType.SIMPLE_REF)
                        .map(k -> keyToReferencedObject(ctx, vi, k))
                        .map(replaceAllSimpleRefsInValueItem(ctx, result))
                        .getOrElse(result);

                // Process complex references
                final ValueItem finalResult = result;
                result = groupedByType.get(ReferenceType.COMPLEX_REF)
                        .map(refList -> refList.map(cr -> complexRefToValueItem(ctx, vi, cr)))
                        .map(tuples -> tuples.get(0))
                        .map(tuple -> {
                            final Pair pair = Pair.of(ctx.getAncestry(), vi, "", tuple._2);
                            final Vector<Tuple3<String, String, Option<Pair>>> vector = Vector.of(Tuple.of(tuple._1, tuple._1, Option.of(pair)));
                            return replaceAllSimpleRefsInValueItem(ctx, finalResult).apply(vector);
                        })
                        .getOrElse(result);

                // Ignore literals as they are handled separately.

                return result;
            }
        }
        return vi;
    }

    private Tuple2<String, ValueItem> complexRefToValueItem(final TransformationContext ctx, final ValueItem vi, final String complexRef) {
        final String ref = (complexRef.startsWith("%`%")) ? complexRef : stripLeadingAndTrailingPercents(complexRef);
        final String chainedMethods = StringUtils.substringAfter(ref, ".");

        final String head = StringUtils.substringBefore(ref, ".");
        final boolean wasQuoted = head.startsWith("`") && head.endsWith("`");
        final String reference = Util.unquote(head);

        final Vector<String> methods = Util.toMethodList(chainedMethods);
        final String[] refList = methods.toJavaArray(String[]::new);
        Vector<Tuple3<String, String, Option<Pair>>> referencedObject = keyToReferencedObject(ctx, vi, Vector.of(reference));

        try {
            final Vector<ValueItem> valueItems = referencedObject.flatMap(t -> {
                if (wasQuoted) {
                    return Option.of(StringPrimitive.of(ctx.getAncestry(), vi, t._2));
                }
                if (t._3.isDefined()) {
                    return t._3;
                }
                if (StringUtils.isNumeric(t._2)) {
                    return Option.of((ValueItem) ctx.getObjectIndex()
                            .get(Integer.parseInt(t._2)));
                } else {
                    return Option.of(StringPrimitive.of(ctx.getAncestry(), vi, t._2));
                }
            })
                    .map(pair -> followNestedRef(ctx, pair, refList, 0));

            if (valueItems.size() == 1) {
                return Tuple.of(complexRef, valueItems.get(0));
            } else if (valueItems.size() > 1) {
                throw new RuntimeException("Expected 1 ValueItem but found: " + valueItems.size());
            } else {
                throw new RuntimeException("Expected 1 ValueItem but found none. ");
            }
        } catch (final DeepReferenceException dre) {
            throw new RuntimeException("Invalid object reference: \"" + complexRef + "\"");
        }
    }

    private ValueItem followNestedRef(final TransformationContext ctx, final ValueItem vi, final String[] refList, final int refIndex) {
        if (refIndex == refList.length) {
            return vi;
        }

        final String ref = refList[refIndex];
        if (StringUtils.isNumeric(ref)) {
            final int refNum = Integer.parseInt(ref);
            if (vi instanceof Array) {
                final Array arr = (Array) vi;
                final ValueItem valueItem = (ValueItem) arr.getArrayItems()
                        .get(refNum);

                return followNestedRef(ctx, valueItem, refList, refIndex + 1);

            } else if (vi instanceof Pair && ((Pair) vi).getValue() instanceof Array) {
                final ValueItem valueItem = (ValueItem) ((Array) ((Pair) vi).getValue()).getArrayItems()
                        .get(refNum);
                return followNestedRef(ctx, valueItem, refList, refIndex + 1);
            } else {
                if (refIndex < (refList.length - 1)) {
                    throw new DeepReferenceException("Invalid reference.");
                } else {
                    throw new RuntimeException("Found a map when expecting an array");
                }
            }
        } else {
            // If we have a Map then try to get a Pair from within it and recurse.
            if (vi instanceof uk.modl.model.Map) {
                final Option<MapItem> matchingMapItem = ((uk.modl.model.Map) vi).getMapItems()
                        .find(mapItem -> {
                            // Check whether the reference key needs de-referencing
                            if (ref.contains("%")) {
                                String rawRef = stripLeadingAndTrailingPercents(ref);

                                final Pair pair = findPairFromAncestry(ctx, vi, rawRef);

                                if (pair != null) {
                                    final String actualRef = pair.getValue()
                                            .toString();

                                    return mapItem instanceof Pair && ((Pair) mapItem).getKey()
                                            .equals(actualRef);
                                } else {
                                    throw new DeepReferenceException("No entry '" + ref + "' in Map '" + vi + "'");
                                }
                            } else {
                                final String hiddenKey = (ref.startsWith("_")) ? ref : "_" + ref;
                                final String unhiddenKey = (ref.startsWith("_")) ? ref.substring(1) : ref;
                                return mapItem instanceof Pair && (((Pair) mapItem).getKey()
                                        .equals(hiddenKey) || ((Pair) mapItem).getKey()
                                        .equals(unhiddenKey));
                            }

                        });
                if (matchingMapItem.isDefined()) {
                    return followNestedRef(ctx, (ValueItem) matchingMapItem.get(), refList, refIndex);
                } else {
                    throw new DeepReferenceException("No entry '" + ref + "' in Map '" + vi + "'");
                }
            }
            // If we have a Pair then take the pair value and recurse if possible
            final int skipRefIndexesForPathElementsWithReferences = (ref.contains("%")) ? refIndex + 1 : refIndex;
            if (vi instanceof Pair) {
                final PairValue value = ((Pair) vi).getValue();

                if (!(value instanceof Primitive)) {
                    final String hiddenKey = (ref.startsWith("_")) ? ref : "_" + ref;
                    final String unhiddenKey = (ref.startsWith("_")) ? ref.substring(1) : ref;
                    if (((Pair) vi).getKey()
                            .equals(hiddenKey) || ((Pair) vi).getKey()
                            .equals(unhiddenKey)) {
                        return followNestedRef(ctx, (ValueItem) value, refList, refIndex + 1);
                    } else {
                        // Use the current refIndex since we haven't yet consumed it.
                        return followNestedRef(ctx, (ValueItem) value, refList, refIndex);
                    }
                }
                if (refIndex == (refList.length - 1) && ((Pair) vi).getKey()
                        .equals(ref)) {
                    return (ValueItem) value;
                }
                // Handle methods and trailing values
                final String valueStr = handleMethodsAndTrailingPathComponents(ctx, refList, skipRefIndexesForPathElementsWithReferences, value.toString());
                if (!StringUtils.isNumeric(valueStr)) {
                    return StringPrimitive.of(vi.getId(), valueStr);
                } else {
                    return NumberPrimitive.of(vi.getId(), valueStr);
                }
            }
            if (vi instanceof StringPrimitive) {
                // Handle methods and trailing values
                final String valueStr = handleMethodsAndTrailingPathComponents(ctx, refList, skipRefIndexesForPathElementsWithReferences, vi.toString());
                return StringPrimitive.of(vi.getId(), valueStr);
            }
        }
        return vi;
    }

    private String handleMethodsAndTrailingPathComponents(final TransformationContext ctx, final String[] refList, final int refIndex, String valueStr) {
        for (int i = refIndex; i < refList.length; i++) {
            final String pathComponent = refList[i];
            if (pathComponent.length() == 1) {
                switch (pathComponent) {
                    case "p":
                        valueStr = Util.replacePunycode(Util.unquote(valueStr));
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
                            throw new RuntimeException("Error processing URL encoding instruction: " + e.getMessage());
                        }
                        break;
                    default:
                        break;
                }
            } else if (pathComponent.startsWith("r<") || pathComponent.startsWith("replace<")) {
                valueStr = Util.replacer(pathComponent, valueStr);
            } else if (pathComponent.startsWith("t<") || pathComponent.startsWith("trim<")) {
                valueStr = Util.trimmer(pathComponent, valueStr);
            } else {
                final String updated = methodsTransform.apply(ctx, pathComponent, valueStr);

                // If unchanged then assume no method was run and just append the path component.
                if (updated.equals(valueStr)) {
                    valueStr += "." + pathComponent;
                } else {
                    valueStr = updated;
                }
            }
        }
        return valueStr;
    }

    /**
     * Replace a pair with a new value that has its references resolved.
     *
     * @param p a Pair with references
     * @return a Pair with the references resolved
     */
    public Tuple2<TransformationContext, Structure> apply(final TransformationContext ctx, final Structure p) {
        if (p == null) {
            return Tuple.of(ctx, null);
        }

        TransformationContext newCtx = ctx;

        if (p instanceof Pair) {
            final Pair pair = (Pair) p;
            if (pair.getValue() instanceof ValueConditional) {
                return Tuple.of(ctx, p);
            } else {
                if (!pair.getValue()
                        .toString()
                        .startsWith("%*")) {
                    final Tuple2<TransformationContext, Pair> resolved = resolve(newCtx, pair);
                    newCtx = resolved._1;

                    newCtx.getAncestry()
                            .replaceChild(((Pair) p).getValue(), resolved._2.getValue());
                    newCtx = accept(newCtx, resolved._2);

                    final Pair transformedPair = resolved._2;

                    return Tuple.of(newCtx, transformedPair);
                }
            }
        }
        if (p instanceof Array) {
            final @NonNull Vector<ArrayItem> items = ((Array) p).getArrayItems();

            Vector<ArrayItem> newItems = Vector.empty();

            for (final ArrayItem item : items) {
                if (item instanceof Structure) {
                    final Tuple2<TransformationContext, Structure> applyResult = apply(ctx, (Structure) item);
                    newItems = newItems.append((ArrayItem) applyResult._2);
                    newCtx = applyResult._1;
                } else {
                    final ValueItem applyResult = apply(ctx, (ValueItem) item);
                    newItems = newItems.append((ArrayItem) applyResult);
                }
            }

            return Tuple.of(newCtx, ((Array) p).with(ctx.getAncestry(), newItems));
        }
        if (p instanceof uk.modl.model.Map) {
            @NonNull final Vector<MapItem> items = ((uk.modl.model.Map) p).getMapItems();

            Vector<MapItem> newItems = Vector.empty();

            for (final MapItem item : items) {
                if (item instanceof Structure) {
                    final Tuple2<TransformationContext, Structure> applyResult = apply(newCtx, (Structure) item);
                    newItems = newItems.append((MapItem) applyResult._2);
                    newCtx = applyResult._1;
                } else {
                    final ValueItem applyResult = apply(ctx, (ValueItem) item);
                    newItems = newItems.append((MapItem) applyResult);
                }
            }

            return Tuple.of(newCtx, ((uk.modl.model.Map) p).with(ctx.getAncestry(), newItems));

        }
        return Tuple.of(ctx, p);
    }

    /**
     * Update the pairKeysWithReferences to new values with the references replaced by actual values
     */
    private Tuple2<TransformationContext, Pair> resolve(final TransformationContext ctx, final Pair p) {
        TransformationContext newCtx = ctx;

        if (p.getValue() instanceof StringPrimitive) {
            final Map<ReferenceType, Vector<String>> groupedByType = getReferenceGroups(p.getValue()
                    .toString());

            // Process the OBJECT_INDEX_REF entries
            Pair result = groupedByType.get(ReferenceType.OBJECT_INDEX_REF)
                    .map(i -> indexToReferencedObject(ctx, i))
                    .map(replaceAllObjectIndexRefs(ctx, p))
                    .getOrElse(p);

            // Process simple String references, e.g. %val% etc.
            result = groupedByType.get(ReferenceType.SIMPLE_REF)
                    .map(k -> keyToReferencedObject(ctx, p, k))
                    .map(replaceAllSimpleRefs(ctx, result))
                    .getOrElse(result);

            // Process complex references
            final Pair finalResult = result;
            result = groupedByType.get(ReferenceType.COMPLEX_REF)
                    .map(refList -> complexRefToReferencedItems(ctx, finalResult, refList))
                    .map(replaceAllSimpleRefs(ctx, result))
                    .getOrElse(result);

            // Ignore literals as they are handled separately.

            return Tuple.of(newCtx, result);
        }
        if (p.getValue() instanceof uk.modl.model.Map) {
            @NonNull final Vector<MapItem> items = ((uk.modl.model.Map) p.getValue()).getMapItems();

            Vector<MapItem> newItems = Vector.empty();

            for (final MapItem item : items) {
                if (item instanceof Structure) {
                    final Tuple2<TransformationContext, Structure> applyResult = apply(newCtx, (Structure) item);
                    newItems = newItems.append((MapItem) applyResult._2);
                    newCtx = applyResult._1;
                } else {
                    newItems = newItems.append(item);
                }
            }

            return Tuple.of(newCtx, p.with(ctx.getAncestry(), ((uk.modl.model.Map) p.getValue()).with(ctx.getAncestry(), newItems)));
        }
        if (p.getValue() instanceof Array) {
            final @NonNull Vector<ArrayItem> items = ((Array) p.getValue()).getArrayItems();

            Vector<ArrayItem> newItems = Vector.empty();

            for (final ArrayItem item : items) {
                if (item instanceof Structure) {
                    final Tuple2<TransformationContext, Structure> applyResult = apply(ctx, (Structure) item);
                    newItems = newItems.append((ArrayItem) applyResult._2);
                    newCtx = applyResult._1;
                } else {
                    newItems = newItems.append(item);
                }
            }

            return Tuple.of(newCtx, p.with(ctx.getAncestry(), ((uk.modl.model.Array) p.getValue()).with(ctx.getAncestry(), newItems)));
        }
        return Tuple.of(newCtx, p);
    }

    private Vector<Tuple3<String, String, Option<Pair>>> complexRefToReferencedItems(final TransformationContext ctx, final Pair p, final Vector<String> refList) {
        return refList.map(ref -> {
            final Tuple2<String, ValueItem> result = complexRefToValueItem(ctx, p, ref);
            return Tuple.of(ref, p.getKey(), Option.of(p.with(ctx.getAncestry(), result._2)));
        });
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
    private Function<Vector<Tuple4<String, String, Integer, Option<ArrayItem>>>, Pair> replaceAllObjectIndexRefs(final TransformationContext ctx, final Pair p) {
        return refTuples -> refTuples.foldLeft(p, replaceObjectIndexRefInArrayItem(ctx));
    }

    /**
     * Return a function to fold a list of replacement values into a value with references, i.e. replace references with their actual values.
     *
     * @param vi a ValueItem with references
     * @return a function to convert the String to a String with references replaced
     */
    private Function<Vector<Tuple4<String, String, Integer, Option<ArrayItem>>>, ValueItem> replaceAllObjectIndexRefsInValueItem(final TransformationContext ctx, final ValueItem vi) {
        return refTuples -> refTuples.foldLeft(vi, replaceObjectIndexRefInValueItem(ctx));
    }

    /**
     * Return a function to fold a list of replacement values into a value with references, i.e. replace references with their actual values.
     *
     * @param p a Pair with references
     * @return a function to convert the Pair in the supplied tuple to a Pair with references replaced
     */
    private Function<Vector<Tuple3<String, String, Option<Pair>>>, Pair> replaceAllSimpleRefs(final TransformationContext ctx, final Pair p) {
        return refTuples -> refTuples.foldLeft(p, replaceSimpleRefInPair(ctx));
    }

    /**
     * Return a function to fold a list of replacement values into a value with references, i.e. replace references with their actual values.
     *
     * @param vi a ValueItem with references
     * @return a function to convert the String to a String with references replaced
     */
    private Function<Vector<Tuple3<String, String, Option<Pair>>>, ValueItem> replaceAllSimpleRefsInValueItem(final TransformationContext ctx, final ValueItem vi) {
        return refTuples -> refTuples.foldLeft(vi, replaceSimpleRefInValueItem(ctx));
    }

    /**
     * Map an index to the object that it references
     *
     * @param list a Vector of String indexes of the form `%1%` or `%1`
     * @return a tuple of the original reference, %-stripped reference, integer value, and possible new value, e.g. ("%1%","1", 1, Option(ArrayItem))
     */
    private Vector<Tuple4<String, String, Integer, Option<ArrayItem>>> indexToReferencedObject(final TransformationContext ctx, final Vector<String> list) {
        return list.map(s -> Tuple.of(s, stripLeadingAndTrailingPercents(s)))// E.g. ("%1%","1")
                .map(t -> t.append(Integer.parseInt(t._2)))// E.g. ("%1%","1", 1)
                .map(t -> {
                    if (t._3 >= 0 && t._3 < ctx.getObjectIndex()
                            .size()) {
                        return t.append(Option.of(ctx.getObjectIndex()
                                .get(t._3)));
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
    private Vector<Tuple3<String, String, Option<Pair>>> keyToReferencedObject(final TransformationContext ctx, final ValueItem vi, final Vector<String> list) {
        return list.map(s -> Tuple.of(s, stripLeadingAndTrailingPercents(s)))// E.g. ("%var%","var")
                .map(t -> {


                    final String hiddenKey = (t._2.startsWith("_")) ? t._2 : "_" + t._2;
                    final String unhiddenKey = (t._2.startsWith("_")) ? t._2.substring(1) : t._2;

                    final Pair transformedPair = ctx.getAncestry()
                            .findByKey(vi, hiddenKey)
                            .orElse(() -> ctx.getAncestry()
                                    .findByKey(vi, unhiddenKey))
                            .getOrElse((Pair) null);

                    final Option<Pair> pair = Option.of(transformedPair);

                    return t.append(pair);
                });// E.g. ("%var%","var", Option(Pair))
    }

    /**
     * Update the Pair in the `curr` tuple by replacing the reference if it matches the index in the `next` tuple
     *
     * @return a tuple with an updated Pair
     */
    private BiFunction<Pair, Tuple4<String, String, Integer, Option<ArrayItem>>, Pair> replaceObjectIndexRefInArrayItem(final TransformationContext ctx) {
        return (curr, next) -> {
            if (next._4.isDefined()) {
                // If the item containing the reference is a StringPrimitive then do String substitution
                if (next._4.get() instanceof StringPrimitive) {
                    final String s = ((StringPrimitive) curr.getValue()).getValue();
                    final String r = ctx.getObjectIndex()
                            .get(next._3)
                            .toString();
                    final String indexReference = next._1;
                    String tmpResult = s;
                    if (!indexReference.endsWith("%")) {
                        tmpResult = s.replace(indexReference + "%", r);
                    }
                    return curr.with(ctx.getAncestry(), StringPrimitive.of(ctx.getAncestry(), curr, tmpResult.replace(indexReference, r)));
                }

                // Otherwise replace the whole thing
                return curr.with(ctx.getAncestry(), (PairValue) ctx.getObjectIndex()
                        .get(next._3));
            }

            return curr;
        };
    }

    /**
     * Update the `curr` value by replacing the reference if it matches the index in the `next` tuple
     *
     * @return a tuple with an updated Pair
     */
    private BiFunction<ValueItem, Tuple4<String, String, Integer, Option<ArrayItem>>, ValueItem> replaceObjectIndexRefInValueItem(final TransformationContext ctx) {
        return (curr, next) -> {
            if (next._4.isDefined()) {
                // If the item containing the reference is a StringPrimitive then do String substitution
                if ((next._4.get() instanceof NumberPrimitive || next._4.get() instanceof StringPrimitive) && curr instanceof StringPrimitive) {
                    final String r = ctx.getObjectIndex()
                            .get(next._3)
                            .toString();

                    final String indexReference = next._1;
                    final String s = ((StringPrimitive) curr).getValue();
                    String tmpResult = s;
                    if (!indexReference.endsWith("%")) {
                        tmpResult = s.replace(indexReference + "%", r);
                    }

                    return ((StringPrimitive) curr).with(ctx.getAncestry(), tmpResult.replace(next._1, r));
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
    private BiFunction<Pair, Tuple3<String, String, Option<Pair>>, Pair> replaceSimpleRefInPair(final TransformationContext ctx) {
        return (curr, next) -> {
            if (next._3.isDefined()) {
                // If the item containing the reference is a StringPrimitive then do String substitution
                final Pair p = next._3.get();
                if (p.getValue() instanceof StringPrimitive && curr.getValue() instanceof StringPrimitive) {
                    final String s = ((StringPrimitive) curr.getValue()).getValue();
                    final String r = ((StringPrimitive) p.getValue()).getValue();
                    return curr.with(ctx.getAncestry(), StringPrimitive.of(ctx.getAncestry(), curr, s.replace(next._1, r)));
                } else if (p.getValue() instanceof NumberPrimitive && curr.getValue() instanceof StringPrimitive) {
                    final String s = ((StringPrimitive) curr.getValue()).getValue();
                    final String r = ((NumberPrimitive) p.getValue()).getValue();
                    if (s.equals(next._1)) {
                        return curr.with(ctx.getAncestry(), NumberPrimitive.of(ctx.getAncestry(), curr, s.replace(next._1, r)));
                    } else {
                        return curr.with(ctx.getAncestry(), StringPrimitive.of(ctx.getAncestry(), curr, s.replace(next._1, r)));
                    }
                }
                // Otherwise replace the whole thing
                return curr.with(ctx.getAncestry(), p.getValue());
            }
            return curr;
        };
    }

    /**
     * Update `curr` String by replacing the reference if it matches the index in the `next` tuple
     *
     * @return an updated String
     */
    private BiFunction<ValueItem, Tuple3<String, String, Option<Pair>>, ValueItem> replaceSimpleRefInValueItem(final TransformationContext ctx) {
        return (curr, next) -> {
            if (next._3.isDefined()) {
                // If the item containing the reference is a StringPrimitive then do String substitution
                if (next._3.get()
                        .getValue() instanceof StringPrimitive && curr instanceof StringPrimitive) {
                    final String r = ((StringPrimitive) next._3.get()
                            .getValue()).getValue();
                    return ((StringPrimitive) curr).with(ctx.getAncestry(), ((StringPrimitive) curr).getValue()
                            .replace(next._1, r));
                } else {
                    return (ValueItem) next._3.get()
                            .getValue();
                }
            }
            return curr;
        };
    }

    private enum ReferenceType {
        COMPLEX_REF, OBJECT_INDEX_REF, LITERAL_REF, SIMPLE_REF
    }

}
