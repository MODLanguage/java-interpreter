package uk.modl.transforms;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import uk.modl.model.TopLevelConditional;

@RequiredArgsConstructor
public class ConditionalsTransform {
    /**
     * The context for this invocation of the interpreter
     */
    @NonNull
    private TransformationContext ctx;

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param tlc argument 1
     * @return the result of function application
     */
    public TopLevelConditional apply(final TopLevelConditional tlc) {
        // TODO:
        return tlc;
    }

    /**
     * @param ctx the TransformationContext
     */
    public void seCtx(final TransformationContext ctx) {
        this.ctx = ctx;
    }
}
