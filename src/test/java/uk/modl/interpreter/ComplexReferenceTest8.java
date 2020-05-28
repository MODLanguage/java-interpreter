package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ComplexReferenceTest8 {

    public static final String EXPECTED = "{\"first_number\":1}";
    public static final String INPUT = "_test_vars(one=1;two=2);first_number=%test_vars.one";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}