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
			null, null, null, null, null, "'='", "';'", "','", "'('", "')'", "'['", 
			"']'"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\20\u00c0\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\3\2\6\2/\n\2\r\2\16\2\60"+
		"\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3<\n\3\3\4\3\4\3\4\3\4\3\4\3\4"+
		"\5\4D\n\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\5\5M\n\5\3\6\3\6\3\7\3\7\3\b\3\b"+
		"\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\5\r^\n\r\3\r\3\r\3\r\6\rc\n\r\r"+
		"\r\16\rd\5\rg\n\r\3\r\5\rj\n\r\3\16\3\16\3\16\7\16o\n\16\f\16\16\16r\13"+
		"\16\5\16t\n\16\3\17\3\17\5\17x\n\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20"+
		"\3\20\7\20\u0082\n\20\f\20\16\20\u0085\13\20\3\20\3\20\3\20\3\20\3\20"+
		"\5\20\u008c\n\20\3\21\3\21\3\22\7\22\u0091\n\22\f\22\16\22\u0094\13\22"+
		"\3\23\3\23\5\23\u0098\n\23\3\23\3\23\6\23\u009c\n\23\r\23\16\23\u009d"+
		"\3\23\6\23\u00a1\n\23\r\23\16\23\u00a2\3\23\3\23\6\23\u00a7\n\23\r\23"+
		"\16\23\u00a8\7\23\u00ab\n\23\f\23\16\23\u00ae\13\23\3\24\3\24\3\25\3\25"+
		"\3\26\3\26\5\26\u00b6\n\26\3\26\3\26\3\26\3\26\3\26\5\26\u00bd\n\26\5"+
		"\26\u00bf\n\26\2\2\27\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27"+
		"\r\31\16\33\2\35\2\37\17!\2#\2%\20\'\2)\2+\2\3\2\13\5\2\13\f\17\17\"\""+
		"\3\2\62;\3\2\63;\4\2GGgg\4\2--//\3\2$$\3\2bb\13\2\n\f\16\17\"\"$$*+=="+
		"??]_\u0080\u0080\t\2\61\61^^ddhhppttvw\2\u00d2\2\3\3\2\2\2\2\5\3\2\2\2"+
		"\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3"+
		"\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\37\3\2\2"+
		"\2\2%\3\2\2\2\3.\3\2\2\2\5;\3\2\2\2\7C\3\2\2\2\tL\3\2\2\2\13N\3\2\2\2"+
		"\rP\3\2\2\2\17R\3\2\2\2\21T\3\2\2\2\23V\3\2\2\2\25X\3\2\2\2\27Z\3\2\2"+
		"\2\31]\3\2\2\2\33s\3\2\2\2\35u\3\2\2\2\37\u008b\3\2\2\2!\u008d\3\2\2\2"+
		"#\u0092\3\2\2\2%\u0097\3\2\2\2\'\u00af\3\2\2\2)\u00b1\3\2\2\2+\u00be\3"+
		"\2\2\2-/\t\2\2\2.-\3\2\2\2/\60\3\2\2\2\60.\3\2\2\2\60\61\3\2\2\2\61\62"+
		"\3\2\2\2\62\63\b\2\2\2\63\4\3\2\2\2\64\65\7p\2\2\65\66\7w\2\2\66\67\7"+
		"n\2\2\67<\7n\2\289\7\62\2\29:\7\62\2\2:<\7\62\2\2;\64\3\2\2\2;8\3\2\2"+
		"\2<\6\3\2\2\2=>\7v\2\2>?\7t\2\2?@\7w\2\2@D\7g\2\2AB\7\62\2\2BD\7\63\2"+
		"\2C=\3\2\2\2CA\3\2\2\2D\b\3\2\2\2EF\7h\2\2FG\7c\2\2GH\7n\2\2HI\7u\2\2"+
		"IM\7g\2\2JK\7\62\2\2KM\7\62\2\2LE\3\2\2\2LJ\3\2\2\2M\n\3\2\2\2NO\7?\2"+
		"\2O\f\3\2\2\2PQ\7=\2\2Q\16\3\2\2\2RS\7.\2\2S\20\3\2\2\2TU\7*\2\2U\22\3"+
		"\2\2\2VW\7+\2\2W\24\3\2\2\2XY\7]\2\2Y\26\3\2\2\2Z[\7_\2\2[\30\3\2\2\2"+
		"\\^\7/\2\2]\\\3\2\2\2]^\3\2\2\2^_\3\2\2\2_f\5\33\16\2`b\7\60\2\2ac\t\3"+
		"\2\2ba\3\2\2\2cd\3\2\2\2db\3\2\2\2de\3\2\2\2eg\3\2\2\2f`\3\2\2\2fg\3\2"+
		"\2\2gi\3\2\2\2hj\5\35\17\2ih\3\2\2\2ij\3\2\2\2j\32\3\2\2\2kt\7\62\2\2"+
		"lp\t\4\2\2mo\t\3\2\2nm\3\2\2\2or\3\2\2\2pn\3\2\2\2pq\3\2\2\2qt\3\2\2\2"+
		"rp\3\2\2\2sk\3\2\2\2sl\3\2\2\2t\34\3\2\2\2uw\t\5\2\2vx\t\6\2\2wv\3\2\2"+
		"\2wx\3\2\2\2xy\3\2\2\2yz\5\33\16\2z\36\3\2\2\2{\u0083\7$\2\2|\u0082\5"+
		"!\21\2}~\7\u0080\2\2~\u0082\7$\2\2\177\u0080\7^\2\2\u0080\u0082\7$\2\2"+
		"\u0081|\3\2\2\2\u0081}\3\2\2\2\u0081\177\3\2\2\2\u0082\u0085\3\2\2\2\u0083"+
		"\u0081\3\2\2\2\u0083\u0084\3\2\2\2\u0084\u0086\3\2\2\2\u0085\u0083\3\2"+
		"\2\2\u0086\u008c\7$\2\2\u0087\u0088\7b\2\2\u0088\u0089\5#\22\2\u0089\u008a"+
		"\7b\2\2\u008a\u008c\3\2\2\2\u008b{\3\2\2\2\u008b\u0087\3\2\2\2\u008c "+
		"\3\2\2\2\u008d\u008e\n\7\2\2\u008e\"\3\2\2\2\u008f\u0091\n\b\2\2\u0090"+
		"\u008f\3\2\2\2\u0091\u0094\3\2\2\2\u0092\u0090\3\2\2\2\u0092\u0093\3\2"+
		"\2\2\u0093$\3\2\2\2\u0094\u0092\3\2\2\2\u0095\u0096\7%\2\2\u0096\u0098"+
		"\7\"\2\2\u0097\u0095\3\2\2\2\u0097\u0098\3\2\2\2\u0098\u009b\3\2\2\2\u0099"+
		"\u009c\5+\26\2\u009a\u009c\5\'\24\2\u009b\u0099\3\2\2\2\u009b\u009a\3"+
		"\2\2\2\u009c\u009d\3\2\2\2\u009d\u009b\3\2\2\2\u009d\u009e\3\2\2\2\u009e"+
		"\u00ac\3\2\2\2\u009f\u00a1\7\"\2\2\u00a0\u009f\3\2\2\2\u00a1\u00a2\3\2"+
		"\2\2\u00a2\u00a0\3\2\2\2\u00a2\u00a3\3\2\2\2\u00a3\u00a6\3\2\2\2\u00a4"+
		"\u00a7\5+\26\2\u00a5\u00a7\5\'\24\2\u00a6\u00a4\3\2\2\2\u00a6\u00a5\3"+
		"\2\2\2\u00a7\u00a8\3\2\2\2\u00a8\u00a6\3\2\2\2\u00a8\u00a9\3\2\2\2\u00a9"+
		"\u00ab\3\2\2\2\u00aa\u00a0\3\2\2\2\u00ab\u00ae\3\2\2\2\u00ac\u00aa\3\2"+
		"\2\2\u00ac\u00ad\3\2\2\2\u00ad&\3\2\2\2\u00ae\u00ac\3\2\2\2\u00af\u00b0"+
		"\n\t\2\2\u00b0(\3\2\2\2\u00b1\u00b2\t\t\2\2\u00b2*\3\2\2\2\u00b3\u00b5"+
		"\7^\2\2\u00b4\u00b6\t\n\2\2\u00b5\u00b4\3\2\2\2\u00b6\u00bf\3\2\2\2\u00b7"+
		"\u00b8\7^\2\2\u00b8\u00bf\5)\25\2\u00b9\u00bc\7\u0080\2\2\u00ba\u00bd"+
		"\5)\25\2\u00bb\u00bd\7w\2\2\u00bc\u00ba\3\2\2\2\u00bc\u00bb\3\2\2\2\u00bd"+
		"\u00bf\3\2\2\2\u00be\u00b3\3\2\2\2\u00be\u00b7\3\2\2\2\u00be\u00b9\3\2"+
		"\2\2\u00bf,\3\2\2\2\34\2\60;CL]dfipsw\u0081\u0083\u008b\u0092\u0097\u009b"+
		"\u009d\u00a2\u00a6\u00a8\u00ac\u00b5\u00bc\u00be\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}