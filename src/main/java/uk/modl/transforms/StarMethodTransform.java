package uk.modl.transforms;

import io.vavr.Function1;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Value;
import uk.modl.model.Map;
import uk.modl.model.MapItem;
import uk.modl.model.Pair;
import uk.modl.model.Structure;
import uk.modl.parser.errors.InterpreterError;

@RequiredArgsConstructor
public class StarMethodTransform implements Function1<Structure, Structure> {

    /**
     * The context for this invocation of the interpreter
     */
    @NonNull
    @Setter
    private TransformationContext ctx;

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
     * @param p argument 1
     * @return the result of function application
     */
    public Structure apply(final Structure p) {
        if (p instanceof Pair && isMethodInstruction(((Pair) p).getKey())) {
            accept((Pair) p);
            return null;
        }
        return p;
    }

    /**
     * Extract a Method instruction from a Pair
     *
     * @param pair the Pair
     */
    private void accept(final Pair pair) {
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
                        case "*id":
                            id = p.getValue()
                                    .toString();
                            break;
                        case "*n":
                        case "*name":
                            name = p.getValue()
                                    .toString();
                            break;
                        case "*t":
                        case "*transform":
                            transform = p.getValue()
                                    .toString();
                            break;
                    }
                } else {
                    throw new InterpreterError("Expected a Pair but found a " + mi.getClass());
                }
            }

            final MethodInstruction m = MethodInstruction.of(id, name, transform);
            ctx.addMethodInstruction(m);
        } else {
            throw new InterpreterError("Expected a map for " + pair.getKey() + " but found a " + pair.getValue()
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

        final String getNameOrId() {
            return (name == null) ? id : name;
        }

    }

}
