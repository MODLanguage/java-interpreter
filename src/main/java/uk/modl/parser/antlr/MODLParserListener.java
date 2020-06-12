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

// Generated from /Users/tonywalmsley/work/num/grammar/antlr4/MODLParser.g4 by ANTLR 4.7.2
package uk.modl.parser.antlr;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MODLParser}.
 */
@SuppressWarnings("EmptyMethod")
public interface MODLParserListener extends ParseTreeListener {

    /**
     * Enter a parse tree produced by {@link MODLParser#modl}.
     *
     * @param ctx the parse tree
     */
    void enterModl(MODLParser.ModlContext ctx);

    /**
     * Exit a parse tree produced by {@link MODLParser#modl}.
     *
     * @param ctx the parse tree
     */
    void exitModl(MODLParser.ModlContext ctx);

    /**
     * Enter a parse tree produced by {@link MODLParser#modl_structure}.
     *
     * @param ctx the parse tree
     */
    void enterModl_structure(MODLParser.Modl_structureContext ctx);

    /**
     * Exit a parse tree produced by {@link MODLParser#modl_structure}.
     *
     * @param ctx the parse tree
     */
    void exitModl_structure(MODLParser.Modl_structureContext ctx);

    /**
     * Enter a parse tree produced by {@link MODLParser#modl_map}.
     *
     * @param ctx the parse tree
     */
    void enterModl_map(MODLParser.Modl_mapContext ctx);

    /**
     * Exit a parse tree produced by {@link MODLParser#modl_map}.
     *
     * @param ctx the parse tree
     */
    void exitModl_map(MODLParser.Modl_mapContext ctx);

    /**
     * Enter a parse tree produced by {@link MODLParser#modl_array}.
     *
     * @param ctx the parse tree
     */
    void enterModl_array(MODLParser.Modl_arrayContext ctx);

    /**
     * Exit a parse tree produced by {@link MODLParser#modl_array}.
     *
     * @param ctx the parse tree
     */
    void exitModl_array(MODLParser.Modl_arrayContext ctx);

    /**
     * Enter a parse tree produced by {@link MODLParser#modl_nb_array}.
     *
     * @param ctx the parse tree
     */
    void enterModl_nb_array(MODLParser.Modl_nb_arrayContext ctx);

    /**
     * Exit a parse tree produced by {@link MODLParser#modl_nb_array}.
     *
     * @param ctx the parse tree
     */
    void exitModl_nb_array(MODLParser.Modl_nb_arrayContext ctx);

    /**
     * Enter a parse tree produced by {@link MODLParser#modl_pair}.
     *
     * @param ctx the parse tree
     */
    void enterModl_pair(MODLParser.Modl_pairContext ctx);

    /**
     * Exit a parse tree produced by {@link MODLParser#modl_pair}.
     *
     * @param ctx the parse tree
     */
    void exitModl_pair(MODLParser.Modl_pairContext ctx);

    /**
     * Enter a parse tree produced by {@link MODLParser#modl_value_item}.
     *
     * @param ctx the parse tree
     */
    void enterModl_value_item(MODLParser.Modl_value_itemContext ctx);

    /**
     * Exit a parse tree produced by {@link MODLParser#modl_value_item}.
     *
     * @param ctx the parse tree
     */
    void exitModl_value_item(MODLParser.Modl_value_itemContext ctx);

    /**
     * Enter a parse tree produced by {@link MODLParser#modl_top_level_conditional}.
     *
     * @param ctx the parse tree
     */
    void enterModl_top_level_conditional(MODLParser.Modl_top_level_conditionalContext ctx);

    /**
     * Exit a parse tree produced by {@link MODLParser#modl_top_level_conditional}.
     *
     * @param ctx the parse tree
     */
    void exitModl_top_level_conditional(MODLParser.Modl_top_level_conditionalContext ctx);

    /**
     * Enter a parse tree produced by {@link MODLParser#modl_top_level_conditional_return}.
     *
     * @param ctx the parse tree
     */
    void enterModl_top_level_conditional_return(MODLParser.Modl_top_level_conditional_returnContext ctx);

    /**
     * Exit a parse tree produced by {@link MODLParser#modl_top_level_conditional_return}.
     *
     * @param ctx the parse tree
     */
    void exitModl_top_level_conditional_return(MODLParser.Modl_top_level_conditional_returnContext ctx);

    /**
     * Enter a parse tree produced by {@link MODLParser#modl_map_conditional}.
     *
     * @param ctx the parse tree
     */
    void enterModl_map_conditional(MODLParser.Modl_map_conditionalContext ctx);

    /**
     * Exit a parse tree produced by {@link MODLParser#modl_map_conditional}.
     *
     * @param ctx the parse tree
     */
    void exitModl_map_conditional(MODLParser.Modl_map_conditionalContext ctx);

    /**
     * Enter a parse tree produced by {@link MODLParser#modl_map_conditional_return}.
     *
     * @param ctx the parse tree
     */
    void enterModl_map_conditional_return(MODLParser.Modl_map_conditional_returnContext ctx);

    /**
     * Exit a parse tree produced by {@link MODLParser#modl_map_conditional_return}.
     *
     * @param ctx the parse tree
     */
    void exitModl_map_conditional_return(MODLParser.Modl_map_conditional_returnContext ctx);

    /**
     * Enter a parse tree produced by {@link MODLParser#modl_map_item}.
     *
     * @param ctx the parse tree
     */
    void enterModl_map_item(MODLParser.Modl_map_itemContext ctx);

    /**
     * Exit a parse tree produced by {@link MODLParser#modl_map_item}.
     *
     * @param ctx the parse tree
     */
    void exitModl_map_item(MODLParser.Modl_map_itemContext ctx);

    /**
     * Enter a parse tree produced by {@link MODLParser#modl_array_conditional}.
     *
     * @param ctx the parse tree
     */
    void enterModl_array_conditional(MODLParser.Modl_array_conditionalContext ctx);

    /**
     * Exit a parse tree produced by {@link MODLParser#modl_array_conditional}.
     *
     * @param ctx the parse tree
     */
    void exitModl_array_conditional(MODLParser.Modl_array_conditionalContext ctx);

    /**
     * Enter a parse tree produced by {@link MODLParser#modl_array_conditional_return}.
     *
     * @param ctx the parse tree
     */
    void enterModl_array_conditional_return(MODLParser.Modl_array_conditional_returnContext ctx);

    /**
     * Exit a parse tree produced by {@link MODLParser#modl_array_conditional_return}.
     *
     * @param ctx the parse tree
     */
    void exitModl_array_conditional_return(MODLParser.Modl_array_conditional_returnContext ctx);

    /**
     * Enter a parse tree produced by {@link MODLParser#modl_array_item}.
     *
     * @param ctx the parse tree
     */
    void enterModl_array_item(MODLParser.Modl_array_itemContext ctx);

    /**
     * Exit a parse tree produced by {@link MODLParser#modl_array_item}.
     *
     * @param ctx the parse tree
     */
    void exitModl_array_item(MODLParser.Modl_array_itemContext ctx);

    /**
     * Enter a parse tree produced by {@link MODLParser#modl_value_conditional}.
     *
     * @param ctx the parse tree
     */
    void enterModl_value_conditional(MODLParser.Modl_value_conditionalContext ctx);

    /**
     * Exit a parse tree produced by {@link MODLParser#modl_value_conditional}.
     *
     * @param ctx the parse tree
     */
    void exitModl_value_conditional(MODLParser.Modl_value_conditionalContext ctx);

    /**
     * Enter a parse tree produced by {@link MODLParser#modl_value_conditional_return}.
     *
     * @param ctx the parse tree
     */
    void enterModl_value_conditional_return(MODLParser.Modl_value_conditional_returnContext ctx);

    /**
     * Exit a parse tree produced by {@link MODLParser#modl_value_conditional_return}.
     *
     * @param ctx the parse tree
     */
    void exitModl_value_conditional_return(MODLParser.Modl_value_conditional_returnContext ctx);

    /**
     * Enter a parse tree produced by {@link MODLParser#modl_condition_test}.
     *
     * @param ctx the parse tree
     */
    void enterModl_condition_test(MODLParser.Modl_condition_testContext ctx);

    /**
     * Exit a parse tree produced by {@link MODLParser#modl_condition_test}.
     *
     * @param ctx the parse tree
     */
    void exitModl_condition_test(MODLParser.Modl_condition_testContext ctx);

    /**
     * Enter a parse tree produced by {@link MODLParser#modl_operator}.
     *
     * @param ctx the parse tree
     */
    void enterModl_operator(MODLParser.Modl_operatorContext ctx);

    /**
     * Exit a parse tree produced by {@link MODLParser#modl_operator}.
     *
     * @param ctx the parse tree
     */
    void exitModl_operator(MODLParser.Modl_operatorContext ctx);

    /**
     * Enter a parse tree produced by {@link MODLParser#modl_condition}.
     *
     * @param ctx the parse tree
     */
    void enterModl_condition(MODLParser.Modl_conditionContext ctx);

    /**
     * Exit a parse tree produced by {@link MODLParser#modl_condition}.
     *
     * @param ctx the parse tree
     */
    void exitModl_condition(MODLParser.Modl_conditionContext ctx);

    /**
     * Enter a parse tree produced by {@link MODLParser#modl_condition_group}.
     *
     * @param ctx the parse tree
     */
    void enterModl_condition_group(MODLParser.Modl_condition_groupContext ctx);

    /**
     * Exit a parse tree produced by {@link MODLParser#modl_condition_group}.
     *
     * @param ctx the parse tree
     */
    void exitModl_condition_group(MODLParser.Modl_condition_groupContext ctx);

    /**
     * Enter a parse tree produced by {@link MODLParser#modl_value}.
     *
     * @param ctx the parse tree
     */
    void enterModl_value(MODLParser.Modl_valueContext ctx);

    /**
     * Exit a parse tree produced by {@link MODLParser#modl_value}.
     *
     * @param ctx the parse tree
     */
    void exitModl_value(MODLParser.Modl_valueContext ctx);

    /**
     * Enter a parse tree produced by {@link MODLParser#modl_array_value_item}.
     *
     * @param ctx the parse tree
     */
    void enterModl_array_value_item(MODLParser.Modl_array_value_itemContext ctx);

    /**
     * Exit a parse tree produced by {@link MODLParser#modl_array_value_item}.
     *
     * @param ctx the parse tree
     */
    void exitModl_array_value_item(MODLParser.Modl_array_value_itemContext ctx);

    /**
     * Enter a parse tree produced by {@link MODLParser#modl_primitive}.
     *
     * @param ctx the parse tree
     */
    void enterModl_primitive(MODLParser.Modl_primitiveContext ctx);

    /**
     * Exit a parse tree produced by {@link MODLParser#modl_primitive}.
     *
     * @param ctx the parse tree
     */
    void exitModl_primitive(MODLParser.Modl_primitiveContext ctx);

}