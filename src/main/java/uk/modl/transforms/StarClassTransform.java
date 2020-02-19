package uk.modl.transforms;

import io.vavr.Function1;
import io.vavr.collection.List;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import uk.modl.model.*;
import uk.modl.parser.errors.InterpreterError;

@RequiredArgsConstructor
public class StarClassTransform implements Function1<Structure, Structure> {

    /**
     * The context for this invocation of the interpreter
     */
    @NonNull
    private TransformationContext ctx;

    /**
     * Check whether the key represents a *class instruction
     *
     * @param key the String to check
     * @return true if the key represents a class instruction
     */
    public static boolean isClassInstruction(final String key) {
        final String lowerCase = key.toLowerCase();
        return lowerCase
                .equals("*c") || lowerCase
                .equals("*class");
    }


    /**
     * Applies this function to one argument and returns the result.
     *
     * @param structure argument 1
     * @return the result of function application
     */
    @Override
    public Structure apply(final Structure structure) {
        if (structure instanceof Pair) {
            final Pair p = (Pair) structure;
            if (isClassInstruction(p.key)) {
                accept(p);
            }
        }
        return structure;
    }

    public void seCtx(final TransformationContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Extract a Class instruction from a Pair
     *
     * @param pair the Pair
     */
    private void accept(final Pair pair) {
        if (pair.value instanceof Map) {
            String id = null;
            String name = null;
            String superclass = null;
            PairValue assign = null;
            List<Pair> pairs = List.empty();

            for (final MapItem mi : ((Map) pair.value).mapItems) {
                if (mi instanceof Pair) {
                    final Pair p = (Pair) mi;
                    switch (p.key) {
                        case "*i":
                        case "*id":
                            id = p.value.toString();
                            break;
                        case "*n":
                        case "*name":
                            name = p.value.toString();
                            break;
                        case "*s":
                        case "*superclass":
                            superclass = p.value.toString();
                            break;
                        case "*a":
                        case "*assign":
                            assign = p.value;
                            break;
                        default:
                            pairs = pairs.append(p);
                    }
                } else {
                    throw new InterpreterError("Expected a Pair but found a " + mi.getClass());
                }
            }

            final ClassInstruction c = new ClassInstruction(id, name, superclass, assign, pairs);
            ctx.addClassInstruction(c);
        } else {
            throw new InterpreterError("Expected a map for " + pair.key + " but found a " + pair.value.getClass());
        }
    }

    @RequiredArgsConstructor
    @EqualsAndHashCode
    static class ClassInstruction {
        public final String id;
        public final String name;
        public final String superclass;
        public final PairValue assign;
        public final List<Pair> pairs;
    }

}
