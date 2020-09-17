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

import io.vavr.collection.HashMap;
import io.vavr.collection.Vector;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.ancestry.Ancestry;
import uk.modl.ancestry.Parent;
import uk.modl.model.Map;
import uk.modl.model.Pair;
import uk.modl.model.StringPrimitive;

import java.net.MalformedURLException;
import java.net.URL;

public class ClassExpansionTransformTest2 {

    final Ancestry ancestry = new Ancestry();

    final Parent parent = () -> 0;


    @Test
    public void testAssigns() throws MalformedURLException {
        final TransformationContext ctx = addClasses(TransformationContext.baseCtx(new URL("file:///"), 10000));

        final ClassExpansionTransform transform = new ClassExpansionTransform();

        final Pair updatedPair = (Pair) transform.apply(ctx, Pair.of(ancestry, parent, "d", Map.of(ancestry, parent, Vector.of(Pair.of(ancestry, parent, "test1", StringPrimitive.of(ancestry, parent, "test2"))))));
        Assert.assertNotNull(updatedPair);
        Assert.assertEquals("delta", updatedPair.getKey());
        Assert.assertTrue(updatedPair.getValue() instanceof Map);

        final Map m = (Map) updatedPair.getValue();

        Assert.assertEquals(5, m.getMapItems()
                .size());
    }

    private TransformationContext addClasses(TransformationContext ctx) {
        {
            final io.vavr.collection.Map<String, Pair> pairs = HashMap.of("v", Pair.of(ancestry, parent, "v", StringPrimitive.of(ancestry, parent, "victor")));

            final StarClassTransform.ClassInstruction classInstruction = StarClassTransform.ClassInstruction.of("a", "alpha", "map", Vector.empty(), pairs);
            ctx = ctx.addClassInstruction(classInstruction);
        }
        {
            final io.vavr.collection.Map<String, Pair> pairs = HashMap.of("w", Pair.of(ancestry, parent, "w", StringPrimitive.of(ancestry, parent, "whisky")));

            final StarClassTransform.ClassInstruction classInstruction = StarClassTransform.ClassInstruction.of("b", "bravo", "alpha", Vector.empty(), pairs);
            ctx = ctx.addClassInstruction(classInstruction);
        }
        {
            final io.vavr.collection.Map<String, Pair> pairs = HashMap.of("x", Pair.of(ancestry, parent, "x", StringPrimitive.of(ancestry, parent, "xray")));

            final StarClassTransform.ClassInstruction classInstruction = StarClassTransform.ClassInstruction.of("c", "charlie", "bravo", Vector.empty(), pairs);
            ctx = ctx.addClassInstruction(classInstruction);
        }
        {
            final io.vavr.collection.Map<String, Pair> pairs = HashMap.of("y", Pair.of(ancestry, parent, "y", StringPrimitive.of(ancestry, parent, "yankee")));

            final StarClassTransform.ClassInstruction classInstruction = StarClassTransform.ClassInstruction.of("d", "delta", "charlie", Vector.empty(), pairs);
            ctx = ctx.addClassInstruction(classInstruction);
        }
        return ctx;
    }

}