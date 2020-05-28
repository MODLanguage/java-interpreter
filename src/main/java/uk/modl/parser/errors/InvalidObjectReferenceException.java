package uk.modl.parser.errors;

public class InvalidObjectReferenceException extends RuntimeException {

    public InvalidObjectReferenceException() {
    }

    public InvalidObjectReferenceException(String message) {
        super(message);
    }

    public InvalidObjectReferenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidObjectReferenceException(Throwable cause) {
        super(cause);
    }

    public InvalidObjectReferenceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
