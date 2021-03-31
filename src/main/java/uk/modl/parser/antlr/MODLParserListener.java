// Generated from MODLParser.g4 by ANTLR 4.7.2
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
	 * Enter a parse tree produced by {@link MODLParser#modl_structure}.
	 * @param ctx the parse tree
	 */
	void enterModl_structure(MODLParser.Modl_structureContext ctx);
	/**
	 * Exit a parse tree produced by {@link MODLParser#modl_structure}.
	 * @param ctx the parse tree
	 */
	void exitModl_structure(MODLParser.Modl_structureContext ctx);
	/**
	 * Enter a parse tree produced by {@link MODLParser#modl_map}.
	 * @param ctx the parse tree
	 */
	void enterModl_map(MODLParser.Modl_mapContext ctx);
	/**
	 * Exit a parse tree produced by {@link MODLParser#modl_map}.
	 * @param ctx the parse tree
	 */
	void exitModl_map(MODLParser.Modl_mapContext ctx);
	/**
	 * Enter a parse tree produced by {@link MODLParser#modl_array}.
	 * @param ctx the parse tree
	 */
	void enterModl_array(MODLParser.Modl_arrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link MODLParser#modl_array}.
	 * @param ctx the parse tree
	 */
	void exitModl_array(MODLParser.Modl_arrayContext ctx);
	/**
	 * Enter a parse tree produced by {@link MODLParser#modl_pair}.
	 * @param ctx the parse tree
	 */
	void enterModl_pair(MODLParser.Modl_pairContext ctx);
	/**
	 * Exit a parse tree produced by {@link MODLParser#modl_pair}.
	 * @param ctx the parse tree
	 */
	void exitModl_pair(MODLParser.Modl_pairContext ctx);
	/**
	 * Enter a parse tree produced by {@link MODLParser#modl_value}.
	 * @param ctx the parse tree
	 */
	void enterModl_value(MODLParser.Modl_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link MODLParser#modl_value}.
	 * @param ctx the parse tree
	 */
	void exitModl_value(MODLParser.Modl_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link MODLParser#modl_primitive}.
	 * @param ctx the parse tree
	 */
	void enterModl_primitive(MODLParser.Modl_primitiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link MODLParser#modl_primitive}.
	 * @param ctx the parse tree
	 */
	void exitModl_primitive(MODLParser.Modl_primitiveContext ctx);
}