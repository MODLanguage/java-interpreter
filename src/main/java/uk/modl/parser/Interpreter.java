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

import org.apache.commons.lang3.tuple.ImmutablePair;
import uk.modl.parser.printers.JsonPrinter;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Interpreter {

    public static int MODL_VERSION = 1;


    public static String parseToJson(String input) throws IOException {
        ModlObject modlObject = interpret(input);

        JsonPrinter jsonPrinter = new JsonPrinter();
        String json = jsonPrinter.printModl(modlObject);
        return json;

    }

    public static ModlObject interpret(String input) throws IOException {
        ModlParsed modlParsed = Parser.parse(input);

        return interpret(modlParsed);
    }

    private static ModlObject interpret(ModlParsed modlParsed) {
        // Go through the ModlParsed object and transform it into a ModlObject object, using the rules we know about and any config rules
        ModlObject modlObject = new ModlObject();


            for (ModlParsed.Structure parsedStructure : modlParsed.getStructures()) {
                ModlObject.Structure structure = interpret(modlObject, parsedStructure);
                if (structure.getPair() == null && structure.getArray() == null &&  structure.getTopLevelConditional() == null &&
                        structure.getMap() == null) {
                    continue;
                }
                modlObject.addStructure(structure);
            }

        return modlObject;
    }

    private static ModlObject.Structure interpret(ModlObject modlObject, ModlParsed.Structure parsedStructure) {
        if (parsedStructure == null) {
            return null;
        }

        ModlObject.Structure structure = modlObject.new Structure();

        structure.setMap(interpret(modlObject, parsedStructure.getMap()));
        structure.setArray(interpret(modlObject, parsedStructure.getArray()));
        ModlObject.Pair pair = interpret(modlObject, parsedStructure.getPair());
        if (pair != null) {
            structure.setPair(pair);
        }
        structure.setTopLevelConditional(interpret(modlObject, parsedStructure.getTopLevelConditional()));

        return structure;
    }

    private static ModlObject.Map interpret(ModlObject modlObject, ModlParsed.Map parsedMap) {
        if (parsedMap == null) {
            return null;
        }

        ModlObject.Map map = modlObject.new Map();

        if (parsedMap.getMapItems() != null) {
            for (ModlParsed.MapItem mapItemParsed : parsedMap.getMapItems()) {
                ModlObject.MapItem mapItem = interpret(modlObject, mapItemParsed);
                if (mapItem != null) {
                    map.addMapItem(mapItem);
                }
            }
        }

        return map;
    }

    private static ModlObject.MapItem interpret(ModlObject modlObject, ModlParsed.MapItem parsedMapItem) {
        if (parsedMapItem == null) {
            return null;
        }
        ModlObject.MapItem mapItem = modlObject.new MapItem();
        if (parsedMapItem.getMapConditional() != null) {
            mapItem.setMapConditional(interpret(modlObject, parsedMapItem.getMapConditional()));
        }
        if (parsedMapItem.getPair() != null) {
            // TODO If we get back an unnamed pair which contains a list of value items, then move the value items directly into this pair
            mapItem.setPair(interpret(modlObject, parsedMapItem.getPair()));
        }
        return mapItem;
    }


        private static ModlObject.Value interpret(ModlObject modlObject, ModlParsed.Value parsedValue) {
        if (parsedValue == null) {
            return null;
        }

        ModlObject.Value value = modlObject.new Value();

        value.setPair(interpret(modlObject, parsedValue.getPair()));
        value.setMap(interpret(modlObject, parsedValue.getMap()));
        value.setArray(interpret(modlObject, parsedValue.getArray()));
        value.setQuoted(interpret(modlObject, parsedValue.getQuoted()));
        value.setNumber(interpret(modlObject, parsedValue.getNumber()));
        value.setTrueVal(interpret(modlObject, parsedValue.getTrueVal()));
        value.setFalseVal(interpret(modlObject, parsedValue.getFalseVal()));
        value.setNullVal(interpret(modlObject, parsedValue.getNullVal()));
        value.setString(interpret(modlObject, parsedValue.getString()));

        if (parsedValue.getMap() != null && value.getMap() == null) {
            value.setMap(modlObject.new Map());
        }
        if (parsedValue.getArray() != null && value.getArray() == null) {
            value.setArray(modlObject.new Array());
        }

        return value;
    }

    private static ModlObject.ConditionTest interpret(ModlObject modlObject, ModlParsed.ConditionTest conditionTestParsed) {
        if (conditionTestParsed == null) {
            return null;
        }

        ModlObject.ConditionTest conditionTest = modlObject.new ConditionTest();
        // SubConditions are either ConditionGroups or Conditions
        for (ImmutablePair<ModlParsed.SubCondition, ImmutablePair<String, Boolean>> subConditionPair : conditionTestParsed.subConditionList) {
            ModlParsed.SubCondition subCondition = subConditionPair.getLeft();
            ImmutablePair<java.lang.String, Boolean> operatorPair = subConditionPair.getRight();
            String operator = operatorPair.getLeft();
            Boolean shouldNegate = operatorPair.getRight();
            if (subCondition instanceof ModlParsed.ConditionGroup) {
                ModlObject.ConditionGroup conditionGroup = interpret(modlObject, (ModlParsed.ConditionGroup)subCondition);
                conditionTest.addSubCondition(operator, shouldNegate, conditionGroup);
            } else if (subCondition instanceof ModlParsed.Condition) {
                ModlObject.Condition condition = interpret(modlObject, (ModlParsed.Condition)subCondition);
                conditionTest.addSubCondition(operator, shouldNegate, condition);
            }
        }

        return conditionTest;
    }

    private static ModlObject.Condition interpret(ModlObject modlObject, ModlParsed.Condition conditionParsed) {
        if (conditionParsed == null) {
            return null;
        }

        String key = conditionParsed.key;
        String operator = conditionParsed.operator;
        List<ModlObject.Value> values = new LinkedList<>();
        for (ModlParsed.Value valueParsed : conditionParsed.values) {
            ModlObject.Value value = interpret(modlObject, valueParsed);
            values.add(value);
        }
        ModlObject.Condition condition = modlObject.new Condition(key, operator, values);
        return condition;
    }

    private static ModlObject.ConditionGroup interpret(ModlObject modlObject, ModlParsed.ConditionGroup conditionGroupParsed) {
        if (conditionGroupParsed == null) {
            return null;
        }
        ModlObject.ConditionGroup conditionGroup = modlObject.new ConditionGroup();
        for (org.apache.commons.lang3.tuple.ImmutablePair<ModlParsed.ConditionTest, java.lang.String> conditionTestPair : conditionGroupParsed.conditionsTestList) {
            ModlParsed.ConditionTest conditionTestParsed = conditionTestPair.getLeft();
            String operator = conditionTestPair.getRight();
            ModlObject.ConditionTest conditionTest = interpret(modlObject, conditionTestParsed);
            conditionGroup.addConditionTest(conditionTest, operator);
        }

        return conditionGroup;
    }

    private static ModlObject.Pair interpret(ModlObject modlObject, ModlParsed.Pair pairParsed) {
        if (pairParsed == null) {
            return null;
        }
        if (pairParsed.getKey() != null && (pairParsed.getKey().equals("_S") || (pairParsed.getKey().equals("_SYNTAX")))) {
            // This is the version number - check it and then ignore it
            if (!(pairParsed.getValueItems().get(0).getValue().getNumber().string.equals(String.valueOf(MODL_VERSION)))) {
                throw new UnsupportedOperationException("Can't handle MODL version " + pairParsed.getValueItems().get(0).getValue().getNumber());
            }
            return null;
        }
        ModlObject.Pair pair = modlObject.new Pair();

        pair.setKey(pairParsed.getKey());

        pair.setMap(interpret(modlObject, pairParsed.getMap()));
        pair.setArray(interpret(modlObject, pairParsed.getArray()));
        if (pairParsed.getValueItems() != null) {
            for (ModlParsed.ValueItem valueParsed : pairParsed.getValueItems()) {
                ModlObject.ValueItem valueItem = interpret(modlObject, valueParsed);
                pair.addValueItem(valueItem);
            }
        }

        return pair;
    }

    private static ModlObject.Array interpret(ModlObject modlObject, ModlParsed.Array arrayParsed) {
        if (arrayParsed == null) {
            return null;
        }
        ModlObject.Array array = modlObject.new Array();

        if (arrayParsed.getArrayItems() != null) {
            for (ModlParsed.ArrayItem arrayItemParsed : arrayParsed.getArrayItems()) {
                ModlObject.ArrayItem arrayItem = interpret(modlObject, arrayItemParsed);
                if (arrayItem != null) {
                    array.addArrayItem(arrayItem);
                }
            }
        }

        return array;
    }


    private static ModlObject.ArrayItem interpret(ModlObject modlObject, ModlParsed.ArrayItem arrayItemParsed) {
        if (arrayItemParsed == null) {
            return null;
        }
        ModlObject.ArrayItem arrayItem = modlObject.new ArrayItem();

        if (arrayItemParsed.getArrayConditional() != null) {
            arrayItem.setArrayConditional(interpret(modlObject, arrayItemParsed.getArrayConditional()));
        }
        if (arrayItemParsed.getValue() != null) {
            arrayItem.setValue(interpret(modlObject, arrayItemParsed.getValue()));
        }

        return arrayItem;
    }

    private static ModlObject.ValueItem interpret(ModlObject modlObject, ModlParsed.ValueItem valueItemParsed) {
        if (valueItemParsed == null) {
            return null;
        }
        ModlObject.ValueItem valueItem = modlObject.new ValueItem();

        if (valueItemParsed.getValueConditional() != null) {
            valueItem.setValueConditional(interpret(modlObject, valueItemParsed.getValueConditional()));
        }
        if (valueItemParsed.getValue() != null) {
            valueItem.setValue(interpret(modlObject, valueItemParsed.getValue()));
        }
        if (valueItemParsed.getValueItems() != null) {
            for (ModlParsed.ValueItem vi : valueItemParsed.getValueItems()) {
                valueItem.addValueItem(interpret(modlObject, vi));
            }
        }

        return valueItem;
    }

    private static ModlObject.False interpret(ModlObject modlObject, ModlParsed.False falseVal) {
        if (falseVal != null) {
            ModlObject.False f = modlObject.new False();
            return f;
        }
        return null;
    }

    private static ModlObject.Null interpret(ModlObject modlObject, ModlParsed.Null val) {
        if (val != null) {
            ModlObject.Null n = modlObject.new Null();
            return n;
        }
        return null;
    }

    private static ModlObject.True interpret(ModlObject modlObject, ModlParsed.True trueVal) {
        if (trueVal != null) {
            ModlObject.True t = modlObject.new True();
            return t;
        }
        return null;
    }

    private static ModlObject.Number interpret(ModlObject modlObject, ModlParsed.Number number) {
        if (number != null) {
            // TODO Should be number
            /*
            These simple rules seems solid:
If all chars are numbers Then
   int
ElseIf all chars are numbers except one and that char is a dot in position two or higher then it’s a float
Else it’s a string.
End
             */
            ModlObject.Number n = modlObject.new Number(number.string);
            return n;
        }
        return null;
    }

    private static ModlObject.String interpret(ModlObject modlObject, ModlParsed.String string) {
        if (string != null) {
            ModlObject.String str = modlObject.new String(string.string);
            return str;
        }
        return null;
    }

    private static ModlObject.Quoted interpret(ModlObject modlObject, ModlParsed.Quoted quoted) {
        if (quoted != null) {
            String s = quoted.string;
            if (s.startsWith("\"") && s.endsWith("\"")) {
                s = s.substring(1, s.length() - 1);
            }
            ModlObject.Quoted string = modlObject.new Quoted(s);
            return string;
        }
        return null;
    }

    private static ModlObject.ValueConditional interpret(ModlObject modlObject, ModlParsed.ValueConditional conditionalParsed) {
        if (conditionalParsed == null) {
            return null;
        }
        ModlObject.ValueConditional conditional = modlObject.new ValueConditional();
        for (Map.Entry<ModlParsed.ConditionTest, ModlParsed.ValueConditionalReturn> conditionalParsedEntry : conditionalParsed.getValueConditionalReturns().entrySet()) {
            conditional.addConditional(interpret(modlObject, conditionalParsedEntry.getKey()),
                    interpret(modlObject, conditionalParsedEntry.getValue()));
        }
        return conditional;
    }

    private static ModlObject.ValueConditionalReturn interpret(ModlObject modlObject, ModlParsed.ValueConditionalReturn conditionalReturnParsed) {
        if (conditionalReturnParsed == null) {
            return null;
        }
        ModlObject.ValueConditionalReturn conditionalReturn = modlObject.new ValueConditionalReturn();
        if (conditionalReturnParsed.getValueItems() != null) {
            for (ModlParsed.ValueItem valueParsed : conditionalReturnParsed.getValueItems()) {
                ModlObject.ValueItem valueItem = interpret(modlObject, valueParsed);
                conditionalReturn.addValueItem(valueItem);
            }
        }
        return conditionalReturn;
    }

    private static ModlObject.ArrayConditional interpret(ModlObject modlObject, ModlParsed.ArrayConditional conditionalParsed) {
        if (conditionalParsed == null) {
            return null;
        }
        ModlObject.ArrayConditional conditional = modlObject.new ArrayConditional();
        if (conditionalParsed.getArrayConditionalReturns() != null) {
            for (Map.Entry<ModlParsed.ConditionTest, ModlParsed.ArrayConditionalReturn> conditionalParsedEntry : conditionalParsed.getArrayConditionalReturns().entrySet()) {
                conditional.addConditional(interpret(modlObject, conditionalParsedEntry.getKey()),
                        interpret(modlObject, conditionalParsedEntry.getValue()));
            }
        }
        return conditional;
    }

    private static ModlObject.ArrayConditionalReturn interpret(ModlObject modlObject, ModlParsed.ArrayConditionalReturn conditionalReturnParsed) {
        if (conditionalReturnParsed == null) {
            return null;
        }
        ModlObject.ArrayConditionalReturn conditionalReturn = modlObject.new ArrayConditionalReturn();
        if (conditionalReturnParsed.getArrayItems() != null) {
            for (ModlParsed.ArrayItem valueParsed : conditionalReturnParsed.getArrayItems()) {
                ModlObject.ArrayItem arrayItem = interpret(modlObject, valueParsed);
                conditionalReturn.addArrayItem(arrayItem);
            }
        }
        return conditionalReturn;
    }

    private static ModlObject.MapConditional interpret(ModlObject modlObject, ModlParsed.MapConditional conditionalParsed) {
        if (conditionalParsed == null) {
            return null;
        }
        ModlObject.MapConditional conditional = modlObject.new MapConditional();
        for (Map.Entry<ModlParsed.ConditionTest, ModlParsed.MapConditionalReturn> conditionalParsedEntry : conditionalParsed.getMapConditionals().entrySet()) {
            conditional.addConditional(interpret(modlObject, conditionalParsedEntry.getKey()),
                    interpret(modlObject, conditionalParsedEntry.getValue()));
        }
        return conditional;
    }

    private static ModlObject.MapConditionalReturn interpret(ModlObject modlObject, ModlParsed.MapConditionalReturn conditionalReturnParsed) {
        if (conditionalReturnParsed == null) {
            return null;
        }
        ModlObject.MapConditionalReturn conditionalReturn = modlObject.new MapConditionalReturn();
        if (conditionalReturnParsed.getMapItems() != null) {
            for (ModlParsed.MapItem valueParsed : conditionalReturnParsed.getMapItems()) {
                ModlObject.MapItem mapItem = interpret(modlObject, valueParsed);
                conditionalReturn.addMapItem(mapItem);
            }
        }
        return conditionalReturn;
    }

    private static ModlObject.TopLevelConditional interpret(ModlObject modlObject, ModlParsed.TopLevelConditional conditionalParsed) {
        if (conditionalParsed == null) {
            return null;
        }
        ModlObject.TopLevelConditional conditional = modlObject.new TopLevelConditional();
        for (Map.Entry<ModlParsed.ConditionTest, ModlParsed.TopLevelConditionalReturn> conditionalParsedEntry : conditionalParsed.getTopLevelConditionalReturns().entrySet()) {
            conditional.addConditional(interpret(modlObject, conditionalParsedEntry.getKey()),
                    interpret(modlObject, conditionalParsedEntry.getValue()));
        }
        return conditional;
    }

    private static ModlObject.TopLevelConditionalReturn interpret(ModlObject modlObject, ModlParsed.TopLevelConditionalReturn conditionalReturnParsed) {
        if (conditionalReturnParsed == null) {
            return null;
        }
        ModlObject.TopLevelConditionalReturn conditionalReturn = modlObject.new TopLevelConditionalReturn();
        if (conditionalReturnParsed.getStructures() != null) {
            for (ModlParsed.Structure valueParsed : conditionalReturnParsed.getStructures()) {
                ModlObject.Structure structure = interpret(modlObject, valueParsed);
                conditionalReturn.addStructure(structure);
            }
        }
        return conditionalReturn;
    }
}
