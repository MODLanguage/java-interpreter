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

package uk.modl.parser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.tuple.ImmutablePair;
import uk.modl.interpreter.ModlObject;
import uk.modl.parser.printers.ModlObjectJsonSerializer;

import java.util.*;

@JsonSerialize(using = ModlObjectJsonSerializer.class)
public class RawModlObject extends ModlObject {

    public interface SubCondition {}

    public void replaceFirstImport(java.lang.String importValue, RawModlObject newRawModlObject) {
        // Replace the first import pair that we find with the given value
        // When we get to that point, then remove the import pair, and add all the structures in the newRawModlObject
        int count = 0;
        for (Structure structure : structures) {
            if (structure instanceof Pair) {
                Pair pair =((Pair)structure);
                if ((pair.getKey().string.equals("*I")) || (pair.getKey().string.equals("*IMPORT"))) {
                    java.lang.String importLocation = ((String)pair.getValues().get(0)).string;
                    if (importLocation.equals(importValue)) {
                        break;
                    }
                }
            }
            count++;
        }
        // FOUND IT!
        // Now we need to remove this pair
        structures.remove(count);
        // And then replace it with all the structures in the newRawModlObject
        for (Structure newStructure : newRawModlObject.structures) {
            structures.add(count, newStructure);
            count++;
        }

    }


    public class ConditionTest {
        java.util.Map<RawModlObject.SubCondition, ImmutablePair<java.lang.String, Boolean>> subConditionMap = new HashMap<>();

        public void addSubCondition(java.lang.String operator, boolean shouldNegate, SubCondition subCondition) {
            subConditionMap.put(subCondition, new ImmutablePair<java.lang.String, Boolean>(operator, shouldNegate));
        }

        public java.util.Map<SubCondition, ImmutablePair<java.lang.String, Boolean>> getSubConditionMap() {
            return subConditionMap;
        }
    }

    public class Condition implements SubCondition {
        java.lang.String key;
        java.lang.String operator;
        List<Value> values = new LinkedList<>();

        public Condition(java.lang.String key, java.lang.String operator, List<Value> values) {
            this.key = key;
            this.operator = operator;
            this.values = values;
        }

        public java.lang.String getKey() {
            return key;
        }

        public java.lang.String getOperator() {
            return operator;
        }

        public List<Value> getValues() {
            return values;
        }
    }

    public class ConditionGroup implements SubCondition {
        java.util.List<ImmutablePair<ConditionTest, java.lang.String>> conditionsTestList = new LinkedList<>();

        public void addConditionTest(ConditionTest conditionTest, java.lang.String operator) {
            conditionsTestList.add(new ImmutablePair<ConditionTest, java.lang.String>(conditionTest, operator));
        }

        public List<ImmutablePair<ConditionTest, java.lang.String>> getConditionsTestList() {
            return conditionsTestList;
        }
    }


    public class ValueConditional implements Value, Conditional {
        java.util.Map<ConditionTest, ValueConditionalReturn> conditionals;

        public java.util.Map<ConditionTest, ValueConditionalReturn> getConditionals() {
            return conditionals;
        }

        public void addConditional(ConditionTest conditionTest, ValueConditionalReturn conditionalReturn) {
            if (conditionals == null) {
                conditionals = new LinkedHashMap<>();
            }
            conditionals.put(conditionTest, conditionalReturn);
        }
    }

    public class ValueConditionalReturn implements Value {
        List<Value> values;

        public void addValue(Value value) {
            if (values == null) {
                values = new LinkedList<>();
            }
            values.add(value);
        }

        public List<Value> getValues() {
            return values;
        }
    }

    public class ArrayConditional extends Array implements Conditional {
        java.util.Map<ConditionTest, ArrayConditionalReturn> conditionals;

        public java.util.Map<ConditionTest, ArrayConditionalReturn> getConditionals() {
            return conditionals;
        }

        public void addConditional(ConditionTest conditionTest, ArrayConditionalReturn conditionalReturn) {
            if (conditionals == null) {
                conditionals = new LinkedHashMap<>();
            }
            conditionals.put(conditionTest, conditionalReturn);
        }
    }

    public class ArrayConditionalReturn extends Array {
    }

    public interface Conditional {}

    //    public class MapConditional extends Pair implements Conditional {
    public class MapConditional extends Pair implements Conditional {
        java.util.Map<ConditionTest, Map> conditionals;

        public java.util.Map<ConditionTest, Map> getConditionals() {
            return conditionals;
        }

        public void addConditional(ConditionTest conditionTest, Map conditionalReturn) {
            if (conditionals == null) {
                conditionals = new LinkedHashMap<>();
            }
            conditionals.put(conditionTest, conditionalReturn);
        }
    }

    public class TopLevelConditional implements Structure {
        java.util.Map<ConditionTest, TopLevelConditionalReturn> conditionals;

        public java.util.Map<ConditionTest, TopLevelConditionalReturn> getConditionals() {
            return conditionals;
        }

        public void addConditional(ConditionTest conditionTest, TopLevelConditionalReturn conditionalReturn) {
            if (conditionals == null) {
                conditionals = new LinkedHashMap<>();
            }
            conditionals.put(conditionTest, conditionalReturn);
        }
    }

    public class TopLevelConditionalReturn implements Structure {
        List<Structure> structures;

        public void addStructure(Structure structure) {
            if (structures == null) {
                structures = new LinkedList<>();
            }
            structures.add(structure);
        }

        public List<Structure> getStructures() {
            return structures;
        }
    }

}
