package uk.modl.error;

import io.vavr.collection.Array;
import lombok.ToString;


/**
 * Accumulate error messages.
 */
@ToString
public class Error {
    private Array<String> errors;

    public Error() {
        errors = Array.empty();
    }

    public Error(final String message) {
        errors = Array.of(message);
    }

    public void append(final String message) {
        errors = errors.append(message);
    }

    public boolean isEmpty() {
        return errors.isEmpty();
    }
}
