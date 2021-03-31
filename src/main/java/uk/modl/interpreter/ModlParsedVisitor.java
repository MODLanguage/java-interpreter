package uk.modl.interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import uk.modl.model.Modl;
import uk.modl.model.ModlArray;
import uk.modl.model.ModlBoolNull;
import uk.modl.model.ModlFloat;
import uk.modl.model.ModlInteger;
import uk.modl.model.ModlMap;
import uk.modl.model.ModlPair;
import uk.modl.model.ModlPrimitive;
import uk.modl.model.ModlString;
import uk.modl.model.ModlStructure;
import uk.modl.model.ModlValue;
import uk.modl.parser.antlr.MODLParser.ModlContext;
import uk.modl.parser.antlr.MODLParser.Modl_arrayContext;
import uk.modl.parser.antlr.MODLParser.Modl_mapContext;
import uk.modl.parser.antlr.MODLParser.Modl_pairContext;
import uk.modl.parser.antlr.MODLParser.Modl_primitiveContext;
import uk.modl.parser.antlr.MODLParser.Modl_structureContext;
import uk.modl.parser.antlr.MODLParser.Modl_valueContext;

@Log4j2
@UtilityClass
public class ModlParsedVisitor {

  public static final Modl visitModl(@NonNull final ModlContext ctx) {
    // Check for a single primitive value at the root.
    final List<ParseTree> children = ctx.children.stream().filter(ModlParsedVisitor::nonTerminal)
        .collect(Collectors.toList());

    if (children.size() == 1 && children.get(0) instanceof Modl_primitiveContext) {
      final ModlPrimitive prim = visitModlPrimitive((Modl_primitiveContext) children.get(0));
      return new Modl(Arrays.asList(prim));
    }

    // No primitive so it must be a list of structures
    final List<ModlStructure> structures = children.stream().map(ModlParsedVisitor::visitModlStructure)
        .collect(Collectors.toList());
    if (structures != null && !structures.isEmpty()) {
      return new Modl(structures);
    }
    return new Modl(new ArrayList<>());

  }

  private static ModlStructure visitModlStructure(@NonNull final ParseTree ctx) {
    if (ctx instanceof Modl_mapContext) {
      return visitModlMap((Modl_mapContext) ctx);
    }
    if (ctx instanceof Modl_arrayContext) {
      return visitModlArray((Modl_arrayContext) ctx);
    }
    if (ctx instanceof Modl_pairContext) {
      return visitModlPair((Modl_pairContext) ctx);
    }
    if (ctx instanceof Modl_structureContext) {
      final Modl_structureContext sctx = (Modl_structureContext) ctx;
      if (sctx.children.size() == 1) {
        return visitModlStructure(sctx.children.get(0));
      }
    }
    return new ModlMap(new ArrayList<>());
  }

  private static ModlPair visitModlPair(@NonNull final Modl_pairContext ctx) {
    if (ctx.children == null) {
      final String message = "No children: " + ctx.getClass().getName();
      log.error(message);
      return new ModlPair("Error", new ModlString(message));
    }
    final String key = ctx.children.get(0).getText();

    validateKey(key);

    final ParseTree value = (ctx.children.get(1) instanceof TerminalNode) ? ctx.children.get(2) : ctx.children.get(1);

    final ModlStructure pairValue = visitChild(value);

    return new ModlPair(key, pairValue);
  }

  private static ModlArray visitModlArray(@NonNull final Modl_arrayContext ctx) {
    final List<ModlValue> children = ctx.children.stream().filter(ModlParsedVisitor::nonTerminal)
        .map(ModlParsedVisitor::visitChild).map(ModlValue.class::cast).collect(Collectors.toList());

    if (children != null && !children.isEmpty()) {
      return new ModlArray(children);
    }
    return new ModlArray(new ArrayList<>());
  }

  private static ModlMap visitModlMap(@NonNull final Modl_mapContext ctx) {
    final List<ModlPair> children = ctx.children.stream().filter(ModlParsedVisitor::nonTerminal)
        .map(ModlParsedVisitor::visitChild).map(ModlPair.class::cast).collect(Collectors.toList());

    if (children != null && !children.isEmpty()) {
      return new ModlMap(children);
    }
    return new ModlMap(new ArrayList<>());
  }

  private static ModlStructure visitModlValue(@NonNull final Modl_valueContext ctx) {
    if (ctx.children != null && !ctx.children.isEmpty()) {
      return visitChild(ctx.children.get(0));
    }
    final String message = "No children: " + ctx.getClass().getName();
    log.error(message);
    return new ModlString(message);
  }

  private static ModlPrimitive visitModlPrimitive(@NonNull final Modl_primitiveContext ctx) {
    final String text = ctx.getText();

    if (text == null || text.equals("null") || text.equals("000")) {
      return ModlBoolNull.MODL_NULL;
    }
    if (text.equals("false") || text.equals("00")) {
      return ModlBoolNull.MODL_FALSE;
    }
    if (text.equals("true") || text.equals("01")) {
      return ModlBoolNull.MODL_TRUE;
    }
    try {
      final int intValue = Integer.parseInt(text, 10);
      if (Integer.toString(intValue).equals(text)) {
        return new ModlInteger(intValue);
      }
      final float floatValue = Float.parseFloat(text);
      if (Float.toString(floatValue).equals(text)) {
        return new ModlFloat(floatValue);
      }
    } catch (final NumberFormatException ex) {
      // Can be ignored.
    }
    return new ModlString(text);
  }

  private static boolean nonTerminal(@NonNull final ParseTree tree) {
    return !(tree instanceof TerminalNode);
  }

  private static ModlStructure visitChild(@NonNull final ParseTree ctx) {
    if (ctx instanceof Modl_mapContext) {
      return visitModlMap((Modl_mapContext) ctx);
    }
    if (ctx instanceof Modl_arrayContext) {
      return visitModlArray((Modl_arrayContext) ctx);
    }
    if (ctx instanceof Modl_pairContext) {
      return visitModlPair((Modl_pairContext) ctx);
    }
    if (ctx instanceof Modl_structureContext) {
      return visitModlStructure(ctx);
    }
    if (ctx instanceof Modl_pairContext) {
      return visitModlPair((Modl_pairContext) ctx);
    }
    if (ctx instanceof Modl_valueContext) {
      return visitModlValue((Modl_valueContext) ctx);
    }
    if (ctx instanceof Modl_primitiveContext) {
      return visitModlPrimitive((Modl_primitiveContext) ctx);
    }

    final String message = "Unknown object: " + ctx.getClass().getName();
    log.error(message);
    return new ModlString(message);
  }

  /**
   * Validates key
   * 
   * @param k
   */
  private void validateKey(@NonNull final String k) {
    if (k.contains("%")) {
      throw new RuntimeException(
          String.format("Interpreter Error: Invalid key - spaces and %% characters are not allowed: %s", k));
    }
  }

}
