package uk.modl.transforms;

import io.vavr.Function1;
import uk.modl.model.Modl;

public class StarClassTransform implements Function1<Modl, Modl> {
    /**
     * Applies this function to one argument and returns the result.
     *
     * @param modl argument 1
     * @return the result of function application
     */
    @Override
    public Modl apply(final Modl modl) {
        // TODO: currently a no-op
        return modl;
    }

}
