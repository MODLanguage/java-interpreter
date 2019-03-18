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
import uk.modl.modlObject.ModlValue;
import uk.modl.parser.ModlObjectCreator;
import uk.modl.parser.RawModlObject;
import uk.modl.parser.printers.JsonPrinter;

import java.io.IOException;
import java.util.*;

import static uk.modl.parser.ModlObjectCreator.MODL_VERSION;

public class Interpreter {

    private final static String IDENTIFIER_REGEX = "\\?|((_|\\*)?[a-zA-Z][0-9a-zA-Z_]*)";

    Map<String, Map<String, Object>> klasses = new LinkedHashMap<>();
    Map<String, ModlValue> variables = new HashMap<>();
    Map<Integer, ModlValue> numberedVariables = new HashMap<>();

    Set<String> pairNames; // TODO Get rid of this!
    Map<String, ModlValue> valuePairs;

    public static String parseToJson(String input) throws IOException {
        ModlObject modlObject = interpret(input);

        JsonPrinter jsonPrinter = new JsonPrinter();
        String json = jsonPrinter.printModl(modlObject);
        return json;

    }

    public static ModlObject interpret(String input) throws IOException {
        RawModlObject rawModlObject = ModlObjectCreator.processModlParsed(input);

        return interpret(rawModlObject);
    }

    public static ModlObject interpret(RawModlObject rawModlObject) {
        Interpreter interpreter = new Interpreter();
        ModlObject modlObject = null;
        while (modlObject == null) {
            try {
                modlObject = interpreter.interpretPrivate(rawModlObject);
            } catch (RequireRestart r) {
                // OK - let's try again with the updated rawModlObject}
            }
        }
        return modlObject;
    }

    private ModlObject interpretPrivate(RawModlObject rawModlObject) throws RequireRestart {
        ModlObject modlObject = new ModlObject();
        ModlClassLoader.loadModlKlassO(rawModlObject, klasses);
        pairNames = new HashSet<>();
        valuePairs = new HashMap<>();

        // Interpret rawModlObject based on specified config files
        boolean startedInterpreting = false;
        boolean needRestart = false;
        RawModlObject loadedRawModlObject = null;
        String importFileValue = null;
        for (RawModlObject.Structure rawStructure : rawModlObject.getStructures()) {

            if (rawStructure instanceof ModlObject.Pair && (((ModlObject.Pair) rawStructure).getKey() != null &&
                    (((ModlObject.Pair) rawStructure).getKey().string != null &&
                            (((ModlObject.Pair) rawStructure).getKey().string.equals("*V") ||
                                    (((ModlObject.Pair) rawStructure).getKey().string.equals("*VERSION")))))) {
                // This is the version number - check it and then ignore it
                String versionString = ((ModlObject.Number) ((ModlObject.Pair) rawStructure).getModlValue()).number;
                if (!(versionString.equals(String.valueOf(MODL_VERSION)))) {
                    throw new UnsupportedOperationException("Can't handle MODL version " + versionString);
                }
                continue;
            }
            if (rawStructure instanceof ModlObject.Pair && (((ModlObject.Pair) rawStructure).getKey() != null &&
                    (((ModlObject.Pair) rawStructure).getKey().string != null &&
                            (((ModlObject.Pair) rawStructure).getKey().string.equals("*I") ||
                                    (((ModlObject.Pair) rawStructure).getKey().string.equals("*IMPORT")))))) {
                if (startedInterpreting) {
//                    throw new UnsupportedOperationException("Cannot have *I config file after other objects");
                }
                // Load in the config file specified by the "I" object
                if (((ModlObject.Pair) rawStructure).getModlValue() instanceof ModlObject.String) {
                    importFileValue = ((ModlObject.String) ((ModlObject.Pair) rawStructure).getModlValue()).string;
                    loadedRawModlObject = loadConfigFile(importFileValue);
                    needRestart = true;
                    break;
                } else if (((ModlObject.Pair) rawStructure).getModlValue() instanceof ModlObject.Number) {
                    importFileValue = ((ModlObject.Number) ((ModlObject.Pair) rawStructure).getModlValue()).number.toString();
                    loadedRawModlObject = loadConfigFile(importFileValue);
                    needRestart = true;
                    break;
                }
                continue;
            }
            if (rawStructure instanceof ModlObject.Pair && ((ModlObject.Pair) rawStructure).getKey() != null &&
                    ((ModlObject.Pair) rawStructure).getKey().string != null &&
                    ((((((ModlObject.Pair) rawStructure).getKey().string.equals("*class")) ||
                            (((ModlObject.Pair) rawStructure).getKey().string.equals("*c"))) || (
                            (((ModlObject.Pair) rawStructure).getKey().string.equals("*m"))) || ((((ModlObject.Pair) rawStructure).getKey().string.equals("*method"))) ||
                            (((ModlObject.Pair) rawStructure).getKey().string.equals("?"))
                    ))) {
                ModlObject.Pair pair = (ModlObject.Pair) rawStructure;
                if (pair.getKey().string.equals("*class") || pair.getKey().string.equals("*c")) {
                    ModlClassLoader.loadClass(rawStructure, klasses);
                    continue;
                } else if (pair.getKey().string.equals("*method") || pair.getKey().string.equals("*m")) {
                    VariableMethodLoader.loadVariableMethod(pair);
                } else if (pair.getKey().string.equals("?")) {
                    VariableLoader.loadConfigNumberedVariables(pair.getModlValue(), numberedVariables);
                } else {
                    if (pair.getKey().string.startsWith("_")) {
                        VariableLoader.loadConfigVar(pair.getKey().string.replaceFirst("_", ""), pair, variables);
                    } else if (pair.getKey().string.startsWith("*")) {
                        throw new RuntimeException("Unrecognised configuration instruction : " + pair.getKey());
                    }
                }
            } else if (!(rawStructure instanceof ModlObject.Pair && ((ModlObject.Pair) rawStructure).getKey() != null &&
                    ((ModlObject.Pair) rawStructure).getKey().string != null &&
                    (((ModlObject.Pair) rawStructure).getKey().string.equals("*V") ||
                            (((ModlObject.Pair) rawStructure).getKey().string.toLowerCase().equals("*VERSION"))))) {
                if (rawStructure instanceof ModlObject.Pair && ((ModlObject.Pair) rawStructure).getKey() != null &&
                        ((ModlObject.Pair) rawStructure).getKey().string != null &&
                        (((ModlObject.Pair) rawStructure).getKey().string.startsWith("*"))) {
                    throw new RuntimeException("Unrecognised configuration instruction : " + ((ModlObject.Pair) rawStructure).getKey().string);
                }
                startedInterpreting = true;
            } else if (rawStructure instanceof ModlObject.Pair && ((ModlObject.Pair) rawStructure).getKey() != null &&
                    ((ModlObject.Pair) rawStructure).getKey().string != null &&
                    (((ModlObject.Pair) rawStructure).getKey().string.startsWith("*"))) {
                throw new RuntimeException("Unrecognised configuration instruction : " + ((ModlObject.Pair) rawStructure).getKey().string);
            }
            List<ModlObject.Structure> structures = interpret(modlObject, rawStructure);
            if (structures != null) {
                for (ModlObject.Structure structure : structures) {
                    modlObject.addStructure(structure);
                }
            }
        }

        if (needRestart) {
            rawModlObject.replaceFirstImport(importFileValue, loadedRawModlObject);
            throw new RequireRestart();
        } else {
            return modlObject;
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
        RawModlObject rawModlObject = FileLoader.loadFile(location);
        return rawModlObject;
    }

    private List<ModlObject.Structure> interpret(ModlObject modlObject, RawModlObject.Structure rawStructure) {

        if (rawStructure == null) {
            return null;
        }
        List<ModlObject.Structure> structures = new LinkedList<>();
        if (!(rawStructure instanceof RawModlObject.TopLevelConditional)) {
            ModlObject.Structure structure = null;
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

    private ModlObject.Pair interpret(ModlObject modlObject, RawModlObject.Pair rawPair, Object parentPair, boolean addToValuePairs) {
        if (rawPair == null) {
            return null;
        }
        if (rawPair.getKey() != null && rawPair.getKey().string != null) {
            if (!(rawPair.getKey().string.matches(IDENTIFIER_REGEX))) {
                throw new RuntimeException("Pair name " + rawPair.getKey().string + " is illegal");
            }
            if (rawPair.getKey().string.equals("?")) {
                numberedVariables = new HashMap<>();
                VariableLoader.loadConfigNumberedVariables(rawPair.getModlValue(), numberedVariables);
                return null;
            }
        }

        ModlObject.Pair pair = modlObject.new Pair();

        if (rawPair.getKey() == null) {
            return null;
        }

        String originalKey = rawPair.getKey().string;
        String newKey = originalKey;
        if (haveModlClass(originalKey)) {
            newKey = transformKey(originalKey);
            rawPair = transformValue(modlObject, rawPair);
        }

        // IF WE ALREADY HAVE A PAIR WITH THIS NAME, AND THE NAME IS UPPER-CASE, THEN RAISE AN ERROR
        if (newKey != null) {
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

        pair.setKey(modlObject.new String(newKey));

        if (pair.getModlValue() instanceof ModlObject.Array) {
            for (ModlValue value : ((ModlObject.Array) (rawPair.getModlValue())).getValues()) {
                addValueFromPair(modlObject, rawPair, parentPair, pair, value);
            }
        } else {
            addValueFromPair(modlObject, rawPair, parentPair, pair, rawPair.getModlValue());
        }

        return pair;
    }

    private void addValueFromPair(ModlObject modlObject, ModlObject.Pair rawPair, Object parentPair, ModlObject.Pair pair, ModlValue value) {
        // Is this a variable prefixed by "%"?
        if (value != null &&
                value instanceof ModlObject.Pair
                && ((ModlObject.Pair) value).getKey().string.startsWith("%")) {
            String key = ((ModlObject.Pair) value).getKey().string;
            // If so, then look up the reference!!
            ModlValue newValue = null;
            if (pairNames.contains(key.replaceFirst("%", "_"))) {
                ModlValue storedValue = valuePairs.get(key.replaceFirst("%", ""));
                if (storedValue instanceof ModlObject.Map) {
                    ModlObject.Map map = (ModlObject.Map) storedValue;
                    newValue = modlObject.new Pair(modlObject.new String("obsolete"), map);
                } else if (storedValue instanceof ModlObject.Array) {
                    List<ModlValue> list = ((ModlObject.Array) storedValue).getValues();
                    Integer index = null;
                    if (((ModlObject.Pair) value).getModlValue() instanceof ModlObject.Number) {
                        index = new Integer(((ModlObject.Number) (((ModlObject.Pair) value).getModlValue())).number);
                    } else {
                        index = new Integer(((ModlObject.Number) ((ModlObject.Array) (((ModlObject.Pair) value).getModlValue())).get(0)).number);
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
        pair.setKey(modlObject.new String(newKey));

        int numParams = 0;
        if (rawPair.getModlValue() != null) {
            if ((!(rawPair.getModlValue() instanceof ModlObject.Array)) &&
                    rawPair.getModlValue() != null &&
                    rawPair.getModlValue() instanceof ModlObject.Map) {
//                rawPair.addModlValue((ModlObject.Map) (rawPair.getModlValue()));
            } else {
                if (rawPair.getModlValue() instanceof ModlObject.Array) {
                    numParams = ((ModlObject.Array) (rawPair.getModlValue())).getValues().size();
                } else {
                    numParams = 1;
                }
            }
        }
        numParams = getNumParams(rawPair, numParams);
        String paramsKeyString = "*params" + numParams;
        Object obj = getModlClass(rawPair.getKey().string).get(paramsKeyString);
        boolean hasParams = (obj != null);

        // If it's not already a map pair, and one of the parent classes in the class hierarchy includes pairs, then it is transformed to a map pair.
        if (anyClassContainsPairs(originalKey) || mapPairAlready(rawPair) || (hasParams)) {
            pair.setKey(modlObject.new String(newKey)); // TODO Do we need to do this again here?!

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
                    wasArray = false;
                }
            }

            if (mapPairAlready(rawPair)) {
                pairs = new LinkedList<>();
                addMapItemsToPair(modlObject, ((ModlObject.Map) (rawPair.getModlValue())).getPairs(), pairs, parentPair);
            }

            if (pairs != null) {
                // Make all the new map values in the new map pair
                makeNewMapPair(modlObject, pair, pairs, wasArray, parentPair);
            }
            if ((!(rawPair.getModlValue() instanceof ModlObject.Pair)) && (!(rawPair.getModlValue() instanceof ModlObject.Map))) {
                if (!hasParams) {
                    // Don't need a pair here - continue
                    ModlValue value = interpret(modlObject, rawPair.getModlValue(), parentPair);
                    pair.addModlValue(value);
                } else {
                    int paramNum = 0;
                    List<ModlValue> params = (List<ModlValue>) obj;
                    String currentClass = null;


                    List<ModlValue> values = new LinkedList<>();
                    ModlValue pairVal = rawPair.getModlValue();
                    if (pairVal instanceof ModlObject.Array) {
                        for (ModlValue vl : ((ModlObject.Array) pairVal).getValues()) {
                            if (vl instanceof RawModlObject.ArrayConditional) {
                                List<ModlValue> vs = interpret(modlObject, (RawModlObject.ArrayConditional) vl, parentPair);
                                for (ModlValue v : vs) {
                                    values.add(v);
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
                            RawModlObject rawModlObject = new RawModlObject();
                            // Get the class which we're interested in, and go through the different entries
                            Object modlClassObj = getModlClass(currentClass);
                            int innerParamNum = 0;
                            ModlObject.Pair innerPair = modlObject.new Pair();
                            ModlObject.Pair valuePair = modlObject.new Pair();
                            String fullClassName = currentClass; // TODO GET THIS FROM THE MODLOBJ!!
                            try {
                                String nameString = ((ModlObject.String) ((ModlValue) getModlClass(currentClass).get("*name"))).string;
                                if (nameString == null) {
                                    nameString = ((ModlObject.String) ((ModlValue) getModlClass(currentClass).get("*n"))).string;
                                }
                                fullClassName = nameString;
                            } catch (Exception e) {
                            }
                            valuePair.setKey(modlObject.new String(fullClassName));
                            for (ModlValue vi : ((ModlObject.Array) valueItem).getValues()) {
                                int valueItemSize = 1;
                                if (valueItem instanceof ModlObject.Array) {
                                    valueItemSize = ((ModlObject.Array) valueItem).getValues().size();
                                }

                                Map<String, Object> modlClassMap = (getModlClass(currentClass));
                                String superclass = ((String) (modlClassMap.get("*superclass")));
                                if (superclass.equals("arr")) {
                                    ModlValue v = interpret(modlObject, vi, parentPair);
                                    valuePair.addModlValue(v);
                                } else if (superclass.equals("map")) {
                                    ModlObject.String innerClassName = ((ModlObject.String) ((Map.Entry<String, LinkedList<ModlValue>>) ((LinkedHashMap<String,
                                            LinkedList<ModlValue>>) modlClassObj).entrySet().toArray()[valueItemSize])
                                            .getValue().get(innerParamNum++));

                                    RawModlObject.Pair newRawPair = rawModlObject.new Pair();
                                    newRawPair.setKey(innerClassName);
                                    newRawPair.addModlValue(vi);

                                    ModlValue v = interpret(modlObject, newRawPair, parentPair);

                                    //  And add it to the pair
                                    valuePair.addModlValue(v);
                                    innerPair.addModlValue(valuePair);
                                } else {
                                    throw new RuntimeException("Superclass " + superclass + " of " + fullClassName + " is not known!");
                                }
                            }
                            pair.addModlValue(valuePair);
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

    private void addNewClassParamValue(ModlObject modlObject, ModlObject.Pair pair, Object parentPair, String currentClass, ModlValue valueItem) {
        ModlValue newValue = interpret(modlObject, valueItem, parentPair);
        ModlObject.Pair valuePair = modlObject.new Pair();
        String fullClassName = currentClass;
        try {
            fullClassName = ((ModlObject.String) ((ModlValue) getModlClass(currentClass).get("*name"))).string;
        } catch (Exception e) {
        }
        valuePair.setKey(modlObject.new String(fullClassName));
        valuePair.addModlValue(newValue);
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

    private void transformPairKey(ModlObject rawModlObject, RawModlObject.Pair originalPair, String newKey, Object parentPair) {
        String transformedKey = newKey;
        if (transformedKey.startsWith("_")) {
            transformedKey = transformedKey.replaceFirst("_", "");
        }
        if (parentPair == null) {
            if (newKey.startsWith("_")) {
                if (originalPair.getModlValue() instanceof ModlObject.Map) {
                    originalPair.setKey(rawModlObject.new String(transformedKey));
                    Map newMap = new HashMap();
                    interpret(rawModlObject, (ModlObject.Map) (originalPair.getModlValue()), newMap);
                }
                if (originalPair.getModlValue() instanceof ModlObject.Array) {
                    originalPair.setKey(rawModlObject.new String(transformedKey));
                    List newList = new LinkedList();
                    interpret(rawModlObject, (ModlObject.Array) (originalPair.getModlValue()), newList);
                }
            }
            if (originalPair.getModlValue() instanceof ModlObject.String) {
                valuePairs.put(transformedKey, transformString(((ModlObject.String) (originalPair.getModlValue())).string));
            } else {
                valuePairs.put(transformedKey, originalPair.getModlValue());
            }
        } else {
            // We have a new definition which must live under an existing mapPair or arrayPair
            if (parentPair instanceof Map) {
                String str = getStringFromValue(originalPair);
                ((Map) parentPair).put(transformedKey, str);
            } else if (parentPair instanceof List) {
                String str = getStringFromValue(originalPair);
                ((List) parentPair).add(str);
            } else {
                throw new RuntimeException("Expecting Map or Array as parentPair!");
            }
        }
    }

    private String getStringFromValue(RawModlObject.Pair originalPair) {
        String str = null;
        ModlValue v = originalPair.getModlValue();
        if (v instanceof ModlObject.Array) {
            v = ((ModlObject.Array) v).get(0);
        }
        if (v != null &&
                v instanceof ModlObject.String) {
            str = ((ModlObject.String) v).string;
        }
        if (v != null &&
                v instanceof ModlObject.Number) {
            str = ((ModlObject.Number) v).number;
        }
        return str;
    }

    public void makeNewMapPair(ModlObject modlObject, ModlObject.Pair pair, List<ModlObject.Pair> rawPairs, boolean wasArray, Object parentPair) {
        for (ModlObject.Pair originalMapPair : rawPairs) {
            ModlObject.Pair newMapPair = originalMapPair;
            if (newMapPair != null) {
                if (wasArray) {
                    ModlValue value = null;
                    if (!newMapPair.getKey().string.startsWith("_")) {
                        value = newMapPair;
                        pair.addModlValue(value);
                    }
                } else {
                    if (!newMapPair.getKey().string.startsWith("_")) {
                        boolean knownItem = false;
                        ModlObject.Map map = null;
                        if (pair.getModlValue() != null) {
                            map = (ModlObject.Map) pair.getModlValue();
                        }
                        if (map == null) {
                            map = modlObject.new Map();
                            pair.addModlValue(map);
                        }
                        if (map.get(newMapPair.getKey()) != null) {
                            knownItem = true;
                        }
                        if (!knownItem) {
                            map.addPair(newMapPair);
                        }
                    }
                }
            }
        }
    }

    private ModlValue makeValueString(ModlObject modlObject, ModlValue value) {
        if (value == null) {
            return null;
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
            newString = "null";
        }


        ModlValue v = modlObject.new String(newString);
        return v;
    }

    private void addMapItemsToPair(ModlObject modlObject, List<RawModlObject.Pair> mapItems, List<ModlObject.Pair> pairs, Object parentPair) {
        if (mapItems == null) {
            return;
        }
        for (RawModlObject.Pair mapItem : mapItems) {
            if (mapItem instanceof RawModlObject.MapConditional) {
                // handle conditionals
                List<ModlObject.Pair> newPairs = interpret(modlObject, (RawModlObject.MapConditional) mapItem, parentPair);
                for (ModlObject.Pair p : newPairs) {
                    pairs.add(p);
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
        ModlObject.Array array = modlObject.new Array();
        for (ModlValue vi : (((ModlObject.Array) rawValueItem)).getValues()) {
            // TODO If this is an ArrayConditional, then get its list of values here
            if (vi instanceof RawModlObject.ArrayConditional) {
                List<ModlValue> newValues = interpret(modlObject, (RawModlObject.ArrayConditional) vi, parentPair);
                for (ModlValue viNew : newValues) {
                    array.addValue(interpret(modlObject, viNew, parentPair));
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
        Map<String, Object> ret = klasses.get(key);
        if (ret != null) {
            return ret;
        }
        return null;
    }

    private boolean haveModlClass(String originalKey) {
        return (getModlClass(originalKey) != null);
    }

    private List<ModlObject.Pair> getPairsFromArray(ModlObject modlObject, RawModlObject.Array array, Object parentPair) {
        return getPairsFromArray(modlObject, array.getValues(), parentPair);
    }

    private List<ModlObject.Pair> getPairsFromArray(ModlObject modlObject, List<ModlValue> arrayItems, Object parentPair) {

        List<ModlObject.Pair> pairs = new LinkedList<>();
        if (arrayItems != null) {
            for (ModlValue arrayItem : arrayItems) {
                if (arrayItem instanceof RawModlObject.ArrayConditional) {
                    List<ModlValue> newArrayItems = interpret(modlObject, (RawModlObject.ArrayConditional) arrayItem, parentPair);
                    for (ModlValue v : newArrayItems) {
                        if (v instanceof ModlObject.Pair) {
                            pairs.add((ModlObject.Pair) v);
                        }
                    }
                } else if (arrayItem != null && arrayItem instanceof ModlObject.Pair) {
                    pairs.add(interpret(modlObject, (ModlObject.Pair) arrayItem, parentPair));
                }
            }
        }
        return pairs;
    }

    private void addAllParentPairs(ModlObject modlObject, ModlObject.Pair pair, String originalKey) {
        Map<String, Object> klass = getModlClass(originalKey);
        for (Map.Entry<String, Object> entry : klass.entrySet()) {
            if (!entry.getKey().startsWith("_") && !(entry.getKey().startsWith("*") && !(entry.getKey().equals("?")))) {
                if (pairHasKey(modlObject, pair, entry.getKey())) {
                    // Only add the new key if it does not already exist in the pair!
                    continue;
                }
                ModlObject.Pair newPair = modlObject.new Pair();
                newPair.setKey(modlObject.new String(entry.getKey()));
                newPair.addModlValue(interpret(modlObject, (ModlValue) (entry.getValue()), null));
                if (pair.getModlValue() != null && pair.getModlValue() instanceof ModlObject.Map) {
                    ((ModlObject.Map) (pair.getModlValue())).addPair(newPair);
                } else {
                    pair.addModlValue(newPair);
                }
            }
        }
    }

    private boolean pairHasKey(ModlObject modlObject, ModlObject.Pair pair, String key) {
        if (pair.getModlValue() == null) {
            return false;
        }
        if (pair.getModlValue() instanceof ModlObject.Pair) {
            if (((ModlObject.Pair) pair.getModlValue()).getKey().equals(key)) {
                return true;
            }
        } else {
            if (pair.getModlValue() instanceof ModlObject.Map) {
                if (((ModlObject.Map) pair.getModlValue()).get(modlObject.new String(key)) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean mapPairAlready(RawModlObject.Pair originalPair) {
        return (originalPair.getModlValue() instanceof ModlObject.Map);
    }

    private boolean anyClassContainsPairs(String originalKey) {
        // If this class, or any of its parent classes, define any pairs, then return true
        // A pair is defined in a class if it has a pair whose key does not start in "_"
        Map<String, Object> klass = getModlClass(originalKey);
        for (String key : klass.keySet()) {
            if (!key.startsWith("_") && !key.startsWith("*") && !key.equals("?")) {
                return true;
            }
        }
        return false;
    }

    private String transformKey(String originalKey) {
        if (getModlClass(originalKey) != null) {
            if ((getModlClass(originalKey).get("*name")) != null &&
                    (getModlClass(originalKey).get("*name")) instanceof ModlObject.String) {
                return ((ModlObject.String) ((getModlClass(originalKey).get("*name")))).string;
            }
            if ((getModlClass(originalKey).get("*n")) != null &&
                    (getModlClass(originalKey).get("*n")) instanceof ModlObject.String) {
                return ((ModlObject.String) ((getModlClass(originalKey).get("*n")))).string;
            }
        }
        return originalKey;
    }

    private RawModlObject.Pair transformValue(ModlObject modlObject, RawModlObject.Pair originalPair) {
        RawModlObject rawModlObject = new RawModlObject();
        if (getModlClass(originalPair.getKey().string) != null) {
            if ((getModlClass(originalPair.getKey().string).get("*name") != null && ((getModlClass(originalPair.getKey().string).get("*name").equals("_v")) ||
                    (getModlClass(originalPair.getKey().string).get("*name").equals("var")))) ||
                    (getModlClass(originalPair.getKey().string).get("*n") != null &&
                            ((getModlClass(originalPair.getKey().string).get("*n").equals("_v")) ||
                                    (getModlClass(originalPair.getKey().string).get("*n").equals("var"))))) {
                VariableLoader.loadConfigNumberedVariables(originalPair.getModlValue(), numberedVariables);
            } else {
                if (getModlClass(originalPair.getKey().string).get("*superclass") != null &&
                        getModlClass(originalPair.getKey().string).get("*superclass").equals("str")) {
                    RawModlObject.Pair pair = rawModlObject.new Pair();
                    pair.setKey(originalPair.getKey());
                    if (originalPair.getModlValue() == null) {
                        return originalPair;
                    }
                    if (originalPair.getModlValue() instanceof ModlObject.String) {
                        return originalPair;
                    }
                    ModlValue value = makeValueString(modlObject, originalPair.getModlValue());
                    ModlValue v = rawModlObject.new String(((ModlObject.String) value).string);
                    pair.addModlValue(v);
                    return pair;
                }
            }
        }
        return originalPair;
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
            return interpret(modlObject, (ModlObject.Number) rawValue);
        }
        if (rawValue instanceof ModlObject.True) {
            return interpret(modlObject, (ModlObject.True) rawValue);
        }
        if (rawValue instanceof ModlObject.False) {
            return interpret(modlObject, (ModlObject.False) rawValue);
        }
        if (rawValue instanceof ModlObject.Null) {
            return interpret(modlObject, (ModlObject.Null) rawValue);
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

        ModlObject.Map map = modlObject.new Map();

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

    private List<ModlObject.Pair> interpretMapPair(ModlObject modlObject, RawModlObject.Pair originalMapItem, Object parentPair) {
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
        ModlObject.Array array = modlObject.new Array();

        if (rawArray.getValues() != null) {
            for (ModlValue originalArrayItem : rawArray.getValues()) {
                ModlValue value = interpret(modlObject, originalArrayItem, parentPair);
                if (value != null) {
                    array.addValue(value);
                    if (parentPair != null) {
                        ((List) parentPair).add(value);
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
    private ModlValue interpret(ModlObject modlObject, RawModlObject.ValueConditional originalConditional, Object parentPair) {
        if (originalConditional == null) {
            return null;
        }
        for (Map.Entry<RawModlObject.ConditionTest, RawModlObject.ValueConditionalReturn> originalConditionalEntry : originalConditional.getConditionals().entrySet()) {
            // Does this conditional evaluate?
            RawModlObject.ConditionTest conditionalTest = originalConditionalEntry.getKey();
            if (evaluates(conditionalTest)) {
                if (originalConditionalEntry.getValue() == null) {
                    return modlObject.new True();
                }
                if (originalConditionalEntry.getValue().getValues().size() == 1) {
                    return interpret(modlObject, originalConditionalEntry.getValue().getValues().get(0), parentPair);
                }
                ModlValue returnValue = modlObject.new Array();
                for (ModlValue valueItem : originalConditionalEntry.getValue().getValues()) {
                    ModlValue v = interpret(modlObject, valueItem, parentPair);
                    ((ModlObject.Array) returnValue).addValue(v);
                }
                return returnValue;
            }
            if (originalConditionalEntry.getValue() == null) {
                return modlObject.new False();
            }

        }
        return null;
    }

    private List<ModlValue> interpret(ModlObject modlObject, RawModlObject.ArrayConditional rawConditional, Object parentPair) {
        if (rawConditional == null) {
            return null;
        }
        if (rawConditional.getConditionals() != null) {
            for (Map.Entry<RawModlObject.ConditionTest, RawModlObject.ArrayConditionalReturn> originalConditionalEntry : rawConditional.getConditionals().entrySet()) {
                // Does this conditional evaluate?
                RawModlObject.ConditionTest conditionalTest = originalConditionalEntry.getKey();
                if (evaluates(conditionalTest)) {
                    // NEED TO INTERPRET THE VALUES!!!
                    List<ModlValue> returnValues = new LinkedList<>();
                    for (ModlValue arrayItem : originalConditionalEntry.getValue().getValues()) {
                        List<ModlValue> values = interpretArrayItem(modlObject, arrayItem, parentPair);
                        for (ModlValue v : values) {
                            returnValues.add(v);
                        }
                    }
                    return returnValues;
                }
            }
        }
        return null;
    }

    private List<ModlObject.Pair> interpret(ModlObject modlObject, RawModlObject.MapConditional originalConditional, Object parentPair) {
        if (originalConditional == null) {
            return null;
        }
        for (Map.Entry<RawModlObject.ConditionTest, RawModlObject.Map> originalConditionalEntry : originalConditional.getConditionals().entrySet()) {
            // Does this conditional evaluate?
            RawModlObject.ConditionTest conditionalTest = originalConditionalEntry.getKey();
            if (evaluates(conditionalTest)) {
                // NEED TO INTERPRET THE VALUES!!!
                List<ModlObject.Pair> returnPairs = new LinkedList<>();
                for (RawModlObject.Pair mapItem : originalConditionalEntry.getValue().getPairs()) {
                    List<ModlObject.Pair> mapItems = interpretMapPair(modlObject, mapItem, parentPair);
                    for (ModlObject.Pair p : mapItems) {
                        returnPairs.add(p);
                    }
                }
                return returnPairs;
            }
        }
        return null;
    }

    private List<ModlObject.Structure> interpret(ModlObject modlObject, RawModlObject.TopLevelConditional originalConditional) {
        if (originalConditional == null) {
            return null;
        }
        for (Map.Entry<RawModlObject.ConditionTest, RawModlObject.TopLevelConditionalReturn> originalConditionalEntry : originalConditional.getConditionals().entrySet()) {
            // Does this conditional evaluate?
            RawModlObject.ConditionTest conditionalTest = originalConditionalEntry.getKey();
            if (evaluates(conditionalTest)) {
                // NEED TO INTERPRET THE VALUES!!!
                List<ModlObject.Structure> returnStructures = new LinkedList<>();
                for (RawModlObject.Structure rawStructure : originalConditionalEntry.getValue().getStructures()) {
                    List<ModlObject.Structure> structures = interpret(modlObject, rawStructure);
                    if (structures != null) {
                        for (ModlObject.Structure structure : structures) {
                            returnStructures.add(structure);
                        }
                    }
                }
                return returnStructures;
            }
        }
        return null;
    }


    private boolean evaluates(RawModlObject.ConditionTest conditionalTest) {
        int nullCount = 0;
        List<Map.Entry<RawModlObject.SubCondition, ImmutablePair<java.lang.String, Boolean>>> conditionalTestOrderedList = new LinkedList<>();
        for (Map.Entry<RawModlObject.SubCondition, ImmutablePair<java.lang.String, Boolean>> conditionalTestEntry : conditionalTest.getSubConditionMap().entrySet()) {
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
        List<ImmutablePair<RawModlObject.ConditionTest, String>> orderedConditionalTestList = getOrderedConditionalTestList(conditionGroup);
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
                if (valuePairs.get(((ModlObject.String) ((ModlObject.String) (values.get(0)))).string) == null) {
                    return false;
                }
                ModlValue valueEntry = (valuePairs.get(((ModlObject.String) values.get(0)).string));
                if (valueEntry instanceof ModlObject.True) {
                    return true;
                }
                if (valueEntry instanceof ModlObject.False) {
                    return false;
                }
                // DO WE NEED TO TRY TO INTERPRET THE VALUEE?!
                return true;
            }
        }
        while (keyString.startsWith("_") || keyString.startsWith("%")) {
            keyString = keyString.substring(1, keyString.length());
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
            String val = transformConditionalArgument(valObj.toString());
            if (val.startsWith("%")) {
                val = val.substring(1, val.length());
            }
            if (conditionOperator.equals("=")) {
                if (conditionalEquals(key.toString(), val)) {
                    return true;
                }
                if (conditionalEquals(key.toString(), valObj)) {
                    return true;
                }
                if (conditionalEquals(key.toString(), transformString(val))) {
                    return true;
                }
                if (conditionalEquals(key.toString(), transformString(valObj))) {
                    return true;
                }
                return false;
            }
            if (conditionOperator.equals("!=")) {
                return !conditionalEquals(key.toString(), val);
            }
            Float valFloat = new Float(val.toString());
            Float keyFloat = new Float(key.toString());
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
        return false;
    }

    private boolean conditionalEquals(String key, Object val) {
        if (val.toString().contains("*")) {
            return conditionalWildcardEquals(key, val.toString());
        }
        return key.equals(val.toString());
    }

    private boolean conditionalWildcardEquals(String key, Object val) {
        // Build a pattern matching string, using HEAD and TAIL operators, and adding wildcards inbetween the phrases, wherever we see a *
        String regex = "";
        if (!(val.toString().startsWith("*"))) {
            regex = "^";
        } else {
            regex = ".*";
        }

        String[] splits = val.toString().split("\\*");
        int i = 0;
        for (String split : splits) {
            if (split.equals("")) {
                continue;
            }
            if (i++ > 0) {
                regex += ".*";
            }
            regex += split;
        }
        if (!(val.toString().endsWith("*"))) {
            regex += "$";
        } else {
            regex += ".*";
        }
        return key.matches(regex);
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
        return origKeyString;
    }

    private ModlObject.False interpret(ModlObject modlObject, RawModlObject.False falseVal) {
        if (falseVal != null) {
            ModlObject.False f = modlObject.new False();
            return f;
        }
        return null;
    }

    private ModlObject.Null interpret(ModlObject modlObject, RawModlObject.Null val) {
        if (val != null) {
            ModlObject.Null n = modlObject.new Null();
            return n;
        }
        return null;
    }

    private ModlObject.True interpret(ModlObject modlObject, RawModlObject.True trueVal) {
        if (trueVal != null) {
            ModlObject.True t = modlObject.new True();
            return t;
        }
        return null;
    }

    private ModlObject.Number interpret(ModlObject modlObject, RawModlObject.Number originalNumber) {
        if (originalNumber != null) {
            ModlObject.Number number = modlObject.new Number(originalNumber.number);
            return number;
        }
        return null;
    }

    private ModlValue interpret(RawModlObject.String string) {
        if (string != null) {
            ModlValue value = transformString(string.string);
            return value;
        }
        return null;
    }

    private ModlValue transformString(String s) {
        StringTransformer stringTransformer = new StringTransformer(valuePairs, variables, numberedVariables);
        return stringTransformer.transformString(s);
    }
}
