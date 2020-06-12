/*
 * MIT License
 *
 * Copyright (c) 2020 NUM Technology Ltd
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

// Generated from /Users/tonywalmsley/work/num/grammar/antlr4/MODLLexer.g4 by ANTLR 4.7.2
package uk.modl.parser.antlr;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MODLLexer extends Lexer {

    public static final int
            WS = 1, NULL = 2, TRUE = 3, FALSE = 4, COLON = 5, EQUALS = 6, STRUCT_SEP = 7, ARR_SEP = 8,
            LBRAC = 9, RBRAC = 10, LSBRAC = 11, RSBRAC = 12, NUMBER = 13, COMMENT = 14, QUOTED = 15,
            STRING = 16, HASH_PREFIX = 17, LCBRAC = 18, CWS = 19, QMARK = 20, FSLASH = 21, GTHAN = 22,
            LTHAN = 23, ASTERISK = 24, AMP = 25, PIPE = 26, EXCLAM = 27, CCOMMENT = 28, RCBRAC = 29;

    public static final int
            CONDITIONAL = 1;

    public static final String[] ruleNames = makeRuleNames();

    /**
     * @deprecated Use {@link #VOCABULARY} instead.
     */
    @Deprecated
    public static final String[] tokenNames;

    public static final String _serializedATN =
            "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\37\u01f6\b\1\b\1" +
                    "\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t" +
                    "\n\4\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4" +
                    "\22\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4" +
                    "\31\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4" +
                    " \t \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4" +
                    "+\t+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4" +
                    "\64\t\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\3\2\6\2v\n\2\r\2\16" +
                    "\2w\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u0087\n\3" +
                    "\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4\u0093\n\4\3\5\3\5\3\5\3\5" +
                    "\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\5\5\u00a1\n\5\3\6\3\6\3\7\3\7\3\b\3\b" +
                    "\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\5\16\u00b4\n\16\3\16\3" +
                    "\16\3\16\6\16\u00b9\n\16\r\16\16\16\u00ba\5\16\u00bd\n\16\3\16\5\16\u00c0" +
                    "\n\16\3\17\3\17\3\17\7\17\u00c5\n\17\f\17\16\17\u00c8\13\17\5\17\u00ca" +
                    "\n\17\3\20\3\20\5\20\u00ce\n\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21" +
                    "\3\21\3\22\7\22\u00da\n\22\f\22\16\22\u00dd\13\22\3\23\3\23\3\23\3\23" +
                    "\3\23\3\23\7\23\u00e5\n\23\f\23\16\23\u00e8\13\23\3\23\3\23\3\23\3\23" +
                    "\3\23\5\23\u00ef\n\23\3\24\3\24\3\25\7\25\u00f4\n\25\f\25\16\25\u00f7" +
                    "\13\25\3\26\3\26\5\26\u00fb\n\26\3\26\3\26\3\26\6\26\u0100\n\26\r\26\16" +
                    "\26\u0101\3\26\5\26\u0105\n\26\3\26\6\26\u0108\n\26\r\26\16\26\u0109\3" +
                    "\26\5\26\u010d\n\26\3\26\3\26\6\26\u0111\n\26\r\26\16\26\u0112\7\26\u0115" +
                    "\n\26\f\26\16\26\u0118\13\26\3\27\3\27\3\30\3\30\3\31\3\31\3\31\5\31\u0121" +
                    "\n\31\3\31\3\31\3\31\3\31\3\31\5\31\u0128\n\31\5\31\u012a\n\31\3\32\3" +
                    "\32\3\32\3\32\3\32\3\32\3\33\3\33\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3" +
                    "\36\6\36\u013c\n\36\r\36\16\36\u013d\3\36\3\36\3\37\3\37\3\37\3\37\3\37" +
                    "\3\37\3\37\3\37\3\37\3\37\3\37\5\37\u014d\n\37\3\37\3\37\3 \3 \3 \3 \3" +
                    " \3 \3 \3 \3 \3 \5 \u015b\n \3 \3 \3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3" +
                    "!\5!\u016b\n!\3!\3!\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3$\3$\3$\3$\3%\3%\3%\3" +
                    "%\3&\3&\3&\3&\3\'\3\'\3\'\3\'\3(\3(\3(\3(\3)\5)\u018c\n)\3)\3)\3)\6)\u0191" +
                    "\n)\r)\16)\u0192\5)\u0195\n)\3)\5)\u0198\n)\3)\3)\3*\3*\3+\3+\3,\3,\3" +
                    "-\3-\3.\3.\3/\3/\3\60\3\60\3\61\3\61\3\62\3\62\3\62\3\62\3\62\3\63\3\63" +
                    "\3\63\3\63\3\63\3\63\7\63\u01b7\n\63\f\63\16\63\u01ba\13\63\3\63\3\63" +
                    "\3\63\3\63\3\63\5\63\u01c1\n\63\3\63\3\63\3\64\3\64\6\64\u01c7\n\64\r" +
                    "\64\16\64\u01c8\3\64\6\64\u01cc\n\64\r\64\16\64\u01cd\3\64\3\64\6\64\u01d2" +
                    "\n\64\r\64\16\64\u01d3\7\64\u01d6\n\64\f\64\16\64\u01d9\13\64\3\64\3\64" +
                    "\3\65\3\65\3\66\3\66\3\67\3\67\3\67\5\67\u01e4\n\67\3\67\3\67\3\67\3\67" +
                    "\5\67\u01ea\n\67\38\38\38\38\38\38\38\39\39\39\39\2\2:\4\3\6\4\b\5\n\6" +
                    "\f\7\16\b\20\t\22\n\24\13\26\f\30\r\32\16\34\17\36\2 \2\"\20$\2&\21(\2" +
                    "*\2,\22.\2\60\2\62\2\64\2\66\28\23:\24<\25>\2@\2B\2D\2F\2H\2J\2L\2N\2" +
                    "P\2R\2T\26V\27X\30Z\31\\\32^\33`\34b\35d\2f\2h\2j\2l\2n\2p\36r\37\4\2" +
                    "\3\16\5\2\13\f\17\17\"\"\3\2\62;\3\2\63;\4\2GGgg\4\2--//\4\2\f\f\17\17" +
                    "\3\2$$\3\2bb\f\2\n\f\16\17\"\"$%*+<=??]_}}\177\u0080\t\2\61\61^^ddhhp" +
                    "pttvv\5\2\62;CHch\13\2\n\f\16\17\"%((*+\61\61<A]_}\u0080\2\u0221\2\4\3" +
                    "\2\2\2\2\6\3\2\2\2\2\b\3\2\2\2\2\n\3\2\2\2\2\f\3\2\2\2\2\16\3\2\2\2\2" +
                    "\20\3\2\2\2\2\22\3\2\2\2\2\24\3\2\2\2\2\26\3\2\2\2\2\30\3\2\2\2\2\32\3" +
                    "\2\2\2\2\34\3\2\2\2\2\"\3\2\2\2\2&\3\2\2\2\2,\3\2\2\2\28\3\2\2\2\2:\3" +
                    "\2\2\2\3<\3\2\2\2\3>\3\2\2\2\3@\3\2\2\2\3B\3\2\2\2\3D\3\2\2\2\3F\3\2\2" +
                    "\2\3H\3\2\2\2\3J\3\2\2\2\3L\3\2\2\2\3N\3\2\2\2\3P\3\2\2\2\3R\3\2\2\2\3" +
                    "T\3\2\2\2\3V\3\2\2\2\3X\3\2\2\2\3Z\3\2\2\2\3\\\3\2\2\2\3^\3\2\2\2\3`\3" +
                    "\2\2\2\3b\3\2\2\2\3d\3\2\2\2\3f\3\2\2\2\3h\3\2\2\2\3p\3\2\2\2\3r\3\2\2" +
                    "\2\4u\3\2\2\2\6\u0086\3\2\2\2\b\u0092\3\2\2\2\n\u00a0\3\2\2\2\f\u00a2" +
                    "\3\2\2\2\16\u00a4\3\2\2\2\20\u00a6\3\2\2\2\22\u00a8\3\2\2\2\24\u00aa\3" +
                    "\2\2\2\26\u00ac\3\2\2\2\30\u00ae\3\2\2\2\32\u00b0\3\2\2\2\34\u00b3\3\2" +
                    "\2\2\36\u00c9\3\2\2\2 \u00cb\3\2\2\2\"\u00d1\3\2\2\2$\u00db\3\2\2\2&\u00ee" +
                    "\3\2\2\2(\u00f0\3\2\2\2*\u00f5\3\2\2\2,\u00fa\3\2\2\2.\u0119\3\2\2\2\60" +
                    "\u011b\3\2\2\2\62\u0129\3\2\2\2\64\u012b\3\2\2\2\66\u0131\3\2\2\28\u0133" +
                    "\3\2\2\2:\u0136\3\2\2\2<\u013b\3\2\2\2>\u014c\3\2\2\2@\u015a\3\2\2\2B" +
                    "\u016a\3\2\2\2D\u016e\3\2\2\2F\u0172\3\2\2\2H\u0176\3\2\2\2J\u017a\3\2" +
                    "\2\2L\u017e\3\2\2\2N\u0182\3\2\2\2P\u0186\3\2\2\2R\u018b\3\2\2\2T\u019b" +
                    "\3\2\2\2V\u019d\3\2\2\2X\u019f\3\2\2\2Z\u01a1\3\2\2\2\\\u01a3\3\2\2\2" +
                    "^\u01a5\3\2\2\2`\u01a7\3\2\2\2b\u01a9\3\2\2\2d\u01ab\3\2\2\2f\u01c0\3" +
                    "\2\2\2h\u01c6\3\2\2\2j\u01dc\3\2\2\2l\u01de\3\2\2\2n\u01e9\3\2\2\2p\u01eb" +
                    "\3\2\2\2r\u01f2\3\2\2\2tv\t\2\2\2ut\3\2\2\2vw\3\2\2\2wu\3\2\2\2wx\3\2" +
                    "\2\2xy\3\2\2\2yz\b\2\2\2z\5\3\2\2\2{|\7\62\2\2|}\7\62\2\2}\u0087\7\62" +
                    "\2\2~\177\7p\2\2\177\u0080\7w\2\2\u0080\u0081\7n\2\2\u0081\u0087\7n\2" +
                    "\2\u0082\u0083\7P\2\2\u0083\u0084\7W\2\2\u0084\u0085\7N\2\2\u0085\u0087" +
                    "\7N\2\2\u0086{\3\2\2\2\u0086~\3\2\2\2\u0086\u0082\3\2\2\2\u0087\7\3\2" +
                    "\2\2\u0088\u0089\7\62\2\2\u0089\u0093\7\63\2\2\u008a\u008b\7v\2\2\u008b" +
                    "\u008c\7t\2\2\u008c\u008d\7w\2\2\u008d\u0093\7g\2\2\u008e\u008f\7V\2\2" +
                    "\u008f\u0090\7T\2\2\u0090\u0091\7W\2\2\u0091\u0093\7G\2\2\u0092\u0088" +
                    "\3\2\2\2\u0092\u008a\3\2\2\2\u0092\u008e\3\2\2\2\u0093\t\3\2\2\2\u0094" +
                    "\u0095\7\62\2\2\u0095\u00a1\7\62\2\2\u0096\u0097\7h\2\2\u0097\u0098\7" +
                    "c\2\2\u0098\u0099\7n\2\2\u0099\u009a\7u\2\2\u009a\u00a1\7g\2\2\u009b\u009c" +
                    "\7H\2\2\u009c\u009d\7C\2\2\u009d\u009e\7N\2\2\u009e\u009f\7U\2\2\u009f" +
                    "\u00a1\7G\2\2\u00a0\u0094\3\2\2\2\u00a0\u0096\3\2\2\2\u00a0\u009b\3\2" +
                    "\2\2\u00a1\13\3\2\2\2\u00a2\u00a3\7<\2\2\u00a3\r\3\2\2\2\u00a4\u00a5\7" +
                    "?\2\2\u00a5\17\3\2\2\2\u00a6\u00a7\7=\2\2\u00a7\21\3\2\2\2\u00a8\u00a9" +
                    "\7.\2\2\u00a9\23\3\2\2\2\u00aa\u00ab\7*\2\2\u00ab\25\3\2\2\2\u00ac\u00ad" +
                    "\7+\2\2\u00ad\27\3\2\2\2\u00ae\u00af\7]\2\2\u00af\31\3\2\2\2\u00b0\u00b1" +
                    "\7_\2\2\u00b1\33\3\2\2\2\u00b2\u00b4\7/\2\2\u00b3\u00b2\3\2\2\2\u00b3" +
                    "\u00b4\3\2\2\2\u00b4\u00b5\3\2\2\2\u00b5\u00bc\5\36\17\2\u00b6\u00b8\7" +
                    "\60\2\2\u00b7\u00b9\t\3\2\2\u00b8\u00b7\3\2\2\2\u00b9\u00ba\3\2\2\2\u00ba" +
                    "\u00b8\3\2\2\2\u00ba\u00bb\3\2\2\2\u00bb\u00bd\3\2\2\2\u00bc\u00b6\3\2" +
                    "\2\2\u00bc\u00bd\3\2\2\2\u00bd\u00bf\3\2\2\2\u00be\u00c0\5 \20\2\u00bf" +
                    "\u00be\3\2\2\2\u00bf\u00c0\3\2\2\2\u00c0\35\3\2\2\2\u00c1\u00ca\7\62\2" +
                    "\2\u00c2\u00c6\t\4\2\2\u00c3\u00c5\t\3\2\2\u00c4\u00c3\3\2\2\2\u00c5\u00c8" +
                    "\3\2\2\2\u00c6\u00c4\3\2\2\2\u00c6\u00c7\3\2\2\2\u00c7\u00ca\3\2\2\2\u00c8" +
                    "\u00c6\3\2\2\2\u00c9\u00c1\3\2\2\2\u00c9\u00c2\3\2\2\2\u00ca\37\3\2\2" +
                    "\2\u00cb\u00cd\t\5\2\2\u00cc\u00ce\t\6\2\2\u00cd\u00cc\3\2\2\2\u00cd\u00ce" +
                    "\3\2\2\2\u00ce\u00cf\3\2\2\2\u00cf\u00d0\5\36\17\2\u00d0!\3\2\2\2\u00d1" +
                    "\u00d2\7%\2\2\u00d2\u00d3\7%\2\2\u00d3\u00d4\3\2\2\2\u00d4\u00d5\5$\22" +
                    "\2\u00d5\u00d6\3\2\2\2\u00d6\u00d7\b\21\2\2\u00d7#\3\2\2\2\u00d8\u00da" +
                    "\n\7\2\2\u00d9\u00d8\3\2\2\2\u00da\u00dd\3\2\2\2\u00db\u00d9\3\2\2\2\u00db" +
                    "\u00dc\3\2\2\2\u00dc%\3\2\2\2\u00dd\u00db\3\2\2\2\u00de\u00e6\7$\2\2\u00df" +
                    "\u00e5\5(\24\2\u00e0\u00e1\7\u0080\2\2\u00e1\u00e5\7$\2\2\u00e2\u00e3" +
                    "\7^\2\2\u00e3\u00e5\7$\2\2\u00e4\u00df\3\2\2\2\u00e4\u00e0\3\2\2\2\u00e4" +
                    "\u00e2\3\2\2\2\u00e5\u00e8\3\2\2\2\u00e6\u00e4\3\2\2\2\u00e6\u00e7\3\2" +
                    "\2\2\u00e7\u00e9\3\2\2\2\u00e8\u00e6\3\2\2\2\u00e9\u00ef\7$\2\2\u00ea" +
                    "\u00eb\7b\2\2\u00eb\u00ec\5*\25\2\u00ec\u00ed\7b\2\2\u00ed\u00ef\3\2\2" +
                    "\2\u00ee\u00de\3\2\2\2\u00ee\u00ea\3\2\2\2\u00ef\'\3\2\2\2\u00f0\u00f1" +
                    "\n\b\2\2\u00f1)\3\2\2\2\u00f2\u00f4\n\t\2\2\u00f3\u00f2\3\2\2\2\u00f4" +
                    "\u00f7\3\2\2\2\u00f5\u00f3\3\2\2\2\u00f5\u00f6\3\2\2\2\u00f6+\3\2\2\2" +
                    "\u00f7\u00f5\3\2\2\2\u00f8\u00f9\7%\2\2\u00f9\u00fb\7\"\2\2\u00fa\u00f8" +
                    "\3\2\2\2\u00fa\u00fb\3\2\2\2\u00fb\u00ff\3\2\2\2\u00fc\u0100\5\62\31\2" +
                    "\u00fd\u0100\5.\27\2\u00fe\u0100\58\34\2\u00ff\u00fc\3\2\2\2\u00ff\u00fd" +
                    "\3\2\2\2\u00ff\u00fe\3\2\2\2\u0100\u0101\3\2\2\2\u0101\u00ff\3\2\2\2\u0101" +
                    "\u0102\3\2\2\2\u0102\u0116\3\2\2\2\u0103\u0105\7%\2\2\u0104\u0103\3\2" +
                    "\2\2\u0104\u0105\3\2\2\2\u0105\u0107\3\2\2\2\u0106\u0108\7\"\2\2\u0107" +
                    "\u0106\3\2\2\2\u0108\u0109\3\2\2\2\u0109\u0107\3\2\2\2\u0109\u010a\3\2" +
                    "\2\2\u010a\u010c\3\2\2\2\u010b\u010d\7%\2\2\u010c\u010b\3\2\2\2\u010c" +
                    "\u010d\3\2\2\2\u010d\u0110\3\2\2\2\u010e\u0111\5\62\31\2\u010f\u0111\5" +
                    ".\27\2\u0110\u010e\3\2\2\2\u0110\u010f\3\2\2\2\u0111\u0112\3\2\2\2\u0112" +
                    "\u0110\3\2\2\2\u0112\u0113\3\2\2\2\u0113\u0115\3\2\2\2\u0114\u0104\3\2" +
                    "\2\2\u0115\u0118\3\2\2\2\u0116\u0114\3\2\2\2\u0116\u0117\3\2\2\2\u0117" +
                    "-\3\2\2\2\u0118\u0116\3\2\2\2\u0119\u011a\n\n\2\2\u011a/\3\2\2\2\u011b" +
                    "\u011c\t\n\2\2\u011c\61\3\2\2\2\u011d\u0120\7^\2\2\u011e\u0121\t\13\2" +
                    "\2\u011f\u0121\5\64\32\2\u0120\u011e\3\2\2\2\u0120\u011f\3\2\2\2\u0121" +
                    "\u012a\3\2\2\2\u0122\u0123\7^\2\2\u0123\u012a\5\60\30\2\u0124\u0127\7" +
                    "\u0080\2\2\u0125\u0128\5\60\30\2\u0126\u0128\5\64\32\2\u0127\u0125\3\2" +
                    "\2\2\u0127\u0126\3\2\2\2\u0128\u012a\3\2\2\2\u0129\u011d\3\2\2\2\u0129" +
                    "\u0122\3\2\2\2\u0129\u0124\3\2\2\2\u012a\63\3\2\2\2\u012b\u012c\7w\2\2" +
                    "\u012c\u012d\5\66\33\2\u012d\u012e\5\66\33\2\u012e\u012f\5\66\33\2\u012f" +
                    "\u0130\5\66\33\2\u0130\65\3\2\2\2\u0131\u0132\t\f\2\2\u0132\67\3\2\2\2" +
                    "\u0133\u0134\7%\2\2\u0134\u0135\5,\26\2\u01359\3\2\2\2\u0136\u0137\7}" +
                    "\2\2\u0137\u0138\3\2\2\2\u0138\u0139\b\35\3\2\u0139;\3\2\2\2\u013a\u013c" +
                    "\t\2\2\2\u013b\u013a\3\2\2\2\u013c\u013d\3\2\2\2\u013d\u013b\3\2\2\2\u013d" +
                    "\u013e\3\2\2\2\u013e\u013f\3\2\2\2\u013f\u0140\b\36\2\2\u0140=\3\2\2\2" +
                    "\u0141\u0142\7\62\2\2\u0142\u0143\7\62\2\2\u0143\u014d\7\62\2\2\u0144" +
                    "\u0145\7p\2\2\u0145\u0146\7w\2\2\u0146\u0147\7n\2\2\u0147\u014d\7n\2\2" +
                    "\u0148\u0149\7P\2\2\u0149\u014a\7W\2\2\u014a\u014b\7N\2\2\u014b\u014d" +
                    "\7N\2\2\u014c\u0141\3\2\2\2\u014c\u0144\3\2\2\2\u014c\u0148\3\2\2\2\u014d" +
                    "\u014e\3\2\2\2\u014e\u014f\b\37\4\2\u014f?\3\2\2\2\u0150\u0151\7\62\2" +
                    "\2\u0151\u015b\7\63\2\2\u0152\u0153\7v\2\2\u0153\u0154\7t\2\2\u0154\u0155" +
                    "\7w\2\2\u0155\u015b\7g\2\2\u0156\u0157\7V\2\2\u0157\u0158\7T\2\2\u0158" +
                    "\u0159\7W\2\2\u0159\u015b\7G\2\2\u015a\u0150\3\2\2\2\u015a\u0152\3\2\2" +
                    "\2\u015a\u0156\3\2\2\2\u015b\u015c\3\2\2\2\u015c\u015d\b \5\2\u015dA\3" +
                    "\2\2\2\u015e\u015f\7\62\2\2\u015f\u016b\7\62\2\2\u0160\u0161\7h\2\2\u0161" +
                    "\u0162\7c\2\2\u0162\u0163\7n\2\2\u0163\u0164\7u\2\2\u0164\u016b\7g\2\2" +
                    "\u0165\u0166\7H\2\2\u0166\u0167\7C\2\2\u0167\u0168\7N\2\2\u0168\u0169" +
                    "\7U\2\2\u0169\u016b\7G\2\2\u016a\u015e\3\2\2\2\u016a\u0160\3\2\2\2\u016a" +
                    "\u0165\3\2\2\2\u016b\u016c\3\2\2\2\u016c\u016d\b!\6\2\u016dC\3\2\2\2\u016e" +
                    "\u016f\7<\2\2\u016f\u0170\3\2\2\2\u0170\u0171\b\"\7\2\u0171E\3\2\2\2\u0172" +
                    "\u0173\7?\2\2\u0173\u0174\3\2\2\2\u0174\u0175\b#\b\2\u0175G\3\2\2\2\u0176" +
                    "\u0177\7=\2\2\u0177\u0178\3\2\2\2\u0178\u0179\b$\t\2\u0179I\3\2\2\2\u017a" +
                    "\u017b\7*\2\2\u017b\u017c\3\2\2\2\u017c\u017d\b%\n\2\u017dK\3\2\2\2\u017e" +
                    "\u017f\7+\2\2\u017f\u0180\3\2\2\2\u0180\u0181\b&\13\2\u0181M\3\2\2\2\u0182" +
                    "\u0183\7]\2\2\u0183\u0184\3\2\2\2\u0184\u0185\b\'\f\2\u0185O\3\2\2\2\u0186" +
                    "\u0187\7_\2\2\u0187\u0188\3\2\2\2\u0188\u0189\b(\r\2\u0189Q\3\2\2\2\u018a" +
                    "\u018c\7/\2\2\u018b\u018a\3\2\2\2\u018b\u018c\3\2\2\2\u018c\u018d\3\2" +
                    "\2\2\u018d\u0194\5\36\17\2\u018e\u0190\7\60\2\2\u018f\u0191\t\3\2\2\u0190" +
                    "\u018f\3\2\2\2\u0191\u0192\3\2\2\2\u0192\u0190\3\2\2\2\u0192\u0193\3\2" +
                    "\2\2\u0193\u0195\3\2\2\2\u0194\u018e\3\2\2\2\u0194\u0195\3\2\2\2\u0195" +
                    "\u0197\3\2\2\2\u0196\u0198\5 \20\2\u0197\u0196\3\2\2\2\u0197\u0198\3\2" +
                    "\2\2\u0198\u0199\3\2\2\2\u0199\u019a\b)\16\2\u019aS\3\2\2\2\u019b\u019c" +
                    "\7A\2\2\u019cU\3\2\2\2\u019d\u019e\7\61\2\2\u019eW\3\2\2\2\u019f\u01a0" +
                    "\7@\2\2\u01a0Y\3\2\2\2\u01a1\u01a2\7>\2\2\u01a2[\3\2\2\2\u01a3\u01a4\7" +
                    ",\2\2\u01a4]\3\2\2\2\u01a5\u01a6\7(\2\2\u01a6_\3\2\2\2\u01a7\u01a8\7~" +
                    "\2\2\u01a8a\3\2\2\2\u01a9\u01aa\7#\2\2\u01aac\3\2\2\2\u01ab\u01ac\7}\2" +
                    "\2\u01ac\u01ad\3\2\2\2\u01ad\u01ae\b\62\3\2\u01ae\u01af\b\62\17\2\u01af" +
                    "e\3\2\2\2\u01b0\u01b8\7$\2\2\u01b1\u01b7\5(\24\2\u01b2\u01b3\7\u0080\2" +
                    "\2\u01b3\u01b7\7$\2\2\u01b4\u01b5\7^\2\2\u01b5\u01b7\7$\2\2\u01b6\u01b1" +
                    "\3\2\2\2\u01b6\u01b2\3\2\2\2\u01b6\u01b4\3\2\2\2\u01b7\u01ba\3\2\2\2\u01b8" +
                    "\u01b6\3\2\2\2\u01b8\u01b9\3\2\2\2\u01b9\u01bb\3\2\2\2\u01ba\u01b8\3\2" +
                    "\2\2\u01bb\u01c1\7$\2\2\u01bc\u01bd\7b\2\2\u01bd\u01be\5*\25\2\u01be\u01bf" +
                    "\7b\2\2\u01bf\u01c1\3\2\2\2\u01c0\u01b0\3\2\2\2\u01c0\u01bc\3\2\2\2\u01c1" +
                    "\u01c2\3\2\2\2\u01c2\u01c3\b\63\20\2\u01c3g\3\2\2\2\u01c4\u01c7\5n\67" +
                    "\2\u01c5\u01c7\5j\65\2\u01c6\u01c4\3\2\2\2\u01c6\u01c5\3\2\2\2\u01c7\u01c8" +
                    "\3\2\2\2\u01c8\u01c6\3\2\2\2\u01c8\u01c9\3\2\2\2\u01c9\u01d7\3\2\2\2\u01ca" +
                    "\u01cc\7\"\2\2\u01cb\u01ca\3\2\2\2\u01cc\u01cd\3\2\2\2\u01cd\u01cb\3\2" +
                    "\2\2\u01cd\u01ce\3\2\2\2\u01ce\u01d1\3\2\2\2\u01cf\u01d2\5n\67\2\u01d0" +
                    "\u01d2\5j\65\2\u01d1\u01cf\3\2\2\2\u01d1\u01d0\3\2\2\2\u01d2\u01d3\3\2" +
                    "\2\2\u01d3\u01d1\3\2\2\2\u01d3\u01d4\3\2\2\2\u01d4\u01d6\3\2\2\2\u01d5" +
                    "\u01cb\3\2\2\2\u01d6\u01d9\3\2\2\2\u01d7\u01d5\3\2\2\2\u01d7\u01d8\3\2" +
                    "\2\2\u01d8\u01da\3\2\2\2\u01d9\u01d7\3\2\2\2\u01da\u01db\b\64\21\2\u01db" +
                    "i\3\2\2\2\u01dc\u01dd\n\r\2\2\u01ddk\3\2\2\2\u01de\u01df\t\r\2\2\u01df" +
                    "m\3\2\2\2\u01e0\u01e3\7^\2\2\u01e1\u01e4\t\13\2\2\u01e2\u01e4\5\64\32" +
                    "\2\u01e3\u01e1\3\2\2\2\u01e3\u01e2\3\2\2\2\u01e4\u01ea\3\2\2\2\u01e5\u01e6" +
                    "\7^\2\2\u01e6\u01ea\5l\66\2\u01e7\u01e8\7\u0080\2\2\u01e8\u01ea\5l\66" +
                    "\2\u01e9\u01e0\3\2\2\2\u01e9\u01e5\3\2\2\2\u01e9\u01e7\3\2\2\2\u01eao" +
                    "\3\2\2\2\u01eb\u01ec\7%\2\2\u01ec\u01ed\7%\2\2\u01ed\u01ee\3\2\2\2\u01ee" +
                    "\u01ef\5$\22\2\u01ef\u01f0\3\2\2\2\u01f0\u01f1\b8\2\2\u01f1q\3\2\2\2\u01f2" +
                    "\u01f3\7\177\2\2\u01f3\u01f4\3\2\2\2\u01f4\u01f5\b9\22\2\u01f5s\3\2\2" +
                    "\2\63\2\3w\u0086\u0092\u00a0\u00b3\u00ba\u00bc\u00bf\u00c6\u00c9\u00cd" +
                    "\u00db\u00e4\u00e6\u00ee\u00f5\u00fa\u00ff\u0101\u0104\u0109\u010c\u0110" +
                    "\u0112\u0116\u0120\u0127\u0129\u013d\u014c\u015a\u016a\u018b\u0192\u0194" +
                    "\u0197\u01b6\u01b8\u01c0\u01c6\u01c8\u01cd\u01d1\u01d3\u01d7\u01e3\u01e9" +
                    "\23\b\2\2\7\3\2\t\4\2\t\5\2\t\6\2\t\7\2\t\b\2\t\t\2\t\13\2\t\f\2\t\r\2" +
                    "\t\16\2\t\17\2\t\24\2\t\21\2\t\22\2\6\2\2";

    public static final ATN _ATN =
            new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    protected static final DFA[] _decisionToDFA;

    protected static final PredictionContextCache _sharedContextCache =
            new PredictionContextCache();

    private static final String[] _LITERAL_NAMES = makeLiteralNames();

    private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();

    public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

    public static String[] channelNames = {
            "DEFAULT_TOKEN_CHANNEL", "HIDDEN"
    };

    public static String[] modeNames = {
            "DEFAULT_MODE", "CONDITIONAL"
    };

    static {
        RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION);
    }

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

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }

    public MODLLexer(CharStream input) {
        super(input);
        _interp = new LexerATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    private static String[] makeRuleNames() {
        return new String[]{
                "WS", "NULL", "TRUE", "FALSE", "COLON", "EQUALS", "STRUCT_SEP", "ARR_SEP",
                "LBRAC", "RBRAC", "LSBRAC", "RSBRAC", "NUMBER", "INT", "EXP", "COMMENT",
                "INSIDE_COMMENT", "QUOTED", "INSIDE_QUOTES", "INSIDE_GRAVES", "STRING",
                "UNRESERVED", "RESERVED_CHARS", "ESCAPED", "UNICODE", "HEX", "HASH_PREFIX",
                "LCBRAC", "CWS", "CNULL", "CTRUE", "CFALSE", "CCOLON", "CEQUALS", "CSTRUCT_SEP",
                "CLBRAC", "CRBRAC", "CLSBRAC", "CRSBRAC", "CNUMBER", "QMARK", "FSLASH",
                "GTHAN", "LTHAN", "ASTERISK", "AMP", "PIPE", "EXCLAM", "CLCBRAC", "CQUOTED",
                "CSTRING", "CUNRESERVED", "CRESERVED_CHARS", "CESCAPED", "CCOMMENT",
                "RCBRAC"
        };
    }

    private static String[] makeLiteralNames() {
        return new String[]{
                null, null, null, null, null, null, null, null, "','", null, null, null,
                null, null, null, null, null, null, "'{'", null, "'?'", "'/'", "'>'",
                "'<'", "'*'", "'&'", "'|'", "'!'", null, "'}'"
        };
    }

    private static String[] makeSymbolicNames() {
        return new String[]{
                null, "WS", "NULL", "TRUE", "FALSE", "COLON", "EQUALS", "STRUCT_SEP",
                "ARR_SEP", "LBRAC", "RBRAC", "LSBRAC", "RSBRAC", "NUMBER", "COMMENT",
                "QUOTED", "STRING", "HASH_PREFIX", "LCBRAC", "CWS", "QMARK", "FSLASH",
                "GTHAN", "LTHAN", "ASTERISK", "AMP", "PIPE", "EXCLAM", "CCOMMENT", "RCBRAC"
        };
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
    public String getGrammarFileName() {
        return "MODLLexer.g4";
    }

    @Override
    public String[] getRuleNames() {
        return ruleNames;
    }

    @Override
    public String getSerializedATN() {
        return _serializedATN;
    }

    @Override
    public String[] getChannelNames() {
        return channelNames;
    }

    @Override
    public String[] getModeNames() {
        return modeNames;
    }

    @Override
    public ATN getATN() {
        return _ATN;
    }

}