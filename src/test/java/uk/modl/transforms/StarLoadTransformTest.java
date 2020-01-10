package uk.modl.transforms;

import io.vavr.control.Either;
import org.junit.Test;
import uk.modl.error.Error;
import uk.modl.model.Modl;
import uk.modl.parser.Parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StarLoadTransformTest {
    private static Parser parser = new Parser();
    private static StarLoadTransform starLoadTransform = new StarLoadTransform();

    @Test
    public void test_load_file_successfully() {
        final Either<Error, Modl> modl = parser.apply("*l=`./src/test/resources/modl/load_test_1.modl`");
        assertTrue(modl.isRight());

        final Either<Error, Modl> result = starLoadTransform.apply(modl);
        assertTrue(result.isRight());

        final Either<Error, Modl> expected = parser.apply("a=b");
        assertEquals(expected.get(), modl.get());
    }
}