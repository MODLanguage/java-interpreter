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
    /**
     * Convert explicit unicode escape sequences to unicode characters.
     * (recursive implementation)
     *
     * @param str a String possible containing escape sequences.
     * @return the string with escape sequences converted to unicode characters.
     */
    static String convertUnicodeSequences(final String str) {
        // Filter out cases with no escape sequences.
        if (str == null) {
            return null;
        }

        int start = 0;
        String result = str;
        while (true) {
            final int backslashUIndex = result.indexOf("\\u", start);
            final int tildeUIndex = result.indexOf("~u", start);

            // Filter out cases with no escape sequences.
            if (tildeUIndex < 0 && backslashUIndex < 0) {
                break;
            }

            int unicodeStrIdx;
            if (tildeUIndex < 0) {
                unicodeStrIdx = backslashUIndex;
            } else if (backslashUIndex < 0) {
                unicodeStrIdx = tildeUIndex;
            } else {
                unicodeStrIdx = Math.min(backslashUIndex, tildeUIndex);
            }

            if (unicodeStrIdx + 6 > result.length()) {
                break;
            }

            start = unicodeStrIdx + 1;

            if (unicodeStrIdx > 0 && result.charAt(unicodeStrIdx - 1) == '~') {
                continue;
            }
            if (unicodeStrIdx > 0 && result.charAt(unicodeStrIdx - 1) == '\\') {
                continue;
            }

            final int codePoint = Integer.parseInt(result.substring(unicodeStrIdx + 2, unicodeStrIdx + 6), 16);
            final char uni_val = (char) codePoint;
            final String left = result.substring(0, unicodeStrIdx);
            final String right = result.substring(unicodeStrIdx + 6);
            result = left + uni_val + right;
        }
        return result;
    }
}
