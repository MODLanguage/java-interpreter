package uk.modl.parser.errors;

public class InterpreterError extends RuntimeException {

    public InterpreterError() {
    }

    public InterpreterError(String message) {
        super(message);
    }

    public InterpreterError(String message, Throwable cause) {
        super(message, cause);
    }

    public InterpreterError(Throwable cause) {
        super(cause);
    }

    public InterpreterError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
