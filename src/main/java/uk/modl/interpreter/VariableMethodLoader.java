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

//import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import uk.modl.parser.RawModlObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
//import java.util.function.Function;

/**
 * Created by alex on 19/09/2018.
 */
public class VariableMethodLoader {
//    public static void loadVariableMethod(RawModlObject.Structure structure, Map<String, Function<String, String>> variableMethods) {
    public static void loadVariableMethod(RawModlObject.Structure structure) {
        /*
*method(
  *id=us
  *name=replace_underscores_with_spaces
  *transform=`replace(_, )`
)
         */
        // Load up the values : e.g. :
        String methodName = ModlClassLoader.getPairValueFor(structure, "*id");
        if (methodName == null) {
            methodName = ModlClassLoader.getPairValueFor(structure, "*i");
        }
        String description = ModlClassLoader.getPairValueFor(   structure, "*name");
        if (description == null) {
            description = ModlClassLoader.getPairValueFor(structure, "*n");
        }
        if (description == null) {
            description = methodName;
        }
        String methodString = ModlClassLoader.getPairValueFor(structure, "*transform");
        if (methodString == null) {
            methodString = ModlClassLoader.getPairValueFor(structure, "*t");
        }
        methodString = methodString.replace("`", "");
//        createVariableMethod(methodName, methodString, variableMethods);
        createVariableMethod(methodName, methodString);
    }

//    private static void createVariableMethod(String methodName, String methodString, Map<String, Function<String, String>> variableMethods) {
    private static void createVariableMethod(String methodName, String methodString) {
        List<Map<String, String>> methodsAndParams = getMethodsAndParams(methodString);

//        // CREATE THE NEW METHOD SOMEHOW
//        try {
//            CtClass vms = ClassPool.getDefault().get("uk.modl.interpreter.VariableMethods");
//
//            // TODO How do we make this method?!
//            String newMethodString = "public static String " + methodName + "(String parameter) { return parameter; }";
//            CtMethod m = CtNewMethod.make(newMethodString, vms);
//
//            vms.addMethod(m);
//            VariableMethods.addMethod(methodName, methodName);
//
//            Method m2 = VariableMethods.class.getMethod(methodName, String.class);
//            String result = (String) m2.invoke("blah", "blah");
//
////            byte[] classFile = vms.toBytecode();
////            HotSwapper hs = new HotSwapper(8000);  // 8000 is a port number.
////            hs.reload("uk.modl.interpreter.VariableMethods", classFile);
//        } catch (NotFoundException e) {
//            throw new RuntimeException("Can't find VariableMethods! : " + e);
//        } catch (CannotCompileException e) {
//            throw new RuntimeException("Can't make new method " + methodName + " : " + e);
////        } catch (IOException e) {
////            throw new RuntimeException("Can't hot swap VariableMethods : " + e);
////        } catch (IllegalConnectorArgumentsException e) {
////            throw new RuntimeException("Can't run HotSwap : " + e);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//
////        Function<String, String> function = createNewFunction(variableMethods, methodsAndParams);
////        variableMethods.put(methodName, function);
    }

//    private static Function<String, String> createNewFunction(Map<String, Function<String, String>> variableMethods, List<Map<String, String>> methodsAndParams) {
//        // Now go through methodsAndParams and build up the lambda
//        return parameter -> {
//            String result = parameter;
//            for (Map<String, String> methodAndParam : methodsAndParams) {
//                String method = (String)(methodAndParam.keySet().toArray()[0]);
//                String params = methodAndParam.get(method);
//                if (params == null || params.length() == 0) {
//                    result = variableMethods.get(method).apply(result);
//                } else {
//                    result = variableMethods.get(method).apply(result + "," + params);
//                }
//            }
//            return result;
//        };
//    }

    private static List<Map<String, String>> getMethodsAndParams(String methodString) {
        // Need to go through the methodString, and add a new "apply" in the new function for ech of the vairable methods called!
        // e.g. "replace(-, ).trim(.).initcap"
        // So find the first method and its parameters, and add that to the method list
        // Then continue from the end of the params for the first method, and find the second method and its params, and add that to the lambda
        // Keep going until we reach the end of the methodString
        List<Map<String, String>> methodsAndParams = new LinkedList<>();
        while (methodString.indexOf("(") > 0 || methodString.length() > 0) {
            // Find the next method
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

            // We can build up all the method names and parameters, as we do here
            //  - and then we can build up the whole new function in ONE lambda, with a series of function calls
            //  - each one operating on the last
            //
            Map<String, String> methodAndParam = new HashMap<>();
            methodAndParam.put(intermediateMethodName, params);
            methodsAndParams.add(methodAndParam);

            // Adjust the method string
            methodString = methodString.substring(endIndex, methodString.length());
        }
        return methodsAndParams;
    }

}
