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

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import uk.modl.model.*;
import uk.modl.utils.Util;

@RequiredArgsConstructor
public class StarClassTransform {

    private static final Vector<String> RESERVED_CLASS_NAMES = Vector.of(
            "arr",
            "map",
            "str",
            "num",
            "bool",
            "null"
    );

    private final ReferencesTransform referencesTransform = new ReferencesTransform();

    /**
     * Check whether the key represents a *class instruction
     *
     * @param s the Structure to check
     * @return true if the key represents a class instruction
     */
    public static boolean isClassInstruction(final @NonNull Structure s) {
        return (s instanceof Pair) && StringUtils.equalsAnyIgnoreCase(((Pair) s).getKey(), "*c", "*class");
    }


    /**
     * Applies this function to one argument and returns the result.
     *
     * @param ctx TransformationContext
     * @param p   Structure
     * @return the result of function application
     */
    public Tuple2<TransformationContext, Structure> apply(final TransformationContext ctx, final @NonNull Structure p) {
        return (isClassInstruction(p))
                ? Tuple.of(accept(ctx, (Pair) p), null)
                : Tuple.of(ctx, p);
    }

    /**
     * Extract a Class instruction from a Pair
     *
     * @param pair the Pair
     * @return TransformationContext
     */
    private TransformationContext accept(final TransformationContext ctx, final @NonNull Pair pair) {
        if (pair.getValue() instanceof Map) {
            String id = null;
            String name = null;
            String superclass = null;
            Vector<ArrayItem> assign = Vector.empty();
            io.vavr.collection.Map<String, Pair> pairs = LinkedHashMap.empty();

            for (val mi : ((Map) pair.getValue()).getMapItems()) {
                if (mi instanceof Pair) {
                    val p = (Pair) mi;

                    switch (p.getKey()
                            .toLowerCase()) {
                        case "*i":
                        case "*id": {
                            val value = p.getValue();
                            if (value instanceof StringPrimitive) {
                                if (Util.isKeywordAllowedInClassesAndMethods(((StringPrimitive) value).getValue())) {
                                    id = value
                                            .toString();
                                } else {
                                    throw new RuntimeException("Class *id is not a valid pair name " + ((StringPrimitive) value).getValue());
                                }
                            } else {
                                throw new RuntimeException("Class *id should be a String value, but is a " + value.getClass()
                                        .getSimpleName());
                            }
                        }
                        break;
                        case "*n":
                        case "*name": {
                            val value = p.getValue();
                            if (value instanceof StringPrimitive) {
                                if (Util.isKeywordAllowedInClassesAndMethods(((StringPrimitive) value).getValue())) {
                                    name = value
                                            .toString();
                                } else {
                                    throw new RuntimeException("Class *name is not a valid pair name " + ((StringPrimitive) value).getValue());
                                }
                            } else {
                                throw new RuntimeException("Class *name should be a String value, but is a " + value.getClass()
                                        .getSimpleName());
                            }
                        }
                        break;
                        case "*s":
                        case "*superclass":
                            val value = p.getValue();
                            if (value instanceof StringPrimitive) {
                                if (Util.isKeywordAllowedInClassesAndMethods(((StringPrimitive) value).getValue())) {
                                    superclass = value
                                            .toString();
                                } else {
                                    throw new RuntimeException("Class *superclass is not a valid pair name " + ((StringPrimitive) value).getValue());
                                }
                            } else {
                                throw new RuntimeException("Class *superclass should be a String value, but is a " + value.getClass()
                                        .getSimpleName());
                            }
                            break;
                        case "*a":
                        case "*assign":
                            assign = extractAssign(p);
                            break;
                        default:
                            if (p.getValue()
                                    .toString()
                                    .contains("%")) {
                                val result = referencesTransform.apply(ctx, (Structure) p);
                                pairs = pairs.put(p.getKey(), (Pair) result._2);
                            } else {
                                pairs = pairs.put(p.getKey(), p);
                            }
                    }
                } else {
                    throw new RuntimeException("Expected a Pair but found a " + mi.getClass());
                }
            }

            validateAssign(assign);

            if (id != null && RESERVED_CLASS_NAMES.contains(id.toLowerCase())) {
                throw new RuntimeException("Reserved class id - cannot redefine: " + id);
            }
            if (name != null && RESERVED_CLASS_NAMES.contains(name.toLowerCase())) {
                throw new RuntimeException("Reserved class name - cannot redefine: " + name);
            }

            if (superclass != null && !RESERVED_CLASS_NAMES.contains(superclass) && !ctx.getClassByNameOrId(superclass)
                    .isDefined()) {
                throw new RuntimeException("Invalid superclass: " + superclass);
            }

            TransformationContext newCtx = ctx.addClassInstruction(ClassInstruction.of(id, name, superclass, assign, pairs));
            if (pair.getKey()
                    .startsWith("*C")) {
                newCtx = newCtx.withStarClassImmutable(true);
            }
            return newCtx;
        } else {
            throw new RuntimeException("Expected a map for " + pair.getKey() + " but found a " + pair.getValue()
                    .getClass());
        }
    }

    private Vector<ArrayItem> extractAssign(final Pair p) {
        final Vector<ArrayItem> assign;
        if (p.getValue() instanceof Array) {
            assign = ((Array) p.getValue()).getArrayItems()
                    .map(ai -> {
                        if (ai instanceof Array) {
                            ((Array) ai).getArrayItems()
                                    .forEach(nestedItem -> {
                                        if (!(nestedItem instanceof StringPrimitive)) {
                                            throw new RuntimeException("*assign statement should be an Array of Arrays of Strings.");
                                        } else if (!Util.isKeywordAllowedInClassesAndMethods(((StringPrimitive) nestedItem).getValue())) {
                                            throw new RuntimeException("*assign statement contains an invalid key: " + ((StringPrimitive) nestedItem).getValue());
                                        }
                                    });
                            return ai;
                        } else {
                            throw new RuntimeException("*assign statement should be an Array of Arrays");
                        }
                    });
        } else {
            throw new RuntimeException("*assign statement should be an Array of Arrays");
        }
        return assign;
    }

    private void validateAssign(final Vector<ArrayItem> assigns) {
        int lastLen = 0;
        for (val assign : assigns) {
            val array = (Array) assign;
            @NonNull val arrayItems = array.getArrayItems();
            if (arrayItems.size() <= lastLen) {
                val strings = arrayItems.map(ai -> "\"" + ai.toString() + "\"")
                        .intersperse(", ");
                val arrayStr = strings.foldLeft("[", (l, r) -> l + r) + "]";
                throw new RuntimeException("Error: Key lists in *assign are not in ascending order of list length: " + arrayStr);
            }
            lastLen = arrayItems.size();
        }

    }

    @Value(staticConstructor = "of")
    public static class ClassInstruction {

        @NonNull
        String id;

        String name;

        String superclass;

        @NonNull
        Vector<ArrayItem> assign;

        @NonNull
        io.vavr.collection.Map<String, Pair> pairs;

        final String getNameOrId() {
            return (name == null) ? id : name;
        }

    }

}
