package uk.modl.transforms;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.vavr.Function1;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.Getter;
import uk.modl.model.*;
import uk.modl.parser.errors.InterpreterError;

import java.util.function.Function;

public class JacksonTransformer implements Function1<Modl, Either<Exception, JsonNode>> {

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param modl argument 1
     * @return the result of function application
     */
    @Override
    public Either<Exception, JsonNode> apply(final Modl modl) {
        try {
            final JsonRenderer renderer = new JsonRenderer();
            renderer.accept(modl);
            return Either.right(renderer.getResult());
        } catch (final Exception e) {
            return Either.left(e);
        }
    }

    private static class JsonRenderer {
        @Getter
        private JsonNode result;

        public void accept(final Modl modl) {
            switch (modl.structures.length()) {
                case 0:
                    break;
                case 1:
                    // For a single structure, pull up the next level to the top level.
                    final ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
                    this.result = objectNode;
                    accept(objectNode, modl.structures.get(0));
                    break;
                default:
                    // For multiple items make the result an ArrayNode
                    final ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
                    this.result = arrayNode;
                    modl.structures.forEach(s -> {
                        accept(arrayNode, s);
                    });
            }
        }

        private void accept(final ArrayNode node, final Structure structure) {
            Option.of(structure)
                    .map(addMapItemsToArrayNode(node))
                    .map(addArrayItemsToArrayNode(node));

        }

        private Function<Structure, Structure> addArrayItemsToArrayNode(final ArrayNode node) {
            return s -> {
                if (s instanceof Array) {

                    final Array arr = (Array) s;
                    arr.arrayItems.forEach(arrayItem -> node.add(accept(arrayItem)));

                }
                return s;
            };
        }

        private Function<Structure, Structure> addMapItemsToArrayNode(final ArrayNode node) {
            return s -> {
                if (s instanceof Map) {

                    final Map map = (Map) s;
                    map.mapItems.forEach(mapItem -> node.add(accept(mapItem)));

                }
                return s;
            };
        }

        private JsonNode accept(final MapItem mapItem) {
            return null;
        }

        private JsonNode accept(final ArrayItem arrayItem) {
            return null;
        }

        private void accept(final ObjectNode node, final Structure structure) {
            Option.of(structure)
                    .map(addMapItemsToObjectNode(node))
                    .map(addArrayItemsToObjectNode(node));
        }

        private Function<Structure, Structure> addArrayItemsToObjectNode(final ObjectNode node) {
            return s -> {
                if (s instanceof Array) {

                    final Array arr = (Array) s;
                    arr.arrayItems.forEach(arrayItem -> {
                        if (arrayItem instanceof Pair) {
                            final Pair p = (Pair) arrayItem;
                            accept(node, p);
                        } else {
                            throw new InterpreterError("Cannot process object type: " + s.getClass()
                                    .getName());
                        }
                    });

                }
                return s;
            };
        }

        private Function<Structure, Structure> addMapItemsToObjectNode(final ObjectNode node) {
            return s -> {
                if (s instanceof Map) {

                    final Map map = (Map) s;
                    map.mapItems.forEach(mapItem -> {
                        if (mapItem instanceof Pair) {
                            final Pair p = (Pair) mapItem;
                            accept(node, p);
                        } else {
                            throw new InterpreterError("Cannot process object type: " + s.getClass()
                                    .getName());
                        }
                    });

                }
                return s;
            };
        }

        private void accept(final ObjectNode node, final Pair pair) {
            Option.of(pair)
                    .map(addValueItemToObjectNode(node));
        }

        private Function<Pair, Pair> addValueItemToObjectNode(final ObjectNode node) {
            return p -> {
                if (p.value instanceof ValueItem) {
                    final ObjectNode newNode = JsonNodeFactory.instance.objectNode();
                    node.set(p.key, newNode);
                    accept(newNode, (ValueItem) p.value);
                }
                return p;
            };
        }

        private void accept(final ObjectNode node, final ValueItem item) {
            Option.of(item)
                    .map(i -> {
                        if (i instanceof Map) {
                            addMapItemsToObjectNode(node).apply((Map) (i));
                        }
                        return i;
                    });
        }
    }
}
