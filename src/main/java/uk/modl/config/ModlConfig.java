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

import uk.modl.parser.ModlObject;

import java.util.*;
import java.util.function.Function;

public class ModlConfig {

    Map<String, Map<String, Object>> klasses = new LinkedHashMap<>();
    Map<String, String> variables = new HashMap<>();
    Map<Integer, String> numberedVariables = new HashMap<>();

    public void loadConfig(ModlObject configModlObject, Map<String, Function<String, String>> variableMethods) {
        // Remember to define "o" for "object" BEFORE we load the config
        loadModlKlassO(configModlObject);

        // Then, load in all the new definitions, and build the class heirarchy
        // we probably don't need to store a heirarchy - just a Map keyed by klass id
        // as we load in more klasses, we can add more keys, copying the info from the superclass before overriding anything and adding more
        //
        // So first, get a ModlObject from the config file
        // Then, go through each klass, building the config map as we go
        for (ModlObject.Structure structure : configModlObject.getStructures()) {
            loadconfig(structure, variableMethods);
        }
    }

    public void loadconfig(ModlObject.Structure structure, Map<String, Function<String, String>> variableMethods) {
        if (structure.getPair() != null && (structure.getPair().getKey().equals("*I") || structure.getPair().getKey().equals("*S"))) {
            return;
        }
        if (structure.getPair() == null || (!(((structure.getPair().getKey().equals("*class")) ||
                (structure.getPair().getKey().equals("*c"))) || ((structure.getPair().getKey().equals("*vm")) ||
                (structure.getPair().getKey().equals("*m"))) || ((structure.getPair().getKey().equals("*method")) ||
                (structure.getPair().getKey().equals("*variable_map"))) ||
                (structure.getPair().getKey().startsWith("_") || structure.getPair().getKey().equals("?")
//                (structure.getPair().getKey().equals("*")  ||
//                        ((structure.getPair().getKey().equals("*v")) || (structure.getPair().getKey().equals("*var")))
                )))) {
            throw new RuntimeException("Expecting '*class' or '*vm' in MODL config file");
        }
        if (structure.getPair().getKey().equals("*class") || structure.getPair().getKey().equals("*c")) {
            loadConfigStructure(structure);
        } else if (structure.getPair().getKey().equals("*method") || structure.getPair().getKey().equals("*m")) {
            loadVariableMethod(structure, variableMethods);
        } else if (structure.getPair().getKey().equals("*vm") || structure.getPair().getKey().equals("*variable_map")) {
            loadConfigVariable(structure.getPair().getMap().getMapItems());
        } else if (structure.getPair().getKey().equals("?")) {
            if (structure.getPair().getArray() != null) {
                loadConfigNumberedVariablesArray(structure.getPair().getArray().getArrayItems());
            } else {
                loadConfigNumberedVariables(structure.getPair().getValueItems());
            }
        } else {
            if (structure.getPair().getKey().startsWith("_")) {
                loadConfigVar(structure.getPair().getKey().replaceFirst("_",""), structure.getPair());
            } else if (structure.getPair().getKey().startsWith("*")) {
                throw new RuntimeException("Unrecognised configuration instruction : " + structure.getPair().getKey());
            }
        }
    }

    private void loadVariableMethod(ModlObject.Structure structure, Map<String, Function<String, String>> variableMethods) {
        // TODO Load up the values : e.g. :
        /*
*method(
  *id=us
  *name=replace_underscores_with_spaces
  *transform=`replace(_, )`
)
         */
        String methodName = getPairValueFor(structure, "*id");
        if (methodName == null) {
            methodName = getPairValueFor(structure, "*i");
        }
        String description = getPairValueFor(structure, "*name");
        if (description == null) {
            description = getPairValueFor(structure, "*n");
        }
        if (description == null) {
            description = methodName;
        }
        String methodString = getPairValueFor(structure, "*transform");
        if (methodString == null) {
            methodString = getPairValueFor(structure, "*t");
        }
        methodString = methodString.replace("`", "");
        String[] splits =  methodString.split("\\(");
        String calledFunction =splits[0];
        splits[1] = splits[1].substring(0, splits[1].length() -1);
        String[] params = splits[1].split(",");
//        String firstParam = params[0];
//        String secondParam = params[1];
        Function<String, String> function = parameter -> {
            return variableMethods.get(calledFunction).apply(parameter + "," + splits[1]); // TODO splits[1]?
        };
        variableMethods.put(methodName, function);
        int delete = 0;
    }

    public void loadConfigNumberedVariables(List<ModlObject.ValueItem> valueItems) {
        numberedVariables = new HashMap<>();
        // We are expecting a list of Values
        int paramNum = 0;
        loadConfigNumberedVariables(valueItems, paramNum);
    }

    private int loadConfigNumberedVariables(List<ModlObject.ValueItem> valueItems, int paramNum) {
        if (valueItems != null) {
            for (ModlObject.ValueItem val : valueItems) {
                if (val.getValue() != null) {
                    paramNum = addConfigNumberedVariable(paramNum, val.getValue());
                } else {
                    paramNum = loadConfigNumberedVariables(val.getValueItems(), paramNum);
                }
            }
        }
        return paramNum;
    }

    public void loadConfigNumberedVariablesArray(List<ModlObject.ArrayItem> arrayItems) {
        numberedVariables = new HashMap<>();
        // We are expecting a list of Values
        int paramNum = 0;
        loadConfigNumberedVariablesArray(arrayItems, paramNum);
    }

    private int loadConfigNumberedVariablesArray(List<ModlObject.ArrayItem> arrayItems, int paramNum) {
        if (arrayItems != null) {
            for (ModlObject.ArrayItem val : arrayItems) {
                if (val.getValue() != null) {
                    paramNum = addConfigNumberedVariable(paramNum, val.getValue());
//                } else {
//                    paramNum = loadConfigNumberedVariables(val.getValueItems(), paramNum);
                }
            }
        }
        return paramNum;
    }

    private int addConfigNumberedVariable(int paramNum, ModlObject.Value val) {
        if (val.getPair() != null) {
            for (ModlObject.ValueItem vi : val.getPair().getValueItems()) {
                numberedVariables.put(paramNum++, vi.getValue().getString().string);
            }
        } else if (val.getArray() != null) {
            for (ModlObject.ArrayItem vi : val.getArray().getArrayItems()) {
                numberedVariables.put(paramNum++, vi.getValue().getString().string);
            }
        }
        else {
            if (val.getString() != null) {
                numberedVariables.put(paramNum++, val.getString().string);
            } else if (val.getQuoted() != null) {
                numberedVariables.put(paramNum++, val.getQuoted().string);
            } else if (val.getNumber() != null) {
                numberedVariables.put(paramNum++, val.getNumber().string);
            }
        }
        return paramNum;
    }

    private void loadConfigVariable(List<ModlObject.MapItem> mapItems) {
        // Load in the variable
        for (ModlObject.MapItem mapItem : mapItems) {
            loadConfigVar(mapItem.getPair().getKey(), mapItem.getPair());
        }
    }

    private void loadConfigVar(String key, ModlObject.Pair pair) {
        if (pair.getValueItems().get(0).getValue().getString() != null) {
            variables.put(key, pair.getValueItems().get(0).getValue().getString().string);
        } else {
            variables.put(key, pair.getValueItems().get(0).getValue().getQuoted().string);
        }
    }

    private void loadConfigStructure(ModlObject.Structure structure) {
        // Load in the new klass
        HashMap<String, Object> values = new HashMap<>();
        String id = getPairValueFor(structure, "*id");
        if (id == null) {
            id = getPairValueFor(structure, "*i");
        }
        if (id == null) {
            throw new RuntimeException("Can't find *id in *class");
        }
        klasses.put(id, values);
        String superclass = getPairValueFor(structure, "*superclass");
        if (superclass == null) {
            superclass = getPairValueFor(structure, "*s");
        }
        values.put("*superclass", superclass);
        String name = getPairValueFor(structure, "*name");
        if (name == null) {
            name = getPairValueFor(structure, "*n");
        }
        if (name == null) {
            name = id;
        }
        // Remember to see if there is a superclass - if so, then copy all its values in first
        Map<String, Object> superKlass = klasses.get(superclass);
        if (superclass != null) {
            if (superclass.toUpperCase().equals(superclass)) {
                throw new RuntimeException("Can't derive from " + superclass + ", as it in upper case and therefore fixed");
            }
        }
        if (superKlass != null) {
            for (Map.Entry<String, Object> entry : superKlass.entrySet()) {
                values.put(entry.getKey(), entry.getValue());
            }
        }

        // Go through the structure and find all the new values and add them (replacing any already there from superklass)
        for (ModlObject.MapItem mapItem : structure.getPair().getMap().getMapItems()) {
            // Remember to avoid "_id" and "_sc" !
            if (mapItem.getPair().getKey().equals("*id") || mapItem.getPair().getKey().equals("*i") ||
                    mapItem.getPair().getKey().equals("*superclass") || mapItem.getPair().getKey().equals("*s")) {
                continue;
            }
            if (mapItem.getPair().getKey().equals("*assign") || mapItem.getPair().getKey().equals("*a")) {
                if (mapItem.getPair().getArray() != null) {
                    loadParams(values, mapItem.getPair().getArray());
                } else {
                    loadParams(values, mapItem.getPair().getValueItems().get(0).getValue().getArray());
                }
                continue;
            }
            // Now add the new value
            values.put(mapItem.getPair().getKey(), mapItem.getPair().getValueItems().get(0).getValue());
        }
    }

    private void loadParams(HashMap<String, Object> values, ModlObject.Array array) {
        // _params : add like _params<n> where n is number of values in array
        for (ModlObject.ArrayItem v : array.getArrayItems()) {
            ModlObject.Array a = v.getValue().getArray();
            String key = "*params" + a.getArrayItems().size();
            List<ModlObject.Value> vs = new LinkedList<>();
            for (ModlObject.ArrayItem ai : v.getValue().getArray().getArrayItems()) {
                vs.add(ai.getValue());
            }
//            values.put(key, a.getValues())
            values.put(key, vs);
        }
    }

    private String getPairValueFor(ModlObject.Structure structure, String pairValue) {
        for (ModlObject.MapItem mapItem : structure.getPair().getMap().getMapItems()) {
            if (mapItem.getPair().getKey().equals(pairValue)) {
                return mapItem.getPair().getValueItems().get(0).getValue().getString().string;
            }
        }
        return null;
    }

    private void loadModlKlassO(ModlObject modlObject) {
                /*
        klass(
            _id=o;
            _sc=map;
            _name=object;
              _help="All objects without a class use this class";
            _output=m
          )
         */
        Map<String, Object> o = new LinkedHashMap<>();
        ModlObject.String superclass = modlObject.new String("map");
        ModlObject.Value superclassValue = modlObject.new Value();
        superclassValue.setString(superclass);
        o.put("*superclass", superclassValue);
        ModlObject.String name = modlObject.new String("object");
        ModlObject.Value nameValue = modlObject.new Value();
        nameValue.setString(name);
        o.put("*name", nameValue);
        ModlObject.String output = modlObject.new String("map");
        ModlObject.Value outputValue = modlObject.new Value();
        outputValue.setString(output);
        o.put("*output", outputValue);


        klasses.put("o", o);
    }
}
