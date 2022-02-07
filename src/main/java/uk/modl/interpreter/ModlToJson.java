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
import io.vavr.control.Either;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import uk.modl.interpreter.model.Modl;
import uk.modl.interpreter.model.ModlArray;
import uk.modl.interpreter.model.ModlBoolNull;
import uk.modl.interpreter.model.ModlFloat;
import uk.modl.interpreter.model.ModlInteger;
import uk.modl.interpreter.model.ModlMap;
import uk.modl.interpreter.model.ModlPair;
import uk.modl.interpreter.model.ModlPrimitive;
import uk.modl.interpreter.model.ModlQuoted;
import uk.modl.interpreter.model.ModlString;
import uk.modl.interpreter.model.ModlStructure;
import uk.modl.interpreter.model.ModlValue;
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
    public static JsonNode convert(@NonNull final Modl modl) {

        final @NonNull Either<ModlPrimitive, List<ModlStructure>> structures = modl.getStructures();

        final Either<JsonNode, JsonNode> jsonNodes = structures.bimap(ModlToJson::toJson, list -> {
            if (list.size() == 1 && list.get(0) instanceof ModlArray) {
                return arrayToJson((ModlArray) list.get(0));
            } else {
                // Handle a top-level object.
                final ObjectNode result = new ObjectNode(JsonNodeFactory.instance);

                list.forEach(structure -> {
                    if (structure instanceof ModlPair) {
                        pairToJson((ModlPair) structure, result);
                    }
                    if (structure instanceof ModlMap) {
                        mapToJson((ModlMap) structure, result);
                    }
                    if (structure instanceof ModlArray) {
                        throw new RuntimeException(
                                "Array cannot be stored directly in a map, it must be a pair");
                    }

                });
                return result.size() == 0 ? null : result;
            }
        });

        if (jsonNodes.isLeft()) {
            return jsonNodes.getLeft();
        }
        return jsonNodes.get();
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
     * @param result JsonNode - modified
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
     * @param result JsonNode - modified
     * @return JsonNode
     */
    private JsonNode mapToJson(final ModlMap m, final ObjectNode result) {
        m.getItems()
                .forEach(i -> pairToJson(i, result));
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
        a.getItems()
                .forEach(x -> result.add(toJson(x)));
        return result;
    }

}
