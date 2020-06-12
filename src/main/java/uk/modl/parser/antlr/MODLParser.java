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

// Generated from /Users/tonywalmsley/work/num/grammar/antlr4/MODLParser.g4 by ANTLR 4.7.2
package uk.modl.parser.antlr;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MODLParser extends Parser {

    public static final int
            WS = 1, NULL = 2, TRUE = 3, FALSE = 4, COLON = 5, EQUALS = 6, STRUCT_SEP = 7, ARR_SEP = 8,
            LBRAC = 9, RBRAC = 10, LSBRAC = 11, RSBRAC = 12, NUMBER = 13, COMMENT = 14, QUOTED = 15,
            STRING = 16, HASH_PREFIX = 17, LCBRAC = 18, CWS = 19, QMARK = 20, FSLASH = 21, GTHAN = 22,
            LTHAN = 23, ASTERISK = 24, AMP = 25, PIPE = 26, EXCLAM = 27, CCOMMENT = 28, RCBRAC = 29;

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

    public static final String[] ruleNames = makeRuleNames();

    /**
     * @deprecated Use {@link #VOCABULARY} instead.
     */
    @Deprecated
    public static final String[] tokenNames;

    public static final String _serializedATN =
            "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\37\u0147\4\2\t\2" +
                    "\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13" +
                    "\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22" +
                    "\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31" +
                    "\3\2\5\2\64\n\2\3\2\3\2\3\2\7\29\n\2\f\2\16\2<\13\2\3\2\5\2?\n\2\5\2A" +
                    "\n\2\3\2\3\2\3\3\3\3\3\3\3\3\5\3I\n\3\3\4\3\4\3\4\3\4\7\4O\n\4\f\4\16" +
                    "\4R\13\4\5\4T\n\4\3\4\3\4\3\5\3\5\3\5\5\5[\n\5\3\5\6\5^\n\5\r\5\16\5_" +
                    "\3\5\3\5\5\5d\n\5\3\5\7\5g\n\5\f\5\16\5j\13\5\7\5l\n\5\f\5\16\5o\13\5" +
                    "\5\5q\n\5\3\5\3\5\3\6\3\6\6\6w\n\6\r\6\16\6x\6\6{\n\6\r\6\16\6|\3\6\7" +
                    "\6\u0080\n\6\f\6\16\6\u0083\13\6\3\6\5\6\u0086\n\6\3\7\3\7\3\7\3\7\3\7" +
                    "\3\7\3\7\5\7\u008f\n\7\3\b\3\b\5\b\u0093\n\b\3\t\3\t\3\t\3\t\3\t\3\t\5" +
                    "\t\u009b\n\t\3\t\3\t\7\t\u009f\n\t\f\t\16\t\u00a2\13\t\3\t\3\t\3\n\3\n" +
                    "\3\n\7\n\u00a9\n\n\f\n\16\n\u00ac\13\n\3\13\3\13\3\13\3\13\3\13\3\13\5" +
                    "\13\u00b4\n\13\3\13\3\13\7\13\u00b8\n\13\f\13\16\13\u00bb\13\13\3\13\3" +
                    "\13\3\f\6\f\u00c0\n\f\r\f\16\f\u00c1\3\r\3\r\5\r\u00c6\n\r\3\16\3\16\3" +
                    "\16\3\16\3\16\3\16\5\16\u00ce\n\16\3\16\3\16\7\16\u00d2\n\16\f\16\16\16" +
                    "\u00d5\13\16\3\16\3\16\3\17\6\17\u00da\n\17\r\17\16\17\u00db\3\20\3\20" +
                    "\5\20\u00e0\n\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\7\21\u00eb" +
                    "\n\21\f\21\16\21\u00ee\13\21\3\21\3\21\3\21\3\21\5\21\u00f4\n\21\3\21" +
                    "\3\21\3\22\3\22\3\22\7\22\u00fb\n\22\f\22\16\22\u00fe\13\22\3\23\5\23" +
                    "\u0101\n\23\3\23\3\23\5\23\u0105\n\23\3\23\3\23\5\23\u0109\n\23\3\23\3" +
                    "\23\5\23\u010d\n\23\7\23\u010f\n\23\f\23\16\23\u0112\13\23\3\24\3\24\3" +
                    "\24\3\24\3\24\3\24\3\24\3\24\3\24\5\24\u011d\n\24\3\25\5\25\u0120\n\25" +
                    "\3\25\5\25\u0123\n\25\3\25\3\25\3\25\7\25\u0128\n\25\f\25\16\25\u012b" +
                    "\13\25\3\26\3\26\3\26\3\26\7\26\u0131\n\26\f\26\16\26\u0134\13\26\3\26" +
                    "\3\26\3\27\3\27\3\27\3\27\3\27\5\27\u013d\n\27\3\30\3\30\3\30\3\30\5\30" +
                    "\u0143\n\30\3\31\3\31\3\31\2\2\32\2\4\6\b\n\f\16\20\22\24\26\30\32\34" +
                    "\36 \"$&(*,.\60\2\5\3\2\21\22\3\2\33\34\5\2\4\6\17\17\21\22\2\u0167\2" +
                    "@\3\2\2\2\4H\3\2\2\2\6J\3\2\2\2\bW\3\2\2\2\nz\3\2\2\2\f\u008e\3\2\2\2" +
                    "\16\u0092\3\2\2\2\20\u0094\3\2\2\2\22\u00a5\3\2\2\2\24\u00ad\3\2\2\2\26" +
                    "\u00bf\3\2\2\2\30\u00c5\3\2\2\2\32\u00c7\3\2\2\2\34\u00d9\3\2\2\2\36\u00df" +
                    "\3\2\2\2 \u00e1\3\2\2\2\"\u00f7\3\2\2\2$\u0100\3\2\2\2&\u011c\3\2\2\2" +
                    "(\u011f\3\2\2\2*\u012c\3\2\2\2,\u013c\3\2\2\2.\u0142\3\2\2\2\60\u0144" +
                    "\3\2\2\2\62\64\5\4\3\2\63\62\3\2\2\2\63\64\3\2\2\2\64A\3\2\2\2\65:\5\4" +
                    "\3\2\66\67\7\t\2\2\679\5\4\3\28\66\3\2\2\29<\3\2\2\2:8\3\2\2\2:;\3\2\2" +
                    "\2;>\3\2\2\2<:\3\2\2\2=?\7\t\2\2>=\3\2\2\2>?\3\2\2\2?A\3\2\2\2@\63\3\2" +
                    "\2\2@\65\3\2\2\2AB\3\2\2\2BC\7\2\2\3C\3\3\2\2\2DI\5\6\4\2EI\5\b\5\2FI" +
                    "\5\20\t\2GI\5\f\7\2HD\3\2\2\2HE\3\2\2\2HF\3\2\2\2HG\3\2\2\2I\5\3\2\2\2" +
                    "JS\7\13\2\2KP\5\30\r\2LM\7\t\2\2MO\5\30\r\2NL\3\2\2\2OR\3\2\2\2PN\3\2" +
                    "\2\2PQ\3\2\2\2QT\3\2\2\2RP\3\2\2\2SK\3\2\2\2ST\3\2\2\2TU\3\2\2\2UV\7\f" +
                    "\2\2V\7\3\2\2\2Wp\7\r\2\2X[\5\36\20\2Y[\5\n\6\2ZX\3\2\2\2ZY\3\2\2\2[m" +
                    "\3\2\2\2\\^\7\t\2\2]\\\3\2\2\2^_\3\2\2\2_]\3\2\2\2_`\3\2\2\2`c\3\2\2\2" +
                    "ad\5\36\20\2bd\5\n\6\2ca\3\2\2\2cb\3\2\2\2dh\3\2\2\2eg\7\t\2\2fe\3\2\2" +
                    "\2gj\3\2\2\2hf\3\2\2\2hi\3\2\2\2il\3\2\2\2jh\3\2\2\2k]\3\2\2\2lo\3\2\2" +
                    "\2mk\3\2\2\2mn\3\2\2\2nq\3\2\2\2om\3\2\2\2pZ\3\2\2\2pq\3\2\2\2qr\3\2\2" +
                    "\2rs\7\16\2\2s\t\3\2\2\2tv\5\36\20\2uw\7\7\2\2vu\3\2\2\2wx\3\2\2\2xv\3" +
                    "\2\2\2xy\3\2\2\2y{\3\2\2\2zt\3\2\2\2{|\3\2\2\2|z\3\2\2\2|}\3\2\2\2}\u0081" +
                    "\3\2\2\2~\u0080\5\36\20\2\177~\3\2\2\2\u0080\u0083\3\2\2\2\u0081\177\3" +
                    "\2\2\2\u0081\u0082\3\2\2\2\u0082\u0085\3\2\2\2\u0083\u0081\3\2\2\2\u0084" +
                    "\u0086\7\7\2\2\u0085\u0084\3\2\2\2\u0085\u0086\3\2\2\2\u0086\13\3\2\2" +
                    "\2\u0087\u0088\t\2\2\2\u0088\u0089\7\b\2\2\u0089\u008f\5\16\b\2\u008a" +
                    "\u008b\7\22\2\2\u008b\u008f\5\6\4\2\u008c\u008d\7\22\2\2\u008d\u008f\5" +
                    "\b\5\2\u008e\u0087\3\2\2\2\u008e\u008a\3\2\2\2\u008e\u008c\3\2\2\2\u008f" +
                    "\r\3\2\2\2\u0090\u0093\5,\27\2\u0091\u0093\5 \21\2\u0092\u0090\3\2\2\2" +
                    "\u0092\u0091\3\2\2\2\u0093\17\3\2\2\2\u0094\u0095\7\24\2\2\u0095\u0096" +
                    "\5$\23\2\u0096\u0097\7\26\2\2\u0097\u00a0\5\22\n\2\u0098\u009a\7\27\2" +
                    "\2\u0099\u009b\5$\23\2\u009a\u0099\3\2\2\2\u009a\u009b\3\2\2\2\u009b\u009c" +
                    "\3\2\2\2\u009c\u009d\7\26\2\2\u009d\u009f\5\22\n\2\u009e\u0098\3\2\2\2" +
                    "\u009f\u00a2\3\2\2\2\u00a0\u009e\3\2\2\2\u00a0\u00a1\3\2\2\2\u00a1\u00a3" +
                    "\3\2\2\2\u00a2\u00a0\3\2\2\2\u00a3\u00a4\7\37\2\2\u00a4\21\3\2\2\2\u00a5" +
                    "\u00aa\5\4\3\2\u00a6\u00a7\7\t\2\2\u00a7\u00a9\5\4\3\2\u00a8\u00a6\3\2" +
                    "\2\2\u00a9\u00ac\3\2\2\2\u00aa\u00a8\3\2\2\2\u00aa\u00ab\3\2\2\2\u00ab" +
                    "\23\3\2\2\2\u00ac\u00aa\3\2\2\2\u00ad\u00ae\7\24\2\2\u00ae\u00af\5$\23" +
                    "\2\u00af\u00b0\7\26\2\2\u00b0\u00b9\5\26\f\2\u00b1\u00b3\7\27\2\2\u00b2" +
                    "\u00b4\5$\23\2\u00b3\u00b2\3\2\2\2\u00b3\u00b4\3\2\2\2\u00b4\u00b5\3\2" +
                    "\2\2\u00b5\u00b6\7\26\2\2\u00b6\u00b8\5\26\f\2\u00b7\u00b1\3\2\2\2\u00b8" +
                    "\u00bb\3\2\2\2\u00b9\u00b7\3\2\2\2\u00b9\u00ba\3\2\2\2\u00ba\u00bc\3\2" +
                    "\2\2\u00bb\u00b9\3\2\2\2\u00bc\u00bd\7\37\2\2\u00bd\25\3\2\2\2\u00be\u00c0" +
                    "\5\30\r\2\u00bf\u00be\3\2\2\2\u00c0\u00c1\3\2\2\2\u00c1\u00bf\3\2\2\2" +
                    "\u00c1\u00c2\3\2\2\2\u00c2\27\3\2\2\2\u00c3\u00c6\5\f\7\2\u00c4\u00c6" +
                    "\5\24\13\2\u00c5\u00c3\3\2\2\2\u00c5\u00c4\3\2\2\2\u00c6\31\3\2\2\2\u00c7" +
                    "\u00c8\7\24\2\2\u00c8\u00c9\5$\23\2\u00c9\u00ca\7\26\2\2\u00ca\u00d3\5" +
                    "\34\17\2\u00cb\u00cd\7\27\2\2\u00cc\u00ce\5$\23\2\u00cd\u00cc\3\2\2\2" +
                    "\u00cd\u00ce\3\2\2\2\u00ce\u00cf\3\2\2\2\u00cf\u00d0\7\26\2\2\u00d0\u00d2" +
                    "\5\34\17\2\u00d1\u00cb\3\2\2\2\u00d2\u00d5\3\2\2\2\u00d3\u00d1\3\2\2\2" +
                    "\u00d3\u00d4\3\2\2\2\u00d4\u00d6\3\2\2\2\u00d5\u00d3\3\2\2\2\u00d6\u00d7" +
                    "\7\37\2\2\u00d7\33\3\2\2\2\u00d8\u00da\5\36\20\2\u00d9\u00d8\3\2\2\2\u00da" +
                    "\u00db\3\2\2\2\u00db\u00d9\3\2\2\2\u00db\u00dc\3\2\2\2\u00dc\35\3\2\2" +
                    "\2\u00dd\u00e0\5.\30\2\u00de\u00e0\5\32\16\2\u00df\u00dd\3\2\2\2\u00df" +
                    "\u00de\3\2\2\2\u00e0\37\3\2\2\2\u00e1\u00e2\7\24\2\2\u00e2\u00e3\5$\23" +
                    "\2\u00e3\u00f3\7\26\2\2\u00e4\u00ec\5\"\22\2\u00e5\u00e6\7\27\2\2\u00e6" +
                    "\u00e7\5$\23\2\u00e7\u00e8\7\26\2\2\u00e8\u00e9\5\"\22\2\u00e9\u00eb\3" +
                    "\2\2\2\u00ea\u00e5\3\2\2\2\u00eb\u00ee\3\2\2\2\u00ec\u00ea\3\2\2\2\u00ec" +
                    "\u00ed\3\2\2\2\u00ed\u00ef\3\2\2\2\u00ee\u00ec\3\2\2\2\u00ef\u00f0\7\27" +
                    "\2\2\u00f0\u00f1\7\26\2\2\u00f1\u00f2\5\"\22\2\u00f2\u00f4\3\2\2\2\u00f3" +
                    "\u00e4\3\2\2\2\u00f3\u00f4\3\2\2\2\u00f4\u00f5\3\2\2\2\u00f5\u00f6\7\37" +
                    "\2\2\u00f6!\3\2\2\2\u00f7\u00fc\5\16\b\2\u00f8\u00f9\7\7\2\2\u00f9\u00fb" +
                    "\5\16\b\2\u00fa\u00f8\3\2\2\2\u00fb\u00fe\3\2\2\2\u00fc\u00fa\3\2\2\2" +
                    "\u00fc\u00fd\3\2\2\2\u00fd#\3\2\2\2\u00fe\u00fc\3\2\2\2\u00ff\u0101\7" +
                    "\35\2\2\u0100\u00ff\3\2\2\2\u0100\u0101\3\2\2\2\u0101\u0104\3\2\2\2\u0102" +
                    "\u0105\5(\25\2\u0103\u0105\5*\26\2\u0104\u0102\3\2\2\2\u0104\u0103\3\2" +
                    "\2\2\u0105\u0110\3\2\2\2\u0106\u0108\t\3\2\2\u0107\u0109\7\35\2\2\u0108" +
                    "\u0107\3\2\2\2\u0108\u0109\3\2\2\2\u0109\u010c\3\2\2\2\u010a\u010d\5(" +
                    "\25\2\u010b\u010d\5*\26\2\u010c\u010a\3\2\2\2\u010c\u010b\3\2\2\2\u010d" +
                    "\u010f\3\2\2\2\u010e\u0106\3\2\2\2\u010f\u0112\3\2\2\2\u0110\u010e\3\2" +
                    "\2\2\u0110\u0111\3\2\2\2\u0111%\3\2\2\2\u0112\u0110\3\2\2\2\u0113\u011d" +
                    "\7\b\2\2\u0114\u011d\7\30\2\2\u0115\u0116\7\30\2\2\u0116\u011d\7\b\2\2" +
                    "\u0117\u011d\7\31\2\2\u0118\u0119\7\31\2\2\u0119\u011d\7\b\2\2\u011a\u011b" +
                    "\7\35\2\2\u011b\u011d\7\b\2\2\u011c\u0113\3\2\2\2\u011c\u0114\3\2\2\2" +
                    "\u011c\u0115\3\2\2\2\u011c\u0117\3\2\2\2\u011c\u0118\3\2\2\2\u011c\u011a" +
                    "\3\2\2\2\u011d\'\3\2\2\2\u011e\u0120\7\22\2\2\u011f\u011e\3\2\2\2\u011f" +
                    "\u0120\3\2\2\2\u0120\u0122\3\2\2\2\u0121\u0123\5&\24\2\u0122\u0121\3\2" +
                    "\2\2\u0122\u0123\3\2\2\2\u0123\u0124\3\2\2\2\u0124\u0129\5,\27\2\u0125" +
                    "\u0126\7\27\2\2\u0126\u0128\5,\27\2\u0127\u0125\3\2\2\2\u0128\u012b\3" +
                    "\2\2\2\u0129\u0127\3\2\2\2\u0129\u012a\3\2\2\2\u012a)\3\2\2\2\u012b\u0129" +
                    "\3\2\2\2\u012c\u012d\7\24\2\2\u012d\u0132\5$\23\2\u012e\u012f\t\3\2\2" +
                    "\u012f\u0131\5$\23\2\u0130\u012e\3\2\2\2\u0131\u0134\3\2\2\2\u0132\u0130" +
                    "\3\2\2\2\u0132\u0133\3\2\2\2\u0133\u0135\3\2\2\2\u0134\u0132\3\2\2\2\u0135" +
                    "\u0136\7\37\2\2\u0136+\3\2\2\2\u0137\u013d\5\6\4\2\u0138\u013d\5\b\5\2" +
                    "\u0139\u013d\5\n\6\2\u013a\u013d\5\f\7\2\u013b\u013d\5\60\31\2\u013c\u0137" +
                    "\3\2\2\2\u013c\u0138\3\2\2\2\u013c\u0139\3\2\2\2\u013c\u013a\3\2\2\2\u013c" +
                    "\u013b\3\2\2\2\u013d-\3\2\2\2\u013e\u0143\5\6\4\2\u013f\u0143\5\f\7\2" +
                    "\u0140\u0143\5\b\5\2\u0141\u0143\5\60\31\2\u0142\u013e\3\2\2\2\u0142\u013f" +
                    "\3\2\2\2\u0142\u0140\3\2\2\2\u0142\u0141\3\2\2\2\u0143/\3\2\2\2\u0144" +
                    "\u0145\t\4\2\2\u0145\61\3\2\2\2/\63:>@HPSZ_chmpx|\u0081\u0085\u008e\u0092" +
                    "\u009a\u00a0\u00aa\u00b3\u00b9\u00c1\u00c5\u00cd\u00d3\u00db\u00df\u00ec" +
                    "\u00f3\u00fc\u0100\u0104\u0108\u010c\u0110\u011c\u011f\u0122\u0129\u0132" +
                    "\u013c\u0142";

    public static final ATN _ATN =
            new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    protected static final DFA[] _decisionToDFA;

    protected static final PredictionContextCache _sharedContextCache =
            new PredictionContextCache();

    private static final String[] _LITERAL_NAMES = makeLiteralNames();

    private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();

    public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

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

    public MODLParser(TokenStream input) {
        super(input);
        _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    private static String[] makeRuleNames() {
        return new String[]{
                "modl", "modl_structure", "modl_map", "modl_array", "modl_nb_array",
                "modl_pair", "modl_value_item", "modl_top_level_conditional", "modl_top_level_conditional_return",
                "modl_map_conditional", "modl_map_conditional_return", "modl_map_item",
                "modl_array_conditional", "modl_array_conditional_return", "modl_array_item",
                "modl_value_conditional", "modl_value_conditional_return", "modl_condition_test",
                "modl_operator", "modl_condition", "modl_condition_group", "modl_value",
                "modl_array_value_item", "modl_primitive"
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
        return "MODLParser.g4";
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
    public ATN getATN() {
        return _ATN;
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
                switch (getInterpreter().adaptivePredict(_input, 3, _ctx)) {
                    case 1: {
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
                    case 2: {
                        {
                            setState(51);
                            modl_structure();
                            setState(56);
                            _errHandler.sync(this);
                            _alt = getInterpreter().adaptivePredict(_input, 1, _ctx);
                            while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                                if (_alt == 1) {
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
                                _alt = getInterpreter().adaptivePredict(_input, 1, _ctx);
                            }
                        }
                        setState(60);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        if (_la == STRUCT_SEP) {
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
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
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
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
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
                        while (_la == STRUCT_SEP) {
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
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
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
                        switch (getInterpreter().adaptivePredict(_input, 7, _ctx)) {
                            case 1: {
                                setState(86);
                                modl_array_item();
                            }
                            break;
                            case 2: {
                                setState(87);
                                modl_nb_array();
                            }
                            break;
                        }
                        setState(107);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        while (_la == STRUCT_SEP) {
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
                                    } while (_la == STRUCT_SEP);
                                    setState(97);
                                    _errHandler.sync(this);
                                    switch (getInterpreter().adaptivePredict(_input, 9, _ctx)) {
                                        case 1: {
                                            setState(95);
                                            modl_array_item();
                                        }
                                        break;
                                        case 2: {
                                            setState(96);
                                            modl_nb_array();
                                        }
                                        break;
                                    }
                                    setState(102);
                                    _errHandler.sync(this);
                                    _alt = getInterpreter().adaptivePredict(_input, 10, _ctx);
                                    while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                                        if (_alt == 1) {
                                            {
                                                {
                                                    setState(99);
                                                    match(STRUCT_SEP);
                                                }
                                            }
                                        }
                                        setState(104);
                                        _errHandler.sync(this);
                                        _alt = getInterpreter().adaptivePredict(_input, 10, _ctx);
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
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
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
                        case 1: {
                            {
                                setState(114);
                                modl_array_item();
                                setState(116);
                                _errHandler.sync(this);
                                _alt = 1;
                                do {
                                    switch (_alt) {
                                        case 1: {
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
                                    _alt = getInterpreter().adaptivePredict(_input, 13, _ctx);
                                } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
                            }
                        }
                        break;
                        default:
                            throw new NoViableAltException(this);
                    }
                    setState(122);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 14, _ctx);
                } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
                setState(127);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 15, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(124);
                                modl_array_item();
                            }
                        }
                    }
                    setState(129);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 15, _ctx);
                }
                setState(131);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 16, _ctx)) {
                    case 1: {
                        setState(130);
                        match(COLON);
                    }
                    break;
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final Modl_pairContext modl_pair() throws RecognitionException {
        Modl_pairContext _localctx = new Modl_pairContext(_ctx, getState());
        enterRule(_localctx, 10, RULE_modl_pair);
        int _la;
        try {
            setState(140);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 17, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(133);
                    _la = _input.LA(1);
                    if (!(_la == QUOTED || _la == STRING)) {
                        _errHandler.recoverInline(this);
                    } else {
                        if (_input.LA(1) == Token.EOF) matchedEOF = true;
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
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final Modl_value_itemContext modl_value_item() throws RecognitionException {
        Modl_value_itemContext _localctx = new Modl_value_itemContext(_ctx, getState());
        enterRule(_localctx, 12, RULE_modl_value_item);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(144);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 18, _ctx)) {
                    case 1: {
                        setState(142);
                        modl_value();
                    }
                    break;
                    case 2: {
                        setState(143);
                        modl_value_conditional();
                    }
                    break;
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
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
                while (_la == FSLASH) {
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
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final Modl_top_level_conditional_returnContext modl_top_level_conditional_return() throws
                                                                                              RecognitionException {
        Modl_top_level_conditional_returnContext _localctx = new Modl_top_level_conditional_returnContext(_ctx, getState());
        enterRule(_localctx, 16, RULE_modl_top_level_conditional_return);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(163);
                modl_structure();
                setState(168);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == STRUCT_SEP) {
                    {
                        {
                            setState(164);
                            match(STRUCT_SEP);
                            setState(165);
                            modl_structure();
                        }
                    }
                    setState(170);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final Modl_map_conditionalContext modl_map_conditional() throws RecognitionException {
        Modl_map_conditionalContext _localctx = new Modl_map_conditionalContext(_ctx, getState());
        enterRule(_localctx, 18, RULE_modl_map_conditional);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(171);
                match(LCBRAC);
                setState(172);
                modl_condition_test();
                setState(173);
                match(QMARK);
                setState(174);
                modl_map_conditional_return();
                setState(183);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == FSLASH) {
                    {
                        {
                            setState(175);
                            match(FSLASH);
                            setState(177);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                            if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NULL) | (1L << TRUE) | (1L << FALSE) | (1L << EQUALS) | (1L << LBRAC) | (1L << LSBRAC) | (1L << NUMBER) | (1L << QUOTED) | (1L << STRING) | (1L << LCBRAC) | (1L << GTHAN) | (1L << LTHAN) | (1L << EXCLAM))) != 0)) {
                                {
                                    setState(176);
                                    modl_condition_test();
                                }
                            }

                            setState(179);
                            match(QMARK);
                            setState(180);
                            modl_map_conditional_return();
                        }
                    }
                    setState(185);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(186);
                match(RCBRAC);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final Modl_map_conditional_returnContext modl_map_conditional_return() throws RecognitionException {
        Modl_map_conditional_returnContext _localctx = new Modl_map_conditional_returnContext(_ctx, getState());
        enterRule(_localctx, 20, RULE_modl_map_conditional_return);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(189);
                _errHandler.sync(this);
                _la = _input.LA(1);
                do {
                    {
                        {
                            setState(188);
                            modl_map_item();
                        }
                    }
                    setState(191);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                } while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << QUOTED) | (1L << STRING) | (1L << LCBRAC))) != 0));
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final Modl_map_itemContext modl_map_item() throws RecognitionException {
        Modl_map_itemContext _localctx = new Modl_map_itemContext(_ctx, getState());
        enterRule(_localctx, 22, RULE_modl_map_item);
        try {
            setState(195);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case QUOTED:
                case STRING:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(193);
                    modl_pair();
                }
                break;
                case LCBRAC:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(194);
                    modl_map_conditional();
                }
                break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final Modl_array_conditionalContext modl_array_conditional() throws RecognitionException {
        Modl_array_conditionalContext _localctx = new Modl_array_conditionalContext(_ctx, getState());
        enterRule(_localctx, 24, RULE_modl_array_conditional);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(197);
                match(LCBRAC);
                setState(198);
                modl_condition_test();
                setState(199);
                match(QMARK);
                setState(200);
                modl_array_conditional_return();
                setState(209);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == FSLASH) {
                    {
                        {
                            setState(201);
                            match(FSLASH);
                            setState(203);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                            if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NULL) | (1L << TRUE) | (1L << FALSE) | (1L << EQUALS) | (1L << LBRAC) | (1L << LSBRAC) | (1L << NUMBER) | (1L << QUOTED) | (1L << STRING) | (1L << LCBRAC) | (1L << GTHAN) | (1L << LTHAN) | (1L << EXCLAM))) != 0)) {
                                {
                                    setState(202);
                                    modl_condition_test();
                                }
                            }

                            setState(205);
                            match(QMARK);
                            setState(206);
                            modl_array_conditional_return();
                        }
                    }
                    setState(211);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(212);
                match(RCBRAC);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final Modl_array_conditional_returnContext modl_array_conditional_return() throws RecognitionException {
        Modl_array_conditional_returnContext _localctx = new Modl_array_conditional_returnContext(_ctx, getState());
        enterRule(_localctx, 26, RULE_modl_array_conditional_return);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(215);
                _errHandler.sync(this);
                _la = _input.LA(1);
                do {
                    {
                        {
                            setState(214);
                            modl_array_item();
                        }
                    }
                    setState(217);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                } while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NULL) | (1L << TRUE) | (1L << FALSE) | (1L << LBRAC) | (1L << LSBRAC) | (1L << NUMBER) | (1L << QUOTED) | (1L << STRING) | (1L << LCBRAC))) != 0));
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final Modl_array_itemContext modl_array_item() throws RecognitionException {
        Modl_array_itemContext _localctx = new Modl_array_itemContext(_ctx, getState());
        enterRule(_localctx, 28, RULE_modl_array_item);
        try {
            setState(221);
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
                    setState(219);
                    modl_array_value_item();
                }
                break;
                case LCBRAC:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(220);
                    modl_array_conditional();
                }
                break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final Modl_value_conditionalContext modl_value_conditional() throws RecognitionException {
        Modl_value_conditionalContext _localctx = new Modl_value_conditionalContext(_ctx, getState());
        enterRule(_localctx, 30, RULE_modl_value_conditional);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(223);
                match(LCBRAC);
                setState(224);
                modl_condition_test();
                setState(225);
                match(QMARK);
                setState(241);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NULL) | (1L << TRUE) | (1L << FALSE) | (1L << LBRAC) | (1L << LSBRAC) | (1L << NUMBER) | (1L << QUOTED) | (1L << STRING) | (1L << LCBRAC))) != 0)) {
                    {
                        setState(226);
                        modl_value_conditional_return();
                        setState(234);
                        _errHandler.sync(this);
                        _alt = getInterpreter().adaptivePredict(_input, 30, _ctx);
                        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                            if (_alt == 1) {
                                {
                                    {
                                        setState(227);
                                        match(FSLASH);
                                        setState(228);
                                        modl_condition_test();
                                        setState(229);
                                        match(QMARK);
                                        setState(230);
                                        modl_value_conditional_return();
                                    }
                                }
                            }
                            setState(236);
                            _errHandler.sync(this);
                            _alt = getInterpreter().adaptivePredict(_input, 30, _ctx);
                        }
                        {
                            setState(237);
                            match(FSLASH);
                            setState(238);
                            match(QMARK);
                            setState(239);
                            modl_value_conditional_return();
                        }
                    }
                }

                setState(243);
                match(RCBRAC);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final Modl_value_conditional_returnContext modl_value_conditional_return() throws RecognitionException {
        Modl_value_conditional_returnContext _localctx = new Modl_value_conditional_returnContext(_ctx, getState());
        enterRule(_localctx, 32, RULE_modl_value_conditional_return);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(245);
                modl_value_item();
                setState(250);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == COLON) {
                    {
                        {
                            setState(246);
                            match(COLON);
                            setState(247);
                            modl_value_item();
                        }
                    }
                    setState(252);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final Modl_condition_testContext modl_condition_test() throws RecognitionException {
        Modl_condition_testContext _localctx = new Modl_condition_testContext(_ctx, getState());
        enterRule(_localctx, 34, RULE_modl_condition_test);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(254);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 33, _ctx)) {
                    case 1: {
                        setState(253);
                        match(EXCLAM);
                    }
                    break;
                }
                setState(258);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 34, _ctx)) {
                    case 1: {
                        setState(256);
                        modl_condition();
                    }
                    break;
                    case 2: {
                        setState(257);
                        modl_condition_group();
                    }
                    break;
                }
                setState(270);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 37, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(260);
                                _la = _input.LA(1);
                                if (!(_la == AMP || _la == PIPE)) {
                                    _errHandler.recoverInline(this);
                                } else {
                                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                                    _errHandler.reportMatch(this);
                                    consume();
                                }
                                setState(262);
                                _errHandler.sync(this);
                                switch (getInterpreter().adaptivePredict(_input, 35, _ctx)) {
                                    case 1: {
                                        setState(261);
                                        match(EXCLAM);
                                    }
                                    break;
                                }
                                setState(266);
                                _errHandler.sync(this);
                                switch (getInterpreter().adaptivePredict(_input, 36, _ctx)) {
                                    case 1: {
                                        setState(264);
                                        modl_condition();
                                    }
                                    break;
                                    case 2: {
                                        setState(265);
                                        modl_condition_group();
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    setState(272);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 37, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final Modl_operatorContext modl_operator() throws RecognitionException {
        Modl_operatorContext _localctx = new Modl_operatorContext(_ctx, getState());
        enterRule(_localctx, 36, RULE_modl_operator);
        try {
            setState(282);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 38, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(273);
                    match(EQUALS);
                }
                break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(274);
                    match(GTHAN);
                }
                break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(275);
                    match(GTHAN);
                    setState(276);
                    match(EQUALS);
                }
                break;
                case 4:
                    enterOuterAlt(_localctx, 4);
                {
                    setState(277);
                    match(LTHAN);
                }
                break;
                case 5:
                    enterOuterAlt(_localctx, 5);
                {
                    setState(278);
                    match(LTHAN);
                    setState(279);
                    match(EQUALS);
                }
                break;
                case 6:
                    enterOuterAlt(_localctx, 6);
                {
                    setState(280);
                    match(EXCLAM);
                    setState(281);
                    match(EQUALS);
                }
                break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final Modl_conditionContext modl_condition() throws RecognitionException {
        Modl_conditionContext _localctx = new Modl_conditionContext(_ctx, getState());
        enterRule(_localctx, 38, RULE_modl_condition);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(285);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 39, _ctx)) {
                    case 1: {
                        setState(284);
                        match(STRING);
                    }
                    break;
                }
                setState(288);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << EQUALS) | (1L << GTHAN) | (1L << LTHAN) | (1L << EXCLAM))) != 0)) {
                    {
                        setState(287);
                        modl_operator();
                    }
                }

                setState(290);
                modl_value();
                setState(295);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == FSLASH) {
                    {
                        {
                            setState(291);
                            match(FSLASH);
                            setState(292);
                            modl_value();
                        }
                    }
                    setState(297);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final Modl_condition_groupContext modl_condition_group() throws RecognitionException {
        Modl_condition_groupContext _localctx = new Modl_condition_groupContext(_ctx, getState());
        enterRule(_localctx, 40, RULE_modl_condition_group);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(298);
                match(LCBRAC);
                setState(299);
                modl_condition_test();
                setState(304);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == AMP || _la == PIPE) {
                    {
                        {
                            setState(300);
                            _la = _input.LA(1);
                            if (!(_la == AMP || _la == PIPE)) {
                                _errHandler.recoverInline(this);
                            } else {
                                if (_input.LA(1) == Token.EOF) matchedEOF = true;
                                _errHandler.reportMatch(this);
                                consume();
                            }
                            setState(301);
                            modl_condition_test();
                        }
                    }
                    setState(306);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(307);
                match(RCBRAC);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final Modl_valueContext modl_value() throws RecognitionException {
        Modl_valueContext _localctx = new Modl_valueContext(_ctx, getState());
        enterRule(_localctx, 42, RULE_modl_value);
        try {
            setState(314);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 43, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(309);
                    modl_map();
                }
                break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(310);
                    modl_array();
                }
                break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(311);
                    modl_nb_array();
                }
                break;
                case 4:
                    enterOuterAlt(_localctx, 4);
                {
                    setState(312);
                    modl_pair();
                }
                break;
                case 5:
                    enterOuterAlt(_localctx, 5);
                {
                    setState(313);
                    modl_primitive();
                }
                break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final Modl_array_value_itemContext modl_array_value_item() throws RecognitionException {
        Modl_array_value_itemContext _localctx = new Modl_array_value_itemContext(_ctx, getState());
        enterRule(_localctx, 44, RULE_modl_array_value_item);
        try {
            setState(320);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 44, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(316);
                    modl_map();
                }
                break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(317);
                    modl_pair();
                }
                break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(318);
                    modl_array();
                }
                break;
                case 4:
                    enterOuterAlt(_localctx, 4);
                {
                    setState(319);
                    modl_primitive();
                }
                break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final Modl_primitiveContext modl_primitive() throws RecognitionException {
        Modl_primitiveContext _localctx = new Modl_primitiveContext(_ctx, getState());
        enterRule(_localctx, 46, RULE_modl_primitive);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(322);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NULL) | (1L << TRUE) | (1L << FALSE) | (1L << NUMBER) | (1L << QUOTED) | (1L << STRING))) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ModlContext extends ParserRuleContext {

        public ModlContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode EOF() {
            return getToken(MODLParser.EOF, 0);
        }

        public List<Modl_structureContext> modl_structure() {
            return getRuleContexts(Modl_structureContext.class);
        }

        public Modl_structureContext modl_structure(int i) {
            return getRuleContext(Modl_structureContext.class, i);
        }

        public List<TerminalNode> STRUCT_SEP() {
            return getTokens(MODLParser.STRUCT_SEP);
        }

        public TerminalNode STRUCT_SEP(int i) {
            return getToken(MODLParser.STRUCT_SEP, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_modl;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).enterModl(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).exitModl(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MODLParserVisitor) return ((MODLParserVisitor<? extends T>) visitor).visitModl(this);
            else return visitor.visitChildren(this);
        }

    }

    public static class Modl_structureContext extends ParserRuleContext {

        public Modl_structureContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public Modl_mapContext modl_map() {
            return getRuleContext(Modl_mapContext.class, 0);
        }

        public Modl_arrayContext modl_array() {
            return getRuleContext(Modl_arrayContext.class, 0);
        }

        public Modl_top_level_conditionalContext modl_top_level_conditional() {
            return getRuleContext(Modl_top_level_conditionalContext.class, 0);
        }

        public Modl_pairContext modl_pair() {
            return getRuleContext(Modl_pairContext.class, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_modl_structure;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).enterModl_structure(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).exitModl_structure(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MODLParserVisitor)
                return ((MODLParserVisitor<? extends T>) visitor).visitModl_structure(this);
            else return visitor.visitChildren(this);
        }

    }

    public static class Modl_mapContext extends ParserRuleContext {

        public Modl_mapContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode LBRAC() {
            return getToken(MODLParser.LBRAC, 0);
        }

        public TerminalNode RBRAC() {
            return getToken(MODLParser.RBRAC, 0);
        }

        public List<Modl_map_itemContext> modl_map_item() {
            return getRuleContexts(Modl_map_itemContext.class);
        }

        public Modl_map_itemContext modl_map_item(int i) {
            return getRuleContext(Modl_map_itemContext.class, i);
        }

        public List<TerminalNode> STRUCT_SEP() {
            return getTokens(MODLParser.STRUCT_SEP);
        }

        public TerminalNode STRUCT_SEP(int i) {
            return getToken(MODLParser.STRUCT_SEP, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_modl_map;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).enterModl_map(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).exitModl_map(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MODLParserVisitor)
                return ((MODLParserVisitor<? extends T>) visitor).visitModl_map(this);
            else return visitor.visitChildren(this);
        }

    }

    public static class Modl_arrayContext extends ParserRuleContext {

        public Modl_arrayContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode LSBRAC() {
            return getToken(MODLParser.LSBRAC, 0);
        }

        public TerminalNode RSBRAC() {
            return getToken(MODLParser.RSBRAC, 0);
        }

        public List<Modl_array_itemContext> modl_array_item() {
            return getRuleContexts(Modl_array_itemContext.class);
        }

        public Modl_array_itemContext modl_array_item(int i) {
            return getRuleContext(Modl_array_itemContext.class, i);
        }

        public List<Modl_nb_arrayContext> modl_nb_array() {
            return getRuleContexts(Modl_nb_arrayContext.class);
        }

        public Modl_nb_arrayContext modl_nb_array(int i) {
            return getRuleContext(Modl_nb_arrayContext.class, i);
        }

        public List<TerminalNode> STRUCT_SEP() {
            return getTokens(MODLParser.STRUCT_SEP);
        }

        public TerminalNode STRUCT_SEP(int i) {
            return getToken(MODLParser.STRUCT_SEP, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_modl_array;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).enterModl_array(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).exitModl_array(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MODLParserVisitor)
                return ((MODLParserVisitor<? extends T>) visitor).visitModl_array(this);
            else return visitor.visitChildren(this);
        }

    }

    public static class Modl_nb_arrayContext extends ParserRuleContext {

        public Modl_nb_arrayContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public List<Modl_array_itemContext> modl_array_item() {
            return getRuleContexts(Modl_array_itemContext.class);
        }

        public Modl_array_itemContext modl_array_item(int i) {
            return getRuleContext(Modl_array_itemContext.class, i);
        }

        public List<TerminalNode> COLON() {
            return getTokens(MODLParser.COLON);
        }

        public TerminalNode COLON(int i) {
            return getToken(MODLParser.COLON, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_modl_nb_array;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).enterModl_nb_array(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).exitModl_nb_array(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MODLParserVisitor)
                return ((MODLParserVisitor<? extends T>) visitor).visitModl_nb_array(this);
            else return visitor.visitChildren(this);
        }

    }

    public static class Modl_pairContext extends ParserRuleContext {

        public Modl_pairContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode EQUALS() {
            return getToken(MODLParser.EQUALS, 0);
        }

        public Modl_value_itemContext modl_value_item() {
            return getRuleContext(Modl_value_itemContext.class, 0);
        }

        public TerminalNode STRING() {
            return getToken(MODLParser.STRING, 0);
        }

        public TerminalNode QUOTED() {
            return getToken(MODLParser.QUOTED, 0);
        }

        public Modl_mapContext modl_map() {
            return getRuleContext(Modl_mapContext.class, 0);
        }

        public Modl_arrayContext modl_array() {
            return getRuleContext(Modl_arrayContext.class, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_modl_pair;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).enterModl_pair(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).exitModl_pair(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MODLParserVisitor)
                return ((MODLParserVisitor<? extends T>) visitor).visitModl_pair(this);
            else return visitor.visitChildren(this);
        }

    }

    public static class Modl_value_itemContext extends ParserRuleContext {

        public Modl_value_itemContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public Modl_valueContext modl_value() {
            return getRuleContext(Modl_valueContext.class, 0);
        }

        public Modl_value_conditionalContext modl_value_conditional() {
            return getRuleContext(Modl_value_conditionalContext.class, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_modl_value_item;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).enterModl_value_item(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).exitModl_value_item(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MODLParserVisitor)
                return ((MODLParserVisitor<? extends T>) visitor).visitModl_value_item(this);
            else return visitor.visitChildren(this);
        }

    }

    public static class Modl_top_level_conditionalContext extends ParserRuleContext {

        public Modl_top_level_conditionalContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode LCBRAC() {
            return getToken(MODLParser.LCBRAC, 0);
        }

        public List<Modl_condition_testContext> modl_condition_test() {
            return getRuleContexts(Modl_condition_testContext.class);
        }

        public Modl_condition_testContext modl_condition_test(int i) {
            return getRuleContext(Modl_condition_testContext.class, i);
        }

        public List<TerminalNode> QMARK() {
            return getTokens(MODLParser.QMARK);
        }

        public TerminalNode QMARK(int i) {
            return getToken(MODLParser.QMARK, i);
        }

        public List<Modl_top_level_conditional_returnContext> modl_top_level_conditional_return() {
            return getRuleContexts(Modl_top_level_conditional_returnContext.class);
        }

        public Modl_top_level_conditional_returnContext modl_top_level_conditional_return(int i) {
            return getRuleContext(Modl_top_level_conditional_returnContext.class, i);
        }

        public TerminalNode RCBRAC() {
            return getToken(MODLParser.RCBRAC, 0);
        }

        public List<TerminalNode> FSLASH() {
            return getTokens(MODLParser.FSLASH);
        }

        public TerminalNode FSLASH(int i) {
            return getToken(MODLParser.FSLASH, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_modl_top_level_conditional;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener)
                ((MODLParserListener) listener).enterModl_top_level_conditional(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener)
                ((MODLParserListener) listener).exitModl_top_level_conditional(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MODLParserVisitor)
                return ((MODLParserVisitor<? extends T>) visitor).visitModl_top_level_conditional(this);
            else return visitor.visitChildren(this);
        }

    }

    public static class Modl_map_conditionalContext extends ParserRuleContext {

        public Modl_map_conditionalContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode LCBRAC() {
            return getToken(MODLParser.LCBRAC, 0);
        }

        public List<Modl_condition_testContext> modl_condition_test() {
            return getRuleContexts(Modl_condition_testContext.class);
        }

        public Modl_condition_testContext modl_condition_test(int i) {
            return getRuleContext(Modl_condition_testContext.class, i);
        }

        public List<TerminalNode> QMARK() {
            return getTokens(MODLParser.QMARK);
        }

        public TerminalNode QMARK(int i) {
            return getToken(MODLParser.QMARK, i);
        }

        public List<Modl_map_conditional_returnContext> modl_map_conditional_return() {
            return getRuleContexts(Modl_map_conditional_returnContext.class);
        }

        public Modl_map_conditional_returnContext modl_map_conditional_return(int i) {
            return getRuleContext(Modl_map_conditional_returnContext.class, i);
        }

        public TerminalNode RCBRAC() {
            return getToken(MODLParser.RCBRAC, 0);
        }

        public List<TerminalNode> FSLASH() {
            return getTokens(MODLParser.FSLASH);
        }

        public TerminalNode FSLASH(int i) {
            return getToken(MODLParser.FSLASH, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_modl_map_conditional;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).enterModl_map_conditional(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).exitModl_map_conditional(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MODLParserVisitor)
                return ((MODLParserVisitor<? extends T>) visitor).visitModl_map_conditional(this);
            else return visitor.visitChildren(this);
        }

    }

    public static class Modl_map_conditional_returnContext extends ParserRuleContext {

        public Modl_map_conditional_returnContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public List<Modl_map_itemContext> modl_map_item() {
            return getRuleContexts(Modl_map_itemContext.class);
        }

        public Modl_map_itemContext modl_map_item(int i) {
            return getRuleContext(Modl_map_itemContext.class, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_modl_map_conditional_return;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener)
                ((MODLParserListener) listener).enterModl_map_conditional_return(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener)
                ((MODLParserListener) listener).exitModl_map_conditional_return(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MODLParserVisitor)
                return ((MODLParserVisitor<? extends T>) visitor).visitModl_map_conditional_return(this);
            else return visitor.visitChildren(this);
        }

    }

    public static class Modl_map_itemContext extends ParserRuleContext {

        public Modl_map_itemContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public Modl_pairContext modl_pair() {
            return getRuleContext(Modl_pairContext.class, 0);
        }

        public Modl_map_conditionalContext modl_map_conditional() {
            return getRuleContext(Modl_map_conditionalContext.class, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_modl_map_item;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).enterModl_map_item(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).exitModl_map_item(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MODLParserVisitor)
                return ((MODLParserVisitor<? extends T>) visitor).visitModl_map_item(this);
            else return visitor.visitChildren(this);
        }

    }

    public static class Modl_array_conditionalContext extends ParserRuleContext {

        public Modl_array_conditionalContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode LCBRAC() {
            return getToken(MODLParser.LCBRAC, 0);
        }

        public List<Modl_condition_testContext> modl_condition_test() {
            return getRuleContexts(Modl_condition_testContext.class);
        }

        public Modl_condition_testContext modl_condition_test(int i) {
            return getRuleContext(Modl_condition_testContext.class, i);
        }

        public List<TerminalNode> QMARK() {
            return getTokens(MODLParser.QMARK);
        }

        public TerminalNode QMARK(int i) {
            return getToken(MODLParser.QMARK, i);
        }

        public List<Modl_array_conditional_returnContext> modl_array_conditional_return() {
            return getRuleContexts(Modl_array_conditional_returnContext.class);
        }

        public Modl_array_conditional_returnContext modl_array_conditional_return(int i) {
            return getRuleContext(Modl_array_conditional_returnContext.class, i);
        }

        public TerminalNode RCBRAC() {
            return getToken(MODLParser.RCBRAC, 0);
        }

        public List<TerminalNode> FSLASH() {
            return getTokens(MODLParser.FSLASH);
        }

        public TerminalNode FSLASH(int i) {
            return getToken(MODLParser.FSLASH, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_modl_array_conditional;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener)
                ((MODLParserListener) listener).enterModl_array_conditional(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener)
                ((MODLParserListener) listener).exitModl_array_conditional(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MODLParserVisitor)
                return ((MODLParserVisitor<? extends T>) visitor).visitModl_array_conditional(this);
            else return visitor.visitChildren(this);
        }

    }

    public static class Modl_array_conditional_returnContext extends ParserRuleContext {

        public Modl_array_conditional_returnContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public List<Modl_array_itemContext> modl_array_item() {
            return getRuleContexts(Modl_array_itemContext.class);
        }

        public Modl_array_itemContext modl_array_item(int i) {
            return getRuleContext(Modl_array_itemContext.class, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_modl_array_conditional_return;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener)
                ((MODLParserListener) listener).enterModl_array_conditional_return(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener)
                ((MODLParserListener) listener).exitModl_array_conditional_return(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MODLParserVisitor)
                return ((MODLParserVisitor<? extends T>) visitor).visitModl_array_conditional_return(this);
            else return visitor.visitChildren(this);
        }

    }

    public static class Modl_array_itemContext extends ParserRuleContext {

        public Modl_array_itemContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public Modl_array_value_itemContext modl_array_value_item() {
            return getRuleContext(Modl_array_value_itemContext.class, 0);
        }

        public Modl_array_conditionalContext modl_array_conditional() {
            return getRuleContext(Modl_array_conditionalContext.class, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_modl_array_item;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).enterModl_array_item(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).exitModl_array_item(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MODLParserVisitor)
                return ((MODLParserVisitor<? extends T>) visitor).visitModl_array_item(this);
            else return visitor.visitChildren(this);
        }

    }

    public static class Modl_value_conditionalContext extends ParserRuleContext {

        public Modl_value_conditionalContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode LCBRAC() {
            return getToken(MODLParser.LCBRAC, 0);
        }

        public List<Modl_condition_testContext> modl_condition_test() {
            return getRuleContexts(Modl_condition_testContext.class);
        }

        public Modl_condition_testContext modl_condition_test(int i) {
            return getRuleContext(Modl_condition_testContext.class, i);
        }

        public List<TerminalNode> QMARK() {
            return getTokens(MODLParser.QMARK);
        }

        public TerminalNode QMARK(int i) {
            return getToken(MODLParser.QMARK, i);
        }

        public TerminalNode RCBRAC() {
            return getToken(MODLParser.RCBRAC, 0);
        }

        public List<Modl_value_conditional_returnContext> modl_value_conditional_return() {
            return getRuleContexts(Modl_value_conditional_returnContext.class);
        }

        public Modl_value_conditional_returnContext modl_value_conditional_return(int i) {
            return getRuleContext(Modl_value_conditional_returnContext.class, i);
        }

        public List<TerminalNode> FSLASH() {
            return getTokens(MODLParser.FSLASH);
        }

        public TerminalNode FSLASH(int i) {
            return getToken(MODLParser.FSLASH, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_modl_value_conditional;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener)
                ((MODLParserListener) listener).enterModl_value_conditional(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener)
                ((MODLParserListener) listener).exitModl_value_conditional(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MODLParserVisitor)
                return ((MODLParserVisitor<? extends T>) visitor).visitModl_value_conditional(this);
            else return visitor.visitChildren(this);
        }

    }

    public static class Modl_value_conditional_returnContext extends ParserRuleContext {

        public Modl_value_conditional_returnContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public List<Modl_value_itemContext> modl_value_item() {
            return getRuleContexts(Modl_value_itemContext.class);
        }

        public Modl_value_itemContext modl_value_item(int i) {
            return getRuleContext(Modl_value_itemContext.class, i);
        }

        public List<TerminalNode> COLON() {
            return getTokens(MODLParser.COLON);
        }

        public TerminalNode COLON(int i) {
            return getToken(MODLParser.COLON, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_modl_value_conditional_return;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener)
                ((MODLParserListener) listener).enterModl_value_conditional_return(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener)
                ((MODLParserListener) listener).exitModl_value_conditional_return(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MODLParserVisitor)
                return ((MODLParserVisitor<? extends T>) visitor).visitModl_value_conditional_return(this);
            else return visitor.visitChildren(this);
        }

    }

    public static class Modl_condition_testContext extends ParserRuleContext {

        public Modl_condition_testContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public List<Modl_conditionContext> modl_condition() {
            return getRuleContexts(Modl_conditionContext.class);
        }

        public Modl_conditionContext modl_condition(int i) {
            return getRuleContext(Modl_conditionContext.class, i);
        }

        public List<Modl_condition_groupContext> modl_condition_group() {
            return getRuleContexts(Modl_condition_groupContext.class);
        }

        public Modl_condition_groupContext modl_condition_group(int i) {
            return getRuleContext(Modl_condition_groupContext.class, i);
        }

        public List<TerminalNode> EXCLAM() {
            return getTokens(MODLParser.EXCLAM);
        }

        public TerminalNode EXCLAM(int i) {
            return getToken(MODLParser.EXCLAM, i);
        }

        public List<TerminalNode> AMP() {
            return getTokens(MODLParser.AMP);
        }

        public TerminalNode AMP(int i) {
            return getToken(MODLParser.AMP, i);
        }

        public List<TerminalNode> PIPE() {
            return getTokens(MODLParser.PIPE);
        }

        public TerminalNode PIPE(int i) {
            return getToken(MODLParser.PIPE, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_modl_condition_test;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).enterModl_condition_test(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).exitModl_condition_test(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MODLParserVisitor)
                return ((MODLParserVisitor<? extends T>) visitor).visitModl_condition_test(this);
            else return visitor.visitChildren(this);
        }

    }

    public static class Modl_operatorContext extends ParserRuleContext {

        public Modl_operatorContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode EQUALS() {
            return getToken(MODLParser.EQUALS, 0);
        }

        public TerminalNode GTHAN() {
            return getToken(MODLParser.GTHAN, 0);
        }

        public TerminalNode LTHAN() {
            return getToken(MODLParser.LTHAN, 0);
        }

        public TerminalNode EXCLAM() {
            return getToken(MODLParser.EXCLAM, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_modl_operator;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).enterModl_operator(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).exitModl_operator(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MODLParserVisitor)
                return ((MODLParserVisitor<? extends T>) visitor).visitModl_operator(this);
            else return visitor.visitChildren(this);
        }

    }

    public static class Modl_conditionContext extends ParserRuleContext {

        public Modl_conditionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public List<Modl_valueContext> modl_value() {
            return getRuleContexts(Modl_valueContext.class);
        }

        public Modl_valueContext modl_value(int i) {
            return getRuleContext(Modl_valueContext.class, i);
        }

        public TerminalNode STRING() {
            return getToken(MODLParser.STRING, 0);
        }

        public Modl_operatorContext modl_operator() {
            return getRuleContext(Modl_operatorContext.class, 0);
        }

        public List<TerminalNode> FSLASH() {
            return getTokens(MODLParser.FSLASH);
        }

        public TerminalNode FSLASH(int i) {
            return getToken(MODLParser.FSLASH, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_modl_condition;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).enterModl_condition(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).exitModl_condition(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MODLParserVisitor)
                return ((MODLParserVisitor<? extends T>) visitor).visitModl_condition(this);
            else return visitor.visitChildren(this);
        }

    }

    public static class Modl_condition_groupContext extends ParserRuleContext {

        public Modl_condition_groupContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode LCBRAC() {
            return getToken(MODLParser.LCBRAC, 0);
        }

        public List<Modl_condition_testContext> modl_condition_test() {
            return getRuleContexts(Modl_condition_testContext.class);
        }

        public Modl_condition_testContext modl_condition_test(int i) {
            return getRuleContext(Modl_condition_testContext.class, i);
        }

        public TerminalNode RCBRAC() {
            return getToken(MODLParser.RCBRAC, 0);
        }

        public List<TerminalNode> AMP() {
            return getTokens(MODLParser.AMP);
        }

        public TerminalNode AMP(int i) {
            return getToken(MODLParser.AMP, i);
        }

        public List<TerminalNode> PIPE() {
            return getTokens(MODLParser.PIPE);
        }

        public TerminalNode PIPE(int i) {
            return getToken(MODLParser.PIPE, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_modl_condition_group;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).enterModl_condition_group(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).exitModl_condition_group(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MODLParserVisitor)
                return ((MODLParserVisitor<? extends T>) visitor).visitModl_condition_group(this);
            else return visitor.visitChildren(this);
        }

    }

    public static class Modl_valueContext extends ParserRuleContext {

        public Modl_valueContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public Modl_mapContext modl_map() {
            return getRuleContext(Modl_mapContext.class, 0);
        }

        public Modl_arrayContext modl_array() {
            return getRuleContext(Modl_arrayContext.class, 0);
        }

        public Modl_nb_arrayContext modl_nb_array() {
            return getRuleContext(Modl_nb_arrayContext.class, 0);
        }

        public Modl_pairContext modl_pair() {
            return getRuleContext(Modl_pairContext.class, 0);
        }

        public Modl_primitiveContext modl_primitive() {
            return getRuleContext(Modl_primitiveContext.class, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_modl_value;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).enterModl_value(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).exitModl_value(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MODLParserVisitor)
                return ((MODLParserVisitor<? extends T>) visitor).visitModl_value(this);
            else return visitor.visitChildren(this);
        }

    }

    public static class Modl_array_value_itemContext extends ParserRuleContext {

        public Modl_array_value_itemContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public Modl_mapContext modl_map() {
            return getRuleContext(Modl_mapContext.class, 0);
        }

        public Modl_pairContext modl_pair() {
            return getRuleContext(Modl_pairContext.class, 0);
        }

        public Modl_arrayContext modl_array() {
            return getRuleContext(Modl_arrayContext.class, 0);
        }

        public Modl_primitiveContext modl_primitive() {
            return getRuleContext(Modl_primitiveContext.class, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_modl_array_value_item;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener)
                ((MODLParserListener) listener).enterModl_array_value_item(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).exitModl_array_value_item(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MODLParserVisitor)
                return ((MODLParserVisitor<? extends T>) visitor).visitModl_array_value_item(this);
            else return visitor.visitChildren(this);
        }

    }

    public static class Modl_primitiveContext extends ParserRuleContext {

        public Modl_primitiveContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode QUOTED() {
            return getToken(MODLParser.QUOTED, 0);
        }

        public TerminalNode NUMBER() {
            return getToken(MODLParser.NUMBER, 0);
        }

        public TerminalNode STRING() {
            return getToken(MODLParser.STRING, 0);
        }

        public TerminalNode TRUE() {
            return getToken(MODLParser.TRUE, 0);
        }

        public TerminalNode FALSE() {
            return getToken(MODLParser.FALSE, 0);
        }

        public TerminalNode NULL() {
            return getToken(MODLParser.NULL, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_modl_primitive;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).enterModl_primitive(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener) ((MODLParserListener) listener).exitModl_primitive(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MODLParserVisitor)
                return ((MODLParserVisitor<? extends T>) visitor).visitModl_primitive(this);
            else return visitor.visitChildren(this);
        }

    }

    public static class Modl_top_level_conditional_returnContext extends ParserRuleContext {

        public Modl_top_level_conditional_returnContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public List<Modl_structureContext> modl_structure() {
            return getRuleContexts(Modl_structureContext.class);
        }

        public Modl_structureContext modl_structure(int i) {
            return getRuleContext(Modl_structureContext.class, i);
        }

        public List<TerminalNode> STRUCT_SEP() {
            return getTokens(MODLParser.STRUCT_SEP);
        }

        public TerminalNode STRUCT_SEP(int i) {
            return getToken(MODLParser.STRUCT_SEP, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_modl_top_level_conditional_return;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener)
                ((MODLParserListener) listener).enterModl_top_level_conditional_return(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MODLParserListener)
                ((MODLParserListener) listener).exitModl_top_level_conditional_return(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MODLParserVisitor)
                return ((MODLParserVisitor<? extends T>) visitor).visitModl_top_level_conditional_return(this);
            else return visitor.visitChildren(this);
        }

    }

}