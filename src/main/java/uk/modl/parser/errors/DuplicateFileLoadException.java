package uk.modl.parser.errors;

public class DuplicateFileLoadException extends InterpreterError {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public DuplicateFileLoadException(final String message) {
    super("Duplicate file load detected on : " + message);
  }
}
