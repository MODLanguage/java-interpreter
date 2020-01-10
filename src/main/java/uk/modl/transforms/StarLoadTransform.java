package uk.modl.transforms;

import io.vavr.Function1;
import io.vavr.control.Either;
import uk.modl.error.Error;
import uk.modl.model.Modl;

public class StarLoadTransform implements Function1<Either<Error, Modl>, Either<Error, Modl>> {
    /**
     * Applies this function to one argument and returns the result.
     *
     * @param modl argument 1
     * @return the result of function application
     */
    @Override
    public Either<Error, Modl> apply(final Either<Error, Modl> modl) {
        // TODO: currently a no-op
        return modl;
    }

}
