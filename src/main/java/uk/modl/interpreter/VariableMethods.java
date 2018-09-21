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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import  org.apache.commons.text.WordUtils;

public class VariableMethods {

    public static Function<String, String> trim =
            parameter -> {
                if (parameter == null) {
                    return null;
                }
                String[] split = parameter.split(",");
                String subject = split[0];
                String trim = split[1];
                int indexOfTrim = subject.indexOf(trim);
                return subject.substring(0, indexOfTrim);
            };

    public static Function<String, String> replace =
            parameter -> {
                if (parameter == null) {
                    return null;
                }
                String[] split = parameter.split(",");
                String subject = split[0];
                String search = split[1];
                String replace = "";
                if (split.length > 2) {
                    replace = split[2];
                }
                return subject.replace(search, replace);
            };

    public static Function<String, String> upperCase =
            parameter -> {
                if (parameter == null) {
                    return null;
                }
                return parameter.toUpperCase();
            };

    public static Function<String, String> downCase =
            parameter -> {
                if (parameter == null) {
                    return null;
                }
                return parameter.toLowerCase();
            };

    public static Function<String, String> sentence =
            parameter -> {
                if (parameter == null) {
                    return null;
                }
            return makeSentence(parameter);
            };

    public static Function<String, String> urlEncode =
            parameter -> {
        if (parameter == null) {
            return null;
        }
                try {
                    return URLEncoder.encode(parameter, StandardCharsets.UTF_8.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return null;
                }
            };

    public static Function<String, String> initCaps =
            parameter -> {
                if (parameter == null) {
                    return null;
                }
        return WordUtils.capitalize(parameter);
            };

    private static Map<String, Function<String, String>> constantVariableMethods;

    public static Map<String, Function<String, String>> getConstantVariableMethods() {
        if (constantVariableMethods != null) {
            return constantVariableMethods;
        } else {
            initialiseConstantVariableMethods();
            return constantVariableMethods;
        }
    }

    private static void initialiseConstantVariableMethods() {
        constantVariableMethods  = new HashMap<>();
        constantVariableMethods.put("u", upperCase);
        constantVariableMethods.put("d", downCase);
        constantVariableMethods.put("i", initCaps);
        constantVariableMethods.put("initcap", initCaps);
        constantVariableMethods.put("ue", urlEncode);
        constantVariableMethods.put("s", sentence);
        constantVariableMethods.put("t", trim);
        constantVariableMethods.put("trim", trim);
        constantVariableMethods.put("r", replace);
        constantVariableMethods.put("replace", replace);
    }


    private static String makeSentence(String parameter) {
        // Now, Capitalise the first word.
        String[] splits = parameter.split(" ");
        splits[0] = WordUtils.capitalize(splits[0]);
        String ret = "";
        return String.join(" ", splits);
    }


}
