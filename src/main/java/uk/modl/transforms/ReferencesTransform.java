package uk.modl.transforms;

import io.vavr.*;
import io.vavr.collection.Map;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import uk.modl.model.*;
import uk.modl.parser.errors.DeepReferenceException;
import uk.modl.utils.Util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReferencesTransform implements Function2<TransformationContext, Structure, Tuple2<TransformationContext, Structure>> {

    private static final Pattern referencePattern = Pattern.compile("(((\\\\%|~%|%)\\w+)(\\.\\w*<`?\\w*`?,`\\w*`>)+|((\\\\%|~%|%)` ?[\\w-]+`[\\w.<>,]*%?)|((\\\\%|~%|%)\\*?[\\w]+(\\.%?\\w*<?[\\w,]*>?)*%?))");

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
    private TransformationContext accept(final TransformationContext ctx, final Pair pair) {
        if (pair.getKey()
                .equals("?")) {

            // Capture the Object Index
            final Array objectIndex;
            if (pair.getValue() instanceof Array) {
                objectIndex = (Array) pair.getValue();
            } else {
                objectIndex = new Array(Vector.of((ArrayItem) pair.getValue()));
            }
            return ctx.withObjectIndex(objectIndex);
        } else {
            // Keep a map of pairs indexed by their key...
            final TransformationContext updatedContext = ctx.addPair(pair.getKey(), pair);

            // ...and by their key without the underscore prefix if there is one.
            if (pair.getKey()
                    .startsWith("_")) {
                return updatedContext.addPair(pair.getKey()
                        .substring(1), pair);
            }
            return updatedContext;
        }
    }


    /**
     * Replace if necessary
     *
     * @param condition a Condition
     * @return a Condition
     */
    public Condition apply(final TransformationContext ctx, final Condition condition) {
        final ValueItem lhs = condition.getLhs();

        ValueItem newLhs = lhs;
        Operator op = condition.getOp();
        Vector<ValueItem> values = condition.getValues();

        if (lhs instanceof StringPrimitive) {
            final String value = ((StringPrimitive) lhs).getValue();

            if (value != null) {
                if (value.contains("%")) {
                    newLhs = apply(ctx, newLhs);
                } else {
                    if (ctx.getPairs()
                            .containsKey(value)) {
                        newLhs = (ValueItem) ctx.getPairs()
                                .get(value)
                                .get()
                                .getValue();
                    }
                }
            }
        }

        if (!(lhs != null && lhs.equals(newLhs))) {
            return new Condition(newLhs, op, values, condition.isShouldNegate());
        }
        return condition;
    }

    /**
     * Replace if necessary
     *
     * @param vi a ValueItem
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
                        .map(k -> keyToReferencedObject(ctx, k))
                        .map(replaceAllSimpleRefsInValueItem(result))
                        .getOrElse(result);

                // Process complex references
                final ValueItem finalResult = result;
                result = groupedByType.get(ReferenceType.COMPLEX_REF)
                        .map(refList -> refList.map(cr -> complexRefToValueItem(ctx, cr)))
                        .map(tuples -> tuples.get(0))
                        .map(tuple -> {
                            final Pair pair = new Pair("", tuple._2);
                            final Vector<Tuple3<String, String, Option<Pair>>> vector = Vector.of(Tuple.of(tuple._1, tuple._1, Option.of(pair)));
                            return replaceAllSimpleRefsInValueItem(finalResult).apply(vector);
                        })
                        .getOrElse(result);

                return result;
            }
        }
        return vi;
    }

    private Tuple2<String, ValueItem> complexRefToValueItem(final TransformationContext ctx, final String complexRef) {
        final String ref = stripLeadingAndTrailingPercents(complexRef);
        final String chainedMethods = StringUtils.substringAfter(ref, ".");

        final String head = StringUtils.substringBefore(ref, ".");
        final boolean wasQuoted = head.startsWith("`") && head.endsWith("`");
        final String reference = Util.unquote(head);

        final Vector<String> methods = Util.toMethodList(chainedMethods);
        final String[] refList = methods.toJavaArray(String[]::new);
        Vector<Tuple3<String, String, Option<Pair>>> referencedObject = keyToReferencedObject(ctx, Vector.of(reference));

        try {
            final Vector<ValueItem> valueItems = referencedObject.flatMap(t -> {
                if (wasQuoted) {
                    return Option.of(new StringPrimitive(t._2));
                }
                if (t._3.isDefined()) {
                    return t._3;
                }
                if (StringUtils.isNumeric(t._2)) {
                    return Option.of((ValueItem) ctx.getObjectIndex()
                            .getArrayItems()
                            .get(Integer.parseInt(t._2)));
                } else {
                    return Option.of(new StringPrimitive(t._2));
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
            }
        } else {
            // If we have a Map then try to get a Pair from within it and recurse.
            if (vi instanceof uk.modl.model.Map) {
                final Option<MapItem> matchingMapItem = ((uk.modl.model.Map) vi).getMapItems()
                        .find(mapItem -> {
                            // Check whether the reference key needs de-referencing
                            if (ref.contains("%")) {
                                String actualRef = ctx.getPairs()
                                        .get(stripLeadingAndTrailingPercents(ref))
                                        .map(pair -> pair.getValue()
                                                .toString())
                                        .getOrElse(ref);

                                return mapItem instanceof Pair && ((Pair) mapItem).getKey()
                                        .equals(actualRef);
                            } else {
                                return mapItem instanceof Pair && ((Pair) mapItem).getKey()
                                        .equals(ref);
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
                    if (((Pair) vi).getKey()
                            .equals(ref)) {
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
                return new StringPrimitive(valueStr);
            }
            if (vi instanceof StringPrimitive) {
                // Handle methods and trailing values
                final String valueStr = handleMethodsAndTrailingPathComponents(ctx, refList, skipRefIndexesForPathElementsWithReferences, vi.toString());
                return new StringPrimitive(valueStr);
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

        if (p instanceof Pair) {
            final Pair pair = (Pair) p;
            if ((pair).getValue() instanceof ValueConditional) {
                return Tuple.of(ctx, p);
            } else {
                final Pair resolved = resolve(ctx, pair);

                final TransformationContext updatedContext = accept(ctx, resolved);

                final Pair transformedPair = updatedContext.getPairs()
                        .get((pair).getKey())
                        .getOrElse(pair);

                return Tuple.of(updatedContext, transformedPair);
            }
        }
        return Tuple.of(ctx, p);
    }

    /**
     * Update the pairKeysWithReferences to new values with the references replaced by actual values
     */
    private Pair resolve(final TransformationContext ctx, final Pair p) {
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
                    .map(k -> keyToReferencedObject(ctx, k))
                    .map(replaceAllSimpleRefs(result))
                    .getOrElse(result);

            // Process complex references
            final Pair finalResult = result;
            result = groupedByType.get(ReferenceType.COMPLEX_REF)
                    .map(refList -> complexRefToReferencedItems(ctx, finalResult, refList))
                    .map(replaceAllSimpleRefs(result))
                    .getOrElse(result);

            return result;
        }
        return p;
    }

    private Vector<Tuple3<String, String, Option<Pair>>> complexRefToReferencedItems(final TransformationContext ctx, final Pair p, final Vector<String> refList) {
        return refList.map(ref -> {
            final Tuple2<String, ValueItem> result = complexRefToValueItem(ctx, ref);
            return Tuple.of(ref, p.getKey(), Option.of(new Pair(p.getKey(), result._2)));
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
    private Vector<Tuple4<String, String, Integer, Option<ArrayItem>>> indexToReferencedObject(final TransformationContext ctx, final Vector<String> list) {
        return list.map(s -> Tuple.of(s, stripLeadingAndTrailingPercents(s)))// E.g. ("%1%","1")
                .map(t -> t.append(Integer.parseInt(t._2)))// E.g. ("%1%","1", 1)
                .map(t -> {
                    if (t._3 >= 0 && t._3 < ctx.getObjectIndex()
                            .getArrayItems()
                            .size()) {
                        return t.append(Option.of(ctx.getObjectIndex()
                                .getArrayItems()
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
    private Vector<Tuple3<String, String, Option<Pair>>> keyToReferencedObject(final TransformationContext ctx, final Vector<String> list) {
        return list.map(s -> Tuple.of(s, stripLeadingAndTrailingPercents(s)))// E.g. ("%var%","var")
                .map(t -> t.append(ctx.getPairs()
                        .get(t._2)));// E.g. ("%var%","var", Option(Pair))
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
                            .getArrayItems()
                            .get(next._3)
                            .toString();
                    final String indexReference = next._1;
                    String tmpResult = s;
                    if (!indexReference.endsWith("%")) {
                        tmpResult = s.replace(indexReference + "%", r);
                    }
                    return new Pair(curr.getKey(), new StringPrimitive(tmpResult.replace(indexReference, r)));
                }
                // Otherwise replace the whole thing
                return new Pair(curr.getKey(), (PairValue) ctx.getObjectIndex()
                        .getArrayItems()
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
                            .getArrayItems()
                            .get(next._3)
                            .toString();

                    final String indexReference = next._1;
                    final String s = ((StringPrimitive) curr).getValue();
                    String tmpResult = s;
                    if (!indexReference.endsWith("%")) {
                        tmpResult = s.replace(indexReference + "%", r);
                    }

                    return new StringPrimitive(tmpResult.replace(next._1, r));
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
                if (p.getValue() instanceof StringPrimitive && curr.getValue() instanceof StringPrimitive) {
                    final String s = ((StringPrimitive) curr.getValue()).getValue();
                    final String r = ((StringPrimitive) p.getValue()).getValue();
                    return new Pair(curr.getKey(), new StringPrimitive(s.replace(next._1, r)));
                } else if (p.getValue() instanceof NumberPrimitive && curr.getValue() instanceof StringPrimitive) {
                    final String s = ((StringPrimitive) curr.getValue()).getValue();
                    final String r = ((NumberPrimitive) p.getValue()).getValue();
                    if (s.equals(next._1)) {
                        return new Pair(curr.getKey(), new NumberPrimitive(s.replace(next._1, r)));
                    } else {
                        return new Pair(curr.getKey(), new StringPrimitive(s.replace(next._1, r)));
                    }
                }
                // Otherwise replace the whole thing
                return new Pair(curr.getKey(), p.getValue());
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
                if (next._3.get()
                        .getValue() instanceof StringPrimitive && curr instanceof StringPrimitive) {
                    final String r = ((StringPrimitive) next._3.get()
                            .getValue()).getValue();
                    return new StringPrimitive(((StringPrimitive) curr).getValue()
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
        COMPLEX_REF, OBJECT_INDEX_REF, SIMPLE_REF
    }

}
