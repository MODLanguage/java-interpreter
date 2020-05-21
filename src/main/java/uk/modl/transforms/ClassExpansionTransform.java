package uk.modl.transforms;

import io.vavr.Function1;
import io.vavr.control.Option;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.math.NumberUtils;
import uk.modl.model.*;
import uk.modl.parser.errors.InterpreterError;
import uk.modl.utils.SupertypeInference;
import uk.modl.utils.Util;

@RequiredArgsConstructor
public class ClassExpansionTransform implements Function1<Structure, Structure> {

    /**
     * The context for this invocation of the interpreter
     */
    @NonNull
    @Setter
    private TransformationContext ctx;

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param s argument 1
     * @return the result of function application
     */
    @Override
    public Structure apply(final Structure s) {
        if (s instanceof Map) {
            return processMap((Map) s);
        }
        if (s instanceof Array) {
            return processArray((Array) s);
        }
        if (s instanceof Pair) {
            return processPair((Pair) s);
        }
        if (s instanceof TopLevelConditional) {
            return processTopLevelConditional((TopLevelConditional) s);
        }
        return s;
    }

    /**
     * @param topLevelConditional
     * @return
     */
    private TopLevelConditional processTopLevelConditional(final TopLevelConditional topLevelConditional) {
        return topLevelConditional;
    }

    /**
     * @param pair
     * @return
     */
    private Structure processPair(final Pair pair) {
        return expandToClass(pair);
    }

    /**
     * @param pair
     * @return
     */
    private Structure expandToClass(final Pair pair) {
        final Option<StarClassTransform.ClassInstruction> maybeCi = ctx.getClassByNameOrId(pair.getKey());
        if (maybeCi.isDefined()) {
            final StarClassTransform.ClassInstruction ci = maybeCi.get();

            final String supertype = SupertypeInference.inferType(ctx, ci, pair.getValue());

            switch (supertype) {
                case "map":
                    return convertPairToMap(pair, ci);
                case "null":
                    return convertPairToNull(pair, ci);
                case "arr":
                    return convertPairToArray(pair, ci);
                case "num":
                    return convertPairToNumber(pair, ci);
                case "str":
                    return convertPairToString(pair, ci);
                case "bool":
                    return convertPairToBoolean(pair, ci);
                default:
                    throw new InterpreterError("Unknown Supertype: " + supertype);
            }
        }
        return pair;
    }

    /**
     * @param pair
     * @return
     */
    private Pair convertPairToMap(final Pair pair, final StarClassTransform.ClassInstruction ci) {
        @NonNull final PairValue pairValue = pair.getValue();

        if (pairValue instanceof Structure) {
            final Structure structure = apply((Structure) pairValue);
            if (structure instanceof ValueItem) {
                return new Pair(ci.getNameOrId(), (PairValue) structure);
            } else {
                throw new InterpreterError("Cannot store this item in a Pair: " + structure.toString());
            }
        }

        return new Pair(ci.getNameOrId(), pairValue);
    }

    /**
     * @param pair
     * @return
     */
    private Pair convertPairToNull(final Pair pair, final StarClassTransform.ClassInstruction ci) {
        return new Pair(ci.getNameOrId(), NullPrimitive.instance);
    }

    /**
     * @param pair
     * @return
     */
    private Pair convertPairToArray(final Pair pair, final StarClassTransform.ClassInstruction ci) {
        @NonNull final PairValue pairValue = pair.getValue();

        if (pairValue instanceof Structure) {
            final Structure structure = apply((Structure) pairValue);
            if (structure instanceof ValueItem) {
                return new Pair(ci.getNameOrId(), (PairValue) structure);
            } else {
                throw new InterpreterError("Cannot store this item in a Pair: " + structure.toString());
            }
        }

        return new Pair(ci.getNameOrId(), pairValue);

    }

    /**
     * @param pair
     * @return
     */
    private Pair convertPairToNumber(final Pair pair, final StarClassTransform.ClassInstruction ci) {
        return new Pair(ci.getNameOrId(), new NumberPrimitive(NumberUtils.createNumber(pair.getValue()
                .toString())
                .toString()));
    }

    /**
     * @param pair
     * @return
     */
    private Pair convertPairToString(final Pair pair, final StarClassTransform.ClassInstruction ci) {
        return new Pair(ci.getNameOrId(), new StringPrimitive(pair.getValue()
                .toString()));
    }

    /**
     * @param pair
     * @return
     */
    private Pair convertPairToBoolean(final Pair pair, final StarClassTransform.ClassInstruction ci) {
        @NonNull final PairValue value = pair.getValue();
        if (Util.truthy(value)) {
            return new Pair(ci.getNameOrId(), TruePrimitive.instance);
        }
        return new Pair(ci.getNameOrId(), FalsePrimitive.instance);
    }

    /**
     * @param array
     * @return
     */
    private Array processArray(final Array array) {
        return new Array(array.getArrayItems()
                .map(this::processArrayItem));
    }

    /**
     * @param arrayItem
     * @return
     */
    private ArrayItem processArrayItem(final ArrayItem arrayItem) {
        if (arrayItem instanceof Map) {
            return processMap((Map) arrayItem);
        }
        if (arrayItem instanceof Pair) {
            return (ArrayItem) processPair((Pair) arrayItem);
        }
        if (arrayItem instanceof Array) {
            return processArray((Array) arrayItem);
        }
        if (arrayItem instanceof Primitive) {
            return processPrimitive((Primitive) arrayItem);
        }
        return arrayItem;
    }

    /**
     * @param primitive
     * @return
     */
    private Primitive processPrimitive(final Primitive primitive) {
        return primitive;
    }

    /**
     * @param map
     * @return
     */
    private Map processMap(final Map map) {
        return new Map(map.getMapItems()
                .map(this::processMapItem));
    }

    /**
     * @param mapItem
     * @return
     */
    private MapItem processMapItem(final MapItem mapItem) {
        if (mapItem instanceof Pair) {
            return (MapItem) processPair((Pair) mapItem);
        }
        if (mapItem instanceof MapConditional) {
            return processMapConditional((MapConditional) mapItem);
        }
        return mapItem;
    }

    /**
     * @param mapConditional
     * @return
     */
    private MapConditional processMapConditional(final MapConditional mapConditional) {
        return mapConditional;
    }

}
