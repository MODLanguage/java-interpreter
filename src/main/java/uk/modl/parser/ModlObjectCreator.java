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

public class ModlObjectCreator {

    public static int MODL_VERSION = 1;


//    public static String parseToJson(String input) throws IOException {
//        RawModlObject rawModlObject = processModlParsed(input);
//
//        JsonPrinter jsonPrinter = new JsonPrinter();
//        String json = jsonPrinter.printModl(rawModlObject);
//        return json;
//
//    }

    public static RawModlObject processModlParsed(String input) throws IOException {
        ModlParsed modlParsed = Parser.parse(input);

        return processModlParsed(modlParsed);
    }

    private static RawModlObject processModlParsed(ModlParsed modlParsed) {
        // Go through the ModlParsed object and transform it into a RawModlObject object, using the rules we know about and any config rules
        RawModlObject rawModlObject = new RawModlObject();


            for (ModlParsed.Structure parsedStructure : modlParsed.getStructures()) {
                RawModlObject.Structure structure = processModlParsed(rawModlObject, parsedStructure);
                if (structure.getPair() == null && structure.getArray() == null &&  structure.getTopLevelConditional() == null &&
                        structure.getMap() == null) {
                    continue;
                }
                rawModlObject.addStructure(structure);
            }

        return rawModlObject;
    }

    private static RawModlObject.Structure processModlParsed(RawModlObject rawModlObject, ModlParsed.Structure parsedStructure) {
        if (parsedStructure == null) {
            return null;
        }

        RawModlObject.Structure structure = rawModlObject.new Structure();

        structure.setMap(processModlParsed(rawModlObject, parsedStructure.getMap()));
        structure.setArray(processModlParsed(rawModlObject, parsedStructure.getArray()));
        RawModlObject.Pair pair = processModlParsed(rawModlObject, parsedStructure.getPair());
        if (pair != null) {
            structure.setPair(pair);
        }
        structure.setTopLevelConditional(processModlParsed(rawModlObject, parsedStructure.getTopLevelConditional()));

        return structure;
    }

    private static RawModlObject.Map processModlParsed(RawModlObject rawModlObject, ModlParsed.Map parsedMap) {
        if (parsedMap == null) {
            return null;
        }

        RawModlObject.Map map = rawModlObject.new Map();

        if (parsedMap.getMapItems() != null) {
            for (ModlParsed.MapItem mapItemParsed : parsedMap.getMapItems()) {
                RawModlObject.MapItem mapItem = processModlParsed(rawModlObject, mapItemParsed);
                if (mapItem != null) {
                    map.addMapItem(mapItem);
                }
            }
        }

        return map;
    }

    private static RawModlObject.MapItem processModlParsed(RawModlObject rawModlObject, ModlParsed.MapItem parsedMapItem) {
        if (parsedMapItem == null) {
            return null;
        }
        RawModlObject.MapItem mapItem = rawModlObject.new MapItem();
        if (parsedMapItem.getMapConditional() != null) {
            mapItem.setMapConditional(processModlParsed(rawModlObject, parsedMapItem.getMapConditional()));
        }
        if (parsedMapItem.getPair() != null) {
            // TODO If we get back an unnamed pair which contains a list of value items, then move the value items directly into this pair
            mapItem.setPair(processModlParsed(rawModlObject, parsedMapItem.getPair()));
        }
        return mapItem;
    }


        private static RawModlObject.Value processModlParsed(RawModlObject rawModlObject, ModlParsed.Value parsedValue) {
        if (parsedValue == null) {
            return null;
        }

        RawModlObject.Value value = rawModlObject.new Value();

        value.setPair(processModlParsed(rawModlObject, parsedValue.getPair()));
        value.setMap(processModlParsed(rawModlObject, parsedValue.getMap()));
        value.setArray(processModlParsed(rawModlObject, parsedValue.getArray()));
        value.setQuoted(processModlParsed(rawModlObject, parsedValue.getQuoted()));
        value.setNumber(processModlParsed(rawModlObject, parsedValue.getNumber()));
        value.setTrueVal(processModlParsed(rawModlObject, parsedValue.getTrueVal()));
        value.setFalseVal(processModlParsed(rawModlObject, parsedValue.getFalseVal()));
        value.setNullVal(processModlParsed(rawModlObject, parsedValue.getNullVal()));
        value.setString(processModlParsed(rawModlObject, parsedValue.getString()));

        if (parsedValue.getMap() != null && value.getMap() == null) {
            value.setMap(rawModlObject.new Map());
        }
        if (parsedValue.getArray() != null && value.getArray() == null) {
            value.setArray(rawModlObject.new Array());
        }

        return value;
    }

    private static RawModlObject.ConditionTest processModlParsed(RawModlObject rawModlObject, ModlParsed.ConditionTest conditionTestParsed) {
        if (conditionTestParsed == null) {
            return null;
        }

        RawModlObject.ConditionTest conditionTest = rawModlObject.new ConditionTest();
        // SubConditions are either ConditionGroups or Conditions
        for (ImmutablePair<ModlParsed.SubCondition, ImmutablePair<String, Boolean>> subConditionPair : conditionTestParsed.subConditionList) {
            ModlParsed.SubCondition subCondition = subConditionPair.getLeft();
            ImmutablePair<java.lang.String, Boolean> operatorPair = subConditionPair.getRight();
            String operator = operatorPair.getLeft();
            Boolean shouldNegate = operatorPair.getRight();
            if (subCondition instanceof ModlParsed.ConditionGroup) {
                RawModlObject.ConditionGroup conditionGroup = processModlParsed(rawModlObject, (ModlParsed.ConditionGroup)subCondition);
                conditionTest.addSubCondition(operator, shouldNegate, conditionGroup);
            } else if (subCondition instanceof ModlParsed.Condition) {
                RawModlObject.Condition condition = processModlParsed(rawModlObject, (ModlParsed.Condition)subCondition);
                conditionTest.addSubCondition(operator, shouldNegate, condition);
            }
        }

        return conditionTest;
    }

    private static RawModlObject.Condition processModlParsed(RawModlObject rawModlObject, ModlParsed.Condition conditionParsed) {
        if (conditionParsed == null) {
            return null;
        }

        String key = conditionParsed.key;
        String operator = conditionParsed.operator;
        List<RawModlObject.Value> values = new LinkedList<>();
        for (ModlParsed.Value valueParsed : conditionParsed.values) {
            RawModlObject.Value value = processModlParsed(rawModlObject, valueParsed);
            values.add(value);
        }
        RawModlObject.Condition condition = rawModlObject.new Condition(key, operator, values);
        return condition;
    }

    private static RawModlObject.ConditionGroup processModlParsed(RawModlObject rawModlObject, ModlParsed.ConditionGroup conditionGroupParsed) {
        if (conditionGroupParsed == null) {
            return null;
        }
        RawModlObject.ConditionGroup conditionGroup = rawModlObject.new ConditionGroup();
        for (org.apache.commons.lang3.tuple.ImmutablePair<ModlParsed.ConditionTest, java.lang.String> conditionTestPair : conditionGroupParsed.conditionsTestList) {
            ModlParsed.ConditionTest conditionTestParsed = conditionTestPair.getLeft();
            String operator = conditionTestPair.getRight();
            RawModlObject.ConditionTest conditionTest = processModlParsed(rawModlObject, conditionTestParsed);
            conditionGroup.addConditionTest(conditionTest, operator);
        }

        return conditionGroup;
    }

    private static RawModlObject.Pair processModlParsed(RawModlObject rawModlObject, ModlParsed.Pair pairParsed) {
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
        RawModlObject.Pair pair = rawModlObject.new Pair();

        pair.setKey(pairParsed.getKey());

        pair.setMap(processModlParsed(rawModlObject, pairParsed.getMap()));
        pair.setArray(processModlParsed(rawModlObject, pairParsed.getArray()));
        if (pairParsed.getValueItems() != null) {
            for (ModlParsed.ValueItem valueParsed : pairParsed.getValueItems()) {
                RawModlObject.ValueItem valueItem = processModlParsed(rawModlObject, valueParsed);
                pair.addValueItem(valueItem);
            }
        }

        return pair;
    }

    private static RawModlObject.Array processModlParsed(RawModlObject rawModlObject, ModlParsed.Array arrayParsed) {
        if (arrayParsed == null) {
            return null;
        }
        RawModlObject.Array array = rawModlObject.new Array();

        if (arrayParsed.getArrayItems() != null) {
            for (ModlParsed.ArrayItem arrayItemParsed : arrayParsed.getArrayItems()) {
                RawModlObject.ArrayItem arrayItem = processModlParsed(rawModlObject, arrayItemParsed);
                if (arrayItem != null) {
                    array.addArrayItem(arrayItem);
                }
            }
        }

        return array;
    }


    private static RawModlObject.ArrayItem processModlParsed(RawModlObject rawModlObject, ModlParsed.ArrayItem arrayItemParsed) {
        if (arrayItemParsed == null) {
            return null;
        }
        RawModlObject.ArrayItem arrayItem = rawModlObject.new ArrayItem();

        if (arrayItemParsed.getArrayConditional() != null) {
            arrayItem.setArrayConditional(processModlParsed(rawModlObject, arrayItemParsed.getArrayConditional()));
        }
        if (arrayItemParsed.getValue() != null) {
            arrayItem.setValue(processModlParsed(rawModlObject, arrayItemParsed.getValue()));
        }

        return arrayItem;
    }

    private static RawModlObject.ValueItem processModlParsed(RawModlObject rawModlObject, ModlParsed.ValueItem valueItemParsed) {
        if (valueItemParsed == null) {
            return null;
        }
        RawModlObject.ValueItem valueItem = rawModlObject.new ValueItem();

        if (valueItemParsed.getValueConditional() != null) {
            valueItem.setValueConditional(processModlParsed(rawModlObject, valueItemParsed.getValueConditional()));
        }
        if (valueItemParsed.getValue() != null) {
            valueItem.setValue(processModlParsed(rawModlObject, valueItemParsed.getValue()));
        }
        if (valueItemParsed.getValueItems() != null) {
            for (ModlParsed.ValueItem vi : valueItemParsed.getValueItems()) {
                valueItem.addValueItem(processModlParsed(rawModlObject, vi));
            }
        }

        return valueItem;
    }

    private static RawModlObject.False processModlParsed(RawModlObject rawModlObject, ModlParsed.False falseVal) {
        if (falseVal != null) {
            RawModlObject.False f = rawModlObject.new False();
            return f;
        }
        return null;
    }

    private static RawModlObject.Null processModlParsed(RawModlObject rawModlObject, ModlParsed.Null val) {
        if (val != null) {
            RawModlObject.Null n = rawModlObject.new Null();
            return n;
        }
        return null;
    }

    private static RawModlObject.True processModlParsed(RawModlObject rawModlObject, ModlParsed.True trueVal) {
        if (trueVal != null) {
            RawModlObject.True t = rawModlObject.new True();
            return t;
        }
        return null;
    }

    private static RawModlObject.Number processModlParsed(RawModlObject rawModlObject, ModlParsed.Number number) {
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
            RawModlObject.Number n = rawModlObject.new Number(number.string);
            return n;
        }
        return null;
    }

    private static RawModlObject.String processModlParsed(RawModlObject rawModlObject, ModlParsed.String string) {
        if (string != null) {
            RawModlObject.String str = rawModlObject.new String(string.string);
            return str;
        }
        return null;
    }

    private static RawModlObject.Quoted processModlParsed(RawModlObject rawModlObject, ModlParsed.Quoted quoted) {
        if (quoted != null) {
            String s = quoted.string;
            if (s.startsWith("\"") && s.endsWith("\"")) {
                s = s.substring(1, s.length() - 1);
            }
            RawModlObject.Quoted string = rawModlObject.new Quoted(s);
            return string;
        }
        return null;
    }

    private static RawModlObject.ValueConditional processModlParsed(RawModlObject rawModlObject, ModlParsed.ValueConditional conditionalParsed) {
        if (conditionalParsed == null) {
            return null;
        }
        RawModlObject.ValueConditional conditional = rawModlObject.new ValueConditional();
        for (Map.Entry<ModlParsed.ConditionTest, ModlParsed.ValueConditionalReturn> conditionalParsedEntry : conditionalParsed.getValueConditionalReturns().entrySet()) {
            conditional.addConditional(processModlParsed(rawModlObject, conditionalParsedEntry.getKey()),
                    processModlParsed(rawModlObject, conditionalParsedEntry.getValue()));
        }
        return conditional;
    }

    private static RawModlObject.ValueConditionalReturn processModlParsed(RawModlObject rawModlObject, ModlParsed.ValueConditionalReturn conditionalReturnParsed) {
        if (conditionalReturnParsed == null) {
            return null;
        }
        RawModlObject.ValueConditionalReturn conditionalReturn = rawModlObject.new ValueConditionalReturn();
        if (conditionalReturnParsed.getValueItems() != null) {
            for (ModlParsed.ValueItem valueParsed : conditionalReturnParsed.getValueItems()) {
                RawModlObject.ValueItem valueItem = processModlParsed(rawModlObject, valueParsed);
                conditionalReturn.addValueItem(valueItem);
            }
        }
        return conditionalReturn;
    }

    private static RawModlObject.ArrayConditional processModlParsed(RawModlObject rawModlObject, ModlParsed.ArrayConditional conditionalParsed) {
        if (conditionalParsed == null) {
            return null;
        }
        RawModlObject.ArrayConditional conditional = rawModlObject.new ArrayConditional();
        if (conditionalParsed.getArrayConditionalReturns() != null) {
            for (Map.Entry<ModlParsed.ConditionTest, ModlParsed.ArrayConditionalReturn> conditionalParsedEntry : conditionalParsed.getArrayConditionalReturns().entrySet()) {
                conditional.addConditional(processModlParsed(rawModlObject, conditionalParsedEntry.getKey()),
                        processModlParsed(rawModlObject, conditionalParsedEntry.getValue()));
            }
        }
        return conditional;
    }

    private static RawModlObject.ArrayConditionalReturn processModlParsed(RawModlObject rawModlObject, ModlParsed.ArrayConditionalReturn conditionalReturnParsed) {
        if (conditionalReturnParsed == null) {
            return null;
        }
        RawModlObject.ArrayConditionalReturn conditionalReturn = rawModlObject.new ArrayConditionalReturn();
        if (conditionalReturnParsed.getArrayItems() != null) {
            for (ModlParsed.ArrayItem valueParsed : conditionalReturnParsed.getArrayItems()) {
                RawModlObject.ArrayItem arrayItem = processModlParsed(rawModlObject, valueParsed);
                conditionalReturn.addArrayItem(arrayItem);
            }
        }
        return conditionalReturn;
    }

    private static RawModlObject.MapConditional processModlParsed(RawModlObject rawModlObject, ModlParsed.MapConditional conditionalParsed) {
        if (conditionalParsed == null) {
            return null;
        }
        RawModlObject.MapConditional conditional = rawModlObject.new MapConditional();
        for (Map.Entry<ModlParsed.ConditionTest, ModlParsed.MapConditionalReturn> conditionalParsedEntry : conditionalParsed.getMapConditionals().entrySet()) {
            conditional.addConditional(processModlParsed(rawModlObject, conditionalParsedEntry.getKey()),
                    processModlParsed(rawModlObject, conditionalParsedEntry.getValue()));
        }
        return conditional;
    }

    private static RawModlObject.MapConditionalReturn processModlParsed(RawModlObject rawModlObject, ModlParsed.MapConditionalReturn conditionalReturnParsed) {
        if (conditionalReturnParsed == null) {
            return null;
        }
        RawModlObject.MapConditionalReturn conditionalReturn = rawModlObject.new MapConditionalReturn();
        if (conditionalReturnParsed.getMapItems() != null) {
            for (ModlParsed.MapItem valueParsed : conditionalReturnParsed.getMapItems()) {
                RawModlObject.MapItem mapItem = processModlParsed(rawModlObject, valueParsed);
                conditionalReturn.addMapItem(mapItem);
            }
        }
        return conditionalReturn;
    }

    private static RawModlObject.TopLevelConditional processModlParsed(RawModlObject rawModlObject, ModlParsed.TopLevelConditional conditionalParsed) {
        if (conditionalParsed == null) {
            return null;
        }
        RawModlObject.TopLevelConditional conditional = rawModlObject.new TopLevelConditional();
        for (Map.Entry<ModlParsed.ConditionTest, ModlParsed.TopLevelConditionalReturn> conditionalParsedEntry : conditionalParsed.getTopLevelConditionalReturns().entrySet()) {
            conditional.addConditional(processModlParsed(rawModlObject, conditionalParsedEntry.getKey()),
                    processModlParsed(rawModlObject, conditionalParsedEntry.getValue()));
        }
        return conditional;
    }

    private static RawModlObject.TopLevelConditionalReturn processModlParsed(RawModlObject rawModlObject, ModlParsed.TopLevelConditionalReturn conditionalReturnParsed) {
        if (conditionalReturnParsed == null) {
            return null;
        }
        RawModlObject.TopLevelConditionalReturn conditionalReturn = rawModlObject.new TopLevelConditionalReturn();
        if (conditionalReturnParsed.getStructures() != null) {
            for (ModlParsed.Structure valueParsed : conditionalReturnParsed.getStructures()) {
                RawModlObject.Structure structure = processModlParsed(rawModlObject, valueParsed);
                conditionalReturn.addStructure(structure);
            }
        }
        return conditionalReturn;
    }
}
