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

import uk.modl.modlObject.ModlObject;
import uk.modl.modlObject.ModlValue;
import uk.modl.parser.RawModlObject;

import java.util.*;

class ModlClassLoader {

    static void loadClass(RawModlObject.Structure structure,
                          Map<String, Map<String, Object>> klasses,
                          Interpreter interpreter) {
        if (structure instanceof ModlObject.Pair) {
            ModlObject.Pair pair = (ModlObject.Pair) structure;
            if (!(((pair.getKey().string.toLowerCase().equals("*class")) ||
                   (pair.getKey().string.toLowerCase().equals("*c"))))) {
                throw new RuntimeException("Expecting '*class' in ModlClassLoader");
            }
            loadClassStructure(structure, klasses, interpreter);
        }
    }

    private static void loadClassStructure(RawModlObject.Structure structure,
                                           Map<String, Map<String, Object>> klasses,
                                           Interpreter interpreter) {
        // Load in the new klass
        HashMap<String, Object> values = new LinkedHashMap<>();
        String id = getPairValueFor(structure, "*id", interpreter);
        if (id == null) {
            id = getPairValueFor(structure, "*i", interpreter);
        }
        if (id == null) {
            throw new RuntimeException("Can't find *id in *class");
        }
        if (klasses.containsKey(id)) {
            throw new RuntimeException("Interpreter Error: Class name or id already defined - cannot redefine: " + id);
        }

        values.put("*id", id);
        String superclass = getPairValueFor(structure, "*superclass", interpreter);
        if (superclass == null) {
            superclass = getPairValueFor(structure, "*s", interpreter);
        }
        values.put("*superclass", superclass);
        String name = getPairValueFor(structure, "*name", interpreter);
        if (name == null) {
            name = getPairValueFor(structure, "*n", interpreter);
        }
        if (name == null) {
            name = id;
        }
        values.put("*name", name); // TODO ???

        // Is the name already defined as an id or name?
        if (klasses.containsKey(name)) {
            throw new RuntimeException("Interpreter Error: Class name or id already defined - cannot redefine: " +
                                       name);
        } else {
            for (Map<String, Object> kl : klasses.values()) {
                final String existingName = kl.get("*name").toString();
                if (existingName.equals(name) || existingName.equals(id)) {
                    throw new RuntimeException("Interpreter Error: Class name or id already defined - cannot redefine: " +
                                               name);
                }
            }
        }

        klasses.put(id, values);

        // Go through the structure and find all the new values and add them (replacing any already there from superklass)
        for (RawModlObject.Pair mapItem : ((ModlObject.Map) ((ModlObject.Pair) structure).getModlValue()).getPairs()) {
            // Remember to avoid "_id" and "_sc" !
            if (mapItem.getKey().string.toLowerCase().equals("*id") ||
                mapItem.getKey().string.toLowerCase().equals("*i") ||
                mapItem.getKey().string.toLowerCase().equals("*superclass") ||
                mapItem.getKey().string.toLowerCase().equals("*s")) {
                continue;
            }
            if (mapItem.getKey().string.toLowerCase().equals("*assign") ||
                mapItem.getKey().string.toLowerCase().equals("*a")) {
                interpreter.addToUpperCaseInstructions(mapItem.getKey().string);
                if (mapItem.getModlValue() instanceof ModlObject.Array) {
                    loadParams(values, (ModlObject.Array) (mapItem.getModlValue()));
                }
                continue;
            }
            // Now add the new value
            if (mapItem.getKey().string.toLowerCase().equals("*n") ||
                mapItem.getKey().string.toLowerCase().equals("*name")) {
                values.put("*name", mapItem.getModlValue());
            } else {
                values.put(mapItem.getKey().string, mapItem.getModlValue());
            }
        }
    }

    private static void loadParams(HashMap<String, Object> values, RawModlObject.Array array) {
        // _params : add like _params<n> where n is number of values in array
        int previousParamSize = -1;
        for (ModlValue v : array.getValues()) {
            RawModlObject.Array a = (ModlObject.Array) v;
            final int numberOfParams = a.getValues().size();
            if (numberOfParams > previousParamSize) {
                previousParamSize = numberOfParams;
            } else {
                throw new RuntimeException(
                    "Interpreter Error: Error: Key lists in *assign are not in ascending order of list length: " +
                    a.toString());
            }
            String key = "*params" + numberOfParams;
            List<ModlValue> vs = new LinkedList<>(a.getValues());
            values.put(key, vs);
        }
    }

    static String getPairValueFor(RawModlObject.Structure structure, String pairValue, Interpreter interpreter) {
        for (RawModlObject.Pair mapItem : ((ModlObject.Map) ((ModlObject.Pair) structure).getModlValue()).getPairs()) {
            if (mapItem.getKey().string.toLowerCase().equals(pairValue.toLowerCase())) {
                interpreter.addToUpperCaseInstructions(mapItem.getKey().string);
                // TODO This does not need to be a String!
                return ((ModlObject.String) mapItem.getModlValue()).string;
            }
        }
        return null;
    }
}
