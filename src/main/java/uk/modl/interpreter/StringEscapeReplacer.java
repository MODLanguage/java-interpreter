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

import java.util.LinkedHashMap;
import java.util.Map;

public class StringEscapeReplacer {
    private static Map<String, String> replacements = new LinkedHashMap<>();

    public static String replace(String stringToTransform) {
        // String-replacement.text to replace escaped characters
        if (replacements.isEmpty()) {
            loadReplacements();
        }
        for (Map.Entry<String, String> replacement : replacements.entrySet()) {
            if (stringToTransform.contains(replacement.getKey())) {
                stringToTransform = stringToTransform.replace(replacement.getKey(), replacement.getValue());
            }
        }

        return convertUnicodeSequences(stringToTransform);
    }

    /**
     * Convert explicit unicode escape sequences to unicode characters.
     * (recursive implementation)
     *
     * @param string a String possible containing escape sequences.
     * @return the string with escape sequences converted to unicode characters.
     */
    private static String convertUnicodeSequences(final String string) {
        // Filter out cases with no escape sequences.
        if (string == null) {
            return null;
        }

        int begin = string.indexOf("\\u");
        if (begin < 0) {
            begin = string.indexOf("~u");
            if (begin < 0) {
                return string;
            }
        }

        // Extract the base-16 codepoint
        final int codepoint = Integer.parseInt(string.substring(begin + 2, begin + 6), 16);

        // Get the full unicode escape sequence to be replaced.
        final String toReplace = string.substring(begin, begin + 6);

        // Create a string to replace it with.
        final String replacement = "" + (char) codepoint;

        // Replace and recurse to handle any other escape sequences
        return convertUnicodeSequences(string.replace(toReplace, replacement));
    }

    private static void loadReplacements() {
        replacements.put("\\%", "%");
        replacements.put("~%", "%");
        replacements.put("~\\", "\\");
        replacements.put("\\\\", "\\");
        replacements.put("~~", "~");
        replacements.put("\\~", "~");

        replacements.put("~(", "(");
        replacements.put("\\(", "(");
        replacements.put("~)", ")");
        replacements.put("\\)", ")");

        replacements.put("~[", "[");
        replacements.put("\\[", "[");
        replacements.put("~]", "]");
        replacements.put("\\]", "]");

        replacements.put("~{", "{");
        replacements.put("\\{", "{");
        replacements.put("~}", "}");
        replacements.put("\\}", "}");

        replacements.put("~;", ";");
        replacements.put("\\;", ";");
        replacements.put("~:", ":");
        replacements.put("\\:", ":");

        replacements.put("~`", "`");
        replacements.put("\\`", "`");
        replacements.put("~\"", "\"");
        replacements.put("\\\"", "\"");

        replacements.put("~=", "=");
        replacements.put("\\=", "=");
        replacements.put("~/", "/");
        replacements.put("\\/", "/");

        replacements.put("<", "<");
        replacements.put("\\<", "<");
        replacements.put("~>", ">");
        replacements.put("\\>", ">");

        replacements.put("~&", "&");
        replacements.put("\\&", "&");

        replacements.put("!", "!");
        replacements.put("\\!", "!");
        replacements.put("~|", "|");
        replacements.put("\\|", "|");

        replacements.put("\\t", "\t");
        replacements.put("\\n", "\n");
        replacements.put("\\b", "\b");
        replacements.put("\\f", "\f");
        replacements.put("\\r", "\r");
    }
}
