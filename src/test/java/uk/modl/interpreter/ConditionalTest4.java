package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ConditionalTest4 {

    public static final String EXPECTED = "{\"result\":\"result is false\"}";
    public static final String INPUT = "_test=00;{test?result=result is true/?result=result is false}";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}