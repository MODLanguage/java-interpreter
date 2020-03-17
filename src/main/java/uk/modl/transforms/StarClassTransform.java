package uk.modl.transforms;

import io.vavr.Function1;
import io.vavr.collection.Vector;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import uk.modl.model.Map;
import uk.modl.model.MapItem;
import uk.modl.model.Pair;
import uk.modl.model.PairValue;
import uk.modl.parser.errors.InterpreterError;

@RequiredArgsConstructor
public class StarClassTransform implements Function1<Pair, Pair> {

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
     * @param p argument 1
     * @return the result of function application
     */
    public Pair apply(final Pair p) {
        if (isClassInstruction(p.key)) {
            accept(p);
        }
        return p;
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
            Vector<Pair> pairs = Vector.empty();

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

            if (superclass == null) {
                superclass = "map";// TODO: implement superclass inference
            }
            final ClassInstruction c = new ClassInstruction(id, name, superclass, assign, pairs);
            ctx.addClassInstruction(c);
        } else {
            throw new InterpreterError("Expected a map for " + pair.key + " but found a " + pair.value.getClass());
        }
    }

    @RequiredArgsConstructor
    @EqualsAndHashCode
    @ToString
    static class ClassInstruction {
        public final String id;
        public final String name;
        public final String superclass;
        public final PairValue assign;
        public final Vector<Pair> pairs;
    }

}
