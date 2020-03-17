package uk.modl.transforms;

import io.vavr.Function1;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import uk.modl.model.*;

@RequiredArgsConstructor
public class ClassExpansionTransform implements Function1<Pair, Pair> {

    /**
     * The context for this invocation of the interpreter
     */
    @NonNull
    private TransformationContext ctx;

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param p argument 1
     * @return the result of function application
     */
    @Override
    public Pair apply(final Pair p) {
        final Option<StarClassTransform.ClassInstruction> maybeClass = ctx.getClassByNameOrId(p.key);
        if (maybeClass.isDefined()) {
            return accept(p, maybeClass.get());
        }
        return p;
    }

    public void seCtx(final TransformationContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Expand a Pair if it matches a class
     *
     * @param p  the Pair
     * @param ci the class to transform to
     * @return the expanded Pair
     */
    private Pair accept(final Pair p, final StarClassTransform.ClassInstruction ci) {
        final String newKey = StringUtils.isEmpty(ci.name) ? ci.id : ci.name;
        PairValue pairValue = null;

        // TODO: ALL of this!
        switch (ci.superclass) {
            case "map":
                pairValue = p.value;
                break;
            case "arr":
                pairValue = new Array(Vector.empty());
                break;
            case "str":
                pairValue = new StringPrimitive(p.value.toString());
                break;
            case "num":
                pairValue = new NumberPrimitive(p.value.numericValue()
                        .toString());
                break;
            default:
                pairValue = inherit(ci.superclass, p.value);
        }
        return new Pair(newKey, pairValue);
    }

    private PairValue inherit(final String superclass, final PairValue value) {
        // TODO:
        return value;
    }

}
