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
import uk.modl.parser.ModlObjectCreator;
import uk.modl.parser.RawModlObject;
import uk.modl.parser.printers.JsonPrinter;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;

public class Interpreter {

    Map<String, Function<String, String>> variableMethods = new HashMap<>();
    Map<String, Map<String, Object>> klasses = new LinkedHashMap<>();
    Map<String, ModlObject.Value> variables = new HashMap<>();
    Map<Integer, ModlObject.Value> numberedVariables = new HashMap<>();

    Set<String> pairNames; // TODO Get rid of this!
    Map<String, ModlObject.Value> valuePairs;

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

    public Interpreter() {
        variableMethods = VariableMethods.getConstantVariableMethods();
    }

    private ModlObject interpretPrivate(RawModlObject rawModlObject) throws RequireRestart {
        ModlObject modlObject = new ModlObject();
//        modlClassLoader = new ModlClassLoader();
        ModlClassLoader.loadModlKlassO(rawModlObject, klasses);
        pairNames = new HashSet<>();
        valuePairs = new HashMap<>();

        // Interpret rawModlObject based on specified config files
        boolean startedInterpreting = false;
        boolean needRestart = false;
        RawModlObject loadedRawModlObject = null;
        String importFileValue = null;
        for (RawModlObject.Structure rawStructure : rawModlObject.getStructures()) {

            if (rawStructure instanceof ModlObject.Pair && (((ModlObject.Pair) rawStructure).getKey().string.equals("*I") ||
                    (((ModlObject.Pair) rawStructure).getKey().string.equals("*IMPORT")))) {
                if (startedInterpreting) {
//                    throw new UnsupportedOperationException("Cannot have *I config file after other objects");
                }
                // Load in the config file specified by the "I" object
                if (((ModlObject.Pair) rawStructure).getValues().get(0) instanceof ModlObject.String) {
                    importFileValue = ((ModlObject.String) ((ModlObject.Pair) rawStructure).getValues().get(0)).string;
                    loadedRawModlObject = loadConfigFile(importFileValue);
                    needRestart = true;
                    break;
                }
                continue;
            }
            if (rawStructure instanceof ModlObject.Pair &&
                    ((((((ModlObject.Pair) rawStructure).getKey().string.equals("*class")) ||
                            (((ModlObject.Pair) rawStructure).getKey().string.equals("*c"))) || (
                            (((ModlObject.Pair) rawStructure).getKey().string.equals("*m"))) || ((((ModlObject.Pair) rawStructure).getKey().string.equals("*method"))) ||
                            (((ModlObject.Pair) rawStructure).getKey().string.equals("?"))
                    ))) {
                ModlObject.Pair pair = (ModlObject.Pair) rawStructure;
                if (pair.getKey().string.equals("*class") || pair.getKey().string.equals("*c")) {
                    // TODO ModlClassLoader should only be used for loading classes!
                    // TODO VariableMethodLoader should be called directly for new methods
                    // TODO Object index and variables should be handled by VariableLoader
                    ModlClassLoader.loadClass(rawStructure, klasses);
                    continue;
            } else if (pair.getKey().string.equals("*method") || pair.getKey().string.equals("*m")) {
                VariableMethodLoader.loadVariableMethod(pair, variableMethods);
            } else if (pair.getKey().string.equals("?")) {

                if (pair.getValues().size() == 1 && pair.getValues().get(0) instanceof ModlObject.Array) {
                    VariableLoader.loadConfigNumberedVariables(((ModlObject.Array) pair.getValues().get(0)).getValues(), numberedVariables);
                } else {
                    VariableLoader.loadConfigNumberedVariables(pair.getValues(), numberedVariables);
                }
            } else {
                if (pair.getKey().string.startsWith("_")) {
                    VariableLoader.loadConfigVar(pair.getKey().string.replaceFirst("_", ""), pair, variables);
                } else if (pair.getKey().string.startsWith("*")) {
                    throw new RuntimeException("Unrecognised configuration instruction : " + pair.getKey());
                }
            }
        } else if (rawStructure instanceof ModlObject.Pair && (((ModlObject.Pair) rawStructure).getKey().string.startsWith("*"))) {
                throw new RuntimeException("Unrecognised configuration instruction : " + ((ModlObject.Pair) rawStructure).getKey().string);
            }
            if (!(rawStructure instanceof ModlObject.Pair && (((ModlObject.Pair) rawStructure).getKey().string.equals("V_") ||
                    (((ModlObject.Pair) rawStructure).getKey().string.equals("version_"))))) {
                startedInterpreting = true;
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

    // TODO This is no longer right!
    // TODO We actually need to load int he file, and create a new RawModlObject from it
    // TODO We then need to add this to the current RawModlObject, replacing the import instruction FOR THAT FILE ONLY
    // TODO    - i.e *I=myfile would disappear, replaced by the new object, but *I=file1:file2 would be replaced by the object created from file1, followed by *I=file2
    // TODO We then need to start interpreting it all over again.
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
        // TODO ADD THE NEW RAWMODLOBJECT TO THE EXISTING RAWMODLOBJECT, REPLACING THE IMPORT INSTRUCTION FOR THIS ONE FILE
        return rawModlObject;
//        modlClassLoader.loadConfig(configRawModlObject, variableMethods);
//        // Now we've loaded it, stick it in the map so we can use it next time
//        return modlClassLoader;
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
                if (pair != null) {
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
        if (rawPair.getKey() != null) {
            if (rawPair.getKey().string.equals("?")) {
                if (rawPair.getValues().size() == 1 && rawPair.getValues().get(0) instanceof ModlObject.Array) {
                    VariableLoader.loadConfigNumberedVariables(((ModlObject.Array) rawPair.getValues().get(0)).getValues(), numberedVariables);
                } else {
                    VariableLoader.loadConfigNumberedVariables(rawPair.getValues(), numberedVariables);
                }
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
//                if (pairNames.contains(newKey)) {
                    throw new RuntimeException(newKey + " can't be defined again as upper-case keys are immutable");
                }
            }
        }
        if (!pairNames.contains("_" + newKey)) {
            if (parentPair == null && !(newKey.startsWith("%")) && addToValuePairs) {
//            if (parentPair == null && !(newKey.startsWith("%"))) {
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

        for (RawModlObject.Value value : rawPair.getValues()) {
            // Is this a variable prefixed by "%"?
            if (value != null &&
                    value instanceof ModlObject.Pair
                    && ((ModlObject.Pair) value).getKey().string.startsWith("%")) {
                String key = ((ModlObject.Pair) value).getKey().string;
                // If so, then look up the reference!!
                if (pairNames.contains(key.replaceFirst("%", "_"))) {
                    ModlObject.Value storedValue = valuePairs.get(key.replaceFirst("%", ""));
                        if (storedValue instanceof ModlObject.Map) {
                            ModlObject.Map map = (ModlObject.Map) storedValue;
                            List<ModlObject.Value> values = map.get(((ModlObject.String) transformString(((ModlObject.String) ((ModlObject.Array) ((ModlObject.Pair) rawPair.getValues().get(0)).getValues().get(0)).get(0)).string)));
                            for (ModlObject.Value v : values) {
                                pair.addValue(v);
                            }
                        } else if (storedValue instanceof ModlObject.Array) {
                            List<ModlObject.Value> list = ((ModlObject.Array) storedValue).getValues();
                            Integer index = null;
                            if (((ModlObject.Pair) value).getValues().get(0) instanceof ModlObject.Number) {
                                index = new Integer(((ModlObject.Number) (((ModlObject.Pair) value).getValues().get(0))).number);
                            } else {
                                index = new Integer(((ModlObject.Number) ((ModlObject.Array) (((ModlObject.Pair) value).getValues().get(0))).get(0)).number);
                            }
                            pair.addValue(list.get(index));
                        }
                }
            } else {
                pair.addValue(interpret(modlObject, value, parentPair));
            }

        }

        return pair;
    }

    private boolean generateModlClassObject(ModlObject modlObject, RawModlObject.Pair rawPair, ModlObject.Pair pair,
                                            String originalKey, String newKey, Object parentPair) {
        pair.setKey(modlObject.new String(newKey));

        int numParams = 0;
        if (rawPair.getValues() != null && rawPair.getValues().size() > 0 && rawPair.getValues().get(0) != null) {
            if ((!(rawPair.getValues().get(0) instanceof ModlObject.Array)) &&
                    rawPair.getValues().get(0) != null &&
                    rawPair.getValues().get(0) instanceof ModlObject.Map) {
                rawPair.addValue((ModlObject.Map) rawPair.getValues().get(0));
            } else {
                if (rawPair.getValues().get(0) instanceof ModlObject.Array) {
                    numParams = ((ModlObject.Array) rawPair.getValues().get(0)).getValues().size();
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

            if (rawPair.getValues().get(0) instanceof ModlObject.Array) {
                pairs = getPairsFromArray(modlObject, (ModlObject.Array) (rawPair.getValues().get(0)), parentPair);
                wasArray = true;
            }

            if (mapPairAlready(rawPair)) {
                pairs = new LinkedList<>();
                addMapItemsToPair(modlObject, ((ModlObject.Map) (rawPair.getValues().get(0))).getPairs(), pairs, parentPair);
            }

            if (pairs != null) {
                // Make all the new map values in the new map pair
                makeNewMapPair(modlObject, pair, pairs, wasArray, parentPair);
            }
            if ((!(rawPair.getValues().get(0) instanceof ModlObject.Pair)) && (!(rawPair.getValues().get(0) instanceof ModlObject.Map))) {
                if (!hasParams) {
                    // Don't need a pair here - continue
                    for (RawModlObject.Value originalValueItem : rawPair.getValues()) {
                        ModlObject.Value value = interpret(modlObject, originalValueItem, parentPair);
                        pair.addValue(value);
                    }
                } else {
                    int paramNum = 0;
                    List<RawModlObject.Value> params = (List<RawModlObject.Value>) obj;
                    String currentClass = null;


                    for (RawModlObject.Value valueItem : rawPair.getValues()) {

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
                                fullClassName = ((ModlObject.String) ((RawModlObject.Value) getModlClass(currentClass).get("*name"))).string;
                            } catch (Exception e) {
                            }
                            valuePair.setKey(modlObject.new String(fullClassName));
                            for (RawModlObject.Value vi : ((ModlObject.Array) valueItem).getValues()) {
                                int valueItemSize = 1;
                                if (valueItem instanceof ModlObject.Array) {
                                    valueItemSize = ((ModlObject.Array) valueItem).getValues().size();
                                }

                                String superclass = ((String) (getModlClass(currentClass).get("*superclass")));
                                if (superclass.equals("arr")) {
                                    ModlObject.Value v = interpret(modlObject, vi, parentPair);
                                    valuePair.addValue(v);
                                } else if (superclass.equals("map")) {
                                    ModlObject.String innerClassName = ((ModlObject.String) ((Map.Entry<String, LinkedList<RawModlObject.Value>>) ((LinkedHashMap<String,
                                            LinkedList<ModlObject.Value>>) modlClassObj).entrySet().toArray()[valueItemSize])
                                            .getValue().get(innerParamNum++));

                                    RawModlObject.Pair newRawPair = rawModlObject.new Pair();
                                    newRawPair.setKey(innerClassName);
                                    newRawPair.addValue(vi);

                                    ModlObject.Value v = interpret(modlObject, newRawPair, parentPair);

                                    //  And add it to the pair
                                    valuePair.addValue(v);
                                    innerPair.addValue(valuePair);
                                } else {
                                    throw new RuntimeException("Superclass " + superclass + " of " + fullClassName + " is not known!");
                                }
                            }
                            pair.addValue(valuePair);
                        } else {
                            ModlObject.Value newValue = interpret(modlObject, valueItem, parentPair);
                            ModlObject.Pair valuePair = modlObject.new Pair();
                            String fullClassName = currentClass; // TODO GET THIS FROM THE MODLOBJ!!
                            try {
                                fullClassName = ((ModlObject.String) ((RawModlObject.Value) getModlClass(currentClass).get("*name"))).string;
                            } catch (Exception e) {
                            }
                            valuePair.setKey(modlObject.new String(fullClassName));
                            valuePair.addValue(newValue);
                            pair.addValue(valuePair);
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

    private int getNumParams(RawModlObject.Pair originalPair, int numParams) {
        if (originalPair.getValues().get(0) instanceof ModlObject.Map) {
            numParams = ((ModlObject.Map) originalPair.getValues().get(0)).getPairs().size();
        } else if (originalPair.getValues().get(0) instanceof ModlObject.Array) {
            numParams = ((ModlObject.Array) originalPair.getValues().get(0)).getValues().size();
        } else if (originalPair.getValues().get(0) != null) {
            numParams = originalPair.getValues().size(); // TODO !!!
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
                if (originalPair.getValues().get(0) instanceof ModlObject.Map) {
                    originalPair.setKey(rawModlObject.new String(transformedKey));
                    Map newMap = new HashMap();
                    interpret(rawModlObject, (ModlObject.Map) (originalPair.getValues().get(0)), newMap);
                }
                if (originalPair.getValues().get(0) instanceof ModlObject.Array) {
                    originalPair.setKey(rawModlObject.new String(transformedKey));
                    List newList = new LinkedList();
                    interpret(rawModlObject, (ModlObject.Array) (originalPair.getValues().get(0)), newList);
                }
            }
            if (originalPair.getValues().get(0) instanceof ModlObject.String) {
                valuePairs.put(transformedKey, transformString(((ModlObject.String)(originalPair.getValues().get(0))).string)); // TODO IS THIS RIGHT?!
            } else {
                valuePairs.put(transformedKey, originalPair.getValues().get(0)); // TODO IS THIS RIGHT?!
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
        ModlObject.Value v = originalPair.getValues().get(0);
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
                    ModlObject.Value value = null;
                    if (!newMapPair.getKey().string.startsWith("_")) {
                        value = newMapPair;
                        pair.addValue(value);
                    }
                } else {
                    if (!newMapPair.getKey().string.startsWith("_")) {
                        boolean knownItem = false;
                        ModlObject.Map map = null;
                        if (pair.getValues().size() > 0) {
                            map = (ModlObject.Map) pair.getValues().get(0);
                        }
                        if (map == null) {
                            map = modlObject.new Map();
                            pair.addValue(map);
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

    private ModlObject.Value makeValueString(ModlObject modlObject, RawModlObject.Value value) {
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


        ModlObject.Value v = modlObject.new String(newString);
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

    private ModlObject.Value interpret(ModlObject modlObject, RawModlObject.Value rawValueItem, Object parentPair) {
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
        for (RawModlObject.Value vi : (((ModlObject.Array) rawValueItem)).getValues()) {
            array.addValue(interpret(modlObject, vi, parentPair));
        }
        return array;
    }

    private Map<String, Object> getModlClass(String key) {
        Map<String, Object> ret = klasses.get(key);
        if (ret != null) {
            return ret;
        }
        for (Map.Entry<String, Map<String, Object>> entry : klasses.entrySet()) {
            for (Map.Entry<String, Object> valueEntry : entry.getValue().entrySet()) {
                if (valueEntry.getKey().equals("*name") || valueEntry.getKey().equals("*n")) {
                    if (((ModlObject.String) (valueEntry.getValue())).string.equals(key)) {
                        return entry.getValue();
                    }
                }
            }
        }
        return null;
    }

    private boolean haveModlClass(String originalKey) {
        return (getModlClass(originalKey) != null);
    }

    private List<ModlObject.Pair> getPairsFromArray(ModlObject modlObject, RawModlObject.Array array, Object parentPair) {
        return getPairsFromArray(modlObject, array.getValues(), parentPair);
    }

    private List<ModlObject.Pair> getPairsFromArray(ModlObject modlObject, List<RawModlObject.Value> arrayItems, Object parentPair) {

        List<ModlObject.Pair> pairs = new LinkedList<>();
        if (arrayItems != null) {
            for (RawModlObject.Value arrayItem : arrayItems) {
                if (arrayItem instanceof RawModlObject.ArrayConditional) {
                    List<ModlObject.Value> newArrayItems = interpret(modlObject, (RawModlObject.ArrayConditional) arrayItem, parentPair);
                    for (ModlObject.Value v : newArrayItems) {
                        pairs.add((ModlObject.Pair) v);
                    }
                }
                if (arrayItem != null && arrayItem instanceof ModlObject.Pair) {
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
                newPair.addValue(interpret(modlObject, (RawModlObject.Value) (entry.getValue()), null));
                if (pair.getValues().size() == 1 && pair.getValues().get(0) instanceof ModlObject.Map) {
                    ((ModlObject.Map) (pair.getValues().get(0))).addPair(newPair);
                } else {
                    pair.addValue(newPair);
                }
            }
        }
    }

    private boolean pairHasKey(ModlObject modlObject, ModlObject.Pair pair, String key) {
        if (pair.getValues().size() == 0) {
            return false;
        }
        if (pair.getValues().get(0) instanceof ModlObject.Pair) {
            if (((ModlObject.Pair) pair.getValues().get(0)).getKey().equals(key)) {
                return true;
            }
        } else {
            if (pair.getValues().get(0) instanceof ModlObject.Map) {
                if (((ModlObject.Map) pair.getValues().get(0)).get(modlObject.new String(key)) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean mapPairAlready(RawModlObject.Pair originalPair) {
        return (originalPair.getValues().get(0) instanceof ModlObject.Map);
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
//                modlClassLoader.loadConfigNumberedVariables(originalPair.getValues());
                VariableLoader.loadConfigNumberedVariables(originalPair.getValues(), numberedVariables);
            } else {
                if (getModlClass(originalPair.getKey().string).get("*superclass") != null &&
                        getModlClass(originalPair.getKey().string).get("*superclass").equals("str")) {
                    RawModlObject.Pair pair = rawModlObject.new Pair();
                    pair.setKey(originalPair.getKey());
                    if (originalPair.getValues().get(0) == null) {
                        return originalPair;
                    }
                    if (originalPair.getValues().get(0) instanceof ModlObject.String) {
                        return originalPair;
                    }
                    ModlObject.Value value = makeValueString(modlObject, originalPair.getValues().get(0));
                    RawModlObject.Value v = rawModlObject.new String(((ModlObject.String) value).string);
                    pair.addValue(v);
                    return pair;
                }
            }
        }
        return originalPair;
    }

    private ModlObject.Value interpretValue(ModlObject modlObject, RawModlObject.Value rawValue, Object parentPair) {
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
            for (RawModlObject.Value originalArrayItem : rawArray.getValues()) {
                ModlObject.Value value = interpret(modlObject, originalArrayItem, parentPair);
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

    private List<ModlObject.Value> interpretArrayItem(ModlObject modlObject, RawModlObject.Value rawArrayItem, Object parentPair) {
        if (rawArrayItem == null) {
            return null;
        }
        List<ModlObject.Value> values = new LinkedList<>();

        if (rawArrayItem instanceof RawModlObject.ArrayConditional) {
            values = interpret(modlObject, (RawModlObject.ArrayConditional) rawArrayItem, parentPair);
        } else {
            ModlObject.Value value = interpret(modlObject, rawArrayItem, parentPair);
            values.add(value);
        }
        return values;
    }

    // We don't want to return this as a conditional any more!
    // We want to return it as whatever the conditional evaluates to
    private ModlObject.Value interpret(ModlObject modlObject, RawModlObject.ValueConditional originalConditional, Object parentPair) {
        if (originalConditional == null) {
            return null;
        }
        for (Map.Entry<RawModlObject.ConditionTest, RawModlObject.ValueConditionalReturn> originalConditionalEntry : originalConditional.getConditionals().entrySet()) {
            // Does this conditional evaluate?
            RawModlObject.ConditionTest conditionalTest = originalConditionalEntry.getKey();
            if (evaluates(conditionalTest)) {
                if (originalConditionalEntry.getValue().getValues().size() == 1) {
                    return interpret(modlObject, originalConditionalEntry.getValue().getValues().get(0), parentPair);
                }
                ModlObject.Value returnValue = modlObject.new Array();
                for (RawModlObject.Value valueItem : originalConditionalEntry.getValue().getValues()) {
                    ModlObject.Value v = interpret(modlObject, valueItem, parentPair);
                    ((ModlObject.Array) returnValue).addValue(v);
                }
                return returnValue;
            }
        }
        return null;
    }

    private List<ModlObject.Value> interpret(ModlObject modlObject, RawModlObject.ArrayConditional rawConditional, Object parentPair) {
        if (rawConditional == null) {
            return null;
        }
        if (rawConditional.getConditionals() != null) {
            for (Map.Entry<RawModlObject.ConditionTest, RawModlObject.ArrayConditionalReturn> originalConditionalEntry : rawConditional.getConditionals().entrySet()) {
                // Does this conditional evaluate?
                RawModlObject.ConditionTest conditionalTest = originalConditionalEntry.getKey();
                if (evaluates(conditionalTest)) {
                    // NEED TO INTERPRET THE VALUES!!!
                    List<ModlObject.Value> returnValues = new LinkedList<>();
                    for (RawModlObject.Value arrayItem : originalConditionalEntry.getValue().getValues()) {
                        List<ModlObject.Value> values = interpretArrayItem(modlObject, arrayItem, parentPair);
                        for (ModlObject.Value v : values) {
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
        List<ImmutablePair<RawModlObject.ConditionTest, java.lang.String>> orderedConditionalTestList = new LinkedList<>();
        int nullCount = 0;
        for (ImmutablePair<RawModlObject.ConditionTest, java.lang.String> conditionalTestEntry : conditionGroup.getConditionsTestList()) {
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
        List<RawModlObject.Value> values = condition.getValues();
        // Try to do object referencing and string transformation on the key
        if (keyString == null) {
            if (values.get(0) instanceof ModlObject.True) {
                return true;
            }
            if (values.get(0) instanceof ModlObject.False) {
                return false;
            }
            ModlObject.Value transformedName = transformString(((ModlObject.String) (values.get(0))).string);
            if (transformedName instanceof ModlObject.True) {
                return true;
            }
            if (transformedName instanceof ModlObject.False) {
                return false;
            }
            if (values.get(0) instanceof ModlObject.String) {
                if (valuePairs.get(((ModlObject.String) ((ModlObject.String)(values.get(0)))).string) == null) {
                    return false;
                }
                ModlObject.Value valueEntry = (valuePairs.get(((ModlObject.String) values.get(0)).string));
                if (valueEntry instanceof ModlObject.True) {
                    return true;
                }
                if (valueEntry instanceof ModlObject.False) {
                    return false;
                }
                // DO WE NEED TO TRY TO INTERPRET THE VALUEE?!
                return true;
            }
//            if (valuePairs.get())
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
            for (RawModlObject.Value value : values) {
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

    private String getObjectFromValueForCondition(RawModlObject.Value value) {
        if (value instanceof ModlObject.String) {
            return ((ModlObject.String) value).string;
        }
        if (value instanceof ModlObject.Number) {
            return ((ModlObject.Number) value).number;
        }
        return null;
    }

    private String transformConditionalArgument(String origKeyString) {
        StringTransformer stringTransformer = new StringTransformer(valuePairs, variableMethods, variables, numberedVariables);
        ModlObject.Value objectRef = stringTransformer.runObjectReferencing("%" + origKeyString, "%" + origKeyString, false);
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

    private ModlObject.Value interpret(RawModlObject.String string) {
        if (string != null) {
            ModlObject.Value value = transformString(string.string);
            return value;
        }
        return null;
    }

    private ModlObject.Value transformString(String s) {
        StringTransformer stringTransformer = new StringTransformer(valuePairs, variableMethods, variables, numberedVariables);
        return stringTransformer.transformString(s);
    }
}
