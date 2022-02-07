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
