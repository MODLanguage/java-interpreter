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

import org.apache.commons.lang3.tuple.MutablePair;
import uk.modl.modlObject.ModlObject;
import uk.modl.modlObject.ModlObjectTreeWalker;
import uk.modl.parser.RawModlObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class StringEscapeReplacer {
    private static Map<String, String> replacements = new LinkedHashMap<>();

    public static void replaceAll(final ModlObject rawModlObject) {
        final ModlObjectTreeWalker walker = new ModlObjectTreeWalker(rawModlObject);
        walker.walk(v -> {
            if (v instanceof ModlObject.String) {
                final ModlObject.String str = (ModlObject.String) v;
                str.string = StringEscapeReplacer.replace(str.string);
            }
            if (v instanceof MutablePair) {
                final MutablePair p = (MutablePair) v;
                if (p.left instanceof String) {
                    p.left = StringEscapeReplacer.replace((String) p.left);
                }
                if (p.right instanceof String) {
                    p.right = StringEscapeReplacer.replace((String) p.right);
                }
            }
            if (v instanceof RawModlObject.Condition) {
                final RawModlObject.Condition c = (RawModlObject.Condition) v;
                c.setKey(StringEscapeReplacer.replace(c.getKey()));
            }
        });
    }

    public static String replace(String stringToTransform) {
        if (stringToTransform != null) {
            stringToTransform = UnicodeEscapeReplacer.convertUnicodeSequences(stringToTransform);
            // String-replacement.text to replace escaped characters
            if (replacements.isEmpty()) {
                loadReplacements();
            }
            for (Map.Entry<String, String> replacement : replacements.entrySet()) {
                if (stringToTransform.contains(replacement.getKey())) {
                    stringToTransform = stringToTransform.replace(replacement.getKey(), replacement.getValue());
                }
            }
        }
        return stringToTransform;
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
