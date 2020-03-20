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
        if (p != null && !p.key.startsWith("*")) {
            final Option<StarClassTransform.ClassInstruction> maybeClass = ctx.getClassByNameOrId(p.key);
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
                if (p.value instanceof Array) {
                    final Vector<MapItem> mapItems = toMapUsingAssign((Array) p.value, ci);
                    pairValue = new Map(mapItems);
                } else if (p.value instanceof Map) {
                    pairValue = p.value;
                }
                break;
            case "arr":
                pairValue = new Array(Vector.empty());
                break;
            case "str":
                pairValue = new StringPrimitive(p.value.toString());
                break;
            case "num":
                pairValue = new NumberPrimitive(p.value.numericValue()
                        .toString());
                break;
            default:
                if (ctx.getClassByNameOrId(ci.superclass)
                        .isDefined()) {
                    pairValue = inherit(ci.name, p.value);
                } else {
                    return p;
                }
        }
        return new Pair(newKey, pairValue);
    }

    private Vector<MapItem> toMapUsingAssign(final Array array, final StarClassTransform.ClassInstruction ci) {
        // Find the correct assign statement
        final Option<ArrayItem> maybeAssignArray = ci.assign.find(arr -> ((Array) arr).arrayItems.size() == array.arrayItems.size());
        final Array assignArray = (Array) maybeAssignArray.getOrElseThrow(() -> new InterpreterError("No matching *assign value of length: " + array.arrayItems.size()));

        // Convert the array items to pairs using the assign statement.
        Vector<MapItem> result = Vector.empty();
        int i = 0;
        while (i < array.arrayItems.size()) {
            final ArrayItem item = array.arrayItems.get(i);
            if (item instanceof PairValue) {
                result = result.append(new Pair(assignArray.arrayItems.get(i)
                        .toString(), (PairValue) item));
            }
            i++;
        }
        return result;
    }

    private PairValue inherit(final String superclass, final PairValue value) {
        // TODO:
        final Option<StarClassTransform.ClassInstruction> maybeClass = ctx.getClassByNameOrId(superclass);

        if (value instanceof Map) {
            final Vector<MapItem> mapItems =
                    maybeClass.map(ci -> recurseAddPairs(ci, Vector.empty()))
                            .getOrElse(Vector.empty())
                            .foldLeft(((Map) value).mapItems, Vector::append);

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
