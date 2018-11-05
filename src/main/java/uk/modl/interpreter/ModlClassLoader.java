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

public class ModlClassLoader {

    public static void loadClass(RawModlObject.Structure structure, Map<String, Map<String, Object>> klasses) {
        if (structure instanceof ModlObject.Pair) {
            ModlObject.Pair pair = (ModlObject.Pair)structure;
            if (pair == null || (!(((pair.getKey().string.equals("*class")) ||
                    (pair.getKey().string.equals("*c")))))) {
                throw new RuntimeException("Expecting '*class' in ModlClassLoader");
            }
                loadClassStructure(structure, klasses);
        }
    }

    private static void loadClassStructure(RawModlObject.Structure structure, Map<String, Map<String, Object>> klasses) {
        // Load in the new klass
        HashMap<String, Object> values = new LinkedHashMap<>();
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
        for (RawModlObject.Pair mapItem : ((ModlObject.Map)((ModlObject.Pair)structure).getModlValue()).getPairs()) {
            // Remember to avoid "_id" and "_sc" !
            if (mapItem.getKey().string.equals("*id") || mapItem.getKey().string.equals("*i") ||
                    mapItem.getKey().string.equals("*superclass") || mapItem.getKey().string.equals("*s")) {
                continue;
            }
            if (mapItem.getKey().string.equals("*assign") || mapItem.getKey().string.equals("*a")) {
                if (mapItem.getModlValue() instanceof ModlObject.Array) {
                    loadParams(values, (ModlObject.Array) (mapItem.getModlValue()));
                }
                continue;
            }
            // Now add the new value
            values.put(mapItem.getKey().string, mapItem.getModlValue());
        }
    }

    private static void loadParams(HashMap<String, Object> values, RawModlObject.Array array) {
        // _params : add like _params<n> where n is number of values in array
        for (ModlValue v : array.getValues()) {
            RawModlObject.Array a = (ModlObject.Array)v;
            String key = "*params" + a.getValues().size();
            List<ModlValue> vs = new LinkedList<>();
            for (ModlValue ai : a.getValues()) {
                vs.add(ai);
            }
            values.put(key, vs);
        }
    }

    public static String getPairValueFor(RawModlObject.Structure structure, String pairValue) {
        for (RawModlObject.Pair mapItem : ((ModlObject.Map)((ModlObject.Pair)structure).getModlValue()).getPairs()) {
            if (mapItem.getKey().string.equals(pairValue)) {
                // TODO This does not need to be a String!
                return ((ModlObject.String)mapItem.getModlValue()).string;
            }
        }
        return null;
    }

    public static void loadModlKlassO(RawModlObject rawModlObject, Map<String, Map<String, Object>> klasses) {
                /*
        klass(
            _id=o;
            _sc=map;
            _name=object;
              _help="All objects without a class use this class";
            _output=m
          )
         */
        // TODO Get this to work for including more files during the load. Anything to do?
        Map<String, Object> o = new LinkedHashMap<>();
        RawModlObject.String superclass = rawModlObject.new String("map");
        o.put("*superclass", superclass);
        RawModlObject.String name = rawModlObject.new String("o");
        o.put("*name", name);
        RawModlObject.String output = rawModlObject.new String("map");
        o.put("*output", output);


        klasses.put("o", o);
    }
}
