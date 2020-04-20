package uk.modl.transforms;

import io.vavr.collection.Vector;
import org.junit.Test;
import uk.modl.model.Array;
import uk.modl.model.Modl;
import uk.modl.model.Pair;
import uk.modl.model.StringPrimitive;
import uk.modl.parser.Parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StarLoadTransformTest {
    private static final Parser parser = new Parser();
    private static final TransformationContext ctx = new TransformationContext();
    private static final StarLoadTransform starLoadTransform = new StarLoadTransform(ctx);

    @Test
    public void test_load_file_successfully() {
        final Modl modl = parser.apply("*l=`./src/test/resources/modl/load_test_1.modl`;c=d");
        assertNotNull(modl);

        final Pair result = starLoadTransform.apply((Pair) modl.getStructures()
                .get(0));
        assertNotNull(result);

        final Pair expected = new Pair("*l", new Array(Vector.of(new Pair("a", new StringPrimitive("b")))));
        assertEquals(expected, result);
    }
}