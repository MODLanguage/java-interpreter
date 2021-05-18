// Generated from MODLParser.g4 by ANTLR 4.7.2
package uk.modl.parser.antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MODLParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		WS=1, NULL=2, TRUE=3, FALSE=4, EQUALS=5, STRUCT_SEP=6, ARR_SEP=7, LBRAC=8, 
		RBRAC=9, LSBRAC=10, RSBRAC=11, NUMBER=12, QUOTED=13, STRING=14;
	public static final int
		RULE_modl = 0, RULE_modl_structure = 1, RULE_modl_map = 2, RULE_modl_array = 3, 
		RULE_modl_pair = 4, RULE_modl_value = 5, RULE_modl_primitive = 6;
	private static String[] makeRuleNames() {
		return new String[] {
			"modl", "modl_structure", "modl_map", "modl_array", "modl_pair", "modl_value", 
			"modl_primitive"
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

	@Override
	public String getGrammarFileName() { return "MODLParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MODLParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ModlContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(MODLParser.EOF, 0); }
		public Modl_primitiveContext modl_primitive() {
			return getRuleContext(Modl_primitiveContext.class,0);
		}
		public List<Modl_structureContext> modl_structure() {
			return getRuleContexts(Modl_structureContext.class);
		}
		public Modl_structureContext modl_structure(int i) {
			return getRuleContext(Modl_structureContext.class,i);
		}
		public List<TerminalNode> STRUCT_SEP() { return getTokens(MODLParser.STRUCT_SEP); }
		public TerminalNode STRUCT_SEP(int i) {
			return getToken(MODLParser.STRUCT_SEP, i);
		}
		public ModlContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterModl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitModl(this);
		}
	}

	public final ModlContext modl() throws RecognitionException {
		ModlContext _localctx = new ModlContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_modl);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(26);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				{
				{
				{
				setState(14);
				modl_structure();
				setState(19);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(15);
						match(STRUCT_SEP);
						setState(16);
						modl_structure();
						}
						} 
					}
					setState(21);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
				}
				}
				setState(23);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==STRUCT_SEP) {
					{
					setState(22);
					match(STRUCT_SEP);
					}
				}

				}
				}
				break;
			case 2:
				{
				setState(25);
				modl_primitive();
				}
				break;
			}
			setState(28);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Modl_structureContext extends ParserRuleContext {
		public Modl_mapContext modl_map() {
			return getRuleContext(Modl_mapContext.class,0);
		}
		public Modl_arrayContext modl_array() {
			return getRuleContext(Modl_arrayContext.class,0);
		}
		public Modl_pairContext modl_pair() {
			return getRuleContext(Modl_pairContext.class,0);
		}
		public Modl_structureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modl_structure; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterModl_structure(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitModl_structure(this);
		}
	}

	public final Modl_structureContext modl_structure() throws RecognitionException {
		Modl_structureContext _localctx = new Modl_structureContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_modl_structure);
		try {
			setState(33);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LBRAC:
				enterOuterAlt(_localctx, 1);
				{
				setState(30);
				modl_map();
				}
				break;
			case LSBRAC:
				enterOuterAlt(_localctx, 2);
				{
				setState(31);
				modl_array();
				}
				break;
			case QUOTED:
			case STRING:
				enterOuterAlt(_localctx, 3);
				{
				setState(32);
				modl_pair();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Modl_mapContext extends ParserRuleContext {
		public TerminalNode LBRAC() { return getToken(MODLParser.LBRAC, 0); }
		public TerminalNode RBRAC() { return getToken(MODLParser.RBRAC, 0); }
		public List<Modl_pairContext> modl_pair() {
			return getRuleContexts(Modl_pairContext.class);
		}
		public Modl_pairContext modl_pair(int i) {
			return getRuleContext(Modl_pairContext.class,i);
		}
		public List<TerminalNode> STRUCT_SEP() { return getTokens(MODLParser.STRUCT_SEP); }
		public TerminalNode STRUCT_SEP(int i) {
			return getToken(MODLParser.STRUCT_SEP, i);
		}
		public Modl_mapContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modl_map; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterModl_map(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitModl_map(this);
		}
	}

	public final Modl_mapContext modl_map() throws RecognitionException {
		Modl_mapContext _localctx = new Modl_mapContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_modl_map);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(35);
			match(LBRAC);
			setState(44);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==QUOTED || _la==STRING) {
				{
				setState(36);
				modl_pair();
				setState(41);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==STRUCT_SEP) {
					{
					{
					setState(37);
					match(STRUCT_SEP);
					setState(38);
					modl_pair();
					}
					}
					setState(43);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(46);
			match(RBRAC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Modl_arrayContext extends ParserRuleContext {
		public TerminalNode LSBRAC() { return getToken(MODLParser.LSBRAC, 0); }
		public TerminalNode RSBRAC() { return getToken(MODLParser.RSBRAC, 0); }
		public List<Modl_valueContext> modl_value() {
			return getRuleContexts(Modl_valueContext.class);
		}
		public Modl_valueContext modl_value(int i) {
			return getRuleContext(Modl_valueContext.class,i);
		}
		public List<TerminalNode> STRUCT_SEP() { return getTokens(MODLParser.STRUCT_SEP); }
		public TerminalNode STRUCT_SEP(int i) {
			return getToken(MODLParser.STRUCT_SEP, i);
		}
		public Modl_arrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modl_array; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterModl_array(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitModl_array(this);
		}
	}

	public final Modl_arrayContext modl_array() throws RecognitionException {
		Modl_arrayContext _localctx = new Modl_arrayContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_modl_array);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(48);
			match(LSBRAC);
			setState(57);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NULL) | (1L << TRUE) | (1L << FALSE) | (1L << LBRAC) | (1L << LSBRAC) | (1L << NUMBER) | (1L << QUOTED) | (1L << STRING))) != 0)) {
				{
				setState(49);
				modl_value();
				setState(54);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==STRUCT_SEP) {
					{
					{
					setState(50);
					match(STRUCT_SEP);
					setState(51);
					modl_value();
					}
					}
					setState(56);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(59);
			match(RSBRAC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Modl_pairContext extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(MODLParser.EQUALS, 0); }
		public Modl_valueContext modl_value() {
			return getRuleContext(Modl_valueContext.class,0);
		}
		public TerminalNode STRING() { return getToken(MODLParser.STRING, 0); }
		public TerminalNode QUOTED() { return getToken(MODLParser.QUOTED, 0); }
		public Modl_mapContext modl_map() {
			return getRuleContext(Modl_mapContext.class,0);
		}
		public Modl_arrayContext modl_array() {
			return getRuleContext(Modl_arrayContext.class,0);
		}
		public Modl_pairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modl_pair; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterModl_pair(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitModl_pair(this);
		}
	}

	public final Modl_pairContext modl_pair() throws RecognitionException {
		Modl_pairContext _localctx = new Modl_pairContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_modl_pair);
		int _la;
		try {
			setState(68);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(61);
				_la = _input.LA(1);
				if ( !(_la==QUOTED || _la==STRING) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(62);
				match(EQUALS);
				setState(63);
				modl_value();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(64);
				match(STRING);
				setState(65);
				modl_map();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(66);
				match(STRING);
				setState(67);
				modl_array();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Modl_valueContext extends ParserRuleContext {
		public Modl_mapContext modl_map() {
			return getRuleContext(Modl_mapContext.class,0);
		}
		public Modl_arrayContext modl_array() {
			return getRuleContext(Modl_arrayContext.class,0);
		}
		public Modl_pairContext modl_pair() {
			return getRuleContext(Modl_pairContext.class,0);
		}
		public Modl_primitiveContext modl_primitive() {
			return getRuleContext(Modl_primitiveContext.class,0);
		}
		public Modl_valueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modl_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterModl_value(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitModl_value(this);
		}
	}

	public final Modl_valueContext modl_value() throws RecognitionException {
		Modl_valueContext _localctx = new Modl_valueContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_modl_value);
		try {
			setState(74);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(70);
				modl_map();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(71);
				modl_array();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(72);
				modl_pair();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(73);
				modl_primitive();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Modl_primitiveContext extends ParserRuleContext {
		public TerminalNode QUOTED() { return getToken(MODLParser.QUOTED, 0); }
		public TerminalNode NUMBER() { return getToken(MODLParser.NUMBER, 0); }
		public TerminalNode STRING() { return getToken(MODLParser.STRING, 0); }
		public TerminalNode TRUE() { return getToken(MODLParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(MODLParser.FALSE, 0); }
		public TerminalNode NULL() { return getToken(MODLParser.NULL, 0); }
		public Modl_primitiveContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modl_primitive; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterModl_primitive(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitModl_primitive(this);
		}
	}

	public final Modl_primitiveContext modl_primitive() throws RecognitionException {
		Modl_primitiveContext _localctx = new Modl_primitiveContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_modl_primitive);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(76);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NULL) | (1L << TRUE) | (1L << FALSE) | (1L << NUMBER) | (1L << QUOTED) | (1L << STRING))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\20Q\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\3\2\3\2\3\2\7\2\24\n\2\f\2"+
		"\16\2\27\13\2\3\2\5\2\32\n\2\3\2\5\2\35\n\2\3\2\3\2\3\3\3\3\3\3\5\3$\n"+
		"\3\3\4\3\4\3\4\3\4\7\4*\n\4\f\4\16\4-\13\4\5\4/\n\4\3\4\3\4\3\5\3\5\3"+
		"\5\3\5\7\5\67\n\5\f\5\16\5:\13\5\5\5<\n\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\5\6G\n\6\3\7\3\7\3\7\3\7\5\7M\n\7\3\b\3\b\3\b\2\2\t\2\4\6\b\n"+
		"\f\16\2\4\3\2\17\20\4\2\4\6\16\20\2W\2\34\3\2\2\2\4#\3\2\2\2\6%\3\2\2"+
		"\2\b\62\3\2\2\2\nF\3\2\2\2\fL\3\2\2\2\16N\3\2\2\2\20\25\5\4\3\2\21\22"+
		"\7\b\2\2\22\24\5\4\3\2\23\21\3\2\2\2\24\27\3\2\2\2\25\23\3\2\2\2\25\26"+
		"\3\2\2\2\26\31\3\2\2\2\27\25\3\2\2\2\30\32\7\b\2\2\31\30\3\2\2\2\31\32"+
		"\3\2\2\2\32\35\3\2\2\2\33\35\5\16\b\2\34\20\3\2\2\2\34\33\3\2\2\2\35\36"+
		"\3\2\2\2\36\37\7\2\2\3\37\3\3\2\2\2 $\5\6\4\2!$\5\b\5\2\"$\5\n\6\2# \3"+
		"\2\2\2#!\3\2\2\2#\"\3\2\2\2$\5\3\2\2\2%.\7\n\2\2&+\5\n\6\2\'(\7\b\2\2"+
		"(*\5\n\6\2)\'\3\2\2\2*-\3\2\2\2+)\3\2\2\2+,\3\2\2\2,/\3\2\2\2-+\3\2\2"+
		"\2.&\3\2\2\2./\3\2\2\2/\60\3\2\2\2\60\61\7\13\2\2\61\7\3\2\2\2\62;\7\f"+
		"\2\2\638\5\f\7\2\64\65\7\b\2\2\65\67\5\f\7\2\66\64\3\2\2\2\67:\3\2\2\2"+
		"8\66\3\2\2\289\3\2\2\29<\3\2\2\2:8\3\2\2\2;\63\3\2\2\2;<\3\2\2\2<=\3\2"+
		"\2\2=>\7\r\2\2>\t\3\2\2\2?@\t\2\2\2@A\7\7\2\2AG\5\f\7\2BC\7\20\2\2CG\5"+
		"\6\4\2DE\7\20\2\2EG\5\b\5\2F?\3\2\2\2FB\3\2\2\2FD\3\2\2\2G\13\3\2\2\2"+
		"HM\5\6\4\2IM\5\b\5\2JM\5\n\6\2KM\5\16\b\2LH\3\2\2\2LI\3\2\2\2LJ\3\2\2"+
		"\2LK\3\2\2\2M\r\3\2\2\2NO\t\3\2\2O\17\3\2\2\2\f\25\31\34#+.8;FL";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}