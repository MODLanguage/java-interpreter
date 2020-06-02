package uk.modl.transforms;

import io.vavr.Function2;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import uk.modl.model.Map;
import uk.modl.model.MapItem;
import uk.modl.model.Pair;
import uk.modl.model.Structure;

@RequiredArgsConstructor
public class StarMethodTransform implements Function2<TransformationContext, Structure, Tuple2<TransformationContext, Structure>> {

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
     * @return
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

        final String getNameOrId() {
            return (name == null) ? id : name;
        }

    }

}
