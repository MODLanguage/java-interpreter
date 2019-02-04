// Generated from /Users/alex/code/NUM/MODL/interpreter/grammar/MODLParser.g4 by ANTLR 4.7
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MODLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MODLParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link MODLParser#modl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModl(MODLParser.ModlContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#structure}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructure(MODLParser.StructureContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#map}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMap(MODLParser.MapContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#array}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray(MODLParser.ArrayContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#nb_array}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNb_array(MODLParser.Nb_arrayContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#pair}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPair(MODLParser.PairContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#value_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue_item(MODLParser.Value_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#top_level_conditional}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTop_level_conditional(MODLParser.Top_level_conditionalContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#top_level_conditional_return}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTop_level_conditional_return(MODLParser.Top_level_conditional_returnContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#map_conditional}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMap_conditional(MODLParser.Map_conditionalContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#map_conditional_return}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMap_conditional_return(MODLParser.Map_conditional_returnContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#map_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMap_item(MODLParser.Map_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#array_conditional}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray_conditional(MODLParser.Array_conditionalContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#array_conditional_return}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray_conditional_return(MODLParser.Array_conditional_returnContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#array_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray_item(MODLParser.Array_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#value_conditional}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue_conditional(MODLParser.Value_conditionalContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#value_conditional_return}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue_conditional_return(MODLParser.Value_conditional_returnContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#condition_test}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondition_test(MODLParser.Condition_testContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator(MODLParser.OperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondition(MODLParser.ConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#condition_group}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondition_group(MODLParser.Condition_groupContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(MODLParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#array_value_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray_value_item(MODLParser.Array_value_itemContext ctx);
}