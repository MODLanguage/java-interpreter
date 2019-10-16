/*
MIT License

Copyright (c) 2018 NUM Technology Ltd

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation
the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of
the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package uk.modl.interpreter;

class UnicodeEscapeReplacer {

    private static final String BACKSLASH_U = "\\u";
    private static final String TILDE_U = "~u";
    private static final char TILDE = '~';
    private static final char BACKSLASH = '\\';
    private static final int ESCAPE_SEQUENCE_LENGHT = 6;
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

            // Do we have a long enough value? Break out if not.
            if (unicodeStrIdx + ESCAPE_SEQUENCE_LENGHT > result.length()) {
                break;
            }

            // Next time round the loop we start searching after the current escape sequence.
            start = unicodeStrIdx + 1;

            // If the escape sequence is itself escaped then don't replace it
            if (unicodeStrIdx > 0 && (result.charAt(unicodeStrIdx - 1) == TILDE || result.charAt(unicodeStrIdx - 1) == BACKSLASH)) {
                continue;
            }

            // Get the codepoint value and replace the escape sequence
            final int codePoint = Integer.parseInt(result.substring(unicodeStrIdx + 2, unicodeStrIdx + ESCAPE_SEQUENCE_LENGHT), HEX);
            result = replace(result, (char) codePoint, unicodeStrIdx);
        }
        return result;
    }

    /**
     * Replace a unicode value in a String
     *
     * @param s             the String with the unicode value to be replaced.
     * @param value         the replacement character
     * @param unicodeStrIdx the index of the unicode escape sequence
     * @return a String with the unicode escape sequence replaced by the replacement character
     */
    private static String replace(final String s, final char value, final int unicodeStrIdx) {
        final String left = s.substring(0, unicodeStrIdx);
        final String right = s.substring(unicodeStrIdx + ESCAPE_SEQUENCE_LENGHT);
        return left + value + right;
    }
}
