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
import java.net.IDN;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class VariableMethods {

    static Pattern STRING = Pattern.compile("(\\w+)");
    static private Map<String, TaskRunner> methods = null;

    private static void initialiseMethods() {
        methods = new HashMap<>();
        addTrimTask();
        addUpperCaseTask();
        addDownCaseTask();
        addInitCapsTask();
        addUrlEncodeTask();
        addSentenceTask();
        addReplaceTask();
        addPunycodeTask();
    }

    private static void addTrimTask() {
        TaskRunner trimTask = new TaskRunner() {
            @Override
            public void run() {
                if (parameter == null) {
                    result = null;
                } else {
                    String[] split = parameter.split(",");
                    String subject = split[0];
                    String trim = split[1];
                    int indexOfTrim = subject.indexOf(trim);
                    result = subject.substring(0, indexOfTrim);
                }
            }
        };
        methods.put("t", trimTask);
        methods.put("trim", trimTask);
    }

    private static void addUpperCaseTask() {
        TaskRunner upperCaseTask = new TaskRunner() {
            @Override
            public void run() {
                if (parameter == null) {
                    result = null;
                } else {
                    result = parameter.toUpperCase();
                }
            }
        };
        methods.put("u", upperCaseTask);
        methods.put("uupcase", upperCaseTask);
    }

    private static void addSentenceTask() {
        TaskRunner sentenceTask = new TaskRunner() {
            @Override
            public void run() {
                if (parameter == null) {
                    result = null;
                } else {
                    result = makeSentence(parameter);
                }
            }
        };
        methods.put("s", sentenceTask);
        methods.put("sentence", sentenceTask);
    }

    private static void addDownCaseTask() {
        TaskRunner downCaseTask = new TaskRunner() {
            @Override
            public void run() {
                if (parameter == null) {
                    result = null;
                } else {
                    result = parameter.toLowerCase();
                }
            }
        };
        methods.put("d", downCaseTask);
        methods.put("downcase", downCaseTask);
    }

    private static void addInitCapsTask() {
        TaskRunner task = new TaskRunner() {
            @Override
            public void run() {
                if (parameter == null) {
                    result = null;
                }
                result = WordUtils.capitalize(parameter);
            }
        };
        methods.put("i", task);
        methods.put("initcap", task);
    }

    private static void addReplaceTask() {
        TaskRunner task = new TaskRunner() {
            @Override
            public void run() {
                if (parameter == null) {
                    result = null;
                } else {
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
        };
        methods.put("r", task);
        methods.put("replace", task);
    }

    private static void addUrlEncodeTask() {
        TaskRunner task = new TaskRunner() {
            @Override
            public void run() {
                if (parameter == null) {
                    result = null;
                } else {
                    try {
                        result = URLEncoder.encode(parameter, StandardCharsets.UTF_8.toString());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        result = null;
                    }
                }
            }
        };
        methods.put("e", task);
        methods.put("urlencode", task);
    }

    private static void addPunycodeTask() {
        TaskRunner task = new TaskRunner() {
            @Override
            public void run() {
                if (parameter == null) {
                    result = null;
                }
                result = replacePunycode(parameter);
            }
        };
        methods.put("p", task);
        methods.put("punydecode", task);
    }

    static String transform(String methodName, String input) {
        if (methods == null) {
            initialiseMethods();
        }

        TaskRunner taskRunner = methods.get(methodName);

        // Now call it!!!
        try {
            taskRunner.setParameter(input);
            taskRunner.run();
            return taskRunner.getResult();
        } catch (Exception e) {
            throw new RuntimeException("Can't call variable transform " + methodName + " : " + e);
        }
    }

    static void addMethod(String shortName, TaskRunner taskRunner) {
        if (methods == null) {
            initialiseMethods();
        }
        methods.put(shortName, taskRunner);
    }

    private static String makeSentence(String parameter) {
        // Now, Capitalise the first word.
        String[] splits = parameter.split(" ");
        splits[0] = WordUtils.capitalize(splits[0]);
        //        return String.join(" ", splits);
        StringBuilder ret = new StringBuilder();
        int count = 0;
        for (String s : splits) {
            if (count > 0) {
                ret.append(" ");
            }
            count++;
            ret.append(s);
        }
        return ret.toString();
    }

    private static String replacePunycode(String stringToTransform) {
        // Prefix it with xn-- (the letters xn and two dashes) and decode using punycode / IDN library. Replace the full part (including graves) with the decoded value.
        if (stringToTransform == null) {
            return null;
        }
        if (stringToTransform.startsWith("`") && stringToTransform.endsWith("`")) {
            stringToTransform = stringToTransform.substring(1, stringToTransform.length() - 1);
        }
        String originalString = stringToTransform;
        stringToTransform = "xn--" + stringToTransform;
        String newStringToTransform = IDN.toUnicode(stringToTransform);
        if (newStringToTransform.equals(stringToTransform)) {
            stringToTransform = originalString;
        } else {
            stringToTransform = newStringToTransform;
        }
        return stringToTransform;
    }

    static boolean isVariableMethod(String s) {
        if (methods == null) {
            initialiseMethods();
        }
        final Matcher matcher = STRING.matcher(s);

        if (matcher.find() && matcher.groupCount() > 0) {
            final String match = matcher.group(0);
            return methods.get(match) != null;
        }
        return false;
    }

    static abstract class TaskRunner implements Runnable {
        String parameter;
        String result;

        void setParameter(String s) {
            parameter = s;
        }

        String getResult() {
            return result;
        }
    }

}
