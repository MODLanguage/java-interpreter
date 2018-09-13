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
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;

public class Interpreter {

    public static Integer CONFIG_VERSION = 0;

    private ModlClassLoader modlClassLoader = null;

    Map<String, Function<String, String>> variableMethods = new HashMap<>();

    Set<String> pairNames;
    Map<String, String> stringPairs;
    Map<String, Map> mapPairs;
    Map<String, List> arrayPairs;
    Map<String, Boolean> boolPairs;

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
        return interpreter.interpretPrivate(rawModlObject);
    }

    public Interpreter() {
        variableMethods = VariableMethods.getConstantVariableMethods();
    }

    private ModlObject interpretPrivate(RawModlObject rawModlObject) {
        ModlObject modlObject = new ModlObject();
        modlClassLoader = new ModlClassLoader();
        pairNames = new HashSet<>();
        stringPairs = new HashMap<>();
        mapPairs = new HashMap<>();
        arrayPairs = new HashMap<>();
        boolPairs = new HashMap<>();

        // Interpret rawModlObject based on specified config files

        // The R object will no longer be in the rawModlObject - the MODL ModlObjectCreator will have stripped it out
        // If there are any S or I objects, they will be at the front of the rawModlObject.
        boolean startedInterpreting = false;
        for (RawModlObject.Structure rawStructure : rawModlObject.getStructures()) {

            if (rawStructure.getPair() != null && (rawStructure.getPair().getKey().equals("*I") || (rawStructure.getPair().getKey().equals("*IMPORT")))) {
                if (startedInterpreting) {
//                    throw new UnsupportedOperationException("Cannot have *I config file after other objects");
                }
                // Load in the config file specified by the "I" object
                if (rawStructure.getPair().getValueItems().get(0).getValue().getString() != null) {
                    loadConfigFile(rawModlObject, rawStructure.getPair().getValueItems().get(0).getValue().getString().string);
                }
                if (rawStructure.getPair().getValueItems().get(0).getValue().getQuoted() != null) {
                    loadConfigFile(rawModlObject, rawStructure.getPair().getValueItems().get(0).getValue().getQuoted().string);
                }
                continue;
            }
            // TODO We should be able to spot this every time we load a pair, and add it to a local config at that point
            if (rawStructure.getPair() != null && ((((rawStructure.getPair().getKey().equals("*class")) ||
                    (rawStructure.getPair().getKey().equals("*c"))) || ((rawStructure.getPair().getKey().equals("*vm")) ||
                    (rawStructure.getPair().getKey().equals("*m"))) || ((rawStructure.getPair().getKey().equals("*method")) ||
                    (rawStructure.getPair().getKey().equals("*variable_map"))) ||
                    (rawStructure.getPair().getKey().equals("?"))
//                    (structure.getPair().getKey().equals("*")) ||
//                    ((structure.getPair().getKey().equals("*v")) || (structure.getPair().getKey().equals("*var")))
            ))) {
                modlClassLoader.loadconfig(rawStructure, variableMethods);
                continue;
            } else if (rawStructure.getPair() != null && (rawStructure.getPair().getKey().startsWith("*"))) {
                throw new RuntimeException("Unrecognised configuration instruction : " + rawStructure.getPair().getKey());
            }
            if (!(rawStructure.getPair() != null && (rawStructure.getPair().getKey().equals("V_") ||
                    (rawStructure.getPair().getKey().equals("version_"))))) {
                startedInterpreting = true;
            }
            List<ModlObject.Structure> structures = interpret(modlObject, rawStructure);
            if (structures != null) {
                for (ModlObject.Structure structure : structures) {
                    modlObject.addStructure(structure);
                }
            }
        }

        return modlObject;
    }

    private ModlClassLoader loadConfigFile(RawModlObject rawModlObject, String location) {
        // We no longer keep configs around - they can be built up dynamically for each new record that comes in
        // Load the config file!
        String contents = null;
        location = transformString(location);
        // If no .modl or no .txt at end, then add .modl
        if (!location.endsWith(".modl") && !location.endsWith(".txt")) {
            location = location + ".modl";
        }
        if (location.startsWith("http:")) {
            // Load from URI
            try {
                contents = new Scanner(new URL(location).openStream(), "UTF-8").useDelimiter("\\A").next();
            } catch (IOException e) {
                throw new RuntimeException(("Could not make URI : " + location + ", error : " + e));
            }
        } else {
            try {
                contents = new String(Files.readAllBytes(Paths.get(location)));
            } catch (IOException e) {
                throw new RuntimeException(("Could not open file : " + location + ", error : " + e));
            }
        }
        RawModlObject configRawModlObject = null;
        try {
            configRawModlObject = ModlObjectCreator.processModlParsed(contents);
        } catch (IOException e) {
            throw new RuntimeException("Could not interpret " + location + ", error : " + e);
        }
        modlClassLoader.loadConfig(configRawModlObject, variableMethods);
        // Now we've loaded it, stick it in the map so we can use it next time
        return modlClassLoader;
    }

    private List<ModlObject.Structure> interpret(ModlObject modlObject, RawModlObject.Structure rawStructure) {

        if (rawStructure == null) {
            return null;
        }
        List<ModlObject.Structure> structures = new LinkedList<>();
        if (rawStructure.getTopLevelConditional() == null) {
            ModlObject.Structure structure = interpret(modlObject, rawStructure.getMap(), null);
            if (structure != null) {
                structures.add(structure);
            }

            structure = interpret(modlObject, rawStructure.getArray(), null);
            if (structure != null) {
                structures.add(structure);
            }

            structure = interpret(modlObject, rawStructure.getPair(), null);
            ModlObject.Pair pair = (ModlObject.Pair)structure;
            if (pair != null) {
                if (!pair.getKey().string.startsWith("_") &&
                        !pair.getKey().string.startsWith("*") &&
                        !pair.getKey().string.equals("?")) {
                    structures.add(pair);
                }
            }
        } else {
            structures = (interpret(modlObject, rawStructure.getTopLevelConditional()));
        }
        return structures;

    }

    private ModlObject.Pair interpret(ModlObject modlObject, RawModlObject.Pair rawPair, Object parentPair) {
        if (rawPair == null) {
            return null;
        }
        if (rawPair.getKey() != null) {
//            if (originalPair.getKey().startsWith("_")) {
//                if (originalPair.getKey().equals("_v") || originalPair.getKey().equals("_var")) {
                if (rawPair.getKey().equals("?")) {
                    modlClassLoader.loadConfigNumberedVariables(rawPair.getValueItems());
                    return null;
                }
//            }
        }

        ModlObject.Pair pair = modlObject.new Pair();

        String originalKey = rawPair.getKey();
        String newKey = originalKey;
        if (haveModlClass(originalKey)) {
            newKey = transformKey(originalKey);
            rawPair = transformValue(modlObject, rawPair);
        }

        // IF WE ALREADY HAVE A PAIR WITH THIS NAME, AND THE NAME IS UPPER-CASE, THEN RAISE AN ERROR
        if (newKey != null) {
            if (newKey.toUpperCase().equals(newKey)) {
                if (pairNames.contains(newKey)) {
                    throw new RuntimeException(newKey + " can't be defined again as upper-case keys are immutable");
                }
            }
        }
        if (!pairNames.contains("_" + newKey)) {
            if (parentPair == null && !(newKey.startsWith("%"))) {
                pairNames.add(newKey);
            }
//        if (originalPair.getValueItems() != null) {
            transformPairKey(modlObject, rawPair, newKey, parentPair);
//        }
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

        ModlObject.Value value = interpret(modlObject, rawPair.getMap(), parentPair);
        if (value != null) {
            pair.setValue(value);
        } else {
            value = interpret(modlObject, rawPair.getArray(), parentPair);
            if (value != null) {
                pair.setValue(value);
            }
        }
        if (rawPair.getValueItems() != null) {
            for (RawModlObject.ValueItem originalValueItem : rawPair.getValueItems()) {
                // Is this a variable prefixed by "%"?
                if (rawPair.getValueItems() != null &&
                    rawPair.getValueItems().get(0).getValue() != null &&
                        rawPair.getValueItems().get(0).getValue().getPair() != null
                && rawPair.getValueItems().get(0).getValue().getPair().getKey().startsWith("%")) {
                    String key = rawPair.getValueItems().get(0).getValue().getPair().getKey();
                    // If so, then look up the reference!!
                    if (pairNames.contains(key.replaceFirst("%", "_"))) {
                        if (mapPairs.containsKey(key.replaceFirst("%", ""))) {
//                            ModlObject.ValueItem valueItem = modlObject.new ValueItem();
                            Map map = mapPairs.get(key.replaceFirst("%", ""));
                            String result = (String) map.get(transformString(rawPair.getValueItems().get(0).getValue().getPair().getArray().getArrayItems().get(0).getValue().getString().string));
                            result = transformString(result);
//                            RawModlObject.Value val = modlObject.new Value();
//                            val.setString(modlObject.new String(result));
//                            valueItem.setValue(val);
//                            pair.addValueItem(valueItem);
                            pair.addValue(modlObject.new String(result));
                        } else if (arrayPairs.containsKey(key.replaceFirst("%", ""))) {
//                            RawModlObject.ValueItem valueItem = modlObject.new ValueItem();
                            List list = arrayPairs.get(key.replaceFirst("%", ""));
                            String result = ((ModlObject.String)(list.get(new Integer(rawPair.getValueItems().get(0).getValue().getPair().getArray().getArrayItems().get(0).getValue().getNumber().string)))).string;
//                            String result = ((RawModlObject.ArrayItem)(list.get(new Integer(rawPair.getValueItems().get(0).getValue().getPair().getArray().getArrayItems().get(0).getValue().getNumber().string))))
//                                    .getValue().getString().string;
                            result = transformString(result);
//                            RawModlObject.Value val = modlObject.new Value();
//                            val.setString(modlObject.new String(result));
//                            valueItem.setValue(val);
//                            pair.addValueItem(valueItem);
                            pair.addValue(modlObject.new String(result));
                        }
                    }
                } else {
                    pair.addValue(interpret(modlObject, originalValueItem, parentPair));
                }

            }
        }

        return pair;
    }

    private boolean generateModlClassObject(ModlObject modlObject, RawModlObject.Pair rawPair, ModlObject.Pair pair,
                                            String originalKey, String newKey, Object parentPair) {
        return false;
//        pair.setKey(modlObject.new String(newKey));
//
//        // TODO WHEN WE EVALUATE NUMPARAMS, WE NEED TO COUNT THE EVALUATED CONDITIONALS RATHER THAN THE INPUT SIZE!!!
//        int numParams = 0;
//        if (rawPair.getValueItems() != null) {
//            // TODO What if the single value is a map?
//            // TODO We'd then want to process it as if it were a map...
//            if (rawPair.getValueItems().size() == 1 &&
//                    // TODO Remember to handle valueItems and their conditionals here!
//                    // is getValue() null?!
//                    rawPair.getValueItems().get(0).getValue() != null &&
//                    rawPair.getValueItems().get(0).getValue().getMap() != null) {
//                rawPair.setMap(rawPair.getValueItems().get(0).getValue().getMap());
//                rawPair.resetValueItems();
//            } else {
//                // TODO expand the conditionals before counting
//                numParams = rawPair.getValueItems().size();
//            }
//        }
//        numParams = getNumParams(rawPair, numParams);
//        String paramsKeyString = "*params" + numParams;
////            Object obj = modlClassLoader.klasses.get(originalPair.getKey()).get(paramsKeyString);
//        Object obj = getModlClass(rawPair.getKey()).get(paramsKeyString);
//        boolean hasParams = (obj != null);
//
//        // If it's not already a map pair, and one of the parent classes in the class hierarchy includes pairs, then it is transformed to a map pair.
//        if (anyClassContainsPairs(originalKey) || mapPairAlready(rawPair) || (hasParams)) {
////                if (!mapPairAlready(originalPair)) {
//                pair.setValue(modlObject.new Map());
//                pair.setKey(modlObject.new String(newKey)); // TODO Do we need to do this again here?!
////                }
//            //  and the pairs from all parent classes in the class hierarchy are added to the new map pair.
//            // Do this recursively up from the child class, only adding pairs if they are not there already
////            addAllParentPairs(rawModlObject, pair, originalKey);
//
//
//            List<ModlObject.Pair> pairs = null;
//            boolean wasArray = false;
//
//            if (rawPair.getArray() != null) {
//                pairs = getPairsFromArray(modlObject, rawPair.getArray(), parentPair);
////                pair.setArray(modlObject.new Array());
//                pair.setValue(modlObject.new Array());
//                wasArray = true;
//            }
//
//            if (mapPairAlready(rawPair)) {
//                pairs = new LinkedList<>();
//                addMapItemsToPair(modlObject, rawPair.getMap().getMapItems(), pairs, parentPair);
//            }
//
//            if (pairs != null) {
//                // Make all the new map values in the new map pair
//                makeNewMapPair(modlObject, pair, pairs, wasArray, parentPair);
//            }
//            if (rawPair.getValueItems() != null && rawPair.getValueItems().size() > 0) {
//                if (!hasParams) {
//                    // Don't need a pair here - continue
//                    for (RawModlObject.ValueItem originalValueItem : rawPair.getValueItems()) {
//                        ModlObject.Value value = interpret(modlObject, originalValueItem.getValue(), parentPair);
//                        pair.addValue(value);
//                    }
//                } else {
//                    int paramNum = 0;
//                    List<RawModlObject.Value> params = (List<RawModlObject.Value>)obj;
//                    String currentClass = null;
//
//
//                    for (RawModlObject.ValueItem valueItem : rawPair.getValueItems()) {
//
//                        // How about checking if the valueItem has more than one valuitem?
//                        // If it does, then make it a pair, and set the key to the currentClass
//                        if (params.get(paramNum).getString() != null) {
//                            currentClass = params.get(paramNum).getString().string;
//                        }
//                        boolean madePair = false;
//                        if (valueItem.getValueItems() != null && (valueItem.getValueItems().size() > 1)) {
//                            // If it does, then make it a pair, and set the key to the currentClass
//                            madePair = makePairFromValueItems(modlObject, currentClass, valueItem);
//                        }
//
//                        List<RawModlObject.ValueItem> vis = getValueItemsFromValueItem(modlObject, valueItem, parentPair);
//                        for (RawModlObject.ValueItem vi : vis) {
//
//                            List<RawModlObject.Value> originalValues = getValuesFromValueItem(modlObject, vi, parentPair);
//
//                            for (RawModlObject.Value value : originalValues) {
//                                // We now need to deal with the assigned params - we only need to do this if it is a multi-value pair as they will be named otherwise
//                                // Of course, each value could also be named - if it is, then we need to make sure it is the type of object we were expecting
//                                // This includes a single Value!
//                                // Remember that each value can be interpreted as any _class -
//                                // - "pn" might be phone number, so "441270987654" is made to become a phone number map pair
//
//                                if (params.get(paramNum).getString().string.equals("?")) {
//                                    loadConfigFromParam(valueItem, value);
//                                } else {
//                                    // We will need to create a new pair of the appropriate type and interpret it
//                                    if (params.get(paramNum).getString() != null) {
//                                        currentClass = params.get(paramNum).getString().string;
//                                    }
//                                    RawModlObject.Pair newPair = modlObject.new Pair();
//                                    newPair.setKey(params.get(paramNum).getString().string);
//                                    RawModlObject.ValueItem v = modlObject.new ValueItem();
//                                    if (modlClassLoader.klasses.get(params.get(paramNum).getString()) != null) {
//                                        if (modlClassLoader.klasses.get(params.get(paramNum).getString().string).get("*superclass").equals("str")) {
//                                            value = makeValueString(modlObject, value);
//                                        }
//                                    }
//                                    v.setValue(value);
//                                    newPair.addValueItem(v);
//                                    if (value.getPair() != null && isDefinedClassName(value.getPair().getKey())) {
//                                            newPair = value.getPair();
//                                            madePair = false;
//                                    } else {
//                                        // Need to interpret as the right type! e.g. if number, and string required, then interpret as string!
//                                        newPair = interpret(modlObject, newPair, parentPair);
//                                    }
//                                    if (!newPair.getKey().startsWith("_") && !newPair.getKey().startsWith("*") &&
//                                            !(newPair.getKey().equals("?"))) {
////                                    if (!newPair.getKey().startsWith("_") ) {
//                                        RawModlObject.MapItem mapItem = modlObject.new MapItem();
//                                        mapItem.setPair(newPair);
//                                        pair.getMap().addMapItem(mapItem);
//                                    }
//                                }
//
//                            }
//                        }
//                        // If we had to make up a new pair with the a key, then we will have a pair within a pair with that key
//                        // So we need to find the bottom-most pair and bring it up a level
//                        if (madePair) {
//                            currentClass = fixUpNewPair(pair, paramNum, params, currentClass);
//                        }
//                            paramNum++;
//                    }
//                }
//                addAllParentPairs(modlObject, pair, originalKey);
//                return true;
//            }
//            addAllParentPairs(modlObject, pair, originalKey);
//            return true;
//        }
//        return false;
    }

    private int getNumParams(RawModlObject.Pair originalPair, int numParams) {
        if (originalPair.getMap() != null) {
            // TODO expand the conditionals before counting
            numParams = originalPair.getMap().getMapItems().size();
        } else if (originalPair.getArray() != null) {
            // TODO expand the conditionals before counting
            numParams = originalPair.getArray().getArrayItems().size();
        } else if (originalPair.getValueItems() != null) {
            // TODO expand the conditionals before counting
            numParams = originalPair.getValueItems().size();
        }
        return numParams;
    }

    private void transformPairKey(ModlObject rawModlObject, RawModlObject.Pair originalPair, String newKey, Object parentPair) {
        String transformedKey = newKey;
        if (transformedKey.startsWith("_")) {
            transformedKey = transformedKey.replaceFirst("_", "");
        }
//        if (pairNames.contains(transformedKey)) { //  || pairNames.contains("_" + transformedKey)) {
//            return;
//        }
        if (parentPair == null) {
            if (originalPair.getValueItems() != null && originalPair.getValueItems().size() == 1) {
                if (originalPair.getValueItems().get(0).getValue() != null &&
                        originalPair.getValueItems().get(0).getValue().getPair() != null) {
                } else if (originalPair.getValueItems().get(0).getValue() != null &&
                        originalPair.getValueItems().get(0).getValue().getTrueVal() != null) {
                    boolPairs.put(transformedKey, true);
                } else if (originalPair.getValueItems().get(0).getValue() != null &&
                        originalPair.getValueItems().get(0).getValue().getFalseVal() != null) {
                    boolPairs.put(transformedKey, false);
                } else {
                    // String of some sort
                    String str = getStringFromValue(originalPair);
                    stringPairs.put(transformedKey, transformString(str));
                }
            }
            if (newKey.startsWith("_")) {
            if (originalPair.getMap() != null) {
                    originalPair.setKey(transformedKey);
//                mapPairs.put(transformedKey, interpret(rawModlObject, originalPair.getMap(), false));
//                RawModlObject.Map newMap = rawModlObject.new Map();
                    Map newMap = new HashMap();
                    mapPairs.put(transformedKey, newMap);
                    interpret(rawModlObject, originalPair.getMap(), newMap);
            }
            if (originalPair.getArray() != null) {
                originalPair.setKey(transformedKey);
//                arrayPairs.put(transformedKey, interpret(rawModlObject, originalPair.getArray(), false));
//                RawModlObject.Array newArray = rawModlObject.new Array();
                List newList = new LinkedList();
                arrayPairs.put(transformedKey, newList);
//                for (RawModlObject.ValueItem vi : originalPair.getValueItems()) {
//                    interpret(rawModlObject, vi, newList);
//                }
                interpret(rawModlObject, originalPair.getArray(), newList);
            }
            }
            // TODO We need to store the interpreted variable underneath than top-level mapPair or arrayPair
            // TODO So if it is a mapPair, then we should add a new pair underneath map.
            // TODO We should also _NOT_ store the pairName in the top-level
            // TODO If it is an arrayPair, then we should store the value under the arrayPair, and NOT store the pairName
        } else {
            // We have a new definition which must live under an existing mapPair or arrayPair
            // TODO Do we have a mapPair or an arrayPair?
            if (parentPair instanceof Map) {
//                ((RawModlObject.Map)parentPair).addMapItem(interpret(rawModlObject, ))
                String str = getStringFromValue(originalPair);
                ((Map)parentPair).put(transformedKey, str);
            } else if (parentPair instanceof List) {
                String str = getStringFromValue(originalPair);
                ((List)parentPair).add(str);
            } else {
                throw new RuntimeException("Expecting Map or Array as parentPair!");
            }
            // TODO Which does it belong to?
            // TODO How do we add it?
            // TODO Do we even need to? Can we not do the transform when the time comes?
            // TODO Or might things have changed in the environment since the original definition?
        }
    }

    private String getStringFromValue(RawModlObject.Pair originalPair) {
        String str = null;
        if (originalPair.getValueItems().get(0).getValue() != null &&
                originalPair.getValueItems().get(0).getValue().getString() != null) {
            str = originalPair.getValueItems().get(0).getValue().getString().string;
        }
        if (originalPair.getValueItems().get(0).getValue() != null &&
                originalPair.getValueItems().get(0).getValue().getQuoted() != null) {
//                    stringPairs.put(transformedKey, transformString(originalPair.getValueItems().get(0).getValue().getQuoted().string));
            str = originalPair.getValueItems().get(0).getValue().getQuoted().string;
        }
        if (originalPair.getValueItems().get(0).getValue() != null &&
                originalPair.getValueItems().get(0).getValue().getNumber() != null) {
//                    stringPairs.put(transformedKey, transformString(originalPair.getValueItems().get(0).getValue().getNumber().string));
            str = originalPair.getValueItems().get(0).getValue().getNumber().string;
        }
        return str;
    }

    public void makeNewMapPair(ModlObject modlObject, ModlObject.Pair pair, List<RawModlObject.Pair> rawPairs, boolean wasArray, Object parentPair) {
        for (RawModlObject.Pair originalMapPair : rawPairs) {
            // TODO Do we need to check the order and types here?
            // TODO Need to look up pair name (e.g. "n") in classes and load the appropriate type of thing here

            // TODO Watch out for ValueItems here! They can be interpreted into list of ValueItems which each need dealt with recursively
            ModlObject.Pair newMapPair = interpret(modlObject, originalMapPair, parentPair);
            if (newMapPair != null) {
                if (wasArray) {
                    ModlObject.Value value = null;
//                                RawModlObject.Structure structure = rawModlObject.new Structure();
                    if (!newMapPair.getKey().string.startsWith("_")) {
//                        value.setPair(newMapPair);
                        value = newMapPair;
                        pair.addValue(value);
                    }
//                    // TODO Make sure we're using the right type of variable here!
//                    RawModlObject.ArrayItem arrayItem = modlObject.new ArrayItem();
//                    arrayItem.setValue(value);
//                    pair.getArray().addArrayItem(arrayItem);
                } else {
                    if (!newMapPair.getKey().string.startsWith("_")) {
                        boolean knownItem = false;

                        // TODO I think we want to add the new pair to the map contained within the pair?
                        // TODO But ONLY if the key is not already known?
                        ModlObject.Map map = (ModlObject.Map)pair.getValue();
//                        if (map.pairs.containsKey(newMapPair.getKey())) {
                        if (map.get(newMapPair.getKey()) != null) {
                            knownItem = true;
                        }
                        if (!knownItem) {
                            map.addPair(newMapPair);
                        }

//                        RawModlObject.MapItem mapItem = modlObject.new MapItem();
//                        mapItem.setPair(newMapPair);
//                        // TODO HACK!!!
//                        if (pair.getMap().getMapItems() != null) {
//                            for (RawModlObject.MapItem mi : pair.getMap().getMapItems()) {
//                                if (mi.getPair() != null &&
//                                        mi.getPair().getKey().equals(newMapPair.getKey())) {
//                                    knownItem = true;
//                                }
//
//                            }
//                        }
//                        if (!knownItem) {
//                            pair.getMap().addMapItem(mapItem);
//                        }
                    }
                }
            }
        }
    }

    private boolean makePairFromValueItems(RawModlObject rawModlObject, String currentClass, RawModlObject.ValueItem valueItem) {
        boolean madePair;
        RawModlObject.Pair newPair = rawModlObject.new Pair();
        newPair.setKey(currentClass);
        for (RawModlObject.ValueItem vit : valueItem.getValueItems()) {
            newPair.addValueItem(vit);
        }
        RawModlObject.Value value = rawModlObject.new Value();
        value.setPair(newPair);
        valueItem.resetValueItems();
        valueItem.setValue(value);
        madePair = true;
        return madePair;
    }

    private String fixUpNewPair(RawModlObject.Pair pair, int paramNum, List<RawModlObject.Value> params, String currentClass) {
        if (params.get(paramNum).getString() != null) {
            currentClass = params.get(paramNum).getString().string;
        }
        for (RawModlObject.MapItem mapItem : pair.getMap().getMapItems()) {
            if (mapItem.getPair() != null) {
                if (mapItem.getPair().getKey().equals(currentClass)) {
                    RawModlObject.Pair p = mapItem.getPair().getValueItems().get(0).getValueItems().get(0).getValue().getPair();
                    if (p.getKey().equals(currentClass)) {
                        mapItem.setPair(p);
                    }
                }
            }
        }
        return currentClass;
    }

    private void loadConfigFromParam(RawModlObject.ValueItem valueItem, RawModlObject.Value value) {
        List<RawModlObject.Value> values = new LinkedList<>();
        values.add(value);
        List<RawModlObject.ValueItem> newvis = new LinkedList<>();
        newvis.add(valueItem);
        modlClassLoader.loadConfigNumberedVariables(newvis);
    }

    private RawModlObject.Value makeValueString(RawModlObject rawModlObject, RawModlObject.Value value) {
        int deleteMe = 0;
        if (value == null) {
            return null;
        }
        if (value.getString() != null) {
            return value;
        }
        String newString = null;
        if (value.getQuoted() != null) {
            newString = value.getQuoted().string;
        }
        if (value.getNumber() != null) {
            newString = value.getNumber().string;
        }
        if (value.getTrueVal() != null) {
            newString = "true";
        }
        if (value.getFalseVal() != null) {
            newString = "false";
        }
        if (value.getNullVal() != null) {
            newString = "null";
        }


        value = rawModlObject.new Value();
        value.setString(rawModlObject.new String(newString));
        // TODO Turn the value into a Value with a String!!!
        return value;
    }

//    private List<RawModlObject.Value> getValuesFromValueItem(RawModlObject rawModlObject, RawModlObject.ValueItem interpretedValueItem, Object parentPair) {
//        List<RawModlObject.Value> values = new LinkedList<>();
//        if (interpretedValueItem.getValueItems() != null) {
//            for (RawModlObject.ValueItem vi : interpretedValueItem.getValueItems()) {
//                values.addAll(getValuesFromValueItem(rawModlObject, vi, parentPair));
//            }
//        }
//        if (interpretedValueItem.getValue() != null) {
//            values.add(interpretedValueItem.getValue());
//        }
//        if (interpretedValueItem.getValueConditional() != null) {
//            values.addAll(getValuesFromValueItem(rawModlObject, interpret(rawModlObject, interpretedValueItem.getValueConditional(), parentPair), parentPair));
//        }
//        return values;
//    }
//
    private boolean isDefinedClassName(String key) {
        for (Map.Entry<String, Map<String, Object>> klass : modlClassLoader.klasses.entrySet()) {
            if (((RawModlObject.Value)(klass.getValue().get("*name"))) != null && ((RawModlObject.Value)(klass.getValue().get("*name"))).getString() != null) {
                if (key.equals(((RawModlObject.Value)(klass.getValue().get("*name"))).getString().string)) {
                    return true;
                }
            }
            if (((RawModlObject.Value)(klass.getValue().get("*n"))) != null && ((RawModlObject.Value)(klass.getValue().get("*n"))).getString() != null) {
                if (key.equals(((RawModlObject.Value)(klass.getValue().get("*n"))).getString().string)) {
                    return true;
                }
            }
        }
        return false;
    }

//    private List<RawModlObject.ValueItem> getValueItemsFromValueItem(RawModlObject rawModlObject,
//                                                                     RawModlObject.ValueItem interpretedValueItem,
//                                                                     Object parentPair) {
//        List<RawModlObject.ValueItem> valueItems = new LinkedList<>();
//        if (interpretedValueItem.getValueItems() != null) {
//            for (RawModlObject.ValueItem vi : interpretedValueItem.getValueItems()) {
//                valueItems.addAll(getValueItemsFromValueItem(rawModlObject, vi, parentPair));
//            }
//        }
//        if (interpretedValueItem.getValue() != null) {
//            RawModlObject.ValueItem vi = rawModlObject.new ValueItem();
//            vi.setValue(interpret(rawModlObject, interpretedValueItem.getValue(), parentPair));
//            valueItems.add(vi);
//        }
//        if (interpretedValueItem.getValueConditional() != null) {
//            valueItems.addAll(getValueItemsFromValueItem(rawModlObject,
//                    interpret(rawModlObject, interpretedValueItem.getValueConditional(), parentPair), parentPair));
//        }
//        return valueItems;
//    }

    private void addMapItemsToPair(ModlObject modlObject, List<RawModlObject.MapItem> mapItems, List<ModlObject.Pair> pairs, Object parentPair) {
        if (mapItems == null) {
            return;
        }
        for (RawModlObject.MapItem mapItem : mapItems) {
            if (mapItem.getPair() != null) {
                pairs.add(interpret(modlObject, mapItem.getPair(), parentPair));
            } else {
                // handle conditionals
                List<ModlObject.Pair> newPairs = interpret(modlObject, mapItem.getMapConditional(), parentPair);
                for (ModlObject.Pair p : newPairs) {
                    pairs.add(p);
                }
//                addMapItemsToPair(modlObject, newMapItems, pairs, parentPair);
            }
        }
    }

    private ModlObject.Value interpret(ModlObject modlObject, RawModlObject.ValueItem rawValueItem, Object parentPair) {
        if (rawValueItem == null) {
            return null;
        }
        if (rawValueItem.getValue() != null) {
            return interpret(modlObject, rawValueItem.getValue(), parentPair);
        }
        if (rawValueItem.getValueConditional() != null) {
            return interpret(modlObject, rawValueItem.getValueConditional(), parentPair);
        }
        if (rawValueItem.getValueItems() != null) {
            if (rawValueItem.getValueItems().size() == 1) {
                return interpret(modlObject, rawValueItem.getValueItems().get(0), parentPair);
            } else {
                ModlObject.Array array = modlObject.new Array();
            for (RawModlObject.ValueItem vi : rawValueItem.getValueItems()) {
                array.addValue(interpret(modlObject, vi, parentPair));
            }
                return array;
            }
////            for (RawModlObject.ValueItem vi : rawValueItem.getValueItems()) {
////                newValueItem.addValueItem(interpret(modlObject, vi, parentPair));
////            }
//            return interpret(modlObject, rawValueItem.getValueItems(), parentPair);
        }
        return null;
    }

    private Map<String, Object> getModlClass(String key) {
        Map<String, Object> ret = modlClassLoader.klasses.get(key);
        if (ret != null) {
            return ret;
        }
        for (Map.Entry<String, Map<String, Object>> entry : modlClassLoader.klasses.entrySet()) {
            for (Map.Entry<String, Object> valueEntry : entry.getValue().entrySet()) {
                if (valueEntry.getKey().equals("*name") || valueEntry.getKey().equals("*n")) {
                    if (((RawModlObject.Value)(valueEntry.getValue())).getString().string.equals(key)) {
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
        return getPairsFromArray(modlObject, array.getArrayItems(), parentPair);
    }

    private List<ModlObject.Pair> getPairsFromArray(ModlObject modlObject, List<RawModlObject.ArrayItem> arrayItems, Object parentPair) {

        List<ModlObject.Pair> pairs = new LinkedList<>();
        if (arrayItems != null) {
            for (RawModlObject.ArrayItem arrayItem : arrayItems) {
                //  NEED TO EVALUATE THE CONDITIONAL HERE!!!
                if (arrayItem.getValue() != null &&
                        arrayItem.getValue().getPair() != null) {
                    pairs.add(interpret(modlObject, arrayItem.getValue().getPair(), parentPair));
                } else {
                    // handle conditionals
                    List<ModlObject.Value> newArrayItems = interpret(modlObject, arrayItem.getArrayConditional(), parentPair);
                    for (ModlObject.Value v : newArrayItems) {
                        pairs.add((ModlObject.Pair)v); // TODO !!!
                    }
//                    List<RawModlObject.Pair> newPairs = getPairsFromArray(modlObject, newArrayItems, parentPair);
//                    for (RawModlObject.Pair p : newPairs) {
//                        pairs.add(p);
//                    }
                }
           }
        }
        return pairs;
    }

    private void addAllParentPairs(ModlObject modlObject, ModlObject.Pair pair, String originalKey) {
        Map<String, Object> klass = getModlClass(originalKey);
        for (Map.Entry<String, Object> entry : klass.entrySet()) {
            if (!entry.getKey().startsWith("_") && !(entry.getKey().startsWith("*") && !(entry.getKey().equals("?")))) {
//            if (!entry.getKey().startsWith("_") ) {
//                if (pairHasKey(pair, entry.getKey())) {
                if (((ModlObject.Map)pair.getValue()).get(modlObject.new String(entry.getKey())) != null) {
                    // Only add the new key if it does not already exist in the pair!
                    continue;
                }
                ModlObject.Pair newPair = modlObject.new Pair();
                newPair.setKey(modlObject.new String(entry.getKey()));
                newPair.setValue((ModlObject.Value)entry.getValue());
                ((ModlObject.Map)pair.getValue()).addPair(newPair);

//                // Fix this up for new grammar structures
//                RawModlObject.ValueItem valueItem = modlObject.new ValueItem();
//                valueItem.setValue((RawModlObject.Value)entry.getValue());
//                newPair.addValueItem(valueItem);
//                RawModlObject.MapItem mapItem = modlObject.new MapItem();
//                mapItem.setPair(newPair);
//                pair.getMap().addMapItem(mapItem);
            }
        }

    }

//    private boolean pairHasKey(RawModlObject.Pair pair, String key) {
//        // Does this pair have a map which has a pair which has this key?
//        RawModlObject.Map map = pair.getMap();
//        if (map == null || map.getMapItems() == null) {
//            return false;
//        }
//        for (RawModlObject.MapItem mapItem : map.getMapItems()) {
//            if (mapItem.getPair() != null && mapItem.getPair().getKey().equals(key)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
    private boolean mapPairAlready(RawModlObject.Pair originalPair) {
        return (originalPair.getMap() != null);
    }

    private boolean anyClassContainsPairs(String originalKey) {
        // If this class, or any of its parent classes, define any pairs, then return true
        // A pair is defined in a class if it has a pair whose key does not start in "_"
        Map<String, Object> klass = getModlClass(originalKey);
        for (String key : klass.keySet()) {
            if (!key.startsWith("_") && !key.startsWith("*") && !key.equals("?")) {
//            if (!key.startsWith("_") ) {
                return true;
            }
        }
        return false;
    }

    private String transformKey(String originalKey) {
        if (getModlClass(originalKey) != null) {
            if (((RawModlObject.Value)getModlClass(originalKey).get("*name")) != null &&
                    ((RawModlObject.Value)getModlClass(originalKey).get("*name")).getString() != null){
                return ((RawModlObject.Value) (getModlClass(originalKey).get("*name"))).getString().string;
            }
            if (((RawModlObject.Value)getModlClass(originalKey).get("*n")) != null &&
                    ((RawModlObject.Value)getModlClass(originalKey).get("*n")).getString() != null){
                return ((RawModlObject.Value) (getModlClass(originalKey).get("*n"))).getString().string;
            }
        }
        return originalKey;
    }

    private RawModlObject.Pair transformValue(ModlObject modlObject, RawModlObject.Pair originalPair) {
        RawModlObject rawModlObject = new RawModlObject();
        if (getModlClass(originalPair.getKey()) != null) {
            if ((getModlClass(originalPair.getKey()).get("*name") != null && ((getModlClass(originalPair.getKey()).get("*name").equals("_v"))  ||
               (getModlClass(originalPair.getKey()).get("*name").equals("var")))) ||
                    (getModlClass(originalPair.getKey()).get("*n") != null &&
                            ((getModlClass(originalPair.getKey()).get("*n").equals("_v"))  ||
                    (getModlClass(originalPair.getKey()).get("*n").equals("var"))))) {
                modlClassLoader.loadConfigNumberedVariables(originalPair.getValueItems());
            } else {
                if (getModlClass(originalPair.getKey()).get("*superclass") != null &&
                        getModlClass(originalPair.getKey()).get("*superclass").equals("str")) {
                    RawModlObject.Pair pair = rawModlObject.new Pair();
                    pair.setKey(originalPair.getKey());
                    if (originalPair.getValueItems().get(0).getValue() == null) {
                        return originalPair;
                    }
                    if (originalPair.getValueItems().get(0).getValue().getString() != null) {
                        return originalPair;
                    }
                    RawModlObject.Value value = makeValueString(rawModlObject, originalPair.getValueItems().get(0).getValue());
                    RawModlObject.ValueItem valueItem = rawModlObject.new ValueItem();
                    valueItem.setValue(value);
                    pair.addValueItem(valueItem);
                    return pair;
                }
            }
        }
        return originalPair;
    }

    private ModlObject.Value interpret(ModlObject modlObject, RawModlObject.Value rawValue, Object parentPair) {
        if (rawValue == null) {
            return null;
        }

        ModlObject.Value value = interpret(modlObject, rawValue.getPair(), parentPair);
        if (value != null) {
            return value;
        }
        value = interpret(modlObject, rawValue.getMap(), parentPair);
        if (value != null) {
            return value;
        }
        value = interpret(modlObject, rawValue.getArray(), parentPair);
        if (value != null) {
            return value;
        }
        value = interpret(modlObject, rawValue.getQuoted());
        if (value != null) {
            return value;
        }
        value = interpret(modlObject, rawValue.getNumber());
        if (value != null) {
            return value;
        }
        value = interpret(modlObject, rawValue.getTrueVal());
        if (value != null) {
            return value;
        }
        value = interpret(modlObject, rawValue.getFalseVal());
        if (value != null) {
            return value;
        }
        value = interpret(modlObject, rawValue.getNullVal());
        if (value != null) {
            return value;
        }
        value = interpret(modlObject, rawValue.getString());
        if (value != null) {
            return value;
        }
        return null;
    }

    private ModlObject.Map interpret(ModlObject modlObject, RawModlObject.Map originalMap, Object parentPair) {
        if (originalMap == null) {
            return null;
        }

        ModlObject.Map map = modlObject.new Map();

        if (originalMap.getMapItems() != null) {
            for (RawModlObject.MapItem originalMapItem : originalMap.getMapItems()) {
                List<ModlObject.Pair> pairs = interpret(modlObject, originalMapItem, parentPair);
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
        return map;
    }


    private List<ModlObject.Pair> interpret(ModlObject modlObject, RawModlObject.MapItem originalMapItem, Object parentPair) {
        if (originalMapItem == null) {
            return null;
        }

        List<ModlObject.Pair> pairs = new LinkedList<>();

        if (originalMapItem.getPair() != null) {
                ModlObject.Pair pair = interpret(modlObject, originalMapItem.getPair(), parentPair);
                if (pair != null) {
                    if (!pair.getKey().string.startsWith("_")) {
                        pairs.add(pair);
                    }
                }
        }
        if (originalMapItem.getMapConditional() != null) {
            // evaluate the conditional
            pairs = interpret(modlObject, originalMapItem.getMapConditional(), parentPair);
        }
        return pairs;
    }


    private ModlObject.Array interpret(ModlObject modlObject, RawModlObject.Array rawArray, Object parentPair) {
        if (rawArray == null) {
            return null;
        }
        ModlObject.Array array = modlObject.new Array();

        if (rawArray.getArrayItems() != null) {
            for (RawModlObject.ArrayItem originalArrayItem : rawArray.getArrayItems()) {
                List<ModlObject.Value> values = interpret(modlObject, originalArrayItem, parentPair);
                if (values != null) {
                    for (ModlObject.Value value : values) {
                        array.addValue(value);
                        if (parentPair != null) {
                            ((List)parentPair).add(value);
                        }
                    }
                }
            }
        }

        return array;
    }

    private List<ModlObject.Value> interpret(ModlObject modlObject, RawModlObject.ArrayItem rawArrayItem, Object parentPair) {
        if (rawArrayItem == null) {
            return null;
        }
        List<ModlObject.Value> values = new LinkedList<>();


        if (rawArrayItem.getValue() != null) {
            ModlObject.Value value = interpret(modlObject, rawArrayItem.getValue(), parentPair);
            values.add(value);
        }
        if (rawArrayItem.getArrayConditional() != null) {
            values = interpret(modlObject, rawArrayItem.getArrayConditional(), parentPair);
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
            if (evaluates(conditionalTest))  {
                // NEED TO INTERPRET THE VALUES!!!
//                RawModlObject.ValueItem returnValueItem = rawModlObject.new ValueItem();
                if (originalConditionalEntry.getValue().getValueItems().size() == 1) {
                    return interpret(modlObject, originalConditionalEntry.getValue().getValueItems().get(0), parentPair);
                }
                ModlObject.Value returnValue = modlObject.new Array();
                for (RawModlObject.ValueItem valueItem : originalConditionalEntry.getValue().getValueItems()) {
//                    RawModlObject.ValueItem vi = interpret(rawModlObject, valueItem, parentPair);
                    ModlObject.Value v = interpret(modlObject, valueItem, parentPair);
//                        returnValueItem.addValueItem(vi);
                    ((ModlObject.Array)returnValue).addValue(v);
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
                    for (RawModlObject.ArrayItem arrayItem : originalConditionalEntry.getValue().getArrayItems()) {
                        List<ModlObject.Value> values = interpret(modlObject, arrayItem, parentPair);
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
        for (Map.Entry<RawModlObject.ConditionTest, RawModlObject.MapConditionalReturn> originalConditionalEntry : originalConditional.getConditionals().entrySet()) {
            // Does this conditional evaluate?
            RawModlObject.ConditionTest conditionalTest = originalConditionalEntry.getKey();
            if (evaluates(conditionalTest))  {
                // NEED TO INTERPRET THE VALUES!!!
                List<ModlObject.Pair> returnPairs = new LinkedList<>();
                for (RawModlObject.MapItem mapItem : originalConditionalEntry.getValue().getMapItems()) {
                    List<ModlObject.Pair> mapItems = interpret(modlObject, mapItem, parentPair);
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
            if (evaluates(conditionalTest))  {
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
                subConditionReturn = evaluates((RawModlObject.ConditionGroup)subCondition);
            } else if (subCondition instanceof RawModlObject.Condition) {
                subConditionReturn = evaluates((RawModlObject.Condition)subCondition);
            }

            // HANDLE NOT OPERATOR!
            if (shouldNegate) {
                subConditionReturn = !subConditionReturn;
            }

            // Do something with it!!!
            if (conditionTestOperator == null) {
                result = subConditionReturn;
            }
            else {
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
            if (values.get(0).getTrueVal() != null) {
                return true;
            }
            if (values.get(0).getFalseVal() != null) {
                return false;
            }
            if (values.get(0).getString() != null && boolPairs.containsKey(values.get(0).getString().string)) {
                return boolPairs.get(values.get(0).getString().string);
            }
        }
        String key = transformConditionalArgument(keyString);

        String conditionOperator = condition.getOperator();

        // Now evaluate it!!
        // Should either be number, string or quoted

        if (values.size() > 1) {
            // This MUST be a "variable assumption"
            // Check that the operator is "="
            // We need to check if the key is equal to ANY of the values
            for (RawModlObject.Value value: values) {
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
            Object valObj = getObjectFromValueForCondition(values.get(0));
            String val = transformConditionalArgument(valObj.toString());
            if (val.startsWith("%")) {
                val = val.substring(1, val.length());
            }
            if (conditionOperator.equals("=")) {
                if (conditionalEquals(key.toString(), val)) {
                    return true;
                }
                if (conditionalEquals(key.toString(), transformString(val))) {
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
            return conditionalWildcardEquals(key, val);
        }
        return key.equals(val);
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
                regex+=".*";
            }
            regex+=split;
        }
        if (!(val.toString().endsWith("*"))) {
            regex += "$";
        } else {
            regex += ".*";
        }

        return key.matches(regex);
    }

    private Object getObjectFromValueForCondition(RawModlObject.Value value) {
        if (value.getQuoted() != null) {
            return value.getQuoted().string;
        }
        if (value.getString() != null) {
            return value.getString().string;
        }
        if (value.getNumber() != null) {
            return value.getNumber().string;
        }
        return null;
    }

    private String transformConditionalArgument(String origKeyString) {
        StringTransformer stringTransformer = new StringTransformer(modlClassLoader, stringPairs, mapPairs, arrayPairs, variableMethods);
        String keyString = stringTransformer.runObjectReferencing("%" + origKeyString, "%" + origKeyString, false);
        if (keyString.equals("%" + origKeyString) && origKeyString.startsWith("%")) {
            keyString = stringTransformer.runObjectReferencing(origKeyString, origKeyString, false);
        }
//        if (keyString.equals("%"+keyString)) {
        if (keyString.equals("%"+origKeyString)) {
//            throw new RuntimeException("Couldn't find conditional key");
            return origKeyString;
        }
        return keyString;
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

    private ModlObject.String interpretAsString(ModlObject modlObject, RawModlObject.Number originalNumber) {
        if (originalNumber != null) {
            ModlObject.String number = modlObject.new String(originalNumber.string);
            return number;
        }
        return null;
    }

    private ModlObject.Number interpret(ModlObject modlObject, RawModlObject.Number originalNumber) {
        if (originalNumber != null) {
            ModlObject.Number number = modlObject.new Number(originalNumber.string);
            return number;
        }
        return null;
    }

    private ModlObject.String interpret(ModlObject modlObject, RawModlObject.String string) {
        if (string != null) {
            ModlObject.String str = modlObject.new String(transformString(string.string));
            return str;
        }
        return null;
    }

    private ModlObject.String interpret(ModlObject modlObject, RawModlObject.Quoted quoted) {
        if (quoted != null) {
            String s = quoted.string;
            ModlObject.String string = modlObject.new String(transformString(s));
            return string;
        }
        return null;
    }

    private String transformString(String s) {
        StringTransformer stringTransformer = new StringTransformer(modlClassLoader, stringPairs, mapPairs, arrayPairs, variableMethods);
        return stringTransformer.transformString(s);
    }

    private void checkVersion(String version) {
        if (!(version.equals(String.valueOf(CONFIG_VERSION)))) {
            throw new UnsupportedOperationException("Can't handle version " + version +
                " config version - only support version " + CONFIG_VERSION);
        }
    }


}
