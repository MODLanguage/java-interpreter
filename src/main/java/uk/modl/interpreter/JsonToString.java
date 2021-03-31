package uk.modl.interpreter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

@UtilityClass
@Log4j2
public class JsonToString {
  private static final ObjectMapper mapper = new ObjectMapper();

  public static String convertPretty(@NonNull final JsonNode jsonNode) {
    try {
      return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
    } catch (JsonProcessingException e) {
      log.error(e);
      return "JSON processing error";
    }
  }

  public static String convert(@NonNull final JsonNode jsonNode) {
    try {
      return mapper.writeValueAsString(jsonNode);
    } catch (JsonProcessingException e) {
      log.error(e);
      return "JSON processing error";
    }
  }

}
