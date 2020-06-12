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

package uk.modl.utils;

import io.vavr.Function2;
import io.vavr.collection.List;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import uk.modl.model.*;
import uk.modl.transforms.StarClassTransform;
import uk.modl.transforms.TransformationContext;

import java.util.Objects;

public class SupertypeInference {

    private static final int MAX_RECURSION_DEPTH = 50;

    public static String inferType(final TransformationContext ctx, final StarClassTransform.ClassInstruction ci, final PairValue pv) {
        String tc = topClass(ctx, ci, 0);
        if (tc == null) {
            if (hasAssignStatement(ctx, ci)) {
                if (allAssignmentKeysAreClasses(ctx, ci)) {
                    tc = "arr";
                } else {
                    tc = "map";
                }
            } else {
                if (hasInheritedPairs(ctx, ci, 0)) {
                    tc = "map";
                } else {
                    if (pv instanceof StringPrimitive) {
                        tc = "str";
                    } else if (pv instanceof NumberPrimitive) {
                        tc = "num";
                    } else if ((pv instanceof TruePrimitive) || (pv instanceof FalsePrimitive)) {
                        tc = "bool";
                    } else if (pv instanceof NullPrimitive) {
                        tc = "null";
                    } else if (pv instanceof Array) {
                        tc = "arr";
                    } else if (pv instanceof Map) {
                        tc = "map";
                    } else if (pv instanceof Pair) {
                        tc = "map";
                    } else {
                        throw new NullPointerException("Unknown object type: " + pv.getClass());
                    }
                }
            }
        }
        return tc;
    }

    private static boolean hasInheritedPairs(final TransformationContext ctx, final StarClassTransform.ClassInstruction ci, final int depth) {
        if (depth > MAX_RECURSION_DEPTH || ci == null) {
            return false;
        }
        if (ci.getPairs()
                .length() > 0) {
            return true;
        }
        return ctx.getClassByNameOrId(ci.getSuperclass())
                .map(sc -> hasInheritedPairs(ctx, sc, depth + 1))
                .getOrElse(false);
    }

    private static boolean allAssignmentKeysAreClasses(final TransformationContext ctx, final StarClassTransform.ClassInstruction ci) {
        // Count the keys that don't map to classes - if we have any then the result is false
        final List<String> allInheritedAssignKeys = allInheritedAssignKeys(ctx, ci, 0);
        boolean allHaveAssigns = allAssignClassesHaveAssigns(ctx, allInheritedAssignKeys);

        final boolean allAssignKeysAreClasses = allInheritedAssignKeys.map(ctx::getClassByNameOrId)
                .count(Objects::isNull) == 0;
        return allHaveAssigns && allAssignKeysAreClasses;
    }

    private static boolean allAssignClassesHaveAssigns(final TransformationContext ctx, final List<String> allInheritedAssignKeys) {
        final int expected = allInheritedAssignKeys.size();

        return expected == allInheritedAssignKeys.count(key -> {
            final Option<StarClassTransform.ClassInstruction> maybeClass = ctx.getClassByNameOrId(key);
            return maybeClass.isDefined() && maybeClass.get()
                    .getAssign()
                    .nonEmpty();
        });
    }

    private static boolean hasAssignStatement(final TransformationContext ctx, final StarClassTransform.ClassInstruction ci) {
        return allInheritedAssignKeys(ctx, ci, 0).nonEmpty();
    }

    private static List<String> allInheritedAssignKeys(final TransformationContext ctx, final StarClassTransform.ClassInstruction ci, final int depth) {

        // Check for max recursion depth
        if (depth > MAX_RECURSION_DEPTH || ci == null) {
            return List.empty();
        }

        // List the assign keys for the current ClassInstruction
        final List<String> assignKeys = ci.getAssign()
                .map(x -> ((Array) x).getArrayItems()
                        .map(Object::toString))
                .foldLeft(List.empty(), (Function2<List<String>, Vector<String>, List<String>>) List::appendAll);

        // Append the assign keys from the superclasses if any
        return assignKeys.appendAll(ctx.getClassByNameOrId(ci.getSuperclass())
                .map(sc -> allInheritedAssignKeys(ctx, sc, depth + 1))
                .getOrElse(List.empty()));
    }

    private static String topClass(final TransformationContext ctx, final StarClassTransform.ClassInstruction ci,
                                   final int depth) {
        //Check for *assign statements
        if (depth > MAX_RECURSION_DEPTH || ci == null) {
            return null;
        }
        return ctx.getClassByNameOrId(ci.getSuperclass())
                .map(sc -> topClass(ctx, sc, depth + 1))
                .getOrElse(ci.getSuperclass());
    }


}
