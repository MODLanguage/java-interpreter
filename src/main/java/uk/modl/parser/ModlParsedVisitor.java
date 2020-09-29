/*
 * MIT License
 *
 * Copyright (c) 2020 NUM Technology Ltd
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package uk.modl.parser;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Vector;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import uk.modl.ancestry.Ancestry;
import uk.modl.ancestry.Parent;
import uk.modl.model.*;
import uk.modl.parser.antlr.MODLParser;
import uk.modl.parser.errors.InterpreterError;
import uk.modl.utils.Util;

import java.util.Arrays;

/**
 * Parser for a MODLParser.ModlContext object
 */
@Log4j2
public class ModlParsedVisitor {

    /**
     * Used to detect redefined immutable names
     */
    final java.util.Set<String> immutableNames = new java.util.TreeSet<>();

    private final Ancestry ancestry;

    /**
     * Immutable result
     */
    @Getter
    private Modl modl;

    private int inConditional = 0;

    private int version = 1;

    /**
     * Constructor
     *
     * @param ctx      a MODLParser.ModlContext generated by Antlr
     * @param ancestry an Ancestry object
     */
    public ModlParsedVisitor(final MODLParser.ModlContext ctx, final Ancestry ancestry) {
        log.trace("ModlParsedVisitor()");
        try {
            this.ancestry = ancestry;

            modl = Modl.of(ancestry, null, Vector.empty());

            val structures = Vector.ofAll(ctx.modl_structure()
                    .stream()
                    .map(ctx1 -> visitStructure(ctx1, modl)));

            // Look for a *V or *VERSION that isn't in the first position of the file.
            var i = 0;
            for (val structure : structures) {
                if (i > 0) {
                    if (structure instanceof Pair) {
                        val first = (Pair) structure;
                        @NonNull final String key = first.getKey();
                        if (key.equals("*V") || key.equals("*VERSION")) {
                            throw new RuntimeException("MODL version should be on the first line if specified.");
                        }
                    }
                }
                i++;
            }

            modl = modl.with(ancestry, structures);
        } catch (final RuntimeException e) {
            if (version > 1) {
                throw new InterpreterError("Interpreter Error: " + e.getMessage() + " - MODL Version 1 interpreter cannot process this MODL Version " + version + " file.");
            }
            throw new InterpreterError("Interpreter Error: " + e.getMessage());

        }
    }

    public void addImmutableName(final Parent parent, final String name) {

        val path = ancestry.pathTo(Vector.empty(), parent) + "/" + name;

        // Check the parent scopes
        val elements = path.split("/");
        for (int i = 1; i < elements.length; i++) {
            val parentPath = String.join("/", Arrays.copyOfRange(elements, 0, i)) + "/" + name;

            if (immutableNames.contains(parentPath)) {
                throw new RuntimeException("Already defined " + name + " as final.");
            }
        }

        if (StringUtils.isAllUpperCase(name)) {
            immutableNames.add(path);
        }
    }

    /**
     * Parse Structures
     *
     * @param ctx    the context
     * @param parent Parent Object
     * @return a Structure
     */
    private Structure visitStructure(final MODLParser.Modl_structureContext ctx, final Parent parent) {
        log.trace("visitStructure()");

        return (ctx.modl_array() != null) ?
                visitArray(ctx.modl_array(), parent) :
                (ctx.modl_map() != null) ?
                        visitMap(ctx.modl_map(), parent) :
                        (ctx.modl_pair() != null) ?
                                visitPair(ctx.modl_pair(), parent) :
                                (ctx.modl_top_level_conditional() != null) ?
                                        visitTopLevelConditional(ctx.modl_top_level_conditional(), parent) :
                                        null;
    }

    /**
     * Parse a TopLevelConditional
     *
     * @param ctx    the context
     * @param parent Parent Object
     * @return a TopLevelConditional
     */
    private TopLevelConditional visitTopLevelConditional(final MODLParser.Modl_top_level_conditionalContext ctx, final Parent parent) {
        log.trace("visitTopLevelConditional()");

        val tlc = TopLevelConditional.of(ancestry, parent, Vector.empty(), Vector.empty(), Vector.empty());

        val tests = (ctx.modl_condition_test() != null) ?
                Vector.ofAll(ctx.modl_condition_test()
                        .stream()
                        .map(ctx2 -> visitConditionTest(ctx2, tlc))) :
                null;

        val returns = (ctx.modl_top_level_conditional_return() != null) ?
                Vector.ofAll(ctx.modl_top_level_conditional_return()
                        .stream()
                        .map(ctx1 -> visitTopLevelConditionalReturn(ctx1, tlc))) :
                null;

        return tlc.with(ancestry, tests, returns);
    }

    /**
     * Parse a MapConditional
     *
     * @param ctx    the context
     * @param parent Parent Object
     * @return a MapConditional
     */
    private MapConditional visitMapConditional(final MODLParser.Modl_map_conditionalContext ctx, final Parent parent) {
        log.trace("visitMapConditional()");

        val mc = MapConditional.of(ancestry, parent, Vector.empty(), Vector.empty(), Vector.empty());

        val tests = (ctx.modl_condition_test() != null) ?
                Vector.ofAll(ctx.modl_condition_test()
                        .stream()
                        .map(ctx2 -> visitConditionTest(ctx2, mc))) :
                null;

        val returns = (ctx.modl_map_conditional_return() != null) ?
                Vector.ofAll(ctx.modl_map_conditional_return()
                        .stream()
                        .map(ctx1 -> visitMapConditionalReturn(ctx1, mc))) :
                null;

        return mc.with(ancestry, tests, returns);
    }

    /**
     * Parse a MapConditionalReturn
     *
     * @param ctx    the context
     * @param parent Parent Object
     * @return a MapConditionalReturn
     */
    private MapConditionalReturn visitMapConditionalReturn(final MODLParser.Modl_map_conditional_returnContext ctx, final Parent parent) {
        log.trace("visitMapConditionalReturn()");
        val mcr = MapConditionalReturn.of(ancestry, parent, Vector.empty());
        val items = Vector.ofAll(ctx.modl_map_item()
                .stream()
                .map(ctx1 -> visitMapItem(ctx1, mcr)));
        return mcr.with(ancestry, items);
    }

    /**
     * Parse a TopLevelConditionalReturn
     *
     * @param ctx    the context
     * @param parent Parent Object
     * @return a TopLevelConditionalReturn
     */
    private TopLevelConditionalReturn visitTopLevelConditionalReturn(final MODLParser.Modl_top_level_conditional_returnContext ctx, final Parent parent) {
        log.trace("visitTopLevelConditionalReturn()");

        val tlcr = TopLevelConditionalReturn.of(ancestry, parent, Vector.empty());
        val structures = (ctx.modl_structure() != null) ?
                Vector.ofAll(ctx.modl_structure()
                        .stream()
                        .map(ctx1 -> visitStructure(ctx1, tlcr))) :
                null;

        return tlcr.with(ancestry, structures);
    }

    /**
     * Parse a ConditionTest
     *
     * @param ctx    the context
     * @param parent Parent Object
     * @return a ConditionTest
     */
    private ConditionTest visitConditionTest(final MODLParser.Modl_condition_testContext ctx, final Parent parent) {
        log.trace("visitConditionTest()");

        val ct = ConditionTest.of(ancestry, parent, Vector.empty());

        Vector<Tuple2<ConditionOrConditionGroupInterface, String>> subConditionList = Vector.empty();

        Tuple2<ConditionOrConditionGroupInterface, String> lastSubCondition = null;
        boolean shouldNegate = false;
        for (val child : ctx.children) {
            if (child instanceof MODLParser.Modl_condition_groupContext) {
                val conditionGroup = visitConditionGroup((MODLParser.Modl_condition_groupContext) child, shouldNegate, ct);
                lastSubCondition = Tuple.of(conditionGroup, null);

                shouldNegate = false;
            } else if (child instanceof MODLParser.Modl_conditionContext) {
                val condition = visitCondition((MODLParser.Modl_conditionContext) child, shouldNegate, ct);
                lastSubCondition = Tuple.of(condition, null);

                shouldNegate = false;
            } else {
                if (child
                        .getText()
                        .equals("!")) {
                    shouldNegate = true;
                } else {
                    assert lastSubCondition != null;
                    subConditionList = subConditionList.append(lastSubCondition.update2(child.getText()));
                    lastSubCondition = null;
                }
            }

        }
        if (lastSubCondition != null) {
            subConditionList = subConditionList.append(lastSubCondition);
        }

        return ct.with(ancestry, subConditionList);
    }

    /**
     * Parse a ConditionGroup
     *
     * @param ctx    the context
     * @param parent Parent Object
     * @return a ConditionGroup
     */
    private ConditionGroup visitConditionGroup(final MODLParser.Modl_condition_groupContext ctx, final boolean shouldNegate, final Parent parent) {
        log.trace("visitConditionGroup()");
        val cg = ConditionGroup.of(ancestry, parent, Vector.empty(), false);
        val subConditionList = handleConditionGroup(ctx, cg);
        return cg.with(ancestry, subConditionList, shouldNegate);
    }

    /**
     * Convert a ConditionGroup context to a list of subconditions
     *
     * @param ctx    the context
     * @param parent Parent Object
     * @return a list of subconditions
     */
    private Vector<Tuple2<ConditionTest, String>> handleConditionGroup(final MODLParser.Modl_condition_groupContext ctx, final Parent parent) {
        log.trace("handleConditionGroup()");
        Vector<Tuple2<ConditionTest, String>> subConditionList = Vector.empty();

        String lastOperator = null;
        for (val child : ctx.children) {
            if (child instanceof MODLParser.Modl_condition_testContext) {
                val conditionGroup = visitConditionTest((MODLParser.Modl_condition_testContext) child, parent);
                subConditionList = subConditionList.append(Tuple.of(conditionGroup, lastOperator));

                lastOperator = null;
            } else {
                lastOperator = child.getText();
            }
        }
        return subConditionList;
    }

    /**
     * Parse a Condition
     *
     * @param ctx    the context
     * @param parent Parent Object
     * @return a Condition
     */
    private Condition visitCondition(final MODLParser.Modl_conditionContext ctx, final boolean shouldNegate, final Parent parent) {
        log.trace("visitCondition()");
        val c = Condition.of(ancestry, parent, NullPrimitive.instance, null, Vector.empty(), false);

        inConditional++;
        val op = (ctx.modl_operator() != null) ? visitOperator(ctx.modl_operator()) : null;

        final Vector<ValueItem> values = (ctx.modl_value() != null) ? Vector.ofAll(ctx.modl_value()
                .stream()
                .map(ctx1 -> visitValue(ctx1, c))) : Vector.empty();

        val lhs = (ctx.STRING() != null) ? ctx.STRING()
                .getText() : null;

        // A crude check for quoted values on the LHS
        if (ctx.getText()
                .startsWith("\"") || ctx.getText()
                .startsWith("`")) {
            throw new RuntimeException("Invalid Left Hand Side for conditional: " + ctx.getText());
        }

        inConditional--;
        return c.with(ancestry, StringPrimitive.of(ancestry, c, lhs), op, values, shouldNegate);
    }

    /**
     * Parse an Operator
     *
     * @param ctx the context
     * @return an Operator
     */
    private Operator visitOperator(final MODLParser.Modl_operatorContext ctx) {
        log.trace("visitOperator()");

        val equals = ctx.EQUALS() != null;
        val negate = ctx.EXCLAM() != null;
        val gthan = ctx.GTHAN() != null;
        val lthan = ctx.LTHAN() != null;

        if (equals) {
            if (gthan) {
                return GreaterThanOrEqualsOperator.instance;
            }
            if (lthan) {
                return LessThanOrEqualsOperator.instance;
            }
            if (negate) {
                return NotEqualsOperator.instance;
            }
            return EqualsOperator.instance;
        }
        if (gthan) {
            return GreaterThanOperator.instance;
        }
        if (lthan) {
            return LessThanOperator.instance;
        }
        return null;// Should never get here unless the grammar changes.
    }

    /**
     * Parse ModlArrays
     *
     * @param ctx    the context
     * @param parent Parent Object
     * @return a list of ArrayItems
     */
    private Array visitArray(final MODLParser.Modl_arrayContext ctx, final Parent parent) {
        log.trace("visitArray()");

        val arr = Array.of(ancestry, parent, Vector.empty());
        Vector<ArrayItem> items = Vector.empty();
        var prev = "";

        for (var child : ctx.children) {
            if (child instanceof MODLParser.Modl_array_itemContext) {
                items = items.append(visitArrayItem((MODLParser.Modl_array_itemContext) child, arr));
            }
            if (child instanceof MODLParser.Modl_nb_arrayContext) {
                items = items.append(visitNbArray((MODLParser.Modl_nb_arrayContext) child, arr));
            }
            if (prev.equals(child.getText())) {
                // Add a null node
                items = items.append(NullPrimitive.instance);
            }
            prev = child.getText();

        }

        return arr.with(ancestry, items);
    }

    /**
     * Parse NbArrays
     *
     * @param ctx    the context
     * @param parent Parent Object
     * @return a list of ArrayItems
     */
    private Array visitNbArray(final MODLParser.Modl_nb_arrayContext ctx, final Parent parent) {
        log.trace("visitNbArray()");

        val arr = Array.of(ancestry, parent, Vector.empty());
        Vector<ArrayItem> items = Vector.empty();
        var prev = "";

        for (var child : ctx.children) {
            if (child instanceof MODLParser.Modl_array_itemContext) {
                items = items.append(visitArrayItem((MODLParser.Modl_array_itemContext) child, arr));
            }
            if (child instanceof MODLParser.Modl_arrayContext) {
                items = items.append(visitArray((MODLParser.Modl_arrayContext) child, arr));
            }
            if (prev.equals(child.getText())) {
                // Add a null node
                items = items.append(NullPrimitive.instance);
            }
            prev = child.getText();
        }

        return arr.with(ancestry, items);
    }

    /**
     * Parse ArrayItems
     *
     * @param ctx    the context
     * @param parent Parent Object
     * @return a list of ArrayItems
     */
    private ArrayItem visitArrayItem(final MODLParser.Modl_array_itemContext ctx, final Parent parent) {
        log.trace("visitArrayItem()");

        return (ctx.modl_array_conditional() != null) ?
                visitArrayConditional(ctx.modl_array_conditional(), parent) :
                visitArrayValueItem(ctx.modl_array_value_item(), parent);
    }

    /**
     * Parse ArrayValue
     *
     * @param ctx    the context
     * @param parent Parent Object
     * @return an ArrayItem
     */
    private ArrayItem visitArrayValueItem(final MODLParser.Modl_array_value_itemContext ctx, final Parent parent) {
        log.trace("visitArrayValueItem()");

        return (ctx.modl_array() != null) ?
                visitArray(ctx.modl_array(), parent) :
                (ctx.modl_map() != null) ?
                        visitMap(ctx.modl_map(), parent) :
                        (ctx.modl_pair() != null) ?
                                visitPair(ctx.modl_pair(), parent) : (ArrayItem) visitPrimitive(ctx.modl_primitive(), parent);
    }

    /**
     * Parse ArrayConditionalContext
     *
     * @param ctx    the context
     * @param parent Parent Object
     * @return an ArrayItem
     */
    private ArrayConditional visitArrayConditional(final MODLParser.Modl_array_conditionalContext ctx, final Parent parent) {
        log.trace("visitArrayConditional()");
        val ac = ArrayConditional.of(ancestry, parent, Vector.empty(), Vector.empty(), Vector.empty());

        val tests = (ctx.modl_condition_test() != null) ?
                Vector.ofAll((ctx.modl_condition_test()
                        .stream()
                        .map(ctx1 -> visitConditionTest(ctx1, ac)))) :
                null;

        val returns = (ctx.modl_array_conditional_return() != null) ?
                Vector.ofAll((ctx.modl_array_conditional_return()
                        .stream()
                        .map(ctx1 -> visitArrayConditionalReturn(ctx1, ac)))) :
                null;

        return ac.with(ancestry, tests, returns);
    }

    /**
     * Parse ArrayConditionalReturn
     *
     * @param ctx    the context
     * @param parent Parent Object
     * @return an ArrayConditionalReturn
     */
    private ArrayConditionalReturn visitArrayConditionalReturn(final MODLParser.Modl_array_conditional_returnContext ctx, final Parent parent) {
        log.trace("visitMapConditionalReturn()");
        val acr = ArrayConditionalReturn.of(ancestry, parent, Vector.empty());

        val items = Vector.ofAll(ctx.modl_array_item()
                .stream()
                .map(ctx1 -> visitArrayItem(ctx1, acr)));
        return acr.with(ancestry, items);
    }

    /**
     * Parse ModlMaps
     *
     * @param ctx    the context
     * @param parent Parent Object
     * @return a list of MapItems
     */
    private Map visitMap(final MODLParser.Modl_mapContext ctx, final Parent parent) {
        log.trace("visitMap()");

        val map = Map.of(ancestry, parent, Vector.empty());

        val items = Vector.ofAll(ctx.modl_map_item()
                .stream()
                .map(ctx1 -> visitMapItem(ctx1, map)));

        return map.with(ancestry, items);
    }

    /**
     * Parse MapItems
     *
     * @param ctx    the context
     * @param parent Parent Object
     * @return a MapItem
     */
    private MapItem visitMapItem(MODLParser.Modl_map_itemContext ctx, final Parent parent) {
        log.trace("visitMapItem()");

        return (ctx.modl_pair() != null) ?
                visitPair(ctx.modl_pair(), parent) :
                (ctx.modl_map_conditional() != null) ?
                        visitMapConditional(ctx.modl_map_conditional(), parent) :
                        null;
    }

    /**
     * Parse a ModlPair
     *
     * @param ctx    the context
     * @param parent Parent Object
     * @return a Pair
     */
    private Pair visitPair(final MODLParser.Modl_pairContext ctx, final Parent parent) {
        log.trace("visitPair()");


        val key = Util.unquote((ctx.QUOTED() != null) ? ctx.QUOTED()
                .getText() : (ctx.STRING() != null) ? ctx.STRING()
                .getText() : null);

        if (inConditional == 0 && key != null && (key.contains("%") || key.contains(" "))) {
            throw new RuntimeException("Invalid key - spaces and % characters are not allowed: " + key);
        }

        val p = Pair.of(ancestry, parent, key, NullPrimitive.instance);
        final PairValue value;
        if (ctx.modl_array() != null) {
            value = visitArray(ctx.modl_array(), p);
        } else if (ctx.modl_map() != null) {
            value = visitMap(ctx.modl_map(), p);
        } else if (ctx.modl_value_item() != null) {
            value = visitValueItem(ctx.modl_value_item(), p);
        } else {
            value = null;
        }

        assert key != null;
        if (key.equals("*VERSION") || p.getKey()
                .equals("*V")) {
            try {
                assert value != null;
                val version = value.numericValue()
                        .intValue();

                if (version <= 0) {
                    throw new RuntimeException("Invalid MODL version: " + value.toString());
                }

                this.version = version;
            } catch (final NumberFormatException e) {
                throw new RuntimeException("Invalid MODL version: " + value.toString());
            }
        }

        addImmutableName(parent, key);

        return p.with(ancestry, key, value);
    }

    /**
     * Parse ValueItems
     *
     * @param ctx    the context
     * @param parent Parent Object
     * @return a ValueItem
     */
    private ValueItem visitValueItem(final MODLParser.Modl_value_itemContext ctx, final Parent parent) {
        log.trace("visitValueItem()");
        if (ctx.modl_value() != null) {
            return visitValue(ctx.modl_value(), parent);
        }
        if (ctx.modl_value_conditional() != null) {
            return visitValueConditional(ctx.modl_value_conditional(), parent);
        }
        return null;
    }

    /**
     * Parse ValueConditional
     *
     * @param ctx    the context
     * @param parent Parent Object
     * @return a ValueItem
     */
    private ValueConditional visitValueConditional(final MODLParser.Modl_value_conditionalContext ctx, final Parent parent) {
        log.trace("visitValueConditional()");
        val vc = ValueConditional.of(ancestry, parent, Vector.empty(), Vector.empty(), Vector.empty());
        val tests = (ctx.modl_condition_test() != null) ?
                Vector.ofAll(ctx.modl_condition_test()
                        .stream()
                        .map(ctx1 -> visitConditionTest(ctx1, vc))) :
                null;

        val returns = (ctx.modl_value_conditional_return() != null) ?
                Vector.ofAll(ctx.modl_value_conditional_return()
                        .stream()
                        .map(ctx1 -> visitValueConditionReturn(ctx1, vc))) :
                null;

        return vc.with(ancestry, tests, returns);
    }

    /**
     * Parse ValueConditionalReturn
     *
     * @param ctx    the context
     * @param parent Parent Object
     * @return a ValueConditionalReturn
     */
    private ValueConditionalReturn visitValueConditionReturn(final MODLParser.Modl_value_conditional_returnContext ctx, final Parent parent) {
        log.trace("visitValueConditionalReturn()");

        val vcr = ValueConditionalReturn.of(ancestry, parent, Vector.empty());

        val items = Vector.ofAll(ctx.modl_value_item()
                .stream()
                .map(ctx1 -> visitValueItem(ctx1, vcr)));

        return vcr.with(ancestry, items);
    }

    /**
     * Parse Values
     *
     * @param ctx    the context
     * @param parent Parent Object
     * @return a Value
     */
    private ValueItem visitValue(final MODLParser.Modl_valueContext ctx, final Parent parent) {
        log.trace("visitValue()");
        return (ctx.modl_array() != null) ?
                visitArray(ctx.modl_array(), parent) :
                (ctx.modl_map() != null) ?
                        visitMap(ctx.modl_map(), parent) :
                        (ctx.modl_pair() != null) ?
                                visitPair(ctx.modl_pair(), parent) :
                                (ctx.modl_nb_array() != null) ?
                                        visitNbArray(ctx.modl_nb_array(), parent) :
                                        (ctx.modl_primitive() != null) ?
                                                visitPrimitive(ctx.modl_primitive(), parent) :
                                                null;
    }

    /**
     * Parse Primitive
     *
     * @param ctx    the context
     * @param parent Parent Object
     * @return a ValueItem
     */
    private ValueItem visitPrimitive(final MODLParser.Modl_primitiveContext ctx, final Parent parent) {
        log.trace("visitPrimitive()");

        return (ctx.FALSE() != null) ?
                FalsePrimitive.instance :
                (ctx.TRUE() != null) ?
                        TruePrimitive.instance :
                        (ctx.STRING() != null) ?
                                StringPrimitive.of(ancestry, parent, Util.unquote(ctx.STRING()
                                        .getText())) :
                                (ctx.NULL() != null) ?
                                        NullPrimitive.instance :
                                        (ctx.NUMBER() != null) ?
                                                NumberPrimitive.of(ancestry, parent, ctx.NUMBER()
                                                        .getText()) :
                                                (ctx.QUOTED() != null) ? StringPrimitive.of(ancestry, parent, Util.unquote(ctx.QUOTED()
                                                        .getText())) :
                                                        null;
    }

}
