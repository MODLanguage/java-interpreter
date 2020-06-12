package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ConditionalTest2 {

    public static final String EXPECTED = "{\"result\":\"test is defined\"}";
    public static final String INPUT = "_test=abc;{test?result=test is defined/?result=test is not defined}";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}