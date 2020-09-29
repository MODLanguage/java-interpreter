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
import lombok.val;
import lombok.var;
import uk.modl.model.Array;
import uk.modl.model.Map;
import uk.modl.model.*;
import uk.modl.transforms.StarClassTransform;
import uk.modl.transforms.TransformationContext;

import java.util.Objects;

import static io.vavr.API.*;
import static io.vavr.Predicates.*;

public class SupertypeInference {

    private static final int MAX_RECURSION_DEPTH = 50;

    public static String inferType(final TransformationContext ctx, final StarClassTransform.ClassInstruction ci, final PairValue pv) {
        var tc = topClass(ctx, ci, 0);
        if (tc == null) {
            if (hasAssignStatement(ctx, ci)) {
                tc = allAssignmentKeysAreClasses(ctx, ci) ? "arr" : "map";
            } else {
                tc = hasInheritedPairs(ctx, ci, 0) ? "map" :
                        Match(pv).of(
                                Case($(instanceOf(StringPrimitive.class)), "str"),
                                Case($(instanceOf(NumberPrimitive.class)), "num"),
                                Case($(instanceOf(TruePrimitive.class)), "bool"),
                                Case($(instanceOf(FalsePrimitive.class)), "bool"),
                                Case($(instanceOf(NullPrimitive.class)), "null"),
                                Case($(instanceOf(Array.class)), "arr"),
                                Case($(instanceOf(Map.class)), "map"),
                                Case($(instanceOf(Pair.class)), "map"),
                                Case($(), () -> {
                                    throw new NullPointerException("Unknown object type: " + pv.getClass());
                                })
                        );
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
        val allInheritedAssignKeys = allInheritedAssignKeys(ctx, ci, 0);
        var allHaveAssigns = allAssignClassesHaveAssigns(ctx, allInheritedAssignKeys);

        val allAssignKeysAreClasses = allInheritedAssignKeys.map(ctx::getClassByNameOrId)
                .count(Objects::isNull) == 0;
        return allHaveAssigns && allAssignKeysAreClasses;
    }

    private static boolean allAssignClassesHaveAssigns(final TransformationContext ctx, final List<String> allInheritedAssignKeys) {
        val expected = allInheritedAssignKeys.size();

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
        val assignKeys = ci.getAssign()
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
