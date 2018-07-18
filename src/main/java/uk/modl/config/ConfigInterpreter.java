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

import org.apache.commons.lang3.tuple.ImmutablePair;
import uk.modl.parser.Interpreter;
import uk.modl.parser.ModlObject;
import uk.modl.parser.ModlParsed;
import uk.modl.parser.printers.JsonPrinter;

import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;

public class ConfigInterpreter {

    public static Integer CONFIG_VERSION = 0;

    private ModlConfig modlConfig = null;

    Map<String, Function<String, String>> variableMethods = new HashMap<>();

    Set<String> pairNames;
    Map<String, String> stringPairs;

    public static String parseToJson(String input) throws IOException {
        ModlObject modlObject = interpret(input);

        JsonPrinter jsonPrinter = new JsonPrinter();
        String json = jsonPrinter.printModl(modlObject);
        return json;

    }

    public static ModlObject interpret(String input) throws IOException {
        ModlObject modlObject = Interpreter.interpret(input);

        return interpret(modlObject);
    }

    public static ModlObject interpret(ModlObject modlObject) {
        ConfigInterpreter configInterpreter = new ConfigInterpreter();
        return configInterpreter.interpretPrivate(modlObject);
    }

    public ConfigInterpreter() {
        variableMethods = VariableMethods.getConstantVariableMethods();
    }

    private ModlObject interpretPrivate(ModlObject modlObject) {
        ModlObject configuredModlObject = new ModlObject();
        modlConfig = new ModlConfig();
        pairNames = new HashSet<>();
        stringPairs = new HashMap<>();

        // Interpret modlObject based on specified config files

        // The R object will no longer be in the modlObject - the MODL Interpreter will have stripped it out
        // If there are any S or I objects, they will be at the front of the modlObject.
        boolean startedInterpreting = false;
        for (ModlObject.Structure structure : modlObject.getStructures()) {

            if (structure.getPair() != null && (structure.getPair().getKey().equals("*I") || (structure.getPair().getKey().equals("*IMPORT")))) {
                if (startedInterpreting) {
//                    throw new UnsupportedOperationException("Cannot have *I config file after other objects");
                }
                // Load in the config file specified by the "I" object
                if (structure.getPair().getValueItems().get(0).getValue().getString() != null) {
                    loadConfigFile(modlObject, structure.getPair().getValueItems().get(0).getValue().getString().string);
                }
                if (structure.getPair().getValueItems().get(0).getValue().getQuoted() != null) {
                    loadConfigFile(modlObject, structure.getPair().getValueItems().get(0).getValue().getQuoted().string);
                }
                continue;
            }
            // TODO We should be able to spot this every time we load a pair, and add it to a local config at that point
            if (structure.getPair() != null && ((((structure.getPair().getKey().equals("*class")) ||
                    (structure.getPair().getKey().equals("*c"))) || ((structure.getPair().getKey().equals("*vm")) ||
                    (structure.getPair().getKey().equals("*m"))) || ((structure.getPair().getKey().equals("*method")) ||
                    (structure.getPair().getKey().equals("*variable_map"))) ||
                    (structure.getPair().getKey().equals("?"))
//                    (structure.getPair().getKey().equals("*")) ||
//                    ((structure.getPair().getKey().equals("*v")) || (structure.getPair().getKey().equals("*var")))
            ))) {
                modlConfig.loadconfig(structure, variableMethods);
                continue;
            } else if (structure.getPair() != null && (structure.getPair().getKey().startsWith("*"))) {
                throw new RuntimeException("Unrecognised configuration instruction : " + structure.getPair().getKey());
            }
            if (!(structure.getPair() != null && (structure.getPair().getKey().equals("V_") ||
                    (structure.getPair().getKey().equals("version_"))))) {
                startedInterpreting = true;
            }
            List<ModlObject.Structure> interpretedStructs = interpret(configuredModlObject, structure);
            if (interpretedStructs != null) {
                for (ModlObject.Structure interpretedStruct : interpretedStructs) {
                    if (interpretedStruct.getPair() == null && interpretedStruct.getMap() == null &&
                            interpretedStruct.getArray() == null) {
                        // Nothing to add!
                    } else {
                        configuredModlObject.addStructure(interpretedStruct);
                    }
                }
            }
        }

        return configuredModlObject;
    }

    private ModlConfig loadConfigFile(ModlObject modlObject, String location) {
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
        ModlObject configModlObject = null;
        try {
            configModlObject = Interpreter.interpret(contents);
        } catch (IOException e) {
            throw new RuntimeException("Could not interpret " + location + ", error : " + e);
        }
        modlConfig.loadConfig(configModlObject, variableMethods);
        // Now we've loaded it, stick it in the map so we can use it next time
        return modlConfig;
    }

    private List<ModlObject.Structure> interpret(ModlObject configuredModlObject, ModlObject.Structure structure) {

        if (structure == null) {
            return null;
        }
        if (structure.getTopLevelConditional() == null) {
            ModlObject.Structure newStructure = configuredModlObject.new Structure();

            newStructure.setMap(interpret(configuredModlObject, structure.getMap()));
            newStructure.setArray(interpret(configuredModlObject, structure.getArray()));

            ModlObject.Pair pair = interpret(configuredModlObject, structure.getPair());
            if (pair != null) {
                if (!pair.getKey().startsWith("_") &&
                        !pair.getKey().startsWith("*") &&
                        !pair.getKey().equals("?")) {
//                if (!pair.getKey().startsWith("_")) {
                    newStructure.setPair(pair);
                }
            }
            List<ModlObject.Structure> structures = new LinkedList<>();
            structures.add(newStructure);
            return structures;
        } else {
            List<ModlObject.Structure> structures = (interpret(configuredModlObject, structure.getTopLevelConditional()));
            // Return the new structures
            return structures;
        }

    }

    private ModlObject.Pair interpret(ModlObject modlObject, ModlObject.Pair originalPair) {
        if (originalPair == null) {
            return null;
        }
        if (originalPair.getKey() != null) {
//            if (originalPair.getKey().startsWith("_")) {
//                if (originalPair.getKey().equals("_v") || originalPair.getKey().equals("_var")) {
                if (originalPair.getKey().equals("?")) {
                    modlConfig.loadConfigNumberedVariables(originalPair.getValueItems());
                    return null;
                }
//            }
        }

        ModlObject.Pair pair = modlObject.new Pair();

        String originalKey = originalPair.getKey();
        String newKey = originalKey;
        if (haveModlClass(originalKey)) {
            newKey = transformKey(originalKey);
            originalPair = transformValue(modlObject, originalPair);
        }

        // IF WE ALREADY HAVE A PAIR WITH THIS NAME, AND THE NAME IS UPPER-CASE, THEN RAISE AN ERROR
        if (newKey != null) {
            if (newKey.toUpperCase().equals(newKey)) {
                if (pairNames.contains(newKey)) {
                    throw new RuntimeException(newKey + " can't be defined again as upper-case keys are immutable");
                }
            }
        }
        pairNames.add(newKey);
        if (originalPair.getValueItems() != null) {
            transformPairKey(originalPair, newKey);
        }
        if (newKey != null && (newKey.startsWith("_") || (newKey.startsWith("*")) || newKey.equals("?"))) {
//        if (newKey != null && (newKey.startsWith("_"))) {
            return null;
        }

        //  A pair with a key that matches a class ID or class name is transformed according to the class definition:
        // TODO Should be able to look up by transformed name too?
        if (haveModlClass(originalKey)) { // modlConfig.klasses.get(originalKey) != null) {
            // The key of the pair is set to the class name.
            // The value of the original standard pair is given the key value in the new map pair
            if (generateModlClassObject(modlObject, originalPair, pair, originalKey, newKey))
                return pair; // TODO In all cases?


        }

        pair.setKey(newKey);

        pair.setMap(interpret(modlObject, originalPair.getMap()));
        pair.setArray(interpret(modlObject, originalPair.getArray()));
        if (originalPair.getValueItems() != null) {
            for (ModlObject.ValueItem originalValueItem : originalPair.getValueItems()) {
                pair.addValueItem(interpret(modlObject, originalValueItem));

            }
        }

        return pair;
    }

    private boolean generateModlClassObject(ModlObject modlObject, ModlObject.Pair originalPair, ModlObject.Pair pair, String originalKey, String newKey) {
        pair.setKey(newKey);

        // TODO WHEN WE EVALUATE NUMPARAMS, WE NEED TO COUNT THE EVALUATED CONDITIONALS RATHER THAN THE INPUT SIZE!!!
        int numParams = 0;
        if (originalPair.getValueItems() != null) {
            // TODO What if the single value is a map?
            // TODO We'd then want to process it as if it were a map...
            if (originalPair.getValueItems().size() == 1 &&
                    // TODO Remember to handle valueItems and their conditionals here!
                    // is getValue() null?!
                    originalPair.getValueItems().get(0).getValue() != null &&
                    originalPair.getValueItems().get(0).getValue().getMap() != null) {
               originalPair.setMap(originalPair.getValueItems().get(0).getValue().getMap());
               originalPair.resetValueItems();
            } else {
                // TODO expand the conditionals before counting
                numParams = originalPair.getValueItems().size();
            }
        }
        numParams = getNumParams(originalPair, numParams);
        String paramsKeyString = "*params" + numParams;
//            Object obj = modlConfig.klasses.get(originalPair.getKey()).get(paramsKeyString);
        Object obj = getModlClass(originalPair.getKey()).get(paramsKeyString);
        boolean hasParams = (obj != null);

        // If it's not already a map pair, and one of the parent classes in the class hierarchy includes pairs, then it is transformed to a map pair.
        if (anyClassContainsPairs(originalKey) || mapPairAlready(originalPair) || (hasParams)) {
//                if (!mapPairAlready(originalPair)) {
                pair.setMap(modlObject.new Map());
                pair.setKey(newKey);
//                }
            //  and the pairs from all parent classes in the class hierarchy are added to the new map pair.
            // Do this recursively up from the child class, only adding pairs if they are not there already
//            addAllParentPairs(modlObject, pair, originalKey);


            List<ModlObject.Pair> pairs = null;
            boolean wasArray = false;

            if (originalPair.getArray() != null) {
                pairs = getPairsFromArray(modlObject, originalPair.getArray());
                pair.setArray(modlObject.new Array());
                wasArray = true;
            }

            if (mapPairAlready(originalPair)) {
                pairs = new LinkedList<>();
                addMapItemsToPair(modlObject, originalPair.getMap().getMapItems(), pairs);
            }

            if (pairs != null) {
                // Make all the new map values in the new map pair
                makeNewMapPair(modlObject, pair, pairs, wasArray);
            }
            if (originalPair.getValueItems() != null && originalPair.getValueItems().size() > 0) {
                if (!hasParams) {
                    // Don't need a pair here - continue
                    for (ModlObject.ValueItem originalValueItem : originalPair.getValueItems()) {
                        ModlObject.Value value = interpret(modlObject, originalValueItem.getValue());
                        ModlObject.ValueItem valueItem = modlObject.new ValueItem();
                        valueItem.setValue(value);
                        pair.addValueItem(valueItem);
                    }
                } else {
                    int paramNum = 0;
                    List<ModlObject.Value> params = (List<ModlObject.Value>)obj;
                    String currentClass = null;


                    for (ModlObject.ValueItem valueItem : originalPair.getValueItems()) {

                        // How about checking if the valueItem has more than one valuitem?
                        // If it does, then make it a pair, and set the key to the currentClass
                        if (params.get(paramNum).getString() != null) {
                            currentClass = params.get(paramNum).getString().string;
                        }
                        boolean madePair = false;
                        if (valueItem.getValueItems() != null && (valueItem.getValueItems().size() > 1)) {
                            // If it does, then make it a pair, and set the key to the currentClass
                            madePair = makePairFromValueItems(modlObject, currentClass, valueItem);
                        }

                        List<ModlObject.ValueItem> vis = getValueItemsFromValueItem(modlObject, valueItem);
                        for (ModlObject.ValueItem vi : vis) {

                            List<ModlObject.Value> originalValues = getValuesFromValueItem(modlObject, vi);

                            for (ModlObject.Value value : originalValues) {
                                // We now need to deal with the assigned params - we only need to do this if it is a multi-value pair as they will be named otherwise
                                // Of course, each value could also be named - if it is, then we need to make sure it is the type of object we were expecting
                                // This includes a single Value!
                                // Remember that each value can be interpreted as any _class -
                                // - "pn" might be phone number, so "441270987654" is made to become a phone number map pair

                                if (params.get(paramNum).getString().string.equals("?")) {
                                    loadConfigFromParam(valueItem, value);
                                } else {
                                    // We will need to create a new pair of the appropriate type and interpret it
                                    if (params.get(paramNum).getString() != null) {
                                        currentClass = params.get(paramNum).getString().string;
                                    }
                                    ModlObject.Pair newPair = modlObject.new Pair();
                                    newPair.setKey(params.get(paramNum).getString().string);
                                    ModlObject.ValueItem v = modlObject.new ValueItem();
                                    if (modlConfig.klasses.get(params.get(paramNum).getString()) != null) {
                                        if (modlConfig.klasses.get(params.get(paramNum).getString().string).get("*superclass").equals("str")) {
                                            value = makeValueString(modlObject, value);
                                        }
                                    }
                                    v.setValue(value);
                                    newPair.addValueItem(v);
                                    if (value.getPair() != null && isDefinedClassName(value.getPair().getKey())) {
                                            newPair = value.getPair();
                                            madePair = false;
                                    } else {
                                        // Need to interpret as the right type! e.g. if number, and string required, then interpret as string!
                                        newPair = interpret(modlObject, newPair);
                                    }
                                    if (!newPair.getKey().startsWith("_") && !newPair.getKey().startsWith("*") &&
                                            !(newPair.getKey().equals("?"))) {
//                                    if (!newPair.getKey().startsWith("_") ) {
                                        ModlObject.MapItem mapItem = modlObject.new MapItem();
                                        mapItem.setPair(newPair);
                                        pair.getMap().addMapItem(mapItem);
                                    }
                                }

                            }
                        }
                        // If we had to make up a new pair with the a key, then we will have a pair within a pair with that key
                        // So we need to find the bottom-most pair and bring it up a level
                        if (madePair) {
                            currentClass = fixUpNewPair(pair, paramNum, params, currentClass);
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

    private int getNumParams(ModlObject.Pair originalPair, int numParams) {
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

    private void transformPairKey(ModlObject.Pair originalPair, String newKey) {
        if (originalPair.getValueItems().size() == 1) {
            String transformedKey = newKey;
            if (transformedKey.startsWith("_")) {
                transformedKey = transformedKey.replaceFirst("_", "");
            }
            if (originalPair.getValueItems().get(0).getValue() != null &&
                    originalPair.getValueItems().get(0).getValue().getString() != null) {
                String str = originalPair.getValueItems().get(0).getValue().getString().string;
                stringPairs.put(transformedKey, transformString(str));
            }
            if (originalPair.getValueItems().get(0).getValue() != null &&
                    originalPair.getValueItems().get(0).getValue().getQuoted() != null) {
                stringPairs.put(transformedKey, transformString(originalPair.getValueItems().get(0).getValue().getQuoted().string));
            }
            if (originalPair.getValueItems().get(0).getValue() != null &&
                    originalPair.getValueItems().get(0).getValue().getNumber() != null) {
                stringPairs.put(transformedKey, transformString(originalPair.getValueItems().get(0).getValue().getNumber().string));
            }
        }
    }

    public void makeNewMapPair(ModlObject modlObject, ModlObject.Pair pair, List<ModlObject.Pair> pairs, boolean wasArray) {
        for (ModlObject.Pair originalMapPair : pairs) {
            // TODO Do we need to check the order and types here?
            // TODO Need to look up pair name (e.g. "n") in classes and load the appropriate type of thing here

            // TODO Watch out for ValueItems here! They can be interpreted into list of ValueItems which each need dealt with recursively
            ModlObject.Pair newMapPair = interpret(modlObject, originalMapPair);
            if (newMapPair != null) {
                if (wasArray) {
                    ModlObject.Value value = modlObject.new Value();
//                                ModlObject.Structure structure = modlObject.new Structure();
                    if (!newMapPair.getKey().startsWith("_")) {
                        value.setPair(newMapPair);
                    }
                    // TODO Make sure we're using the right type of variable here!
                    ModlObject.ArrayItem arrayItem = modlObject.new ArrayItem();
                    arrayItem.setValue(value);
                    pair.getArray().addArrayItem(arrayItem);
                } else {
                    if (!newMapPair.getKey().startsWith("_")) {
                        ModlObject.MapItem mapItem = modlObject.new MapItem();
                        mapItem.setPair(newMapPair);
                        // TODO HACK!!!
                        boolean knownItem = false;
                        if (pair.getMap().getMapItems() != null) {
                            for (ModlObject.MapItem mi : pair.getMap().getMapItems()) {
                                if (mi.getPair() != null &&
                                        mi.getPair().getKey().equals(newMapPair.getKey())) {
                                    knownItem = true;
                                }

                            }
                        }
                        if (!knownItem) {
                            pair.getMap().addMapItem(mapItem);
                        }
                    }
                }
            }
        }
    }

    private boolean makePairFromValueItems(ModlObject modlObject, String currentClass, ModlObject.ValueItem valueItem) {
        boolean madePair;
        ModlObject.Pair newPair = modlObject.new Pair();
        newPair.setKey(currentClass);
        for (ModlObject.ValueItem vit : valueItem.getValueItems()) {
            newPair.addValueItem(vit);
        }
        ModlObject.Value value = modlObject.new Value();
        value.setPair(newPair);
        valueItem.resetValueItems();
        valueItem.setValue(value);
        madePair = true;
        return madePair;
    }

    private String fixUpNewPair(ModlObject.Pair pair, int paramNum, List<ModlObject.Value> params, String currentClass) {
        if (params.get(paramNum).getString() != null) {
            currentClass = params.get(paramNum).getString().string;
        }
        for (ModlObject.MapItem mapItem : pair.getMap().getMapItems()) {
            if (mapItem.getPair() != null) {
                if (mapItem.getPair().getKey().equals(currentClass)) {
                    ModlObject.Pair p = mapItem.getPair().getValueItems().get(0).getValueItems().get(0).getValue().getPair();
                    if (p.getKey().equals(currentClass)) {
                        mapItem.setPair(p);
                    }
                }
            }
        }
        return currentClass;
    }

    private void loadConfigFromParam(ModlObject.ValueItem valueItem, ModlObject.Value value) {
        List<ModlObject.Value> values = new LinkedList<>();
        values.add(value);
        List<ModlObject.ValueItem> newvis = new LinkedList<>();
        newvis.add(valueItem);
        modlConfig.loadConfigNumberedVariables(newvis);
    }

    private ModlObject.Value makeValueString(ModlObject modlObject, ModlObject.Value value) {
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


        value = modlObject.new Value();
        value.setString(modlObject.new String(newString));
        // TODO Turn the value into a Value with a String!!!
        return value;
    }

    private List<ModlObject.Value> getValuesFromValueItem(ModlObject modlObject, ModlObject.ValueItem interpretedValueItem) {
        List<ModlObject.Value> values = new LinkedList<>();
        if (interpretedValueItem.getValueItems() != null) {
            for (ModlObject.ValueItem vi : interpretedValueItem.getValueItems()) {
                values.addAll(getValuesFromValueItem(modlObject, vi));
            }
        }
        if (interpretedValueItem.getValue() != null) {
            values.add(interpretedValueItem.getValue());
        }
        if (interpretedValueItem.getValueConditional() != null) {
            values.addAll(getValuesFromValueItem(modlObject, interpret(modlObject, interpretedValueItem.getValueConditional())));
        }
        return values;
    }

    private boolean isDefinedClassName(String key) {
        for (Map.Entry<String, Map<String, Object>> klass : modlConfig.klasses.entrySet()) {
            if (((ModlObject.Value)(klass.getValue().get("*name"))) != null && ((ModlObject.Value)(klass.getValue().get("*name"))).getString() != null) {
                if (key.equals(((ModlObject.Value)(klass.getValue().get("*name"))).getString().string)) {
                    return true;
                }
            }
            if (((ModlObject.Value)(klass.getValue().get("*n"))) != null && ((ModlObject.Value)(klass.getValue().get("*n"))).getString() != null) {
                if (key.equals(((ModlObject.Value)(klass.getValue().get("*n"))).getString().string)) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<ModlObject.ValueItem> getValueItemsFromValueItem(ModlObject modlObject, ModlObject.ValueItem interpretedValueItem) {
        List<ModlObject.ValueItem> valueItems = new LinkedList<>();
        if (interpretedValueItem.getValueItems() != null) {
            for (ModlObject.ValueItem vi : interpretedValueItem.getValueItems()) {
                valueItems.addAll(getValueItemsFromValueItem(modlObject, vi));
            }
        }
        if (interpretedValueItem.getValue() != null) {
            ModlObject.ValueItem vi = modlObject.new ValueItem();
            vi.setValue(interpret(modlObject, interpretedValueItem.getValue()));
            valueItems.add(vi);
        }
        if (interpretedValueItem.getValueConditional() != null) {
            valueItems.addAll(getValueItemsFromValueItem(modlObject, interpret(modlObject, interpretedValueItem.getValueConditional())));
        }
        return valueItems;
    }

    private void addMapItemsToPair(ModlObject modlObject, List<ModlObject.MapItem> mapItems, List<ModlObject.Pair> pairs) {
        if (mapItems == null) {
            return;
        }
        for (ModlObject.MapItem mapItem : mapItems) {
            if (mapItem.getPair() != null) {
                pairs.add(mapItem.getPair());
            } else {
                // handle conditionals
                List<ModlObject.MapItem> newMapItems = interpret(modlObject, mapItem.getMapConditional());
                addMapItemsToPair(modlObject, newMapItems, pairs);
            }
        }
    }

    private ModlObject.ValueItem interpret(ModlObject modlObject, ModlObject.ValueItem originalValueItem) {
        if (originalValueItem == null) {
            return null;
        }
        ModlObject.ValueItem newValueItem = modlObject.new ValueItem();

        if (originalValueItem.getValue() != null) {
            ModlObject.ValueItem valueItem = modlObject.new ValueItem();
            valueItem.setValue(interpret(modlObject, originalValueItem.getValue()));
            newValueItem.addValueItem(valueItem);
        }
        if (originalValueItem.getValueConditional() != null) {
            newValueItem .addValueItem(interpret(modlObject, originalValueItem.getValueConditional()));
        }
        if (originalValueItem.getValueItems() != null) {
            for (ModlObject.ValueItem vi : originalValueItem.getValueItems()) {
                newValueItem.addValueItem(interpret(modlObject, vi));
            }
        }

        return newValueItem;
    }

    private Map<String, Object> getModlClass(String key) {
        Map<String, Object> ret = modlConfig.klasses.get(key);
        if (ret != null) {
            return ret;
        }
        for (Map.Entry<String, Map<String, Object>> entry : modlConfig.klasses.entrySet()) {
            for (Map.Entry<String, Object> valueEntry : entry.getValue().entrySet()) {
                if (valueEntry.getKey().equals("*name") || valueEntry.getKey().equals("*n")) {
                    if (((ModlObject.Value)(valueEntry.getValue())).getString().string.equals(key)) {
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

    private List<ModlObject.Pair> getPairsFromArray(ModlObject modlObject, ModlObject.Array array) {
        return getPairsFromArray(modlObject, array.getArrayItems());
    }

    private List<ModlObject.Pair> getPairsFromArray(ModlObject modlObject, List<ModlObject.ArrayItem> arrayItems) {

        List<ModlObject.Pair> pairs = new LinkedList<>();
        if (arrayItems != null) {
            for (ModlObject.ArrayItem arrayItem : arrayItems) {
                //  NEED TO EVALUATE THE CONDITIONAL HERE!!!
                if (arrayItem.getValue() != null &&
                        arrayItem.getValue().getPair() != null) {
                    pairs.add(arrayItem.getValue().getPair());
                } else {
                    // handle conditionals
                    List<ModlObject.ArrayItem> newArrayItems = interpret(modlObject, arrayItem.getArrayConditional());
                    List<ModlObject.Pair> newPairs = getPairsFromArray(modlObject, newArrayItems);
                    for (ModlObject.Pair p : newPairs) {
                        pairs.add(p);
                    }
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
                if (pairHasKey(pair, entry.getKey())) {
                    // Only add the new key if it does not already exist in the pair!
                    continue;
                }
                ModlObject.Pair newPair = modlObject.new Pair();
                newPair.setKey(entry.getKey());
                // Fix this up for new grammar structures
                ModlObject.ValueItem valueItem = modlObject.new ValueItem();
                valueItem.setValue((ModlObject.Value)entry.getValue());
                newPair.addValueItem(valueItem);
                ModlObject.MapItem mapItem = modlObject.new MapItem();
                mapItem.setPair(newPair);
                pair.getMap().addMapItem(mapItem);
            }
        }

    }

    private boolean pairHasKey(ModlObject.Pair pair, String key) {
        // Does this pair have a map which has a pair which has this key?
        ModlObject.Map map = pair.getMap();
        if (map == null || map.getMapItems() == null) {
            return false;
        }
        for (ModlObject.MapItem mapItem : map.getMapItems()) {
            if (mapItem.getPair() != null && mapItem.getPair().getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    private boolean mapPairAlready(ModlObject.Pair originalPair) {
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
            if (((ModlObject.Value)getModlClass(originalKey).get("*name")) != null &&
                    ((ModlObject.Value)getModlClass(originalKey).get("*name")).getString() != null){
                return ((ModlObject.Value) (getModlClass(originalKey).get("*name"))).getString().string;
            }
            if (((ModlObject.Value)getModlClass(originalKey).get("*n")) != null &&
                    ((ModlObject.Value)getModlClass(originalKey).get("*n")).getString() != null){
                return ((ModlObject.Value) (getModlClass(originalKey).get("*n"))).getString().string;
            }
        }
        return originalKey;
    }

    private ModlObject.Pair transformValue(ModlObject modlObject, ModlObject.Pair originalPair) {
        if (getModlClass(originalPair.getKey()) != null) {
            if ((getModlClass(originalPair.getKey()).get("*name") != null && ((getModlClass(originalPair.getKey()).get("*name").equals("_v"))  ||
               (getModlClass(originalPair.getKey()).get("*name").equals("var")))) ||
                    (getModlClass(originalPair.getKey()).get("*n") != null &&
                            ((getModlClass(originalPair.getKey()).get("*n").equals("_v"))  ||
                    (getModlClass(originalPair.getKey()).get("*n").equals("var"))))) {
                modlConfig.loadConfigNumberedVariables(originalPair.getValueItems());
            } else {
                if (getModlClass(originalPair.getKey()).get("*superclass") != null &&
                        getModlClass(originalPair.getKey()).get("*superclass").equals("str")) {
                    ModlObject.Pair pair = modlObject.new Pair();
                    pair.setKey(originalPair.getKey());
                    if (originalPair.getValueItems().get(0).getValue() == null) {
                        return originalPair;
                    }
                    if (originalPair.getValueItems().get(0).getValue().getString() != null) {
                        return originalPair;
                    }
                    ModlObject.Value value = makeValueString(modlObject, originalPair.getValueItems().get(0).getValue());
                    ModlObject.ValueItem valueItem = modlObject.new ValueItem();
                    valueItem.setValue(value);
                    pair.addValueItem(valueItem);
                    return pair;
                }
            }
        }
        return originalPair;
    }

    private ModlObject.Value interpret(ModlObject modlObject, ModlObject.Value originalValue) {
        if (originalValue == null) {
            return null;
        }

        ModlObject.Value value = modlObject.new Value();

        value.setPair(interpret(modlObject, originalValue.getPair()));
        value.setMap(interpret(modlObject, originalValue.getMap()));
        value.setArray(interpret(modlObject, originalValue.getArray()));
        value.setQuoted(interpret(modlObject, originalValue.getQuoted()));
        value.setNumber(interpret(modlObject, originalValue.getNumber()));
        value.setTrueVal(interpret(modlObject, originalValue.getTrueVal()));
        value.setFalseVal(interpret(modlObject, originalValue.getFalseVal()));
        value.setNullVal(interpret(modlObject, originalValue.getNullVal()));
        if (value.getString() == null) {
            value.setString(interpret(modlObject, originalValue.getString()));
        }

        return value;
    }

    private ModlObject.Map interpret(ModlObject modlObject, ModlObject.Map originalMap) {
        if (originalMap == null) {
            return null;
        }

        ModlObject.Map map = modlObject.new Map();

        if (originalMap.getMapItems() != null) {
            for (ModlObject.MapItem originalMapItem : originalMap.getMapItems()) {
                List<ModlObject.MapItem> mapItems = interpret(modlObject, originalMapItem);
                for (ModlObject.MapItem mapItem : mapItems) {
                    if (mapItem != null) {
                        if (!mapItem.getPair().getKey().startsWith("_") &&
                                !mapItem.getPair().getKey().startsWith("*") &&
                                !mapItem.getPair().getKey().equals("?")) {
//                        if (!mapItem.getPair().getKey().startsWith("_")) {
                            map.addMapItem(mapItem);
                        }
                    }
                }
            }
        }
        return map;
    }


    private List<ModlObject.MapItem> interpret(ModlObject modlObject, ModlObject.MapItem originalMapItem) {
        if (originalMapItem == null) {
            return null;
        }

        List<ModlObject.MapItem> mapItems = new LinkedList<>();

        if (originalMapItem.getPair() != null) {
                ModlObject.Pair pair = interpret(modlObject, originalMapItem.getPair());
                if (pair != null) {
                    if (!pair.getKey().startsWith("_")) {
                    ModlObject.MapItem mapItem = modlObject.new MapItem();
                        mapItem.setPair(pair);
                        mapItems.add(mapItem);
                    }
                }
        }
        if (originalMapItem.getMapConditional() != null) {
            // evaluate the conditional
            mapItems = interpret(modlObject, originalMapItem.getMapConditional());
        }
        return mapItems;
    }


    private ModlObject.Array interpret(ModlObject modlObject, ModlObject.Array originalArray) {
        if (originalArray == null) {
            return null;
        }
        ModlObject.Array array = modlObject.new Array();

        for (ModlObject.ArrayItem originalArrayItem : originalArray.getArrayItems()) {
            List<ModlObject.ArrayItem> arrayItems = interpret(modlObject, originalArrayItem);
            if (arrayItems != null) {
                for (ModlObject.ArrayItem arrayItem : arrayItems) {
                    array.addArrayItem(arrayItem);
                }
            }
        }

        return array;
    }

    private List<ModlObject.ArrayItem> interpret(ModlObject modlObject, ModlObject.ArrayItem originalArrayItem) {
        if (originalArrayItem == null) {
            return null;
        }
        List<ModlObject.ArrayItem> arrayItems = new LinkedList<>();


        if (originalArrayItem.getValue() != null) {
            ModlObject.Value value = interpret(modlObject, originalArrayItem.getValue());
            ModlObject.ArrayItem arrayItem = modlObject.new ArrayItem();
            arrayItem.setValue(value);
            arrayItems.add(arrayItem);
        }
        if (originalArrayItem.getArrayConditional() != null) {
            arrayItems = interpret(modlObject, originalArrayItem.getArrayConditional());
        }

        return arrayItems;
    }

    // We don't want to return this as a conditional any more!
    // We want to return it as whatever the conditional evaluates to
    private ModlObject.ValueItem interpret(ModlObject modlObject, ModlObject.ValueConditional originalConditional) {
        if (originalConditional == null) {
            return null;
        }
        for (Map.Entry<ModlObject.ConditionTest, ModlObject.ValueConditionalReturn> originalConditionalEntry : originalConditional.getConditionals().entrySet()) {
            // Does this conditional evaluate?
            ModlObject.ConditionTest conditionalTest = originalConditionalEntry.getKey();
            if (evaluates(conditionalTest))  {
                // NEED TO INTERPRET THE VALUES!!!
                ModlObject.ValueItem returnValueItem = modlObject.new ValueItem();
                for (ModlObject.ValueItem valueItem : originalConditionalEntry.getValue().getValueItems()) {
                    ModlObject.ValueItem vi = interpret(modlObject, valueItem);
                        returnValueItem.addValueItem(vi);
                }
                return returnValueItem;
            }
        }

        return null;
    }

    private List<ModlObject.ArrayItem> interpret(ModlObject modlObject, ModlObject.ArrayConditional originalConditional) {
        if (originalConditional == null) {
            return null;
        }
        if (originalConditional.getConditionals() != null) {
            for (Map.Entry<ModlObject.ConditionTest, ModlObject.ArrayConditionalReturn> originalConditionalEntry : originalConditional.getConditionals().entrySet()) {
                // Does this conditional evaluate?
                ModlObject.ConditionTest conditionalTest = originalConditionalEntry.getKey();
                if (evaluates(conditionalTest)) {
                    // NEED TO INTERPRET THE VALUES!!!
                    List<ModlObject.ArrayItem> returnArrayItems = new LinkedList<>();
                    for (ModlObject.ArrayItem arrayItem : originalConditionalEntry.getValue().getArrayItems()) {
                        List<ModlObject.ArrayItem> arrayItems = interpret(modlObject, arrayItem);
                        for (ModlObject.ArrayItem ai : arrayItems) {
                            returnArrayItems.add(ai);
                        }
                    }
                    return returnArrayItems;
                }
            }
        }

        return null;
    }

    private List<ModlObject.MapItem> interpret(ModlObject modlObject, ModlObject.MapConditional originalConditional) {
        if (originalConditional == null) {
            return null;
        }
        for (Map.Entry<ModlObject.ConditionTest, ModlObject.MapConditionalReturn> originalConditionalEntry : originalConditional.getConditionals().entrySet()) {
            // Does this conditional evaluate?
            ModlObject.ConditionTest conditionalTest = originalConditionalEntry.getKey();
            if (evaluates(conditionalTest))  {
                // NEED TO INTERPRET THE VALUES!!!
                List<ModlObject.MapItem> returnMapItems = new LinkedList<>();
                for (ModlObject.MapItem mapItem : originalConditionalEntry.getValue().getMapItems()) {
                    List<ModlObject.MapItem> mapItems = interpret(modlObject, mapItem);
                    for (ModlObject.MapItem mi : mapItems) {
                        returnMapItems.add(mi);
                    }
                }
                return returnMapItems;
            }
        }

        return null;
    }

    private List<ModlObject.Structure> interpret(ModlObject modlObject, ModlObject.TopLevelConditional originalConditional) {
        if (originalConditional == null) {
            return null;
        }
        for (Map.Entry<ModlObject.ConditionTest, ModlObject.TopLevelConditionalReturn> originalConditionalEntry : originalConditional.getConditionals().entrySet()) {
            // Does this conditional evaluate?
            ModlObject.ConditionTest conditionalTest = originalConditionalEntry.getKey();
            if (evaluates(conditionalTest))  {
                // NEED TO INTERPRET THE VALUES!!!
                List<ModlObject.Structure> returnStructures = new LinkedList<>();
                for (ModlObject.Structure structure : originalConditionalEntry.getValue().getStructures()) {
                    List<ModlObject.Structure> structures = interpret(modlObject, structure);
                    if (structures != null) {
                        for (ModlObject.Structure s : structures) {
                            returnStructures.add(s);
                        }
                    }
                }
                return returnStructures;
            }
        }

        return null;
    }


    private boolean evaluates(ModlObject.ConditionTest conditionalTest) {
        int nullCount = 0;
        List<Map.Entry<ModlObject.SubCondition, ImmutablePair<java.lang.String, Boolean>>> conditionalTestOrderedList = new LinkedList<>();
        for (Map.Entry<ModlObject.SubCondition, ImmutablePair<java.lang.String, Boolean>> conditionalTestEntry : conditionalTest.getSubConditionMap().entrySet()) {
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
        for (Map.Entry<ModlObject.SubCondition, ImmutablePair<java.lang.String, Boolean>> conditionalTestEntry : conditionalTestOrderedList) {
            ModlObject.SubCondition subCondition = conditionalTestEntry.getKey();
            ImmutablePair<String, Boolean> conditionTestOperatorPair = conditionalTestEntry.getValue();
            String conditionTestOperator = conditionTestOperatorPair.getLeft();
            Boolean shouldNegate = conditionTestOperatorPair.getRight();
            boolean subConditionReturn = true;
            if (subCondition instanceof ModlObject.ConditionGroup) {
                subConditionReturn = evaluates((ModlObject.ConditionGroup)subCondition);
            } else if (subCondition instanceof ModlObject.Condition) {
                subConditionReturn = evaluates((ModlObject.Condition)subCondition);
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

    private boolean evaluates(ModlObject.ConditionGroup conditionGroup) {
        List<ImmutablePair<ModlObject.ConditionTest, java.lang.String>> orderedConditionalTestList = new LinkedList<>();
        int nullCount = 0;
        for (ImmutablePair<ModlObject.ConditionTest, java.lang.String> conditionalTestEntry : conditionGroup.getConditionsTestList()) {
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
        for (ImmutablePair<ModlObject.ConditionTest, java.lang.String> conditionTestPair : orderedConditionalTestList) {
            ModlObject.ConditionTest ct = conditionTestPair.getLeft();
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

    private boolean evaluates(ModlObject.Condition condition) {
        String keyString = condition.getKey();
        // Try to do object referencing and string transformation on the key
        String key = transformConditionalArgument(keyString);

        String conditionOperator = condition.getOperator();
        List<ModlObject.Value> values = condition.getValues();

        // Now evaluate it!!
        // Should either be number, string or quoted

        if (values.size() > 1) {
            // This MUST be a "variable assumption"
            // Check that the operator is "="
            // We need to check if the key is equal to ANY of the values
            for (ModlObject.Value value: values) {
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
                return conditionalEquals(key.toString(), val);
            }
            if (conditionOperator.equals("!=")) {
                return !(key.toString().equals(val));
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

    private Object getObjectFromValueForCondition(ModlObject.Value value) {
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

    private String transformConditionalArgument(String keyString) {
        StringTransformer stringTransformer = new StringTransformer(modlConfig, stringPairs, variableMethods);
        keyString = stringTransformer.runObjectReferencing("%" + keyString, "%" + keyString, false);
        if (keyString.equals("%"+keyString)) {
            throw new RuntimeException("Couldn't find conditional key");
        }
        return keyString;
    }

    private ModlObject.False interpret(ModlObject modlObject, ModlObject.False falseVal) {
        if (falseVal != null) {
            ModlObject.False f = modlObject.new False();
            return f;
        }
        return null;
    }

    private ModlObject.Null interpret(ModlObject modlObject, ModlObject.Null val) {
        if (val != null) {
            ModlObject.Null n = modlObject.new Null();
            return n;
        }
        return null;
    }

    private ModlObject.True interpret(ModlObject modlObject, ModlObject.True trueVal) {
        if (trueVal != null) {
            ModlObject.True t = modlObject.new True();
            return t;
        }
        return null;
    }

    private ModlObject.String interpretAsString(ModlObject modlObject, ModlObject.Number originalNumber) {
        if (originalNumber != null) {
            ModlObject.String number = modlObject.new String(originalNumber.string);
            return number;
        }
        return null;
    }

    private ModlObject.Number interpret(ModlObject modlObject, ModlObject.Number originalNumber) {
        if (originalNumber != null) {
            ModlObject.Number number = modlObject.new Number(originalNumber.string);
            return number;
        }
        return null;
    }

    private ModlObject.String interpret(ModlObject modlObject, ModlObject.String string) {
        if (string != null) {
            ModlObject.String str = modlObject.new String(transformString(string.string));
            return str;
        }
        return null;
    }

    private ModlObject.Quoted interpret(ModlObject modlObject, ModlObject.Quoted quoted) {
        if (quoted != null) {
            String s = quoted.string;
            ModlObject.Quoted string = modlObject.new Quoted(transformString(s));
            return string;
        }
        return null;
    }

    private String transformString(String s) {
        StringTransformer stringTransformer = new StringTransformer(modlConfig, stringPairs, variableMethods);
        return stringTransformer.transformString(s);
    }

    private void checkVersion(String version) {
        if (!(version.equals(String.valueOf(CONFIG_VERSION)))) {
            throw new UnsupportedOperationException("Can't handle version " + version +
                " config version - only support version " + CONFIG_VERSION);
        }
    }


}
