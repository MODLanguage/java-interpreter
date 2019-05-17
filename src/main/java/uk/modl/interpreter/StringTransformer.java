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

import static java.lang.Character.isLetter;

//import java.util.function.Function;

public class StringTransformer {

    Map<String, ModlValue> valuePairs;
    Map<String, ModlValue> variables;
    Map<Integer, ModlValue> numberedVariables;


    public StringTransformer(Map<String, ModlValue> valuePairs,
                             Map<String, ModlValue> variables,
                             Map<Integer, ModlValue> numberedVariables) {
        this.valuePairs = valuePairs;
        this.variables = variables;
        this.numberedVariables = numberedVariables;
    }

    protected ModlValue transformString(String stringToTransform) {
        if (stringToTransform == null) {
            return null;
        }
        ModlObject modlObject = new ModlObject();
        if (stringToTransform.toLowerCase().equals("true")) {
            return modlObject.new True();
        }
        if (stringToTransform.toLowerCase().equals("false")) {
            return modlObject.new False();
        }

        // 5: Replace the strings as per the txt document attached "string-replacement.txt"
        stringToTransform = replaceEscapedStrings(stringToTransform);

        // Replace any unicode encodings
        stringToTransform = StringEscapeUtils.unescapeJava(stringToTransform);

        // Implement Elliott's algorithm for string transformation :
        // 1 : Find all parts of the sting that are enclosed in graves, e.g `test` where neither of the graves is prefixed with an escape character ~ (tilde) or \ (backslash).
        List<String> graveParts = getGravePartsFromString(stringToTransform);
        // [ 2: If no parts are found, run “Object Referencing detection” ] - basically go to 4:
        // 3 : If parts are found loop through them in turn:
        for (String gravePart : graveParts) {
            //     If a part begins with a % then run “Object Referencing”, else run “Punycode encoded parts”
            String newGravePart = null;
            if (gravePart.startsWith("`%")) {
//                stringToTransform = runObjectReferencing(gravePart, stringToTransform, true);
                ModlValue ret = runObjectReferencing(gravePart, stringToTransform, true);
                if (ret instanceof ModlObject.String) {
                    stringToTransform = ((ModlObject.String) ret).string;
                    String nonGravePart = gravePart.substring(1, gravePart.length() - 1);
                    stringToTransform = stringToTransform.replace(gravePart, nonGravePart);
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
                newGravePart = gravePart.substring(1, gravePart.length() - 1);
                stringToTransform = stringToTransform.replace(gravePart, newGravePart);
            }
        }
        // 4: Find all non-space parts of the string that are prefixed with % (percent sign). These are object references – run “Object Referencing”
        List<String> percentParts = getPercentPartsFromString(stringToTransform);
        for (String percentPart : percentParts) {
            ModlValue ret = runObjectReferencing(percentPart, stringToTransform, false);
            if (ret instanceof ModlObject.String) {
                stringToTransform = ((ModlObject.String) ret).string;
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

        return modlObject.new String(stringToTransform);
    }

    private List<String> getPercentPartsFromString(String stringToTransform) {
        // Find all non-space parts of the string that are prefixed with % (percent sign).
        List<String> percentParts = new LinkedList<>();
        int currentIndex = 0;
        boolean finished = false;
        while (!finished) {
            finished = true;
            Integer startIndex = getNextPercent(stringToTransform, currentIndex);
            if (startIndex != null) {
                Integer endIndex = null;
                // If the first character after the % is a number, then keep reading until we get to a non-number (taking account of transform chains)
                // If the first character after the % is a letter, then keep reading until we get to a space
                if (startIndex < stringToTransform.length() - 1 && !isNumber(stringToTransform.substring(startIndex + 1, startIndex + 2))) {
                    // Just read to the next space
                    int spaceEndIndex = stringToTransform.indexOf(" ", startIndex);
                    int colonEndIndex = stringToTransform.indexOf(":", startIndex);
                    if (spaceEndIndex == -1) {
                        spaceEndIndex = 99999;
                    }
                    if (colonEndIndex == -1) {
                        colonEndIndex = 99999;
                    }
                    endIndex = Math.min(spaceEndIndex, colonEndIndex);
                    if (endIndex > stringToTransform.length()) {
                        endIndex = stringToTransform.length();
                    }
                } else if (startIndex == stringToTransform.length() - 1) {
                    return percentParts;
                } else {
                    endIndex = getEndOfNumber(stringToTransform, startIndex + 1);
                }
                if (endIndex != null && endIndex != -1) {
                    if (endIndex > startIndex + 1) {
                        String gravePart = stringToTransform.substring(startIndex, endIndex);
                        percentParts.add(gravePart);
                        currentIndex = endIndex + 1;
                    }
                    finished = false;
                }
            }
        }
        return percentParts;
    }

    private Integer getEndOfNumber(String stringToTransform, Integer startIndex) {
        // We actually need to keep processing this string until we get to an end
        // The end (for the number type) can be a space, or else any non-number character
        // If the end is a ".", then check to see if we have a variable transform

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
        if (!(stringToTransform.substring(currentIndex, currentIndex + 1).equals(".")) &&
                !(stringToTransform.substring(currentIndex, currentIndex + 1).equals("."))) {
            // - if it is not a ".", then return the end of the number
            return currentIndex;
        }
        if ((stringToTransform.substring(currentIndex, currentIndex + 1).equals("."))) {
            return stringToTransform.length();
        }
        // If there is a ".", then keep reading until either :
        String newMethod = "";
        while (true) {

            currentIndex++;
            if (currentIndex > stringToTransform.length() - 1) {
                return currentIndex;
            }
            String nextChar = stringToTransform.substring(currentIndex, currentIndex + 1);
            if (nextChar.equals(".")) {
                // or b) we come to another "." - in which case keep going and build up a new transform name from the characters
                if (newMethod.length() > 0) {
                    newMethod = "";
                } else {
                    return currentIndex;
                }
            } else {
                //   a) we don't have any more variable methods that match the lengthened string
                if (!isLetter(nextChar.charAt(0)) && !isNumber(String.valueOf(nextChar.charAt(0)))) {
                    return currentIndex;
                }
                if (isVariableMethod(newMethod + nextChar)) {
                    newMethod += nextChar;
                } else {
                    if (newMethod.length() > 0) {
                        return currentIndex;
                    } else {
                        return currentIndex - 1; // cope with "."
                    }
                }
            }
        }
    }

    private boolean isVariableMethod(String s) {
        return VariableMethods.isVariableMethod(s);
    }

    private boolean isNumber(String substring) {
        if ((substring.equals("0") || substring.equals("1") || substring.equals("2") || substring.equals("3") || substring.equals("4"))
                || substring.equals("5") || substring.equals("6") || substring.equals("7") || substring.equals("8")
                || substring.equals("9")) {
            return true;
        }
        return false;
    }

    private Integer getNextPercent(String stringToTransform, Integer startIndex) {
        // From startIndex, find the next grave. If it is prefixed by either ~ or \ then ignore it and find the next one
        int index = stringToTransform.indexOf("%", startIndex);
        if (index == -1) {
            return null;
        }
        return index;
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

    private String replaceEscapedStrings(String stringToTransform) {
        // String-replacement.text to replace escaped characters
        return StringEscapeReplacer.replace(stringToTransform);
    }


    public ModlValue runObjectReferencing(String percentPart, String stringToTransform, boolean isGraved) {
    /*
    Object Referencing
If the reference includes a . (dot / full stop / period) then the reference key should be considered everything to the left of the . (dot / full stop / period).

If the reference includes any part enclosed in [ and ] this is a deep object reference, see deep object referencing instructions and return back to this section.

Replace the reference key with the value of that key. We will call this the subject.

If there was a period in the original string, any part to the right of the first period (until the end of the part not including the end grave) is considered the transform chain. Split the transform chain by . (dot / full stop / period) and create an array from the methods.

Loop through the array and pass the subject to the named transform, transforming the subject. Repeat the process (with the transformed subject) until all methods in the chain have been applied and the subject is fully transformed.

Replace the part originally found (including graves) with the transformed subject.
     */
        int startOffset = 1;
        int endOffset = 0;
        if (isGraved) {
            startOffset += 1;
            endOffset += 1;
        }
        ModlObject modlObject = new ModlObject();
        String subject = percentPart.substring(startOffset, percentPart.length() - endOffset);

        String remainder = null;
        int indexOfDot = percentPart.indexOf(".");
        if (indexOfDot != -1) {
            subject = percentPart.substring(startOffset, indexOfDot);
            remainder = percentPart.substring(indexOfDot + 1, percentPart.length() - endOffset);
        }

        ModlValue value = getValueForReference(subject);
        if (remainder != null) {
            final String[] remainderHolder = {remainder};
            value = getValueForReferenceRecursive(value, remainderHolder);
            remainder = remainderHolder[0];
        }

        if (value == null) {
            return modlObject.new String(stringToTransform);
//            value = modlObject.new String(subject);
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

        if (remainder != null) {
            String[] methods = remainder.split("\\.");
            if (methods.length == 0) {
                methods = new String[1];
                methods[0] = remainder;
            }
            for (String method : methods) {
                if (method.indexOf("(") >= 0) {
                    // HANDLE TRIM AND REPLACE HERE!!
                    // We need to strip the "(<params>)" and apply the transform to the subject AND the params!
                    // TODO (we might need to check for escaped "."s one day...
                    int startParamsIndex = method.indexOf("(");
                    String paramsString = method.substring(startParamsIndex + 1, method.length() - 1); //  - 1);
                    String methodString = method.substring(0, startParamsIndex);
                    subject = VariableMethods.transform(methodString, subject + "," + paramsString);

                } else {
                    if (!VariableMethods.isVariableMethod(method)) {
                        // Nothing to do - leave it alone!
                        subject = subject + "." + method;
                    } else {
                        subject = VariableMethods.transform(method, subject);
                    }
                }
            }
        }

        stringToTransform = stringToTransform.replace(percentPart, subject);

        return modlObject.new String(stringToTransform);
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
     * For keys such as a>b>c>d>e, each call to this transform takes the first part and uses it to find the referenced
     * object in the current ctx object, then calls itself with this new object as the context and the remaining part
     * of the nested object reference (b>c>d>e in this case) until all the parts of the reference are used up.
     *
     * @param ctx The should contain a value for the given key
     * @param keyHolder The key of the object that we need - possibly a nested reference.
     * @return The value that was referenced, or a RuntimeException if the reference is invalid.
     */
    private ModlValue getValueForReferenceRecursive(ModlValue ctx, final String[] keyHolder) {

        String key = keyHolder[0];
        // Check for nested keys
        final int indexOfGreaterThanSymbol = key.indexOf(".");
        final boolean isNested = indexOfGreaterThanSymbol > -1;
        final String remainder;
        String currentKey;
        if (isNested) {
            remainder = key.substring(indexOfGreaterThanSymbol + 1);
            currentKey = key.substring(0, indexOfGreaterThanSymbol);
        } else {
            remainder = null;
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
                newCtx = ctx.get(currentKey);
            }
        }

        // Recurse if we're still nested
        if (isNested) {
            keyHolder[0] = remainder;
            return getValueForReferenceRecursive(newCtx, keyHolder);
        } else if (newCtx == null) {
            // The currentKey number or name must be invalid.
            throw new RuntimeException("Invalid Object Reference: " + currentKey);
        }
        // Success, return the value we found.
        return newCtx;
    }
}
