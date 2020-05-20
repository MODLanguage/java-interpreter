package uk.modl.transforms;

import io.vavr.Function1;
import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import uk.modl.model.*;

@RequiredArgsConstructor
public class PercentStarInstructionTransform implements Function1<Structure, Structure> {

    /**
     * The context for this invocation of the interpreter
     */
    @NonNull
    @Setter
    private TransformationContext ctx;

    /**
     * Replace if necessary
     *
     * @param vi a ValueItem
     * @return a ValueItem
     */
    public PairValue apply(final PairValue vi) {
        if (vi instanceof StringPrimitive) {
            final String s = ((StringPrimitive) vi).getValue();
            if (s.startsWith("%*")) {
                return instructionToReferencedItems(s);
            }
        }
        return vi;
    }

    private Array instructionToReferencedItems(final String ir) {
        if ("%*load".equals(ir)) {
            return new Array(ctx.getFilesLoaded()
                    .map(f -> (ArrayItem) new StringPrimitive(f)));
        } else if ("%*class".equals(ir)) {
            return new Array(ctx.getClasses()
                    .map(this::classInstructionToArrayItem)
                    .toVector());
        } else if ("%*method".equals(ir)) {
            return new Array(ctx.getMethods()
                    .map(this::methodInstructionToArrayItem)
                    .toVector());
        }
        return new Array(Vector.empty());
    }

    /**
     * Convert a StarMethodTransform.MethodInstruction to an ArrayItem
     *
     * @param m a StarMethodTransform.MethodInstruction
     * @return an ArrayItem
     */
    private ArrayItem methodInstructionToArrayItem(final StarMethodTransform.MethodInstruction m) {
        Vector<MapItem> mthdItems = Vector.empty();
        final Pair transformPair = new Pair("transform", new StringPrimitive(m.transform));
        if (m.name != null) {
            final Pair namePair = new Pair("name", new StringPrimitive(m.name));
            mthdItems = mthdItems.append(namePair);
        }
        mthdItems = mthdItems.append(transformPair);


        final MapItem mthdMap = new Pair(m.id, new uk.modl.model.Map(mthdItems));
        final Vector<MapItem> mthd = Vector.of(mthdMap);
        return new uk.modl.model.Map(mthd);
    }

    /**
     * Convert a StarClassTransform.ClassInstruction to an ArrayItem
     *
     * @param ci StarClassTransform.ClassInstruction
     * @return an ArrayItem
     */
    private ArrayItem classInstructionToArrayItem(final StarClassTransform.ClassInstruction ci) {

        Vector<MapItem> clssItems = Vector.empty();

        if (ci.getName() != null) {
            final Pair p = new Pair("name", new StringPrimitive(ci.getName()));
            clssItems = clssItems.append(p);
        }

        {
            final Pair p = new Pair("superclass", new StringPrimitive(ci.getSuperclass()));
            clssItems = clssItems.append(p);
        }

        if (ci.getAssign()
                .nonEmpty()) {
            final Pair p = new Pair("assign", new Array(ci.getAssign()));
            clssItems = clssItems.append(p);
        }

        if (ci.getPairs() != null) {
            clssItems = clssItems.appendAll(ci.getPairs());
        }

        final MapItem clssMap = new Pair(ci.getId(), new uk.modl.model.Map(clssItems));
        final Vector<MapItem> clss = Vector.of(clssMap);
        return new uk.modl.model.Map(clss);
    }

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param s argument 1
     * @return the result of function application
     */
    @Override
    public Structure apply(final Structure s) {
        if (s instanceof Pair) {
            final Pair pair = (Pair) s;
            final PairValue newValue = apply(pair.getValue());
            if (newValue != pair.getValue()) {
                return new Pair(pair.getKey(), newValue);
            }
        }
        return s;
    }

}
