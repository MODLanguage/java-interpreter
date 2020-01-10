package uk.modl.transforms;

import io.vavr.Function1;
import io.vavr.control.Either;
import uk.modl.model.Modl;

public class StarLoadTransform implements Function1<Either<Throwable, Modl>, Either<Throwable, Modl>> {
    /**
     * Applies this function to one argument and returns the result.
     *
     * @param modl argument 1
     * @return the result of function application
     */
    @Override
    public Either<Throwable, Modl> apply(final Either<Throwable, Modl> modl) {
        // TODO: currently a no-op
        return modl;
    }

}
