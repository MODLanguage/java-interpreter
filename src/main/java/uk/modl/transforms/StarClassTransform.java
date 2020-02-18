package uk.modl.transforms;

import io.vavr.Function1;
import uk.modl.model.Structure;

public class StarClassTransform implements Function1<Structure, Structure> {
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

}
