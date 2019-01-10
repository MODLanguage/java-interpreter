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

import org.apache.commons.text.WordUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class VariableMethods {

    static private Map<String, String> methodNames = null;

    private static void initialiseMethods() {
        methodNames = new HashMap<>();
        methodNames.put("u", "upperCase");
        methodNames.put("d", "downCase");
        methodNames.put("i", "initCaps");
        methodNames.put("initcap", "initCaps");
        methodNames.put("ue", "urlEncode");
        methodNames.put("s", "sentence");
        methodNames.put("t", "trim");
        methodNames.put("trim", "trim");
        methodNames.put("r", "replace");
        methodNames.put("replace", "replace");
    }

    public static void addMethod(String shortName, String longName) {
        if (methodNames == null) {
            initialiseMethods();
        }
        methodNames.put(shortName, longName);
    }

    public static String transform(String methodName, String input) {
        if (methodNames == null) {
            initialiseMethods();
        }
        String transformMethodName = methodNames.get(methodName);

        // Now call it!!!
        Class<?> type = String.class;
        try {
            Method m = VariableMethods.class.getMethod(transformMethodName, type);
            String result = (String) m.invoke(input, input);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Can't call variable method " + methodName + " : " + e);
        }
    }

    // Need to make this Java 1.7 compatible
    // Can't use lambdas or functions.
    // Can we take an interface "String transform(String)" - and then make Runnables which implement this?
    static public String trim(String s) {
        class TrimTask implements Runnable {
            String parameter;
            String result;
            TrimTask(String param) { parameter = param; }
            public void run() {
                if (parameter == null) {
                    result = null;
                }
                String[] split = parameter.split(",");
                String subject = split[0];
                String trim = split[1];
                int indexOfTrim = subject.indexOf(trim);
                result = subject.substring(0, indexOfTrim);
            }
        }
        TrimTask trimTask = new TrimTask(s);
        trimTask.run();
        return trimTask.result;
    }

//    public static Function<String, String> trim =
//            parameter -> {
//                if (parameter == null) {
//                    return null;
//                }
//                String[] split = parameter.split(",");
//                String subject = split[0];
//                String trim = split[1];
//                int indexOfTrim = subject.indexOf(trim);
//                return subject.substring(0, indexOfTrim);
//            };

    public static String replace(String s) {
        class ReplaceTask implements Runnable {
            String parameter;
            String result;
            ReplaceTask(String param) { parameter = param; }
            public void run() {
                if (parameter == null) {
                    result = null;
                }
                String[] split = parameter.split(",");
                String subject = split[0];
                String search = split[1];
                String replace = "";
                if (split.length > 2) {
                    replace = split[2];
                }
                result = subject.replace(search, replace);
            }
        }
        ReplaceTask replaceTask = new ReplaceTask(s);
        replaceTask.run();
        return replaceTask.result;
    }

//    public static Function<String, String> replace =
//            parameter -> {
//                if (parameter == null) {
//                    return null;
//                }
//                String[] split = parameter.split(",");
//                String subject = split[0];
//                String search = split[1];
//                String replace = "";
//                if (split.length > 2) {
//                    replace = split[2];
//                }
//                return subject.replace(search, replace);
//            };

    public static String upperCase(String s) {
        class UpperCaseTask implements Runnable {
            String parameter;
            String result;
            UpperCaseTask(String param) { parameter = param; }
            public void run() {
                if (parameter == null) {
                    result = null;
                }
                result = parameter.toUpperCase();
            }
        }
        UpperCaseTask upperCaseTask = new UpperCaseTask(s);
        upperCaseTask.run();
        return upperCaseTask.result;
    }

//    public static Function<String, String> upperCase =
//            parameter -> {
//                if (parameter == null) {
//                    return null;
//                }
//                return parameter.toUpperCase();
//            };

    static public String downCase(String s) {
        class DownCaseTask implements Runnable {
            String parameter;
            String result;
            DownCaseTask(String param) { parameter = param; }
            public void run() {
                if (parameter == null) {
                    result = null;
                }
                result = parameter.toLowerCase();
            }
        }
        DownCaseTask downCaseTask = new DownCaseTask(s);
        downCaseTask.run();
        return downCaseTask.result;
    }

//    public static Function<String, String> downCase =
//            parameter -> {
//                if (parameter == null) {
//                    return null;
//                }
//                return parameter.toLowerCase();
//            };

    static public String sentence(String s) {
        class SentenceTask implements Runnable {
            String parameter;
            String result;
            SentenceTask(String param) { parameter = param; }
            public void run() {
                if (parameter == null) {
                    result = null;
                }
                result = makeSentence(parameter);
            }
        }
        SentenceTask sentenceTask = new SentenceTask(s);
        sentenceTask.run();
        return sentenceTask.result;
    }

//    public static Function<String, String> sentence =
//            parameter -> {
//                if (parameter == null) {
//                    return null;
//                }
//            return makeSentence(parameter);
//            };

    static public String urlEncode(String s) {
        class UrlEncodeTask implements Runnable {
            String parameter;
            String result;
            UrlEncodeTask(String param) { parameter = param; }
            public void run() {
                if (parameter == null) {
                    result = null;
                }
                try {
                    result = URLEncoder.encode(parameter, StandardCharsets.UTF_8.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    result = null;
                }
            }
        }
        UrlEncodeTask urlEncodeTask = new UrlEncodeTask(s);
        urlEncodeTask.run();
        return urlEncodeTask.result;
    }

//    public static Function<String, String> urlEncode =
//            parameter -> {
//        if (parameter == null) {
//            return null;
//        }
//                try {
//                    return URLEncoder.encode(parameter, StandardCharsets.UTF_8.toString());
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                    return null;
//                }
//            };

    static public String initCaps(String s) {
        class InitCapsTask implements Runnable {
            String parameter;
            String result;
            InitCapsTask(String param) { parameter = param; }
            public void run() {
                if (parameter == null) {
                    result = null;
                }
                result = WordUtils.capitalize(parameter);;
            }
        }
        InitCapsTask initCapsTask = new InitCapsTask(s);
        initCapsTask.run();
        return initCapsTask.result;
    }

//    public static Function<String, String> initCaps =
//            parameter -> {
//                if (parameter == null) {
//                    return null;
//                }
//        return WordUtils.capitalize(parameter);
//            };

//    private static Map<String, Function<String, String>> constantVariableMethods;
//
//    public static Map<String, Function<String, String>> getConstantVariableMethods() {
//        if (constantVariableMethods != null) {
//            return constantVariableMethods;
//        } else {
//            initialiseConstantVariableMethods();
//            return constantVariableMethods;
//        }
//    }
//
//    private static void initialiseConstantVariableMethods() {
//        constantVariableMethods  = new HashMap<>();
//        constantVariableMethods.put("u", upperCase);
//        constantVariableMethods.put("d", downCase);
//        constantVariableMethods.put("i", initCaps);
//        constantVariableMethods.put("initcap", initCaps);
//        constantVariableMethods.put("ue", urlEncode);
//        constantVariableMethods.put("s", sentence);
//        constantVariableMethods.put("t", trim);
//        constantVariableMethods.put("trim", trim);
//        constantVariableMethods.put("r", replace);
//        constantVariableMethods.put("replace", replace);
//    }


    private static String makeSentence(String parameter) {
        // Now, Capitalise the first word.
        String[] splits = parameter.split(" ");
        splits[0] = WordUtils.capitalize(splits[0]);
//        return String.join(" ", splits);
        String ret = "";
        int count = 0;
        for (String s : splits) {
            if (count > 0) {
                ret += " ";
            }
            count++;
            ret += s;
        }
        return ret;
    }


    public static boolean isVariableMethod(String s) {
        if (methodNames == null) {
            initialiseMethods();
        }
        if (methodNames.get(s) != null) {
            return true;
        }
        return false;
    }
}
