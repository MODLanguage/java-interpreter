// Generated from /Users/alex/code/NUM/MODL/MODL_grammar/src/main/java/uk/modl/parser/antlr/MODLParser.g4 by ANTLR 4.7
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
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		NULL=1, TRUE=2, FALSE=3, WS=4, NEWLINE=5, COLON=6, EQUALS=7, SC=8, LBRAC=9, 
		RBRAC=10, LSBRAC=11, RSBRAC=12, NUMBER=13, COMMENT=14, STRING=15, HASH_PREFIX=16, 
		QUOTED=17, GRAVED=18, LCBRAC=19, CWS=20, QMARK=21, FSLASH=22, GTHAN=23, 
		LTHAN=24, ASTERISK=25, AMP=26, PIPE=27, EXCLAM=28, CCOMMENT=29, RCBRAC=30;
	public static final int
		RULE_modl = 0, RULE_structure = 1, RULE_map = 2, RULE_array = 3, RULE_pair = 4, 
		RULE_top_level_conditional = 5, RULE_top_level_conditional_return = 6, 
		RULE_map_conditional = 7, RULE_map_conditional_return = 8, RULE_map_item = 9, 
		RULE_array_conditional = 10, RULE_array_conditional_return = 11, RULE_array_item = 12, 
		RULE_value_conditional = 13, RULE_value_conditional_return = 14, RULE_value_item = 15, 
		RULE_condition_test = 16, RULE_condition = 17, RULE_condition_group = 18, 
		RULE_value = 19;
	public static final String[] ruleNames = {
		"modl", "structure", "map", "array", "pair", "top_level_conditional", 
		"top_level_conditional_return", "map_conditional", "map_conditional_return", 
		"map_item", "array_conditional", "array_conditional_return", "array_item", 
		"value_conditional", "value_conditional_return", "value_item", "condition_test", 
		"condition", "condition_group", "value"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, "'{'", null, "'?'", "'/'", "'>'", 
		"'<'", "'*'", "'&'", "'|'", "'!'", null, "'}'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "NULL", "TRUE", "FALSE", "WS", "NEWLINE", "COLON", "EQUALS", "SC", 
		"LBRAC", "RBRAC", "LSBRAC", "RSBRAC", "NUMBER", "COMMENT", "STRING", "HASH_PREFIX", 
		"QUOTED", "GRAVED", "LCBRAC", "CWS", "QMARK", "FSLASH", "GTHAN", "LTHAN", 
		"ASTERISK", "AMP", "PIPE", "EXCLAM", "CCOMMENT", "RCBRAC"
	};
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
		public List<StructureContext> structure() {
			return getRuleContexts(StructureContext.class);
		}
		public StructureContext structure(int i) {
			return getRuleContext(StructureContext.class,i);
		}
		public TerminalNode EOF() { return getToken(MODLParser.EOF, 0); }
		public List<TerminalNode> NEWLINE() { return getTokens(MODLParser.NEWLINE); }
		public TerminalNode NEWLINE(int i) {
			return getToken(MODLParser.NEWLINE, i);
		}
		public List<TerminalNode> SC() { return getTokens(MODLParser.SC); }
		public TerminalNode SC(int i) {
			return getToken(MODLParser.SC, i);
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
			setState(43);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(40);
				match(NEWLINE);
				}
				}
				setState(45);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(46);
			structure();
			setState(64);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(59);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
					case 1:
						{
						setState(47);
						match(SC);
						}
						break;
					case 2:
						{
						setState(49); 
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
							{
							setState(48);
							match(NEWLINE);
							}
							}
							setState(51); 
							_errHandler.sync(this);
							_la = _input.LA(1);
						} while ( _la==NEWLINE );
						}
						break;
					case 3:
						{
						setState(53);
						match(SC);
						setState(55); 
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
							{
							setState(54);
							match(NEWLINE);
							}
							}
							setState(57); 
							_errHandler.sync(this);
							_la = _input.LA(1);
						} while ( _la==NEWLINE );
						}
						break;
					}
					setState(61);
					structure();
					}
					} 
				}
				setState(66);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			}
			setState(68);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SC) {
				{
				setState(67);
				match(SC);
				}
			}

			setState(73);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(70);
				match(NEWLINE);
				}
				}
				setState(75);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(76);
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

	public static class StructureContext extends ParserRuleContext {
		public MapContext map() {
			return getRuleContext(MapContext.class,0);
		}
		public ArrayContext array() {
			return getRuleContext(ArrayContext.class,0);
		}
		public Top_level_conditionalContext top_level_conditional() {
			return getRuleContext(Top_level_conditionalContext.class,0);
		}
		public PairContext pair() {
			return getRuleContext(PairContext.class,0);
		}
		public StructureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_structure; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterStructure(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitStructure(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitStructure(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StructureContext structure() throws RecognitionException {
		StructureContext _localctx = new StructureContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_structure);
		try {
			setState(82);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LBRAC:
				enterOuterAlt(_localctx, 1);
				{
				setState(78);
				map();
				}
				break;
			case LSBRAC:
				enterOuterAlt(_localctx, 2);
				{
				setState(79);
				array();
				}
				break;
			case LCBRAC:
				enterOuterAlt(_localctx, 3);
				{
				setState(80);
				top_level_conditional();
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 4);
				{
				setState(81);
				pair();
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

	public static class MapContext extends ParserRuleContext {
		public TerminalNode LBRAC() { return getToken(MODLParser.LBRAC, 0); }
		public List<Map_itemContext> map_item() {
			return getRuleContexts(Map_itemContext.class);
		}
		public Map_itemContext map_item(int i) {
			return getRuleContext(Map_itemContext.class,i);
		}
		public TerminalNode RBRAC() { return getToken(MODLParser.RBRAC, 0); }
		public List<TerminalNode> NEWLINE() { return getTokens(MODLParser.NEWLINE); }
		public TerminalNode NEWLINE(int i) {
			return getToken(MODLParser.NEWLINE, i);
		}
		public List<TerminalNode> SC() { return getTokens(MODLParser.SC); }
		public TerminalNode SC(int i) {
			return getToken(MODLParser.SC, i);
		}
		public MapContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_map; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterMap(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitMap(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitMap(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapContext map() throws RecognitionException {
		MapContext _localctx = new MapContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_map);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(84);
			match(LBRAC);
			setState(88);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(85);
				match(NEWLINE);
				}
				}
				setState(90);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(91);
			map_item();
			setState(104);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(93);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==SC) {
						{
						setState(92);
						match(SC);
						}
					}

					setState(98);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==NEWLINE) {
						{
						{
						setState(95);
						match(NEWLINE);
						}
						}
						setState(100);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(101);
					map_item();
					}
					} 
				}
				setState(106);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			}
			setState(110);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(107);
				match(NEWLINE);
				}
				}
				setState(112);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(113);
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

	public static class ArrayContext extends ParserRuleContext {
		public TerminalNode LSBRAC() { return getToken(MODLParser.LSBRAC, 0); }
		public List<Array_itemContext> array_item() {
			return getRuleContexts(Array_itemContext.class);
		}
		public Array_itemContext array_item(int i) {
			return getRuleContext(Array_itemContext.class,i);
		}
		public TerminalNode RSBRAC() { return getToken(MODLParser.RSBRAC, 0); }
		public List<TerminalNode> NEWLINE() { return getTokens(MODLParser.NEWLINE); }
		public TerminalNode NEWLINE(int i) {
			return getToken(MODLParser.NEWLINE, i);
		}
		public List<TerminalNode> SC() { return getTokens(MODLParser.SC); }
		public TerminalNode SC(int i) {
			return getToken(MODLParser.SC, i);
		}
		public ArrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterArray(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitArray(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitArray(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayContext array() throws RecognitionException {
		ArrayContext _localctx = new ArrayContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_array);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(115);
			match(LSBRAC);
			setState(119);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(116);
				match(NEWLINE);
				}
				}
				setState(121);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(122);
			array_item();
			setState(135);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(124);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==SC) {
						{
						setState(123);
						match(SC);
						}
					}

					setState(129);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==NEWLINE) {
						{
						{
						setState(126);
						match(NEWLINE);
						}
						}
						setState(131);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(132);
					array_item();
					}
					} 
				}
				setState(137);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			}
			setState(139);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SC) {
				{
				setState(138);
				match(SC);
				}
			}

			setState(144);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(141);
				match(NEWLINE);
				}
				}
				setState(146);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(147);
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

	public static class PairContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(MODLParser.STRING, 0); }
		public TerminalNode EQUALS() { return getToken(MODLParser.EQUALS, 0); }
		public List<Value_itemContext> value_item() {
			return getRuleContexts(Value_itemContext.class);
		}
		public Value_itemContext value_item(int i) {
			return getRuleContext(Value_itemContext.class,i);
		}
		public List<TerminalNode> COLON() { return getTokens(MODLParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(MODLParser.COLON, i);
		}
		public List<TerminalNode> NEWLINE() { return getTokens(MODLParser.NEWLINE); }
		public TerminalNode NEWLINE(int i) {
			return getToken(MODLParser.NEWLINE, i);
		}
		public MapContext map() {
			return getRuleContext(MapContext.class,0);
		}
		public ArrayContext array() {
			return getRuleContext(ArrayContext.class,0);
		}
		public PairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pair; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterPair(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitPair(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitPair(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PairContext pair() throws RecognitionException {
		PairContext _localctx = new PairContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_pair);
		int _la;
		try {
			int _alt;
			setState(169);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(149);
				match(STRING);
				setState(150);
				match(EQUALS);
				setState(151);
				value_item();
				setState(162);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(155);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while (_la==NEWLINE) {
							{
							{
							setState(152);
							match(NEWLINE);
							}
							}
							setState(157);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						setState(158);
						match(COLON);
						setState(159);
						value_item();
						}
						} 
					}
					setState(164);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(165);
				match(STRING);
				setState(166);
				map();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(167);
				match(STRING);
				setState(168);
				array();
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

	public static class Top_level_conditionalContext extends ParserRuleContext {
		public TerminalNode LCBRAC() { return getToken(MODLParser.LCBRAC, 0); }
		public List<Condition_testContext> condition_test() {
			return getRuleContexts(Condition_testContext.class);
		}
		public Condition_testContext condition_test(int i) {
			return getRuleContext(Condition_testContext.class,i);
		}
		public List<TerminalNode> QMARK() { return getTokens(MODLParser.QMARK); }
		public TerminalNode QMARK(int i) {
			return getToken(MODLParser.QMARK, i);
		}
		public List<Top_level_conditional_returnContext> top_level_conditional_return() {
			return getRuleContexts(Top_level_conditional_returnContext.class);
		}
		public Top_level_conditional_returnContext top_level_conditional_return(int i) {
			return getRuleContext(Top_level_conditional_returnContext.class,i);
		}
		public TerminalNode RCBRAC() { return getToken(MODLParser.RCBRAC, 0); }
		public List<TerminalNode> NEWLINE() { return getTokens(MODLParser.NEWLINE); }
		public TerminalNode NEWLINE(int i) {
			return getToken(MODLParser.NEWLINE, i);
		}
		public List<TerminalNode> FSLASH() { return getTokens(MODLParser.FSLASH); }
		public TerminalNode FSLASH(int i) {
			return getToken(MODLParser.FSLASH, i);
		}
		public Top_level_conditionalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_top_level_conditional; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterTop_level_conditional(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitTop_level_conditional(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitTop_level_conditional(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Top_level_conditionalContext top_level_conditional() throws RecognitionException {
		Top_level_conditionalContext _localctx = new Top_level_conditionalContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_top_level_conditional);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(171);
			match(LCBRAC);
			setState(175);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(172);
					match(NEWLINE);
					}
					} 
				}
				setState(177);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
			}
			setState(178);
			condition_test();
			setState(179);
			match(QMARK);
			setState(183);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(180);
				match(NEWLINE);
				}
				}
				setState(185);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(186);
			top_level_conditional_return();
			setState(190);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(187);
					match(NEWLINE);
					}
					} 
				}
				setState(192);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
			}
			setState(213);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==FSLASH) {
				{
				{
				setState(193);
				match(FSLASH);
				setState(197);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(194);
						match(NEWLINE);
						}
						} 
					}
					setState(199);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
				}
				setState(201);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NULL) | (1L << TRUE) | (1L << FALSE) | (1L << NEWLINE) | (1L << EQUALS) | (1L << LBRAC) | (1L << LSBRAC) | (1L << NUMBER) | (1L << STRING) | (1L << QUOTED) | (1L << LCBRAC) | (1L << GTHAN) | (1L << LTHAN) | (1L << ASTERISK) | (1L << EXCLAM))) != 0)) {
					{
					setState(200);
					condition_test();
					}
				}

				setState(203);
				match(QMARK);
				setState(207);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NEWLINE) {
					{
					{
					setState(204);
					match(NEWLINE);
					}
					}
					setState(209);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(210);
				top_level_conditional_return();
				}
				}
				setState(215);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(219);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(216);
				match(NEWLINE);
				}
				}
				setState(221);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(222);
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

	public static class Top_level_conditional_returnContext extends ParserRuleContext {
		public List<StructureContext> structure() {
			return getRuleContexts(StructureContext.class);
		}
		public StructureContext structure(int i) {
			return getRuleContext(StructureContext.class,i);
		}
		public List<TerminalNode> SC() { return getTokens(MODLParser.SC); }
		public TerminalNode SC(int i) {
			return getToken(MODLParser.SC, i);
		}
		public List<TerminalNode> NEWLINE() { return getTokens(MODLParser.NEWLINE); }
		public TerminalNode NEWLINE(int i) {
			return getToken(MODLParser.NEWLINE, i);
		}
		public Top_level_conditional_returnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_top_level_conditional_return; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterTop_level_conditional_return(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitTop_level_conditional_return(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitTop_level_conditional_return(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Top_level_conditional_returnContext top_level_conditional_return() throws RecognitionException {
		Top_level_conditional_returnContext _localctx = new Top_level_conditional_returnContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_top_level_conditional_return);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(224);
			structure();
			setState(234);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,31,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(229);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
					case 1:
						{
						setState(225);
						match(SC);
						}
						break;
					case 2:
						{
						setState(226);
						match(NEWLINE);
						}
						break;
					case 3:
						{
						setState(227);
						match(SC);
						setState(228);
						match(NEWLINE);
						}
						break;
					}
					setState(231);
					structure();
					}
					} 
				}
				setState(236);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,31,_ctx);
			}
			setState(238);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SC) {
				{
				setState(237);
				match(SC);
				}
			}

			setState(243);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,33,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(240);
					match(NEWLINE);
					}
					} 
				}
				setState(245);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,33,_ctx);
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

	public static class Map_conditionalContext extends ParserRuleContext {
		public TerminalNode LCBRAC() { return getToken(MODLParser.LCBRAC, 0); }
		public List<Condition_testContext> condition_test() {
			return getRuleContexts(Condition_testContext.class);
		}
		public Condition_testContext condition_test(int i) {
			return getRuleContext(Condition_testContext.class,i);
		}
		public List<TerminalNode> QMARK() { return getTokens(MODLParser.QMARK); }
		public TerminalNode QMARK(int i) {
			return getToken(MODLParser.QMARK, i);
		}
		public List<Map_conditional_returnContext> map_conditional_return() {
			return getRuleContexts(Map_conditional_returnContext.class);
		}
		public Map_conditional_returnContext map_conditional_return(int i) {
			return getRuleContext(Map_conditional_returnContext.class,i);
		}
		public TerminalNode RCBRAC() { return getToken(MODLParser.RCBRAC, 0); }
		public List<TerminalNode> NEWLINE() { return getTokens(MODLParser.NEWLINE); }
		public TerminalNode NEWLINE(int i) {
			return getToken(MODLParser.NEWLINE, i);
		}
		public List<TerminalNode> FSLASH() { return getTokens(MODLParser.FSLASH); }
		public TerminalNode FSLASH(int i) {
			return getToken(MODLParser.FSLASH, i);
		}
		public Map_conditionalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_map_conditional; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterMap_conditional(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitMap_conditional(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitMap_conditional(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Map_conditionalContext map_conditional() throws RecognitionException {
		Map_conditionalContext _localctx = new Map_conditionalContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_map_conditional);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(246);
			match(LCBRAC);
			setState(250);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(247);
					match(NEWLINE);
					}
					} 
				}
				setState(252);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
			}
			setState(253);
			condition_test();
			setState(254);
			match(QMARK);
			setState(258);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(255);
				match(NEWLINE);
				}
				}
				setState(260);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(261);
			map_conditional_return();
			setState(265);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,36,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(262);
					match(NEWLINE);
					}
					} 
				}
				setState(267);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,36,_ctx);
			}
			setState(288);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==FSLASH) {
				{
				{
				setState(268);
				match(FSLASH);
				setState(272);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,37,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(269);
						match(NEWLINE);
						}
						} 
					}
					setState(274);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,37,_ctx);
				}
				setState(276);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NULL) | (1L << TRUE) | (1L << FALSE) | (1L << NEWLINE) | (1L << EQUALS) | (1L << LBRAC) | (1L << LSBRAC) | (1L << NUMBER) | (1L << STRING) | (1L << QUOTED) | (1L << LCBRAC) | (1L << GTHAN) | (1L << LTHAN) | (1L << ASTERISK) | (1L << EXCLAM))) != 0)) {
					{
					setState(275);
					condition_test();
					}
				}

				setState(278);
				match(QMARK);
				setState(282);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NEWLINE) {
					{
					{
					setState(279);
					match(NEWLINE);
					}
					}
					setState(284);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(285);
				map_conditional_return();
				}
				}
				setState(290);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(294);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(291);
				match(NEWLINE);
				}
				}
				setState(296);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(297);
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

	public static class Map_conditional_returnContext extends ParserRuleContext {
		public List<Map_itemContext> map_item() {
			return getRuleContexts(Map_itemContext.class);
		}
		public Map_itemContext map_item(int i) {
			return getRuleContext(Map_itemContext.class,i);
		}
		public List<TerminalNode> SC() { return getTokens(MODLParser.SC); }
		public TerminalNode SC(int i) {
			return getToken(MODLParser.SC, i);
		}
		public List<TerminalNode> NEWLINE() { return getTokens(MODLParser.NEWLINE); }
		public TerminalNode NEWLINE(int i) {
			return getToken(MODLParser.NEWLINE, i);
		}
		public Map_conditional_returnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_map_conditional_return; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterMap_conditional_return(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitMap_conditional_return(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitMap_conditional_return(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Map_conditional_returnContext map_conditional_return() throws RecognitionException {
		Map_conditional_returnContext _localctx = new Map_conditional_returnContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_map_conditional_return);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(299);
			map_item();
			setState(319);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,45,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(314);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
					case 1:
						{
						setState(300);
						match(SC);
						}
						break;
					case 2:
						{
						setState(304);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while (_la==NEWLINE) {
							{
							{
							setState(301);
							match(NEWLINE);
							}
							}
							setState(306);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						}
						break;
					case 3:
						{
						setState(307);
						match(SC);
						setState(311);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while (_la==NEWLINE) {
							{
							{
							setState(308);
							match(NEWLINE);
							}
							}
							setState(313);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						}
						break;
					}
					setState(316);
					map_item();
					}
					} 
				}
				setState(321);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,45,_ctx);
			}
			setState(323);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SC) {
				{
				setState(322);
				match(SC);
				}
			}

			setState(328);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,47,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(325);
					match(NEWLINE);
					}
					} 
				}
				setState(330);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,47,_ctx);
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

	public static class Map_itemContext extends ParserRuleContext {
		public PairContext pair() {
			return getRuleContext(PairContext.class,0);
		}
		public Map_conditionalContext map_conditional() {
			return getRuleContext(Map_conditionalContext.class,0);
		}
		public Map_itemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_map_item; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterMap_item(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitMap_item(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitMap_item(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Map_itemContext map_item() throws RecognitionException {
		Map_itemContext _localctx = new Map_itemContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_map_item);
		try {
			setState(333);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(331);
				pair();
				}
				break;
			case LCBRAC:
				enterOuterAlt(_localctx, 2);
				{
				setState(332);
				map_conditional();
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

	public static class Array_conditionalContext extends ParserRuleContext {
		public TerminalNode LCBRAC() { return getToken(MODLParser.LCBRAC, 0); }
		public List<Condition_testContext> condition_test() {
			return getRuleContexts(Condition_testContext.class);
		}
		public Condition_testContext condition_test(int i) {
			return getRuleContext(Condition_testContext.class,i);
		}
		public List<TerminalNode> QMARK() { return getTokens(MODLParser.QMARK); }
		public TerminalNode QMARK(int i) {
			return getToken(MODLParser.QMARK, i);
		}
		public List<Array_conditional_returnContext> array_conditional_return() {
			return getRuleContexts(Array_conditional_returnContext.class);
		}
		public Array_conditional_returnContext array_conditional_return(int i) {
			return getRuleContext(Array_conditional_returnContext.class,i);
		}
		public TerminalNode RCBRAC() { return getToken(MODLParser.RCBRAC, 0); }
		public List<TerminalNode> NEWLINE() { return getTokens(MODLParser.NEWLINE); }
		public TerminalNode NEWLINE(int i) {
			return getToken(MODLParser.NEWLINE, i);
		}
		public List<TerminalNode> FSLASH() { return getTokens(MODLParser.FSLASH); }
		public TerminalNode FSLASH(int i) {
			return getToken(MODLParser.FSLASH, i);
		}
		public Array_conditionalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array_conditional; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterArray_conditional(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitArray_conditional(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitArray_conditional(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Array_conditionalContext array_conditional() throws RecognitionException {
		Array_conditionalContext _localctx = new Array_conditionalContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_array_conditional);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(335);
			match(LCBRAC);
			setState(339);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,49,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(336);
					match(NEWLINE);
					}
					} 
				}
				setState(341);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,49,_ctx);
			}
			setState(342);
			condition_test();
			setState(343);
			match(QMARK);
			setState(347);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(344);
				match(NEWLINE);
				}
				}
				setState(349);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(350);
			array_conditional_return();
			setState(354);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,51,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(351);
					match(NEWLINE);
					}
					} 
				}
				setState(356);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,51,_ctx);
			}
			setState(377);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==FSLASH) {
				{
				{
				setState(357);
				match(FSLASH);
				setState(361);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,52,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(358);
						match(NEWLINE);
						}
						} 
					}
					setState(363);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,52,_ctx);
				}
				setState(365);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NULL) | (1L << TRUE) | (1L << FALSE) | (1L << NEWLINE) | (1L << EQUALS) | (1L << LBRAC) | (1L << LSBRAC) | (1L << NUMBER) | (1L << STRING) | (1L << QUOTED) | (1L << LCBRAC) | (1L << GTHAN) | (1L << LTHAN) | (1L << ASTERISK) | (1L << EXCLAM))) != 0)) {
					{
					setState(364);
					condition_test();
					}
				}

				setState(367);
				match(QMARK);
				setState(371);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NEWLINE) {
					{
					{
					setState(368);
					match(NEWLINE);
					}
					}
					setState(373);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(374);
				array_conditional_return();
				}
				}
				setState(379);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(383);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(380);
				match(NEWLINE);
				}
				}
				setState(385);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(386);
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

	public static class Array_conditional_returnContext extends ParserRuleContext {
		public List<Array_itemContext> array_item() {
			return getRuleContexts(Array_itemContext.class);
		}
		public Array_itemContext array_item(int i) {
			return getRuleContext(Array_itemContext.class,i);
		}
		public List<TerminalNode> SC() { return getTokens(MODLParser.SC); }
		public TerminalNode SC(int i) {
			return getToken(MODLParser.SC, i);
		}
		public List<TerminalNode> NEWLINE() { return getTokens(MODLParser.NEWLINE); }
		public TerminalNode NEWLINE(int i) {
			return getToken(MODLParser.NEWLINE, i);
		}
		public Array_conditional_returnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array_conditional_return; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterArray_conditional_return(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitArray_conditional_return(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitArray_conditional_return(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Array_conditional_returnContext array_conditional_return() throws RecognitionException {
		Array_conditional_returnContext _localctx = new Array_conditional_returnContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_array_conditional_return);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(388);
			array_item();
			setState(408);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,60,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(403);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,59,_ctx) ) {
					case 1:
						{
						setState(389);
						match(SC);
						}
						break;
					case 2:
						{
						setState(393);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while (_la==NEWLINE) {
							{
							{
							setState(390);
							match(NEWLINE);
							}
							}
							setState(395);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						}
						break;
					case 3:
						{
						setState(396);
						match(SC);
						setState(400);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while (_la==NEWLINE) {
							{
							{
							setState(397);
							match(NEWLINE);
							}
							}
							setState(402);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						}
						break;
					}
					setState(405);
					array_item();
					}
					} 
				}
				setState(410);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,60,_ctx);
			}
			setState(412);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SC) {
				{
				setState(411);
				match(SC);
				}
			}

			setState(417);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,62,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(414);
					match(NEWLINE);
					}
					} 
				}
				setState(419);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,62,_ctx);
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

	public static class Array_itemContext extends ParserRuleContext {
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public Array_conditionalContext array_conditional() {
			return getRuleContext(Array_conditionalContext.class,0);
		}
		public Array_itemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array_item; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterArray_item(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitArray_item(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitArray_item(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Array_itemContext array_item() throws RecognitionException {
		Array_itemContext _localctx = new Array_itemContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_array_item);
		try {
			setState(422);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NULL:
			case TRUE:
			case FALSE:
			case LBRAC:
			case LSBRAC:
			case NUMBER:
			case STRING:
			case QUOTED:
				enterOuterAlt(_localctx, 1);
				{
				setState(420);
				value();
				}
				break;
			case LCBRAC:
				enterOuterAlt(_localctx, 2);
				{
				setState(421);
				array_conditional();
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

	public static class Value_conditionalContext extends ParserRuleContext {
		public TerminalNode LCBRAC() { return getToken(MODLParser.LCBRAC, 0); }
		public List<Condition_testContext> condition_test() {
			return getRuleContexts(Condition_testContext.class);
		}
		public Condition_testContext condition_test(int i) {
			return getRuleContext(Condition_testContext.class,i);
		}
		public List<TerminalNode> QMARK() { return getTokens(MODLParser.QMARK); }
		public TerminalNode QMARK(int i) {
			return getToken(MODLParser.QMARK, i);
		}
		public List<Value_conditional_returnContext> value_conditional_return() {
			return getRuleContexts(Value_conditional_returnContext.class);
		}
		public Value_conditional_returnContext value_conditional_return(int i) {
			return getRuleContext(Value_conditional_returnContext.class,i);
		}
		public TerminalNode RCBRAC() { return getToken(MODLParser.RCBRAC, 0); }
		public List<TerminalNode> FSLASH() { return getTokens(MODLParser.FSLASH); }
		public TerminalNode FSLASH(int i) {
			return getToken(MODLParser.FSLASH, i);
		}
		public List<TerminalNode> NEWLINE() { return getTokens(MODLParser.NEWLINE); }
		public TerminalNode NEWLINE(int i) {
			return getToken(MODLParser.NEWLINE, i);
		}
		public Value_conditionalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value_conditional; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterValue_conditional(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitValue_conditional(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitValue_conditional(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Value_conditionalContext value_conditional() throws RecognitionException {
		Value_conditionalContext _localctx = new Value_conditionalContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_value_conditional);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(424);
			match(LCBRAC);
			setState(428);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,64,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(425);
					match(NEWLINE);
					}
					} 
				}
				setState(430);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,64,_ctx);
			}
			setState(431);
			condition_test();
			setState(432);
			match(QMARK);
			setState(436);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(433);
				match(NEWLINE);
				}
				}
				setState(438);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(439);
			value_conditional_return();
			setState(443);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,66,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(440);
					match(NEWLINE);
					}
					} 
				}
				setState(445);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,66,_ctx);
			}
			setState(465);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,69,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(446);
					match(FSLASH);
					setState(450);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,67,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(447);
							match(NEWLINE);
							}
							} 
						}
						setState(452);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,67,_ctx);
					}
					setState(453);
					condition_test();
					setState(454);
					match(QMARK);
					setState(458);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==NEWLINE) {
						{
						{
						setState(455);
						match(NEWLINE);
						}
						}
						setState(460);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(461);
					value_conditional_return();
					}
					} 
				}
				setState(467);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,69,_ctx);
			}
			setState(471);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(468);
				match(NEWLINE);
				}
				}
				setState(473);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			{
			setState(474);
			match(FSLASH);
			setState(478);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(475);
				match(NEWLINE);
				}
				}
				setState(480);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(481);
			match(QMARK);
			setState(485);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(482);
				match(NEWLINE);
				}
				}
				setState(487);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(488);
			value_conditional_return();
			}
			setState(493);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(490);
				match(NEWLINE);
				}
				}
				setState(495);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(496);
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

	public static class Value_conditional_returnContext extends ParserRuleContext {
		public List<Value_itemContext> value_item() {
			return getRuleContexts(Value_itemContext.class);
		}
		public Value_itemContext value_item(int i) {
			return getRuleContext(Value_itemContext.class,i);
		}
		public List<TerminalNode> COLON() { return getTokens(MODLParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(MODLParser.COLON, i);
		}
		public List<TerminalNode> NEWLINE() { return getTokens(MODLParser.NEWLINE); }
		public TerminalNode NEWLINE(int i) {
			return getToken(MODLParser.NEWLINE, i);
		}
		public Value_conditional_returnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value_conditional_return; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterValue_conditional_return(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitValue_conditional_return(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitValue_conditional_return(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Value_conditional_returnContext value_conditional_return() throws RecognitionException {
		Value_conditional_returnContext _localctx = new Value_conditional_returnContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_value_conditional_return);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(498);
			value_item();
			setState(509);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,75,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(502);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==NEWLINE) {
						{
						{
						setState(499);
						match(NEWLINE);
						}
						}
						setState(504);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(505);
					match(COLON);
					setState(506);
					value_item();
					}
					} 
				}
				setState(511);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,75,_ctx);
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

	public static class Value_itemContext extends ParserRuleContext {
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public TerminalNode LBRAC() { return getToken(MODLParser.LBRAC, 0); }
		public List<Value_itemContext> value_item() {
			return getRuleContexts(Value_itemContext.class);
		}
		public Value_itemContext value_item(int i) {
			return getRuleContext(Value_itemContext.class,i);
		}
		public TerminalNode RBRAC() { return getToken(MODLParser.RBRAC, 0); }
		public Value_conditionalContext value_conditional() {
			return getRuleContext(Value_conditionalContext.class,0);
		}
		public List<TerminalNode> COLON() { return getTokens(MODLParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(MODLParser.COLON, i);
		}
		public List<TerminalNode> NEWLINE() { return getTokens(MODLParser.NEWLINE); }
		public TerminalNode NEWLINE(int i) {
			return getToken(MODLParser.NEWLINE, i);
		}
		public Value_itemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value_item; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterValue_item(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitValue_item(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitValue_item(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Value_itemContext value_item() throws RecognitionException {
		Value_itemContext _localctx = new Value_itemContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_value_item);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(531);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,78,_ctx) ) {
			case 1:
				{
				setState(512);
				value();
				}
				break;
			case 2:
				{
				setState(513);
				match(LBRAC);
				setState(514);
				value_item();
				setState(525);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NEWLINE || _la==COLON) {
					{
					{
					setState(518);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==NEWLINE) {
						{
						{
						setState(515);
						match(NEWLINE);
						}
						}
						setState(520);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(521);
					match(COLON);
					setState(522);
					value_item();
					}
					}
					setState(527);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(528);
				match(RBRAC);
				}
				break;
			case 3:
				{
				setState(530);
				value_conditional();
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

	public static class Condition_testContext extends ParserRuleContext {
		public List<ConditionContext> condition() {
			return getRuleContexts(ConditionContext.class);
		}
		public ConditionContext condition(int i) {
			return getRuleContext(ConditionContext.class,i);
		}
		public List<Condition_groupContext> condition_group() {
			return getRuleContexts(Condition_groupContext.class);
		}
		public Condition_groupContext condition_group(int i) {
			return getRuleContext(Condition_groupContext.class,i);
		}
		public TerminalNode EXCLAM() { return getToken(MODLParser.EXCLAM, 0); }
		public List<TerminalNode> AMP() { return getTokens(MODLParser.AMP); }
		public TerminalNode AMP(int i) {
			return getToken(MODLParser.AMP, i);
		}
		public List<TerminalNode> PIPE() { return getTokens(MODLParser.PIPE); }
		public TerminalNode PIPE(int i) {
			return getToken(MODLParser.PIPE, i);
		}
		public Condition_testContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_condition_test; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterCondition_test(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitCondition_test(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitCondition_test(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Condition_testContext condition_test() throws RecognitionException {
		Condition_testContext _localctx = new Condition_testContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_condition_test);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(534);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,79,_ctx) ) {
			case 1:
				{
				setState(533);
				match(EXCLAM);
				}
				break;
			}
			setState(538);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NULL:
			case TRUE:
			case FALSE:
			case NEWLINE:
			case EQUALS:
			case LBRAC:
			case LSBRAC:
			case NUMBER:
			case STRING:
			case QUOTED:
			case GTHAN:
			case LTHAN:
			case ASTERISK:
			case EXCLAM:
				{
				setState(536);
				condition();
				}
				break;
			case LCBRAC:
				{
				setState(537);
				condition_group();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(547);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,82,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(540);
					_la = _input.LA(1);
					if ( !(_la==AMP || _la==PIPE) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(543);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case NULL:
					case TRUE:
					case FALSE:
					case NEWLINE:
					case EQUALS:
					case LBRAC:
					case LSBRAC:
					case NUMBER:
					case STRING:
					case QUOTED:
					case GTHAN:
					case LTHAN:
					case ASTERISK:
					case EXCLAM:
						{
						setState(541);
						condition();
						}
						break;
					case LCBRAC:
						{
						setState(542);
						condition_group();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					}
					} 
				}
				setState(549);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,82,_ctx);
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

	public static class ConditionContext extends ParserRuleContext {
		public List<ValueContext> value() {
			return getRuleContexts(ValueContext.class);
		}
		public ValueContext value(int i) {
			return getRuleContext(ValueContext.class,i);
		}
		public List<TerminalNode> NEWLINE() { return getTokens(MODLParser.NEWLINE); }
		public TerminalNode NEWLINE(int i) {
			return getToken(MODLParser.NEWLINE, i);
		}
		public TerminalNode STRING() { return getToken(MODLParser.STRING, 0); }
		public TerminalNode EQUALS() { return getToken(MODLParser.EQUALS, 0); }
		public TerminalNode GTHAN() { return getToken(MODLParser.GTHAN, 0); }
		public TerminalNode LTHAN() { return getToken(MODLParser.LTHAN, 0); }
		public TerminalNode EXCLAM() { return getToken(MODLParser.EXCLAM, 0); }
		public List<TerminalNode> ASTERISK() { return getTokens(MODLParser.ASTERISK); }
		public TerminalNode ASTERISK(int i) {
			return getToken(MODLParser.ASTERISK, i);
		}
		public List<TerminalNode> PIPE() { return getTokens(MODLParser.PIPE); }
		public TerminalNode PIPE(int i) {
			return getToken(MODLParser.PIPE, i);
		}
		public ConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_condition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitCondition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionContext condition() throws RecognitionException {
		ConditionContext _localctx = new ConditionContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_condition);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(553);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(550);
				match(NEWLINE);
				}
				}
				setState(555);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(557);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,84,_ctx) ) {
			case 1:
				{
				setState(556);
				match(STRING);
				}
				break;
			}
			setState(568);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,85,_ctx) ) {
			case 1:
				{
				setState(559);
				match(EQUALS);
				}
				break;
			case 2:
				{
				setState(560);
				match(GTHAN);
				}
				break;
			case 3:
				{
				setState(561);
				match(GTHAN);
				setState(562);
				match(EQUALS);
				}
				break;
			case 4:
				{
				setState(563);
				match(LTHAN);
				}
				break;
			case 5:
				{
				setState(564);
				match(LTHAN);
				setState(565);
				match(EQUALS);
				}
				break;
			case 6:
				{
				setState(566);
				match(EXCLAM);
				setState(567);
				match(EQUALS);
				}
				break;
			}
			{
			setState(571);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASTERISK) {
				{
				setState(570);
				match(ASTERISK);
				}
			}

			setState(573);
			value();
			setState(575);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASTERISK) {
				{
				setState(574);
				match(ASTERISK);
				}
			}

			setState(587);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,90,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(577);
					match(PIPE);
					setState(579);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==ASTERISK) {
						{
						setState(578);
						match(ASTERISK);
						}
					}

					setState(581);
					value();
					setState(583);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==ASTERISK) {
						{
						setState(582);
						match(ASTERISK);
						}
					}

					}
					} 
				}
				setState(589);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,90,_ctx);
			}
			}
			setState(593);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(590);
				match(NEWLINE);
				}
				}
				setState(595);
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

	public static class Condition_groupContext extends ParserRuleContext {
		public TerminalNode LCBRAC() { return getToken(MODLParser.LCBRAC, 0); }
		public List<Condition_testContext> condition_test() {
			return getRuleContexts(Condition_testContext.class);
		}
		public Condition_testContext condition_test(int i) {
			return getRuleContext(Condition_testContext.class,i);
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
		public Condition_groupContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_condition_group; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterCondition_group(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitCondition_group(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitCondition_group(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Condition_groupContext condition_group() throws RecognitionException {
		Condition_groupContext _localctx = new Condition_groupContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_condition_group);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(596);
			match(LCBRAC);
			setState(597);
			condition_test();
			setState(602);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AMP || _la==PIPE) {
				{
				{
				setState(598);
				_la = _input.LA(1);
				if ( !(_la==AMP || _la==PIPE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(599);
				condition_test();
				}
				}
				setState(604);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(605);
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

	public static class ValueContext extends ParserRuleContext {
		public MapContext map() {
			return getRuleContext(MapContext.class,0);
		}
		public ArrayContext array() {
			return getRuleContext(ArrayContext.class,0);
		}
		public PairContext pair() {
			return getRuleContext(PairContext.class,0);
		}
		public TerminalNode QUOTED() { return getToken(MODLParser.QUOTED, 0); }
		public TerminalNode NUMBER() { return getToken(MODLParser.NUMBER, 0); }
		public TerminalNode STRING() { return getToken(MODLParser.STRING, 0); }
		public TerminalNode TRUE() { return getToken(MODLParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(MODLParser.FALSE, 0); }
		public TerminalNode NULL() { return getToken(MODLParser.NULL, 0); }
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MODLParserListener ) ((MODLParserListener)listener).exitValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MODLParserVisitor ) return ((MODLParserVisitor<? extends T>)visitor).visitValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_value);
		try {
			setState(616);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,93,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(607);
				map();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(608);
				array();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(609);
				pair();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(610);
				match(QUOTED);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(611);
				match(NUMBER);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(612);
				match(STRING);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(613);
				match(TRUE);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(614);
				match(FALSE);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(615);
				match(NULL);
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

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3 \u026d\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\3\2\7\2,\n\2\f\2\16\2/\13\2\3\2\3\2\3\2"+
		"\6\2\64\n\2\r\2\16\2\65\3\2\3\2\6\2:\n\2\r\2\16\2;\5\2>\n\2\3\2\7\2A\n"+
		"\2\f\2\16\2D\13\2\3\2\5\2G\n\2\3\2\7\2J\n\2\f\2\16\2M\13\2\3\2\3\2\3\3"+
		"\3\3\3\3\3\3\5\3U\n\3\3\4\3\4\7\4Y\n\4\f\4\16\4\\\13\4\3\4\3\4\5\4`\n"+
		"\4\3\4\7\4c\n\4\f\4\16\4f\13\4\3\4\7\4i\n\4\f\4\16\4l\13\4\3\4\7\4o\n"+
		"\4\f\4\16\4r\13\4\3\4\3\4\3\5\3\5\7\5x\n\5\f\5\16\5{\13\5\3\5\3\5\5\5"+
		"\177\n\5\3\5\7\5\u0082\n\5\f\5\16\5\u0085\13\5\3\5\7\5\u0088\n\5\f\5\16"+
		"\5\u008b\13\5\3\5\5\5\u008e\n\5\3\5\7\5\u0091\n\5\f\5\16\5\u0094\13\5"+
		"\3\5\3\5\3\6\3\6\3\6\3\6\7\6\u009c\n\6\f\6\16\6\u009f\13\6\3\6\3\6\7\6"+
		"\u00a3\n\6\f\6\16\6\u00a6\13\6\3\6\3\6\3\6\3\6\5\6\u00ac\n\6\3\7\3\7\7"+
		"\7\u00b0\n\7\f\7\16\7\u00b3\13\7\3\7\3\7\3\7\7\7\u00b8\n\7\f\7\16\7\u00bb"+
		"\13\7\3\7\3\7\7\7\u00bf\n\7\f\7\16\7\u00c2\13\7\3\7\3\7\7\7\u00c6\n\7"+
		"\f\7\16\7\u00c9\13\7\3\7\5\7\u00cc\n\7\3\7\3\7\7\7\u00d0\n\7\f\7\16\7"+
		"\u00d3\13\7\3\7\7\7\u00d6\n\7\f\7\16\7\u00d9\13\7\3\7\7\7\u00dc\n\7\f"+
		"\7\16\7\u00df\13\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\5\b\u00e8\n\b\3\b\7\b\u00eb"+
		"\n\b\f\b\16\b\u00ee\13\b\3\b\5\b\u00f1\n\b\3\b\7\b\u00f4\n\b\f\b\16\b"+
		"\u00f7\13\b\3\t\3\t\7\t\u00fb\n\t\f\t\16\t\u00fe\13\t\3\t\3\t\3\t\7\t"+
		"\u0103\n\t\f\t\16\t\u0106\13\t\3\t\3\t\7\t\u010a\n\t\f\t\16\t\u010d\13"+
		"\t\3\t\3\t\7\t\u0111\n\t\f\t\16\t\u0114\13\t\3\t\5\t\u0117\n\t\3\t\3\t"+
		"\7\t\u011b\n\t\f\t\16\t\u011e\13\t\3\t\7\t\u0121\n\t\f\t\16\t\u0124\13"+
		"\t\3\t\7\t\u0127\n\t\f\t\16\t\u012a\13\t\3\t\3\t\3\n\3\n\3\n\7\n\u0131"+
		"\n\n\f\n\16\n\u0134\13\n\3\n\3\n\7\n\u0138\n\n\f\n\16\n\u013b\13\n\5\n"+
		"\u013d\n\n\3\n\7\n\u0140\n\n\f\n\16\n\u0143\13\n\3\n\5\n\u0146\n\n\3\n"+
		"\7\n\u0149\n\n\f\n\16\n\u014c\13\n\3\13\3\13\5\13\u0150\n\13\3\f\3\f\7"+
		"\f\u0154\n\f\f\f\16\f\u0157\13\f\3\f\3\f\3\f\7\f\u015c\n\f\f\f\16\f\u015f"+
		"\13\f\3\f\3\f\7\f\u0163\n\f\f\f\16\f\u0166\13\f\3\f\3\f\7\f\u016a\n\f"+
		"\f\f\16\f\u016d\13\f\3\f\5\f\u0170\n\f\3\f\3\f\7\f\u0174\n\f\f\f\16\f"+
		"\u0177\13\f\3\f\7\f\u017a\n\f\f\f\16\f\u017d\13\f\3\f\7\f\u0180\n\f\f"+
		"\f\16\f\u0183\13\f\3\f\3\f\3\r\3\r\3\r\7\r\u018a\n\r\f\r\16\r\u018d\13"+
		"\r\3\r\3\r\7\r\u0191\n\r\f\r\16\r\u0194\13\r\5\r\u0196\n\r\3\r\7\r\u0199"+
		"\n\r\f\r\16\r\u019c\13\r\3\r\5\r\u019f\n\r\3\r\7\r\u01a2\n\r\f\r\16\r"+
		"\u01a5\13\r\3\16\3\16\5\16\u01a9\n\16\3\17\3\17\7\17\u01ad\n\17\f\17\16"+
		"\17\u01b0\13\17\3\17\3\17\3\17\7\17\u01b5\n\17\f\17\16\17\u01b8\13\17"+
		"\3\17\3\17\7\17\u01bc\n\17\f\17\16\17\u01bf\13\17\3\17\3\17\7\17\u01c3"+
		"\n\17\f\17\16\17\u01c6\13\17\3\17\3\17\3\17\7\17\u01cb\n\17\f\17\16\17"+
		"\u01ce\13\17\3\17\3\17\7\17\u01d2\n\17\f\17\16\17\u01d5\13\17\3\17\7\17"+
		"\u01d8\n\17\f\17\16\17\u01db\13\17\3\17\3\17\7\17\u01df\n\17\f\17\16\17"+
		"\u01e2\13\17\3\17\3\17\7\17\u01e6\n\17\f\17\16\17\u01e9\13\17\3\17\3\17"+
		"\3\17\7\17\u01ee\n\17\f\17\16\17\u01f1\13\17\3\17\3\17\3\20\3\20\7\20"+
		"\u01f7\n\20\f\20\16\20\u01fa\13\20\3\20\3\20\7\20\u01fe\n\20\f\20\16\20"+
		"\u0201\13\20\3\21\3\21\3\21\3\21\7\21\u0207\n\21\f\21\16\21\u020a\13\21"+
		"\3\21\3\21\7\21\u020e\n\21\f\21\16\21\u0211\13\21\3\21\3\21\3\21\5\21"+
		"\u0216\n\21\3\22\5\22\u0219\n\22\3\22\3\22\5\22\u021d\n\22\3\22\3\22\3"+
		"\22\5\22\u0222\n\22\7\22\u0224\n\22\f\22\16\22\u0227\13\22\3\23\7\23\u022a"+
		"\n\23\f\23\16\23\u022d\13\23\3\23\5\23\u0230\n\23\3\23\3\23\3\23\3\23"+
		"\3\23\3\23\3\23\3\23\3\23\5\23\u023b\n\23\3\23\5\23\u023e\n\23\3\23\3"+
		"\23\5\23\u0242\n\23\3\23\3\23\5\23\u0246\n\23\3\23\3\23\5\23\u024a\n\23"+
		"\7\23\u024c\n\23\f\23\16\23\u024f\13\23\3\23\7\23\u0252\n\23\f\23\16\23"+
		"\u0255\13\23\3\24\3\24\3\24\3\24\7\24\u025b\n\24\f\24\16\24\u025e\13\24"+
		"\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\5\25\u026b\n\25"+
		"\3\25\2\2\26\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(\2\3\3\2\34\35"+
		"\2\u02ca\2-\3\2\2\2\4T\3\2\2\2\6V\3\2\2\2\bu\3\2\2\2\n\u00ab\3\2\2\2\f"+
		"\u00ad\3\2\2\2\16\u00e2\3\2\2\2\20\u00f8\3\2\2\2\22\u012d\3\2\2\2\24\u014f"+
		"\3\2\2\2\26\u0151\3\2\2\2\30\u0186\3\2\2\2\32\u01a8\3\2\2\2\34\u01aa\3"+
		"\2\2\2\36\u01f4\3\2\2\2 \u0215\3\2\2\2\"\u0218\3\2\2\2$\u022b\3\2\2\2"+
		"&\u0256\3\2\2\2(\u026a\3\2\2\2*,\7\7\2\2+*\3\2\2\2,/\3\2\2\2-+\3\2\2\2"+
		"-.\3\2\2\2.\60\3\2\2\2/-\3\2\2\2\60B\5\4\3\2\61>\7\n\2\2\62\64\7\7\2\2"+
		"\63\62\3\2\2\2\64\65\3\2\2\2\65\63\3\2\2\2\65\66\3\2\2\2\66>\3\2\2\2\67"+
		"9\7\n\2\28:\7\7\2\298\3\2\2\2:;\3\2\2\2;9\3\2\2\2;<\3\2\2\2<>\3\2\2\2"+
		"=\61\3\2\2\2=\63\3\2\2\2=\67\3\2\2\2>?\3\2\2\2?A\5\4\3\2@=\3\2\2\2AD\3"+
		"\2\2\2B@\3\2\2\2BC\3\2\2\2CF\3\2\2\2DB\3\2\2\2EG\7\n\2\2FE\3\2\2\2FG\3"+
		"\2\2\2GK\3\2\2\2HJ\7\7\2\2IH\3\2\2\2JM\3\2\2\2KI\3\2\2\2KL\3\2\2\2LN\3"+
		"\2\2\2MK\3\2\2\2NO\7\2\2\3O\3\3\2\2\2PU\5\6\4\2QU\5\b\5\2RU\5\f\7\2SU"+
		"\5\n\6\2TP\3\2\2\2TQ\3\2\2\2TR\3\2\2\2TS\3\2\2\2U\5\3\2\2\2VZ\7\13\2\2"+
		"WY\7\7\2\2XW\3\2\2\2Y\\\3\2\2\2ZX\3\2\2\2Z[\3\2\2\2[]\3\2\2\2\\Z\3\2\2"+
		"\2]j\5\24\13\2^`\7\n\2\2_^\3\2\2\2_`\3\2\2\2`d\3\2\2\2ac\7\7\2\2ba\3\2"+
		"\2\2cf\3\2\2\2db\3\2\2\2de\3\2\2\2eg\3\2\2\2fd\3\2\2\2gi\5\24\13\2h_\3"+
		"\2\2\2il\3\2\2\2jh\3\2\2\2jk\3\2\2\2kp\3\2\2\2lj\3\2\2\2mo\7\7\2\2nm\3"+
		"\2\2\2or\3\2\2\2pn\3\2\2\2pq\3\2\2\2qs\3\2\2\2rp\3\2\2\2st\7\f\2\2t\7"+
		"\3\2\2\2uy\7\r\2\2vx\7\7\2\2wv\3\2\2\2x{\3\2\2\2yw\3\2\2\2yz\3\2\2\2z"+
		"|\3\2\2\2{y\3\2\2\2|\u0089\5\32\16\2}\177\7\n\2\2~}\3\2\2\2~\177\3\2\2"+
		"\2\177\u0083\3\2\2\2\u0080\u0082\7\7\2\2\u0081\u0080\3\2\2\2\u0082\u0085"+
		"\3\2\2\2\u0083\u0081\3\2\2\2\u0083\u0084\3\2\2\2\u0084\u0086\3\2\2\2\u0085"+
		"\u0083\3\2\2\2\u0086\u0088\5\32\16\2\u0087~\3\2\2\2\u0088\u008b\3\2\2"+
		"\2\u0089\u0087\3\2\2\2\u0089\u008a\3\2\2\2\u008a\u008d\3\2\2\2\u008b\u0089"+
		"\3\2\2\2\u008c\u008e\7\n\2\2\u008d\u008c\3\2\2\2\u008d\u008e\3\2\2\2\u008e"+
		"\u0092\3\2\2\2\u008f\u0091\7\7\2\2\u0090\u008f\3\2\2\2\u0091\u0094\3\2"+
		"\2\2\u0092\u0090\3\2\2\2\u0092\u0093\3\2\2\2\u0093\u0095\3\2\2\2\u0094"+
		"\u0092\3\2\2\2\u0095\u0096\7\16\2\2\u0096\t\3\2\2\2\u0097\u0098\7\21\2"+
		"\2\u0098\u0099\7\t\2\2\u0099\u00a4\5 \21\2\u009a\u009c\7\7\2\2\u009b\u009a"+
		"\3\2\2\2\u009c\u009f\3\2\2\2\u009d\u009b\3\2\2\2\u009d\u009e\3\2\2\2\u009e"+
		"\u00a0\3\2\2\2\u009f\u009d\3\2\2\2\u00a0\u00a1\7\b\2\2\u00a1\u00a3\5 "+
		"\21\2\u00a2\u009d\3\2\2\2\u00a3\u00a6\3\2\2\2\u00a4\u00a2\3\2\2\2\u00a4"+
		"\u00a5\3\2\2\2\u00a5\u00ac\3\2\2\2\u00a6\u00a4\3\2\2\2\u00a7\u00a8\7\21"+
		"\2\2\u00a8\u00ac\5\6\4\2\u00a9\u00aa\7\21\2\2\u00aa\u00ac\5\b\5\2\u00ab"+
		"\u0097\3\2\2\2\u00ab\u00a7\3\2\2\2\u00ab\u00a9\3\2\2\2\u00ac\13\3\2\2"+
		"\2\u00ad\u00b1\7\25\2\2\u00ae\u00b0\7\7\2\2\u00af\u00ae\3\2\2\2\u00b0"+
		"\u00b3\3\2\2\2\u00b1\u00af\3\2\2\2\u00b1\u00b2\3\2\2\2\u00b2\u00b4\3\2"+
		"\2\2\u00b3\u00b1\3\2\2\2\u00b4\u00b5\5\"\22\2\u00b5\u00b9\7\27\2\2\u00b6"+
		"\u00b8\7\7\2\2\u00b7\u00b6\3\2\2\2\u00b8\u00bb\3\2\2\2\u00b9\u00b7\3\2"+
		"\2\2\u00b9\u00ba\3\2\2\2\u00ba\u00bc\3\2\2\2\u00bb\u00b9\3\2\2\2\u00bc"+
		"\u00c0\5\16\b\2\u00bd\u00bf\7\7\2\2\u00be\u00bd\3\2\2\2\u00bf\u00c2\3"+
		"\2\2\2\u00c0\u00be\3\2\2\2\u00c0\u00c1\3\2\2\2\u00c1\u00d7\3\2\2\2\u00c2"+
		"\u00c0\3\2\2\2\u00c3\u00c7\7\30\2\2\u00c4\u00c6\7\7\2\2\u00c5\u00c4\3"+
		"\2\2\2\u00c6\u00c9\3\2\2\2\u00c7\u00c5\3\2\2\2\u00c7\u00c8\3\2\2\2\u00c8"+
		"\u00cb\3\2\2\2\u00c9\u00c7\3\2\2\2\u00ca\u00cc\5\"\22\2\u00cb\u00ca\3"+
		"\2\2\2\u00cb\u00cc\3\2\2\2\u00cc\u00cd\3\2\2\2\u00cd\u00d1\7\27\2\2\u00ce"+
		"\u00d0\7\7\2\2\u00cf\u00ce\3\2\2\2\u00d0\u00d3\3\2\2\2\u00d1\u00cf\3\2"+
		"\2\2\u00d1\u00d2\3\2\2\2\u00d2\u00d4\3\2\2\2\u00d3\u00d1\3\2\2\2\u00d4"+
		"\u00d6\5\16\b\2\u00d5\u00c3\3\2\2\2\u00d6\u00d9\3\2\2\2\u00d7\u00d5\3"+
		"\2\2\2\u00d7\u00d8\3\2\2\2\u00d8\u00dd\3\2\2\2\u00d9\u00d7\3\2\2\2\u00da"+
		"\u00dc\7\7\2\2\u00db\u00da\3\2\2\2\u00dc\u00df\3\2\2\2\u00dd\u00db\3\2"+
		"\2\2\u00dd\u00de\3\2\2\2\u00de\u00e0\3\2\2\2\u00df\u00dd\3\2\2\2\u00e0"+
		"\u00e1\7 \2\2\u00e1\r\3\2\2\2\u00e2\u00ec\5\4\3\2\u00e3\u00e8\7\n\2\2"+
		"\u00e4\u00e8\7\7\2\2\u00e5\u00e6\7\n\2\2\u00e6\u00e8\7\7\2\2\u00e7\u00e3"+
		"\3\2\2\2\u00e7\u00e4\3\2\2\2\u00e7\u00e5\3\2\2\2\u00e8\u00e9\3\2\2\2\u00e9"+
		"\u00eb\5\4\3\2\u00ea\u00e7\3\2\2\2\u00eb\u00ee\3\2\2\2\u00ec\u00ea\3\2"+
		"\2\2\u00ec\u00ed\3\2\2\2\u00ed\u00f0\3\2\2\2\u00ee\u00ec\3\2\2\2\u00ef"+
		"\u00f1\7\n\2\2\u00f0\u00ef\3\2\2\2\u00f0\u00f1\3\2\2\2\u00f1\u00f5\3\2"+
		"\2\2\u00f2\u00f4\7\7\2\2\u00f3\u00f2\3\2\2\2\u00f4\u00f7\3\2\2\2\u00f5"+
		"\u00f3\3\2\2\2\u00f5\u00f6\3\2\2\2\u00f6\17\3\2\2\2\u00f7\u00f5\3\2\2"+
		"\2\u00f8\u00fc\7\25\2\2\u00f9\u00fb\7\7\2\2\u00fa\u00f9\3\2\2\2\u00fb"+
		"\u00fe\3\2\2\2\u00fc\u00fa\3\2\2\2\u00fc\u00fd\3\2\2\2\u00fd\u00ff\3\2"+
		"\2\2\u00fe\u00fc\3\2\2\2\u00ff\u0100\5\"\22\2\u0100\u0104\7\27\2\2\u0101"+
		"\u0103\7\7\2\2\u0102\u0101\3\2\2\2\u0103\u0106\3\2\2\2\u0104\u0102\3\2"+
		"\2\2\u0104\u0105\3\2\2\2\u0105\u0107\3\2\2\2\u0106\u0104\3\2\2\2\u0107"+
		"\u010b\5\22\n\2\u0108\u010a\7\7\2\2\u0109\u0108\3\2\2\2\u010a\u010d\3"+
		"\2\2\2\u010b\u0109\3\2\2\2\u010b\u010c\3\2\2\2\u010c\u0122\3\2\2\2\u010d"+
		"\u010b\3\2\2\2\u010e\u0112\7\30\2\2\u010f\u0111\7\7\2\2\u0110\u010f\3"+
		"\2\2\2\u0111\u0114\3\2\2\2\u0112\u0110\3\2\2\2\u0112\u0113\3\2\2\2\u0113"+
		"\u0116\3\2\2\2\u0114\u0112\3\2\2\2\u0115\u0117\5\"\22\2\u0116\u0115\3"+
		"\2\2\2\u0116\u0117\3\2\2\2\u0117\u0118\3\2\2\2\u0118\u011c\7\27\2\2\u0119"+
		"\u011b\7\7\2\2\u011a\u0119\3\2\2\2\u011b\u011e\3\2\2\2\u011c\u011a\3\2"+
		"\2\2\u011c\u011d\3\2\2\2\u011d\u011f\3\2\2\2\u011e\u011c\3\2\2\2\u011f"+
		"\u0121\5\22\n\2\u0120\u010e\3\2\2\2\u0121\u0124\3\2\2\2\u0122\u0120\3"+
		"\2\2\2\u0122\u0123\3\2\2\2\u0123\u0128\3\2\2\2\u0124\u0122\3\2\2\2\u0125"+
		"\u0127\7\7\2\2\u0126\u0125\3\2\2\2\u0127\u012a\3\2\2\2\u0128\u0126\3\2"+
		"\2\2\u0128\u0129\3\2\2\2\u0129\u012b\3\2\2\2\u012a\u0128\3\2\2\2\u012b"+
		"\u012c\7 \2\2\u012c\21\3\2\2\2\u012d\u0141\5\24\13\2\u012e\u013d\7\n\2"+
		"\2\u012f\u0131\7\7\2\2\u0130\u012f\3\2\2\2\u0131\u0134\3\2\2\2\u0132\u0130"+
		"\3\2\2\2\u0132\u0133\3\2\2\2\u0133\u013d\3\2\2\2\u0134\u0132\3\2\2\2\u0135"+
		"\u0139\7\n\2\2\u0136\u0138\7\7\2\2\u0137\u0136\3\2\2\2\u0138\u013b\3\2"+
		"\2\2\u0139\u0137\3\2\2\2\u0139\u013a\3\2\2\2\u013a\u013d\3\2\2\2\u013b"+
		"\u0139\3\2\2\2\u013c\u012e\3\2\2\2\u013c\u0132\3\2\2\2\u013c\u0135\3\2"+
		"\2\2\u013d\u013e\3\2\2\2\u013e\u0140\5\24\13\2\u013f\u013c\3\2\2\2\u0140"+
		"\u0143\3\2\2\2\u0141\u013f\3\2\2\2\u0141\u0142\3\2\2\2\u0142\u0145\3\2"+
		"\2\2\u0143\u0141\3\2\2\2\u0144\u0146\7\n\2\2\u0145\u0144\3\2\2\2\u0145"+
		"\u0146\3\2\2\2\u0146\u014a\3\2\2\2\u0147\u0149\7\7\2\2\u0148\u0147\3\2"+
		"\2\2\u0149\u014c\3\2\2\2\u014a\u0148\3\2\2\2\u014a\u014b\3\2\2\2\u014b"+
		"\23\3\2\2\2\u014c\u014a\3\2\2\2\u014d\u0150\5\n\6\2\u014e\u0150\5\20\t"+
		"\2\u014f\u014d\3\2\2\2\u014f\u014e\3\2\2\2\u0150\25\3\2\2\2\u0151\u0155"+
		"\7\25\2\2\u0152\u0154\7\7\2\2\u0153\u0152\3\2\2\2\u0154\u0157\3\2\2\2"+
		"\u0155\u0153\3\2\2\2\u0155\u0156\3\2\2\2\u0156\u0158\3\2\2\2\u0157\u0155"+
		"\3\2\2\2\u0158\u0159\5\"\22\2\u0159\u015d\7\27\2\2\u015a\u015c\7\7\2\2"+
		"\u015b\u015a\3\2\2\2\u015c\u015f\3\2\2\2\u015d\u015b\3\2\2\2\u015d\u015e"+
		"\3\2\2\2\u015e\u0160\3\2\2\2\u015f\u015d\3\2\2\2\u0160\u0164\5\30\r\2"+
		"\u0161\u0163\7\7\2\2\u0162\u0161\3\2\2\2\u0163\u0166\3\2\2\2\u0164\u0162"+
		"\3\2\2\2\u0164\u0165\3\2\2\2\u0165\u017b\3\2\2\2\u0166\u0164\3\2\2\2\u0167"+
		"\u016b\7\30\2\2\u0168\u016a\7\7\2\2\u0169\u0168\3\2\2\2\u016a\u016d\3"+
		"\2\2\2\u016b\u0169\3\2\2\2\u016b\u016c\3\2\2\2\u016c\u016f\3\2\2\2\u016d"+
		"\u016b\3\2\2\2\u016e\u0170\5\"\22\2\u016f\u016e\3\2\2\2\u016f\u0170\3"+
		"\2\2\2\u0170\u0171\3\2\2\2\u0171\u0175\7\27\2\2\u0172\u0174\7\7\2\2\u0173"+
		"\u0172\3\2\2\2\u0174\u0177\3\2\2\2\u0175\u0173\3\2\2\2\u0175\u0176\3\2"+
		"\2\2\u0176\u0178\3\2\2\2\u0177\u0175\3\2\2\2\u0178\u017a\5\30\r\2\u0179"+
		"\u0167\3\2\2\2\u017a\u017d\3\2\2\2\u017b\u0179\3\2\2\2\u017b\u017c\3\2"+
		"\2\2\u017c\u0181\3\2\2\2\u017d\u017b\3\2\2\2\u017e\u0180\7\7\2\2\u017f"+
		"\u017e\3\2\2\2\u0180\u0183\3\2\2\2\u0181\u017f\3\2\2\2\u0181\u0182\3\2"+
		"\2\2\u0182\u0184\3\2\2\2\u0183\u0181\3\2\2\2\u0184\u0185\7 \2\2\u0185"+
		"\27\3\2\2\2\u0186\u019a\5\32\16\2\u0187\u0196\7\n\2\2\u0188\u018a\7\7"+
		"\2\2\u0189\u0188\3\2\2\2\u018a\u018d\3\2\2\2\u018b\u0189\3\2\2\2\u018b"+
		"\u018c\3\2\2\2\u018c\u0196\3\2\2\2\u018d\u018b\3\2\2\2\u018e\u0192\7\n"+
		"\2\2\u018f\u0191\7\7\2\2\u0190\u018f\3\2\2\2\u0191\u0194\3\2\2\2\u0192"+
		"\u0190\3\2\2\2\u0192\u0193\3\2\2\2\u0193\u0196\3\2\2\2\u0194\u0192\3\2"+
		"\2\2\u0195\u0187\3\2\2\2\u0195\u018b\3\2\2\2\u0195\u018e\3\2\2\2\u0196"+
		"\u0197\3\2\2\2\u0197\u0199\5\32\16\2\u0198\u0195\3\2\2\2\u0199\u019c\3"+
		"\2\2\2\u019a\u0198\3\2\2\2\u019a\u019b\3\2\2\2\u019b\u019e\3\2\2\2\u019c"+
		"\u019a\3\2\2\2\u019d\u019f\7\n\2\2\u019e\u019d\3\2\2\2\u019e\u019f\3\2"+
		"\2\2\u019f\u01a3\3\2\2\2\u01a0\u01a2\7\7\2\2\u01a1\u01a0\3\2\2\2\u01a2"+
		"\u01a5\3\2\2\2\u01a3\u01a1\3\2\2\2\u01a3\u01a4\3\2\2\2\u01a4\31\3\2\2"+
		"\2\u01a5\u01a3\3\2\2\2\u01a6\u01a9\5(\25\2\u01a7\u01a9\5\26\f\2\u01a8"+
		"\u01a6\3\2\2\2\u01a8\u01a7\3\2\2\2\u01a9\33\3\2\2\2\u01aa\u01ae\7\25\2"+
		"\2\u01ab\u01ad\7\7\2\2\u01ac\u01ab\3\2\2\2\u01ad\u01b0\3\2\2\2\u01ae\u01ac"+
		"\3\2\2\2\u01ae\u01af\3\2\2\2\u01af\u01b1\3\2\2\2\u01b0\u01ae\3\2\2\2\u01b1"+
		"\u01b2\5\"\22\2\u01b2\u01b6\7\27\2\2\u01b3\u01b5\7\7\2\2\u01b4\u01b3\3"+
		"\2\2\2\u01b5\u01b8\3\2\2\2\u01b6\u01b4\3\2\2\2\u01b6\u01b7\3\2\2\2\u01b7"+
		"\u01b9\3\2\2\2\u01b8\u01b6\3\2\2\2\u01b9\u01bd\5\36\20\2\u01ba\u01bc\7"+
		"\7\2\2\u01bb\u01ba\3\2\2\2\u01bc\u01bf\3\2\2\2\u01bd\u01bb\3\2\2\2\u01bd"+
		"\u01be\3\2\2\2\u01be\u01d3\3\2\2\2\u01bf\u01bd\3\2\2\2\u01c0\u01c4\7\30"+
		"\2\2\u01c1\u01c3\7\7\2\2\u01c2\u01c1\3\2\2\2\u01c3\u01c6\3\2\2\2\u01c4"+
		"\u01c2\3\2\2\2\u01c4\u01c5\3\2\2\2\u01c5\u01c7\3\2\2\2\u01c6\u01c4\3\2"+
		"\2\2\u01c7\u01c8\5\"\22\2\u01c8\u01cc\7\27\2\2\u01c9\u01cb\7\7\2\2\u01ca"+
		"\u01c9\3\2\2\2\u01cb\u01ce\3\2\2\2\u01cc\u01ca\3\2\2\2\u01cc\u01cd\3\2"+
		"\2\2\u01cd\u01cf\3\2\2\2\u01ce\u01cc\3\2\2\2\u01cf\u01d0\5\36\20\2\u01d0"+
		"\u01d2\3\2\2\2\u01d1\u01c0\3\2\2\2\u01d2\u01d5\3\2\2\2\u01d3\u01d1\3\2"+
		"\2\2\u01d3\u01d4\3\2\2\2\u01d4\u01d9\3\2\2\2\u01d5\u01d3\3\2\2\2\u01d6"+
		"\u01d8\7\7\2\2\u01d7\u01d6\3\2\2\2\u01d8\u01db\3\2\2\2\u01d9\u01d7\3\2"+
		"\2\2\u01d9\u01da\3\2\2\2\u01da\u01dc\3\2\2\2\u01db\u01d9\3\2\2\2\u01dc"+
		"\u01e0\7\30\2\2\u01dd\u01df\7\7\2\2\u01de\u01dd\3\2\2\2\u01df\u01e2\3"+
		"\2\2\2\u01e0\u01de\3\2\2\2\u01e0\u01e1\3\2\2\2\u01e1\u01e3\3\2\2\2\u01e2"+
		"\u01e0\3\2\2\2\u01e3\u01e7\7\27\2\2\u01e4\u01e6\7\7\2\2\u01e5\u01e4\3"+
		"\2\2\2\u01e6\u01e9\3\2\2\2\u01e7\u01e5\3\2\2\2\u01e7\u01e8\3\2\2\2\u01e8"+
		"\u01ea\3\2\2\2\u01e9\u01e7\3\2\2\2\u01ea\u01eb\5\36\20\2\u01eb\u01ef\3"+
		"\2\2\2\u01ec\u01ee\7\7\2\2\u01ed\u01ec\3\2\2\2\u01ee\u01f1\3\2\2\2\u01ef"+
		"\u01ed\3\2\2\2\u01ef\u01f0\3\2\2\2\u01f0\u01f2\3\2\2\2\u01f1\u01ef\3\2"+
		"\2\2\u01f2\u01f3\7 \2\2\u01f3\35\3\2\2\2\u01f4\u01ff\5 \21\2\u01f5\u01f7"+
		"\7\7\2\2\u01f6\u01f5\3\2\2\2\u01f7\u01fa\3\2\2\2\u01f8\u01f6\3\2\2\2\u01f8"+
		"\u01f9\3\2\2\2\u01f9\u01fb\3\2\2\2\u01fa\u01f8\3\2\2\2\u01fb\u01fc\7\b"+
		"\2\2\u01fc\u01fe\5 \21\2\u01fd\u01f8\3\2\2\2\u01fe\u0201\3\2\2\2\u01ff"+
		"\u01fd\3\2\2\2\u01ff\u0200\3\2\2\2\u0200\37\3\2\2\2\u0201\u01ff\3\2\2"+
		"\2\u0202\u0216\5(\25\2\u0203\u0204\7\13\2\2\u0204\u020f\5 \21\2\u0205"+
		"\u0207\7\7\2\2\u0206\u0205\3\2\2\2\u0207\u020a\3\2\2\2\u0208\u0206\3\2"+
		"\2\2\u0208\u0209\3\2\2\2\u0209\u020b\3\2\2\2\u020a\u0208\3\2\2\2\u020b"+
		"\u020c\7\b\2\2\u020c\u020e\5 \21\2\u020d\u0208\3\2\2\2\u020e\u0211\3\2"+
		"\2\2\u020f\u020d\3\2\2\2\u020f\u0210\3\2\2\2\u0210\u0212\3\2\2\2\u0211"+
		"\u020f\3\2\2\2\u0212\u0213\7\f\2\2\u0213\u0216\3\2\2\2\u0214\u0216\5\34"+
		"\17\2\u0215\u0202\3\2\2\2\u0215\u0203\3\2\2\2\u0215\u0214\3\2\2\2\u0216"+
		"!\3\2\2\2\u0217\u0219\7\36\2\2\u0218\u0217\3\2\2\2\u0218\u0219\3\2\2\2"+
		"\u0219\u021c\3\2\2\2\u021a\u021d\5$\23\2\u021b\u021d\5&\24\2\u021c\u021a"+
		"\3\2\2\2\u021c\u021b\3\2\2\2\u021d\u0225\3\2\2\2\u021e\u0221\t\2\2\2\u021f"+
		"\u0222\5$\23\2\u0220\u0222\5&\24\2\u0221\u021f\3\2\2\2\u0221\u0220\3\2"+
		"\2\2\u0222\u0224\3\2\2\2\u0223\u021e\3\2\2\2\u0224\u0227\3\2\2\2\u0225"+
		"\u0223\3\2\2\2\u0225\u0226\3\2\2\2\u0226#\3\2\2\2\u0227\u0225\3\2\2\2"+
		"\u0228\u022a\7\7\2\2\u0229\u0228\3\2\2\2\u022a\u022d\3\2\2\2\u022b\u0229"+
		"\3\2\2\2\u022b\u022c\3\2\2\2\u022c\u022f\3\2\2\2\u022d\u022b\3\2\2\2\u022e"+
		"\u0230\7\21\2\2\u022f\u022e\3\2\2\2\u022f\u0230\3\2\2\2\u0230\u023a\3"+
		"\2\2\2\u0231\u023b\7\t\2\2\u0232\u023b\7\31\2\2\u0233\u0234\7\31\2\2\u0234"+
		"\u023b\7\t\2\2\u0235\u023b\7\32\2\2\u0236\u0237\7\32\2\2\u0237\u023b\7"+
		"\t\2\2\u0238\u0239\7\36\2\2\u0239\u023b\7\t\2\2\u023a\u0231\3\2\2\2\u023a"+
		"\u0232\3\2\2\2\u023a\u0233\3\2\2\2\u023a\u0235\3\2\2\2\u023a\u0236\3\2"+
		"\2\2\u023a\u0238\3\2\2\2\u023a\u023b\3\2\2\2\u023b\u023d\3\2\2\2\u023c"+
		"\u023e\7\33\2\2\u023d\u023c\3\2\2\2\u023d\u023e\3\2\2\2\u023e\u023f\3"+
		"\2\2\2\u023f\u0241\5(\25\2\u0240\u0242\7\33\2\2\u0241\u0240\3\2\2\2\u0241"+
		"\u0242\3\2\2\2\u0242\u024d\3\2\2\2\u0243\u0245\7\35\2\2\u0244\u0246\7"+
		"\33\2\2\u0245\u0244\3\2\2\2\u0245\u0246\3\2\2\2\u0246\u0247\3\2\2\2\u0247"+
		"\u0249\5(\25\2\u0248\u024a\7\33\2\2\u0249\u0248\3\2\2\2\u0249\u024a\3"+
		"\2\2\2\u024a\u024c\3\2\2\2\u024b\u0243\3\2\2\2\u024c\u024f\3\2\2\2\u024d"+
		"\u024b\3\2\2\2\u024d\u024e\3\2\2\2\u024e\u0253\3\2\2\2\u024f\u024d\3\2"+
		"\2\2\u0250\u0252\7\7\2\2\u0251\u0250\3\2\2\2\u0252\u0255\3\2\2\2\u0253"+
		"\u0251\3\2\2\2\u0253\u0254\3\2\2\2\u0254%\3\2\2\2\u0255\u0253\3\2\2\2"+
		"\u0256\u0257\7\25\2\2\u0257\u025c\5\"\22\2\u0258\u0259\t\2\2\2\u0259\u025b"+
		"\5\"\22\2\u025a\u0258\3\2\2\2\u025b\u025e\3\2\2\2\u025c\u025a\3\2\2\2"+
		"\u025c\u025d\3\2\2\2\u025d\u025f\3\2\2\2\u025e\u025c\3\2\2\2\u025f\u0260"+
		"\7 \2\2\u0260\'\3\2\2\2\u0261\u026b\5\6\4\2\u0262\u026b\5\b\5\2\u0263"+
		"\u026b\5\n\6\2\u0264\u026b\7\23\2\2\u0265\u026b\7\17\2\2\u0266\u026b\7"+
		"\21\2\2\u0267\u026b\7\4\2\2\u0268\u026b\7\5\2\2\u0269\u026b\7\3\2\2\u026a"+
		"\u0261\3\2\2\2\u026a\u0262\3\2\2\2\u026a\u0263\3\2\2\2\u026a\u0264\3\2"+
		"\2\2\u026a\u0265\3\2\2\2\u026a\u0266\3\2\2\2\u026a\u0267\3\2\2\2\u026a"+
		"\u0268\3\2\2\2\u026a\u0269\3\2\2\2\u026b)\3\2\2\2`-\65;=BFKTZ_djpy~\u0083"+
		"\u0089\u008d\u0092\u009d\u00a4\u00ab\u00b1\u00b9\u00c0\u00c7\u00cb\u00d1"+
		"\u00d7\u00dd\u00e7\u00ec\u00f0\u00f5\u00fc\u0104\u010b\u0112\u0116\u011c"+
		"\u0122\u0128\u0132\u0139\u013c\u0141\u0145\u014a\u014f\u0155\u015d\u0164"+
		"\u016b\u016f\u0175\u017b\u0181\u018b\u0192\u0195\u019a\u019e\u01a3\u01a8"+
		"\u01ae\u01b6\u01bd\u01c4\u01cc\u01d3\u01d9\u01e0\u01e7\u01ef\u01f8\u01ff"+
		"\u0208\u020f\u0215\u0218\u021c\u0221\u0225\u022b\u022f\u023a\u023d\u0241"+
		"\u0245\u0249\u024d\u0253\u025c\u026a";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}