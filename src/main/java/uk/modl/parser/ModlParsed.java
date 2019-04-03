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
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.lang3.tuple.ImmutablePair;
import uk.modl.parser.antlr.MODLLexer;
import uk.modl.parser.antlr.MODLParser;
import uk.modl.parser.antlr.MODLParserBaseListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class ModlParsed extends MODLParserBaseListener {

    // This class holds the ModlParsed information.
    // It needs to implement inner classes for handling each rule.
    // See http://jakubdziworski.github.io/java/2016/04/01/antlr_visitor_vs_listener.html

    private interface ValueObject {
    }

    List<Structure> structures = new LinkedList<>();

    @Override
    public void enterModl(MODLParser.ModlContext ctx) {
        for (MODLParser.Modl_structureContext str : ctx.modl_structure()) {
            Structure structure = new Structure();
            str.enterRule(structure);
            structures.add(structure);
        }
    }


    public List<Structure> getStructures() {
        return structures;
    }

    public class Structure extends MODLParserBaseListener implements ValueObject {
        Array array;
        Pair pair;
        TopLevelConditional topLevelConditional;
        Map map;


        public void enterModl_structure(MODLParser.Modl_structureContext ctx) {
            if (ctx.modl_pair() != null) {
                pair = new Pair();
                ctx
                        .modl_pair()
                        .enterRule(pair);
            } else if (ctx.modl_top_level_conditional() != null) {
                topLevelConditional = new TopLevelConditional();
                ctx
                        .modl_top_level_conditional()
                        .enterRule(topLevelConditional);
            } else if (ctx.modl_map() != null) {
                map = new Map();
                ctx
                        .modl_map()
                        .enterRule(map);
            } else if (ctx.modl_array() != null) {
                array = new Array();
                ctx
                        .modl_array()
                        .enterRule(array);
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
        public void enterModl_map(MODLParser.Modl_mapContext ctx) {
            if (ctx.modl_map_item() != null) {
                mapItems = new LinkedList<>();
                //                ctx.modl_map_item().forEach(mi -> {
                for (MODLParser.Modl_map_itemContext mi : ctx.modl_map_item()) {
                    MapItem mapItem = new MapItem();
                    mi.enterRule(mapItem);
                    mapItems.add(mapItem);
                }
                ;
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
        public void enterModl_map_item(MODLParser.Modl_map_itemContext ctx) {
            if (ctx.modl_pair() != null) {
                pair = new Pair();
                ctx
                        .modl_pair()
                        .enterRule(pair);
            }
            if (ctx.modl_map_conditional() != null) {
                mapConditional = new MapConditional();
                ctx
                        .modl_map_conditional()
                        .enterRule(mapConditional);
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
        NbArray nbArray;
        Pair pair;
        Quoted quoted;
        Number number;
        True trueVal;
        False falseVal;
        Null nullVal;
        String string;

        @Override
        public void enterModl_value(MODLParser.Modl_valueContext ctx) {
            if (ctx.NUMBER() != null) {
                number =
                        new Number(ctx
                                .NUMBER()
                                .getText());
            } else if (ctx.modl_map() != null) {
                map = new Map();
                ctx
                        .modl_map()
                        .enterRule(map);
            } else if (ctx.modl_nb_array() != null) {
                nbArray = new NbArray();
                ctx
                        .modl_nb_array()
                        .enterRule(nbArray);
            } else if (ctx.modl_array() != null) {
                array = new Array();
                ctx
                        .modl_array()
                        .enterRule(array);
            } else if (ctx.modl_pair() != null) {
                pair = new Pair();
                ctx
                        .modl_pair()
                        .enterRule(pair);
            } else if (ctx.STRING() != null) {
                java.lang.String textValue = additionalStringProcessing(ctx
                        .STRING()
                        .getText());
                string =
                        new String(textValue);
            } else if (ctx.QUOTED() != null) {
                java.lang.String textValue = additionalStringProcessing(ctx
                        .QUOTED()
                        .getText());
                quoted =
                        new Quoted(textValue);
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

        public NbArray getNbArray() {
            return nbArray;
        }
    }

    /**
     * Regex to find (possibly empty) strings of the form `abcd`
     *
     */
    private static final Pattern gravedPattern = Pattern.compile("^`([^`]*)`$");

    /**
     * Special handling for STRING contents.
     *
     * @param text the input java.lang.String
     * @return the processed java.lang.String
     */

    private static java.lang.String additionalStringProcessing(final java.lang.String text) {

        // Special case for a possibly empty graved string ``
//        if (text != null) {
//            final Matcher matcher = gravedPattern.matcher(text);
//            if (matcher.matches()) {
//                return matcher.group(1);
//            }
//        }
        return text;
    }

    public class ArrayValueItem extends MODLParserBaseListener implements ValueObject {
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
        public void enterModl_array_value_item(MODLParser.Modl_array_value_itemContext ctx) {
            if (ctx.NUMBER() != null) {
                number =
                        new Number(ctx
                                .NUMBER()
                                .getText());
            } else if (ctx.modl_map() != null) {
                map = new Map();
                ctx
                        .modl_map()
                        .enterRule(map);
            } else if (ctx.modl_array() != null) {
                array = new Array();
                ctx
                        .modl_array()
                        .enterRule(array);
            } else if (ctx.modl_pair() != null) {
                pair = new Pair();
                ctx
                        .modl_pair()
                        .enterRule(pair);
            } else if (ctx.STRING() != null) {
                java.lang.String textValue = additionalStringProcessing(ctx
                        .STRING()
                        .getText());
                string =
                        new String(textValue);
            } else if (ctx.QUOTED() != null) {
                java.lang.String textValue = additionalStringProcessing(ctx
                        .QUOTED()
                        .getText());
                quoted =
                        new Quoted(textValue);
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
        ValueConditional valueConditional;

        @Override
        public void enterModl_value_item(MODLParser.Modl_value_itemContext ctx) {
            if (ctx.modl_value_conditional() != null) {
                valueConditional = new ValueConditional();
                ctx
                        .modl_value_conditional()
                        .enterRule(valueConditional);
            }
            if (ctx.modl_value() != null) {
                value = new Value();
                ctx
                        .modl_value()
                        .enterRule(value);
            }
        }

        public Value getValue() {
            return value;
        }

        public ValueConditional getValueConditional() {
            return valueConditional;
        }

    }


    public class Pair extends MODLParserBaseListener implements ValueObject {
        java.lang.String key;
        Map map;
        Array array;
        ValueItem valueItem;

        @Override
        public void enterModl_pair(MODLParser.Modl_pairContext ctx) {
            if (ctx.STRING() != null) {
                key =
                        ctx
                                .STRING()
                                .toString();
            }
            if (ctx.QUOTED() != null) {
                key =
                        ctx
                                .QUOTED()
                                .toString();
                key = key.substring(1, key.length() - 1);
            }
            if (ctx.modl_array() != null) {
                array = new Array();
                ctx
                        .modl_array()
                        .enterRule(array);
            } else if (ctx.modl_map() != null) {
                map = new Map();
                ctx
                        .modl_map()
                        .enterRule(map);
            } else if (ctx.modl_value_item() != null) {
                valueItem = new ValueItem();
                ctx
                        .modl_value_item()
                        .enterRule(valueItem);
            }
        }

        public ValueItem getValueItem() {
            return valueItem;
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
        java.util.List<org.apache.commons.lang3.tuple.ImmutablePair<SubCondition, ImmutablePair<java.lang.String, Boolean>>>
                subConditionList =
                new LinkedList<>();

        @Override
        public void enterModl_condition_test(MODLParser.Modl_condition_testContext ctx) {
            if (ctx.children.size() > 0) {
                java.lang.String lastOperator = null;
                boolean shouldNegate = false;
                for (ParseTree child : ctx.children) {
                    if (child instanceof MODLParser.Modl_condition_groupContext) {
                        ConditionGroup conditionGroup = new ConditionGroup();
                        ((MODLParser.Modl_condition_groupContext) child).enterRule(conditionGroup);
                        subConditionList.add(new ImmutablePair<>((SubCondition) conditionGroup,
                                new ImmutablePair<>(lastOperator, shouldNegate)));
                        lastOperator = null;
                        shouldNegate = false;
                    } else if (child instanceof MODLParser.Modl_conditionContext) {
                        Condition condition = new Condition();
                        ((MODLParser.Modl_conditionContext) child).enterRule(condition);
                        subConditionList.add(new ImmutablePair<>((SubCondition) condition,
                                new ImmutablePair<>(lastOperator, shouldNegate)));
                        lastOperator = null;
                        shouldNegate = false;
                    } else {
                        if (child
                                .getText()
                                .equals("!")) {
                            shouldNegate = true;
                        } else {
                            lastOperator = child.getText();
                        }
                    }

                }

            }
        }
    }

    public interface SubCondition extends ValueObject {
    }

    public class ConditionGroup extends MODLParserBaseListener implements SubCondition {
        java.util.List<org.apache.commons.lang3.tuple.ImmutablePair<ConditionTest, java.lang.String>>
                conditionsTestList =
                new LinkedList<>();

        @Override
        public void enterModl_condition_group(MODLParser.Modl_condition_groupContext ctx) {
            if (ctx.children.size() > 0) {
                java.lang.String lastOperator = null;
                for (ParseTree child : ctx.children) {
                    if (child instanceof MODLParser.Modl_condition_testContext) {
                        ConditionTest conditionTest = new ConditionTest();
                        ((MODLParser.Modl_condition_testContext) child).enterRule(conditionTest);
                        conditionsTestList.add(new org.apache.commons.lang3.tuple.ImmutablePair<>(conditionTest,
                                lastOperator));
                        lastOperator = null;
                    } else {
                        if (child
                                .getText()
                                .equals("{") || child
                                .getText()
                                .equals("}")) {
                        } else {
                            lastOperator = child.getText();
                        }
                    }

                }

            }
        }
    }

    public class Condition extends MODLParserBaseListener implements SubCondition {
        java.lang.String key;
        java.lang.String operator;
        List<Value> values = new LinkedList<>();

        @Override
        public void enterModl_condition(MODLParser.Modl_conditionContext ctx) {
            if (ctx.STRING() != null) {
                key =
                        ctx
                                .STRING()
                                .getText();
            }
            if (ctx.modl_operator() != null) {
                operator =
                        ctx
                                .modl_operator()
                                .getText();
            }
            for (MODLParser.Modl_valueContext v : ctx.modl_value()) {
                Value value = new Value();
                v.enterRule(value);
                values.add(value);
            }
        }
    }

    public class MapConditionalReturn extends MODLParserBaseListener implements ValueObject {
        List<MapItem> mapItems = new LinkedList<>();
        ;

        @Override
        public void enterModl_map_conditional_return(MODLParser.Modl_map_conditional_returnContext ctx) {
            if (ctx
                    .modl_map_item()
                    .size() > 0) {
                for (MODLParser.Modl_map_itemContext mi : ctx.modl_map_item()) {
                    MapItem mapItem = new MapItem();
                    mi.enterRule(mapItem);
                    mapItems.add(mapItem);
                }
                ;
            }
        }

        public List<MapItem> getMapItems() {
            return mapItems;
        }
    }

    public class MapConditional extends MODLParserBaseListener implements ValueObject {
        java.util.Map<ConditionTest, MapConditionalReturn> mapConditionals;

        @Override
        public void enterModl_map_conditional(MODLParser.Modl_map_conditionalContext ctx) {
            mapConditionals = new LinkedHashMap<>();
            for (int i = 0;
                 i < ctx
                         .modl_condition_test()
                         .size();
                 i++) {
                ConditionTest conditionTest = new ConditionTest();
                ctx
                        .modl_condition_test(i)
                        .enterRule(conditionTest);

                MapConditionalReturn conditionalReturn = new MapConditionalReturn();
                ctx
                        .modl_map_conditional_return(i)
                        .enterRule(conditionalReturn);
                mapConditionals.put(conditionTest, conditionalReturn);
            }
            if (ctx
                    .modl_map_conditional_return()
                    .size() > ctx
                    .modl_condition_test()
                    .size()) {
                ConditionTest conditionTest = new ConditionTest();
                MapConditionalReturn conditionalReturn = new MapConditionalReturn();
                ctx
                        .modl_map_conditional_return(ctx
                                .modl_map_conditional_return()
                                .size() - 1)
                        .enterRule(conditionalReturn);
                mapConditionals.put(conditionTest, conditionalReturn);
            }
        }

        public java.util.Map<ConditionTest, MapConditionalReturn> getMapConditionals() {
            return mapConditionals;
        }
    }

    public class TopLevelConditionalReturn extends MODLParserBaseListener implements ValueObject {
        List<Structure> structures = new LinkedList<>();
        ;

        @Override
        public void enterModl_top_level_conditional_return(MODLParser.Modl_top_level_conditional_returnContext ctx) {
            if (ctx
                    .modl_structure()
                    .size() > 0) {
                //                ctx.modl_structure().forEach(str -> {
                for (MODLParser.Modl_structureContext str : ctx.modl_structure()) {
                    Structure structure = new Structure();
                    str.enterRule(structure);
                    this.structures.add(structure);
                }
                ;
            }
        }

        public List<Structure> getStructures() {
            return structures;
        }
    }

    public class TopLevelConditional extends MODLParserBaseListener implements ValueObject {
        java.util.Map<ConditionTest, TopLevelConditionalReturn> topLevelConditionalReturns;

        @Override
        public void enterModl_top_level_conditional(MODLParser.Modl_top_level_conditionalContext ctx) {
            topLevelConditionalReturns = new LinkedHashMap<>();
            for (int i = 0;
                 i < ctx
                         .modl_condition_test()
                         .size();
                 i++) {
                ConditionTest conditionTest = new ConditionTest();
                ctx
                        .modl_condition_test(i)
                        .enterRule(conditionTest);

                TopLevelConditionalReturn conditionalReturn = new TopLevelConditionalReturn();
                ctx
                        .modl_top_level_conditional_return(i)
                        .enterRule(conditionalReturn);
                topLevelConditionalReturns.put(conditionTest, conditionalReturn);
            }
            if (ctx
                    .modl_top_level_conditional_return()
                    .size() > ctx
                    .modl_condition_test()
                    .size()) {
                ConditionTest conditionTest = new ConditionTest();
                TopLevelConditionalReturn conditionalReturn = new TopLevelConditionalReturn();
                ctx
                        .modl_top_level_conditional_return(ctx
                                .modl_top_level_conditional_return()
                                .size() - 1)
                        .enterRule(conditionalReturn);
                topLevelConditionalReturns.put(conditionTest, conditionalReturn);
            }
        }

        public java.util.Map<ConditionTest, TopLevelConditionalReturn> getTopLevelConditionalReturns() {
            return topLevelConditionalReturns;
        }
    }

    public class ArrayConditionalReturn extends MODLParserBaseListener implements ValueObject {
        List<ArrayItem> arrayItems = new LinkedList<>();
        ;

        @Override
        public void enterModl_array_conditional_return(MODLParser.Modl_array_conditional_returnContext ctx) {
            if (ctx
                    .modl_array_item()
                    .size() > 0) {
                //                ctx.modl_array_item().forEach(ai -> {
                for (MODLParser.Modl_array_itemContext ai : ctx.modl_array_item()) {
                    ArrayItem arrayItem = new ArrayItem();
                    ai.enterRule(arrayItem);
                    this.arrayItems.add(arrayItem);
                }
                ;
            }
        }

        public List<ArrayItem> getArrayItems() {
            return arrayItems;
        }
    }

    public class ArrayConditional extends MODLParserBaseListener implements ValueObject {
        java.util.Map<ConditionTest, ArrayConditionalReturn> arrayConditionalReturns;

        @Override
        public void enterModl_array_conditional(MODLParser.Modl_array_conditionalContext ctx) {
            arrayConditionalReturns = new LinkedHashMap<>();
            for (int i = 0;
                 i < ctx
                         .modl_condition_test()
                         .size();
                 i++) {
                ConditionTest conditionTest = new ConditionTest();
                ctx
                        .modl_condition_test(i)
                        .enterRule(conditionTest);

                ArrayConditionalReturn conditionalReturn = new ArrayConditionalReturn();
                ctx
                        .modl_array_conditional_return(i)
                        .enterRule(conditionalReturn);
                arrayConditionalReturns.put(conditionTest, conditionalReturn);
            }
            if (ctx
                    .modl_array_conditional_return()
                    .size() > ctx
                    .modl_condition_test()
                    .size()) {
                ConditionTest conditionTest = new ConditionTest();
                ArrayConditionalReturn conditionalReturn = new ArrayConditionalReturn();
                ctx
                        .modl_array_conditional_return(ctx
                                .modl_array_conditional_return()
                                .size() - 1)
                        .enterRule(conditionalReturn);
                arrayConditionalReturns.put(conditionTest, conditionalReturn);
            }
        }

        public java.util.Map<ConditionTest, ArrayConditionalReturn> getArrayConditionalReturns() {
            return arrayConditionalReturns;
        }
    }

    public class ValueConditionalReturn extends MODLParserBaseListener implements ValueObject {
        List<ValueItem> valueItems = new LinkedList<>();
        ;

        @Override
        public void enterModl_value_conditional_return(MODLParser.Modl_value_conditional_returnContext ctx) {
            if (ctx
                    .modl_value_item()
                    .size() > 0) {
                //                ctx.modl_value_item().forEach(vi -> {
                for (MODLParser.Modl_value_itemContext vi : ctx.modl_value_item()) {
                    ValueItem valueItem = new ValueItem();
                    vi.enterRule(valueItem);
                    this.valueItems.add(valueItem);
                }
                ;
            }
        }

        public List<ValueItem> getValueItems() {
            return valueItems;
        }
    }

    public class ValueConditional extends MODLParserBaseListener implements ValueObject {
        java.util.Map<ConditionTest, ValueConditionalReturn> valueConditionalReturns;

        @Override
        public void enterModl_value_conditional(MODLParser.Modl_value_conditionalContext ctx) {
            valueConditionalReturns = new LinkedHashMap<>();
            for (int i = 0;
                 i < ctx
                         .modl_condition_test()
                         .size();
                 i++) {
                ConditionTest conditionTest = new ConditionTest();
                ctx
                        .modl_condition_test(i)
                        .enterRule(conditionTest);

                if (ctx.modl_value_conditional_return().size() > 0) {
                    ValueConditionalReturn conditionalReturn = new ValueConditionalReturn();
                    ctx
                            .modl_value_conditional_return(i)
                            .enterRule(conditionalReturn);
                    valueConditionalReturns.put(conditionTest, conditionalReturn);
                } else {
                    valueConditionalReturns.put(conditionTest, null);
                }
            }
            if (ctx
                    .modl_value_conditional_return()
                    .size() > ctx
                    .modl_condition_test()
                    .size()) {
                ConditionTest conditionTest = new ConditionTest();
                ValueConditionalReturn conditionalReturn = new ValueConditionalReturn();
                ctx
                        .modl_value_conditional_return(ctx
                                .modl_value_conditional_return()
                                .size() - 1)
                        .enterRule(conditionalReturn);
                valueConditionalReturns.put(conditionTest, conditionalReturn);
            }
        }

        public java.util.Map<ConditionTest, ValueConditionalReturn> getValueConditionalReturns() {
            return valueConditionalReturns;
        }
    }

    public class NbArray extends MODLParserBaseListener implements AbstractArrayItem {
        List<ArrayItem> arrayItems = new LinkedList<>();

        @Override
        public void enterModl_nb_array(MODLParser.Modl_nb_arrayContext ctx) {
            int i = 0;
            Object previous = null;
            for (ParseTree pt : ctx.children) {
                if (pt instanceof MODLParser.Modl_array_itemContext) {
                    ArrayItem arrayItem = new ArrayItem();
                    ((MODLParser.Modl_array_itemContext) pt).enterRule(arrayItem);
                    arrayItems.add(i++, arrayItem);
                } else if (pt instanceof TerminalNode) {
                    if (previous != null &&
                            previous instanceof TerminalNode &&
                            pt instanceof TerminalNode) {

                        // If we get here then we have two terminal nodes in a row, so we need to output something unless
                        // the terminal symbols are newlines
                        //
                        int prevSymbol = ((TerminalNode) previous).getSymbol().getType();
                        int currentSymbol = ((TerminalNode) pt).getSymbol().getType();

                        if (prevSymbol != MODLLexer.NEWLINE && currentSymbol != MODLLexer.NEWLINE) {
                            ArrayItem arrayItem = handleEmptyArrayItem();

                            arrayItems.add(i++, arrayItem);
                        }
                    }
                }
                previous = pt;
            }

        }

        public List<ArrayItem> getArrayItems() {
            return arrayItems;
        }
    }

    private ArrayItem handleEmptyArrayItem() {
        // Create something for the blank array item
        //
        // The problem is that we might not have any context to tell us what type we need to create
        // so this currently defaults to the null value
        //
        // TODO: Is there a way to know the type to create or is Null always acceptable?
        ArrayItem arrayItem = new ArrayItem();

        arrayItem.arrayValueItem = new ArrayValueItem();
        arrayItem.arrayValueItem.nullVal = new Null();
        return arrayItem;
    }

    public interface AbstractArrayItem extends ValueObject {

    }

    public class Array extends MODLParserBaseListener implements ValueObject {
        // We now have a list of <array_item|nbArray>
        List<AbstractArrayItem> abstractArrayItems = new ArrayList<>();

        @Override
        public void enterModl_array(MODLParser.Modl_arrayContext ctx) {
            // Create the new abstractArrayItems list first, sized to the total of array_item().size() and nbArray.size()
            abstractArrayItems = new LinkedList<>();

            int i = 0;
            Object previous = null;
            for (ParseTree pt : ctx.children) {
                if (pt instanceof MODLParser.Modl_array_itemContext) {
                    ArrayItem arrayItem = new ArrayItem();
                    ((MODLParser.Modl_array_itemContext) pt).enterRule(arrayItem);
                    abstractArrayItems.add(i++, arrayItem);
                } else if (pt instanceof MODLParser.Modl_nb_arrayContext) {
                    NbArray nbArray = new NbArray();
                    ((MODLParser.Modl_nb_arrayContext) pt).enterRule(nbArray);
                    abstractArrayItems.add(i++, nbArray);
                } else if (pt instanceof TerminalNode) {
                    if (previous != null &&
                            previous instanceof TerminalNode &&
                            pt instanceof TerminalNode) {

                        // If we get here then we have two terminal nodes in a row, so we need to output something unless
                        // the terminal symbols are newlines
                        //
                        int prevSymbol = ((TerminalNode) previous).getSymbol().getType();
                        int currentSymbol = ((TerminalNode) pt).getSymbol().getType();

                        if (prevSymbol == MODLLexer.LSBRAC && currentSymbol == MODLLexer.RSBRAC) {
                            continue; // This allows empty arrays
                        }

                        if (prevSymbol != MODLLexer.NEWLINE && currentSymbol != MODLLexer.NEWLINE) {

                            // Create something for the blank array item
                            //
                            // The problem is that we might not have any context to tell us what type we need to create
                            // so this currently defaults to the null
                            //
                            // TODO: Is there a way to know the type to create or is Null always acceptable?
                            ArrayItem arrayItem = handleEmptyArrayItem();

                            abstractArrayItems.add(i++, arrayItem);
                        }
                    }
                }
                previous = pt;
            }
        }

        public List<AbstractArrayItem> getAbstractArrayItems() {
            return abstractArrayItems;
        }
    }

    public class ArrayItem extends MODLParserBaseListener implements AbstractArrayItem {
        ArrayValueItem arrayValueItem;
        ArrayConditional arrayConditional;

        @Override
        public void enterModl_array_item(MODLParser.Modl_array_itemContext ctx) {
            if (ctx.modl_array_conditional() != null) {
                arrayConditional = new ArrayConditional();
                ctx
                        .modl_array_conditional()
                        .enterRule(arrayConditional);
            }
            if (ctx.modl_array_value_item() != null) {
                arrayValueItem = new ArrayValueItem();
                ctx
                        .modl_array_value_item()
                        .enterRule(arrayValueItem);
            }
        }

        public ArrayValueItem getArrayValueItem() {
            return arrayValueItem;
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
