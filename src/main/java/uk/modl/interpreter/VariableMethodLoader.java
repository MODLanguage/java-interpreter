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

import uk.modl.parser.RawModlObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
//import java.util.function.Function;

/**
 * Created by alex on 19/09/2018.
 */
class VariableMethodLoader {
    static void loadVariableMethod(List<MethodDescriptor> methodList,
                                   RawModlObject.Structure structure,
                                   Interpreter interpreter) {

        final MethodDescriptor desc = new MethodDescriptor();
        // Load up the values : e.g. :
        String methodName = ModlClassLoader.getPairValueFor(structure, "*id", interpreter);
        if (methodName == null) {
            methodName = ModlClassLoader.getPairValueFor(structure, "*i", interpreter);
        }
        desc.id = methodName;
        String description = ModlClassLoader.getPairValueFor(structure, "*name", interpreter);
        if (description == null) {
            description = ModlClassLoader.getPairValueFor(structure, "*n", interpreter);
        }
        desc.name = description;

        String methodString = ModlClassLoader.getPairValueFor(structure, "*transform", interpreter);
        if (methodString == null) {
            methodString = ModlClassLoader.getPairValueFor(structure, "*t", interpreter);
        }
        if (methodString != null) {
            methodString = methodString.replace("`", "");
        }
        desc.transform = methodString;

        createVariableMethod(methodName, methodString);
        methodList.add(desc);
    }

    private static void createVariableMethod(String methodName, String methodString) {
        final List<Map<String, String>> methodsAndParams = getMethodsAndParams(methodString);

        // We need to create a TaskRunner, and then add that to the VariableMethods.
        VariableMethods.TaskRunner newTask = new VariableMethods.TaskRunner() {
            @Override
            public void run() {
                // Make the new transform!!!
                String intermediateResult = parameter;
                for (Map<String, String> methodAndParam : methodsAndParams) {
                    String method = (String) (methodAndParam.keySet().toArray()[0]);
                    String params = methodAndParam.get(method);
                    if (params == null || params.length() == 0) {
                        intermediateResult = VariableMethods.transform(method, intermediateResult);
                    } else {
                        intermediateResult = VariableMethods.transform(method, intermediateResult + "," + params);
                    }
                }
                result = intermediateResult;
            }
        };
        VariableMethods.addMethod(methodName, newTask);

    }

    private static List<Map<String, String>> getMethodsAndParams(String methodString) {
        // Need to go through the methodString, and add a new "apply" in the new function for ech of the vairable methods called!
        // e.g. "replace(-, ).trim(.).initcap"
        // So find the first transform and its parameters, and add that to the transform list
        // Then continue from the end of the params for the first transform, and find the second transform and its params, and add that to the lambda
        // Keep going until we reach the end of the methodString
        List<Map<String, String>> methodsAndParams = new LinkedList<>();
        while (methodString.indexOf("(") > 0 || methodString.length() > 0) {
            // Find the next transform
            // This can either be by the first bracket, or the first "." - whichever comes first
            int bracketIndex = methodString.indexOf("(");
            int dotIndex = methodString.indexOf(".");
            int endOfStringIndex = methodString.length();
            int actualIndex = bracketIndex;
            if (actualIndex < 0) {
                actualIndex = dotIndex;
            } else {
                if (dotIndex > 0) {
                    actualIndex = Math.min(dotIndex, bracketIndex);
                }
            }
            if (actualIndex < 0) {
                actualIndex = endOfStringIndex;
            }


            String intermediateMethodName = methodString.substring(0, actualIndex);

            // Get the parameters
            // If we split on a "(", then we need to find the closing ")" - the stuff inbetween these are the params
            // If we split on ".", then there are no params
            String params = null;
            int endIndex = endOfStringIndex;
            if (actualIndex == bracketIndex) {
                int closeBracketIndex = methodString.indexOf(")");
                params = methodString.substring(bracketIndex + 1, closeBracketIndex);
                endIndex = closeBracketIndex + 1;
                if (endIndex < methodString.length()) {
                    endIndex++;
                }
            } else {
                if (actualIndex == dotIndex) {
                    endIndex = actualIndex + 1;
                }
            }

            // We can build up all the transform names and parameters, as we do here
            //  - and then we can build up the whole new function in ONE lambda, with a series of function calls
            //  - each one operating on the last
            //
            Map<String, String> methodAndParam = new HashMap<>();
            methodAndParam.put(intermediateMethodName, params);
            methodsAndParams.add(methodAndParam);

            // Adjust the transform string
            methodString = methodString.substring(endIndex);
        }
        return methodsAndParams;
    }

    public static class MethodDescriptor {
        public String name;
        String id;
        String transform;
    }

}
