// Generated from MODLLexer.g4 by ANTLR 4.7.2
package uk.modl.parser.antlr;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MODLLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		WS=1, NULL=2, TRUE=3, FALSE=4, EQUALS=5, STRUCT_SEP=6, ARR_SEP=7, LBRAC=8, 
		RBRAC=9, LSBRAC=10, RSBRAC=11, NUMBER=12, QUOTED=13, STRING=14;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"WS", "NULL", "TRUE", "FALSE", "EQUALS", "STRUCT_SEP", "ARR_SEP", "LBRAC", 
			"RBRAC", "LSBRAC", "RSBRAC", "NUMBER", "INT", "EXP", "QUOTED", "INSIDE_QUOTES", 
			"INSIDE_GRAVES", "STRING", "UNRESERVED", "RESERVED_CHARS", "ESCAPED"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, "'null'", "'true'", "'false'", "'='", "';'", "','", "'('", 
			"')'", "'['", "']'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "WS", "NULL", "TRUE", "FALSE", "EQUALS", "STRUCT_SEP", "ARR_SEP", 
			"LBRAC", "RBRAC", "LSBRAC", "RSBRAC", "NUMBER", "QUOTED", "STRING"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public MODLLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "MODLLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\20\u00ba\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\3\2\6\2/\n\2\r\2\16\2\60"+
		"\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3"+
		"\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\5\rT"+
		"\n\r\3\r\3\r\3\r\6\rY\n\r\r\r\16\rZ\5\r]\n\r\3\r\5\r`\n\r\3\16\3\16\3"+
		"\16\7\16e\n\16\f\16\16\16h\13\16\5\16j\n\16\3\17\3\17\5\17n\n\17\3\17"+
		"\3\17\3\20\3\20\3\20\3\20\3\20\3\20\7\20x\n\20\f\20\16\20{\13\20\3\20"+
		"\3\20\3\20\3\20\3\20\3\20\3\20\7\20\u0084\n\20\f\20\16\20\u0087\13\20"+
		"\3\20\5\20\u008a\n\20\3\21\3\21\3\22\3\22\3\23\3\23\5\23\u0092\n\23\3"+
		"\23\3\23\6\23\u0096\n\23\r\23\16\23\u0097\3\23\6\23\u009b\n\23\r\23\16"+
		"\23\u009c\3\23\3\23\6\23\u00a1\n\23\r\23\16\23\u00a2\7\23\u00a5\n\23\f"+
		"\23\16\23\u00a8\13\23\3\24\3\24\3\25\3\25\3\26\3\26\5\26\u00b0\n\26\3"+
		"\26\3\26\3\26\3\26\3\26\5\26\u00b7\n\26\5\26\u00b9\n\26\2\2\27\3\3\5\4"+
		"\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\2\35\2\37\17!\2#"+
		"\2%\20\'\2)\2+\2\3\2\13\5\2\13\f\17\17\"\"\3\2\62;\3\2\63;\4\2GGgg\4\2"+
		"--//\3\2$$\3\2bb\f\2\n\f\16\17\"\"$$*+==??]_bb\u0080\u0080\t\2\61\61^"+
		"^ddhhppttvw\2\u00cb\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2"+
		"\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3"+
		"\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\37\3\2\2\2\2%\3\2\2\2\3.\3\2\2\2\5"+
		"\64\3\2\2\2\79\3\2\2\2\t>\3\2\2\2\13D\3\2\2\2\rF\3\2\2\2\17H\3\2\2\2\21"+
		"J\3\2\2\2\23L\3\2\2\2\25N\3\2\2\2\27P\3\2\2\2\31S\3\2\2\2\33i\3\2\2\2"+
		"\35k\3\2\2\2\37\u0089\3\2\2\2!\u008b\3\2\2\2#\u008d\3\2\2\2%\u0091\3\2"+
		"\2\2\'\u00a9\3\2\2\2)\u00ab\3\2\2\2+\u00b8\3\2\2\2-/\t\2\2\2.-\3\2\2\2"+
		"/\60\3\2\2\2\60.\3\2\2\2\60\61\3\2\2\2\61\62\3\2\2\2\62\63\b\2\2\2\63"+
		"\4\3\2\2\2\64\65\7p\2\2\65\66\7w\2\2\66\67\7n\2\2\678\7n\2\28\6\3\2\2"+
		"\29:\7v\2\2:;\7t\2\2;<\7w\2\2<=\7g\2\2=\b\3\2\2\2>?\7h\2\2?@\7c\2\2@A"+
		"\7n\2\2AB\7u\2\2BC\7g\2\2C\n\3\2\2\2DE\7?\2\2E\f\3\2\2\2FG\7=\2\2G\16"+
		"\3\2\2\2HI\7.\2\2I\20\3\2\2\2JK\7*\2\2K\22\3\2\2\2LM\7+\2\2M\24\3\2\2"+
		"\2NO\7]\2\2O\26\3\2\2\2PQ\7_\2\2Q\30\3\2\2\2RT\7/\2\2SR\3\2\2\2ST\3\2"+
		"\2\2TU\3\2\2\2U\\\5\33\16\2VX\7\60\2\2WY\t\3\2\2XW\3\2\2\2YZ\3\2\2\2Z"+
		"X\3\2\2\2Z[\3\2\2\2[]\3\2\2\2\\V\3\2\2\2\\]\3\2\2\2]_\3\2\2\2^`\5\35\17"+
		"\2_^\3\2\2\2_`\3\2\2\2`\32\3\2\2\2aj\7\62\2\2bf\t\4\2\2ce\t\3\2\2dc\3"+
		"\2\2\2eh\3\2\2\2fd\3\2\2\2fg\3\2\2\2gj\3\2\2\2hf\3\2\2\2ia\3\2\2\2ib\3"+
		"\2\2\2j\34\3\2\2\2km\t\5\2\2ln\t\6\2\2ml\3\2\2\2mn\3\2\2\2no\3\2\2\2o"+
		"p\5\33\16\2p\36\3\2\2\2qy\7$\2\2rx\5!\21\2st\7\u0080\2\2tx\7$\2\2uv\7"+
		"^\2\2vx\7$\2\2wr\3\2\2\2ws\3\2\2\2wu\3\2\2\2x{\3\2\2\2yw\3\2\2\2yz\3\2"+
		"\2\2z|\3\2\2\2{y\3\2\2\2|\u008a\7$\2\2}\u0085\7b\2\2~\u0084\5#\22\2\177"+
		"\u0080\7\u0080\2\2\u0080\u0084\7b\2\2\u0081\u0082\7^\2\2\u0082\u0084\7"+
		"b\2\2\u0083~\3\2\2\2\u0083\177\3\2\2\2\u0083\u0081\3\2\2\2\u0084\u0087"+
		"\3\2\2\2\u0085\u0083\3\2\2\2\u0085\u0086\3\2\2\2\u0086\u0088\3\2\2\2\u0087"+
		"\u0085\3\2\2\2\u0088\u008a\7b\2\2\u0089q\3\2\2\2\u0089}\3\2\2\2\u008a"+
		" \3\2\2\2\u008b\u008c\n\7\2\2\u008c\"\3\2\2\2\u008d\u008e\n\b\2\2\u008e"+
		"$\3\2\2\2\u008f\u0090\7%\2\2\u0090\u0092\7\"\2\2\u0091\u008f\3\2\2\2\u0091"+
		"\u0092\3\2\2\2\u0092\u0095\3\2\2\2\u0093\u0096\5+\26\2\u0094\u0096\5\'"+
		"\24\2\u0095\u0093\3\2\2\2\u0095\u0094\3\2\2\2\u0096\u0097\3\2\2\2\u0097"+
		"\u0095\3\2\2\2\u0097\u0098\3\2\2\2\u0098\u00a6\3\2\2\2\u0099\u009b\7\""+
		"\2\2\u009a\u0099\3\2\2\2\u009b\u009c\3\2\2\2\u009c\u009a\3\2\2\2\u009c"+
		"\u009d\3\2\2\2\u009d\u00a0\3\2\2\2\u009e\u00a1\5+\26\2\u009f\u00a1\5\'"+
		"\24\2\u00a0\u009e\3\2\2\2\u00a0\u009f\3\2\2\2\u00a1\u00a2\3\2\2\2\u00a2"+
		"\u00a0\3\2\2\2\u00a2\u00a3\3\2\2\2\u00a3\u00a5\3\2\2\2\u00a4\u009a\3\2"+
		"\2\2\u00a5\u00a8\3\2\2\2\u00a6\u00a4\3\2\2\2\u00a6\u00a7\3\2\2\2\u00a7"+
		"&\3\2\2\2\u00a8\u00a6\3\2\2\2\u00a9\u00aa\n\t\2\2\u00aa(\3\2\2\2\u00ab"+
		"\u00ac\t\t\2\2\u00ac*\3\2\2\2\u00ad\u00af\7^\2\2\u00ae\u00b0\t\n\2\2\u00af"+
		"\u00ae\3\2\2\2\u00b0\u00b9\3\2\2\2\u00b1\u00b2\7^\2\2\u00b2\u00b9\5)\25"+
		"\2\u00b3\u00b6\7\u0080\2\2\u00b4\u00b7\5)\25\2\u00b5\u00b7\7w\2\2\u00b6"+
		"\u00b4\3\2\2\2\u00b6\u00b5\3\2\2\2\u00b7\u00b9\3\2\2\2\u00b8\u00ad\3\2"+
		"\2\2\u00b8\u00b1\3\2\2\2\u00b8\u00b3\3\2\2\2\u00b9,\3\2\2\2\32\2\60SZ"+
		"\\_fimwy\u0083\u0085\u0089\u0091\u0095\u0097\u009c\u00a0\u00a2\u00a6\u00af"+
		"\u00b6\u00b8\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}