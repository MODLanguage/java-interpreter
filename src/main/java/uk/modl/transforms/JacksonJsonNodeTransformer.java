package uk.modl.transforms;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.vavr.Function1;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import lombok.extern.log4j.Log4j2;
import uk.modl.model.*;

import java.util.function.Function;
import java.util.function.Predicate;

@Log4j2
public class JacksonJsonNodeTransformer implements Function1<Modl, JsonNode> {
    /**
     * Predicate to filter out items that should not be in the output.
     */
    private static Predicate<Structure> shouldAppearInOutput = (s -> {
        if (s instanceof Pair) {
            final Pair p = (Pair) s;
            return !p.key.startsWith("_") && !p.key.startsWith("*") && !p.key.equals("?");
        }
        return true;
    });

    /**
     * The result of converting a Modl object to a JsonNode
     */
    private JsonNode result;

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param modl argument 1
     * @return the result of function application
     */
    @Override
    public JsonNode apply(final Modl modl) {
        final Vector<Structure> filtered = modl.structures.filter(shouldAppearInOutput);

        switch (filtered.size()) {
            case 0:
                break;
            case 1:
                // For a single structure, pull up the next level to the top level.
                final Structure structure = filtered.get(0);
                if (structure instanceof Array) {
                    final ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
                    this.result = arrayNode;
                    filtered.forEach(s -> accept(arrayNode, s));
                } else {
                    final ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
                    this.result = objectNode;
                    accept(objectNode, structure);
                }
                break;
            default:
                // For multiple items make the result an ArrayNode
                final ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
                this.result = arrayNode;
                filtered.forEach(s -> accept(arrayNode, s));
        }
        return result;
    }

    private void accept(final ArrayNode node, final Structure structure) {
        Option.of(structure)
                .map(addMapToArrayNode(node))
                .map(addArrayToArrayNode(node))
                .map(addPairToArrayNode(node));

    }

    private Function<Object, Object> addPairToArrayNode(final ArrayNode node) {
        return p -> {
            if (p instanceof Pair) {

                final Pair pair = (Pair) p;
                if (pair.key.startsWith("_")) {
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
                } else if (pv instanceof Primitive) {
                    final Primitive prim = (Primitive) ((Pair) p).value;
                    node.add(JsonNodeFactory.instance.textNode(prim.toString()));
                } else if (pv instanceof Array) {
                    // Arrays are added elsewhere
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
                arr.arrayItems.forEach(arrayItem -> accept(newNode, arrayItem));

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
                .map(addPairValueToArrayNode(node));
    }

    private void accept(final ObjectNode node, final Structure structure) {
        Option.of(structure)
                .map(addMapToObjectNode(node))
                .map(addPairToObjectNode(node));
    }

    private Function<Object, Object> addMapToObjectNode(final ObjectNode node) {
        return s -> {
            if (s instanceof Map) {

                final Map map = (Map) s;
                map.mapItems.forEach(mapItem -> {
                    if (mapItem instanceof Pair) {
                        final Pair p = (Pair) mapItem;
                        accept(node, p);
                    } else {
                        log.error("Cannot process object type: {}", s.getClass()
                                .getName());
                    }
                });

            }
            return s;
        };
    }

    private void accept(final ObjectNode node, final Pair pair) {
        Option.of(pair)
                .map(addPairToObjectNode(node));
    }

    private Function<Object, Object> addPairToObjectNode(final ObjectNode node) {
        return p -> {
            if (p instanceof Pair) {
                final Pair pair = (Pair) p;
                if (pair.key.startsWith("_")) {
                    return p;
                }
                if (pair.value instanceof StringPrimitive) {
                    final Primitive prim = (Primitive) ((Pair) p).value;
                    node.set(pair.key, JsonNodeFactory.instance.textNode(prim.toString()));
                } else if (pair.value instanceof NumberPrimitive) {
                    final NumberPrimitive prim = (NumberPrimitive) ((Pair) p).value;
                    final Number n = prim.numericValue();
                    if (n instanceof Double) {
                        node.set(pair.key, JsonNodeFactory.instance.numberNode(n.doubleValue()));
                    } else if (n instanceof Float) {
                        node.set(pair.key, JsonNodeFactory.instance.numberNode(n.floatValue()));
                    } else if (n instanceof Integer) {
                        node.set(pair.key, JsonNodeFactory.instance.numberNode(n.intValue()));
                    } else if (n instanceof Long) {
                        node.set(pair.key, JsonNodeFactory.instance.numberNode(n.longValue()));
                    }
                } else if (pair.value instanceof TruePrimitive) {
                    node.set(pair.key, JsonNodeFactory.instance.booleanNode(true));
                } else if (pair.value instanceof FalsePrimitive) {
                    node.set(pair.key, JsonNodeFactory.instance.booleanNode(false));
                } else if (pair.value instanceof NullPrimitive) {
                    node.set(pair.key, JsonNodeFactory.instance.nullNode());
                } else if (pair.value instanceof Primitive) {
                    final Primitive prim = (Primitive) ((Pair) p).value;
                    node.set(pair.key, JsonNodeFactory.instance.textNode(prim.toString()));
                } else if (pair.value instanceof Array) {
                    final ArrayNode newNode = JsonNodeFactory.instance.arrayNode();
                    node.set(pair.key, newNode);
                    ((Array) pair.value).arrayItems.forEach(ai -> {
                        accept(newNode, ai);
                    });
                } else if (pair.value instanceof Map) {
                    final ObjectNode newNode = JsonNodeFactory.instance.objectNode();
                    node.set(pair.key, newNode);
                    addMapToObjectNode(newNode).apply(pair.value);
                } else if (pair.value instanceof ValueItem) {
                    final ObjectNode newNode = JsonNodeFactory.instance.objectNode();
                    node.set(pair.key, newNode);
                    accept(newNode, (ValueItem) pair.value);
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
