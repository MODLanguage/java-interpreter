package uk.modl.transforms;

import io.vavr.Function2;
import io.vavr.collection.Vector;
import lombok.RequiredArgsConstructor;
import uk.modl.model.*;

@RequiredArgsConstructor
public class PercentStarInstructionTransform implements Function2<TransformationContext, Structure, Structure> {

    /**
     * Replace if necessary
     *
     * @param ctx TransformationContext
     * @param vi  a ValueItem
     * @return a ValueItem
     */
    public PairValue apply(final TransformationContext ctx, final PairValue vi) {
        if (vi instanceof StringPrimitive) {
            final String s = ((StringPrimitive) vi).getValue();
            if (s.startsWith("%*")) {
                return instructionToReferencedItems(ctx, s);
            }
        }
        return vi;
    }

    private Array instructionToReferencedItems(final TransformationContext ctx, final String ir) {
        if ("%*load".equals(ir)) {
            return Array.of(ctx.getFilesLoaded()
                    .map(f -> (ArrayItem) StringPrimitive.of(f)));
        } else if ("%*class".equals(ir)) {
            return Array.of(ctx.getClasses()
                    .map(this::classInstructionToArrayItem)
                    .toVector());
        } else if ("%*method".equals(ir)) {
            return Array.of(ctx.getMethods()
                    .map(this::methodInstructionToArrayItem)
                    .toVector());
        }
        return Array.of(Vector.empty());
    }

    /**
     * Convert a StarMethodTransform.MethodInstruction to an ArrayItem
     *
     * @param m a StarMethodTransform.MethodInstruction
     * @return an ArrayItem
     */
    private ArrayItem methodInstructionToArrayItem(final StarMethodTransform.MethodInstruction m) {
        Vector<MapItem> mthdItems = Vector.empty();
        final Pair transformPair = Pair.of("transform", StringPrimitive.of(m.getTransform()));
        if (m.getName() != null) {
            final Pair namePair = Pair.of("name", StringPrimitive.of(m.getName()));
            mthdItems = mthdItems.append(namePair);
        }
        mthdItems = mthdItems.append(transformPair);


        final MapItem mthdMap = Pair.of(m.getId(), uk.modl.model.Map.of(mthdItems));
        final Vector<MapItem> mthd = Vector.of(mthdMap);
        return uk.modl.model.Map.of(mthd);
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
            final Pair p = Pair.of("name", StringPrimitive.of(ci.getName()));
            clssItems = clssItems.append(p);
        }

        {
            final Pair p = Pair.of("superclass", StringPrimitive.of(ci.getSuperclass()));
            clssItems = clssItems.append(p);
        }

        if (ci.getAssign()
                .nonEmpty()) {
            final Pair p = Pair.of("assign", Array.of(ci.getAssign()));
            clssItems = clssItems.append(p);
        }

        if (ci.getPairs() != null) {
            clssItems = clssItems.appendAll(ci.getPairs()
                    .values());
        }

        final MapItem clssMap = Pair.of(ci.getId(), uk.modl.model.Map.of(clssItems));
        final Vector<MapItem> clss = Vector.of(clssMap);
        return uk.modl.model.Map.of(clss);
    }

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param s argument 1
     * @return the result of function application
     */
    @Override
    public Structure apply(final TransformationContext ctx, final Structure s) {
        if (s instanceof Pair) {
            final Pair pair = (Pair) s;
            final PairValue newValue = apply(ctx, pair.getValue());
            if (newValue != pair.getValue()) {
                return Pair.of(pair.getKey(), newValue);
            }
        }
        return s;
    }

}
