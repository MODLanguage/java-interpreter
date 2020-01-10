package uk.modl.utils;

import io.vavr.Function1;
import io.vavr.control.Either;
import uk.modl.error.Error;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Util {
    /**
     * Map a filename to Either an Error or the file contents as a String
     */
    public static Function1<String, Either<Error, String>> getFileContents = (filename) -> {
        try {
            return Either.right(Files.readAllLines(Paths.get(filename))
                    .stream()
                    .collect(Collectors.joining()));
        } catch (final Throwable e) {
            return Either.left(new Error(e.getMessage()));
        }
    };
}
