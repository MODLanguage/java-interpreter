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

import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.tuple.ImmutablePair;
import uk.modl.parser.antlr.MODLParser;
import uk.modl.parser.antlr.MODLParserBaseListener;

import java.util.*;

public class ModlParsed extends MODLParserBaseListener {

    // This class holds the ModlParsed information.
    // It needs to implement inner classes for handling each rule.
    // See http://jakubdziworski.github.io/java/2016/04/01/antlr_visitor_vs_listener.html

    private interface ValueObject {}

    List<Structure> structures = new LinkedList<>();

    @Override
    public void enterModl(MODLParser.ModlContext ctx) {
        ctx.structure().forEach(str -> {
            Structure structure = new Structure();
            str.enterRule(structure);
            structures.add(structure);
        });
    }


    public List<Structure> getStructures() {
        return structures;
    }

    public class Structure extends MODLParserBaseListener implements ValueObject {
        Array array;
        Pair pair;
        TopLevelConditional topLevelConditional;
        Map map;


        public void enterStructure(MODLParser.StructureContext ctx) {
            if (ctx.pair() != null) {
                pair = new Pair();
                ctx.pair().enterRule(pair);
            } else if (ctx.top_level_conditional() != null) {
                topLevelConditional = new TopLevelConditional();
                ctx.top_level_conditional().enterRule(topLevelConditional);
            } else if (ctx.map() != null) {
                map = new Map();
                ctx.map().enterRule(map);
            } else if (ctx.array() != null) {
                array = new Array();
                ctx.array().enterRule(array);
            }
        }

        public Array getArray() {
            return array;
        }

        public Pair getPair() {
            return pair;
        }

        public TopLevelConditional getTopLevelConditional() {
            return topLevelConditional;
        }

        public Map getMap() {
            return map;
        }
    }

    public class Map extends MODLParserBaseListener implements ValueObject {
        List<MapItem> mapItems;

        @Override
        public void enterMap(MODLParser.MapContext ctx) {
            if (ctx.map_item() != null) {
                mapItems = new LinkedList<>();
                ctx.map_item().forEach(mi -> {
                    MapItem mapItem = new MapItem();
                    mi.enterRule(mapItem);
                    mapItems.add(mapItem);
                });
            }
        }
        public List<MapItem> getMapItems() {
            return mapItems;
        }
    }

    public class MapItem extends MODLParserBaseListener implements ValueObject {
        Pair pair;
        MapConditional mapConditional;

        @Override
        public void enterMap_item(MODLParser.Map_itemContext ctx) {
            if (ctx.pair() != null) {
                pair = new Pair();
                ctx.pair().enterRule(pair);
            }
            if (ctx.map_conditional() != null) {
                mapConditional = new MapConditional();
                ctx.map_conditional().enterRule(mapConditional);
            }
            }

        public Pair getPair() {
            return pair;
        }

        public MapConditional getMapConditional() {
            return mapConditional;
        }
    }

    public class Value extends MODLParserBaseListener implements ValueObject {
        //        ValueObject valueObject; // can be one of Pair or Conditional
        Map map;
        Array array;
        Pair pair;
        Quoted quoted;
        Number number;
        True trueVal;
        False falseVal;
        Null nullVal;
        String string;

        @Override
        public void enterValue(MODLParser.ValueContext ctx) {
            if (ctx.NUMBER() != null) {
                number = new Number(ctx.NUMBER().getText());
            } else if (ctx.map() != null) {
                map = new Map();
                ctx.map().enterRule(map);
            } else if (ctx.array() != null) {
                array = new Array();
                ctx.array().enterRule(array);
            } else if (ctx.pair() != null) {
                pair = new Pair();
                ctx.pair().enterRule(pair);
            } else if (ctx.STRING() != null) {
                string = new String(ctx.STRING().getText());
            } else if (ctx.QUOTED() != null) {
                quoted = new Quoted(ctx.QUOTED().getText());
            } else if (ctx.NULL() != null) {
                nullVal = new Null();
            } else if (ctx.TRUE() != null) {
                trueVal = new True();
            } else if (ctx.FALSE() != null) {
                falseVal = new False();
            }

            // ignoring comments!
        }

        public Quoted getQuoted() {
            return quoted;
        }

        public Number getNumber() {
            return number;
        }

        public True getTrueVal() {
            return trueVal;
        }

        public False getFalseVal() {
            return falseVal;
        }

        public Null getNullVal() {
            return nullVal;
        }

        public String getString() {
            return string;
        }

        public Map getMap() {
            return map;
        }

        public Array getArray() {
            return array;
        }

        public Pair getPair() {
            return pair;
        }
    }

    public class ValueItem extends MODLParserBaseListener implements ValueObject {
        Value value;
        List<ValueItem> valueItems;
        ValueConditional valueConditional;

        @Override
        public void enterValue_item(MODLParser.Value_itemContext ctx) {
            if (ctx.value_conditional() != null) {
                valueConditional = new ValueConditional();
                ctx.value_conditional().enterRule(valueConditional);
            }
            if (ctx.value() != null) {
                value = new Value();
                ctx.value().enterRule(value);
            }
            if (ctx.value_item() != null) {
                valueItems =  new LinkedList<>();
                ctx.value_item().forEach(val -> {
                    ValueItem valueItem = new ValueItem();
                    val.enterRule(valueItem);
                    valueItems.add(valueItem);
                });
            }
        }

        public Value getValue() {
            return value;
        }

        public ValueConditional getValueConditional() {
            return valueConditional;
        }

        public List<ValueItem> getValueItems() {
            return valueItems;
        }
    }


    public class Pair extends MODLParserBaseListener implements ValueObject {
        java.lang.String key;
        Map map;
        Array array;
        List<ValueItem> valueItems;

        @Override
        public void enterPair(MODLParser.PairContext ctx) {
            if (ctx.array() != null) {
                if (ctx.STRING() != null) {
                    key = ctx.STRING().toString();
                }
                array = new Array();
                ctx.array().enterRule(array);
            } else if (ctx.map() != null) {
                if (ctx.STRING() != null) {
                    key = ctx.STRING().toString();
                }
                map = new Map();
                ctx.map().enterRule(map);
            } else {
                if (ctx.STRING() != null) {
                    key = ctx.STRING().toString();
                } else if (ctx.QUOTED() != null) {
                    key = ctx.QUOTED().toString().replaceAll("\"", "");
                }
                valueItems =  new LinkedList<>();
                ctx.value_item().forEach(val -> {
                    ValueItem valueItem = new ValueItem();
                    val.enterRule(valueItem);
                    valueItems.add(valueItem);
                });
            }
        }

        public List<ValueItem> getValueItems() {
            return valueItems;
        }

        public java.lang.String getKey() {
            return key;
        }

        public Map getMap() {
            return map;
        }

        public Array getArray() {
            return array;
        }

    }

    public class String implements ValueObject {
        public java.lang.String string;

        public String(java.lang.String string) {
            this.string = string;
        }
    }

    public class Number implements ValueObject {
        public java.lang.String string;

        public Number(java.lang.String string) {
            this.string = string;
        }
    }

    public class Quoted implements ValueObject {
        public java.lang.String string;

        public Quoted(java.lang.String string) {
            this.string = string;
        }
    }

    public class ConditionTest extends MODLParserBaseListener implements ValueObject {
        java.util.List<org.apache.commons.lang3.tuple.ImmutablePair<SubCondition, ImmutablePair<java.lang.String, Boolean>>> subConditionList = new LinkedList<>();

        @Override
        public void enterCondition_test(MODLParser.Condition_testContext ctx) {
            if (ctx.children.size() > 0) {
                int index = 0;
                java.lang.String lastOperator = null;
                boolean shouldNegate = false;
                for (ParseTree child : ctx.children) {
                    if (child instanceof MODLParser.Condition_groupContext) {
                        ConditionGroup conditionGroup = new ConditionGroup();
                        ((MODLParser.Condition_groupContext)child).enterRule(conditionGroup);
                        subConditionList.add(new ImmutablePair<>(conditionGroup, new ImmutablePair<>(lastOperator, shouldNegate)));
                        lastOperator = null;
                        shouldNegate = false;
                    } else  if (child instanceof MODLParser.ConditionContext) {
                        Condition condition = new Condition();
                        ((MODLParser.ConditionContext)child).enterRule(condition);
                        subConditionList.add(new ImmutablePair<>(condition, new ImmutablePair<>(lastOperator, shouldNegate)));
                        lastOperator = null;
                        shouldNegate = false;
                    } else {
                        if (child.getText().equals("!")) {
                            shouldNegate = true;
                        } else {
                            lastOperator = child.getText();
                        }
                    }
                    index++;

                }

            }
        }
    }

    public interface SubCondition extends ValueObject {}

    public class ConditionGroup extends MODLParserBaseListener implements SubCondition {
        java.util.List<org.apache.commons.lang3.tuple.ImmutablePair<ConditionTest, java.lang.String>> conditionsTestList = new LinkedList<>();

        @Override
        public void enterCondition_group(MODLParser.Condition_groupContext ctx) {
            if (ctx.children.size() > 0) {
                int index = 0;
                java.lang.String lastOperator = null;
                for (ParseTree child : ctx.children) {
                    if (child instanceof MODLParser.Condition_testContext) {
                        ConditionTest conditionTest = new ConditionTest();
                        ((MODLParser.Condition_testContext) child).enterRule(conditionTest);
                        conditionsTestList.add(new org.apache.commons.lang3.tuple.ImmutablePair<>(conditionTest, lastOperator));
                        lastOperator = null;
                    } else {
                        if (child.getText().equals("{") || child.getText().equals("}")) {
                        } else {
                            lastOperator = child.getText();
                        }
                    }
                    index++;

                }

            }
        }
    }

    public class Condition extends MODLParserBaseListener implements  SubCondition {
        java.lang.String key;
        java.lang.String operator;
        List<Value> values = new LinkedList<>();

        // TODO Should we just store the condition String here?
        // TODO We can then parse it in the "evaluates" method later

        @Override
        public void enterCondition(MODLParser.ConditionContext ctx) {
            if (ctx.STRING() != null) {
                key = ctx.STRING().getText();
            }
            if (ctx.operator() != null) {
                operator = ctx.operator().getText();
            }
            for (MODLParser.ValueContext v : ctx.value()) {
                Value value = new Value();
                v.enterRule(value);
                values.add(value);
            }
        }
    }

    public class MapConditionalReturn extends MODLParserBaseListener implements ValueObject {
        List<MapItem> mapItems = new LinkedList<>();;

        @Override
        public void enterMap_conditional_return(MODLParser.Map_conditional_returnContext ctx) {
            if (ctx.map_item().size() > 0) {
                ctx.map_item().forEach(mi -> {
                    MapItem mapItem = new MapItem();
                    mi.enterRule(mapItem);
                    mapItems.add(mapItem);
                });
            }
        }

        public List<MapItem> getMapItems() {
            return mapItems;
        }
    }

    public class MapConditional extends MODLParserBaseListener implements ValueObject {
        java.util.Map<ConditionTest, MapConditionalReturn> mapConditionals;

        @Override
        public void enterMap_conditional(MODLParser.Map_conditionalContext ctx) {
            mapConditionals = new LinkedHashMap<>();
            for (int i = 0; i < ctx.condition_test().size(); i++) {
                ConditionTest conditionTest = new ConditionTest();
                ctx.condition_test(i).enterRule(conditionTest);

                MapConditionalReturn conditionalReturn = new MapConditionalReturn();
                ctx.map_conditional_return(i).enterRule(conditionalReturn);
                mapConditionals.put(conditionTest, conditionalReturn);
            }
            if (ctx.map_conditional_return().size() > ctx.condition_test().size()) {
//                ConditionTest conditionTest = new ConditionTest(new String ("else"));
                ConditionTest conditionTest = new ConditionTest();
                MapConditionalReturn conditionalReturn = new MapConditionalReturn();
                ctx.map_conditional_return(ctx.map_conditional_return().size() - 1).enterRule(conditionalReturn);
                mapConditionals.put(conditionTest, conditionalReturn);
            }
        }

        public java.util.Map<ConditionTest, MapConditionalReturn> getMapConditionals() {
            return mapConditionals;
        }
    }

    public class TopLevelConditionalReturn extends MODLParserBaseListener implements ValueObject {
        List<Structure> structures = new LinkedList<>();;

        @Override
        public void enterTop_level_conditional_return(MODLParser.Top_level_conditional_returnContext ctx) {
            if (ctx.structure().size() > 0) {
                ctx.structure().forEach(str -> {
                    Structure structure = new Structure();
                    str.enterRule(structure);
                    this.structures.add(structure);
                });
            }
        }

        public List<Structure> getStructures() {
            return structures;
        }
    }

    public class TopLevelConditional extends MODLParserBaseListener implements ValueObject {
        java.util.Map<ConditionTest, TopLevelConditionalReturn> topLevelConditionalReturns;

        @Override
        public void enterTop_level_conditional(MODLParser.Top_level_conditionalContext ctx) {
            topLevelConditionalReturns = new LinkedHashMap<>();
            for (int i = 0; i < ctx.condition_test().size(); i++) {
                ConditionTest conditionTest = new ConditionTest();
                ctx.condition_test(i).enterRule(conditionTest);

                TopLevelConditionalReturn conditionalReturn = new TopLevelConditionalReturn();
                ctx.top_level_conditional_return(i).enterRule(conditionalReturn);
                topLevelConditionalReturns.put(conditionTest, conditionalReturn);
            }
            if (ctx.top_level_conditional_return().size() > ctx.condition_test().size()) {
//                ConditionTest conditionTest = new ConditionTest(new String ("else"));
                // TODO Add True here?!
                ConditionTest conditionTest = new ConditionTest();
                TopLevelConditionalReturn conditionalReturn = new TopLevelConditionalReturn();
                ctx.top_level_conditional_return(ctx.top_level_conditional_return().size() - 1).enterRule(conditionalReturn);
                topLevelConditionalReturns.put(conditionTest, conditionalReturn);
            }
        }

        public java.util.Map<ConditionTest, TopLevelConditionalReturn> getTopLevelConditionalReturns() {
            return topLevelConditionalReturns;
        }
    }

    public class ArrayConditionalReturn extends MODLParserBaseListener implements ValueObject {
        List<ArrayItem> arrayItems = new LinkedList<>();;

        @Override
        public void enterArray_conditional_return(MODLParser.Array_conditional_returnContext ctx) {
            if (ctx.array_item().size() > 0) {
                ctx.array_item().forEach(str -> {
                    ArrayItem arrayItem = new ArrayItem();
                    str.enterRule(arrayItem);
                    this.arrayItems.add(arrayItem);
                });
            }
        }

        public List<ArrayItem> getArrayItems() {
            return arrayItems;
        }
    }

    public class ArrayConditional extends MODLParserBaseListener implements ValueObject {
        java.util.Map<ConditionTest, ArrayConditionalReturn> arrayConditionalReturns;

        @Override
        public void enterArray_conditional(MODLParser.Array_conditionalContext ctx) {
            arrayConditionalReturns = new LinkedHashMap<>();
            for (int i = 0; i < ctx.condition_test().size(); i++) {
                ConditionTest conditionTest = new ConditionTest();
                ctx.condition_test(i).enterRule(conditionTest);

                ArrayConditionalReturn conditionalReturn = new ArrayConditionalReturn();
                ctx.array_conditional_return(i).enterRule(conditionalReturn);
                arrayConditionalReturns.put(conditionTest, conditionalReturn);
            }
            if (ctx.array_conditional_return().size() > ctx.condition_test().size()) {
//                ConditionTest conditionTest = new ConditionTest(new String ("else"));
                ConditionTest conditionTest = new ConditionTest();
                ArrayConditionalReturn conditionalReturn = new ArrayConditionalReturn();
                ctx.array_conditional_return(ctx.array_conditional_return().size() - 1).enterRule(conditionalReturn);
                arrayConditionalReturns.put(conditionTest, conditionalReturn);
            }
        }

        public java.util.Map<ConditionTest, ArrayConditionalReturn> getArrayConditionalReturns() {
            return arrayConditionalReturns;
        }
    }

    public class ValueConditionalReturn extends MODLParserBaseListener implements ValueObject {
        List<ValueItem> valueItems = new LinkedList<>();;

        @Override
        public void enterValue_conditional_return(MODLParser.Value_conditional_returnContext ctx) {
            if (ctx.value_item().size() > 0) {
                ctx.value_item().forEach(str -> {
                    ValueItem valueItem = new ValueItem();
                    str.enterRule(valueItem);
                    this.valueItems.add(valueItem);
                });
            }
        }

        public List<ValueItem> getValueItems() {
            return valueItems;
        }
    }

    public class ValueConditional extends MODLParserBaseListener implements ValueObject {
        java.util.Map<ConditionTest, ValueConditionalReturn> valueConditionalReturns;

        @Override
        public void enterValue_conditional(MODLParser.Value_conditionalContext ctx) {
            valueConditionalReturns = new LinkedHashMap<>();
            for (int i = 0; i < ctx.condition_test().size(); i++) {
                ConditionTest conditionTest = new ConditionTest();
                ctx.condition_test(i).enterRule(conditionTest);

                ValueConditionalReturn conditionalReturn = new ValueConditionalReturn();
                ctx.value_conditional_return(i).enterRule(conditionalReturn);
                valueConditionalReturns.put(conditionTest, conditionalReturn);
            }
            if (ctx.value_conditional_return().size() > ctx.condition_test().size()) {
//                ConditionTest conditionTest = new ConditionTest(new String ("else"));
                ConditionTest conditionTest = new ConditionTest();
                ValueConditionalReturn conditionalReturn = new ValueConditionalReturn();
                ctx.value_conditional_return(ctx.value_conditional_return().size() - 1).enterRule(conditionalReturn);
                valueConditionalReturns.put(conditionTest, conditionalReturn);
            }
        }

        public java.util.Map<ConditionTest, ValueConditionalReturn> getValueConditionalReturns() {
            return valueConditionalReturns;
        }
    }

//    public class ConditionalReturn extends MODLBaseListener implements ValueObject {
//        List<Value> values = new LinkedList<>();;
//
//        @Override
//        public void enterConditional_return(MODLParser.Conditional_returnContext ctx) {
//            if (ctx.value().size() > 0) {
//                ctx.value().forEach(val -> {
//                    Value value = new Value();
//                    val.enterRule(value);
//                    values.add(value);
//                });
//            }
//        }
//
//        public List<Value> getValues() {
//            return values;
//        }
//
//    }
//
//    public class Conditional extends MODLBaseListener implements ValueObject {
//        java.util.Map<ConditionTest, ConditionalReturn> conditionals;
//
//        @Override
//        public void enterConditional(MODLParser.ConditionalContext ctx) {
//            conditionals = new LinkedHashMap<>();
//            for (int i = 0; i < ctx.condition_test().size(); i++) {
//                ConditionTest conditionTest = new ConditionTest();
//                ctx.condition_test(i).enterRule(conditionTest);
//
//                ConditionalReturn conditionalReturn = new ConditionalReturn();
//                ctx.conditional_return(i).enterRule(conditionalReturn);
//                conditionals.put(conditionTest, conditionalReturn);
//            }
//            if (ctx.conditional_return().size() > ctx.condition_test().size()) {
//                ConditionTest conditionTest = new ConditionTest(new String ("else"));
//                ConditionalReturn conditionalReturn = new ConditionalReturn();
//                ctx.conditional_return(ctx.conditional_return().size() - 1).enterRule(conditionalReturn);
//                conditionals.put(conditionTest, conditionalReturn);
//            }
//        }
//
//        public java.util.Map<ConditionTest, ConditionalReturn> getConditionals() {
//            return conditionals;
//        }
//    }

    public class Array extends MODLParserBaseListener implements ValueObject {
        List<ArrayItem> arrayItems = new LinkedList<>();

        @Override
        public void enterArray(MODLParser.ArrayContext ctx) {
            if (ctx.array_item().size() > 0) {
                ctx.array_item().forEach(ai -> {
                    ArrayItem arrayItem = new ArrayItem();
                    ai.enterRule(arrayItem);
                    arrayItems.add(arrayItem);
                });
            }
        }

        public List<ArrayItem> getArrayItems() {
            return arrayItems;
        }
    }

    public class ArrayItem extends MODLParserBaseListener implements ValueObject {
        Value value;
        ArrayConditional arrayConditional;

        @Override
        public void enterArray_item(MODLParser.Array_itemContext ctx) {
            if (ctx.array_conditional() != null) {
                arrayConditional = new ArrayConditional();
                ctx.array_conditional().enterRule(arrayConditional);
            }
            if (ctx.value() != null) {
                value = new Value();
                ctx.value().enterRule(value);
            }
        }

        public Value getValue() {
            return value;
        }

        public ArrayConditional getArrayConditional() {
            return arrayConditional;
        }
    }

    public class True extends MODLParserBaseListener implements ValueObject {

    }

    public class False extends MODLParserBaseListener implements ValueObject {

    }

    public class Null extends MODLParserBaseListener implements ValueObject {

    }
}
