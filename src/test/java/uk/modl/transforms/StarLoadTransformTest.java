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

import io.vavr.Tuple2;
import io.vavr.collection.Vector;
import org.junit.Test;
import uk.modl.ancestry.Ancestry;
import uk.modl.ancestry.Parent;
import uk.modl.model.*;
import uk.modl.parser.Parser;

import static org.junit.Assert.*;

public class StarLoadTransformTest {

    private static final Parser parser = new Parser();

    private static final StarLoadTransform starLoadTransform = new StarLoadTransform();

    final Ancestry ancestry = new Ancestry();

    final Parent parent = () -> 0;

    @Test
    public void test_load_file_successfully() {
        final TransformationContext ctx = TransformationContext.baseCtx(null);
        final Modl modl = parser.apply("*l=`./src/test/resources/modl/load_test_1.modl`;c=d", ctx.getAncestry());
        assertNotNull(modl);

        final Tuple2<TransformationContext, Structure> result = starLoadTransform.apply(ctx, modl.getStructures()
                .get(0));
        assertNotNull(result);

        final Pair expected = Pair.of(ancestry, parent, "*l", Array.of(ancestry, parent, Vector.of(Pair.of(ancestry, parent, "a", StringPrimitive.of(ancestry, parent, "b")))));
        final Pair resultPair = (Pair) result._2;

        assertEquals(expected.getKey(), resultPair.getKey());
        assertEquals(expected.getValue()
                .toString(), resultPair.getValue()
                .toString());
    }

}