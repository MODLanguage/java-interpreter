package uk.modl.error;

import io.vavr.collection.Array;
import lombok.ToString;


/**
 * Accumulate error messages.
 */
@ToString
public class Error {
    private Array<String> errors;

    public Error(final String message) {
        errors = Array.of(message);
    }
}
