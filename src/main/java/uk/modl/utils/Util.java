package uk.modl.utils;

import io.vavr.Function1;
import io.vavr.collection.List;
import io.vavr.control.Either;
import uk.modl.error.Error;
import uk.modl.model.Array;
import uk.modl.model.PairValue;
import uk.modl.model.Primitive;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class Util {
    /**
     * Map a filename to Either an Error or the file contents as a String
     */
    public static Function1<String, Either<Error, String>> getFileContents = (filename) -> {
        try {
            return Either.right(String.join("", Files.readAllLines(Paths.get(filename))));
        } catch (final Throwable e) {
            return Either.left(new Error(e.getMessage()));
        }
    };

    /**
     * Map a PairValue to a list of Strings - for use as file names.
     */
    public static Function1<PairValue, List<String>> getFilenames = (pairValue) -> {
        if (pairValue instanceof Primitive) {
            final Primitive pv = (Primitive) pairValue;
            return List.of(pv.toString());
        }
        if (pairValue instanceof Array) {
            final Array a = (Array) pairValue;
            return List.ofAll(a.arrayItems.map(Objects::toString));
        }
        return List.of("TODO");
    };
}
