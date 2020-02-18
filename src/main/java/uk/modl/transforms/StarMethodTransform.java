package uk.modl.transforms;

import io.vavr.Function1;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import uk.modl.model.Structure;

@RequiredArgsConstructor
public class StarMethodTransform implements Function1<Structure, Structure> {

    /**
     * The context for this invocation of the interpreter
     */
    @NonNull
    private TransformationContext ctx;

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param structure argument 1
     * @return the result of function application
     */
    @Override
    public Structure apply(final Structure structure) {
        // TODO: currently a no-op
        return structure;
    }

    public void seCtx(final TransformationContext ctx) {
        this.ctx = ctx;
    }
}
