package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ConditionalTest3 {

    public static final String EXPECTED = "{\"result\":true}";
    public static final String INPUT = "_test=01;{test?result=%test}";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}