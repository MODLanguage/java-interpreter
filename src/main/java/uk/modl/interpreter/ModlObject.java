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

    List<Structure> structures = new LinkedList<>();

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

        public Value get(String key) {
            for (Pair pair : pairs) {
                if (pair.getKey().string.equals(key)) {
                    return pair;
                }
            }
            return null;
        }

        public Value get(Integer index) {
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
            this.value = value;
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

        public void setValue(Value value) {
            this.value = value;
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
            this.value = new Array();
            ((Array) this.value).addValue(oldValue);
            ((Array) this.value).addValue(value);
        }
    }

    public class String implements Value {
        public final java.lang.String string;

        public String(java.lang.String string) {
            this.string = string;
        }
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
