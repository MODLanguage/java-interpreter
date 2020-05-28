package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class SimpleReferenceTest3 {

    public static final String EXPECTED = "{\"print\":123}";
    public static final String INPUT = "_test=123;print=%_test";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}