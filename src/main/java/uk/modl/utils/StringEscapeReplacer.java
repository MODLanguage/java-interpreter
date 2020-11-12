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

import lombok.val;
import lombok.var;

import java.util.LinkedHashMap;
import java.util.Map;

public class StringEscapeReplacer {

    private static final Map<String, String> replacements = new LinkedHashMap<>();

    /*
      Synchronized since two quick successive requests in the java-interpreter-server can clash.
     */
    static {
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

    public static String replace(final String stringToTransform) {
        var result = UnicodeEscapeReplacer.convertUnicodeSequences(stringToTransform);

        for (int i = 0; i < result.length(); i++) {
            for (val replacement : replacements.entrySet()) {
                final String key = replacement.getKey();
                if (result.startsWith(key, i)) {
                    final String value = replacement.getValue();
                    result = result.substring(0, i) + value + result.substring(i + key.length());
                    break;// we only do one replacement at each position
                }
            }
        }
        return result;
    }

}