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
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import uk.modl.model.*;
import uk.modl.utils.Util;

@RequiredArgsConstructor
public class StarMethodTransform {

    /**
     * Check whether the key represents a *method instruction
     *
     * @param key the String to check
     * @return true if the key represents a method instruction
     */
    public static boolean isMethodInstruction(final String key) {
        final String lowerCase = key.toLowerCase();
        return lowerCase
                .equals("*m") || lowerCase
                .equals("*method");
    }

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param ctx TransformationContext
     * @param p   Structure
     * @return the result of function application
     */
    public Tuple2<TransformationContext, Structure> apply(final TransformationContext ctx, final Structure p) {
        if (p instanceof Pair && isMethodInstruction(((Pair) p).getKey())) {
            final TransformationContext updatedContext = accept(ctx, (Pair) p);
            return Tuple.of(updatedContext, null);
        }
        return Tuple.of(ctx, p);
    }

    /**
     * Extract a Method instruction from a Pair
     *
     * @param pair the Pair
     * @return TransformationContext
     */
    private TransformationContext accept(final TransformationContext ctx, final Pair pair) {
        if (pair.getValue() instanceof Map) {
            String name = null;
            String id = null;
            String transform = null;

            for (final MapItem mi : ((Map) pair.getValue()).getMapItems()) {
                if (mi instanceof Pair) {
                    final Pair p = (Pair) mi;
                    switch (p.getKey()
                            .toLowerCase()) {
                        case "*i":
                        case "*id": {
                            final PairValue value = p.getValue();
                            if (value instanceof StringPrimitive) {
                                if (Util.isKeywordAllowedInClassesAndMethods(((StringPrimitive) value).getValue())) {
                                    id = value
                                            .toString();
                                } else {
                                    throw new RuntimeException("Method *id is not a valid pair name " + ((StringPrimitive) value).getValue());
                                }
                            } else {
                                throw new RuntimeException("Method *id should be a String value, but is a " + value.getClass()
                                        .getSimpleName());
                            }
                        }
                        break;
                        case "*n":
                        case "*name": {
                            final PairValue value = p.getValue();
                            if (value instanceof StringPrimitive) {
                                if (Util.isKeywordAllowedInClassesAndMethods(((StringPrimitive) value).getValue())) {
                                    name = value
                                            .toString();
                                } else {
                                    throw new RuntimeException("Method *name is not a valid pair name " + ((StringPrimitive) value).getValue());
                                }
                            } else {
                                throw new RuntimeException("Method *name should be a String value, but is a " + value.getClass()
                                        .getSimpleName());
                            }
                        }
                        break;
                        case "*t":
                        case "*transform": {
                            final PairValue value = p.getValue();
                            if (value instanceof StringPrimitive) {
                                transform = value
                                        .toString();
                            } else {
                                throw new RuntimeException("Method *transform should be a String value, but is a " + value.getClass()
                                        .getSimpleName());
                            }
                        }
                        break;
                    }
                } else {
                    throw new RuntimeException("Expected a Pair but found a " + mi.getClass());
                }
            }

            final MethodInstruction m = MethodInstruction.of(id, name, transform);
            return ctx.addMethodInstruction(m);
        } else {
            throw new RuntimeException("Expected a map for " + pair.getKey() + " but found a " + pair.getValue()
                    .getClass());
        }
    }

    @Value(staticConstructor = "of")
    public static class MethodInstruction {

        @NonNull
        String id;

        String name;

        @NonNull
        String transform;

    }

}
