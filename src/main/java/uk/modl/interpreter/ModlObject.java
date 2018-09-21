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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import uk.modl.parser.printers.ModlObjectJsonSerializer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by alex on 13/09/2018.
 */
@JsonSerialize(using = ModlObjectJsonSerializer.class)
public class ModlObject {

    public interface Value {}

    public interface Structure {}

    protected List<Structure> structures = new LinkedList<>();

    public List<Structure> getStructures() {
        return structures;
    }

    public void addStructure(Structure structure) {
        if (structure != null) {
            structures.add(structure);
        }
    }

    public class Map implements Structure, Value {
        List<Pair> pairs = new LinkedList<>();

        public void addPair(Pair pair) {
            pairs.add(pair);
        }

//        public List<Value> get(String key) {
        public Value get(String key) {
            for (Pair pair : pairs) {
                if (pair.getKey().string.equals(key.string)) {
//                    return pair.getValues();
                    return pair.getValue();
                }
            }
            return null;
        }

        public Pair get(Integer index) {
            return pairs.get(index);
        }

        public List<Pair> getPairs() {
            return pairs;
        }
    }

    public class Array implements Structure, Value {
        List<Value> values = new LinkedList<>();

        public void addValue(Value value) {
            values.add(value);
        }

        public Value get(int index) {
            return values.get(index);
        }

        public Pair get(String name) {
            for (Value v : values) {
                if (v instanceof Pair) {
                    if (((Pair)v).getKey().string.equals(name.string)) {
                        return (Pair)v;
                    }
                }
            }
            return null;
        }

        public List<Value> getValues() {
            return values;
        }
    }

    public class Pair implements Structure, Value {
        private String key;
        private Value value;

        public Pair() {}

        public Pair(String key, Value value) {
            this.key = key;
            this.value = (value);
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Value getValue() {
            return value;
        }

        public void addValue(Value value) {
            if (value == null) {
                return;
            }
            if (this.value == null) {
                this.value = value;
                return;
            }
            Value oldValue = this.value;
            if (this.value instanceof ModlObject.Map) {
                ((Map)this.value).addPair((Pair)value);
            } else if (this.value instanceof ModlObject.Pair && value instanceof ModlObject.Pair) {
                this.value = new Map();
                ((Map)this.value).addPair((Pair)oldValue);
                ((Map)this.value).addPair((Pair)value);
            } else {
                this.value = new Array();
                ((Array) this.value).addValue(oldValue);
                ((Array) this.value).addValue(value);
            }
        }
    }

    public class String implements Value {
        public final java.lang.String string;

        public String(java.lang.String string) {
            this.string = string;
        }

        public java.lang.String toString() {return string;}
    }

    public class Number implements Value {
        public final java.lang.String number;

        public Number(java.lang.String number) {
            this.number = number;
        }
    }

    public class True implements Value {

    }

    public class False implements Value {

    }

    public class Null implements Value {

    }

}
