package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ComplexReferenceTest3 {

    public static final String EXPECTED = "{\"x\":true}";
    public static final String INPUT = "_x=[1;2;3;4];_y=[1;2;3;4];x={%x.2=%y.2?}";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}