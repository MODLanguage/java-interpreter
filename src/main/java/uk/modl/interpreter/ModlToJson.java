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

/**
 * Class to convert a Modl object to a JsonNode tree
 */
@UtilityClass
public class ModlToJson {

  /**
   * Convert a Modl object to a JsonNode tree
   * 
   * @param modl Modl
   * @return JsonNode
   */
  public static final JsonNode convert(@NonNull final Modl modl) {

    final List<ModlStructure> structures = modl.getStructures();

    if (structures.size() == 1 && structures.get(0) instanceof ModlArray) {

      // Handle a top-level array
      return arrayToJson((ModlArray) structures.get(0));
    } else if (structures.size() == 1 && structures.get(0) instanceof ModlPrimitive) {

      // Handle a top-level primitive
      return toJson((ModlValue) structures.get(0));
    } else {

      // Handle a top-level object.
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
   * Convert a ModlValue to a JsonNode
   * 
   * @param value ModlValue
   * @return JsonNode
   */
  private JsonNode toJson(@NonNull final ModlValue value) {
    if (value instanceof ModlArray) {
      return arrayToJson((ModlArray) value);
    }
    if (value instanceof ModlMap) {
      return mapToJson((ModlMap) value, new ObjectNode(JsonNodeFactory.instance));
    }
    if (value instanceof ModlPair) {
      return pairToJson((ModlPair) value, new ObjectNode(JsonNodeFactory.instance));
    }
    if (value instanceof ModlQuoted) {
      return new TextNode(StringEscapeReplacer.replace(unquote(((ModlQuoted) value).getValue())));
    }
    if (value instanceof ModlInteger) {
      return new IntNode(((ModlInteger) value).getValue());
    }
    if (value instanceof ModlFloat) {
      return new FloatNode(((ModlFloat) value).getValue());
    }
    if (value instanceof ModlString) {
      return new TextNode(StringEscapeReplacer.replace(unquote(((ModlString) value).getValue())));
    }
    if (value == ModlBoolNull.MODL_FALSE) {
      return BooleanNode.FALSE;
    }
    if (value == ModlBoolNull.MODL_TRUE) {
      return BooleanNode.TRUE;
    }
    if (value == ModlBoolNull.MODL_NULL) {
      return null;
    }
    return NullNode.instance;
  }

  /**
   * Remove double quotes and back-ticks from a string.
   * 
   * @param s String
   * @return String
   */
  private static String unquote(final String s) {
    return (s.startsWith("`") && s.endsWith("`")) || (s.startsWith("\"") && s.endsWith("\""))
        ? s.substring(1, s.length() - 1)
        : s;
  }

  /**
   * Convert a ModlPair to a JsonNode
   * 
   * @param p      ModlPair
   * @param result JsonNode - modifed
   * @return JsonNode
   */
  private JsonNode pairToJson(final ModlPair p, final ObjectNode result) {
    final String key = StringEscapeReplacer.replace(unquote(p.getKey()));
    result.set(key, toJson((ModlValue) p.getValue()));
    return result;
  }

  /**
   * Convert a ModlMap to a JsonNode
   * 
   * @param m      ModlMap
   * @param result JsonNode - modifed
   * @return JsonNode
   */
  private JsonNode mapToJson(final ModlMap m, final ObjectNode result) {
    m.getItems().forEach(i -> pairToJson(i, result));
    return result;
  }

  /**
   * Convert a ModlArray to a JsonNode
   * 
   * @param a ModlArray
   * @return JsonNode
   */
  private JsonNode arrayToJson(final ModlArray a) {
    final ArrayNode result = new ArrayNode(JsonNodeFactory.instance);
    a.getItems().forEach(x -> result.add(toJson(x)));
    return result;
  }

}
