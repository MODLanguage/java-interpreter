package uk.modl.transforms;

import io.vavr.Function1;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import uk.modl.model.ArrayConditional;
import uk.modl.model.Structure;
import uk.modl.model.TopLevelConditional;

@RequiredArgsConstructor
public class ConditionalsTransform implements Function1<Structure, Structure> {
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
        // TODO:
        if (structure instanceof TopLevelConditional) {
            final TopLevelConditional tlc = (TopLevelConditional) structure;
        } else if (structure instanceof ArrayConditional) {
            // TODO:
            System.out.println("CONDITION: ArrayConditional");
        }
        return structure;
    }

    public void seCtx(final TransformationContext ctx) {
        this.ctx = ctx;
    }
}
