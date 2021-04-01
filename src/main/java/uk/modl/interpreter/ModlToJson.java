package uk.modl.interpreter;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.FloatNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import uk.modl.model.Modl;
import uk.modl.model.ModlArray;
import uk.modl.model.ModlBoolNull;
import uk.modl.model.ModlFloat;
import uk.modl.model.ModlInteger;
import uk.modl.model.ModlMap;
import uk.modl.model.ModlPair;
import uk.modl.model.ModlPrimitive;
import uk.modl.model.ModlQuoted;
import uk.modl.model.ModlString;
import uk.modl.model.ModlStructure;
import uk.modl.model.ModlValue;
import uk.modl.utils.StringEscapeReplacer;

@UtilityClass
public class ModlToJson {

  public static final JsonNode convert(@NonNull final Modl modl) {

    final List<ModlStructure> structures = modl.getStructures();

    if (structures.size() == 1 && structures.get(0) instanceof ModlArray) {
      return arrayToJson((ModlArray) structures.get(0));
    } else if (structures.size() == 1 && structures.get(0) instanceof ModlPrimitive) {
      return toJson((ModlValue) structures.get(0));
    } else {
      final ObjectNode result = new ObjectNode(JsonNodeFactory.instance);

      structures.forEach(structure -> {
        if (structure instanceof ModlPair) {
          pairToJson((ModlPair) structure, result);
        }
        if (structure instanceof ModlMap) {
          mapToJson((ModlMap) structure, result);
        }
        if (structure instanceof ModlArray) {
          throw new RuntimeException("Array cannot be stored directly in a map, it must be a pair");
        }

      });
      return result.size() == 0 ? null : result;
    }
  }

  /**
   * To json
   * 
   * @param x
   * @returns json
   */
  private JsonNode toJson(@NonNull final ModlValue x) {
    if (x instanceof ModlArray) {
      return arrayToJson((ModlArray) x);
    }
    if (x instanceof ModlMap) {
      return mapToJson((ModlMap) x, new ObjectNode(JsonNodeFactory.instance));
    }
    if (x instanceof ModlPair) {
      return pairToJson((ModlPair) x, new ObjectNode(JsonNodeFactory.instance));
    }
    if (x instanceof ModlQuoted) {
      return new TextNode(StringEscapeReplacer.replace(unquote(((ModlQuoted) x).getValue())));
    }
    if (x instanceof ModlInteger) {
      return new IntNode(((ModlInteger) x).getValue());
    }
    if (x instanceof ModlFloat) {
      return new FloatNode(((ModlFloat) x).getValue());
    }
    if (x instanceof ModlString) {
      return new TextNode(StringEscapeReplacer.replace(unquote(((ModlString) x).getValue())));
    }
    if (x == ModlBoolNull.MODL_FALSE) {
      return BooleanNode.FALSE;
    }
    if (x == ModlBoolNull.MODL_TRUE) {
      return BooleanNode.TRUE;
    }
    if (x == ModlBoolNull.MODL_NULL) {
      return null;
    }
    return NullNode.instance;
  }

  private static String unquote(final String s) {
    return (s.startsWith("`") && s.endsWith("`")) || (s.startsWith("\"") && s.endsWith("\""))
        ? s.substring(1, s.length() - 1)
        : s;
  }

  /**
   * Pairs to json
   * 
   * @param p
   * @param result
   * @returns
   */
  private JsonNode pairToJson(final ModlPair p, final ObjectNode result) {
    final String key = StringEscapeReplacer.replace(unquote(p.getKey()));
    result.set(key, toJson((ModlValue) p.getValue()));
    return result;
  }

  /**
   * Maps to json
   * 
   * @param m
   * @param result
   * @returns to json
   */
  private JsonNode mapToJson(final ModlMap m, final ObjectNode result) {
    m.getItems().forEach(i -> pairToJson(i, result));
    return result;
  }

  /**
   * Arrays to json
   * 
   * @param a
   * @returns to json
   */
  private JsonNode arrayToJson(final ModlArray a) {
    final ArrayNode result = new ArrayNode(JsonNodeFactory.instance);
    a.getItems().forEach(x -> result.add(toJson(x)));
    return result;
  }

}
