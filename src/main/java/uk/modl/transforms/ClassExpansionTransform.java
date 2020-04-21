package uk.modl.transforms;

import io.vavr.Function1;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import uk.modl.model.*;
import uk.modl.parser.errors.InterpreterError;

@RequiredArgsConstructor
public class ClassExpansionTransform implements Function1<Pair, Pair> {

    /**
     * The context for this invocation of the interpreter
     */
    @NonNull
    private TransformationContext ctx;

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param p argument 1
     * @return the result of function application
     */
    @Override
    public Pair apply(final Pair p) {
        if (p != null && !p.getKey()
                .startsWith("*")) {
            final Option<StarClassTransform.ClassInstruction> maybeClass = ctx.getClassByNameOrId(p.getKey());
            if (maybeClass.isDefined()) {
                return accept(p, maybeClass.get());
            }
        }
        return p;
    }

    public void setCtx(final TransformationContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Expand a Pair if it matches a class
     *
     * @param p  the Pair
     * @param ci the class to transform to
     * @return the expanded Pair
     */
    private Pair accept(final Pair p, final StarClassTransform.ClassInstruction ci) {
        final String newKey = StringUtils.isEmpty(ci.getName()) ? ci.getId() : ci.getName();
        PairValue pairValue = null;

        var superclass = (ci.getSuperclass() == null) ? inferSuperclass(p.getValue()) : ci.getSuperclass();
        switch (superclass) {
            case "map":
                if (p.getValue() instanceof Array) {
                    final Vector<MapItem> mapItems = toMapUsingAssign((Array) p.getValue(), ci);
                    pairValue = new Map(mapItems);
                } else if (p.getValue() instanceof Map) {
                    pairValue = p.getValue();
                } else if (p.getValue() instanceof StringPrimitive) {
                    pairValue = p.getValue();
                } else if (p.getValue() instanceof NumberPrimitive) {
                    pairValue = p.getValue();
                } else if (p.getValue() instanceof TruePrimitive) {
                    pairValue = p.getValue();
                } else if (p.getValue() instanceof FalsePrimitive) {
                    pairValue = p.getValue();
                } else if (p.getValue() instanceof NullPrimitive) {
                    pairValue = p.getValue();
                }
                break;
            case "arr":
                pairValue = new Array(Vector.empty());
                break;
            case "str":
                pairValue = new StringPrimitive(p.getValue()
                        .toString());
                break;
            case "num":
                pairValue = new NumberPrimitive(p.getValue()
                        .numericValue()
                        .toString());
                break;
            default:
                if (ctx.getClassByNameOrId(superclass)
                        .isDefined()) {
                    pairValue = inherit(ci.getName(), p.getValue());
                } else {
                    return p;
                }
        }
        return new Pair(newKey, pairValue);
    }

    private String inferSuperclass(final PairValue value) {
        if (value instanceof NumberPrimitive) {
            return "num";
        }
        if (value instanceof StringPrimitive) {
            return "str";
        }
        if (value instanceof Array) {
            return "arr";
        }
        if (value instanceof Map) {
            return "map";
        }
        if (value instanceof TruePrimitive) {
            return "str";
        }
        if (value instanceof FalsePrimitive) {
            return "str";
        }
        if (value instanceof NullPrimitive) {
            return "str";
        }
        return null;
    }

    private Vector<MapItem> toMapUsingAssign(final Array array, final StarClassTransform.ClassInstruction ci) {
        // Find the correct assign statement
        final Array assignArray = (Array) ci.getAssign()
                .find(arr -> ((Array) arr).getArrayItems()
                        .size() == array.getArrayItems()
                        .size())
                .getOrElseThrow(() -> new InterpreterError("No matching *assign value of length: " + array.getArrayItems()
                        .size()));

        // Convert the array items to pairs using the assign statement.
        return array.getArrayItems()
                .zipWith(assignArray.getArrayItems(), (item, assign) -> {
                    if (item instanceof PairValue) {
                        return new Pair(assign.toString(), (PairValue) item);
                    } else if (item instanceof ArrayConditional) {
                        return new Pair(assign.toString(), new Array(((ArrayConditional) item).getResult()));
                    } else {
                        throw new NullPointerException("NEED TO HANDLE : " + item.getClass()
                                .toString());// TODO: Fix this
                    }
                });
    }

    private PairValue inherit(final String superclass, final PairValue value) {
        // TODO:
        final Option<StarClassTransform.ClassInstruction> maybeClass = ctx.getClassByNameOrId(superclass);

        if (value instanceof Map) {
            final Vector<MapItem> mapItems =
                    maybeClass.map(ci -> recurseAddPairs(ci, Vector.empty()))
                            .getOrElse(Vector.empty())
                            .foldLeft(((Map) value).getMapItems(), Vector::append);

            return new Map(mapItems);
        }
        return value;
    }

    private Vector<Pair> recurseAddPairs(final StarClassTransform.ClassInstruction ci, final Vector<Pair> pairs) {
        Vector<Pair> items = Vector.empty();
        final Option<StarClassTransform.ClassInstruction> superclass = ctx.getClassByNameOrId(ci.getSuperclass());
        if (superclass.isDefined()) {
            items = recurseAddPairs(superclass.get(), pairs);
        }
        return ci.getPairs()
                .appendAll(items);
    }
}
