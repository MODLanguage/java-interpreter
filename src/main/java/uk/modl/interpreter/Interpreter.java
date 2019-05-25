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

import org.apache.commons.lang3.tuple.ImmutablePair;
import uk.modl.modlObject.ModlObject;
import uk.modl.modlObject.ModlObjectTreeWalker;
import uk.modl.modlObject.ModlValue;
import uk.modl.parser.ModlObjectCreator;
import uk.modl.parser.RawModlObject;
import uk.modl.parser.printers.JsonPrinter;

import java.io.IOException;
import java.util.*;

import static uk.modl.parser.ModlObjectCreator.MODL_VERSION;

public class Interpreter {

    public static final int MAX_CLASS_HIERARCHY_DEPTH = 50;
    public static VariableMethods variableMethods = null;
    private Map<String, Map<String, Object>> klasses = new LinkedHashMap<>();
    private Map<String, ModlValue> variables = new HashMap<>();
    private Map<Integer, ModlValue> numberedVariables = new HashMap<>();
    // Store any uppercase instructions we've seen, so we know not to allow them again
    private Set<String> uppercaseInstructions = new HashSet<>();
    private List<String> loadedFiles = new ArrayList<>();
    private List<VariableMethodLoader.MethodDescriptor> methodList = new ArrayList<>();
    private Set<String> pairNames; // TODO Get rid of this!
    private Map<String, ModlValue> valuePairs;
    private List<String> PRIMITIVES = Arrays.asList("num", "str", "map", "arr", "bool", "null");

    public static String parseToJson(String input) throws IOException {
        ModlObject modlObject = interpret(input);

        return JsonPrinter.printModl(modlObject);

    }

    public static ModlObject interpret(String input) throws IOException {
        RawModlObject rawModlObject = ModlObjectCreator.processModlParsed(input);

        return interpret(rawModlObject);
    }

    public static ModlObject interpret(RawModlObject rawModlObject) {
        Interpreter interpreter = new Interpreter();
        variableMethods = new VariableMethods();
        ModlObject modlObject = null;
        while (modlObject == null) {
            try {
                modlObject = interpreter.interpretPrivate(rawModlObject);
            } catch (RequireRestart r) {
                // OK - let's try again with the updated rawModlObject}
            }
        }

        final ModlObjectTreeWalker walker = new ModlObjectTreeWalker(modlObject);
        walker.walk(new ModlObjectTreeWalker.Visitor() {
            @Override public void visit(final Object v) {
                if (v instanceof ModlObject.String) {
                    final ModlObject.String str = (ModlObject.String) v;
                    str.string = StringEscapeReplacer.replace(str.string);
                }
            }
        });
        return modlObject;
    }

    private static List<ImmutablePair<RawModlObject.ConditionTest, String>> getOrderedConditionalTestList(RawModlObject.ConditionGroup conditionGroup) {
        List<ImmutablePair<RawModlObject.ConditionTest, String>> orderedConditionalTestList = new LinkedList<>();
        int nullCount = 0;
        for (ImmutablePair<RawModlObject.ConditionTest, String> conditionalTestEntry : conditionGroup.getConditionsTestList()) {
            // Work out where this should be in the list
            // There are only & and | and null here!
            String operator = conditionalTestEntry.getValue();
            if (operator == null) {
                orderedConditionalTestList.add(nullCount++, conditionalTestEntry);
            } else {
                if (operator.equals("|")) {
                    orderedConditionalTestList.add(conditionalTestEntry);
                } else if (operator.equals("&")) {
                    orderedConditionalTestList.add(nullCount, conditionalTestEntry);
                }
            }
        }
        return orderedConditionalTestList;
    }

    private static void validatePairKey(final String newKey) {
        boolean keyIsAllDigits = true;
        char invalidCharacter = '\0';
        int i = 0;
        for (char c : newKey.toCharArray()) {
            if (!Character.isDigit(c)) {
                if (i == 0 && c == '_') {
                    continue;
                }
                keyIsAllDigits = false;
            }
            if ("!$@-+'#^*£&".contains("" + c)) {
                if (i != 0 || c != '*') {
                    invalidCharacter = c;
                }
            }
            i++;
        }
        if (keyIsAllDigits) {
            throw new RuntimeException("Entirely numeric keys are not allowed: " + newKey);
        }
        if (invalidCharacter != '\0') {
            throw new RuntimeException("Interpreter Error: Invalid key - '" +
                                       invalidCharacter +
                                       "' character not allowed: " +
                                       newKey);
        }
    }

    private ModlObject interpretPrivate(RawModlObject rawModlObject) throws RequireRestart {
        ModlObject modlObject = new ModlObject();
        //ModlClassLoader.loadModlKlassO(rawModlObject, klasses);
        pairNames = new HashSet<>();
        valuePairs = new HashMap<>();
        String versionString = null;
        boolean versionNumberIsWrong = false;

        // Interpret rawModlObject based on specified config files
        boolean needRestart = false;
        RawModlObject loadedRawModlObject = null;
        String importFileValue = null;
        try {
            int i = 0;
            for (RawModlObject.Structure rawStructure : rawModlObject.getStructures()) {

                if (rawStructure instanceof ModlObject.Pair && (((ModlObject.Pair) rawStructure).getKey() != null &&
                                                                (((ModlObject.Pair) rawStructure).getKey().string !=
                                                                 null &&
                                                                 (((ModlObject.Pair) rawStructure).getKey().string.toLowerCase()
                                                                                                                  .equals(
                                                                                                                      "*v") ||
                                                                  (((ModlObject.Pair) rawStructure).getKey().string.toLowerCase()
                                                                                                                   .equals(
                                                                                                                       "*version")))))) {
                    addToUpperCaseInstructions(((ModlObject.Pair) rawStructure).getKey().string);
                    // This is the version number - check it and then ignore it
                    versionString = ((ModlObject.Number) ((ModlObject.Pair) rawStructure).getModlValue()).number;
                    float versionNumber = Float.valueOf(versionString);
                    if (versionNumber < 1.0 || versionNumber != Math.abs(versionNumber)) {
                        throw new RuntimeException("Interpreter Error: Invalid MODL version: " + versionString);
                    }
                    if (!(versionString.equals(String.valueOf(MODL_VERSION)))) {
                        versionNumberIsWrong = true;
                    }
                    if (i != 0) {
                        throw new RuntimeException(
                            "Interpreter Error: MODL version should be on the first line if specified.");
                    }
                    continue;
                }
                if (rawStructure instanceof ModlObject.Pair && (((ModlObject.Pair) rawStructure).getKey() != null &&
                                                                (((ModlObject.Pair) rawStructure).getKey().string !=
                                                                 null &&
                                                                 (((ModlObject.Pair) rawStructure).getKey().string.toLowerCase()
                                                                                                                  .equals(
                                                                                                                      "*l") ||
                                                                  (((ModlObject.Pair) rawStructure).getKey().string.toLowerCase()
                                                                                                                   .equals(
                                                                                                                       "*load")))))) {
                    addToUpperCaseInstructions(((ModlObject.Pair) rawStructure).getKey().string);

                    // Load in the config file specified by the "l" object
                    if (((ModlObject.Pair) rawStructure).getModlValue() instanceof ModlObject.String) {
                        importFileValue = ((ModlObject.String) ((ModlObject.Pair) rawStructure).getModlValue()).string;
                        loadedRawModlObject = loadConfigFile(importFileValue);
                        needRestart = true;
                        break;
                    } else if (((ModlObject.Pair) rawStructure).getModlValue() instanceof ModlObject.Number) {
                        importFileValue =
                            ((ModlObject.Number) ((ModlObject.Pair) rawStructure).getModlValue()).number;
                        loadedRawModlObject = loadConfigFile(importFileValue);
                        needRestart = true;
                        break;
                    }
                    continue;
                }
                if (rawStructure instanceof ModlObject.Pair && ((ModlObject.Pair) rawStructure).getKey() != null &&
                    ((ModlObject.Pair) rawStructure).getKey().string != null &&
                    ((((((ModlObject.Pair) rawStructure).getKey().string.toLowerCase().equals("*class")) ||
                       (((ModlObject.Pair) rawStructure).getKey().string.toLowerCase().equals("*c"))) || (
                          (((ModlObject.Pair) rawStructure).getKey().string.toLowerCase().equals("*m"))) ||
                      ((((ModlObject.Pair) rawStructure).getKey().string.toLowerCase().equals("*method"))) ||
                      (((ModlObject.Pair) rawStructure).getKey().string.toLowerCase().equals("?"))
                    ))) {
                    ModlObject.Pair pair = (ModlObject.Pair) rawStructure;
                    if (pair.getKey().string.toLowerCase().equals("*class") ||
                        pair.getKey().string.toLowerCase().equals("*c")) {
                        addToUpperCaseInstructions(pair.getKey().string);
                        ModlClassLoader.loadClass(rawStructure, klasses, this);
                        continue;
                    } else if (pair.getKey().string.toLowerCase().equals("*method") ||
                               pair.getKey().string.toLowerCase().equals("*m")) {
                        addToUpperCaseInstructions(pair.getKey().string);
                        VariableMethodLoader.loadVariableMethod(methodList, pair, this);
                    } else if (pair.getKey().string.equals("?")) {
                        VariableLoader.loadConfigNumberedVariables(pair.getModlValue(), numberedVariables);
                    } else {
                        if (pair.getKey().string.startsWith("_")) {
                            VariableLoader.loadConfigVar(pair.getKey().string.replaceFirst("_", ""), pair, variables);
                        } else if (pair.getKey().string.startsWith("*")) {
                            throw new RuntimeException("Unrecognised configuration instruction : " + pair.getKey());
                        }
                    }
                } else if (!(rawStructure instanceof ModlObject.Pair &&
                             ((ModlObject.Pair) rawStructure).getKey() != null &&
                             ((ModlObject.Pair) rawStructure).getKey().string != null &&
                             (((ModlObject.Pair) rawStructure).getKey().string.toLowerCase().equals("*v") ||
                              (((ModlObject.Pair) rawStructure).getKey().string.toLowerCase().equals("*version"))))) {
                    if (rawStructure instanceof ModlObject.Pair && ((ModlObject.Pair) rawStructure).getKey() != null &&
                        ((ModlObject.Pair) rawStructure).getKey().string != null &&
                        (((ModlObject.Pair) rawStructure).getKey().string.startsWith("*"))) {
                        throw new RuntimeException("Unrecognised configuration instruction : " +
                                                   ((ModlObject.Pair) rawStructure).getKey().string);
                    }
                } else if (rawStructure instanceof ModlObject.Pair &&
                           ((ModlObject.Pair) rawStructure).getKey() != null &&
                           ((ModlObject.Pair) rawStructure).getKey().string != null &&
                           (((ModlObject.Pair) rawStructure).getKey().string.startsWith("*"))) {
                    throw new RuntimeException("Unrecognised configuration instruction : " +
                                               ((ModlObject.Pair) rawStructure).getKey().string);
                }
                List<ModlObject.Structure> structures = interpret(modlObject, rawStructure);
                if (structures != null) {
                    for (ModlObject.Structure structure : structures) {
                        modlObject.addStructure(structure);
                    }
                }
                i++;
            }

            if (needRestart) {
                rawModlObject.replaceFirstImport(importFileValue, loadedRawModlObject);
                throw new RequireRestart();
            } else {
                return modlObject;
            }
        } catch (final Exception e) {
            if (versionNumberIsWrong) {
                throw new UnsupportedOperationException("Can't handle MODL version " + versionString);
            } else
                throw e;
        }
    }

    void addToUpperCaseInstructions(String string) {
        if (uppercaseInstructions.contains(string.toUpperCase())) {
            throw new RuntimeException("Already defined " + string + " as final!");
        }
        if (string.toUpperCase().equals(string)) {
            uppercaseInstructions.add(string);
        }
    }

    private RawModlObject loadConfigFile(String location) {
        // We no longer keep configs around - they can be built up dynamically for each new record that comes in
        // Load the config file!
        Object value = transformString(location);
        if (value instanceof ModlObject.String) {
            location = ((ModlObject.String) value).string;
        } else {
            throw new RuntimeException("Was expecting String for location, but got " + value.getClass());
        }
        return FileLoader.loadFile(loadedFiles, location);
    }

    private List<ModlObject.Structure> interpret(ModlObject modlObject, RawModlObject.Structure rawStructure) {

        if (rawStructure == null) {
            return null;
        }
        List<ModlObject.Structure> structures = new LinkedList<>();
        if (!(rawStructure instanceof RawModlObject.TopLevelConditional)) {
            ModlObject.Structure structure;
            if (rawStructure instanceof ModlObject.Map) {
                structure = interpret(modlObject, (ModlObject.Map) rawStructure, null);
                if (structure != null) {
                    structures.add(structure);
                }
            }

            if (rawStructure instanceof ModlObject.Array) {
                structure = interpret(modlObject, (ModlObject.Array) rawStructure, null);
                if (structure != null) {
                    structures.add(structure);
                }
            }

            if (rawStructure instanceof ModlObject.Pair) {
                structure = interpret(modlObject, (ModlObject.Pair) rawStructure, null);
                ModlObject.Pair pair = (ModlObject.Pair) structure;
                if (pair != null && pair.getKey() != null && pair.getKey().string != null) {
                    if (!pair.getKey().string.startsWith("_") &&
                        !pair.getKey().string.startsWith("*") &&
                        !pair.getKey().string.equals("?")) {
                        structures.add(pair);
                    }
                }
            }
        } else {
            structures = (interpret(modlObject, (RawModlObject.TopLevelConditional) rawStructure));
        }
        return structures;

    }

    private ModlObject.Pair interpret(ModlObject modlObject, RawModlObject.Pair rawPair, Object parentPair) {
        return interpret(modlObject, rawPair, parentPair, true);
    }

    private ModlObject.Pair interpret(ModlObject modlObject,
                                      RawModlObject.Pair rawPair,
                                      Object parentPair,
                                      boolean addToValuePairs) {
        if (rawPair == null) {
            return null;
        }
        if (rawPair.getKey() != null && rawPair.getKey().string != null) {
            if (rawPair.getKey().string.equals("?")) {
                numberedVariables = new HashMap<>();
                VariableLoader.loadConfigNumberedVariables(rawPair.getModlValue(), numberedVariables);
                return null;
            }
            if (rawPair.getKey().string.contains("%") || rawPair.getKey().string.contains("`.")) {
                StringTransformer stringTransformer = new StringTransformer(valuePairs, variables, numberedVariables);
                ModlValue result =
                    stringTransformer.transformString(rawPair.getKey().string);

                final String newString = result.toString();
                if (newString.matches("^[0-9]*$")) {
                    throw new RuntimeException("Pair name " + rawPair.getKey().string + " is illegal");
                }
                rawPair.setKey(new ModlObject.String(newString));
            }
        }

        ModlObject.Pair pair = new ModlObject.Pair();

        if (rawPair.getKey() == null) {
            return null;
        }

        String originalKey = rawPair.getKey().string;
        if (originalKey == null) {
            throw new RuntimeException("Interpreter Error: Invalid key - 'null'");
        }
        String newKey = originalKey;
        if (haveModlClass(originalKey)) {
            newKey = transformKey(originalKey);
            rawPair = transformValue(rawPair);
        }


        if (newKey != null) {
            // We might now have a de-referenced key this is all numeric so catch it here.
            validatePairKey(newKey);

            // IF WE ALREADY HAVE A PAIR WITH THIS NAME, AND THE NAME IS UPPER-CASE, THEN RAISE AN ERROR
            if (newKey.toUpperCase().equals(newKey)) {
                if (pairNames.contains(newKey) && addToValuePairs) {
                    throw new RuntimeException(newKey + " can't be defined again as upper-case keys are immutable");
                }
            }
        }
        if (!pairNames.contains("_" + newKey) && newKey != null) {
            if (parentPair == null && !(newKey.startsWith("%")) && addToValuePairs) {
                pairNames.add(newKey);
            }
            transformPairKey(modlObject, rawPair, newKey, parentPair);
        }
        if (newKey != null && (newKey.startsWith("_") || (newKey.startsWith("*")) || newKey.equals("?"))) {
            return null;
        }

        //  A pair with a key that matches a class ID or class name is transformed according to the class definition:
        // TODO Should be able to look up by transformed name too?
        if (haveModlClass(originalKey)) {
            // The key of the pair is set to the class name.
            // The value of the original standard pair is given the key value in the new map pair
            if (generateModlClassObject(modlObject, rawPair, pair, originalKey, newKey, parentPair))
                return pair; // TODO In all cases?


        }

        pair.setKey(new ModlObject.String(newKey));

        if (pair.getModlValue() instanceof ModlObject.Array) {
            for (ModlValue value : ((ModlObject.Array) (rawPair.getModlValue())).getValues()) {
                addValueFromPair(modlObject, rawPair, parentPair, pair, value);
            }
        } else {
            addValueFromPair(modlObject, rawPair, parentPair, pair, rawPair.getModlValue());
        }

        return pair;
    }

    private void addValueFromPair(ModlObject modlObject,
                                  ModlObject.Pair rawPair,
                                  Object parentPair,
                                  ModlObject.Pair pair,
                                  ModlValue value) {
        // Is this a variable prefixed by "%"?
        if (value instanceof ModlObject.Pair
            && ((ModlObject.Pair) value).getKey().string.startsWith("%")) {
            String key = ((ModlObject.Pair) value).getKey().string;
            // If so, then look up the reference!!
            ModlValue newValue = null;
            if (pairNames.contains(key.replaceFirst("%", "_"))) {
                ModlValue storedValue = valuePairs.get(key.replaceFirst("%", ""));
                if (storedValue instanceof ModlObject.Map) {
                    pair.addModlValue(rawPair.getModlValue());
                    return;
                } else if (storedValue instanceof ModlObject.Array) {
                    List<ModlValue> list = ((ModlObject.Array) storedValue).getValues();
                    int index;
                    if (((ModlObject.Pair) value).getModlValue() instanceof ModlObject.Number) {
                        index =
                            Integer.valueOf(((ModlObject.Number) (((ModlObject.Pair) value).getModlValue())).number);
                    } else {
                        index =
                            Integer.valueOf(((ModlObject.Number) ((ModlObject.Pair) value).getModlValue()
                                                                                          .get(0)).number);
                    }
                    newValue = (list.get(index));
                }
            } else {
                newValue = (transformString(((ModlObject.Pair) value).getKey().string));
            }
            // Now go through the object reference for value.getModlValue() until we are at the end of it!
            //            newValue = runDeepReference((ModlObject.Pair) value, newValue);
            pair.addModlValue(newValue);
        } else {
            pair.addModlValue(interpret(modlObject, value, parentPair));
        }
    }

    private boolean generateModlClassObject(ModlObject modlObject, RawModlObject.Pair rawPair, ModlObject.Pair pair,
                                            String originalKey, String newKey, Object parentPair) {
        pair.setKey(new ModlObject.String(newKey));

        int numParams = 0;
        if (rawPair.getModlValue() != null) {
            if ((rawPair.getModlValue() instanceof ModlObject.Array) ||
                rawPair.getModlValue() == null ||
                !(rawPair.getModlValue() instanceof ModlObject.Map)) {
                if (rawPair.getModlValue() instanceof ModlObject.Array) {
                    numParams = ((ModlObject.Array) (rawPair.getModlValue())).getValues().size();
                } else {
                    numParams = 1;
                }
            }
        }
        numParams = getNumParams(rawPair, numParams);
        String paramsKeyString = "*params" + numParams;
        Object obj = findParamsObject(rawPair, paramsKeyString);
        if (obj == null && rawPair.getModlValue() instanceof ModlObject.Array && hasAssignStatement(1, originalKey)) {
            throw new RuntimeException(
                "Interpreter Error: No key list of the correct length in class t - looking for one of length " +
                numParams);
        }

        boolean hasParams = (obj != null);

        // If it's not already a map pair, and one of the parent classes in the class hierarchy includes pairs, then it is transformed to a map pair.
        if (anyClassContainsPairs(1, originalKey) || mapPairAlready(rawPair) || (hasParams)) {
            pair.setKey(new ModlObject.String(newKey)); // TODO Do we need to do this again here?!

            List<ModlObject.Pair> pairs = null;
            boolean wasArray = false;

            if (rawPair.getModlValue() instanceof ModlObject.Array) {
                // TODO They are not necessarily pairs!!!
                // TODO But they will _become_ pairs when paired up with the ModlClass
                try {
                    pairs = getPairsFromArray(modlObject, (ModlObject.Array) (rawPair.getModlValue()), parentPair);
                    if (pairs.size() > 0) {
                        wasArray = true;
                    } else {
                        pairs = null;
                    }
                } catch (Exception e) {
                    // Ignore - leaving wasArray as false.
                }
            }

            if (mapPairAlready(rawPair)) {
                pairs = new LinkedList<>();
                addMapItemsToPair(modlObject,
                                  ((ModlObject.Map) (rawPair.getModlValue())).getPairs(),
                                  pairs,
                                  parentPair);
            }

            if (pairs != null) {
                // Make all the new map values in the new map pair
                makeNewMapPair(pair, pairs, wasArray);
            }
            if ((!(rawPair.getModlValue() instanceof ModlObject.Pair)) &&
                (!(rawPair.getModlValue() instanceof ModlObject.Map))) {
                if (!hasParams) {
                    // Don't need a pair here - continue
                    ModlValue value = interpret(modlObject, rawPair.getModlValue(), parentPair);
                    pair.addModlValue(value);
                } else {
                    int paramNum = 0;
                    @SuppressWarnings("unchecked")
                    List<ModlValue> params = (List<ModlValue>) obj;
                    String currentClass = null;


                    List<ModlValue> values = new LinkedList<>();
                    ModlValue pairVal = rawPair.getModlValue();
                    if (pairVal instanceof ModlObject.Array) {
                        for (ModlValue vl : ((ModlObject.Array) pairVal).getValues()) {
                            if (vl instanceof RawModlObject.ArrayConditional) {
                                List<ModlValue>
                                    vs =
                                    interpret(modlObject, (RawModlObject.ArrayConditional) vl, parentPair);
                                if (vs != null) {
                                    values.addAll(vs);
                                }
                            } else {
                                values.add(vl);
                            }
                        }
                    } else {
                        values.add(pairVal);
                    }
                    for (ModlValue valueItem : values) {

                        // How about checking if the valueItem has more than one valuitem?
                        // If it does, then make it a pair, and set the key to the currentClass
                        if (params.get(paramNum) instanceof ModlObject.String) {
                            currentClass = ((ModlObject.String) params.get(paramNum)).string;
                        }

                        if (valueItem instanceof ModlObject.Array) {
                            // Get the class which we're interested in, and go through the different entries
                            Object modlClassObj = getModlClass(currentClass);
                            if (modlClassObj == null) {
                                addNewClassParamValue(modlObject, pair, parentPair, currentClass, valueItem);
                            } else {
                                int innerParamNum = 0;
                                ModlObject.Pair innerPair = new ModlObject.Pair();
                                ModlObject.Pair valuePair = new ModlObject.Pair();
                                String fullClassName = currentClass; // TODO GET THIS FROM THE MODLOBJ!!
                                try {
                                    String
                                        nameString =
                                        ((ModlObject.String) getModlClass(currentClass).get("*name")).string;
                                    if (nameString == null) {
                                        nameString =
                                            ((ModlObject.String) getModlClass(currentClass).get("*n")).string;
                                    }
                                    fullClassName = nameString;
                                } catch (Exception e) {
                                    // Ignore
                                }
                                valuePair.setKey(new ModlObject.String(fullClassName));
                                for (ModlValue vi : ((ModlObject.Array) valueItem).getValues()) {

                                    Map<String, Object> modlClassMap = (getModlClass(currentClass));
                                    String superclass = ((String) (modlClassMap.get("*superclass")));
                                    if (superclass.equals("arr")) {
                                        ModlValue v = interpret(modlObject, vi, parentPair);
                                        valuePair.addModlValue(v);
                                    } else if (superclass.equals("map")) {
                                        @SuppressWarnings("unchecked")
                                        ModlObject.String
                                            innerClassName =
                                            ((ModlObject.String) (((LinkedHashMap<String,
                                                LinkedList<ModlValue>>) modlClassObj).get(paramsKeyString)
                                                                                     .get(innerParamNum++)));

                                        RawModlObject.Pair newRawPair = new ModlObject.Pair();
                                        newRawPair.setKey(innerClassName);
                                        newRawPair.addModlValue(vi);

                                        ModlValue v = interpret(modlObject, newRawPair, parentPair);

                                        //  And add it to the pair
                                        valuePair.addModlValue(v);
                                        innerPair.addModlValue(valuePair);
                                    } else {
                                        throw new RuntimeException("Superclass " +
                                                                   superclass +
                                                                   " of " +
                                                                   fullClassName +
                                                                   " is not known!");
                                    }
                                }
                                pair.addModlValue(valuePair);
                            }
                        } else {
                            addNewClassParamValue(modlObject, pair, parentPair, currentClass, valueItem);
                        }


                        paramNum++;
                    }
                }
                addAllParentPairs(modlObject, pair, originalKey);
                return true;
            }
            addAllParentPairs(modlObject, pair, originalKey);
            return true;
        }
        return false;
    }

    private Object findParamsObject(final ModlObject.Pair rawPair, final String paramsKeyString) {
        final Map<String, Object> klass = getModlClass(rawPair.getKey().string);
        Object result = klass.get(paramsKeyString);

        // Search up the class hierarchy to find a params object of the right length
        if (result == null) {
            String superclass = (String) klass.get("*superclass");
            while (superclass != null) {
                final Map<String, Object> sk = getModlClass(superclass);
                if (sk != null) {
                    result = sk.get(paramsKeyString);
                    if (result != null) {
                        break;
                    }
                    superclass = (String) sk.get("*superclass");
                } else {
                    superclass = null;
                }
            }
        }
        return result;
    }

    private void addNewClassParamValue(ModlObject modlObject,
                                       ModlObject.Pair pair,
                                       Object parentPair,
                                       String currentClass,
                                       ModlValue valueItem) {
        ModlValue newValue = interpret(modlObject, valueItem, parentPair);
        ModlObject.Pair valuePair = new ModlObject.Pair();
        String fullClassName = currentClass;
        final Map<String, Object> aClass = getModlClass(currentClass);

        if (aClass != null) {
            final Object nameObject = aClass.get("*name");
            if (nameObject instanceof ModlObject.String) {
                fullClassName = ((ModlObject.String) nameObject).string;
            }
        }

        valuePair.setKey(new ModlObject.String(fullClassName));

        String classType = null;
        if (aClass != null) {
            classType = getSuperclassPrimitive((String) aClass.get("*superclass"));
        }
        if ("str".equals(classType)) {
            valuePair.addModlValue(makeValueString(newValue));
        } else if ("num".equals(classType)) {
            valuePair.addModlValue(makeValueNumber(newValue));
        } else if ("arr".equals(classType)) {
            valuePair.addModlValue(makeValueArray(newValue));
        } else if ("mao".equals(classType)) {
            valuePair.addModlValue(makeValueMap(newValue));
        } else {
            valuePair.addModlValue(newValue);
        }
        pair.addModlValue(valuePair);
    }

    private int getNumParams(RawModlObject.Pair originalPair, int numParams) {
        if (originalPair.getModlValue() instanceof ModlObject.Map) {
            numParams = ((ModlObject.Map) originalPair.getModlValue()).getPairs().size();
        } else if (originalPair.getModlValue() instanceof ModlObject.Array) {
            numParams = ((ModlObject.Array) originalPair.getModlValue()).getValues().size();
        } else if (originalPair.getModlValue() != null) {
            numParams = 1;
        }
        return numParams;
    }

    private ModlValue transformPairKey(ModlObject rawModlObject,
                                       RawModlObject.Pair originalPair,
                                       String newKey,
                                       Object parentPair) {
        String transformedKey = newKey;
        if (transformedKey.startsWith("_")) {
            transformedKey = transformedKey.replaceFirst("_", "");
        }
        if (parentPair == null) {
            if (newKey.startsWith("_")) {
                if (originalPair.getModlValue() instanceof ModlObject.Map) {
                    originalPair.setKey(new ModlObject.String(transformedKey));
                    Map newMap = new HashMap();
                    interpret(rawModlObject, (ModlObject.Map) (originalPair.getModlValue()), newMap);
                }
                if (originalPair.getModlValue() instanceof ModlObject.Array) {
                    originalPair.setKey(new ModlObject.String(transformedKey));
                    List newList = new LinkedList();
                    interpret(rawModlObject, (ModlObject.Array) (originalPair.getModlValue()), newList);
                }
            }
            if (originalPair.getModlValue() instanceof ModlObject.String) {
                final ModlValue
                    transformedValue =
                    transformString(((ModlObject.String) (originalPair.getModlValue())).string);
                valuePairs.put(transformedKey,
                               transformedValue);
                return transformedValue;
            } else {
                valuePairs.put(transformedKey, originalPair.getModlValue());
                return originalPair.getModlValue();
            }
        } else {
            // We have a new definition which must live under an existing mapPair or arrayPair
            if (parentPair instanceof Map) {
                String str = getStringFromValue(originalPair);
                @SuppressWarnings("unchecked") final Map<String, String> theMap = (Map) parentPair;
                theMap.put(transformedKey, str);
            } else if (parentPair instanceof List) {
                String str = getStringFromValue(originalPair);
                @SuppressWarnings("unchecked") final List<String> theList = (List) parentPair;
                theList.add(str);
            } else {
                throw new RuntimeException("Expecting Map or Array as parentPair!");
            }
        }
        return originalPair.getModlValue();
    }

    private String getStringFromValue(RawModlObject.Pair originalPair) {
        String str = null;
        ModlValue v = originalPair.getModlValue();
        if (v instanceof ModlObject.Array) {
            v = v.get(0);
        }
        if (v instanceof ModlObject.String) {
            str = ((ModlObject.String) v).string;
        }
        if (v instanceof ModlObject.Number) {
            str = ((ModlObject.Number) v).number;
        }
        return str;
    }

    private void makeNewMapPair(ModlObject.Pair pair,
                                List<ModlObject.Pair> rawPairs,
                                boolean wasArray) {
        for (ModlObject.Pair originalMapPair : rawPairs) {
            if (originalMapPair != null) {
                if (wasArray) {
                    ModlValue value;
                    if (!originalMapPair.getKey().string.startsWith("_")) {
                        value = originalMapPair;
                        pair.addModlValue(value);
                    }
                } else {
                    if (!originalMapPair.getKey().string.startsWith("_")) {
                        boolean knownItem = false;
                        ModlObject.Map map = null;
                        if (pair.getModlValue() != null) {
                            map = (ModlObject.Map) pair.getModlValue();
                        }
                        if (map == null) {
                            map = new ModlObject.Map();
                            pair.addModlValue(map);
                        }
                        if (map.get(originalMapPair.getKey()) != null) {
                            knownItem = true;
                        }
                        if (!knownItem) {
                            map.addPair(originalMapPair);
                        }
                    }
                }
            }
        }
    }

    private ModlObject.Array makeValueArray(ModlValue value) {
        if (value == null) {
            return null;
        }
        if (value instanceof ModlObject.Map) {
            throw new RuntimeException("Illegal value for array transformation : " + value.toString());
        }
        ModlObject.Array result = new ModlObject.Array();
        result.addValue(value);
        return result;
    }

    private ModlObject.Map makeValueMap(ModlValue value) {
        if (value == null) {
            return null;
        }
        if (value instanceof ModlObject.Array) {
            throw new RuntimeException("Illegal value for map transformation : " + value.toString());
        }
        ModlObject.Map result = new ModlObject.Map();
        result.addPair(new ModlObject.Pair(new ModlObject.String("value"), value));
        return result;
    }

    private ModlObject.Number makeValueNumber(ModlValue value) {
        if (value == null) {
            throw new RuntimeException("Interpreter Error: Cannot convert null to a num.");
        }
        String newNumber = null;
        if (value instanceof ModlObject.String) {
            newNumber = Float.toString(Float.parseFloat(((ModlObject.String) value).string));
        }
        if (value instanceof ModlObject.Number) {
            newNumber = ((ModlObject.Number) value).number;
        }
        if (value instanceof ModlObject.True) {
            newNumber = "1";
        }
        if (value instanceof ModlObject.False) {
            newNumber = "0";
        }
        if (value instanceof ModlObject.Null) {
            newNumber = null;
        }
        if (newNumber == null) {
            throw new RuntimeException("Interpreter Error: Cannot convert null to a num.");
        }

        return new ModlObject.Number(newNumber);
    }

    private ModlObject.String makeValueString(ModlValue value) {
        if (value == null) {
            throw new RuntimeException("Interpreter Error: Cannot convert null to a str.");
        }
        String newString = null;
        if (value instanceof ModlObject.String) {
            newString = ((ModlObject.String) value).string;
        }
        if (value instanceof ModlObject.Number) {
            newString = ((ModlObject.Number) value).number;
        }
        if (value instanceof ModlObject.True) {
            newString = "true";
        }
        if (value instanceof ModlObject.False) {
            newString = "false";
        }
        if (value instanceof ModlObject.Null) {
            newString = null;
        }
        if (newString == null) {
            throw new RuntimeException("Interpreter Error: Cannot convert null to a str.");
        }


        return new ModlObject.String(newString);
    }

    private void addMapItemsToPair(ModlObject modlObject,
                                   List<RawModlObject.Pair> mapItems,
                                   List<ModlObject.Pair> pairs,
                                   Object parentPair) {
        if (mapItems == null) {
            return;
        }
        for (RawModlObject.Pair mapItem : mapItems) {
            if (mapItem instanceof RawModlObject.MapConditional) {
                // handle conditionals
                List<ModlObject.Pair>
                    newPairs =
                    interpret(modlObject, (RawModlObject.MapConditional) mapItem, parentPair);
                if (newPairs != null) {
                    pairs.addAll(newPairs);
                }
            } else if (mapItem != null) {
                pairs.add(interpret(modlObject, mapItem, parentPair, false));
            }
        }
    }

    private ModlValue interpret(ModlObject modlObject, ModlValue rawValueItem, Object parentPair) {
        if (rawValueItem == null) {
            return null;
        }
        if (rawValueItem instanceof RawModlObject.ValueConditional) {
            return interpret(modlObject, (RawModlObject.ValueConditional) rawValueItem, parentPair);
        }
        if (!(rawValueItem instanceof ModlObject.Array)) {
            return interpretValue(modlObject, rawValueItem, parentPair);
        }
        ModlObject.Array array = new ModlObject.Array();
        for (ModlValue vi : (((ModlObject.Array) rawValueItem)).getValues()) {
            // TODO If this is an ArrayConditional, then get its list of values here
            if (vi instanceof RawModlObject.ArrayConditional) {
                List<ModlValue> newValues = interpret(modlObject, (RawModlObject.ArrayConditional) vi, parentPair);
                if (newValues != null) {
                    for (ModlValue viNew : newValues) {
                        array.addValue(interpret(modlObject, viNew, parentPair));
                    }
                }
            } else {
                array.addValue(interpret(modlObject, vi, parentPair));
            }
        }
        return array;
    }

    private Map<String, Object> getModlClass(String key) {
        for (Map.Entry<String, Map<String, Object>> entry : klasses.entrySet()) {
            for (Map.Entry<String, Object> valueEntry : entry.getValue().entrySet()) {
                if (valueEntry.getKey().equals("*name") || valueEntry.getKey().equals("*n") ||
                    valueEntry.getKey().equals("*id") || valueEntry.getKey().equals("*i")) {
                    if (valueEntry.getValue() instanceof String) {
                        if (valueEntry.getValue().equals(key)) {
                            return entry.getValue();
                        }
                    } else {
                        if (((ModlObject.String) (valueEntry.getValue())).string.equals(key)) {
                            return entry.getValue();
                        }
                    }
                }
            }
        }
        return klasses.get(key);
    }

    private boolean haveModlClass(String originalKey) {
        return (getModlClass(originalKey) != null);
    }

    private List<ModlObject.Pair> getPairsFromArray(ModlObject modlObject,
                                                    RawModlObject.Array array,
                                                    Object parentPair) {
        return getPairsFromArray(modlObject, array.getValues(), parentPair);
    }

    private List<ModlObject.Pair> getPairsFromArray(ModlObject modlObject,
                                                    List<ModlValue> arrayItems,
                                                    Object parentPair) {

        List<ModlObject.Pair> pairs = new LinkedList<>();
        if (arrayItems != null) {
            for (ModlValue arrayItem : arrayItems) {
                if (arrayItem instanceof RawModlObject.ArrayConditional) {
                    List<ModlValue>
                        newArrayItems =
                        interpret(modlObject, (RawModlObject.ArrayConditional) arrayItem, parentPair);
                    if (newArrayItems != null) {
                        for (ModlValue v : newArrayItems) {
                            if (v instanceof ModlObject.Pair) {
                                pairs.add((ModlObject.Pair) v);
                            }
                        }
                    }
                } else if (arrayItem instanceof ModlObject.Pair) {
                    pairs.add(interpret(modlObject, (ModlObject.Pair) arrayItem, parentPair));
                }
            }
        }
        return pairs;
    }

    private void addAllParentPairs(ModlObject modlObject, ModlObject.Pair pair, String originalKey) {
        Map<String, Object> klass = getModlClass(originalKey);
        if (klass != null) {
            for (Map.Entry<String, Object> entry : klass.entrySet()) {
                if (!entry.getKey().startsWith("_") &&
                    !(entry.getKey().startsWith("*") && !(entry.getKey().equals("?")))) {
                    if (pairHasKey(pair, entry.getKey())) {
                        // Only add the new key if it does not already exist in the pair!
                        continue;
                    }
                    ModlObject.Pair newPair = new ModlObject.Pair();
                    newPair.setKey(new ModlObject.String(entry.getKey()));
                    newPair.addModlValue(interpret(modlObject, (ModlValue) (entry.getValue()), null));
                    if (pair.getModlValue() != null && pair.getModlValue() instanceof ModlObject.Map) {
                        ((ModlObject.Map) (pair.getModlValue())).addPair(newPair);
                    } else {
                        pair.addModlValue(newPair);
                    }
                }
            }
            addAllParentPairs(modlObject, pair, (String) klass.get("*superclass"));
        }
    }

    private boolean pairHasKey(ModlObject.Pair pair, String key) {
        if (pair.getModlValue() == null) {
            return false;
        }
        if (pair.getModlValue() instanceof ModlObject.Pair) {
            return key.equals(((ModlObject.Pair) pair.getModlValue()).getKey().string);
        } else {
            if (pair.getModlValue() instanceof ModlObject.Map) {
                return ((ModlObject.Map) pair.getModlValue()).get(new ModlObject.String(key)) != null;
            }
        }
        return false;
    }

    private boolean mapPairAlready(RawModlObject.Pair originalPair) {
        return (originalPair.getModlValue() instanceof ModlObject.Map);
    }

    private boolean anyClassContainsPairs(final int depth, final String originalKey) {
        if (depth > MAX_CLASS_HIERARCHY_DEPTH) {
            throw new RuntimeException("Interpreter Error: Reached max class hierarchy depth: " +
                                       MAX_CLASS_HIERARCHY_DEPTH);
        }
        // If this class, or any of its parent classes, define any pairs, then return true
        // A pair is defined in a class if it has a pair whose key does not start in "_"
        Map<String, Object> klass = getModlClass(originalKey);
        if (klass != null) {
            for (String key : klass.keySet()) {
                if (!key.startsWith("_") && !key.startsWith("*") && !key.equals("?")) {
                    return true;
                }
            }
            final String superclass = (String) klass.get("*superclass");
            if (superclass != null) {
                return anyClassContainsPairs(depth + 1, superclass);
            }
        }
        return false;
    }

    private boolean hasAssignStatement(final int depth, String originalKey) {
        if (depth > MAX_CLASS_HIERARCHY_DEPTH) {
            throw new RuntimeException("Interpreter Error: Reached max class hierarchy depth: " +
                                       MAX_CLASS_HIERARCHY_DEPTH);
        }
        // If this class, or any of its parent classes, has an assign statement return true;
        Map<String, Object> klass = getModlClass(originalKey);
        if (klass != null) {
            for (String k : klass.keySet()) {
                if (k.startsWith("*params")) {
                    return true;
                }
            }

            final String superclass = (String) klass.get("*superclass");
            if (superclass != null) {
                return hasAssignStatement(depth + 1, superclass);
            }
        }
        return false;
    }

    private String transformKey(String originalKey) {
        Map<String, Object> map = getModlClass(originalKey);
        if (map != null) {
            if ((map.get("*name")) != null &&
                (map.get("*name")) instanceof ModlObject.String) {
                return ((ModlObject.String) (map.get("*name"))).string;
            }
            if ((map.get("*n")) != null &&
                (map.get("*n")) instanceof ModlObject.String) {
                return ((ModlObject.String) ((map.get("*n")))).string;
            }
        }
        return originalKey;
    }

    private RawModlObject.Pair transformValue(RawModlObject.Pair originalPair) {
        Map<String, Object> classMap = getModlClass(originalPair.getKey().string);
        if (classMap != null) {
            if ((classMap.get("*name") != null && ((classMap.get("*name").equals("_v")) ||
                                                   (classMap.get("*name").equals("var")))) ||
                (classMap.get("*n") != null &&
                 ((classMap.get("*n").equals("_v")) ||
                  (classMap.get("*n").equals("var"))))) {
                VariableLoader.loadConfigNumberedVariables(originalPair.getModlValue(), numberedVariables);
            } else {
                // Work up the superclass chain until we get to a basic class
                final String superclassName = (String) classMap.get("*superclass");
                boolean hasSuperclass = superclassName != null;
                if (!hasSuperclass && anyClassContainsPairs(1, classMap.get("*name").toString())) {
                    classMap.put("*superclass", "map");
                } else if (!hasSuperclass && hasAssignStatement(0, (String) classMap.get("*id"))) {
                    classMap.put("*superclass", "map");
                    if (originalPair.getModlValue() instanceof ModlObject.Array) {
                        return originalPair;
                    }
                    RawModlObject.Pair pair = new ModlObject.Pair();
                    pair.setKey(originalPair.getKey());
                    pair.addModlValue(makeValueArray(originalPair.getModlValue()));
                    return pair;
                } else if (!hasSuperclass) {
                    if (originalPair.getModlValue() instanceof ModlObject.Number) {
                        classMap.put("*superclass", "num");
                    } else if (originalPair.getModlValue() instanceof ModlObject.String) {
                        classMap.put("*superclass", "str");
                    } else if (originalPair.getModlValue() instanceof ModlObject.Array) {
                        classMap.put("*superclass", "arr");
                    } else if (originalPair.getModlValue() instanceof ModlObject.True) {
                        classMap.put("*superclass", "bool");
                    } else if (originalPair.getModlValue() instanceof ModlObject.False) {
                        classMap.put("*superclass", "bool");
                    } else if (originalPair.getModlValue() instanceof ModlObject.Null) {
                        classMap.put("*superclass", "null");
                    } else if (originalPair.getModlValue() instanceof ModlObject.Map) {
                        classMap.put("*superclass", "map");
                    } else {
                        throw new RuntimeException("Interpreter Error: Unhandled object type: " +
                                                   originalPair.getModlValue().getClass().getName());
                    }
                }

                //
                // Handle object type conversions here as described on the MODL Grammar GitHub Wiki.
                //
                final String superclassString;
                if (classMap.get("*superclass") instanceof String) {
                    superclassString = (String) classMap.get("*superclass");
                } else {
                    superclassString = ((ModlObject.String) classMap.get("*superclass")).string;
                }
                String mostSuperClass = getSuperclassPrimitive(superclassString);
                if (originalPair.getModlValue() == null) {
                    return originalPair;
                }
                RawModlObject.Pair pair = new ModlObject.Pair();
                pair.setKey(originalPair.getKey());
                if ("str".equals(mostSuperClass)) {
                    if (originalPair.getModlValue() instanceof ModlObject.String) {
                        return originalPair;
                    }
                    ModlObject.String s = makeValueString(originalPair.getModlValue());
                    pair.addModlValue(s);
                    return pair;
                } else if ("num".equals(mostSuperClass)) {
                    if (originalPair.getModlValue() instanceof ModlObject.Number) {
                        return originalPair;
                    }
                    ModlObject.Number number = makeValueNumber(originalPair.getModlValue());
                    pair.addModlValue(number);
                    return pair;
                } else if ("bool".equals(mostSuperClass)) {
                    return originalPair;
                } else if ("null".equals(mostSuperClass)) {
                    return originalPair;
                } else if ("arr".equals(mostSuperClass)) {
                    if (originalPair.getModlValue() instanceof ModlObject.Array) {
                        return originalPair;
                    }
                    ModlObject.Array array = makeValueArray(originalPair.getModlValue());
                    pair.addModlValue(array);
                    return pair;
                } else if ("map".equals(mostSuperClass)) {
                    if (originalPair.getModlValue() instanceof ModlObject.Map) {
                        return originalPair;
                    }
                    if (hasAssignStatement(0, (String) classMap.get("*id"))) {
                        return originalPair;
                    }
                    ModlObject.Map map = makeValueMap(originalPair.getModlValue());
                    pair.addModlValue(map);
                    return pair;
                } else {
                    throw new RuntimeException("Superclass " +
                                               superclassString +
                                               " is not available for " +
                                               originalPair.getModlValue().getClass());
                }
            }
        }
        return originalPair;
    }

    private String getSuperclassPrimitive(String originalSuperClass) {
        // Work up the chain until we come to a primitive superclass - i.e. map, arr, str or num
        // If we can't find one, then return null
        String currentSuperclass = originalSuperClass;
        while (!PRIMITIVES.contains(currentSuperclass)) {
            currentSuperclass = getNextSuperclassUp(currentSuperclass);
            if (currentSuperclass == null) {
                return null;
            }
        }
        if (PRIMITIVES.contains(currentSuperclass)) {
            return currentSuperclass;
        }
        return null;
    }

    private String getNextSuperclassUp(String currentSuperclass) {
        Map<String, Object> classMap = getModlClass(currentSuperclass);
        if (classMap != null) {
            if (classMap.get("*superclass") instanceof String) {
                return (String) classMap.get("*superclass");
            } else {
                return ((ModlObject.String) classMap.get("*superclass")).string;
            }
        }
        return null;
    }

    private ModlValue interpretValue(ModlObject modlObject, ModlValue rawValue, Object parentPair) {
        if (rawValue == null) {
            return null;
        }

        if (rawValue instanceof ModlObject.Pair) {
            return interpret(modlObject, (ModlObject.Pair) rawValue, parentPair);
        }
        if (rawValue instanceof ModlObject.Map) {
            return interpret(modlObject, (ModlObject.Map) rawValue, parentPair);
        }
        if (rawValue instanceof ModlObject.Array) {
            return interpret(modlObject, (ModlObject.Array) rawValue, parentPair);
        }
        if (rawValue instanceof ModlObject.Number) {
            return interpret((ModlObject.Number) rawValue);
        }
        if (rawValue instanceof ModlObject.True) {
            return interpret((ModlObject.True) rawValue);
        }
        if (rawValue instanceof ModlObject.False) {
            return interpret((ModlObject.False) rawValue);
        }
        if (rawValue instanceof ModlObject.Null) {
            return interpret((ModlObject.Null) rawValue);
        }
        if (rawValue instanceof ModlObject.String) {
            return interpret((ModlObject.String) rawValue);
        }
        return null;
    }

    private ModlObject.Map interpret(ModlObject modlObject, RawModlObject.Map originalMap, Object parentPair) {
        if (originalMap == null) {
            return null;
        }

        ModlObject.Map map = new ModlObject.Map();

        if (originalMap.getPairs() != null) {
            for (RawModlObject.Pair originalMapItem : originalMap.getPairs()) {
                List<ModlObject.Pair> pairs = interpretMapPair(modlObject, originalMapItem, parentPair);
                if (pairs != null) {
                    for (ModlObject.Pair pair : pairs) {
                        if (pair != null) {
                            if (!pair.getKey().string.startsWith("_") &&
                                !pair.getKey().string.startsWith("*") &&
                                !pair.getKey().string.equals("?")) {
                                map.addPair(pair);
                            }
                        }
                    }
                }
            }
        }
        return map;
    }

    private List<ModlObject.Pair> interpretMapPair(ModlObject modlObject,
                                                   RawModlObject.Pair originalMapItem,
                                                   Object parentPair) {
        if (originalMapItem == null) {
            return null;
        }

        List<ModlObject.Pair> pairs = new LinkedList<>();

        if (originalMapItem instanceof RawModlObject.MapConditional) {
            // evaluate the conditional
            pairs = interpret(modlObject, (RawModlObject.MapConditional) originalMapItem, parentPair);
        }
        ModlObject.Pair pair = interpret(modlObject, originalMapItem, parentPair);
        if (pair != null) {
            if (!pair.getKey().string.startsWith("_")) {
                pairs.add(pair);
            }
        }
        return pairs;
    }

    private ModlObject.Array interpret(ModlObject modlObject, RawModlObject.Array rawArray, Object parentPair) {
        if (rawArray == null) {
            return null;
        }
        ModlObject.Array array = new ModlObject.Array();

        if (rawArray.getValues() != null) {
            for (ModlValue originalArrayItem : rawArray.getValues()) {
                ModlValue value = interpret(modlObject, originalArrayItem, parentPair);
                if (value != null) {
                    array.addValue(value);
                    if (parentPair != null) {
                        @SuppressWarnings("unchecked") final List<ModlValue> theList = (List) parentPair;
                        theList.add(value);
                    }
                }
            }
        }
        return array;
    }

    private List<ModlValue> interpretArrayItem(ModlObject modlObject, ModlValue rawArrayItem, Object parentPair) {
        if (rawArrayItem == null) {
            return null;
        }
        List<ModlValue> values = new LinkedList<>();

        if (rawArrayItem instanceof RawModlObject.ArrayConditional) {
            values = interpret(modlObject, (RawModlObject.ArrayConditional) rawArrayItem, parentPair);
        } else {
            ModlValue value = interpret(modlObject, rawArrayItem, parentPair);
            values.add(value);
        }
        return values;
    }

    // We don't want to return this as a conditional any more!
    // We want to return it as whatever the conditional evaluates to
    private ModlValue interpret(ModlObject modlObject,
                                RawModlObject.ValueConditional originalConditional,
                                Object parentPair) {
        if (originalConditional == null) {
            return null;
        }
        for (Map.Entry<RawModlObject.ConditionTest, RawModlObject.ValueConditionalReturn> originalConditionalEntry : originalConditional
            .getConditionals()
            .entrySet()) {
            // Does this conditional evaluate?
            RawModlObject.ConditionTest conditionalTest = originalConditionalEntry.getKey();
            if (evaluates(conditionalTest)) {
                if (originalConditionalEntry.getValue() == null) {
                    return new ModlObject.True();
                }
                if (originalConditionalEntry.getValue().getValues().size() == 1) {
                    return interpret(modlObject, originalConditionalEntry.getValue().getValues().get(0), parentPair);
                }
                ModlObject.Array returnValue = new ModlObject.Array();
                for (ModlValue valueItem : originalConditionalEntry.getValue().getValues()) {
                    ModlValue v = interpret(modlObject, valueItem, parentPair);
                    returnValue.addValue(v);
                }
                return returnValue;
            }
            if (originalConditionalEntry.getValue() == null) {
                return new ModlObject.False();
            }

        }
        return null;
    }

    private List<ModlValue> interpret(ModlObject modlObject,
                                      RawModlObject.ArrayConditional rawConditional,
                                      Object parentPair) {
        if (rawConditional == null) {
            return null;
        }
        if (rawConditional.getConditionals() != null) {
            for (Map.Entry<RawModlObject.ConditionTest, RawModlObject.ArrayConditionalReturn> originalConditionalEntry : rawConditional
                .getConditionals()
                .entrySet()) {
                // Does this conditional evaluate?
                RawModlObject.ConditionTest conditionalTest = originalConditionalEntry.getKey();
                if (evaluates(conditionalTest)) {
                    // NEED TO INTERPRET THE VALUES!!!
                    List<ModlValue> returnValues = new LinkedList<>();
                    for (ModlValue arrayItem : originalConditionalEntry.getValue().getValues()) {
                        List<ModlValue> values = interpretArrayItem(modlObject, arrayItem, parentPair);
                        if (values != null) {
                            returnValues.addAll(values);
                        }
                    }
                    return returnValues;
                }
            }
        }
        return null;
    }

    private List<ModlObject.Pair> interpret(ModlObject modlObject,
                                            RawModlObject.MapConditional originalConditional,
                                            Object parentPair) {
        if (originalConditional == null) {
            return null;
        }
        for (Map.Entry<RawModlObject.ConditionTest, RawModlObject.Map> originalConditionalEntry : originalConditional.getConditionals()
                                                                                                                     .entrySet()) {
            // Does this conditional evaluate?
            RawModlObject.ConditionTest conditionalTest = originalConditionalEntry.getKey();
            if (evaluates(conditionalTest)) {
                // NEED TO INTERPRET THE VALUES!!!
                List<ModlObject.Pair> returnPairs = new LinkedList<>();
                for (RawModlObject.Pair mapItem : originalConditionalEntry.getValue().getPairs()) {
                    List<ModlObject.Pair> mapItems = interpretMapPair(modlObject, mapItem, parentPair);
                    if (mapItems != null) {
                        returnPairs.addAll(mapItems);
                    }
                }
                return returnPairs;
            }
        }
        return null;
    }

    private List<ModlObject.Structure> interpret(ModlObject modlObject,
                                                 RawModlObject.TopLevelConditional originalConditional) {
        if (originalConditional == null) {
            return null;
        }
        for (Map.Entry<RawModlObject.ConditionTest, RawModlObject.TopLevelConditionalReturn> originalConditionalEntry : originalConditional
            .getConditionals()
            .entrySet()) {
            // Does this conditional evaluate?
            RawModlObject.ConditionTest conditionalTest = originalConditionalEntry.getKey();
            if (evaluates(conditionalTest)) {
                // NEED TO INTERPRET THE VALUES!!!
                List<ModlObject.Structure> returnStructures = new LinkedList<>();
                for (RawModlObject.Structure rawStructure : originalConditionalEntry.getValue().getStructures()) {
                    List<ModlObject.Structure> structures = interpret(modlObject, rawStructure);
                    if (structures != null) {
                        returnStructures.addAll(structures);
                    }
                }
                return returnStructures;
            }
        }
        return null;
    }

    private boolean evaluates(RawModlObject.ConditionTest conditionalTest) {
        int nullCount = 0;
        List<Map.Entry<RawModlObject.SubCondition, ImmutablePair<java.lang.String, Boolean>>>
            conditionalTestOrderedList =
            new LinkedList<>();
        for (Map.Entry<RawModlObject.SubCondition, ImmutablePair<java.lang.String, Boolean>> conditionalTestEntry : conditionalTest
            .getSubConditionMap()
            .entrySet()) {
            // There are only & and | and null here!
            // Work out where this should be in the list
            String operator = conditionalTestEntry.getValue().getLeft();
            if (operator == null) {
                conditionalTestOrderedList.add(nullCount++, conditionalTestEntry);
            } else {
                if (operator.equals("|")) {
                    conditionalTestOrderedList.add(conditionalTestEntry);
                } else if (operator.equals("&")) {
                    conditionalTestOrderedList.add(nullCount, conditionalTestEntry);
                }
            }
        }

        boolean result = true;
        for (Map.Entry<RawModlObject.SubCondition, ImmutablePair<java.lang.String, Boolean>> conditionalTestEntry : conditionalTestOrderedList) {
            RawModlObject.SubCondition subCondition = conditionalTestEntry.getKey();
            ImmutablePair<String, Boolean> conditionTestOperatorPair = conditionalTestEntry.getValue();
            String conditionTestOperator = conditionTestOperatorPair.getLeft();
            Boolean shouldNegate = conditionTestOperatorPair.getRight();
            boolean subConditionReturn = true;
            if (subCondition instanceof RawModlObject.ConditionGroup) {
                subConditionReturn = evaluates((RawModlObject.ConditionGroup) subCondition);
            } else if (subCondition instanceof RawModlObject.Condition) {
                subConditionReturn = evaluates((RawModlObject.Condition) subCondition);
            }

            // HANDLE NOT OPERATOR!
            if (shouldNegate) {
                subConditionReturn = !subConditionReturn;
            }

            // Do something with it!!!
            if (conditionTestOperator == null) {
                result = subConditionReturn;
            } else {
                if (conditionTestOperator.equals("&")) {
                    result = result && subConditionReturn;
                } else if (conditionTestOperator.equals("|")) {
                    result = result || subConditionReturn;
                }
            }
        }
        return result;
    }

    private boolean evaluates(RawModlObject.ConditionGroup conditionGroup) {
        List<ImmutablePair<RawModlObject.ConditionTest, String>>
            orderedConditionalTestList =
            getOrderedConditionalTestList(conditionGroup);
        boolean result = true;
        for (ImmutablePair<RawModlObject.ConditionTest, java.lang.String> conditionTestPair : orderedConditionalTestList) {
            RawModlObject.ConditionTest ct = conditionTestPair.getLeft();
            java.lang.String conditionGroupOperator = conditionTestPair.getRight();
            boolean ctReturn = evaluates(ct);
            if (conditionGroupOperator == null) {
                result = ctReturn;
            } else {
                if (conditionGroupOperator.equals("&")) {
                    result = result && ctReturn;
                } else if (conditionGroupOperator.equals("|")) {
                    result = result || ctReturn;
                }
            }
        }
        return result;
    }

    private boolean evaluates(RawModlObject.Condition condition) {
        String keyString = condition.getKey();
        List<ModlValue> values = condition.getValues();
        // Try to do object referencing and string transformation on the key
        if (keyString == null) {
            if (values.get(0) instanceof ModlObject.True) {
                return true;
            }
            if (values.get(0) instanceof ModlObject.False) {
                return false;
            }
            ModlValue transformedName = transformString(((ModlObject.String) (values.get(0))).string);
            if (transformedName instanceof ModlObject.True) {
                return true;
            }
            if (transformedName instanceof ModlObject.False) {
                return false;
            }
            if (values.get(0) instanceof ModlObject.String) {
                if (valuePairs.get(((ModlObject.String) values.get(0)).string) == null) {
                    return false;
                }
                ModlValue valueEntry = (valuePairs.get(((ModlObject.String) values.get(0)).string));
                if (valueEntry instanceof ModlObject.True) {
                    return true;
                }
                return !(valueEntry instanceof ModlObject.False);
                // DO WE NEED TO TRY TO INTERPRET THE VALUEE?!
            }
        }
        while (keyString != null && (keyString.startsWith("_") || keyString.startsWith("%"))) {
            keyString = keyString.substring(1);
        }
        String key = transformConditionalArgument(keyString);

        String conditionOperator = condition.getOperator();

        // Now evaluate it!!
        // Should either be number, string or quoted

        if (values.size() > 1) {
            // This MUST be a "variable assumption"
            // Check that the operator is "="
            // We need to check if the key is equal to ANY of the values
            for (ModlValue value : values) {
                Object val = getObjectFromValueForCondition(value);
                if (conditionOperator.equals("=")) {
                    if (conditionalEquals(key, val)) {
                        return true;
                    }
                }
            }
            return false;
        } else {
            // Single value
            // Get the basic Java primitive out of the value
            // Then cast it to Object
            // Then do whatever the conditionOperator says!
            String valObj = getObjectFromValueForCondition(values.get(0));
            String origKeyString = valObj;
            String val = origKeyString;
            if (origKeyString != null && origKeyString.startsWith("%")) {
                origKeyString = origKeyString.substring(1);
                val = transformConditionalArgument(origKeyString);
            } else {
                // Does it reference a pair?
                String tmpVal = val;
                if (val.startsWith("`")) {
                    tmpVal = val.replaceAll("`", "");
                }
                if (pairNames.contains(tmpVal) || pairNames.contains("_" + tmpVal)) {
                    final ModlValue pairValue = valuePairs.get(tmpVal);
                    if (pairValue.isString()) {
                        val = (String) pairValue.getValue();
                    } else if (pairValue.isNumber()) {
                        val = ((ModlObject.Number) pairValue).number;
                    } else if (pairValue.isArray() || pairValue.isMap()) {
                        val = pairValue.toString();
                    }
                }
            }

            if (conditionOperator.equals("=")) {
                if (conditionalEquals(key, val)) {
                    return true;
                }
                if (conditionalEquals(key, valObj)) {
                    return true;
                }
                if (conditionalEquals(key, transformString(val))) {
                    return true;
                }
                return conditionalEquals(key, transformString(valObj));
            }
            if (conditionOperator.equals("!=")) {
                return !conditionalEquals(key, val);
            }
            if (val != null) {
                Float valFloat = Float.valueOf(val);
                Float keyFloat = Float.valueOf(key);
                if (conditionOperator.equals(">")) {
                    return keyFloat.compareTo(valFloat) > 0;
                }
                if (conditionOperator.equals("<")) {
                    return keyFloat.compareTo(valFloat) < 0;
                }
                if (conditionOperator.equals("<=")) {
                    return keyFloat.compareTo(valFloat) <= 0;
                }
                if (conditionOperator.equals(">=")) {
                    return keyFloat.compareTo(valFloat) >= 0;
                }
            }
        }
        return false;
    }

    private boolean conditionalEquals(String key, Object val) {
        if (val != null && val.toString().contains("*")) {
            return conditionalWildcardEquals(key, val.toString());
        }
        if (val == null) {
            return key == null;
        }
        return key.equals(val.toString());
    }

    private boolean conditionalWildcardEquals(String key, Object val) {
        // Build a pattern matching string, using HEAD and TAIL operators, and adding wildcards inbetween the phrases, wherever we see a *
        StringBuilder regex;
        if (!(val.toString().startsWith("*"))) {
            regex = new StringBuilder("^");
        } else {
            regex = new StringBuilder(".*");
        }

        String[] splits = val.toString().split("\\*");
        int i = 0;
        for (String split : splits) {
            if (split.equals("")) {
                continue;
            }
            if (i++ > 0) {
                regex.append(".*");
            }
            regex.append(split);
        }
        if (!(val.toString().endsWith("*"))) {
            regex.append("$");
        } else {
            regex.append(".*");
        }
        return key.matches(regex.toString());
    }

    private String getObjectFromValueForCondition(ModlValue value) {
        if (value instanceof ModlObject.String) {
            return ((ModlObject.String) value).string;
        }
        if (value instanceof ModlObject.Number) {
            return ((ModlObject.Number) value).number;
        }
        return null;
    }

    private String transformConditionalArgument(String origKeyString) {
        StringTransformer stringTransformer = new StringTransformer(valuePairs, variables, numberedVariables);
        ModlValue objectRef = stringTransformer.runObjectReferencing("%" + origKeyString, "%" + origKeyString, false);
        if (objectRef instanceof ModlObject.String) {
            String keyString = ((ModlObject.String) objectRef).string;
            if (keyString.equals("%" + origKeyString) && origKeyString.startsWith("%")) {
                objectRef = stringTransformer.runObjectReferencing(origKeyString, origKeyString, false);
                if (objectRef instanceof ModlObject.String) {
                    keyString = ((ModlObject.String) objectRef).string;
                    if (keyString.equals("%" + origKeyString)) {
                        return origKeyString;
                    }
                    return keyString;
                }
                if (objectRef instanceof ModlObject.Number) {
                    keyString = ((ModlObject.Number) objectRef).number;
                    return keyString;
                }
            }
            return keyString;
        }
        if (objectRef instanceof ModlObject.Number) {
            return ((ModlObject.Number) objectRef).number;
        }
        if (objectRef instanceof ModlObject.Array || objectRef instanceof ModlObject.Map) {
            return objectRef.toString();
        }
        return origKeyString;
    }

    private ModlObject.False interpret(RawModlObject.False falseVal) {
        if (falseVal != null) {
            return new ModlObject.False();
        }
        return null;
    }

    private ModlObject.Null interpret(RawModlObject.Null val) {
        if (val != null) {
            return new ModlObject.Null();
        }
        return null;
    }

    private ModlObject.True interpret(RawModlObject.True trueVal) {
        if (trueVal != null) {
            return new ModlObject.True();
        }
        return null;
    }

    private ModlObject.Number interpret(RawModlObject.Number originalNumber) {
        if (originalNumber != null) {
            return new ModlObject.Number(originalNumber.number);
        }
        return null;
    }

    private ModlValue interpret(RawModlObject.String string) {
        if (string != null) {
            ModlValue value = transformString(string.string);

            if (string.string.startsWith("%*")) {
                if (string.string.equals("%*class")) {
                    value = InstructionProcessor.processClassInstruction(klasses);
                }
                if (string.string.equals("%*load")) {
                    value = InstructionProcessor.processLoadInstruction(loadedFiles);
                }
                if (string.string.equals("%*method")) {
                    value = InstructionProcessor.processMethodInstruction(methodList);
                }
            }
            return value;
        }
        return null;
    }

    private ModlValue transformString(String s) {
        StringTransformer stringTransformer = new StringTransformer(valuePairs, variables, numberedVariables);
        return stringTransformer.transformString(s);
    }
}
