package uk.modl.transforms;

import io.vavr.Function1;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
        final String newKey = StringUtils.isEmpty(ci.name) ? ci.id : ci.name;
        PairValue pairValue = null;

        // TODO: ALL of this!
        switch (ci.superclass) {
            case "map":
                if (p.getValue() instanceof Array) {
                    final Vector<MapItem> mapItems = toMapUsingAssign((Array) p.getValue(), ci);
                    pairValue = new Map(mapItems);
                } else if (p.getValue() instanceof Map) {
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
                if (ctx.getClassByNameOrId(ci.superclass)
                        .isDefined()) {
                    pairValue = inherit(ci.name, p.getValue());
                } else {
                    return p;
                }
        }
        return new Pair(newKey, pairValue);
    }

    private Vector<MapItem> toMapUsingAssign(final Array array, final StarClassTransform.ClassInstruction ci) {
        // Find the correct assign statement
        final Array assignArray = (Array) ci.assign.find(arr -> ((Array) arr).getArrayItems()
                .size() == array.getArrayItems()
                .size())
                .getOrElseThrow(() -> new InterpreterError("No matching *assign value of length: " + array.getArrayItems()
                        .size()));

        // Convert the array items to pairs using the assign statement.
        return array.getArrayItems()
                .zipWith(assignArray.getArrayItems(), (item, assign) -> new Pair(assign.toString(), (PairValue) item));
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
        final Option<StarClassTransform.ClassInstruction> superclass = ctx.getClassByNameOrId(ci.superclass);
        if (superclass.isDefined()) {
            items = recurseAddPairs(superclass.get(), pairs);
        }
        return ci.pairs.appendAll(items);
    }
}
