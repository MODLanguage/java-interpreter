package uk.modl.error;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Accumulate error messages.
 */
@ToString
public class Error {
    private List<String> errors;

    public Error() {
        errors = new ArrayList<>();
    }

    public Error(final String message) {
        this();
        errors.add(message);
    }
}
