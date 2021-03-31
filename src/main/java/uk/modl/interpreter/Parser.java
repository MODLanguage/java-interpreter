package uk.modl.interpreter;

import java.util.BitSet;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import uk.modl.model.Modl;
import uk.modl.parser.antlr.MODLLexer;
import uk.modl.parser.antlr.MODLParser;
import uk.modl.parser.antlr.MODLParser.ModlContext;

public class Parser {

  public Modl parse(@NonNull final String s) {
    final CodePointCharStream inputStream = CharStreams.fromString(s);
    final MODLLexer lexer = new MODLLexer(inputStream);
    final TokenStream commonTokenStream = new CommonTokenStream(lexer);
    final MODLParser parser = new MODLParser(commonTokenStream);

    lexer.addErrorListener(ThrowingErrorListener.INSTANCE);
    parser.addErrorListener(ThrowingErrorListener.INSTANCE);

    final ModlContext context = parser.modl();
    return visitModl(context);
  }

  /**
   * TODO:
   * 
   * @param context
   * @return
   */
  private Modl visitModl(ModlContext context) {
    return null;
  }

}

@Log4j2
class ThrowingErrorListener implements ANTLRErrorListener {
  public static final ThrowingErrorListener INSTANCE = new ThrowingErrorListener();

  @Override
  public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
      String msg, RecognitionException e) {
    if (recognizer != null) {
      final String message = String.format("line %d:%d %s %s", line, charPositionInLine, msg, offendingSymbol);
      throw new ParseCancellationException(message);
    } else {
      throw new ParseCancellationException("'recognizer' parameter not present");
    }
  }

  @Override
  public void reportAmbiguity(org.antlr.v4.runtime.Parser recognizer, DFA dfa, int startIndex, int stopIndex,
      boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
    log.warn("reportAmbiguity");
  }

  @Override
  public void reportAttemptingFullContext(org.antlr.v4.runtime.Parser recognizer, DFA dfa, int startIndex,
      int stopIndex, BitSet conflictingAlts, ATNConfigSet configs) {
    log.warn("reportAttemptingFullContext");
  }

  @Override
  public void reportContextSensitivity(org.antlr.v4.runtime.Parser recognizer, DFA dfa, int startIndex, int stopIndex,
      int prediction, ATNConfigSet configs) {
    log.warn("reportContextSensitivity");
  }
}