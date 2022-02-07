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

package uk.modl.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.experimental.UtilityClass;

@UtilityClass
class UnicodeEscapeReplacer {

    private static final int HEX = 16;

    private static final int UNICODE_SEQ_LEN = 6;

    private static final Pattern reg = Pattern.compile("[~\\\\]u[0-9a-fA-F]{4}");

    /**
     * Convert explicit unicode escape sequences to unicode characters. (recursive
     * implementation)
     *
     * @param str a String possible containing escape sequences.
     * @return the string with escape sequences converted to unicode characters.
     */
    static String convertUnicodeSequences(final String str) {
        Matcher m;
        int start = 0;
        StringBuilder result = new StringBuilder();
        m = reg.matcher(str);
        if (m.find()) {
            do {
                if (m.start() > 0 && (str.charAt(m.start() - 1) == '\\' || str.charAt(m.start() - 1) == '~')) {
                    // Its escaped so copy over as-is, without the leading slash
                    if (start < m.start() - 1) {
                        // Copy up to the slash
                        result.append(str, start, m.start() - 1);
                    }
                    // Copy from after the slash
                    result.append(str, m.start(), m.start() + UNICODE_SEQ_LEN);

                    // Point to the next character after the current escape sequence
                } else {
                    if (start < m.start()) {
                        // Copy up to the current escape sequence
                        result.append(str, start, m.start());
                    }

                    // Append the converted character
                    final int c = Integer.parseInt(str.substring(m.start() + 2, 4), HEX);
                    result.append(Character.toChars(c));

                    // Point to the next character after the current escape sequence
                }
                start = m.start() + UNICODE_SEQ_LEN;
            } while (m.find());
        } else {
            result.append(str.substring(start));
        }

        return result.toString();
    }

}
