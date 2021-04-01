package uk.modl.interpreter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

/**
 * Convert a Jackson JsonNode to a JSON string
 */
@UtilityClass
@Log4j2
public class JsonToString {

  /**
   * The object mapper
   */
  private static final ObjectMapper mapper = new ObjectMapper();

  /**
   * Pretty-print JSON
   * 
   * @param jsonNode JsonNode
   * @return String
   */
  public static String convertPretty(@NonNull final JsonNode jsonNode) {
    return convert(mapper.writerWithDefaultPrettyPrinter(), jsonNode);
  }

  /**
   * Print JSON
   * 
   * @param jsonNode JsonNode
   * @return String
   */
  public static String convert(@NonNull final JsonNode jsonNode) {
    return convert(mapper.writer(), jsonNode);
  }

  /**
   * Convert a JsonNode to a JSON string
   * 
   * @param m    ObjectWriter
   * @param node JsonNode
   * @return String
   */
  private static String convert(@NonNull final ObjectWriter m, @NonNull final JsonNode node) {
    try {
      return m.writeValueAsString(node);
    } catch (final JsonProcessingException e) {
      log.error(e);
      return "JSON processing error";
    }
  }

}
