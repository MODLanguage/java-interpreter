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

    final Ancestry ancestry = new Ancestry();

    final Parent parent = () -> 0;


    private static final Parser parser = new Parser();

    private static final StarLoadTransform starLoadTransform = new StarLoadTransform();

    @Test
    public void test_load_file_successfully() {
        final TransformationContext ctx = TransformationContext.emptyCtx();
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