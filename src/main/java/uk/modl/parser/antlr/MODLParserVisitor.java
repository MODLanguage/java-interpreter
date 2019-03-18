package uk.modl.parser.antlr;
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
	 * Visit a parse tree produced by {@link MODLParser#modl_structure}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModl_structure(MODLParser.Modl_structureContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#modl_map}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModl_map(MODLParser.Modl_mapContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#modl_array}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModl_array(MODLParser.Modl_arrayContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#modl_nb_array}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModl_nb_array(MODLParser.Modl_nb_arrayContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#modl_pair}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModl_pair(MODLParser.Modl_pairContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#modl_value_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModl_value_item(MODLParser.Modl_value_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#modl_top_level_conditional}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModl_top_level_conditional(MODLParser.Modl_top_level_conditionalContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#modl_top_level_conditional_return}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModl_top_level_conditional_return(MODLParser.Modl_top_level_conditional_returnContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#modl_map_conditional}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModl_map_conditional(MODLParser.Modl_map_conditionalContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#modl_map_conditional_return}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModl_map_conditional_return(MODLParser.Modl_map_conditional_returnContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#modl_map_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModl_map_item(MODLParser.Modl_map_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#modl_array_conditional}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModl_array_conditional(MODLParser.Modl_array_conditionalContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#modl_array_conditional_return}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModl_array_conditional_return(MODLParser.Modl_array_conditional_returnContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#modl_array_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModl_array_item(MODLParser.Modl_array_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#modl_value_conditional}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModl_value_conditional(MODLParser.Modl_value_conditionalContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#modl_value_conditional_return}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModl_value_conditional_return(MODLParser.Modl_value_conditional_returnContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#modl_condition_test}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModl_condition_test(MODLParser.Modl_condition_testContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#modl_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModl_operator(MODLParser.Modl_operatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#modl_condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModl_condition(MODLParser.Modl_conditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#modl_condition_group}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModl_condition_group(MODLParser.Modl_condition_groupContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#modl_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModl_value(MODLParser.Modl_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link MODLParser#modl_array_value_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModl_array_value_item(MODLParser.Modl_array_value_itemContext ctx);
}