package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ConditionalTest9 {

    public static final String EXPECTED = "{\"result\":\"out\"}";
    public static final String INPUT = "_test=abcdefg;result={{test!=a*}?in/?out}";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}