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

import org.apache.commons.text.StringEscapeUtils;
import uk.modl.modlObject.ModlObject;
import uk.modl.modlObject.ModlValue;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

//import java.util.function.Function;

public class StringTransformer {

    private Map<String, ModlValue> valuePairs;
    private Map<String, ModlValue> variables;
    private Map<Integer, ModlValue> numberedVariables;


    StringTransformer(Map<String, ModlValue> valuePairs,
                      Map<String, ModlValue> variables,
                      Map<Integer, ModlValue> numberedVariables) {
        this.valuePairs = valuePairs;
        this.variables = variables;
        this.numberedVariables = numberedVariables;
    }

    public static List<String> getPercentPartsFromString(String stringToTransform) {
        // Find all non-space parts of the string that are prefixed with % (percent sign) and don't end with %
        List<String> percentParts = new LinkedList<>();
        int currentIndex = 0;
        boolean finished = false;
        while (!finished) {
            finished = true;
            int startIndex = getNextPercent(stringToTransform, currentIndex);
            if (startIndex > -1) {
                int endIndex;
                // If the first character after the % is a number, then keep reading until we get to a non-number (taking account of method chains)
                // If the first character after the % is a letter, then keep reading until we get to a space
                if (startIndex < stringToTransform.length() - 1) {
                    // Just read to the next character that terminates a reference.
                    endIndex = getReferenceEndIndex(stringToTransform, startIndex);

                    if (endIndex > stringToTransform.length()) {
                        endIndex = stringToTransform.length();
                    }
                } else if (startIndex == stringToTransform.length() - 1) {
                    return percentParts;
                } else {
                    endIndex = getEndOfNumber(stringToTransform, startIndex + 1);
                }
                if (endIndex != -1) {
                    if (endIndex > startIndex + 1) {
                        String gravePart = stringToTransform.substring(startIndex, endIndex);
                        percentParts.add(gravePart);
                        currentIndex = endIndex;
                    } else if (endIndex == (startIndex + 1)) {
                        finished = true;
                        continue;
                    }
                    finished = false;
                }
            }
        }
        return percentParts;
    }

    private static int getReferenceEndIndex(final String stringToTransform, final int startIndex) {
        int endIndex = 99999;
        boolean inMethodParams = false;
        boolean inGraves = false;
        final String endChars = " :%!<,>";

        char prev = '\0';
        for (int i = startIndex + 1; i < stringToTransform.length(); i++) {
            final char c = stringToTransform.charAt(i);
            if (c == '<') {
                inMethodParams = true;
            } else if (c == '>') {
                inMethodParams = false;
            } else if (c == '`') {
                if (!inGraves && !inMethodParams && i > startIndex + 1) {
                    endIndex = i;
                    break;
                }
                inGraves = !inGraves;
            } else {
                if (endChars.indexOf(c) > -1 && !inGraves && !inMethodParams && prev != '.') {
                    endIndex = i;
                    if (c == '%') {
                        endIndex++;
                    }
                    break;
                }
            }
            prev = c;
        }
        if (endIndex > stringToTransform.length()) {
            endIndex = stringToTransform.length();
        }
        return endIndex;
    }

    private static Integer getEndOfNumber(String stringToTransform, Integer startIndex) {
        // We actually need to keep processing this string until we get to an end
        // The end (for the number type) can be a space, or else any non-number character
        // If the end is a ".", then check to see if we have a variable method

        // First, find the end of the number
        Integer currentIndex = startIndex;
        if (currentIndex == stringToTransform.length()) {
            return currentIndex;
        }
        while (isNumber(stringToTransform.substring(currentIndex, currentIndex + 1))) {
            currentIndex++;
            if (currentIndex == stringToTransform.length()) {
                return currentIndex;
            }
        }

        // Check the first character after the number
        final String cAfterNumber = stringToTransform.substring(currentIndex, currentIndex + 1);
        if (!(cAfterNumber.equals("."))) {
            if (cAfterNumber.equals("%")) {
                return currentIndex + 1;
            }
            // - if it is not a ".", then return the end of the number
            return currentIndex;
        }

        // If there is a ".", then keep reading until either :
        while (true) {

            currentIndex++;
            if (currentIndex > stringToTransform.length() - 1) {
                return currentIndex;
            }
            char nextChar = stringToTransform.charAt(currentIndex);
            if (nextChar != '.' && !Character.isLetterOrDigit(nextChar)) {
                // or b) we come to another "." - in which case keep going and build up a new method name from the characters
                return currentIndex;
            }
        }
    }

    private static boolean isNumber(String substring) {
        return (substring.equals("0") ||
                substring.equals("1") ||
                substring.equals("2") ||
                substring.equals("3") ||
                substring.equals("4"))
                || substring.equals("5") || substring.equals("6") || substring.equals("7") || substring.equals("8")
                || substring.equals("9");
    }

    private static int getNextPercent(String stringToTransform, Integer startIndex) {
        // From startIndex, find the next grave. If it is prefixed by either ~ or \ then ignore it and find the next one
        return stringToTransform.indexOf("%", startIndex);
    }

    ModlValue transformString(String stringToTransform) {
        if (stringToTransform == null) {
            return null;
        }

        // 5: Replace the strings as per the txt document attached "string-replacement.txt"
        // Defer until after all other processing is complete.

        // Replace any unicode encodings
        stringToTransform = StringEscapeUtils.unescapeJava(stringToTransform);

        // Implement Elliott's algorithm for string transformation :
        // 1 : Find all parts of the sting that are enclosed in graves, e.g `test` where neither of the graves is prefixed with an escape character ~ (tilde) or \ (backslash).
        List<String> graveParts = getGravePartsFromString(stringToTransform);
        // [ 2: If no parts are found, run “Object Referencing detection” ] - basically go to 4:
        // 3 : If parts are found loop through them in turn:
        for (String gravePart : graveParts) {
            //     If a part begins with a % then run “Object Referencing”, else run “Punycode encoded parts”
            if (gravePart.startsWith("`%")) {
                //                stringToTransform = runObjectReferencing(gravePart, stringToTransform, true);
                ModlValue ret = runObjectReferencing(gravePart, stringToTransform, true);
                if (ret instanceof ModlObject.String) {
                    final String retStr = ((ModlObject.String) ret).string;
                    if (retStr.equals(stringToTransform)) {
                        stringToTransform = retStr;
                    } else {
                        stringToTransform = retStr;
                        String nonGravePart = gravePart.substring(1, gravePart.length() - 1);
                        stringToTransform = stringToTransform.replace(gravePart, nonGravePart);
                    }
                    stringToTransform = Util.degrave(stringToTransform);
                } else if (ret instanceof ModlObject.Number) {
                    if (gravePart.equals(stringToTransform)) {
                        return ret;
                    }
                    String number = ((ModlObject.Number) ret).number;
                    stringToTransform = stringToTransform.replace(gravePart, number);
                } else {
                    return ret;
                }
            } else {
                stringToTransform = Util.degrave(stringToTransform);
            }
        }
        // 4: Find all non-space parts of the string that are prefixed with % (percent sign). These are object references – run “Object Referencing”
        List<String> percentParts = getPercentPartsFromString(stringToTransform);
        for (String percentPart : percentParts) {
            ModlValue ret = runObjectReferencing(percentPart, stringToTransform, false);
            if (ret instanceof ModlObject.String) {
                final ModlObject.String theString = (ModlObject.String) ret;
                if (stringToTransform.equals(theString.string) &&
                        !stringToTransform.startsWith("%*") &&
                        stringToTransform.contains(".") &&
                        stringToTransform.contains("%") &&
                        stringToTransform.indexOf("%") == stringToTransform.lastIndexOf("%")) {
                    throw new RuntimeException("Interpreter Error: Cannot resolve reference in: \"" +
                            stringToTransform +
                            "\"");
                }
                stringToTransform = theString.string;
            } else if (ret instanceof ModlObject.Number) {
                if (percentPart.equals(stringToTransform)) {
                    return ret;
                }
                String number = ((ModlObject.Number) ret).number;
                stringToTransform = stringToTransform.replace(percentPart, number);
            } else {
                return ret;
            }
        }

        return new ModlObject.String(stringToTransform);
    }

    private List<String> getGravePartsFromString(String stringToTransform) {
        List<String> graveParts = new LinkedList<>();
        // Find all parts of the sting that are enclosed in graves, e.g `test` where neither of the graves is prefixed with an escape character ~ (tilde) or \ (backslash).
        int currentIndex = 0;
        boolean finished = false;
        while (!finished) {
            finished = true;
            Integer startIndex = getNextNonPrefixedGrave(stringToTransform, currentIndex);
            if (startIndex != null) {
                Integer endIndex = getNextNonPrefixedGrave(stringToTransform, startIndex + 1);
                if (endIndex != null) {
                    String gravePart = stringToTransform.substring(startIndex, endIndex + 1);
                    graveParts.add(gravePart);
                    currentIndex = endIndex + 1;
                    finished = false;
                }
            }
        }
        return graveParts;
    }

    private Integer getNextNonPrefixedGrave(String stringToTransform, Integer startIndex) {
        // From startIndex, find the next grave. If it is prefixed by either ~ or \ then ignore it and find the next one
        int index = stringToTransform.indexOf("`", startIndex);
        if (index == -1) {
            return null;
        }
        if (index > startIndex) {
            String prefix = stringToTransform.substring(index - 1, index);
            if (prefix.equals("~") || prefix.equals("\\")) {
                return getNextNonPrefixedGrave(stringToTransform, index + 1);
            } else {
                return index;
            }
        } else {
            return startIndex;
        }
    }

    ModlValue runObjectReferencing(String originalPercentPart, String stringToTransform, boolean isGraved) {
    /*
    Object Referencing
If the reference includes a . (dot / full stop / period) then the reference key should be considered everything to the left of the . (dot / full stop / period).

If the reference includes any part enclosed in [ and ] this is a deep object reference, see deep object referencing instructions and return back to this section.

Replace the reference key with the value of that key. We will call this the subject.

If there was a period in the original string, any part to the right of the first period (until the end of the part not including the end grave) is considered the method chain. Split the method chain by . (dot / full stop / period) and create an array from the methods.

Loop through the array and pass the subject to the named method, transforming the subject. Repeat the process (with the transformed subject) until all methods in the chain have been applied and the subject is fully transformed.

Replace the part originally found (including graves) with the transformed subject.
     */
        String percentPart = originalPercentPart;
        int startOffset = 1;
        int endOffset = 0;
        if (isGraved) {
            startOffset += 1;
            endOffset += 1;
        }
        if (percentPart.endsWith("%")) {
            percentPart = percentPart.substring(0, percentPart.length() - 1);
        }
        String subject = percentPart.substring(startOffset, percentPart.length() - endOffset);

        String remainder = null;
        int indexOfDot = percentPart.indexOf(".");
        if (indexOfDot != -1) {
            subject = percentPart.substring(startOffset, indexOfDot);
            remainder = percentPart.substring(indexOfDot + 1, percentPart.length() - endOffset);
        }

        if (subject.startsWith("`") && subject.endsWith("`") && subject.length() > 1) {
            subject = subject.substring(1, subject.length() - 1);
        } else {

            ModlValue value = getValueForReference(subject);
            if (remainder != null) {
                final String[] remainderHolder = {remainder};
                value = getValueForReferenceRecursive(value, remainderHolder);
                remainder = remainderHolder[0];
            }

            if (value == null) {
                return new ModlObject.String(stringToTransform);
                //            value = new ModlObject.String(subject);
            } else if (value instanceof ModlObject.String) {
                subject = ((ModlObject.String) value).string;
            } else if (value instanceof ModlObject.Number) {
                if (remainder == null) {
                    return value;
                }
                subject = ((ModlObject.Number) value).number;
            } else {
                return value;
            }
        }

        if (remainder != null) {
            subject = runMethods(subject, remainder);
        }

        if (isGraved) {
            originalPercentPart = originalPercentPart.substring(1, originalPercentPart.length() - 1);
            stringToTransform = stringToTransform.replaceFirst(originalPercentPart, subject);
        } else {
            stringToTransform = stringToTransform.replaceFirst(originalPercentPart, subject);
        }

        return new ModlObject.String(stringToTransform);
    }

    private String runMethods(String subject, String remainder) {
        String[] methods = remainder.split("\\.");
        if (methods.length == 0) {
            methods = new String[1];
            methods[0] = remainder;
        }
        boolean stalled = false;
        for (String method : methods) {
            if (!stalled) {
                if (method.contains("<")) {
                    // HANDLE TRIM AND REPLACE HERE!!
                    // We need to strip the "(<params>)" and apply the method to the subject AND the params!
                    // TODO (we might need to check for escaped "."s one day...
                    int startParamsIndex = method.indexOf("<");
                    String paramsString = method.substring(startParamsIndex + 1, method.length() - 1); //  - 1);
                    String methodString = method.substring(0, startParamsIndex);
                    subject = Interpreter.variableMethods.transform(methodString, subject + "," + paramsString);
                    remainder = null;
                } else {
                    if (!Interpreter.variableMethods.isVariableMethod(method)) {
                        // Nothing to do - leave it alone!
                        subject = subject + "." + method;
                        remainder = "";
                        stalled = true;
                    } else {
                        final Matcher matcher = VariableMethods.STRING.matcher(method);

                        if (matcher.find() && matcher.groupCount() > 0) {
                            final String match = matcher.group(0);
                            remainder = method.substring(match.length());
                            if (!match.equals(method)) {
                                method = match;
                            }
                            subject = Interpreter.variableMethods.transform(method, subject);
                        }
                    }
                }
            } else {
                remainder += "." + method;
            }
        }
        if (remainder != null) {
            subject = subject + remainder;
        }
        return subject;
    }

    private ModlValue getValueForReference(String subject) {
        // Subject might be a nested object reference, so handle it here
        int indexOfGreaterThanSymbol = subject.indexOf(".");
        boolean isNested = indexOfGreaterThanSymbol > -1;
        String remainder;
        if (isNested) {
            remainder = subject.substring(indexOfGreaterThanSymbol + 1);
            subject = subject.substring(0, indexOfGreaterThanSymbol);
        } else {
            remainder = null;
        }

        // Find the first level object reference, whether nested or not

        ModlValue value = null;
        boolean found = false;

        // Make any variable replacements, etc.
        for (Integer i = 0; i < numberedVariables.size(); i++) {
            if (subject.equals(i.toString())) {
                //                if (numberedVariables.get(i) instanceof ModlObject.String) {
                //                    value = numberedVariables.get(i);
                //                } else {
                value = numberedVariables.get(i);
                //                }
                //                if (remainder != null) {
                //                    indexOfGreaterThanSymbol = remainder.indexOf(".");
                //                    if (indexOfGreaterThanSymbol > -1) {
                //                        remainder = remainder.substring(indexOfGreaterThanSymbol + 1);
                //                        subject = remainder.substring(0, indexOfGreaterThanSymbol);
                //                    }
                //                } else {
                //                    subject = null;
                //                }
                found = true;
                break;
            }
        }
        if (!found) {
            for (Map.Entry<String, ModlValue> variableEntry : variables.entrySet()) {
                if (subject.equals(variableEntry.getKey())) {
                    value = variableEntry.getValue();
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            for (Map.Entry<String, ModlValue> variableEntry : valuePairs.entrySet()) {
                if (subject.equals(variableEntry.getKey()) || subject.equals("_" + variableEntry.getKey())) {
                    value = variableEntry.getValue();
                    break;
                }
            }
        }

        // If we have a nested reference follow it recursively until we find the value we need.
        if (value != null && isNested) {
            return getValueForReferenceRecursive(value, new String[]{remainder});
        }
        return value;
    }

    /**
     * For keys such as a>b>c>d>e, each call to this method takes the first part and uses it to find the referenced
     * object in the current ctx object, then calls itself with this new object as the context and the remaining part
     * of the nested object reference (b>c>d>e in this case) until all the parts of the reference are used up.
     *
     * @param ctx       The should contain a value for the given key
     * @param keyHolder The key of the object that we need - possibly a nested reference.
     * @return The value that was referenced, or a RuntimeException if the reference is invalid.
     */
    private ModlValue getValueForReferenceRecursive(ModlValue ctx, final String[] keyHolder) {

        String key = keyHolder[0];
        // Check for nested keys
        final int indexOfGreaterThanSymbol = key.indexOf(".");
        boolean isNested = indexOfGreaterThanSymbol > -1;
        final String remainder;
        String currentKey;
        if (isNested) {
            remainder = key.substring(indexOfGreaterThanSymbol + 1);
            currentKey = key.substring(0, indexOfGreaterThanSymbol);
        } else {
            remainder = null;
            keyHolder[0] = remainder;
            currentKey = key;
        }

        // Get the nested value via its name or number
        ModlValue newCtx;
        if (isNumber(currentKey)) {
            if (!(ctx instanceof ModlObject.Array)) {
                throw new RuntimeException("Object reference is numerical for non-Array value");
            }
            int index = Integer.parseInt(currentKey);
            newCtx = ctx.get(index);
        } else {
            currentKey = ((ModlObject.String) (transformString(currentKey))).string;
            if (ctx instanceof ModlObject.Pair) {
                if (!currentKey.equals(((ModlObject.Pair) ctx).getKey().string)) {
                    throw new RuntimeException("Object reference should match the key name for a Pair");
                }
                newCtx = ((ModlObject.Pair) ctx).getModlValue();
            } else {
                if (ctx instanceof ModlObject.Map || ctx instanceof ModlObject.Array) {
                    newCtx = ctx.get(currentKey);
                } else {
                    keyHolder[0] = key;
                    newCtx = ctx;
                    isNested = false;
                }
            }
        }

        // Recurse if we're still nested
        if (isNested) {
            keyHolder[0] = remainder;
            return getValueForReferenceRecursive(newCtx, keyHolder);
        } else {
            if (newCtx == null) {
                keyHolder[0] = currentKey;
            }
        }
        // Success, return the value we found.
        return newCtx;
    }
}
