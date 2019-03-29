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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class VariableMethods {

    static private Map<String, TaskRunner> methods = null;

    public static abstract class TaskRunner implements Runnable {
        protected String parameter;
        protected String result;
        public void setParameter(String s) {
            parameter = s;
        }
        public String getResult() {
            return result;
        }
    }

    private static void initialiseMethods() {
        methods = new HashMap<>();
        addTrimTask();
        addUpperCaseTask();
        addDownCaseTask();
        addInitCapsTask();
        addUrlEncodeTask();
        addSentenceTask();
        addReplaceTask();
    }

    private static void addTrimTask() {
        TaskRunner trimTask = new TaskRunner() {
            @Override
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
                }
                result = parameter.toUpperCase();
            }
        };
        methods.put("u", upperCaseTask);
    }

    private static void addSentenceTask() {
        TaskRunner sentenceTask = new TaskRunner() {
            @Override
            public void run() {
                if (parameter == null) {
                    result = null;
                }
                result = makeSentence(parameter);
            }
        };
        methods.put("s", sentenceTask);
    }

    private static void addDownCaseTask() {
        TaskRunner downCaseTask = new TaskRunner() {
            @Override
            public void run() {
                if (parameter == null) {
                    result = null;
                }
                result = parameter.toLowerCase();
            }
        };
        methods.put("d", downCaseTask);
    }

    private static void addInitCapsTask() {
        TaskRunner task = new TaskRunner() {
            @Override
            public void run() {
                if (parameter == null) {
                    result = null;
                }
                result = WordUtils.capitalize(parameter);;
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
                }
                try {
                    result = URLEncoder.encode(parameter, StandardCharsets.UTF_8.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    result = null;
                }
            }
        };
        methods.put("e", task);
    }

    public static String transform(String methodName, String input) {
        if (methods == null) {
            initialiseMethods();
        }
        TaskRunner taskRunner = methods.get(methodName);

        // Now call it!!!
        try {
            taskRunner.setParameter(input);
            taskRunner.run();
            String result = taskRunner.getResult();
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Can't call variable method " + methodName + " : " + e);
        }
    }

    public static void addMethod(String shortName, TaskRunner taskRunner) {
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
        if (methods == null) {
            initialiseMethods();
        }
        if (methods.get(s) != null) {
            return true;
        }
        return false;
    }

}
