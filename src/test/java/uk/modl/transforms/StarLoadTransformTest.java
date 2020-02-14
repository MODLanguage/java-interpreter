package uk.modl.transforms;

import org.junit.Test;
import uk.modl.model.Modl;
import uk.modl.parser.Parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StarLoadTransformTest {
    private static Parser parser = new Parser();
    private static StarLoadTransform starLoadTransform = new StarLoadTransform();

    @Test
    public void test_load_file_successfully() {
        final Modl modl = parser.apply("*l=`./src/test/resources/modl/load_test_1.modl`;c=d");
        assertNotNull(modl);

        final Modl result = starLoadTransform.apply(modl);
        assertNotNull(result);

        final Modl expected = parser.apply("a=b;c=d");
        assertEquals(expected, result);
    }
}