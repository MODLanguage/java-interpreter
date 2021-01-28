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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import io.vavr.Tuple2;
import io.vavr.collection.Vector;
import uk.modl.ancestry.Ancestry;
import uk.modl.ancestry.Parent;
import uk.modl.model.Array;
import uk.modl.model.Modl;
import uk.modl.model.Pair;
import uk.modl.model.StringPrimitive;
import uk.modl.model.Structure;
import uk.modl.parser.Parser;
import uk.modl.parser.errors.RecursiveFileLoadException;

public class StarLoadTransformTest {

  private static final Parser parser = new Parser();

  private StarLoadTransform starLoadTransform = null;

  public static final int TIMEOUT_SECONDS = 10000;

  final Ancestry ancestry = new Ancestry();

  final Parent parent = () -> 0;

  @Before
  public void before() {
    starLoadTransform = new StarLoadTransform();
  }

  @Test
  public void test_load_file_successfully() {
    final TransformationContext ctx = TransformationContext.baseCtx(null, TIMEOUT_SECONDS);
    final Modl modl = parser.apply("*l=`./src/test/resources/modl/load_test_1.modl`;c=d", ctx.getAncestry(),
        TIMEOUT_SECONDS);
    assertNotNull(modl);

    final Tuple2<TransformationContext, Structure> result = starLoadTransform.apply(ctx, modl.getStructures().get(0));
    assertNotNull(result);

    final Pair expected = Pair.of(ancestry, parent, "*l", Array.of(ancestry, parent,
        Vector.of(Pair.of(ancestry, parent, "a", StringPrimitive.of(ancestry, parent, "b")))));
    final Pair resultPair = (Pair) result._2;

    assertEquals(expected.getKey(), resultPair.getKey());
    assertEquals(expected.getValue().toString(), resultPair.getValue().toString());
  }

  @Test(expected = RecursiveFileLoadException.class)
  public void test_recursion_prevention() {
    final TransformationContext ctx = TransformationContext.baseCtx(null, TIMEOUT_SECONDS);
    final Modl modl = parser.apply("*l=`./src/test/resources/modl/load_test_2.modl`;c=d", ctx.getAncestry(),
        TIMEOUT_SECONDS);

    starLoadTransform.apply(ctx, modl.getStructures().get(0));

  }

  @Test
  public void test_that_loading_the_same_file_twice_is_accepted() {
    final TransformationContext ctx = TransformationContext.baseCtx(null, TIMEOUT_SECONDS);
    final Modl modl = parser.apply("*l=`./src/test/resources/modl/load_test_3.modl`;c=d", ctx.getAncestry(),
        TIMEOUT_SECONDS);

    final Tuple2<TransformationContext, Structure> result = starLoadTransform.apply(ctx, modl.getStructures().get(0));
    assertNotNull(result);

    final Array expected = Array.of(ancestry, parent,
        Vector.of(
            Pair.of(ancestry, parent, "*load",
                Array.of(ancestry, parent,
                    Vector.of(Pair.of(ancestry, parent, "a", StringPrimitive.of(ancestry, parent, "b"))))),
            Pair.of(ancestry, parent, "a", StringPrimitive.of(ancestry, parent, "b"))));

    final Pair resultPair = (Pair) result._2;

    assertEquals(expected.toString(), resultPair.getValue().toString());

  }

}