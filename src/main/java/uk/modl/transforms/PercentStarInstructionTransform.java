package uk.modl.transforms;

import io.vavr.Function3;
import io.vavr.collection.Vector;
import lombok.RequiredArgsConstructor;
import uk.modl.ancestry.Parent;
import uk.modl.model.*;

@RequiredArgsConstructor
public class PercentStarInstructionTransform implements Function3<TransformationContext, Parent, Structure, Structure> {

    /**
     * Replace if necessary
     *
     * @param ctx    TransformationContext
     * @param parent a Parent
     * @param vi     a ValueItem
     * @return a ValueItem
     */
    public PairValue apply(final TransformationContext ctx, final Parent parent, final PairValue vi) {
        if (vi instanceof StringPrimitive) {
            final String s = ((StringPrimitive) vi).getValue();
            if (s.startsWith("%*")) {
                return instructionToReferencedItems(ctx, parent, s);
            }
        }
        return vi;
    }

    private Array instructionToReferencedItems(final TransformationContext ctx, final Parent parent, final String ir) {
        final Array arr = Array.of(ctx.getAncestry(), parent, Vector.empty());

        if ("%*load".equals(ir)) {
            return arr.with(ctx.getAncestry(), ctx.getFilesLoaded()
                    .map(f -> (ArrayItem) StringPrimitive.of(ctx.getAncestry(), arr, f)));
        } else if ("%*class".equals(ir)) {
            return arr.with(ctx.getAncestry(), ctx.getClasses()
                    .map(classInstruction -> classInstructionToArrayItem(ctx, parent, classInstruction))
                    .toVector());
        } else if ("%*method".equals(ir)) {
            return arr.with(ctx.getAncestry(), ctx.getMethods()
                    .map(methodInstruction -> methodInstructionToArrayItem(ctx, parent, methodInstruction))
                    .toVector());
        }
        return arr;
    }

    /**
     * Convert a StarMethodTransform.MethodInstruction to an ArrayItem
     *
     * @param m a StarMethodTransform.MethodInstruction
     * @return an ArrayItem
     */
    private ArrayItem methodInstructionToArrayItem(final TransformationContext ctx, final Parent parent, final StarMethodTransform.MethodInstruction m) {
        Vector<MapItem> mthdItems = Vector.empty();

        final Map resultMap = Map.of(ctx.getAncestry(), parent, Vector.empty());

        final Pair pair = Pair.of(ctx.getAncestry(), resultMap, "", NullPrimitive.instance);

        final Map map = Map.of(ctx.getAncestry(), pair, Vector.empty());

        final Pair transformPair = pair.with(ctx.getAncestry(), "transform", StringPrimitive.of(ctx.getAncestry(), pair, m.getTransform()));
        if (m.getName() != null) {
            final Pair namePair = Pair.of(ctx.getAncestry(), map, "name", NullPrimitive.instance);
            final Pair newNamePair = namePair.with(ctx.getAncestry(), "name", StringPrimitive.of(ctx.getAncestry(), namePair, m.getName()));
            mthdItems = mthdItems.append(newNamePair);
        }
        mthdItems = mthdItems.append(transformPair);

        final MapItem mthdMap = pair.with(ctx.getAncestry(), m.getId(), map.with(ctx.getAncestry(), mthdItems));
        final Vector<MapItem> mthd = Vector.of(mthdMap);

        return resultMap.with(ctx.getAncestry(), mthd);
    }

    /**
     * Convert a StarClassTransform.ClassInstruction to an ArrayItem
     *
     * @param ci StarClassTransform.ClassInstruction
     * @return an ArrayItem
     */
    private ArrayItem classInstructionToArrayItem(final TransformationContext ctx, final Parent parent, final StarClassTransform.ClassInstruction ci) {
        final Map resultMap = Map.of(ctx.getAncestry(), parent, Vector.empty());
        final Pair pair = Pair.of(ctx.getAncestry(), resultMap, "", NullPrimitive.instance);

        Vector<MapItem> clssItems = Vector.empty();

        if (ci.getName() != null) {
            final Pair p = Pair.of(ctx.getAncestry(), pair, "name", NullPrimitive.instance);

            clssItems = clssItems.append(p.with(ctx.getAncestry(), "name", StringPrimitive.of(ctx.getAncestry(), p, ci.getName())));
        }

        {
            final Pair p = Pair.of(ctx.getAncestry(), pair, "superclass", NullPrimitive.instance);
            clssItems = clssItems.append(p.with(ctx.getAncestry(), "superclass", StringPrimitive.of(ctx.getAncestry(), p, ci.getSuperclass())));
        }

        if (ci.getAssign()
                .nonEmpty()) {

            final Pair p = Pair.of(ctx.getAncestry(), pair, "assign", NullPrimitive.instance);
            clssItems = clssItems.append(p.with(ctx.getAncestry(), "assign", Array.of(ctx.getAncestry(), p, ci.getAssign())));
        }

        if (ci.getPairs() != null) {
            clssItems = clssItems.appendAll(ci.getPairs()
                    .values());
        }

        final MapItem clssMap = pair.with(ctx.getAncestry(), ci.getId(), uk.modl.model.Map.of(ctx.getAncestry(), pair, clssItems));
        final Vector<MapItem> clss = Vector.of(clssMap);
        return resultMap.with(ctx.getAncestry(), clss);
    }

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param s argument 1
     * @return the result of function application
     */
    @Override
    public Structure apply(final TransformationContext ctx, final Parent parent, final Structure s) {
        if (s instanceof Pair) {
            final Pair pair = (Pair) s;
            final PairValue newValue = apply(ctx, parent, pair.getValue());
            if (newValue != pair.getValue()) {
                return pair.with(ctx.getAncestry(), pair.getKey(), newValue);
            }
        }
        return s;
    }

}
