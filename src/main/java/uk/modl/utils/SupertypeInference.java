package uk.modl.utils;

import io.vavr.Function2;
import io.vavr.collection.List;
import io.vavr.collection.Vector;
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
                    tc = "map";
                } else {
                    tc = "arr";
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
        return allInheritedAssignKeys(ctx, ci, 0).map(ctx::getClassByNameOrId)
                .count(Objects::isNull) == 0;
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
