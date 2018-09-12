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

package uk.modl.config;

import org.apache.commons.text.StringEscapeUtils;
import uk.modl.parser.ModlObject;

import java.net.IDN;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.lang.Character.isLetter;

public class StringTransformer {

    ModlClassLoader modlClassLoader;
    Map<String, String> stringPairs;
    Map<String, Map> mapPairs;
    Map<String, List> arrayPairs;
    Map<String, Function<String, String>> variableMethods;


    public StringTransformer(ModlClassLoader modlClassLoader, Map<String, String> stringPairs,
                             Map<String, Map> mapPairs, Map<String, List> arrayPairs,
                             Map<String, Function<String, String>> variableMethods) {
        this.modlClassLoader = modlClassLoader;
        this.stringPairs = stringPairs;
        this.arrayPairs = arrayPairs;
        this.mapPairs = mapPairs;
        this.variableMethods = variableMethods;
    }

    protected String transformString(String stringToTransform) {
        if (stringToTransform == null) {
            return null;
        }
//        System.out.println("Transforming : " + stringToTransform);

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
                stringToTransform = runObjectReferencing(gravePart, stringToTransform, true);
                String nonGravePart = gravePart.substring(1, gravePart.length() - 1);
                stringToTransform = stringToTransform.replace(gravePart, nonGravePart);
//                newGravePart  = runObjectReferencing(gravePart, stringToTransform, true, false);
            } else {
                // else run “Punycode encoded parts”
                newGravePart = replacePunycode(gravePart);
                stringToTransform = stringToTransform.replace(gravePart, newGravePart);
            }
        }
        // 4: Find all non-space parts of the string that are prefixed with % (percent sign). These are object references – run “Object Referencing”
        List<String> percentParts = getPercentPartsFromString(stringToTransform);
        for (String percentPart : percentParts) {
            stringToTransform = runObjectReferencing(percentPart, stringToTransform, false);
        }
        // 5: Replace the strings as per the txt document attached "string-replacement.txt"
        stringToTransform = replaceEscapedStrings(stringToTransform);

//        System.out.println("Transformed : " + stringToTransform);
        return stringToTransform;
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
                // If the first character after the % is a number, then keep reading until we get to a non-number (taking account of method chains)
                // If the first character after the % is a letter, then keep reading until we get to a space
                if (startIndex < stringToTransform.length() - 1 && !isNumber(stringToTransform.substring(startIndex +1, startIndex + 2))) {
                    // Just read to the next space
                    int spaceEndIndex = stringToTransform.indexOf(" ", startIndex);
                    int colonEndIndex = stringToTransform.indexOf(":", startIndex);
                    if (spaceEndIndex == -1 ){
                        spaceEndIndex = 99999;
                    }
                    if (colonEndIndex == -1 ){
                        colonEndIndex = 99999;
                    }
                    endIndex = Math.min(spaceEndIndex, colonEndIndex);
                    if (endIndex > stringToTransform.length()) {
                        endIndex = stringToTransform.length();
                    }
                } else if (startIndex == stringToTransform.length() -1 ){
                    return percentParts;
                }
                else {
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
        // If the end is a ".", then check to see if we have a variable method

        // First, find the end of the number
        Integer currentIndex = startIndex;
        if (currentIndex == stringToTransform.length() ) {
            return currentIndex;
        }
        while (isNumber(stringToTransform.substring(currentIndex, currentIndex + 1))) {
            currentIndex++;
            if (currentIndex == stringToTransform.length()) {
                return currentIndex;
            }
        }

        // Check the first character after the number
        if (!(stringToTransform.substring(currentIndex, currentIndex + 1).equals("."))) {
            // - if it is not a ".", then return the end of the number
            return currentIndex;
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
                // or b) we come to another "." - in which case keep going and build up a new method name from the characters
                if (newMethod.length() > 0) {
                    newMethod = "";
                } else {
                    return currentIndex;
                }
            } else {
                //   a) we don't have any more variable methods that match the lengthened string
                if (!isLetter(nextChar.charAt(0))) {
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
        for (Map.Entry<String, Function<String, String>> entry : variableMethods.entrySet()) {
            if (s.equals(entry.getKey())) {
                return true;
            }
        }
        return false;
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


    private String replacePunycode(String stringToTransform) {
        // Prefix it with xn— (the letters xn and two dashes) and decode using punycode / IDN library. Replace the full part (including graves) with the decoded value.
        if (stringToTransform == null) {
            return stringToTransform;
        }
        if (stringToTransform.startsWith("`") && stringToTransform.endsWith("`")) {
            stringToTransform = stringToTransform.substring(1, stringToTransform.length() -1);
            String originalString = stringToTransform;
            stringToTransform = "xn--" + stringToTransform;
            String newStringToTransform = IDN.toUnicode(stringToTransform);
            if (newStringToTransform.equals(stringToTransform)) {
                stringToTransform = originalString;
            } else {
                stringToTransform = newStringToTransform;
            }
        }
        return stringToTransform;
    }


    public String runObjectReferencing(String percentPart, String stringToTransform, boolean isGraved) {
    /*
    Object Referencing
If the reference includes a . (dot / full stop / period) then the reference key should be considered everything to the left of the . (dot / full stop / period).

If the reference includes any part enclosed in [ and ] this is a deep object reference, see deep object referencing instructions and return back to this section.

Replace the reference key with the value of that key. We will call this the subject.

If there was a period in the original string, any part to the right of the first period (until the end of the part not including the end grave) is considered the method chain. Split the method chain by . (dot / full stop / period) and create an array from the methods.

Loop through the array and pass the subject to the named method, transforming the subject. Repeat the process (with the transformed subject) until all methods in the chain have been applied and the subject is fully transformed.

Replace the part originally found (including graves) with the transformed subject.
     */
    int startOffset = 1;
    int endOffset = 0;
    if (isGraved) {
        startOffset = 2;
        endOffset = 1;
    }
        String subject = percentPart.substring(startOffset, percentPart.length() - endOffset);

        String methodChain = null;
        int indexOfDot = percentPart.indexOf(".");
        if (indexOfDot != -1) {
            subject = percentPart.substring(startOffset, indexOfDot);
            methodChain = percentPart.substring(indexOfDot + 1, percentPart.length() - endOffset);
        }
        String oldSubject = subject;
        subject = getValueForReference(subject);
        if (subject == null) {
                return stringToTransform;
        }

        if (methodChain != null) {
            String[] methods = methodChain.split("\\.");
            if (methods.length == 0) {
                methods = new String[1];
                methods[0] = methodChain;
            }
            for (String method : methods) {
                if (method.indexOf("(") >= 0) {
                    // HANDLE TRIM AND REPLACE HERE!!
                    // We need to strip the "(<params>)" and apply the method to the subject AND the params!
                    // TODO (we might need to check for escaped "."s one day...
                    int startParamsIndex = method.indexOf("(");
                    String paramsString = method.substring(startParamsIndex + 1, method.length()-1); //  - 1);
                    String methodString = method.substring(0, startParamsIndex);
                    subject = variableMethods.get(methodString).apply(subject + "," + paramsString);

                } else {
                    if (variableMethods.get(method) == null) {
                        // Nothing to do - leave it alone!
                        subject = subject + "." + method;
                    } else {
                        subject = variableMethods.get(method).apply(subject);
                    }
                }
            }
        }

        stringToTransform = stringToTransform.replace(percentPart, subject);

        return stringToTransform;
    }

    private String getValueForReference(String subject) {
        String value = subject;
        boolean found = false;

        // Make any variable replacements, etc.
        for (Integer i = 0; i < modlClassLoader.numberedVariables.size(); i++) {
            if (subject.equals(i.toString())) {
                if (modlClassLoader.numberedVariables.get(i) instanceof String) {
                    value = (String) modlClassLoader.numberedVariables.get(i);
                } else {
                    value = ((ModlObject.Value) modlClassLoader.numberedVariables.get(i)).getString().string;
                }
                found = true;
            }
        }
        for (Map.Entry<String, String> variableEntry : modlClassLoader.variables.entrySet()) {
            if (subject.equals(variableEntry.getKey())) {
                value = variableEntry.getValue();
                found = true;
            }
        }
        for (Map.Entry<String, String> variableEntry : stringPairs.entrySet()) {
            if (subject.equals(variableEntry.getKey())) {
                value = variableEntry.getValue();
                found = true;
            }
        }
        for (Map.Entry<String, Map> variableEntry : mapPairs.entrySet()) {
            if (subject.equals(variableEntry.getKey())) {
//                value = variableEntry.getValue();
                found = true;
            }
        }
        for (Map.Entry<String, List> variableEntry : arrayPairs.entrySet()) {
            if (subject.equals(variableEntry.getKey())) {
//                value = variableEntry.getValue();
                found = true;
            }
        }

        if (!found) {
            return null;
        }
        return value;
    }
}
