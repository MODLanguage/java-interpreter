// Generated from /Users/alex/code/NUM/MODL/java-interpreter/src/main/java/uk/modl/parser/antlr/MODLParser.g4 by ANTLR 4.7
package uk.modl.parser.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MODLParser}.
 */
public interface MODLParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MODLParser#modl}.
	 * @param ctx the parse tree
	 */
	void enterModl(MODLParser.ModlContext ctx);
	/**
	 * Exit a parse tree produced by {@link MODLParser#modl}.
	 * @param ctx the parse tree
	 */
	void exitModl(MODLParser.ModlContext ctx);
	/**
	 * Enter a parse tree produced by {@link MODLParser#structure}.
	 * @param ctx the parse tree
	 */
	void enterStructure(MODLParser.StructureContext ctx);
	/**
	 * Exit a parse tree produced by {@link MODLParser#structure}.
	 * @param ctx the parse tree
	 */
	void exitStructure(MODLParser.StructureContext ctx);
	/**
	 * Enter a parse tree produced by {@link MODLParser#map}.
	 * @param ctx the parse tree
	 */
	void enterMap(MODLParser.MapContext ctx);
	/**
	 * Exit a parse tree produced by {@link MODLParser#map}.
	 * @param ctx the parse tree
	 */
	void exitMap(MODLParser.MapContext ctx);
	/**
	 * Enter a parse tree produced by {@link MODLParser#array}.
	 * @param ctx the parse tree
	 */
	void enterArray(MODLParser.ArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link MODLParser#array}.
	 * @param ctx the parse tree
	 */
	void exitArray(MODLParser.ArrayContext ctx);
	/**
	 * Enter a parse tree produced by {@link MODLParser#pair}.
	 * @param ctx the parse tree
	 */
	void enterPair(MODLParser.PairContext ctx);
	/**
	 * Exit a parse tree produced by {@link MODLParser#pair}.
	 * @param ctx the parse tree
	 */
	void exitPair(MODLParser.PairContext ctx);
	/**
	 * Enter a parse tree produced by {@link MODLParser#value_item}.
	 * @param ctx the parse tree
	 */
	void enterValue_item(MODLParser.Value_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link MODLParser#value_item}.
	 * @param ctx the parse tree
	 */
	void exitValue_item(MODLParser.Value_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link MODLParser#top_level_conditional}.
	 * @param ctx the parse tree
	 */
	void enterTop_level_conditional(MODLParser.Top_level_conditionalContext ctx);
	/**
	 * Exit a parse tree produced by {@link MODLParser#top_level_conditional}.
	 * @param ctx the parse tree
	 */
	void exitTop_level_conditional(MODLParser.Top_level_conditionalContext ctx);
	/**
	 * Enter a parse tree produced by {@link MODLParser#top_level_conditional_return}.
	 * @param ctx the parse tree
	 */
	void enterTop_level_conditional_return(MODLParser.Top_level_conditional_returnContext ctx);
	/**
	 * Exit a parse tree produced by {@link MODLParser#top_level_conditional_return}.
	 * @param ctx the parse tree
	 */
	void exitTop_level_conditional_return(MODLParser.Top_level_conditional_returnContext ctx);
	/**
	 * Enter a parse tree produced by {@link MODLParser#map_conditional}.
	 * @param ctx the parse tree
	 */
	void enterMap_conditional(MODLParser.Map_conditionalContext ctx);
	/**
	 * Exit a parse tree produced by {@link MODLParser#map_conditional}.
	 * @param ctx the parse tree
	 */
	void exitMap_conditional(MODLParser.Map_conditionalContext ctx);
	/**
	 * Enter a parse tree produced by {@link MODLParser#map_conditional_return}.
	 * @param ctx the parse tree
	 */
	void enterMap_conditional_return(MODLParser.Map_conditional_returnContext ctx);
	/**
	 * Exit a parse tree produced by {@link MODLParser#map_conditional_return}.
	 * @param ctx the parse tree
	 */
	void exitMap_conditional_return(MODLParser.Map_conditional_returnContext ctx);
	/**
	 * Enter a parse tree produced by {@link MODLParser#map_item}.
	 * @param ctx the parse tree
	 */
	void enterMap_item(MODLParser.Map_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link MODLParser#map_item}.
	 * @param ctx the parse tree
	 */
	void exitMap_item(MODLParser.Map_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link MODLParser#array_conditional}.
	 * @param ctx the parse tree
	 */
	void enterArray_conditional(MODLParser.Array_conditionalContext ctx);
	/**
	 * Exit a parse tree produced by {@link MODLParser#array_conditional}.
	 * @param ctx the parse tree
	 */
	void exitArray_conditional(MODLParser.Array_conditionalContext ctx);
	/**
	 * Enter a parse tree produced by {@link MODLParser#array_conditional_return}.
	 * @param ctx the parse tree
	 */
	void enterArray_conditional_return(MODLParser.Array_conditional_returnContext ctx);
	/**
	 * Exit a parse tree produced by {@link MODLParser#array_conditional_return}.
	 * @param ctx the parse tree
	 */
	void exitArray_conditional_return(MODLParser.Array_conditional_returnContext ctx);
	/**
	 * Enter a parse tree produced by {@link MODLParser#array_item}.
	 * @param ctx the parse tree
	 */
	void enterArray_item(MODLParser.Array_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link MODLParser#array_item}.
	 * @param ctx the parse tree
	 */
	void exitArray_item(MODLParser.Array_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link MODLParser#value_conditional}.
	 * @param ctx the parse tree
	 */
	void enterValue_conditional(MODLParser.Value_conditionalContext ctx);
	/**
	 * Exit a parse tree produced by {@link MODLParser#value_conditional}.
	 * @param ctx the parse tree
	 */
	void exitValue_conditional(MODLParser.Value_conditionalContext ctx);
	/**
	 * Enter a parse tree produced by {@link MODLParser#value_conditional_return}.
	 * @param ctx the parse tree
	 */
	void enterValue_conditional_return(MODLParser.Value_conditional_returnContext ctx);
	/**
	 * Exit a parse tree produced by {@link MODLParser#value_conditional_return}.
	 * @param ctx the parse tree
	 */
	void exitValue_conditional_return(MODLParser.Value_conditional_returnContext ctx);
	/**
	 * Enter a parse tree produced by {@link MODLParser#condition_test}.
	 * @param ctx the parse tree
	 */
	void enterCondition_test(MODLParser.Condition_testContext ctx);
	/**
	 * Exit a parse tree produced by {@link MODLParser#condition_test}.
	 * @param ctx the parse tree
	 */
	void exitCondition_test(MODLParser.Condition_testContext ctx);
	/**
	 * Enter a parse tree produced by {@link MODLParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperator(MODLParser.OperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link MODLParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperator(MODLParser.OperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link MODLParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterCondition(MODLParser.ConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MODLParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitCondition(MODLParser.ConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MODLParser#condition_group}.
	 * @param ctx the parse tree
	 */
	void enterCondition_group(MODLParser.Condition_groupContext ctx);
	/**
	 * Exit a parse tree produced by {@link MODLParser#condition_group}.
	 * @param ctx the parse tree
	 */
	void exitCondition_group(MODLParser.Condition_groupContext ctx);
	/**
	 * Enter a parse tree produced by {@link MODLParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(MODLParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link MODLParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(MODLParser.ValueContext ctx);
}