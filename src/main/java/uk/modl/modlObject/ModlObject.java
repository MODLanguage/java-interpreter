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

package uk.modl.modlObject;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import uk.modl.interpreter.StringEscapeReplacer;
import uk.modl.parser.printers.ModlObjectJsonSerializer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by alex on 13/09/2018.
 */
@JsonSerialize(using = ModlObjectJsonSerializer.class)
public class ModlObject extends ModlValue {

    @Override
    public boolean isModlObject() { return true; };

    public abstract static class Structure extends ModlValue {}

    protected List<Structure> structures = new LinkedList<>();

    public List<Structure> getStructures() {
        return structures;
    }

    @Override
    public List<? extends ModlValue> getModlValues() { return structures; }

    public void addStructure(Structure structure) {
        if (structure != null) {
            structures.add(structure);
        }
    }

    @Override
    public List<java.lang.String> getKeys() {
        List<java.lang.String> keys = new LinkedList<>();
        for (Structure structure : structures) {
            if (structure instanceof Pair) {
                Pair pair = (Pair)structure;
                keys.add(pair.getKey().string);
            }
        }
        return keys;
    }

    @Override
    public ModlValue get(java.lang.String name) {
        for (Structure structure : structures) {
            if (structure instanceof Pair) {
                Pair pair = (Pair)structure;
                if (pair.getKey().string.equals(name)) {
                    return pair.getModlValue();
                }
            }
        }
        return null;
    }

    public ModlValue get(Integer index) {
        return structures.get(index);
    }

//    public static class Map implements Structure, ModlValue {
    public static class Map extends Structure {

        @Override
        public boolean isMap() { return true; };

        List<Pair> pairs = new LinkedList<>();

        public void addPair(Pair pair) {
            pairs.add(pair);
        }

//        public List<Value> get(String key) {
        public ModlValue get(String key) {
            for (Pair pair : pairs) {
                if (pair.getKey().string.equals(key.string)) {
//                    return pair.getValues();
                    return pair.getModlValue();
                }
            }
            return null;
        }

        @Override
        public Pair get(Integer index) {
            return pairs.get(index);
        }

        public List<Pair> getPairs() {
            return pairs;
        }

        @Override
        public List<? extends ModlValue> getModlValues() { return pairs; }

        @Override
        public List<java.lang.String> getKeys() {
            List<java.lang.String> keys = new LinkedList<>();
            for (Structure structure : pairs) {
                if (structure instanceof Pair) {
                    Pair pair = (Pair)structure;
                    keys.add(pair.getKey().string);
                }
            }
            return keys;
        }

        @Override
        public ModlValue get(java.lang.String name) {
            for (Pair pair : pairs) {
                    if (pair.getKey().string.equals(name)) {
                        return pair.getModlValue();
                    }
                }
            return null;
        }

    }

//    public static class Array implements Structure, ModlValue {
    public static class Array extends Structure {

        @Override
        public boolean isArray() { return true; };

        List<ModlValue> values = new LinkedList<>();

        public void addValue(ModlValue value) {
            values.add(value);
        }

        @Override
        public ModlValue get(Integer index) {
            return values.get(index);
        }

        public Pair get(String name) {
            for (ModlValue v : values) {
                if (v instanceof Pair) {
                    if (((Pair)v).getKey().string.equals(name.string)) {
                        return (Pair)v;
                    }
                }
            }
            return null;
        }

        public List<ModlValue> getValues() {
            return values;
        }

        @Override
        public List<? extends ModlValue> getModlValues() { return values; }

    }

//    public static class Pair implements Structure, ModlValue {
    public static class Pair extends Structure {

        @Override
        public boolean isPair() { return true; };

        private String key;
        private ModlValue modlValue;

        public Pair() {}

        public Pair(String key, ModlValue modlValue) {
            this.key = key;
            this.modlValue = (modlValue);
        }

        public String getKey() {
            return key;
        }

        @Override
        public List<java.lang.String> getKeys() {
            List<java.lang.String> keys = new LinkedList<>();
            keys.add(key.string);
            return keys;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public ModlValue getModlValue() {
            return modlValue;
        }

        public void addModlValue(ModlValue value) {
            if (value == null) {
                return;
            }
            if (this.modlValue == null) {
                this.modlValue = value;
                return;
            }
            ModlValue oldValue = this.modlValue;
            if (this.modlValue instanceof ModlObject.Map) {
                ((Map)this.modlValue).addPair((Pair)value);
            } else if (this.modlValue instanceof ModlObject.Pair && value instanceof ModlObject.Pair) {
                this.modlValue = new Map();
                ((Map)this.modlValue).addPair((Pair)oldValue);
                ((Map)this.modlValue).addPair((Pair)value);
            } else {
                this.modlValue = new Array();
                ((Array) this.modlValue).addValue(oldValue);
                ((Array) this.modlValue).addValue(value);
            }
        }

        @Override
        public Object getValue() {
            return modlValue;
        }

        @Override
        public List<? extends ModlValue> getModlValues() {
            List<ModlValue> values = new LinkedList<>();
            values.add(modlValue);
            return values;
        }

        public void setModlValue(ModlValue mv) {
            this.modlValue = mv;
        }
    }

    public static class String extends ModlValue {

        @Override
        public boolean isString() { return true; };

        public final java.lang.String string;

        public String(java.lang.String string) {

            this.string = StringEscapeReplacer.replace(string);
            
        }

        public java.lang.String toString() {return string;}

        @Override
        public Object getValue() {
            return string;
        }

        @Override
        public boolean isTerminal() { return true; };

    }

    public static class Number extends ModlValue {

        @Override
        public boolean isNumber() { return true; };

        public final java.lang.String number;

        public Number(java.lang.String number) {
            this.number = number;
        }

        @Override
        public Object getValue() {
            return number;
        }

        @Override
        public boolean isTerminal() { return true; };

    }

    public static class True extends ModlValue {

        @Override
        public boolean isTrue() { return true; };

        @Override
        public boolean isTerminal() { return true; };

        @Override
        public Object getValue() {
            return true;
        }

    }

    public static class False extends ModlValue {

        @Override
        public boolean isFalse() { return true; };

        @Override
        public boolean isTerminal() { return true; };

        @Override
        public Object getValue() {
            return false;
        }
    }

    public static class Null extends ModlValue {

        @Override
        public boolean isNull() { return true; };

        @Override
        public boolean isTerminal() { return true; };

        @Override
        public Object getValue() {
            return null;
        }
    }

}
