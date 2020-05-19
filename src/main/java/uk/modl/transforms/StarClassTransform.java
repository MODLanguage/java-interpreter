package uk.modl.transforms;

import io.vavr.Function1;
import io.vavr.collection.Vector;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import uk.modl.model.*;
import uk.modl.parser.errors.InterpreterError;

@RequiredArgsConstructor
public class StarClassTransform implements Function1<Pair, Pair> {

    /**
     * The context for this invocation of the interpreter
     */
    @NonNull
    @Setter
    private TransformationContext ctx;

    /**
     * Check whether the key represents a *class instruction
     *
     * @param key the String to check
     * @return true if the key represents a class instruction
     */
    public static boolean isClassInstruction(final @NonNull String key) {
        return StringUtils.equalsAnyIgnoreCase(key, "*c", "*class");
    }


    /**
     * Applies this function to one argument and returns the result.
     *
     * @param p argument 1
     * @return the result of function application
     */
    public Pair apply(final @NonNull Pair p) {
        if (isClassInstruction(p.getKey())) {
            accept(p);
            return null;
        }
        return p;
    }

    /**
     * Extract a Class instruction from a Pair
     *
     * @param pair the Pair
     */
    private void accept(final @NonNull Pair pair) {
        if (pair.getValue() instanceof Map) {
            String id = null;
            String name = null;
            String superclass = null;
            Vector<ArrayItem> assign = null;
            var pairs = Vector.<Pair>empty();

            for (final MapItem mi : ((Map) pair.getValue()).getMapItems()) {
                if (mi instanceof Pair) {
                    final Pair p = (Pair) mi;
                    switch (p.getKey()
                            .toLowerCase()) {
                        case "*i":
                        case "*id":
                            id = p.getValue()
                                    .toString();
                            break;
                        case "*n":
                        case "*name":
                            name = p.getValue()
                                    .toString();
                            break;
                        case "*s":
                        case "*superclass":
                            superclass = p.getValue()
                                    .toString();
                            break;
                        case "*a":
                        case "*assign":
                            if (p.getValue() instanceof Array) {
                                assign = ((Array) p.getValue()).getArrayItems()
                                        .map(ai -> {
                                            if (ai instanceof Array) {
                                                return ai;
                                            } else {
                                                throw new InterpreterError("*assign statement should be an Array of Arrays");
                                            }
                                        });
                            } else {
                                throw new InterpreterError("*assign statement should be an Array of Arrays");
                            }
                            break;
                        default:
                            pairs = pairs.append(p);
                    }
                } else {
                    throw new InterpreterError("Expected a Pair but found a " + mi.getClass());
                }
            }

            ctx.addClassInstruction(ClassInstruction.of(id, name, superclass, assign, pairs));
        } else {
            throw new InterpreterError("Expected a map for " + pair.getKey() + " but found a " + pair.getValue()
                    .getClass());
        }
    }

    @Value(staticConstructor = "of")
    static class ClassInstruction {

        String id;

        String name;

        String superclass;

        Vector<ArrayItem> assign;

        Vector<Pair> pairs;

    }

}
