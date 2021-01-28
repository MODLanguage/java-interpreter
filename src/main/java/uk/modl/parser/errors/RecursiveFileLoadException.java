package uk.modl.parser.errors;

public class RecursiveFileLoadException extends InterpreterError {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public RecursiveFileLoadException(final String message) {
    super("Recursive file load detected on : " + message);
  }
}
