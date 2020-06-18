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

import io.vavr.collection.Vector;
import lombok.RequiredArgsConstructor;
import uk.modl.ancestry.Parent;
import uk.modl.model.*;

@RequiredArgsConstructor
public class PercentStarInstructionTransform {

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

        if (isStarLoad(ir)) {
            return arr.with(ctx.getAncestry(), ctx.getFilesLoaded()
                    .map(f -> toStringPrimitive(ctx, arr, f)));
        } else if (isStarClass(ir)) {
            return arr.with(ctx.getAncestry(), ctx.getClasses()
                    .map(classInstruction -> classInstructionToArrayItem(ctx, parent, classInstruction))
                    .toVector());
        } else if (isStarMethod(ir)) {
            return arr.with(ctx.getAncestry(), ctx.getMethods()
                    .map(methodInstruction -> methodInstructionToArrayItem(ctx, parent, methodInstruction))
                    .toVector());
        }
        return arr;
    }

    private ArrayItem toStringPrimitive(final TransformationContext ctx, final Array arr, final String f) {
        return StringPrimitive.of(ctx.getAncestry(), arr, f);
    }

    private boolean isStarMethod(final String ir) {
        return "%*method".equals(ir);
    }

    private boolean isStarClass(final String ir) {
        return "%*class".equals(ir);
    }

    private boolean isStarLoad(final String ir) {
        return "%*load".equals(ir);
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

        return resultMap.with(ctx.getAncestry(), Vector.of(pair.with(ctx.getAncestry(), m.getId(), map.with(ctx.getAncestry(), mthdItems))));
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

        return resultMap.with(ctx.getAncestry(), Vector.of(pair.with(ctx.getAncestry(), ci.getId(), Map.of(ctx.getAncestry(), pair, clssItems))));
    }

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param ctx    TransformationContext
     * @param parent Parent
     * @param s      Structure
     * @return the result of function application
     */
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
