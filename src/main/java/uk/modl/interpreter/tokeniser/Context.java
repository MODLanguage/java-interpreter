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

package uk.modl.interpreter.tokeniser;

import java.util.LinkedList;
import java.util.regex.Pattern;

import lombok.NonNull;

public class Context {
    private static final String WS = " \t\r\n";
    private static final String nonStringTokens = "[]();\"=`";
    private static final Pattern INTEGER_REGEX = Pattern.compile("^-?(?:0|[1-9]\\d*)$");
    private static final Pattern FLOAT_REGEX = Pattern.compile("^-?(?:0|[1-9]\\d*)(?:\\.\\d+)?(?:[eE][+-]?\\d+)?$");
    private final String s;
    private final LinkedList<Token> tokens = new LinkedList<>();
    private int tokStart = 0;

    Context(@NonNull final String s) {
        this.s = s;
    }

    /**
     * Find the end position of a quoted string.
     *
     * @param s         the MODL string
     * @param start     the start position
     * @param quoteChar the quote character for this quoted string
     * @return the end position of the next token in the string.
     */
    private static int scanToEndOfQuoted(@NonNull final String s, int start, char quoteChar) {
        int end = start + 1;

        while (end < s.length()) {
            final char endChar = s.charAt(end);
            final char prevChar = s.charAt(end - 1);

            // Is it an end quote without an escape char?
            if (endChar == quoteChar) {
                if (prevChar != '\\' && prevChar != '~') {
                    break;
                }
            }
            end++;
        }
        if (s.charAt(end) != quoteChar) {
            throw new TokeniserException(
                    String.format("Unclosed quote: %c in %s near %d:%d", quoteChar, s, start, end));
        }
        return end + 1;
    }

    /**
     * Find the end position of a string.
     *
     * @param s     the MODL string
     * @param start the start position
     * @return the end position of the next token in the string
     */
    private static int scanToEndOfString(@NonNull final String s, int start) {
        int end = start + 1;
        while (end < s.length()) {
            if (nonStringTokens.indexOf(s.charAt(end)) > -1 && !Context.escaped(s, end - 1)) {
                break;
            }
            end++;
        }
        return end;
    }

    /**
     * Check whether there is an escape character at the index position.
     *
     * @param s     the string
     * @param index the index
     */
    private static boolean escaped(@NonNull final String s, int index) {
        return index >= 0 && (s.charAt(index) == '~' || s.charAt(index) == '\\');
    }

    /**
     * The token parsing loop.
     *
     * @return a Token[]
     */
    LinkedList<Token> parse() {
        boolean more;
        do {
            more = this.next();
        } while (more);
        return this.tokens;
    }

    /**
     * Parse the next token and add it to the Token array.
     *
     * @return true if there are more possible tokens to be found.
     */
    private boolean next() {
        // Skip white spaces.
        while (this.tokStart < s.length()) {
            char ws = this.s.charAt(this.tokStart);
            if (WS.indexOf(ws) < 0) {
                break;
            }
            this.tokStart++;
        }

        TokenType tokType;

        if (this.tokStart >= this.s.length()) {
            // Nothing left to parse.
            return false;
        }

        int tokEnd;
        switch (this.s.charAt(this.tokStart)) {
            case '(': {
                tokType = TokenType.LPAREN;
                tokEnd = this.tokStart + 1;
                break;
            }
            case ')': {
                tokType = TokenType.RPAREN;
                tokEnd = this.tokStart + 1;
                break;
            }
            case '[': {
                tokType = TokenType.LBRACKET;
                tokEnd = this.tokStart + 1;
                break;
            }
            case ']': {
                tokType = TokenType.RBRACKET;
                tokEnd = this.tokStart + 1;
                break;
            }
            case ';': {
                tokType = TokenType.STRUCT_SEP;
                tokEnd = this.tokStart + 1;
                break;
            }
            case '=': {
                tokType = TokenType.EQUALS;
                tokEnd = this.tokStart + 1;
                break;
            }
            case '`':
            case '"': {
                tokType = TokenType.QUOTED;
                tokEnd = scanToEndOfQuoted(this.s, this.tokStart, this.s.charAt(this.tokStart));
                break;
            }
            default:
                tokType = TokenType.STRING;
                tokEnd = scanToEndOfString(this.s, this.tokStart);
                break;
        }

        String tokValue = this.s.substring(this.tokStart, tokEnd)
                .trim();
        if (INTEGER_REGEX.matcher(tokValue)
                .matches()) {
            final int number = Integer.parseInt(tokValue);
            this.tokens.add(new Token(TokenType.INTEGER, number, this.tokStart, tokEnd));
        } else if (FLOAT_REGEX.matcher(tokValue)
                .matches()) {
            final float number = Float.parseFloat(tokValue);
            this.tokens.add(new Token(TokenType.FLOAT, number, this.tokStart, tokEnd));
        } else if (tokValue.equals("null")) {
            this.tokens.add(new Token(TokenType.NULL, null, this.tokStart, tokEnd));
        } else if (tokValue.equals("true")) {
            this.tokens.add(new Token(TokenType.TRUE, true, this.tokStart, tokEnd));
        } else if (tokValue.equals("false")) {
            this.tokens.add(new Token(TokenType.FALSE, false, this.tokStart, tokEnd));
        } else {
            this.tokens.add(new Token(tokType, tokValue, this.tokStart, tokEnd));
        }
        this.tokStart = tokEnd;
        return tokEnd < this.s.length();
    }
}
