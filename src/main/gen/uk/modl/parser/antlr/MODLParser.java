// Generated from /Users/alex/code/NUM/MODL/java-interpreter/src/main/java/uk/modl/parser/antlr/MODLParser.g4 by ANTLR 4.7
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
		WS=1, NULL=2, TRUE=3, FALSE=4, NEWLINE=5, COLON=6, EQUALS=7, SC=8, LBRAC=9, 
		RBRAC=10, LSBRAC=11, RSBRAC=12, NUMBER=13, COMMENT=14, STRING=15, HASH_PREFIX=16, 
		QUOTED=17, GRAVED=18, LCBRAC=19, CWS=20, QMARK=21, FSLASH=22, GTHAN=23, 
		LTHAN=24, ASTERISK=25, AMP=26, PIPE=27, EXCLAM=28, OPERATOR=29, CCOMMENT=30, 
		RCBRAC=31;
	public static final int
		RULE_modl = 0, RULE_structure = 1, RULE_map = 2, RULE_array = 3, RULE_pair = 4, 
		RULE_value_item = 5, RULE_top_level_conditional = 6, RULE_top_level_conditional_return = 7, 
		RULE_map_conditional = 8, RULE_map_conditional_return = 9, RULE_map_item = 10, 
		RULE_array_conditional = 11, RULE_array_conditional_return = 12, RULE_array_item = 13, 
		RULE_value_conditional = 14, RULE_value_conditional_return = 15, RULE_condition_test = 16, 
		RULE_condition = 17, RULE_condition_group = 18, RULE_value = 19;
	public static final String[] ruleNames = {
		"modl", "structure", "map", "array", "pair", "value_item", "top_level_conditional", 
		"top_level_conditional_return", "map_conditional", "map_conditional_return", 
		"map_item", "array_conditional", "array_conditional_return", "array_item", 
		"value_conditional", "value_conditional_return", "condition_test", "condition", 
		"condition_group", "value"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, "'{'", null, "'?'", "'/'", "'>'", 
		"'<'", "'*'", "'&'", "'|'", "'!'", null, null, "'}'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "WS", "NULL", "TRUE", "FALSE", "NEWLINE", "COLON", "EQUALS", "SC", 
		"LBRAC", "RBRAC", "LSBRAC", "RSBRAC", "NUMBER", "COMMENT", "STRING", "HASH_PREFIX", 
		"QUOTED", "GRAVED", "LCBRAC", "CWS", "QMARK", "FSLASH", "GTHAN", "LTHAN", 
		"ASTERISK", "AMP", "PIPE", "EXCLAM", "OPERATOR", "CCOMMENT", "RCBRAC"
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
		public TerminalNode EOF() { return getToken(MODLParser.EOF, 0); }
		public List<StructureContext> structure() {
			return getRuleContexts(StructureContext.class);
		}
		public StructureContext structure(int i) {
			return getRuleContext(StructureContext.class,i);
		}
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
			setState(70);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
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

				}
				break;
			}
			setState(75);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(72);
				match(NEWLINE);
				}
				}
				setState(77);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(78);
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
			setState(84);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LBRAC:
				enterOuterAlt(_localctx, 1);
				{
				setState(80);
				map();
				}
				break;
			case LSBRAC:
				enterOuterAlt(_localctx, 2);
				{
				setState(81);
				array();
				}
				break;
			case LCBRAC:
				enterOuterAlt(_localctx, 3);
				{
				setState(82);
				top_level_conditional();
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 4);
				{
				setState(83);
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
			setState(86);
			match(LBRAC);
			setState(90);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(87);
				match(NEWLINE);
				}
				}
				setState(92);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(93);
			map_item();
			setState(106);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(95);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==SC) {
						{
						setState(94);
						match(SC);
						}
					}

					setState(100);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==NEWLINE) {
						{
						{
						setState(97);
						match(NEWLINE);
						}
						}
						setState(102);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(103);
					map_item();
					}
					} 
				}
				setState(108);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			}
			setState(112);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(109);
				match(NEWLINE);
				}
				}
				setState(114);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(115);
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
			setState(117);
			match(LSBRAC);
			setState(121);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(118);
				match(NEWLINE);
				}
				}
				setState(123);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(124);
			array_item();
			setState(137);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(126);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==SC) {
						{
						setState(125);
						match(SC);
						}
					}

					setState(131);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==NEWLINE) {
						{
						{
						setState(128);
						match(NEWLINE);
						}
						}
						setState(133);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(134);
					array_item();
					}
					} 
				}
				setState(139);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
			}
			setState(141);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SC) {
				{
				setState(140);
				match(SC);
				}
			}

			setState(146);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(143);
				match(NEWLINE);
				}
				}
				setState(148);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(149);
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
			setState(171);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(151);
				match(STRING);
				setState(152);
				match(EQUALS);
				setState(153);
				value_item();
				setState(164);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(157);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while (_la==NEWLINE) {
							{
							{
							setState(154);
							match(NEWLINE);
							}
							}
							setState(159);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						setState(160);
						match(COLON);
						setState(161);
						value_item();
						}
						} 
					}
					setState(166);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(167);
				match(STRING);
				setState(168);
				map();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(169);
				match(STRING);
				setState(170);
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
		enterRule(_localctx, 10, RULE_value_item);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(192);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
			case 1:
				{
				setState(173);
				value();
				}
				break;
			case 2:
				{
				setState(174);
				match(LBRAC);
				setState(175);
				value_item();
				setState(186);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NEWLINE || _la==COLON) {
					{
					{
					setState(179);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==NEWLINE) {
						{
						{
						setState(176);
						match(NEWLINE);
						}
						}
						setState(181);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(182);
					match(COLON);
					setState(183);
					value_item();
					}
					}
					setState(188);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(189);
				match(RBRAC);
				}
				break;
			case 3:
				{
				setState(191);
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
		enterRule(_localctx, 12, RULE_top_level_conditional);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(194);
			match(LCBRAC);
			setState(198);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(195);
					match(NEWLINE);
					}
					} 
				}
				setState(200);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			}
			setState(201);
			condition_test();
			setState(202);
			match(QMARK);
			setState(206);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(203);
				match(NEWLINE);
				}
				}
				setState(208);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(209);
			top_level_conditional_return();
			setState(213);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(210);
					match(NEWLINE);
					}
					} 
				}
				setState(215);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
			}
			setState(236);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==FSLASH) {
				{
				{
				setState(216);
				match(FSLASH);
				setState(220);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(217);
						match(NEWLINE);
						}
						} 
					}
					setState(222);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
				}
				setState(224);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NULL) | (1L << TRUE) | (1L << FALSE) | (1L << NEWLINE) | (1L << LBRAC) | (1L << LSBRAC) | (1L << NUMBER) | (1L << STRING) | (1L << QUOTED) | (1L << LCBRAC) | (1L << ASTERISK) | (1L << EXCLAM) | (1L << OPERATOR))) != 0)) {
					{
					setState(223);
					condition_test();
					}
				}

				setState(226);
				match(QMARK);
				setState(230);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NEWLINE) {
					{
					{
					setState(227);
					match(NEWLINE);
					}
					}
					setState(232);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(233);
				top_level_conditional_return();
				}
				}
				setState(238);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(242);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(239);
				match(NEWLINE);
				}
				}
				setState(244);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(245);
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
		enterRule(_localctx, 14, RULE_top_level_conditional_return);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(247);
			structure();
			setState(257);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,35,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(252);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
					case 1:
						{
						setState(248);
						match(SC);
						}
						break;
					case 2:
						{
						setState(249);
						match(NEWLINE);
						}
						break;
					case 3:
						{
						setState(250);
						match(SC);
						setState(251);
						match(NEWLINE);
						}
						break;
					}
					setState(254);
					structure();
					}
					} 
				}
				setState(259);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,35,_ctx);
			}
			setState(261);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SC) {
				{
				setState(260);
				match(SC);
				}
			}

			setState(266);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,37,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(263);
					match(NEWLINE);
					}
					} 
				}
				setState(268);
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
		enterRule(_localctx, 16, RULE_map_conditional);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(269);
			match(LCBRAC);
			setState(273);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,38,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(270);
					match(NEWLINE);
					}
					} 
				}
				setState(275);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,38,_ctx);
			}
			setState(276);
			condition_test();
			setState(277);
			match(QMARK);
			setState(281);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(278);
				match(NEWLINE);
				}
				}
				setState(283);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(284);
			map_conditional_return();
			setState(288);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,40,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(285);
					match(NEWLINE);
					}
					} 
				}
				setState(290);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,40,_ctx);
			}
			setState(311);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==FSLASH) {
				{
				{
				setState(291);
				match(FSLASH);
				setState(295);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,41,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(292);
						match(NEWLINE);
						}
						} 
					}
					setState(297);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,41,_ctx);
				}
				setState(299);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NULL) | (1L << TRUE) | (1L << FALSE) | (1L << NEWLINE) | (1L << LBRAC) | (1L << LSBRAC) | (1L << NUMBER) | (1L << STRING) | (1L << QUOTED) | (1L << LCBRAC) | (1L << ASTERISK) | (1L << EXCLAM) | (1L << OPERATOR))) != 0)) {
					{
					setState(298);
					condition_test();
					}
				}

				setState(301);
				match(QMARK);
				setState(305);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NEWLINE) {
					{
					{
					setState(302);
					match(NEWLINE);
					}
					}
					setState(307);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(308);
				map_conditional_return();
				}
				}
				setState(313);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(317);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(314);
				match(NEWLINE);
				}
				}
				setState(319);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(320);
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
		enterRule(_localctx, 18, RULE_map_conditional_return);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(322);
			map_item();
			setState(342);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,49,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(337);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
					case 1:
						{
						setState(323);
						match(SC);
						}
						break;
					case 2:
						{
						setState(327);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while (_la==NEWLINE) {
							{
							{
							setState(324);
							match(NEWLINE);
							}
							}
							setState(329);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						}
						break;
					case 3:
						{
						setState(330);
						match(SC);
						setState(334);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while (_la==NEWLINE) {
							{
							{
							setState(331);
							match(NEWLINE);
							}
							}
							setState(336);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						}
						break;
					}
					setState(339);
					map_item();
					}
					} 
				}
				setState(344);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,49,_ctx);
			}
			setState(346);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SC) {
				{
				setState(345);
				match(SC);
				}
			}

			setState(351);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,51,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(348);
					match(NEWLINE);
					}
					} 
				}
				setState(353);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,51,_ctx);
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
		enterRule(_localctx, 20, RULE_map_item);
		try {
			setState(356);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(354);
				pair();
				}
				break;
			case LCBRAC:
				enterOuterAlt(_localctx, 2);
				{
				setState(355);
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
		enterRule(_localctx, 22, RULE_array_conditional);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(358);
			match(LCBRAC);
			setState(362);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,53,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(359);
					match(NEWLINE);
					}
					} 
				}
				setState(364);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,53,_ctx);
			}
			setState(365);
			condition_test();
			setState(366);
			match(QMARK);
			setState(370);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(367);
				match(NEWLINE);
				}
				}
				setState(372);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(373);
			array_conditional_return();
			setState(377);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,55,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(374);
					match(NEWLINE);
					}
					} 
				}
				setState(379);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,55,_ctx);
			}
			setState(400);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==FSLASH) {
				{
				{
				setState(380);
				match(FSLASH);
				setState(384);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,56,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(381);
						match(NEWLINE);
						}
						} 
					}
					setState(386);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,56,_ctx);
				}
				setState(388);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NULL) | (1L << TRUE) | (1L << FALSE) | (1L << NEWLINE) | (1L << LBRAC) | (1L << LSBRAC) | (1L << NUMBER) | (1L << STRING) | (1L << QUOTED) | (1L << LCBRAC) | (1L << ASTERISK) | (1L << EXCLAM) | (1L << OPERATOR))) != 0)) {
					{
					setState(387);
					condition_test();
					}
				}

				setState(390);
				match(QMARK);
				setState(394);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NEWLINE) {
					{
					{
					setState(391);
					match(NEWLINE);
					}
					}
					setState(396);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(397);
				array_conditional_return();
				}
				}
				setState(402);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(406);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(403);
				match(NEWLINE);
				}
				}
				setState(408);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(409);
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
		enterRule(_localctx, 24, RULE_array_conditional_return);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(411);
			array_item();
			setState(431);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,64,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(426);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,63,_ctx) ) {
					case 1:
						{
						setState(412);
						match(SC);
						}
						break;
					case 2:
						{
						setState(416);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while (_la==NEWLINE) {
							{
							{
							setState(413);
							match(NEWLINE);
							}
							}
							setState(418);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						}
						break;
					case 3:
						{
						setState(419);
						match(SC);
						setState(423);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while (_la==NEWLINE) {
							{
							{
							setState(420);
							match(NEWLINE);
							}
							}
							setState(425);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						}
						break;
					}
					setState(428);
					array_item();
					}
					} 
				}
				setState(433);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,64,_ctx);
			}
			setState(435);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SC) {
				{
				setState(434);
				match(SC);
				}
			}

			setState(440);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,66,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(437);
					match(NEWLINE);
					}
					} 
				}
				setState(442);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,66,_ctx);
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
		enterRule(_localctx, 26, RULE_array_item);
		try {
			setState(445);
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
				setState(443);
				value();
				}
				break;
			case LCBRAC:
				enterOuterAlt(_localctx, 2);
				{
				setState(444);
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
		enterRule(_localctx, 28, RULE_value_conditional);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(447);
			match(LCBRAC);
			setState(451);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,68,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(448);
					match(NEWLINE);
					}
					} 
				}
				setState(453);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,68,_ctx);
			}
			setState(454);
			condition_test();
			setState(455);
			match(QMARK);
			setState(459);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(456);
				match(NEWLINE);
				}
				}
				setState(461);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(462);
			value_conditional_return();
			setState(466);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,70,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(463);
					match(NEWLINE);
					}
					} 
				}
				setState(468);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,70,_ctx);
			}
			setState(488);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,73,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(469);
					match(FSLASH);
					setState(473);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,71,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(470);
							match(NEWLINE);
							}
							} 
						}
						setState(475);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,71,_ctx);
					}
					setState(476);
					condition_test();
					setState(477);
					match(QMARK);
					setState(481);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==NEWLINE) {
						{
						{
						setState(478);
						match(NEWLINE);
						}
						}
						setState(483);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(484);
					value_conditional_return();
					}
					} 
				}
				setState(490);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,73,_ctx);
			}
			setState(494);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(491);
				match(NEWLINE);
				}
				}
				setState(496);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			{
			setState(497);
			match(FSLASH);
			setState(501);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(498);
				match(NEWLINE);
				}
				}
				setState(503);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(504);
			match(QMARK);
			setState(508);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(505);
				match(NEWLINE);
				}
				}
				setState(510);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(511);
			value_conditional_return();
			}
			setState(516);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(513);
				match(NEWLINE);
				}
				}
				setState(518);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(519);
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
		enterRule(_localctx, 30, RULE_value_conditional_return);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(521);
			value_item();
			setState(532);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,79,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(525);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==NEWLINE) {
						{
						{
						setState(522);
						match(NEWLINE);
						}
						}
						setState(527);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(528);
					match(COLON);
					setState(529);
					value_item();
					}
					} 
				}
				setState(534);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,79,_ctx);
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
			setState(536);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EXCLAM) {
				{
				setState(535);
				match(EXCLAM);
				}
			}

			setState(540);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NULL:
			case TRUE:
			case FALSE:
			case NEWLINE:
			case LBRAC:
			case LSBRAC:
			case NUMBER:
			case STRING:
			case QUOTED:
			case ASTERISK:
			case OPERATOR:
				{
				setState(538);
				condition();
				}
				break;
			case LCBRAC:
				{
				setState(539);
				condition_group();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(552);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,84,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(542);
					_la = _input.LA(1);
					if ( !(_la==AMP || _la==PIPE) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(544);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==EXCLAM) {
						{
						setState(543);
						match(EXCLAM);
						}
					}

					setState(548);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case NULL:
					case TRUE:
					case FALSE:
					case NEWLINE:
					case LBRAC:
					case LSBRAC:
					case NUMBER:
					case STRING:
					case QUOTED:
					case ASTERISK:
					case OPERATOR:
						{
						setState(546);
						condition();
						}
						break;
					case LCBRAC:
						{
						setState(547);
						condition_group();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					}
					} 
				}
				setState(554);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,84,_ctx);
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
		public TerminalNode OPERATOR() { return getToken(MODLParser.OPERATOR, 0); }
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
			setState(558);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(555);
				match(NEWLINE);
				}
				}
				setState(560);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(562);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,86,_ctx) ) {
			case 1:
				{
				setState(561);
				match(STRING);
				}
				break;
			}
			setState(565);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==OPERATOR) {
				{
				setState(564);
				match(OPERATOR);
				}
			}

			{
			setState(568);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASTERISK) {
				{
				setState(567);
				match(ASTERISK);
				}
			}

			setState(570);
			value();
			setState(572);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASTERISK) {
				{
				setState(571);
				match(ASTERISK);
				}
			}

			setState(584);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,92,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(574);
					match(PIPE);
					setState(576);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==ASTERISK) {
						{
						setState(575);
						match(ASTERISK);
						}
					}

					setState(578);
					value();
					setState(580);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==ASTERISK) {
						{
						setState(579);
						match(ASTERISK);
						}
					}

					}
					} 
				}
				setState(586);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,92,_ctx);
			}
			}
			setState(590);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(587);
				match(NEWLINE);
				}
				}
				setState(592);
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
			setState(593);
			match(LCBRAC);
			setState(594);
			condition_test();
			setState(599);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AMP || _la==PIPE) {
				{
				{
				setState(595);
				_la = _input.LA(1);
				if ( !(_la==AMP || _la==PIPE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(596);
				condition_test();
				}
				}
				setState(601);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(602);
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
			setState(613);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,95,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(604);
				map();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(605);
				array();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(606);
				pair();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(607);
				match(QUOTED);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(608);
				match(NUMBER);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(609);
				match(STRING);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(610);
				match(TRUE);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(611);
				match(FALSE);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(612);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3!\u026a\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\3\2\7\2,\n\2\f\2\16\2/\13\2\3\2\3\2\3\2"+
		"\6\2\64\n\2\r\2\16\2\65\3\2\3\2\6\2:\n\2\r\2\16\2;\5\2>\n\2\3\2\7\2A\n"+
		"\2\f\2\16\2D\13\2\3\2\5\2G\n\2\5\2I\n\2\3\2\7\2L\n\2\f\2\16\2O\13\2\3"+
		"\2\3\2\3\3\3\3\3\3\3\3\5\3W\n\3\3\4\3\4\7\4[\n\4\f\4\16\4^\13\4\3\4\3"+
		"\4\5\4b\n\4\3\4\7\4e\n\4\f\4\16\4h\13\4\3\4\7\4k\n\4\f\4\16\4n\13\4\3"+
		"\4\7\4q\n\4\f\4\16\4t\13\4\3\4\3\4\3\5\3\5\7\5z\n\5\f\5\16\5}\13\5\3\5"+
		"\3\5\5\5\u0081\n\5\3\5\7\5\u0084\n\5\f\5\16\5\u0087\13\5\3\5\7\5\u008a"+
		"\n\5\f\5\16\5\u008d\13\5\3\5\5\5\u0090\n\5\3\5\7\5\u0093\n\5\f\5\16\5"+
		"\u0096\13\5\3\5\3\5\3\6\3\6\3\6\3\6\7\6\u009e\n\6\f\6\16\6\u00a1\13\6"+
		"\3\6\3\6\7\6\u00a5\n\6\f\6\16\6\u00a8\13\6\3\6\3\6\3\6\3\6\5\6\u00ae\n"+
		"\6\3\7\3\7\3\7\3\7\7\7\u00b4\n\7\f\7\16\7\u00b7\13\7\3\7\3\7\7\7\u00bb"+
		"\n\7\f\7\16\7\u00be\13\7\3\7\3\7\3\7\5\7\u00c3\n\7\3\b\3\b\7\b\u00c7\n"+
		"\b\f\b\16\b\u00ca\13\b\3\b\3\b\3\b\7\b\u00cf\n\b\f\b\16\b\u00d2\13\b\3"+
		"\b\3\b\7\b\u00d6\n\b\f\b\16\b\u00d9\13\b\3\b\3\b\7\b\u00dd\n\b\f\b\16"+
		"\b\u00e0\13\b\3\b\5\b\u00e3\n\b\3\b\3\b\7\b\u00e7\n\b\f\b\16\b\u00ea\13"+
		"\b\3\b\7\b\u00ed\n\b\f\b\16\b\u00f0\13\b\3\b\7\b\u00f3\n\b\f\b\16\b\u00f6"+
		"\13\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\5\t\u00ff\n\t\3\t\7\t\u0102\n\t\f\t"+
		"\16\t\u0105\13\t\3\t\5\t\u0108\n\t\3\t\7\t\u010b\n\t\f\t\16\t\u010e\13"+
		"\t\3\n\3\n\7\n\u0112\n\n\f\n\16\n\u0115\13\n\3\n\3\n\3\n\7\n\u011a\n\n"+
		"\f\n\16\n\u011d\13\n\3\n\3\n\7\n\u0121\n\n\f\n\16\n\u0124\13\n\3\n\3\n"+
		"\7\n\u0128\n\n\f\n\16\n\u012b\13\n\3\n\5\n\u012e\n\n\3\n\3\n\7\n\u0132"+
		"\n\n\f\n\16\n\u0135\13\n\3\n\7\n\u0138\n\n\f\n\16\n\u013b\13\n\3\n\7\n"+
		"\u013e\n\n\f\n\16\n\u0141\13\n\3\n\3\n\3\13\3\13\3\13\7\13\u0148\n\13"+
		"\f\13\16\13\u014b\13\13\3\13\3\13\7\13\u014f\n\13\f\13\16\13\u0152\13"+
		"\13\5\13\u0154\n\13\3\13\7\13\u0157\n\13\f\13\16\13\u015a\13\13\3\13\5"+
		"\13\u015d\n\13\3\13\7\13\u0160\n\13\f\13\16\13\u0163\13\13\3\f\3\f\5\f"+
		"\u0167\n\f\3\r\3\r\7\r\u016b\n\r\f\r\16\r\u016e\13\r\3\r\3\r\3\r\7\r\u0173"+
		"\n\r\f\r\16\r\u0176\13\r\3\r\3\r\7\r\u017a\n\r\f\r\16\r\u017d\13\r\3\r"+
		"\3\r\7\r\u0181\n\r\f\r\16\r\u0184\13\r\3\r\5\r\u0187\n\r\3\r\3\r\7\r\u018b"+
		"\n\r\f\r\16\r\u018e\13\r\3\r\7\r\u0191\n\r\f\r\16\r\u0194\13\r\3\r\7\r"+
		"\u0197\n\r\f\r\16\r\u019a\13\r\3\r\3\r\3\16\3\16\3\16\7\16\u01a1\n\16"+
		"\f\16\16\16\u01a4\13\16\3\16\3\16\7\16\u01a8\n\16\f\16\16\16\u01ab\13"+
		"\16\5\16\u01ad\n\16\3\16\7\16\u01b0\n\16\f\16\16\16\u01b3\13\16\3\16\5"+
		"\16\u01b6\n\16\3\16\7\16\u01b9\n\16\f\16\16\16\u01bc\13\16\3\17\3\17\5"+
		"\17\u01c0\n\17\3\20\3\20\7\20\u01c4\n\20\f\20\16\20\u01c7\13\20\3\20\3"+
		"\20\3\20\7\20\u01cc\n\20\f\20\16\20\u01cf\13\20\3\20\3\20\7\20\u01d3\n"+
		"\20\f\20\16\20\u01d6\13\20\3\20\3\20\7\20\u01da\n\20\f\20\16\20\u01dd"+
		"\13\20\3\20\3\20\3\20\7\20\u01e2\n\20\f\20\16\20\u01e5\13\20\3\20\3\20"+
		"\7\20\u01e9\n\20\f\20\16\20\u01ec\13\20\3\20\7\20\u01ef\n\20\f\20\16\20"+
		"\u01f2\13\20\3\20\3\20\7\20\u01f6\n\20\f\20\16\20\u01f9\13\20\3\20\3\20"+
		"\7\20\u01fd\n\20\f\20\16\20\u0200\13\20\3\20\3\20\3\20\7\20\u0205\n\20"+
		"\f\20\16\20\u0208\13\20\3\20\3\20\3\21\3\21\7\21\u020e\n\21\f\21\16\21"+
		"\u0211\13\21\3\21\3\21\7\21\u0215\n\21\f\21\16\21\u0218\13\21\3\22\5\22"+
		"\u021b\n\22\3\22\3\22\5\22\u021f\n\22\3\22\3\22\5\22\u0223\n\22\3\22\3"+
		"\22\5\22\u0227\n\22\7\22\u0229\n\22\f\22\16\22\u022c\13\22\3\23\7\23\u022f"+
		"\n\23\f\23\16\23\u0232\13\23\3\23\5\23\u0235\n\23\3\23\5\23\u0238\n\23"+
		"\3\23\5\23\u023b\n\23\3\23\3\23\5\23\u023f\n\23\3\23\3\23\5\23\u0243\n"+
		"\23\3\23\3\23\5\23\u0247\n\23\7\23\u0249\n\23\f\23\16\23\u024c\13\23\3"+
		"\23\7\23\u024f\n\23\f\23\16\23\u0252\13\23\3\24\3\24\3\24\3\24\7\24\u0258"+
		"\n\24\f\24\16\24\u025b\13\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3"+
		"\25\3\25\3\25\5\25\u0268\n\25\3\25\2\2\26\2\4\6\b\n\f\16\20\22\24\26\30"+
		"\32\34\36 \"$&(\2\3\3\2\34\35\2\u02c4\2H\3\2\2\2\4V\3\2\2\2\6X\3\2\2\2"+
		"\bw\3\2\2\2\n\u00ad\3\2\2\2\f\u00c2\3\2\2\2\16\u00c4\3\2\2\2\20\u00f9"+
		"\3\2\2\2\22\u010f\3\2\2\2\24\u0144\3\2\2\2\26\u0166\3\2\2\2\30\u0168\3"+
		"\2\2\2\32\u019d\3\2\2\2\34\u01bf\3\2\2\2\36\u01c1\3\2\2\2 \u020b\3\2\2"+
		"\2\"\u021a\3\2\2\2$\u0230\3\2\2\2&\u0253\3\2\2\2(\u0267\3\2\2\2*,\7\7"+
		"\2\2+*\3\2\2\2,/\3\2\2\2-+\3\2\2\2-.\3\2\2\2.\60\3\2\2\2/-\3\2\2\2\60"+
		"B\5\4\3\2\61>\7\n\2\2\62\64\7\7\2\2\63\62\3\2\2\2\64\65\3\2\2\2\65\63"+
		"\3\2\2\2\65\66\3\2\2\2\66>\3\2\2\2\679\7\n\2\28:\7\7\2\298\3\2\2\2:;\3"+
		"\2\2\2;9\3\2\2\2;<\3\2\2\2<>\3\2\2\2=\61\3\2\2\2=\63\3\2\2\2=\67\3\2\2"+
		"\2>?\3\2\2\2?A\5\4\3\2@=\3\2\2\2AD\3\2\2\2B@\3\2\2\2BC\3\2\2\2CF\3\2\2"+
		"\2DB\3\2\2\2EG\7\n\2\2FE\3\2\2\2FG\3\2\2\2GI\3\2\2\2H-\3\2\2\2HI\3\2\2"+
		"\2IM\3\2\2\2JL\7\7\2\2KJ\3\2\2\2LO\3\2\2\2MK\3\2\2\2MN\3\2\2\2NP\3\2\2"+
		"\2OM\3\2\2\2PQ\7\2\2\3Q\3\3\2\2\2RW\5\6\4\2SW\5\b\5\2TW\5\16\b\2UW\5\n"+
		"\6\2VR\3\2\2\2VS\3\2\2\2VT\3\2\2\2VU\3\2\2\2W\5\3\2\2\2X\\\7\13\2\2Y["+
		"\7\7\2\2ZY\3\2\2\2[^\3\2\2\2\\Z\3\2\2\2\\]\3\2\2\2]_\3\2\2\2^\\\3\2\2"+
		"\2_l\5\26\f\2`b\7\n\2\2a`\3\2\2\2ab\3\2\2\2bf\3\2\2\2ce\7\7\2\2dc\3\2"+
		"\2\2eh\3\2\2\2fd\3\2\2\2fg\3\2\2\2gi\3\2\2\2hf\3\2\2\2ik\5\26\f\2ja\3"+
		"\2\2\2kn\3\2\2\2lj\3\2\2\2lm\3\2\2\2mr\3\2\2\2nl\3\2\2\2oq\7\7\2\2po\3"+
		"\2\2\2qt\3\2\2\2rp\3\2\2\2rs\3\2\2\2su\3\2\2\2tr\3\2\2\2uv\7\f\2\2v\7"+
		"\3\2\2\2w{\7\r\2\2xz\7\7\2\2yx\3\2\2\2z}\3\2\2\2{y\3\2\2\2{|\3\2\2\2|"+
		"~\3\2\2\2}{\3\2\2\2~\u008b\5\34\17\2\177\u0081\7\n\2\2\u0080\177\3\2\2"+
		"\2\u0080\u0081\3\2\2\2\u0081\u0085\3\2\2\2\u0082\u0084\7\7\2\2\u0083\u0082"+
		"\3\2\2\2\u0084\u0087\3\2\2\2\u0085\u0083\3\2\2\2\u0085\u0086\3\2\2\2\u0086"+
		"\u0088\3\2\2\2\u0087\u0085\3\2\2\2\u0088\u008a\5\34\17\2\u0089\u0080\3"+
		"\2\2\2\u008a\u008d\3\2\2\2\u008b\u0089\3\2\2\2\u008b\u008c\3\2\2\2\u008c"+
		"\u008f\3\2\2\2\u008d\u008b\3\2\2\2\u008e\u0090\7\n\2\2\u008f\u008e\3\2"+
		"\2\2\u008f\u0090\3\2\2\2\u0090\u0094\3\2\2\2\u0091\u0093\7\7\2\2\u0092"+
		"\u0091\3\2\2\2\u0093\u0096\3\2\2\2\u0094\u0092\3\2\2\2\u0094\u0095\3\2"+
		"\2\2\u0095\u0097\3\2\2\2\u0096\u0094\3\2\2\2\u0097\u0098\7\16\2\2\u0098"+
		"\t\3\2\2\2\u0099\u009a\7\21\2\2\u009a\u009b\7\t\2\2\u009b\u00a6\5\f\7"+
		"\2\u009c\u009e\7\7\2\2\u009d\u009c\3\2\2\2\u009e\u00a1\3\2\2\2\u009f\u009d"+
		"\3\2\2\2\u009f\u00a0\3\2\2\2\u00a0\u00a2\3\2\2\2\u00a1\u009f\3\2\2\2\u00a2"+
		"\u00a3\7\b\2\2\u00a3\u00a5\5\f\7\2\u00a4\u009f\3\2\2\2\u00a5\u00a8\3\2"+
		"\2\2\u00a6\u00a4\3\2\2\2\u00a6\u00a7\3\2\2\2\u00a7\u00ae\3\2\2\2\u00a8"+
		"\u00a6\3\2\2\2\u00a9\u00aa\7\21\2\2\u00aa\u00ae\5\6\4\2\u00ab\u00ac\7"+
		"\21\2\2\u00ac\u00ae\5\b\5\2\u00ad\u0099\3\2\2\2\u00ad\u00a9\3\2\2\2\u00ad"+
		"\u00ab\3\2\2\2\u00ae\13\3\2\2\2\u00af\u00c3\5(\25\2\u00b0\u00b1\7\13\2"+
		"\2\u00b1\u00bc\5\f\7\2\u00b2\u00b4\7\7\2\2\u00b3\u00b2\3\2\2\2\u00b4\u00b7"+
		"\3\2\2\2\u00b5\u00b3\3\2\2\2\u00b5\u00b6\3\2\2\2\u00b6\u00b8\3\2\2\2\u00b7"+
		"\u00b5\3\2\2\2\u00b8\u00b9\7\b\2\2\u00b9\u00bb\5\f\7\2\u00ba\u00b5\3\2"+
		"\2\2\u00bb\u00be\3\2\2\2\u00bc\u00ba\3\2\2\2\u00bc\u00bd\3\2\2\2\u00bd"+
		"\u00bf\3\2\2\2\u00be\u00bc\3\2\2\2\u00bf\u00c0\7\f\2\2\u00c0\u00c3\3\2"+
		"\2\2\u00c1\u00c3\5\36\20\2\u00c2\u00af\3\2\2\2\u00c2\u00b0\3\2\2\2\u00c2"+
		"\u00c1\3\2\2\2\u00c3\r\3\2\2\2\u00c4\u00c8\7\25\2\2\u00c5\u00c7\7\7\2"+
		"\2\u00c6\u00c5\3\2\2\2\u00c7\u00ca\3\2\2\2\u00c8\u00c6\3\2\2\2\u00c8\u00c9"+
		"\3\2\2\2\u00c9\u00cb\3\2\2\2\u00ca\u00c8\3\2\2\2\u00cb\u00cc\5\"\22\2"+
		"\u00cc\u00d0\7\27\2\2\u00cd\u00cf\7\7\2\2\u00ce\u00cd\3\2\2\2\u00cf\u00d2"+
		"\3\2\2\2\u00d0\u00ce\3\2\2\2\u00d0\u00d1\3\2\2\2\u00d1\u00d3\3\2\2\2\u00d2"+
		"\u00d0\3\2\2\2\u00d3\u00d7\5\20\t\2\u00d4\u00d6\7\7\2\2\u00d5\u00d4\3"+
		"\2\2\2\u00d6\u00d9\3\2\2\2\u00d7\u00d5\3\2\2\2\u00d7\u00d8\3\2\2\2\u00d8"+
		"\u00ee\3\2\2\2\u00d9\u00d7\3\2\2\2\u00da\u00de\7\30\2\2\u00db\u00dd\7"+
		"\7\2\2\u00dc\u00db\3\2\2\2\u00dd\u00e0\3\2\2\2\u00de\u00dc\3\2\2\2\u00de"+
		"\u00df\3\2\2\2\u00df\u00e2\3\2\2\2\u00e0\u00de\3\2\2\2\u00e1\u00e3\5\""+
		"\22\2\u00e2\u00e1\3\2\2\2\u00e2\u00e3\3\2\2\2\u00e3\u00e4\3\2\2\2\u00e4"+
		"\u00e8\7\27\2\2\u00e5\u00e7\7\7\2\2\u00e6\u00e5\3\2\2\2\u00e7\u00ea\3"+
		"\2\2\2\u00e8\u00e6\3\2\2\2\u00e8\u00e9\3\2\2\2\u00e9\u00eb\3\2\2\2\u00ea"+
		"\u00e8\3\2\2\2\u00eb\u00ed\5\20\t\2\u00ec\u00da\3\2\2\2\u00ed\u00f0\3"+
		"\2\2\2\u00ee\u00ec\3\2\2\2\u00ee\u00ef\3\2\2\2\u00ef\u00f4\3\2\2\2\u00f0"+
		"\u00ee\3\2\2\2\u00f1\u00f3\7\7\2\2\u00f2\u00f1\3\2\2\2\u00f3\u00f6\3\2"+
		"\2\2\u00f4\u00f2\3\2\2\2\u00f4\u00f5\3\2\2\2\u00f5\u00f7\3\2\2\2\u00f6"+
		"\u00f4\3\2\2\2\u00f7\u00f8\7!\2\2\u00f8\17\3\2\2\2\u00f9\u0103\5\4\3\2"+
		"\u00fa\u00ff\7\n\2\2\u00fb\u00ff\7\7\2\2\u00fc\u00fd\7\n\2\2\u00fd\u00ff"+
		"\7\7\2\2\u00fe\u00fa\3\2\2\2\u00fe\u00fb\3\2\2\2\u00fe\u00fc\3\2\2\2\u00ff"+
		"\u0100\3\2\2\2\u0100\u0102\5\4\3\2\u0101\u00fe\3\2\2\2\u0102\u0105\3\2"+
		"\2\2\u0103\u0101\3\2\2\2\u0103\u0104\3\2\2\2\u0104\u0107\3\2\2\2\u0105"+
		"\u0103\3\2\2\2\u0106\u0108\7\n\2\2\u0107\u0106\3\2\2\2\u0107\u0108\3\2"+
		"\2\2\u0108\u010c\3\2\2\2\u0109\u010b\7\7\2\2\u010a\u0109\3\2\2\2\u010b"+
		"\u010e\3\2\2\2\u010c\u010a\3\2\2\2\u010c\u010d\3\2\2\2\u010d\21\3\2\2"+
		"\2\u010e\u010c\3\2\2\2\u010f\u0113\7\25\2\2\u0110\u0112\7\7\2\2\u0111"+
		"\u0110\3\2\2\2\u0112\u0115\3\2\2\2\u0113\u0111\3\2\2\2\u0113\u0114\3\2"+
		"\2\2\u0114\u0116\3\2\2\2\u0115\u0113\3\2\2\2\u0116\u0117\5\"\22\2\u0117"+
		"\u011b\7\27\2\2\u0118\u011a\7\7\2\2\u0119\u0118\3\2\2\2\u011a\u011d\3"+
		"\2\2\2\u011b\u0119\3\2\2\2\u011b\u011c\3\2\2\2\u011c\u011e\3\2\2\2\u011d"+
		"\u011b\3\2\2\2\u011e\u0122\5\24\13\2\u011f\u0121\7\7\2\2\u0120\u011f\3"+
		"\2\2\2\u0121\u0124\3\2\2\2\u0122\u0120\3\2\2\2\u0122\u0123\3\2\2\2\u0123"+
		"\u0139\3\2\2\2\u0124\u0122\3\2\2\2\u0125\u0129\7\30\2\2\u0126\u0128\7"+
		"\7\2\2\u0127\u0126\3\2\2\2\u0128\u012b\3\2\2\2\u0129\u0127\3\2\2\2\u0129"+
		"\u012a\3\2\2\2\u012a\u012d\3\2\2\2\u012b\u0129\3\2\2\2\u012c\u012e\5\""+
		"\22\2\u012d\u012c\3\2\2\2\u012d\u012e\3\2\2\2\u012e\u012f\3\2\2\2\u012f"+
		"\u0133\7\27\2\2\u0130\u0132\7\7\2\2\u0131\u0130\3\2\2\2\u0132\u0135\3"+
		"\2\2\2\u0133\u0131\3\2\2\2\u0133\u0134\3\2\2\2\u0134\u0136\3\2\2\2\u0135"+
		"\u0133\3\2\2\2\u0136\u0138\5\24\13\2\u0137\u0125\3\2\2\2\u0138\u013b\3"+
		"\2\2\2\u0139\u0137\3\2\2\2\u0139\u013a\3\2\2\2\u013a\u013f\3\2\2\2\u013b"+
		"\u0139\3\2\2\2\u013c\u013e\7\7\2\2\u013d\u013c\3\2\2\2\u013e\u0141\3\2"+
		"\2\2\u013f\u013d\3\2\2\2\u013f\u0140\3\2\2\2\u0140\u0142\3\2\2\2\u0141"+
		"\u013f\3\2\2\2\u0142\u0143\7!\2\2\u0143\23\3\2\2\2\u0144\u0158\5\26\f"+
		"\2\u0145\u0154\7\n\2\2\u0146\u0148\7\7\2\2\u0147\u0146\3\2\2\2\u0148\u014b"+
		"\3\2\2\2\u0149\u0147\3\2\2\2\u0149\u014a\3\2\2\2\u014a\u0154\3\2\2\2\u014b"+
		"\u0149\3\2\2\2\u014c\u0150\7\n\2\2\u014d\u014f\7\7\2\2\u014e\u014d\3\2"+
		"\2\2\u014f\u0152\3\2\2\2\u0150\u014e\3\2\2\2\u0150\u0151\3\2\2\2\u0151"+
		"\u0154\3\2\2\2\u0152\u0150\3\2\2\2\u0153\u0145\3\2\2\2\u0153\u0149\3\2"+
		"\2\2\u0153\u014c\3\2\2\2\u0154\u0155\3\2\2\2\u0155\u0157\5\26\f\2\u0156"+
		"\u0153\3\2\2\2\u0157\u015a\3\2\2\2\u0158\u0156\3\2\2\2\u0158\u0159\3\2"+
		"\2\2\u0159\u015c\3\2\2\2\u015a\u0158\3\2\2\2\u015b\u015d\7\n\2\2\u015c"+
		"\u015b\3\2\2\2\u015c\u015d\3\2\2\2\u015d\u0161\3\2\2\2\u015e\u0160\7\7"+
		"\2\2\u015f\u015e\3\2\2\2\u0160\u0163\3\2\2\2\u0161\u015f\3\2\2\2\u0161"+
		"\u0162\3\2\2\2\u0162\25\3\2\2\2\u0163\u0161\3\2\2\2\u0164\u0167\5\n\6"+
		"\2\u0165\u0167\5\22\n\2\u0166\u0164\3\2\2\2\u0166\u0165\3\2\2\2\u0167"+
		"\27\3\2\2\2\u0168\u016c\7\25\2\2\u0169\u016b\7\7\2\2\u016a\u0169\3\2\2"+
		"\2\u016b\u016e\3\2\2\2\u016c\u016a\3\2\2\2\u016c\u016d\3\2\2\2\u016d\u016f"+
		"\3\2\2\2\u016e\u016c\3\2\2\2\u016f\u0170\5\"\22\2\u0170\u0174\7\27\2\2"+
		"\u0171\u0173\7\7\2\2\u0172\u0171\3\2\2\2\u0173\u0176\3\2\2\2\u0174\u0172"+
		"\3\2\2\2\u0174\u0175\3\2\2\2\u0175\u0177\3\2\2\2\u0176\u0174\3\2\2\2\u0177"+
		"\u017b\5\32\16\2\u0178\u017a\7\7\2\2\u0179\u0178\3\2\2\2\u017a\u017d\3"+
		"\2\2\2\u017b\u0179\3\2\2\2\u017b\u017c\3\2\2\2\u017c\u0192\3\2\2\2\u017d"+
		"\u017b\3\2\2\2\u017e\u0182\7\30\2\2\u017f\u0181\7\7\2\2\u0180\u017f\3"+
		"\2\2\2\u0181\u0184\3\2\2\2\u0182\u0180\3\2\2\2\u0182\u0183\3\2\2\2\u0183"+
		"\u0186\3\2\2\2\u0184\u0182\3\2\2\2\u0185\u0187\5\"\22\2\u0186\u0185\3"+
		"\2\2\2\u0186\u0187\3\2\2\2\u0187\u0188\3\2\2\2\u0188\u018c\7\27\2\2\u0189"+
		"\u018b\7\7\2\2\u018a\u0189\3\2\2\2\u018b\u018e\3\2\2\2\u018c\u018a\3\2"+
		"\2\2\u018c\u018d\3\2\2\2\u018d\u018f\3\2\2\2\u018e\u018c\3\2\2\2\u018f"+
		"\u0191\5\32\16\2\u0190\u017e\3\2\2\2\u0191\u0194\3\2\2\2\u0192\u0190\3"+
		"\2\2\2\u0192\u0193\3\2\2\2\u0193\u0198\3\2\2\2\u0194\u0192\3\2\2\2\u0195"+
		"\u0197\7\7\2\2\u0196\u0195\3\2\2\2\u0197\u019a\3\2\2\2\u0198\u0196\3\2"+
		"\2\2\u0198\u0199\3\2\2\2\u0199\u019b\3\2\2\2\u019a\u0198\3\2\2\2\u019b"+
		"\u019c\7!\2\2\u019c\31\3\2\2\2\u019d\u01b1\5\34\17\2\u019e\u01ad\7\n\2"+
		"\2\u019f\u01a1\7\7\2\2\u01a0\u019f\3\2\2\2\u01a1\u01a4\3\2\2\2\u01a2\u01a0"+
		"\3\2\2\2\u01a2\u01a3\3\2\2\2\u01a3\u01ad\3\2\2\2\u01a4\u01a2\3\2\2\2\u01a5"+
		"\u01a9\7\n\2\2\u01a6\u01a8\7\7\2\2\u01a7\u01a6\3\2\2\2\u01a8\u01ab\3\2"+
		"\2\2\u01a9\u01a7\3\2\2\2\u01a9\u01aa\3\2\2\2\u01aa\u01ad\3\2\2\2\u01ab"+
		"\u01a9\3\2\2\2\u01ac\u019e\3\2\2\2\u01ac\u01a2\3\2\2\2\u01ac\u01a5\3\2"+
		"\2\2\u01ad\u01ae\3\2\2\2\u01ae\u01b0\5\34\17\2\u01af\u01ac\3\2\2\2\u01b0"+
		"\u01b3\3\2\2\2\u01b1\u01af\3\2\2\2\u01b1\u01b2\3\2\2\2\u01b2\u01b5\3\2"+
		"\2\2\u01b3\u01b1\3\2\2\2\u01b4\u01b6\7\n\2\2\u01b5\u01b4\3\2\2\2\u01b5"+
		"\u01b6\3\2\2\2\u01b6\u01ba\3\2\2\2\u01b7\u01b9\7\7\2\2\u01b8\u01b7\3\2"+
		"\2\2\u01b9\u01bc\3\2\2\2\u01ba\u01b8\3\2\2\2\u01ba\u01bb\3\2\2\2\u01bb"+
		"\33\3\2\2\2\u01bc\u01ba\3\2\2\2\u01bd\u01c0\5(\25\2\u01be\u01c0\5\30\r"+
		"\2\u01bf\u01bd\3\2\2\2\u01bf\u01be\3\2\2\2\u01c0\35\3\2\2\2\u01c1\u01c5"+
		"\7\25\2\2\u01c2\u01c4\7\7\2\2\u01c3\u01c2\3\2\2\2\u01c4\u01c7\3\2\2\2"+
		"\u01c5\u01c3\3\2\2\2\u01c5\u01c6\3\2\2\2\u01c6\u01c8\3\2\2\2\u01c7\u01c5"+
		"\3\2\2\2\u01c8\u01c9\5\"\22\2\u01c9\u01cd\7\27\2\2\u01ca\u01cc\7\7\2\2"+
		"\u01cb\u01ca\3\2\2\2\u01cc\u01cf\3\2\2\2\u01cd\u01cb\3\2\2\2\u01cd\u01ce"+
		"\3\2\2\2\u01ce\u01d0\3\2\2\2\u01cf\u01cd\3\2\2\2\u01d0\u01d4\5 \21\2\u01d1"+
		"\u01d3\7\7\2\2\u01d2\u01d1\3\2\2\2\u01d3\u01d6\3\2\2\2\u01d4\u01d2\3\2"+
		"\2\2\u01d4\u01d5\3\2\2\2\u01d5\u01ea\3\2\2\2\u01d6\u01d4\3\2\2\2\u01d7"+
		"\u01db\7\30\2\2\u01d8\u01da\7\7\2\2\u01d9\u01d8\3\2\2\2\u01da\u01dd\3"+
		"\2\2\2\u01db\u01d9\3\2\2\2\u01db\u01dc\3\2\2\2\u01dc\u01de\3\2\2\2\u01dd"+
		"\u01db\3\2\2\2\u01de\u01df\5\"\22\2\u01df\u01e3\7\27\2\2\u01e0\u01e2\7"+
		"\7\2\2\u01e1\u01e0\3\2\2\2\u01e2\u01e5\3\2\2\2\u01e3\u01e1\3\2\2\2\u01e3"+
		"\u01e4\3\2\2\2\u01e4\u01e6\3\2\2\2\u01e5\u01e3\3\2\2\2\u01e6\u01e7\5 "+
		"\21\2\u01e7\u01e9\3\2\2\2\u01e8\u01d7\3\2\2\2\u01e9\u01ec\3\2\2\2\u01ea"+
		"\u01e8\3\2\2\2\u01ea\u01eb\3\2\2\2\u01eb\u01f0\3\2\2\2\u01ec\u01ea\3\2"+
		"\2\2\u01ed\u01ef\7\7\2\2\u01ee\u01ed\3\2\2\2\u01ef\u01f2\3\2\2\2\u01f0"+
		"\u01ee\3\2\2\2\u01f0\u01f1\3\2\2\2\u01f1\u01f3\3\2\2\2\u01f2\u01f0\3\2"+
		"\2\2\u01f3\u01f7\7\30\2\2\u01f4\u01f6\7\7\2\2\u01f5\u01f4\3\2\2\2\u01f6"+
		"\u01f9\3\2\2\2\u01f7\u01f5\3\2\2\2\u01f7\u01f8\3\2\2\2\u01f8\u01fa\3\2"+
		"\2\2\u01f9\u01f7\3\2\2\2\u01fa\u01fe\7\27\2\2\u01fb\u01fd\7\7\2\2\u01fc"+
		"\u01fb\3\2\2\2\u01fd\u0200\3\2\2\2\u01fe\u01fc\3\2\2\2\u01fe\u01ff\3\2"+
		"\2\2\u01ff\u0201\3\2\2\2\u0200\u01fe\3\2\2\2\u0201\u0202\5 \21\2\u0202"+
		"\u0206\3\2\2\2\u0203\u0205\7\7\2\2\u0204\u0203\3\2\2\2\u0205\u0208\3\2"+
		"\2\2\u0206\u0204\3\2\2\2\u0206\u0207\3\2\2\2\u0207\u0209\3\2\2\2\u0208"+
		"\u0206\3\2\2\2\u0209\u020a\7!\2\2\u020a\37\3\2\2\2\u020b\u0216\5\f\7\2"+
		"\u020c\u020e\7\7\2\2\u020d\u020c\3\2\2\2\u020e\u0211\3\2\2\2\u020f\u020d"+
		"\3\2\2\2\u020f\u0210\3\2\2\2\u0210\u0212\3\2\2\2\u0211\u020f\3\2\2\2\u0212"+
		"\u0213\7\b\2\2\u0213\u0215\5\f\7\2\u0214\u020f\3\2\2\2\u0215\u0218\3\2"+
		"\2\2\u0216\u0214\3\2\2\2\u0216\u0217\3\2\2\2\u0217!\3\2\2\2\u0218\u0216"+
		"\3\2\2\2\u0219\u021b\7\36\2\2\u021a\u0219\3\2\2\2\u021a\u021b\3\2\2\2"+
		"\u021b\u021e\3\2\2\2\u021c\u021f\5$\23\2\u021d\u021f\5&\24\2\u021e\u021c"+
		"\3\2\2\2\u021e\u021d\3\2\2\2\u021f\u022a\3\2\2\2\u0220\u0222\t\2\2\2\u0221"+
		"\u0223\7\36\2\2\u0222\u0221\3\2\2\2\u0222\u0223\3\2\2\2\u0223\u0226\3"+
		"\2\2\2\u0224\u0227\5$\23\2\u0225\u0227\5&\24\2\u0226\u0224\3\2\2\2\u0226"+
		"\u0225\3\2\2\2\u0227\u0229\3\2\2\2\u0228\u0220\3\2\2\2\u0229\u022c\3\2"+
		"\2\2\u022a\u0228\3\2\2\2\u022a\u022b\3\2\2\2\u022b#\3\2\2\2\u022c\u022a"+
		"\3\2\2\2\u022d\u022f\7\7\2\2\u022e\u022d\3\2\2\2\u022f\u0232\3\2\2\2\u0230"+
		"\u022e\3\2\2\2\u0230\u0231\3\2\2\2\u0231\u0234\3\2\2\2\u0232\u0230\3\2"+
		"\2\2\u0233\u0235\7\21\2\2\u0234\u0233\3\2\2\2\u0234\u0235\3\2\2\2\u0235"+
		"\u0237\3\2\2\2\u0236\u0238\7\37\2\2\u0237\u0236\3\2\2\2\u0237\u0238\3"+
		"\2\2\2\u0238\u023a\3\2\2\2\u0239\u023b\7\33\2\2\u023a\u0239\3\2\2\2\u023a"+
		"\u023b\3\2\2\2\u023b\u023c\3\2\2\2\u023c\u023e\5(\25\2\u023d\u023f\7\33"+
		"\2\2\u023e\u023d\3\2\2\2\u023e\u023f\3\2\2\2\u023f\u024a\3\2\2\2\u0240"+
		"\u0242\7\35\2\2\u0241\u0243\7\33\2\2\u0242\u0241\3\2\2\2\u0242\u0243\3"+
		"\2\2\2\u0243\u0244\3\2\2\2\u0244\u0246\5(\25\2\u0245\u0247\7\33\2\2\u0246"+
		"\u0245\3\2\2\2\u0246\u0247\3\2\2\2\u0247\u0249\3\2\2\2\u0248\u0240\3\2"+
		"\2\2\u0249\u024c\3\2\2\2\u024a\u0248\3\2\2\2\u024a\u024b\3\2\2\2\u024b"+
		"\u0250\3\2\2\2\u024c\u024a\3\2\2\2\u024d\u024f\7\7\2\2\u024e\u024d\3\2"+
		"\2\2\u024f\u0252\3\2\2\2\u0250\u024e\3\2\2\2\u0250\u0251\3\2\2\2\u0251"+
		"%\3\2\2\2\u0252\u0250\3\2\2\2\u0253\u0254\7\25\2\2\u0254\u0259\5\"\22"+
		"\2\u0255\u0256\t\2\2\2\u0256\u0258\5\"\22\2\u0257\u0255\3\2\2\2\u0258"+
		"\u025b\3\2\2\2\u0259\u0257\3\2\2\2\u0259\u025a\3\2\2\2\u025a\u025c\3\2"+
		"\2\2\u025b\u0259\3\2\2\2\u025c\u025d\7!\2\2\u025d\'\3\2\2\2\u025e\u0268"+
		"\5\6\4\2\u025f\u0268\5\b\5\2\u0260\u0268\5\n\6\2\u0261\u0268\7\23\2\2"+
		"\u0262\u0268\7\17\2\2\u0263\u0268\7\21\2\2\u0264\u0268\7\5\2\2\u0265\u0268"+
		"\7\6\2\2\u0266\u0268\7\4\2\2\u0267\u025e\3\2\2\2\u0267\u025f\3\2\2\2\u0267"+
		"\u0260\3\2\2\2\u0267\u0261\3\2\2\2\u0267\u0262\3\2\2\2\u0267\u0263\3\2"+
		"\2\2\u0267\u0264\3\2\2\2\u0267\u0265\3\2\2\2\u0267\u0266\3\2\2\2\u0268"+
		")\3\2\2\2b-\65;=BFHMV\\aflr{\u0080\u0085\u008b\u008f\u0094\u009f\u00a6"+
		"\u00ad\u00b5\u00bc\u00c2\u00c8\u00d0\u00d7\u00de\u00e2\u00e8\u00ee\u00f4"+
		"\u00fe\u0103\u0107\u010c\u0113\u011b\u0122\u0129\u012d\u0133\u0139\u013f"+
		"\u0149\u0150\u0153\u0158\u015c\u0161\u0166\u016c\u0174\u017b\u0182\u0186"+
		"\u018c\u0192\u0198\u01a2\u01a9\u01ac\u01b1\u01b5\u01ba\u01bf\u01c5\u01cd"+
		"\u01d4\u01db\u01e3\u01ea\u01f0\u01f7\u01fe\u0206\u020f\u0216\u021a\u021e"+
		"\u0222\u0226\u022a\u0230\u0234\u0237\u023a\u023e\u0242\u0246\u024a\u0250"+
		"\u0259\u0267";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}