package uk.modl.transforms;

import io.vavr.collection.Vector;
import org.junit.Test;
import uk.modl.model.*;
import uk.modl.parser.Parser;

import static org.junit.Assert.*;

public class StarLoadTransformTest {

    private static final Parser parser = new Parser();

    private static final TransformationContext ctx = new TransformationContext();

    private static final StarLoadTransform starLoadTransform = new StarLoadTransform(ctx);

    @Test
    public void test_load_file_successfully() {
        final Modl modl = parser.apply("*l=`./src/test/resources/modl/load_test_1.modl`;c=d");
        assertNotNull(modl);

        final Structure result = starLoadTransform.apply(modl.getStructures()
                .get(0));
        assertNotNull(result);

        final Pair expected = new Pair("*l", new Array(Vector.of(new Pair("a", new StringPrimitive("b")))));
        assertEquals(expected, result);
    }

}