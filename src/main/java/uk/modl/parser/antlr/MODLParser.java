// Generated from /Users/tonywalmsley/work/num/grammar/antlr4/MODLParser.g4 by ANTLR 4.7.2
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
		WS=1, NULL=2, TRUE=3, FALSE=4, COLON=5, EQUALS=6, STRUCT_SEP=7, ARR_SEP=8, 
		LBRAC=9, RBRAC=10, LSBRAC=11, RSBRAC=12, NUMBER=13, COMMENT=14, QUOTED=15, 
		STRING=16, HASH_PREFIX=17, LCBRAC=18, CWS=19, QMARK=20, FSLASH=21, GTHAN=22, 
		LTHAN=23, ASTERISK=24, AMP=25, PIPE=26, EXCLAM=27, CCOMMENT=28, RCBRAC=29;
	public static final int
		RULE_modl = 0, RULE_modl_structure = 1, RULE_modl_map = 2, RULE_modl_array = 3, 
		RULE_modl_nb_array = 4, RULE_modl_pair = 5, RULE_modl_value_item = 6, 
		RULE_modl_top_level_conditional = 7, RULE_modl_top_level_conditional_return = 8, 
		RULE_modl_map_conditional = 9, RULE_modl_map_conditional_return = 10, 
		RULE_modl_map_item = 11, RULE_modl_array_conditional = 12, RULE_modl_array_conditional_return = 13, 
		RULE_modl_array_item = 14, RULE_modl_value_conditional = 15, RULE_modl_value_conditional_return = 16, 
		RULE_modl_condition_test = 17, RULE_modl_operator = 18, RULE_modl_condition = 19, 
		RULE_modl_condition_group = 20, RULE_modl_value = 21, RULE_modl_array_value_item = 22, 
		RULE_modl_primitive = 23;
	private static String[] makeRuleNames() {
		return new String[] {
			"modl", "modl_structure", "modl_map", "modl_array", "modl_nb_array", 
			"modl_pair", "modl_value_item", "modl_top_level_conditional", "modl_top_level_conditional_return", 
			"modl_map_conditional", "modl_map_conditional_return", "modl_map_item", 
			"modl_array_conditional", "modl_array_conditional_return", "modl_array_item", 
			"modl_value_conditional", "modl_value_conditional_return", "modl_condition_test", 
			"modl_operator", "modl_condition", "modl_condition_group", "modl_value", 
			"modl_array_value_item", "modl_primitive"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, "','", null, null, null, 
			null, null, null, null, null, null, "'{'", null, "'?'", "'/'", "'>'", 
			"'<'", "'*'", "'&'", "'|'", "'!'", null, "'}'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "WS", "NULL", "TRUE", "FALSE", "COLON", "EQUALS", "STRUCT_SEP", 
			"ARR_SEP", "LBRAC", "RBRAC", "LSBRAC", "RSBRAC", "NUMBER", "COMMENT", 
			"QUOTED", "STRING", "HASH_PREFIX", "LCBRAC", "CWS", "QMARK", "FSLASH", 
			"GTHAN", "LTHAN", "ASTERISK", "AMP", "PIPE", "EXCLAM", "CCOMMENT", "RCBRAC"
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
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitModl(this);
			else return visitor.visitChildren(this);
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
			setState(62);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				{
				{
				setState(49);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LBRAC) | (1L << LSBRAC) | (1L << QUOTED) | (1L << STRING) | (1L << LCBRAC))) != 0)) {
					{
					setState(48);
					modl_structure();
					}
				}

				}
				}
				break;
			case 2:
				{
				{
				setState(51);
				modl_structure();
				setState(56);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(52);
						match(STRUCT_SEP);
						setState(53);
						modl_structure();
						}
						} 
					}
					setState(58);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
				}
				}
				setState(60);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==STRUCT_SEP) {
					{
					setState(59);
					match(STRUCT_SEP);
					}
				}

				}
				break;
			}
			setState(64);
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
		public Modl_top_level_conditionalContext modl_top_level_conditional() {
			return getRuleContext(Modl_top_level_conditionalContext.class,0);
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
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitModl_structure(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Modl_structureContext modl_structure() throws RecognitionException {
		Modl_structureContext _localctx = new Modl_structureContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_modl_structure);
		try {
			setState(70);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LBRAC:
				enterOuterAlt(_localctx, 1);
				{
				setState(66);
				modl_map();
				}
				break;
			case LSBRAC:
				enterOuterAlt(_localctx, 2);
				{
				setState(67);
				modl_array();
				}
				break;
			case LCBRAC:
				enterOuterAlt(_localctx, 3);
				{
				setState(68);
				modl_top_level_conditional();
				}
				break;
			case QUOTED:
			case STRING:
				enterOuterAlt(_localctx, 4);
				{
				setState(69);
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
		public List<Modl_map_itemContext> modl_map_item() {
			return getRuleContexts(Modl_map_itemContext.class);
		}
		public Modl_map_itemContext modl_map_item(int i) {
			return getRuleContext(Modl_map_itemContext.class,i);
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
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitModl_map(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Modl_mapContext modl_map() throws RecognitionException {
		Modl_mapContext _localctx = new Modl_mapContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_modl_map);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(72);
			match(LBRAC);
			setState(81);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << QUOTED) | (1L << STRING) | (1L << LCBRAC))) != 0)) {
				{
				setState(73);
				modl_map_item();
				setState(78);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==STRUCT_SEP) {
					{
					{
					setState(74);
					match(STRUCT_SEP);
					setState(75);
					modl_map_item();
					}
					}
					setState(80);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(83);
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
		public List<Modl_array_itemContext> modl_array_item() {
			return getRuleContexts(Modl_array_itemContext.class);
		}
		public Modl_array_itemContext modl_array_item(int i) {
			return getRuleContext(Modl_array_itemContext.class,i);
		}
		public List<Modl_nb_arrayContext> modl_nb_array() {
			return getRuleContexts(Modl_nb_arrayContext.class);
		}
		public Modl_nb_arrayContext modl_nb_array(int i) {
			return getRuleContext(Modl_nb_arrayContext.class,i);
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
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitModl_array(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Modl_arrayContext modl_array() throws RecognitionException {
		Modl_arrayContext _localctx = new Modl_arrayContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_modl_array);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(85);
			match(LSBRAC);
			setState(110);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NULL) | (1L << TRUE) | (1L << FALSE) | (1L << LBRAC) | (1L << LSBRAC) | (1L << NUMBER) | (1L << QUOTED) | (1L << STRING) | (1L << LCBRAC))) != 0)) {
				{
				setState(88);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
				case 1:
					{
					setState(86);
					modl_array_item();
					}
					break;
				case 2:
					{
					setState(87);
					modl_nb_array();
					}
					break;
				}
				setState(107);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==STRUCT_SEP) {
					{
					{
					setState(91); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(90);
						match(STRUCT_SEP);
						}
						}
						setState(93); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( _la==STRUCT_SEP );
					setState(97);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
					case 1:
						{
						setState(95);
						modl_array_item();
						}
						break;
					case 2:
						{
						setState(96);
						modl_nb_array();
						}
						break;
					}
					setState(102);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(99);
							match(STRUCT_SEP);
							}
							} 
						}
						setState(104);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
					}
					}
					}
					setState(109);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(112);
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

	public static class Modl_nb_arrayContext extends ParserRuleContext {
		public List<Modl_array_itemContext> modl_array_item() {
			return getRuleContexts(Modl_array_itemContext.class);
		}
		public Modl_array_itemContext modl_array_item(int i) {
			return getRuleContext(Modl_array_itemContext.class,i);
		}
		public List<TerminalNode> COLON() { return getTokens(MODLParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(MODLParser.COLON, i);
		}
		public Modl_nb_arrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modl_nb_array; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterModl_nb_array(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitModl_nb_array(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitModl_nb_array(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Modl_nb_arrayContext modl_nb_array() throws RecognitionException {
		Modl_nb_arrayContext _localctx = new Modl_nb_arrayContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_modl_nb_array);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(120); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(114);
					modl_array_item();
					setState(116); 
					_errHandler.sync(this);
					_alt = 1;
					do {
						switch (_alt) {
						case 1:
							{
							{
							setState(115);
							match(COLON);
							}
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(118); 
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
					} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(122); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(127);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(124);
					modl_array_item();
					}
					} 
				}
				setState(129);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			}
			setState(131);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				{
				setState(130);
				match(COLON);
				}
				break;
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

	public static class Modl_pairContext extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(MODLParser.EQUALS, 0); }
		public Modl_value_itemContext modl_value_item() {
			return getRuleContext(Modl_value_itemContext.class,0);
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
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitModl_pair(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Modl_pairContext modl_pair() throws RecognitionException {
		Modl_pairContext _localctx = new Modl_pairContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_modl_pair);
		int _la;
		try {
			setState(140);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(133);
				_la = _input.LA(1);
				if ( !(_la==QUOTED || _la==STRING) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(134);
				match(EQUALS);
				setState(135);
				modl_value_item();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(136);
				match(STRING);
				setState(137);
				modl_map();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(138);
				match(STRING);
				setState(139);
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

	public static class Modl_value_itemContext extends ParserRuleContext {
		public Modl_valueContext modl_value() {
			return getRuleContext(Modl_valueContext.class,0);
		}
		public Modl_value_conditionalContext modl_value_conditional() {
			return getRuleContext(Modl_value_conditionalContext.class,0);
		}
		public Modl_value_itemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modl_value_item; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterModl_value_item(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitModl_value_item(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitModl_value_item(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Modl_value_itemContext modl_value_item() throws RecognitionException {
		Modl_value_itemContext _localctx = new Modl_value_itemContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_modl_value_item);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(144);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				{
				setState(142);
				modl_value();
				}
				break;
			case 2:
				{
				setState(143);
				modl_value_conditional();
				}
				break;
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

	public static class Modl_top_level_conditionalContext extends ParserRuleContext {
		public TerminalNode LCBRAC() { return getToken(MODLParser.LCBRAC, 0); }
		public List<Modl_condition_testContext> modl_condition_test() {
			return getRuleContexts(Modl_condition_testContext.class);
		}
		public Modl_condition_testContext modl_condition_test(int i) {
			return getRuleContext(Modl_condition_testContext.class,i);
		}
		public List<TerminalNode> QMARK() { return getTokens(MODLParser.QMARK); }
		public TerminalNode QMARK(int i) {
			return getToken(MODLParser.QMARK, i);
		}
		public List<Modl_top_level_conditional_returnContext> modl_top_level_conditional_return() {
			return getRuleContexts(Modl_top_level_conditional_returnContext.class);
		}
		public Modl_top_level_conditional_returnContext modl_top_level_conditional_return(int i) {
			return getRuleContext(Modl_top_level_conditional_returnContext.class,i);
		}
		public TerminalNode RCBRAC() { return getToken(MODLParser.RCBRAC, 0); }
		public List<TerminalNode> FSLASH() { return getTokens(MODLParser.FSLASH); }
		public TerminalNode FSLASH(int i) {
			return getToken(MODLParser.FSLASH, i);
		}
		public Modl_top_level_conditionalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modl_top_level_conditional; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterModl_top_level_conditional(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitModl_top_level_conditional(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitModl_top_level_conditional(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Modl_top_level_conditionalContext modl_top_level_conditional() throws RecognitionException {
		Modl_top_level_conditionalContext _localctx = new Modl_top_level_conditionalContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_modl_top_level_conditional);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(146);
			match(LCBRAC);
			setState(147);
			modl_condition_test();
			setState(148);
			match(QMARK);
			setState(149);
			modl_top_level_conditional_return();
			setState(158);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==FSLASH) {
				{
				{
				setState(150);
				match(FSLASH);
				setState(152);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NULL) | (1L << TRUE) | (1L << FALSE) | (1L << EQUALS) | (1L << LBRAC) | (1L << LSBRAC) | (1L << NUMBER) | (1L << QUOTED) | (1L << STRING) | (1L << LCBRAC) | (1L << GTHAN) | (1L << LTHAN) | (1L << EXCLAM))) != 0)) {
					{
					setState(151);
					modl_condition_test();
					}
				}

				setState(154);
				match(QMARK);
				setState(155);
				modl_top_level_conditional_return();
				}
				}
				setState(160);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(161);
			match(RCBRAC);
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

	public static class Modl_top_level_conditional_returnContext extends ParserRuleContext {
		public List<Modl_structureContext> modl_structure() {
			return getRuleContexts(Modl_structureContext.class);
		}
		public Modl_structureContext modl_structure(int i) {
			return getRuleContext(Modl_structureContext.class,i);
		}
		public Modl_top_level_conditional_returnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modl_top_level_conditional_return; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterModl_top_level_conditional_return(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitModl_top_level_conditional_return(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitModl_top_level_conditional_return(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Modl_top_level_conditional_returnContext modl_top_level_conditional_return() throws RecognitionException {
		Modl_top_level_conditional_returnContext _localctx = new Modl_top_level_conditional_returnContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_modl_top_level_conditional_return);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(166);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LBRAC) | (1L << LSBRAC) | (1L << QUOTED) | (1L << STRING) | (1L << LCBRAC))) != 0)) {
				{
				{
				setState(163);
				modl_structure();
				}
				}
				setState(168);
				_errHandler.sync(this);
				_la = _input.LA(1);
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

	public static class Modl_map_conditionalContext extends ParserRuleContext {
		public TerminalNode LCBRAC() { return getToken(MODLParser.LCBRAC, 0); }
		public List<Modl_condition_testContext> modl_condition_test() {
			return getRuleContexts(Modl_condition_testContext.class);
		}
		public Modl_condition_testContext modl_condition_test(int i) {
			return getRuleContext(Modl_condition_testContext.class,i);
		}
		public List<TerminalNode> QMARK() { return getTokens(MODLParser.QMARK); }
		public TerminalNode QMARK(int i) {
			return getToken(MODLParser.QMARK, i);
		}
		public List<Modl_map_conditional_returnContext> modl_map_conditional_return() {
			return getRuleContexts(Modl_map_conditional_returnContext.class);
		}
		public Modl_map_conditional_returnContext modl_map_conditional_return(int i) {
			return getRuleContext(Modl_map_conditional_returnContext.class,i);
		}
		public TerminalNode RCBRAC() { return getToken(MODLParser.RCBRAC, 0); }
		public List<TerminalNode> FSLASH() { return getTokens(MODLParser.FSLASH); }
		public TerminalNode FSLASH(int i) {
			return getToken(MODLParser.FSLASH, i);
		}
		public Modl_map_conditionalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modl_map_conditional; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterModl_map_conditional(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitModl_map_conditional(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitModl_map_conditional(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Modl_map_conditionalContext modl_map_conditional() throws RecognitionException {
		Modl_map_conditionalContext _localctx = new Modl_map_conditionalContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_modl_map_conditional);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(169);
			match(LCBRAC);
			setState(170);
			modl_condition_test();
			setState(171);
			match(QMARK);
			setState(172);
			modl_map_conditional_return();
			setState(181);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==FSLASH) {
				{
				{
				setState(173);
				match(FSLASH);
				setState(175);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NULL) | (1L << TRUE) | (1L << FALSE) | (1L << EQUALS) | (1L << LBRAC) | (1L << LSBRAC) | (1L << NUMBER) | (1L << QUOTED) | (1L << STRING) | (1L << LCBRAC) | (1L << GTHAN) | (1L << LTHAN) | (1L << EXCLAM))) != 0)) {
					{
					setState(174);
					modl_condition_test();
					}
				}

				setState(177);
				match(QMARK);
				setState(178);
				modl_map_conditional_return();
				}
				}
				setState(183);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(184);
			match(RCBRAC);
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

	public static class Modl_map_conditional_returnContext extends ParserRuleContext {
		public List<Modl_map_itemContext> modl_map_item() {
			return getRuleContexts(Modl_map_itemContext.class);
		}
		public Modl_map_itemContext modl_map_item(int i) {
			return getRuleContext(Modl_map_itemContext.class,i);
		}
		public Modl_map_conditional_returnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modl_map_conditional_return; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterModl_map_conditional_return(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitModl_map_conditional_return(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitModl_map_conditional_return(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Modl_map_conditional_returnContext modl_map_conditional_return() throws RecognitionException {
		Modl_map_conditional_returnContext _localctx = new Modl_map_conditional_returnContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_modl_map_conditional_return);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(187); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(186);
				modl_map_item();
				}
				}
				setState(189); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << QUOTED) | (1L << STRING) | (1L << LCBRAC))) != 0) );
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

	public static class Modl_map_itemContext extends ParserRuleContext {
		public Modl_pairContext modl_pair() {
			return getRuleContext(Modl_pairContext.class,0);
		}
		public Modl_map_conditionalContext modl_map_conditional() {
			return getRuleContext(Modl_map_conditionalContext.class,0);
		}
		public Modl_map_itemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modl_map_item; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterModl_map_item(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitModl_map_item(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitModl_map_item(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Modl_map_itemContext modl_map_item() throws RecognitionException {
		Modl_map_itemContext _localctx = new Modl_map_itemContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_modl_map_item);
		try {
			setState(193);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case QUOTED:
			case STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(191);
				modl_pair();
				}
				break;
			case LCBRAC:
				enterOuterAlt(_localctx, 2);
				{
				setState(192);
				modl_map_conditional();
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

	public static class Modl_array_conditionalContext extends ParserRuleContext {
		public TerminalNode LCBRAC() { return getToken(MODLParser.LCBRAC, 0); }
		public List<Modl_condition_testContext> modl_condition_test() {
			return getRuleContexts(Modl_condition_testContext.class);
		}
		public Modl_condition_testContext modl_condition_test(int i) {
			return getRuleContext(Modl_condition_testContext.class,i);
		}
		public List<TerminalNode> QMARK() { return getTokens(MODLParser.QMARK); }
		public TerminalNode QMARK(int i) {
			return getToken(MODLParser.QMARK, i);
		}
		public List<Modl_array_conditional_returnContext> modl_array_conditional_return() {
			return getRuleContexts(Modl_array_conditional_returnContext.class);
		}
		public Modl_array_conditional_returnContext modl_array_conditional_return(int i) {
			return getRuleContext(Modl_array_conditional_returnContext.class,i);
		}
		public TerminalNode RCBRAC() { return getToken(MODLParser.RCBRAC, 0); }
		public List<TerminalNode> FSLASH() { return getTokens(MODLParser.FSLASH); }
		public TerminalNode FSLASH(int i) {
			return getToken(MODLParser.FSLASH, i);
		}
		public Modl_array_conditionalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modl_array_conditional; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterModl_array_conditional(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitModl_array_conditional(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitModl_array_conditional(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Modl_array_conditionalContext modl_array_conditional() throws RecognitionException {
		Modl_array_conditionalContext _localctx = new Modl_array_conditionalContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_modl_array_conditional);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(195);
			match(LCBRAC);
			setState(196);
			modl_condition_test();
			setState(197);
			match(QMARK);
			setState(198);
			modl_array_conditional_return();
			setState(207);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==FSLASH) {
				{
				{
				setState(199);
				match(FSLASH);
				setState(201);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NULL) | (1L << TRUE) | (1L << FALSE) | (1L << EQUALS) | (1L << LBRAC) | (1L << LSBRAC) | (1L << NUMBER) | (1L << QUOTED) | (1L << STRING) | (1L << LCBRAC) | (1L << GTHAN) | (1L << LTHAN) | (1L << EXCLAM))) != 0)) {
					{
					setState(200);
					modl_condition_test();
					}
				}

				setState(203);
				match(QMARK);
				setState(204);
				modl_array_conditional_return();
				}
				}
				setState(209);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(210);
			match(RCBRAC);
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

	public static class Modl_array_conditional_returnContext extends ParserRuleContext {
		public List<Modl_array_itemContext> modl_array_item() {
			return getRuleContexts(Modl_array_itemContext.class);
		}
		public Modl_array_itemContext modl_array_item(int i) {
			return getRuleContext(Modl_array_itemContext.class,i);
		}
		public Modl_array_conditional_returnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modl_array_conditional_return; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterModl_array_conditional_return(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitModl_array_conditional_return(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitModl_array_conditional_return(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Modl_array_conditional_returnContext modl_array_conditional_return() throws RecognitionException {
		Modl_array_conditional_returnContext _localctx = new Modl_array_conditional_returnContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_modl_array_conditional_return);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(213); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(212);
				modl_array_item();
				}
				}
				setState(215); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NULL) | (1L << TRUE) | (1L << FALSE) | (1L << LBRAC) | (1L << LSBRAC) | (1L << NUMBER) | (1L << QUOTED) | (1L << STRING) | (1L << LCBRAC))) != 0) );
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

	public static class Modl_array_itemContext extends ParserRuleContext {
		public Modl_array_value_itemContext modl_array_value_item() {
			return getRuleContext(Modl_array_value_itemContext.class,0);
		}
		public Modl_array_conditionalContext modl_array_conditional() {
			return getRuleContext(Modl_array_conditionalContext.class,0);
		}
		public Modl_array_itemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modl_array_item; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterModl_array_item(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitModl_array_item(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitModl_array_item(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Modl_array_itemContext modl_array_item() throws RecognitionException {
		Modl_array_itemContext _localctx = new Modl_array_itemContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_modl_array_item);
		try {
			setState(219);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NULL:
			case TRUE:
			case FALSE:
			case LBRAC:
			case LSBRAC:
			case NUMBER:
			case QUOTED:
			case STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(217);
				modl_array_value_item();
				}
				break;
			case LCBRAC:
				enterOuterAlt(_localctx, 2);
				{
				setState(218);
				modl_array_conditional();
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

	public static class Modl_value_conditionalContext extends ParserRuleContext {
		public TerminalNode LCBRAC() { return getToken(MODLParser.LCBRAC, 0); }
		public List<Modl_condition_testContext> modl_condition_test() {
			return getRuleContexts(Modl_condition_testContext.class);
		}
		public Modl_condition_testContext modl_condition_test(int i) {
			return getRuleContext(Modl_condition_testContext.class,i);
		}
		public List<TerminalNode> QMARK() { return getTokens(MODLParser.QMARK); }
		public TerminalNode QMARK(int i) {
			return getToken(MODLParser.QMARK, i);
		}
		public TerminalNode RCBRAC() { return getToken(MODLParser.RCBRAC, 0); }
		public List<Modl_value_conditional_returnContext> modl_value_conditional_return() {
			return getRuleContexts(Modl_value_conditional_returnContext.class);
		}
		public Modl_value_conditional_returnContext modl_value_conditional_return(int i) {
			return getRuleContext(Modl_value_conditional_returnContext.class,i);
		}
		public List<TerminalNode> FSLASH() { return getTokens(MODLParser.FSLASH); }
		public TerminalNode FSLASH(int i) {
			return getToken(MODLParser.FSLASH, i);
		}
		public Modl_value_conditionalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modl_value_conditional; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterModl_value_conditional(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitModl_value_conditional(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitModl_value_conditional(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Modl_value_conditionalContext modl_value_conditional() throws RecognitionException {
		Modl_value_conditionalContext _localctx = new Modl_value_conditionalContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_modl_value_conditional);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(221);
			match(LCBRAC);
			setState(222);
			modl_condition_test();
			setState(223);
			match(QMARK);
			setState(239);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NULL) | (1L << TRUE) | (1L << FALSE) | (1L << LBRAC) | (1L << LSBRAC) | (1L << NUMBER) | (1L << QUOTED) | (1L << STRING) | (1L << LCBRAC))) != 0)) {
				{
				setState(224);
				modl_value_conditional_return();
				setState(232);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(225);
						match(FSLASH);
						setState(226);
						modl_condition_test();
						setState(227);
						match(QMARK);
						setState(228);
						modl_value_conditional_return();
						}
						} 
					}
					setState(234);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
				}
				{
				setState(235);
				match(FSLASH);
				setState(236);
				match(QMARK);
				setState(237);
				modl_value_conditional_return();
				}
				}
			}

			setState(241);
			match(RCBRAC);
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

	public static class Modl_value_conditional_returnContext extends ParserRuleContext {
		public List<Modl_value_itemContext> modl_value_item() {
			return getRuleContexts(Modl_value_itemContext.class);
		}
		public Modl_value_itemContext modl_value_item(int i) {
			return getRuleContext(Modl_value_itemContext.class,i);
		}
		public List<TerminalNode> COLON() { return getTokens(MODLParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(MODLParser.COLON, i);
		}
		public Modl_value_conditional_returnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modl_value_conditional_return; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterModl_value_conditional_return(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitModl_value_conditional_return(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitModl_value_conditional_return(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Modl_value_conditional_returnContext modl_value_conditional_return() throws RecognitionException {
		Modl_value_conditional_returnContext _localctx = new Modl_value_conditional_returnContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_modl_value_conditional_return);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(243);
			modl_value_item();
			setState(248);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COLON) {
				{
				{
				setState(244);
				match(COLON);
				setState(245);
				modl_value_item();
				}
				}
				setState(250);
				_errHandler.sync(this);
				_la = _input.LA(1);
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

	public static class Modl_condition_testContext extends ParserRuleContext {
		public List<Modl_conditionContext> modl_condition() {
			return getRuleContexts(Modl_conditionContext.class);
		}
		public Modl_conditionContext modl_condition(int i) {
			return getRuleContext(Modl_conditionContext.class,i);
		}
		public List<Modl_condition_groupContext> modl_condition_group() {
			return getRuleContexts(Modl_condition_groupContext.class);
		}
		public Modl_condition_groupContext modl_condition_group(int i) {
			return getRuleContext(Modl_condition_groupContext.class,i);
		}
		public List<TerminalNode> EXCLAM() { return getTokens(MODLParser.EXCLAM); }
		public TerminalNode EXCLAM(int i) {
			return getToken(MODLParser.EXCLAM, i);
		}
		public List<TerminalNode> AMP() { return getTokens(MODLParser.AMP); }
		public TerminalNode AMP(int i) {
			return getToken(MODLParser.AMP, i);
		}
		public List<TerminalNode> PIPE() { return getTokens(MODLParser.PIPE); }
		public TerminalNode PIPE(int i) {
			return getToken(MODLParser.PIPE, i);
		}
		public Modl_condition_testContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modl_condition_test; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterModl_condition_test(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitModl_condition_test(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitModl_condition_test(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Modl_condition_testContext modl_condition_test() throws RecognitionException {
		Modl_condition_testContext _localctx = new Modl_condition_testContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_modl_condition_test);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(252);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				{
				setState(251);
				match(EXCLAM);
				}
				break;
			}
			setState(256);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
			case 1:
				{
				setState(254);
				modl_condition();
				}
				break;
			case 2:
				{
				setState(255);
				modl_condition_group();
				}
				break;
			}
			setState(268);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,37,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(258);
					_la = _input.LA(1);
					if ( !(_la==AMP || _la==PIPE) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(260);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
					case 1:
						{
						setState(259);
						match(EXCLAM);
						}
						break;
					}
					setState(264);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
					case 1:
						{
						setState(262);
						modl_condition();
						}
						break;
					case 2:
						{
						setState(263);
						modl_condition_group();
						}
						break;
					}
					}
					} 
				}
				setState(270);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,37,_ctx);
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

	public static class Modl_operatorContext extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(MODLParser.EQUALS, 0); }
		public TerminalNode GTHAN() { return getToken(MODLParser.GTHAN, 0); }
		public TerminalNode LTHAN() { return getToken(MODLParser.LTHAN, 0); }
		public TerminalNode EXCLAM() { return getToken(MODLParser.EXCLAM, 0); }
		public Modl_operatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modl_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterModl_operator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitModl_operator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitModl_operator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Modl_operatorContext modl_operator() throws RecognitionException {
		Modl_operatorContext _localctx = new Modl_operatorContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_modl_operator);
		try {
			setState(280);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(271);
				match(EQUALS);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(272);
				match(GTHAN);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(273);
				match(GTHAN);
				setState(274);
				match(EQUALS);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(275);
				match(LTHAN);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(276);
				match(LTHAN);
				setState(277);
				match(EQUALS);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(278);
				match(EXCLAM);
				setState(279);
				match(EQUALS);
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

	public static class Modl_conditionContext extends ParserRuleContext {
		public List<Modl_valueContext> modl_value() {
			return getRuleContexts(Modl_valueContext.class);
		}
		public Modl_valueContext modl_value(int i) {
			return getRuleContext(Modl_valueContext.class,i);
		}
		public TerminalNode STRING() { return getToken(MODLParser.STRING, 0); }
		public Modl_operatorContext modl_operator() {
			return getRuleContext(Modl_operatorContext.class,0);
		}
		public List<TerminalNode> FSLASH() { return getTokens(MODLParser.FSLASH); }
		public TerminalNode FSLASH(int i) {
			return getToken(MODLParser.FSLASH, i);
		}
		public Modl_conditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modl_condition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterModl_condition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitModl_condition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitModl_condition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Modl_conditionContext modl_condition() throws RecognitionException {
		Modl_conditionContext _localctx = new Modl_conditionContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_modl_condition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(283);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
			case 1:
				{
				setState(282);
				match(STRING);
				}
				break;
			}
			setState(286);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << EQUALS) | (1L << GTHAN) | (1L << LTHAN) | (1L << EXCLAM))) != 0)) {
				{
				setState(285);
				modl_operator();
				}
			}

			setState(288);
			modl_value();
			setState(293);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==FSLASH) {
				{
				{
				setState(289);
				match(FSLASH);
				setState(290);
				modl_value();
				}
				}
				setState(295);
				_errHandler.sync(this);
				_la = _input.LA(1);
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

	public static class Modl_condition_groupContext extends ParserRuleContext {
		public TerminalNode LCBRAC() { return getToken(MODLParser.LCBRAC, 0); }
		public List<Modl_condition_testContext> modl_condition_test() {
			return getRuleContexts(Modl_condition_testContext.class);
		}
		public Modl_condition_testContext modl_condition_test(int i) {
			return getRuleContext(Modl_condition_testContext.class,i);
		}
		public TerminalNode RCBRAC() { return getToken(MODLParser.RCBRAC, 0); }
		public List<TerminalNode> AMP() { return getTokens(MODLParser.AMP); }
		public TerminalNode AMP(int i) {
			return getToken(MODLParser.AMP, i);
		}
		public List<TerminalNode> PIPE() { return getTokens(MODLParser.PIPE); }
		public TerminalNode PIPE(int i) {
			return getToken(MODLParser.PIPE, i);
		}
		public Modl_condition_groupContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modl_condition_group; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterModl_condition_group(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitModl_condition_group(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitModl_condition_group(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Modl_condition_groupContext modl_condition_group() throws RecognitionException {
		Modl_condition_groupContext _localctx = new Modl_condition_groupContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_modl_condition_group);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(296);
			match(LCBRAC);
			setState(297);
			modl_condition_test();
			setState(302);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AMP || _la==PIPE) {
				{
				{
				setState(298);
				_la = _input.LA(1);
				if ( !(_la==AMP || _la==PIPE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(299);
				modl_condition_test();
				}
				}
				setState(304);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(305);
			match(RCBRAC);
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
		public Modl_nb_arrayContext modl_nb_array() {
			return getRuleContext(Modl_nb_arrayContext.class,0);
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
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitModl_value(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Modl_valueContext modl_value() throws RecognitionException {
		Modl_valueContext _localctx = new Modl_valueContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_modl_value);
		try {
			setState(312);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(307);
				modl_map();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(308);
				modl_array();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(309);
				modl_nb_array();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(310);
				modl_pair();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(311);
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

	public static class Modl_array_value_itemContext extends ParserRuleContext {
		public Modl_mapContext modl_map() {
			return getRuleContext(Modl_mapContext.class,0);
		}
		public Modl_pairContext modl_pair() {
			return getRuleContext(Modl_pairContext.class,0);
		}
		public Modl_arrayContext modl_array() {
			return getRuleContext(Modl_arrayContext.class,0);
		}
		public Modl_primitiveContext modl_primitive() {
			return getRuleContext(Modl_primitiveContext.class,0);
		}
		public Modl_array_value_itemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modl_array_value_item; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterModl_array_value_item(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitModl_array_value_item(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitModl_array_value_item(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Modl_array_value_itemContext modl_array_value_item() throws RecognitionException {
		Modl_array_value_itemContext _localctx = new Modl_array_value_itemContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_modl_array_value_item);
		try {
			setState(318);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(314);
				modl_map();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(315);
				modl_pair();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(316);
				modl_array();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(317);
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
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitModl_primitive(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Modl_primitiveContext modl_primitive() throws RecognitionException {
		Modl_primitiveContext _localctx = new Modl_primitiveContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_modl_primitive);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(320);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\37\u0145\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\3\2\5\2\64\n\2\3\2\3\2\3\2\7\29\n\2\f\2\16\2<\13\2\3\2\5\2?\n\2\5\2A"+
		"\n\2\3\2\3\2\3\3\3\3\3\3\3\3\5\3I\n\3\3\4\3\4\3\4\3\4\7\4O\n\4\f\4\16"+
		"\4R\13\4\5\4T\n\4\3\4\3\4\3\5\3\5\3\5\5\5[\n\5\3\5\6\5^\n\5\r\5\16\5_"+
		"\3\5\3\5\5\5d\n\5\3\5\7\5g\n\5\f\5\16\5j\13\5\7\5l\n\5\f\5\16\5o\13\5"+
		"\5\5q\n\5\3\5\3\5\3\6\3\6\6\6w\n\6\r\6\16\6x\6\6{\n\6\r\6\16\6|\3\6\7"+
		"\6\u0080\n\6\f\6\16\6\u0083\13\6\3\6\5\6\u0086\n\6\3\7\3\7\3\7\3\7\3\7"+
		"\3\7\3\7\5\7\u008f\n\7\3\b\3\b\5\b\u0093\n\b\3\t\3\t\3\t\3\t\3\t\3\t\5"+
		"\t\u009b\n\t\3\t\3\t\7\t\u009f\n\t\f\t\16\t\u00a2\13\t\3\t\3\t\3\n\7\n"+
		"\u00a7\n\n\f\n\16\n\u00aa\13\n\3\13\3\13\3\13\3\13\3\13\3\13\5\13\u00b2"+
		"\n\13\3\13\3\13\7\13\u00b6\n\13\f\13\16\13\u00b9\13\13\3\13\3\13\3\f\6"+
		"\f\u00be\n\f\r\f\16\f\u00bf\3\r\3\r\5\r\u00c4\n\r\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\5\16\u00cc\n\16\3\16\3\16\7\16\u00d0\n\16\f\16\16\16\u00d3"+
		"\13\16\3\16\3\16\3\17\6\17\u00d8\n\17\r\17\16\17\u00d9\3\20\3\20\5\20"+
		"\u00de\n\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\7\21\u00e9\n"+
		"\21\f\21\16\21\u00ec\13\21\3\21\3\21\3\21\3\21\5\21\u00f2\n\21\3\21\3"+
		"\21\3\22\3\22\3\22\7\22\u00f9\n\22\f\22\16\22\u00fc\13\22\3\23\5\23\u00ff"+
		"\n\23\3\23\3\23\5\23\u0103\n\23\3\23\3\23\5\23\u0107\n\23\3\23\3\23\5"+
		"\23\u010b\n\23\7\23\u010d\n\23\f\23\16\23\u0110\13\23\3\24\3\24\3\24\3"+
		"\24\3\24\3\24\3\24\3\24\3\24\5\24\u011b\n\24\3\25\5\25\u011e\n\25\3\25"+
		"\5\25\u0121\n\25\3\25\3\25\3\25\7\25\u0126\n\25\f\25\16\25\u0129\13\25"+
		"\3\26\3\26\3\26\3\26\7\26\u012f\n\26\f\26\16\26\u0132\13\26\3\26\3\26"+
		"\3\27\3\27\3\27\3\27\3\27\5\27\u013b\n\27\3\30\3\30\3\30\3\30\5\30\u0141"+
		"\n\30\3\31\3\31\3\31\2\2\32\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \""+
		"$&(*,.\60\2\5\3\2\21\22\3\2\33\34\5\2\4\6\17\17\21\22\2\u0165\2@\3\2\2"+
		"\2\4H\3\2\2\2\6J\3\2\2\2\bW\3\2\2\2\nz\3\2\2\2\f\u008e\3\2\2\2\16\u0092"+
		"\3\2\2\2\20\u0094\3\2\2\2\22\u00a8\3\2\2\2\24\u00ab\3\2\2\2\26\u00bd\3"+
		"\2\2\2\30\u00c3\3\2\2\2\32\u00c5\3\2\2\2\34\u00d7\3\2\2\2\36\u00dd\3\2"+
		"\2\2 \u00df\3\2\2\2\"\u00f5\3\2\2\2$\u00fe\3\2\2\2&\u011a\3\2\2\2(\u011d"+
		"\3\2\2\2*\u012a\3\2\2\2,\u013a\3\2\2\2.\u0140\3\2\2\2\60\u0142\3\2\2\2"+
		"\62\64\5\4\3\2\63\62\3\2\2\2\63\64\3\2\2\2\64A\3\2\2\2\65:\5\4\3\2\66"+
		"\67\7\t\2\2\679\5\4\3\28\66\3\2\2\29<\3\2\2\2:8\3\2\2\2:;\3\2\2\2;>\3"+
		"\2\2\2<:\3\2\2\2=?\7\t\2\2>=\3\2\2\2>?\3\2\2\2?A\3\2\2\2@\63\3\2\2\2@"+
		"\65\3\2\2\2AB\3\2\2\2BC\7\2\2\3C\3\3\2\2\2DI\5\6\4\2EI\5\b\5\2FI\5\20"+
		"\t\2GI\5\f\7\2HD\3\2\2\2HE\3\2\2\2HF\3\2\2\2HG\3\2\2\2I\5\3\2\2\2JS\7"+
		"\13\2\2KP\5\30\r\2LM\7\t\2\2MO\5\30\r\2NL\3\2\2\2OR\3\2\2\2PN\3\2\2\2"+
		"PQ\3\2\2\2QT\3\2\2\2RP\3\2\2\2SK\3\2\2\2ST\3\2\2\2TU\3\2\2\2UV\7\f\2\2"+
		"V\7\3\2\2\2Wp\7\r\2\2X[\5\36\20\2Y[\5\n\6\2ZX\3\2\2\2ZY\3\2\2\2[m\3\2"+
		"\2\2\\^\7\t\2\2]\\\3\2\2\2^_\3\2\2\2_]\3\2\2\2_`\3\2\2\2`c\3\2\2\2ad\5"+
		"\36\20\2bd\5\n\6\2ca\3\2\2\2cb\3\2\2\2dh\3\2\2\2eg\7\t\2\2fe\3\2\2\2g"+
		"j\3\2\2\2hf\3\2\2\2hi\3\2\2\2il\3\2\2\2jh\3\2\2\2k]\3\2\2\2lo\3\2\2\2"+
		"mk\3\2\2\2mn\3\2\2\2nq\3\2\2\2om\3\2\2\2pZ\3\2\2\2pq\3\2\2\2qr\3\2\2\2"+
		"rs\7\16\2\2s\t\3\2\2\2tv\5\36\20\2uw\7\7\2\2vu\3\2\2\2wx\3\2\2\2xv\3\2"+
		"\2\2xy\3\2\2\2y{\3\2\2\2zt\3\2\2\2{|\3\2\2\2|z\3\2\2\2|}\3\2\2\2}\u0081"+
		"\3\2\2\2~\u0080\5\36\20\2\177~\3\2\2\2\u0080\u0083\3\2\2\2\u0081\177\3"+
		"\2\2\2\u0081\u0082\3\2\2\2\u0082\u0085\3\2\2\2\u0083\u0081\3\2\2\2\u0084"+
		"\u0086\7\7\2\2\u0085\u0084\3\2\2\2\u0085\u0086\3\2\2\2\u0086\13\3\2\2"+
		"\2\u0087\u0088\t\2\2\2\u0088\u0089\7\b\2\2\u0089\u008f\5\16\b\2\u008a"+
		"\u008b\7\22\2\2\u008b\u008f\5\6\4\2\u008c\u008d\7\22\2\2\u008d\u008f\5"+
		"\b\5\2\u008e\u0087\3\2\2\2\u008e\u008a\3\2\2\2\u008e\u008c\3\2\2\2\u008f"+
		"\r\3\2\2\2\u0090\u0093\5,\27\2\u0091\u0093\5 \21\2\u0092\u0090\3\2\2\2"+
		"\u0092\u0091\3\2\2\2\u0093\17\3\2\2\2\u0094\u0095\7\24\2\2\u0095\u0096"+
		"\5$\23\2\u0096\u0097\7\26\2\2\u0097\u00a0\5\22\n\2\u0098\u009a\7\27\2"+
		"\2\u0099\u009b\5$\23\2\u009a\u0099\3\2\2\2\u009a\u009b\3\2\2\2\u009b\u009c"+
		"\3\2\2\2\u009c\u009d\7\26\2\2\u009d\u009f\5\22\n\2\u009e\u0098\3\2\2\2"+
		"\u009f\u00a2\3\2\2\2\u00a0\u009e\3\2\2\2\u00a0\u00a1\3\2\2\2\u00a1\u00a3"+
		"\3\2\2\2\u00a2\u00a0\3\2\2\2\u00a3\u00a4\7\37\2\2\u00a4\21\3\2\2\2\u00a5"+
		"\u00a7\5\4\3\2\u00a6\u00a5\3\2\2\2\u00a7\u00aa\3\2\2\2\u00a8\u00a6\3\2"+
		"\2\2\u00a8\u00a9\3\2\2\2\u00a9\23\3\2\2\2\u00aa\u00a8\3\2\2\2\u00ab\u00ac"+
		"\7\24\2\2\u00ac\u00ad\5$\23\2\u00ad\u00ae\7\26\2\2\u00ae\u00b7\5\26\f"+
		"\2\u00af\u00b1\7\27\2\2\u00b0\u00b2\5$\23\2\u00b1\u00b0\3\2\2\2\u00b1"+
		"\u00b2\3\2\2\2\u00b2\u00b3\3\2\2\2\u00b3\u00b4\7\26\2\2\u00b4\u00b6\5"+
		"\26\f\2\u00b5\u00af\3\2\2\2\u00b6\u00b9\3\2\2\2\u00b7\u00b5\3\2\2\2\u00b7"+
		"\u00b8\3\2\2\2\u00b8\u00ba\3\2\2\2\u00b9\u00b7\3\2\2\2\u00ba\u00bb\7\37"+
		"\2\2\u00bb\25\3\2\2\2\u00bc\u00be\5\30\r\2\u00bd\u00bc\3\2\2\2\u00be\u00bf"+
		"\3\2\2\2\u00bf\u00bd\3\2\2\2\u00bf\u00c0\3\2\2\2\u00c0\27\3\2\2\2\u00c1"+
		"\u00c4\5\f\7\2\u00c2\u00c4\5\24\13\2\u00c3\u00c1\3\2\2\2\u00c3\u00c2\3"+
		"\2\2\2\u00c4\31\3\2\2\2\u00c5\u00c6\7\24\2\2\u00c6\u00c7\5$\23\2\u00c7"+
		"\u00c8\7\26\2\2\u00c8\u00d1\5\34\17\2\u00c9\u00cb\7\27\2\2\u00ca\u00cc"+
		"\5$\23\2\u00cb\u00ca\3\2\2\2\u00cb\u00cc\3\2\2\2\u00cc\u00cd\3\2\2\2\u00cd"+
		"\u00ce\7\26\2\2\u00ce\u00d0\5\34\17\2\u00cf\u00c9\3\2\2\2\u00d0\u00d3"+
		"\3\2\2\2\u00d1\u00cf\3\2\2\2\u00d1\u00d2\3\2\2\2\u00d2\u00d4\3\2\2\2\u00d3"+
		"\u00d1\3\2\2\2\u00d4\u00d5\7\37\2\2\u00d5\33\3\2\2\2\u00d6\u00d8\5\36"+
		"\20\2\u00d7\u00d6\3\2\2\2\u00d8\u00d9\3\2\2\2\u00d9\u00d7\3\2\2\2\u00d9"+
		"\u00da\3\2\2\2\u00da\35\3\2\2\2\u00db\u00de\5.\30\2\u00dc\u00de\5\32\16"+
		"\2\u00dd\u00db\3\2\2\2\u00dd\u00dc\3\2\2\2\u00de\37\3\2\2\2\u00df\u00e0"+
		"\7\24\2\2\u00e0\u00e1\5$\23\2\u00e1\u00f1\7\26\2\2\u00e2\u00ea\5\"\22"+
		"\2\u00e3\u00e4\7\27\2\2\u00e4\u00e5\5$\23\2\u00e5\u00e6\7\26\2\2\u00e6"+
		"\u00e7\5\"\22\2\u00e7\u00e9\3\2\2\2\u00e8\u00e3\3\2\2\2\u00e9\u00ec\3"+
		"\2\2\2\u00ea\u00e8\3\2\2\2\u00ea\u00eb\3\2\2\2\u00eb\u00ed\3\2\2\2\u00ec"+
		"\u00ea\3\2\2\2\u00ed\u00ee\7\27\2\2\u00ee\u00ef\7\26\2\2\u00ef\u00f0\5"+
		"\"\22\2\u00f0\u00f2\3\2\2\2\u00f1\u00e2\3\2\2\2\u00f1\u00f2\3\2\2\2\u00f2"+
		"\u00f3\3\2\2\2\u00f3\u00f4\7\37\2\2\u00f4!\3\2\2\2\u00f5\u00fa\5\16\b"+
		"\2\u00f6\u00f7\7\7\2\2\u00f7\u00f9\5\16\b\2\u00f8\u00f6\3\2\2\2\u00f9"+
		"\u00fc\3\2\2\2\u00fa\u00f8\3\2\2\2\u00fa\u00fb\3\2\2\2\u00fb#\3\2\2\2"+
		"\u00fc\u00fa\3\2\2\2\u00fd\u00ff\7\35\2\2\u00fe\u00fd\3\2\2\2\u00fe\u00ff"+
		"\3\2\2\2\u00ff\u0102\3\2\2\2\u0100\u0103\5(\25\2\u0101\u0103\5*\26\2\u0102"+
		"\u0100\3\2\2\2\u0102\u0101\3\2\2\2\u0103\u010e\3\2\2\2\u0104\u0106\t\3"+
		"\2\2\u0105\u0107\7\35\2\2\u0106\u0105\3\2\2\2\u0106\u0107\3\2\2\2\u0107"+
		"\u010a\3\2\2\2\u0108\u010b\5(\25\2\u0109\u010b\5*\26\2\u010a\u0108\3\2"+
		"\2\2\u010a\u0109\3\2\2\2\u010b\u010d\3\2\2\2\u010c\u0104\3\2\2\2\u010d"+
		"\u0110\3\2\2\2\u010e\u010c\3\2\2\2\u010e\u010f\3\2\2\2\u010f%\3\2\2\2"+
		"\u0110\u010e\3\2\2\2\u0111\u011b\7\b\2\2\u0112\u011b\7\30\2\2\u0113\u0114"+
		"\7\30\2\2\u0114\u011b\7\b\2\2\u0115\u011b\7\31\2\2\u0116\u0117\7\31\2"+
		"\2\u0117\u011b\7\b\2\2\u0118\u0119\7\35\2\2\u0119\u011b\7\b\2\2\u011a"+
		"\u0111\3\2\2\2\u011a\u0112\3\2\2\2\u011a\u0113\3\2\2\2\u011a\u0115\3\2"+
		"\2\2\u011a\u0116\3\2\2\2\u011a\u0118\3\2\2\2\u011b\'\3\2\2\2\u011c\u011e"+
		"\7\22\2\2\u011d\u011c\3\2\2\2\u011d\u011e\3\2\2\2\u011e\u0120\3\2\2\2"+
		"\u011f\u0121\5&\24\2\u0120\u011f\3\2\2\2\u0120\u0121\3\2\2\2\u0121\u0122"+
		"\3\2\2\2\u0122\u0127\5,\27\2\u0123\u0124\7\27\2\2\u0124\u0126\5,\27\2"+
		"\u0125\u0123\3\2\2\2\u0126\u0129\3\2\2\2\u0127\u0125\3\2\2\2\u0127\u0128"+
		"\3\2\2\2\u0128)\3\2\2\2\u0129\u0127\3\2\2\2\u012a\u012b\7\24\2\2\u012b"+
		"\u0130\5$\23\2\u012c\u012d\t\3\2\2\u012d\u012f\5$\23\2\u012e\u012c\3\2"+
		"\2\2\u012f\u0132\3\2\2\2\u0130\u012e\3\2\2\2\u0130\u0131\3\2\2\2\u0131"+
		"\u0133\3\2\2\2\u0132\u0130\3\2\2\2\u0133\u0134\7\37\2\2\u0134+\3\2\2\2"+
		"\u0135\u013b\5\6\4\2\u0136\u013b\5\b\5\2\u0137\u013b\5\n\6\2\u0138\u013b"+
		"\5\f\7\2\u0139\u013b\5\60\31\2\u013a\u0135\3\2\2\2\u013a\u0136\3\2\2\2"+
		"\u013a\u0137\3\2\2\2\u013a\u0138\3\2\2\2\u013a\u0139\3\2\2\2\u013b-\3"+
		"\2\2\2\u013c\u0141\5\6\4\2\u013d\u0141\5\f\7\2\u013e\u0141\5\b\5\2\u013f"+
		"\u0141\5\60\31\2\u0140\u013c\3\2\2\2\u0140\u013d\3\2\2\2\u0140\u013e\3"+
		"\2\2\2\u0140\u013f\3\2\2\2\u0141/\3\2\2\2\u0142\u0143\t\4\2\2\u0143\61"+
		"\3\2\2\2/\63:>@HPSZ_chmpx|\u0081\u0085\u008e\u0092\u009a\u00a0\u00a8\u00b1"+
		"\u00b7\u00bf\u00c3\u00cb\u00d1\u00d9\u00dd\u00ea\u00f1\u00fa\u00fe\u0102"+
		"\u0106\u010a\u010e\u011a\u011d\u0120\u0127\u0130\u013a\u0140";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}