package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ConditionalTest11 {

    public static final String EXPECTED = "{\"test\":1}";
    public static final String INPUT = "{01?test=1}";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}