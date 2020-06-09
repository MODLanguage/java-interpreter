package uk.modl.transforms;

import io.vavr.Tuple2;
import io.vavr.collection.Vector;
import org.junit.Test;
import uk.modl.model.*;
import uk.modl.parser.Parser;

import static org.junit.Assert.*;

public class StarLoadTransformTest {

    private static final Parser parser = new Parser();

    private static final StarLoadTransform starLoadTransform = new StarLoadTransform();

    @Test
    public void test_load_file_successfully() {
        final Modl modl = parser.apply("*l=`./src/test/resources/modl/load_test_1.modl`;c=d");
        assertNotNull(modl);

        final Tuple2<TransformationContext, Structure> result = starLoadTransform.apply(TransformationContext.emptyCtx(), modl.getStructures()
                .get(0));
        assertNotNull(result);

        final Pair expected = Pair.of("*l", Array.of(Vector.of(Pair.of("a", StringPrimitive.of("b")))));
        final Pair resultPair = (Pair) result._2;

        assertEquals(expected.getKey(), resultPair.getKey());
        assertEquals(expected.getValue()
                .toString(), resultPair.getValue()
                .toString());
    }

}