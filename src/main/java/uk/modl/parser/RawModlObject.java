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
import org.apache.commons.lang3.tuple.MutablePair;
import uk.modl.modlObject.ModlObject;
import uk.modl.modlObject.ModlValue;
import uk.modl.parser.printers.ModlObjectJsonSerializer;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

@JsonSerialize(using = ModlObjectJsonSerializer.class)
public class RawModlObject extends ModlObject {

    // TODO Add manipulation methods to this class

    public void replaceFirstImport(java.lang.String importValue, RawModlObject newRawModlObject) {
        // Replace the first import pair that we find with the given value
        // When we get to that point, then remove the import pair, and add all the structures in the newRawModlObject
        int count = 0;
        for (Structure structure : structures) {
            if (structure instanceof Pair) {
                Pair pair = ((Pair) structure);
                if ((pair.getKey().string.toLowerCase()
                        .equals("*l")) ||
                        (pair.getKey().string.toLowerCase()
                                .equals("*load"))) {
                    java.lang.String importLocation;
                    if (pair.getModlValue() instanceof ModlObject.String) {
                        importLocation = ((String) pair.getModlValue()).string;
                    } else {
                        importLocation = ((Number) pair.getModlValue()).number;
                    }
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

    public interface SubCondition {
    }

    interface Conditional {
    }

    public static class ArrayConditional extends Array implements Conditional {
        java.util.Map<ConditionTest, ArrayConditionalReturn> conditionals;

        public java.util.Map<ConditionTest, ArrayConditionalReturn> getConditionals() {
            return conditionals;
        }

        void addConditional(ConditionTest conditionTest, ArrayConditionalReturn conditionalReturn) {
            if (conditionals == null) {
                conditionals = new LinkedHashMap<>();
            }
            conditionals.put(conditionTest, conditionalReturn);
        }
    }

    public static class ArrayConditionalReturn extends Array {
    }

    //    public static class MapConditional extends Pair implements Conditional {
    public static class MapConditional extends Pair implements Conditional {
        java.util.Map<ConditionTest, Map> conditionals;

        public java.util.Map<ConditionTest, Map> getConditionals() {
            return conditionals;
        }

        void addConditional(ConditionTest conditionTest, Map conditionalReturn) {
            if (conditionals == null) {
                conditionals = new LinkedHashMap<>();
            }
            conditionals.put(conditionTest, conditionalReturn);
        }
    }

    public static class ConditionTest {
        java.util.Map<RawModlObject.SubCondition, MutablePair<java.lang.String, Boolean>>
                subConditionMap =
                new HashMap<>();

        void addSubCondition(java.lang.String operator, boolean shouldNegate, SubCondition subCondition) {
            subConditionMap.put(subCondition, MutablePair.of(operator, shouldNegate));
        }

        public java.util.Map<SubCondition, MutablePair<java.lang.String, Boolean>> getSubConditionMap() {
            return subConditionMap;
        }
    }

    public static class Condition implements SubCondition {
        java.lang.String key;
        java.lang.String operator;
        List<ModlValue> values;

        Condition(java.lang.String key, java.lang.String operator, List<ModlValue> values) {
            this.key = key;
            this.operator = operator;
            this.values = values;
        }

        public java.lang.String getKey() {
            return key;
        }

        public void setKey(final java.lang.String newKey) {
            key = newKey;
        }

        public java.lang.String getOperator() {
            return operator;
        }

        public List<ModlValue> getValues() {
            return values;
        }
    }

    public static class ConditionGroup implements SubCondition {
        java.util.List<MutablePair<ConditionTest, java.lang.String>> conditionsTestList = new LinkedList<>();

        void addConditionTest(ConditionTest conditionTest, java.lang.String operator) {
            conditionsTestList.add(MutablePair.of(conditionTest, operator));
        }

        public List<MutablePair<ConditionTest, java.lang.String>> getConditionsTestList() {
            return conditionsTestList;
        }
    }

    public static class ValueConditional extends ModlValue implements Conditional {
        java.util.Map<ConditionTest, ValueConditionalReturn> conditionals;

        public java.util.Map<ConditionTest, ValueConditionalReturn> getConditionals() {
            return conditionals;
        }

        void addConditional(ConditionTest conditionTest, ValueConditionalReturn conditionalReturn) {
            if (conditionals == null) {
                conditionals = new LinkedHashMap<>();
            }
            conditionals.put(conditionTest, conditionalReturn);
        }
    }

    public static class ValueConditionalReturn extends ModlValue {
        List<ModlValue> values;

        void addValue(ModlValue value) {
            if (values == null) {
                values = new LinkedList<>();
            }
            values.add(value);
        }

        public List<ModlValue> getValues() {
            return values;
        }
    }

    public static class TopLevelConditional extends Structure {
        java.util.Map<ConditionTest, TopLevelConditionalReturn> conditionals;

        public java.util.Map<ConditionTest, TopLevelConditionalReturn> getConditionals() {
            return conditionals;
        }

        void addConditional(ConditionTest conditionTest, TopLevelConditionalReturn conditionalReturn) {
            if (conditionals == null) {
                conditionals = new LinkedHashMap<>();
            }
            conditionals.put(conditionTest, conditionalReturn);
        }
    }

    public static class TopLevelConditionalReturn extends Structure {
        List<Structure> structures;

        void addStructure(Structure structure) {
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
