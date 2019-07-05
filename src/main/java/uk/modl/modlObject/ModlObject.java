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
import uk.modl.parser.printers.ModlObjectJsonSerializer;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by alex on 13/09/2018.
 */
@JsonSerialize(using = ModlObjectJsonSerializer.class)
public class ModlObject extends ModlValue {

    protected List<Structure> structures = new LinkedList<>();

    @Override
    public boolean isModlObject() {
        return true;
    }

    public List<Structure> getStructures() {
        return structures;
    }

    @Override
    public List<? extends ModlValue> getModlValues() {
        return structures;
    }

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
                Pair pair = (Pair) structure;
                keys.add(pair.getKey().string);
            }
        }
        return keys;
    }

    @Override
    public ModlValue get(java.lang.String name) {
        for (Structure structure : structures) {
            if (structure instanceof Pair) {
                Pair pair = (Pair) structure;
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

    public abstract static class Structure extends ModlValue {
    }

    //    public static class Map implements Structure, ModlValue {
    public static class Map extends Structure {

        List<Pair> pairs = new LinkedList<>();

        @Override
        public boolean isMap() {
            return true;
        }

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
        public List<? extends ModlValue> getModlValues() {
            return pairs;
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

        @Override
        public java.lang.String toString() {
            final java.lang.String s = pairs.stream()
                    .map(p -> "\"" + p.getKey() + "\"=>\"" + p.getValue()
                            .toString() + "\"")
                    .collect(Collectors.joining(", "));
            return "{" + s +
                    '}';
        }
    }

    //    public static class Array implements Structure, ModlValue {
    public static class Array extends Structure {

        List<ModlValue> values = new LinkedList<>();

        @Override
        public boolean isArray() {
            return true;
        }

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
                    if (((Pair) v).getKey().string.equals(name.string)) {
                        return (Pair) v;
                    }
                }
            }
            return null;
        }

        public List<ModlValue> getValues() {
            return values;
        }

        @Override
        public List<? extends ModlValue> getModlValues() {
            return values;
        }

        @Override
        public java.lang.String toString() {
            final java.lang.String valuesStr = this.values.stream()
                    .map(v -> {
                        if (v.isNumber()) {
                            return v.toString();
                        } else {
                            return "\"" + v.toString() + "\"";
                        }
                    })
                    .collect(Collectors.joining(", "));
            return "[" + valuesStr + "]";
        }
    }

    //    public static class Pair implements Structure, ModlValue {
    public static class Pair extends Structure {

        private String key;
        private ModlValue modlValue;

        public Pair() {
        }

        public Pair(String key, ModlValue modlValue) {
            this.key = key;
            this.modlValue = (modlValue);
        }

        @Override
        public boolean isPair() {
            return true;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        @Override
        public List<java.lang.String> getKeys() {
            List<java.lang.String> keys = new LinkedList<>();
            keys.add(key.string);
            return keys;
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
                ((Map) this.modlValue).addPair((Pair) value);
            } else if (this.modlValue instanceof ModlObject.Pair && value instanceof ModlObject.Pair) {
                final ModlObject.Pair oldPair = (Pair) oldValue;
                final ModlObject.Pair newPair = (Pair) value;
                if (oldPair.getKey()
                        .equals(newPair.getKey())) {
                    this.modlValue = new Array();
                    ((Array) this.modlValue).addValue(oldValue);
                    ((Array) this.modlValue).addValue(value);
                } else {
                    this.modlValue = new Map();
                    ((Map) this.modlValue).addPair((Pair) oldValue);
                    ((Map) this.modlValue).addPair((Pair) value);
                }
            } else if (this.modlValue instanceof Array) {
                ((Array) this.modlValue).addValue(value);
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

        @Override
        public java.lang.String toString() {
            return "Pair{" +
                    "key=" + key +
                    ", modlValue=" + modlValue +
                    '}';
        }
    }

    public static class String extends ModlValue {

        public java.lang.String string;

        public String(java.lang.String string) {

            this.string = string;

        }

        @Override
        public boolean isString() {
            return true;
        }

        public java.lang.String toString() {
            return string;
        }

        @Override
        public Object getValue() {
            return string;
        }

        @Override
        public boolean isTerminal() {
            return true;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            final String string1 = (String) o;
            return Objects.equals(string, string1.string);
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }

    public static class Number extends ModlValue {

        public final java.lang.String number;

        public Number(java.lang.String number) {
            this.number = number;
        }

        @Override
        public boolean isNumber() {
            return true;
        }

        @Override
        public Object getValue() {
            return number;
        }

        @Override
        public boolean isTerminal() {
            return true;
        }

        @Override
        public java.lang.String toString() {
            return number;
        }
    }

    public static class True extends ModlValue {

        @Override
        public boolean isTrue() {
            return true;
        }

        @Override
        public boolean isTerminal() {
            return true;
        }

        @Override
        public Object getValue() {
            return true;
        }

        @Override
        public java.lang.String toString() {
            return "true";
        }
    }

    public static class False extends ModlValue {

        @Override
        public boolean isFalse() {
            return true;
        }

        @Override
        public boolean isTerminal() {
            return true;
        }

        @Override
        public Object getValue() {
            return false;
        }

        @Override
        public java.lang.String toString() {
            return "false";
        }
    }

    public static class Null extends ModlValue {

        @Override
        public boolean isNull() {
            return true;
        }

        @Override
        public boolean isTerminal() {
            return true;
        }

        @Override
        public Object getValue() {
            return null;
        }

        @Override
        public java.lang.String toString() {
            return "null";
        }
    }

}
