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

package uk.modl.transforms;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import lombok.extern.log4j.Log4j2;
import uk.modl.model.*;
import uk.modl.utils.StringEscapeReplacer;

import java.util.function.Function;
import java.util.function.Predicate;

@Log4j2
public class JacksonJsonNodeTransform {

    /**
     * Predicate to filter out items that should not be in the output.
     */
    private static final Predicate<Structure> shouldAppearInOutput = (s -> {
        if (s instanceof Pair) {
            final Pair p = (Pair) s;
            return !p.getKey()
                    .startsWith("_") && !p.getKey()
                    .startsWith("*") && !p.getKey()
                    .equals("?");
        }
        return true;
    });

    private final TransformationContext ctx;

    public JacksonJsonNodeTransform(final TransformationContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param modl argument 1
     * @return the result of function application
     */
    public JsonNode apply(final Modl modl) {
        JsonNode result = null;// In case this object is being re-used
        final Vector<Structure> filtered = modl.getStructures()
                .filter(shouldAppearInOutput);

        switch (filtered.size()) {
            case 0:
                break;
            case 1:
                // For a single structure, pull up the next level to the top level.
                final Structure structure = filtered.get(0);
                if (structure instanceof Array) {
                    final ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
                    result = arrayNode;
                    ((Array) structure).getArrayItems()
                            .forEach(arrayItem -> accept(arrayNode, arrayItem));
                } else {
                    final ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
                    result = objectNode;
                    accept(objectNode, structure);
                }
                break;
            default:
                // If there are no top level pairs, make the top level an array, otherwise make it a map.
                if (filtered.count(s -> s instanceof Pair) > 0) {
                    final ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
                    result = objectNode;
                    filtered.forEach(s -> accept(objectNode, s));
                } else {
                    final ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode(filtered.size());
                    result = arrayNode;
                    filtered.forEach(s -> accept(arrayNode, (ArrayItem) s));

                }
        }
        if (result != null && !result.elements()
                .hasNext()) {
            result = null;
        }
        return result;
    }

    private Function<Object, Object> addPairToArrayNode(final ArrayNode node) {
        return p -> {
            if (p instanceof Pair) {

                final Pair pair = (Pair) p;
                if (pair.getKey()
                        .startsWith("_")) {
                    return p;
                }
                final ObjectNode newNode = JsonNodeFactory.instance.objectNode();
                node.add(newNode);
                addPairToObjectNode(newNode).apply(p);

            }
            return p;
        };
    }

    private Function<Object, Object> addPairValueToArrayNode(final ArrayNode node) {
        return p -> {
            if (p instanceof PairValue) {

                final PairValue pv = (PairValue) p;

                if (pv instanceof StringPrimitive) {
                    final Primitive prim = (Primitive) pv;
                    node.add(JsonNodeFactory.instance.textNode(prim.toString()));
                } else if (pv instanceof NumberPrimitive) {
                    final NumberPrimitive prim = (NumberPrimitive) pv;
                    final Number n = prim.numericValue();
                    if (n instanceof Double || n instanceof Float) {
                        node.add(JsonNodeFactory.instance.numberNode(n.doubleValue()));
                    } else if (n instanceof Integer || n instanceof Long) {
                        node.add(JsonNodeFactory.instance.numberNode(n.longValue()));
                    }
                } else if (pv instanceof TruePrimitive) {
                    node.add(JsonNodeFactory.instance.booleanNode(true));
                } else if (pv instanceof FalsePrimitive) {
                    node.add(JsonNodeFactory.instance.booleanNode(false));
                } else if (pv instanceof NullPrimitive) {
                    node.add(JsonNodeFactory.instance.nullNode());
                }
            }
            return p;
        };
    }

    private Function<Object, Object> addArrayToArrayNode(final ArrayNode node) {
        return s -> {
            if (s instanceof Array) {

                final Array arr = (Array) s;
                final ArrayNode newNode = JsonNodeFactory.instance.arrayNode();
                node.add(newNode);
                arr.getArrayItems()
                        .forEach(arrayItem -> accept(newNode, arrayItem));

            }
            return s;
        };
    }

    private Function<Object, Object> addMapToArrayNode(final ArrayNode node) {
        return s -> {
            if (s instanceof Map) {

                // Add a new Object node and add the map items to it.
                final ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
                node.add(objectNode);
                final Map map = (Map) s;
                accept(objectNode, map);
            }
            return s;
        };
    }

    private void accept(final ObjectNode node, final Map map) {
        Option.of(map)
                .map(addMapToObjectNode(node));

    }

    private void accept(final ArrayNode node, final ArrayItem arrayItem) {
        Option.of(arrayItem)
                .map(addArrayToArrayNode(node))
                .map(addMapToArrayNode(node))
                .map(addPairToArrayNode(node))
                .map(addPairValueToArrayNode(node))
                .map(addArrayConditionalToArrayNode(node));
    }

    private void accept(final ObjectNode node, final Structure structure) {
        Option.of(structure)
                .filter(shouldAppearInOutput)
                .map(addMapToObjectNode(node))
                .map(addTopLevelConditionalToObjectNode(node))
                .map(addPairToObjectNode(node));
    }

    private Function<Object, Object> addMapToObjectNode(final ObjectNode node) {
        return s -> {
            if (s instanceof Map) {

                final Map map = (Map) s;
                map.getMapItems()
                        .forEach(mapItem -> {
                            if (mapItem instanceof Pair) {
                                final Pair p = (Pair) mapItem;
                                accept(node, p);
                            } else if (mapItem instanceof MapConditional) {
                                accept(node, (MapConditional) mapItem);
                            } else {
                                log.error("Cannot process object type: {}", mapItem.getClass()
                                        .getName());
                            }
                        });

            }
            return s;
        };
    }

    private void accept(final ObjectNode node, final MapConditional mc) {
        Option.of(mc)
                .map(addMapConditionalToMapNode(node));
    }

    private Function<Object, Object> addTopLevelConditionalToObjectNode(final ObjectNode node) {
        return s -> {
            if (s instanceof TopLevelConditional) {
                ((TopLevelConditional) s).getResult()
                        .forEach(structure -> accept(node, structure));
            }
            return s;
        };
    }

    private Function<Object, Object> addArrayConditionalToArrayNode(final ArrayNode node) {
        return s -> {
            if (s instanceof ArrayConditional) {
                ((ArrayConditional) s).getResult()
                        .forEach(arrayItem -> accept(node, arrayItem));
            }
            return s;
        };
    }

    private Function<Object, Object> addMapConditionalToMapNode(final ObjectNode node) {
        return s -> {
            if (s instanceof MapConditional) {
                ((MapConditional) s).getResult()
                        .forEach(mapItem -> accept(node, (ValueItem) mapItem));
            }
            return s;
        };
    }

    private void accept(final ObjectNode node, final Pair pair) {
        Option.of(pair)
                .filter(shouldAppearInOutput)
                .map(addPairToObjectNode(node));
    }

    private Function<Object, Object> addPairToObjectNode(final ObjectNode node) {
        return p -> {
            if (p instanceof Pair) {
                final Pair pair = (Pair) p;
                if (pair.getKey()
                        .startsWith("_")) {
                    return p;
                }
                final String key = StringEscapeReplacer.replace(pair.getKey());

                if (pair.getValue() instanceof StringPrimitive) {
                    final Primitive prim = (Primitive) ((Pair) p).getValue();
                    final String text = StringEscapeReplacer.replace(prim.toString());
                    node.set(key, JsonNodeFactory.instance.textNode(text));
                } else if (pair.getValue() instanceof NumberPrimitive) {
                    final NumberPrimitive prim = (NumberPrimitive) ((Pair) p).getValue();
                    final Number n = prim.numericValue();
                    if (n instanceof Double) {
                        node.set(key, JsonNodeFactory.instance.numberNode(n.doubleValue()));
                    } else if (n instanceof Float) {
                        node.set(key, JsonNodeFactory.instance.numberNode(n.floatValue()));
                    } else if (n instanceof Integer) {
                        node.set(key, JsonNodeFactory.instance.numberNode(n.intValue()));
                    } else if (n instanceof Long) {
                        node.set(key, JsonNodeFactory.instance.numberNode(n.longValue()));
                    }
                } else if (pair.getValue() instanceof TruePrimitive) {
                    node.set(key, JsonNodeFactory.instance.booleanNode(true));
                } else if (pair.getValue() instanceof FalsePrimitive) {
                    node.set(key, JsonNodeFactory.instance.booleanNode(false));
                } else if (pair.getValue() instanceof NullPrimitive) {
                    node.set(key, JsonNodeFactory.instance.nullNode());
                } else if (pair.getValue() instanceof Primitive) {
                    final Primitive prim = (Primitive) ((Pair) p).getValue();
                    node.set(key, JsonNodeFactory.instance.textNode(prim.toString()));
                } else if (pair.getValue() instanceof Array) {
                    final ArrayNode newNode = JsonNodeFactory.instance.arrayNode();
                    node.set(key, newNode);
                    ((Array) pair.getValue()).getArrayItems()
                            .forEach(ai -> accept(newNode, ai));
                } else if (pair.getValue() instanceof Map) {
                    final ObjectNode newNode = JsonNodeFactory.instance.objectNode();
                    node.set(key, newNode);
                    addMapToObjectNode(newNode).apply(pair.getValue());
                } else if (pair.getValue() instanceof ValueConditional) {
                    final Vector<ValueItem> valueItems = ((ValueConditional) pair.getValue()).getResult();
                    if (valueItems.size() == 1) {
                        accept(node, Pair.of(ctx.getAncestry(), pair, key, valueItems.get(0)));
                    } else {
                        accept(node, Pair.of(ctx.getAncestry(), pair, key, Array.of(ctx.getAncestry(), pair, valueItems.map(v -> (ArrayItem) v))));
                    }
                } else if (pair.getValue() instanceof ValueItem) {
                    final ObjectNode newNode = JsonNodeFactory.instance.objectNode();
                    node.set(key, newNode);
                    accept(newNode, (ValueItem) pair.getValue());
                }
            }
            return p;
        };
    }

    private void accept(final ObjectNode node, final ValueItem item) {
        Option.of(item)
                .map(addMapToObjectNode(node))
                .map(addPairToObjectNode(node));
    }

}
