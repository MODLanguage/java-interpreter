package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ComplexReferenceTest6 {

    public static final String EXPECTED = "{\"object\":{\"print_test\":\"123.test\"}}";
    public static final String INPUT = "_test=123;object(print_test = %test.test)";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}