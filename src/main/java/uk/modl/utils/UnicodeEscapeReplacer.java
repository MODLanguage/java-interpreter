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

class UnicodeEscapeReplacer {

    private static final String BACKSLASH_U = "\\u";

    private static final String TILDE_U = "~u";

    private static final char TILDE = '~';

    private static final char BACKSLASH = '\\';

    private static final int HEX = 16;

    /**
     * Convert explicit unicode escape sequences to unicode characters.
     * (recursive implementation)
     *
     * @param str a String possible containing escape sequences.
     * @return the string with escape sequences converted to unicode characters.
     */
    static String convertUnicodeSequences(final String str) {
        int start = 0;
        String result = str;
        while (result != null) {
            // We could have a backslash-u escape sequence or a ~u escape sequence
            final int backslashUIndex = result.indexOf(BACKSLASH_U, start);
            final int tildeUIndex = result.indexOf(TILDE_U, start);

            // Filter out cases with no escape sequences.
            int unicodeStrIdx;
            if (tildeUIndex < 0 && backslashUIndex < 0) {
                break;
            } else if (tildeUIndex < 0) {
                unicodeStrIdx = backslashUIndex;// No ~? Must be backslash
            } else if (backslashUIndex < 0) {
                unicodeStrIdx = tildeUIndex;// No backslash? Must be ~
            } else {
                // Pick the first escaped character and proceed with that one.
                unicodeStrIdx = Math.min(backslashUIndex, tildeUIndex);
            }

            final TryParseResult tryParse = tryParse(result, unicodeStrIdx + 2);

            // Next time round the loop we start searching after the current escape sequence.
            start = unicodeStrIdx + 1;

            // If the escape sequence is itself escaped then don't replace it
            if (unicodeStrIdx > 0 && (result.charAt(unicodeStrIdx - 1) == TILDE || result.charAt(unicodeStrIdx - 1) == BACKSLASH)) {
                continue;
            }

            // Get the codepoint value and replace the escape sequence
            if (tryParse.codePoint > 0) {
                final char[] chars = Character.toChars(tryParse.codePoint);
                result = replace(result, chars, unicodeStrIdx, tryParse.length + 2);
            }
        }
        return result;
    }

    /**
     * Replace a unicode value in a String
     *
     * @param s             the String with the unicode value to be replaced.
     * @param value         the replacement character
     * @param unicodeStrIdx the index of the unicode escape sequence
     * @param length        the number of characters to be replaced.
     * @return a String with the unicode escape sequence replaced by the replacement character
     */
    private static String replace(final String s, final char[] value, final int unicodeStrIdx, final int length) {
        final String left = s.substring(0, unicodeStrIdx);
        final int end = Math.min(s.length(), unicodeStrIdx + length);
        final String right = s.substring(end);
        return left + String.valueOf(value) + right;
    }

    /**
     * Check whether the value is a valid unicode codepoint
     *
     * @param value the int to check
     * @return true if the value is a valid unicode codepoint
     */
    private static boolean isValidRange(final int value) {
        return (value >= 0x100000 && value <= 0x10ffff) || (value >= 0x10000 && value <= 0xfffff) || (value >= 0 && value <= 0xd7ff) || (value >= 0xe000 && value <= 0xffff);
    }

    /**
     * Can we get `n` hex digits from the string at the `idx` location?
     *
     * @param s   the String to check
     * @param idx the index to start searching in the string
     * @param n   the number of digits needed
     * @return true if enough digits are available
     */
    private static boolean hasEnoughDigits(final String s, final int idx, final int n) {
        int i = 0;
        char[] chars = s.toCharArray();
        while (i < n && (idx + i) < s.length()) {
            char c = chars[idx + i];
            if (!Character.isDigit(c) && !("abcdefABCDEF".indexOf(c) > -1)) {
                return false;
            }
            i++;
        }
        return i == n;
    }

    /**
     * Attempt to parse a unicode character starting at `idx` in `str`
     *
     * @param str the String to parse
     * @param idx the starting index
     * @return a TryParseResult with codepoint set to 0 on failure.
     */
    private static TryParseResult tryParse(final String str, final int idx) {

        // Check for a 6-digit unicode value
        if (hasEnoughDigits(str, idx, 6)) {
            final int value = Integer.parseInt(str.substring(idx, idx + 6), HEX);
            if (isValidRange(value)) {
                return new TryParseResult(value, 6);
            }
        }

        // Check for a 5-digit unicode value
        if (hasEnoughDigits(str, idx, 5)) {
            final int value = Integer.parseInt(str.substring(idx, idx + 5), HEX);
            if (isValidRange(value)) {
                return new TryParseResult(value, 5);
            }
        }

        // Check for a 4-digit unicode value
        if (hasEnoughDigits(str, idx, 4)) {
            final int value = Integer.parseInt(str.substring(idx, idx + 4), HEX);
            if (isValidRange(value)) {
                return new TryParseResult(value, 4);
            }
        }

        // Failed
        return new TryParseResult(0, 4);
    }

    /**
     * Class to hold the result of the tryParse method
     */
    private static class TryParseResult {

        final int codePoint;

        final int length;

        /**
         * Constructor
         *
         * @param codePoint the codepoint that was found, or zero
         * @param length    the number of characters used by the codepoint.
         */
        private TryParseResult(final int codePoint, final int length) {
            this.codePoint = codePoint;
            this.length = length;
        }

    }

}