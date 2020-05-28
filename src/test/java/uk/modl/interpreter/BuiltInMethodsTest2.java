package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class BuiltInMethodsTest2 {

    public static final String EXPECTED = "{\"test\":\"Blah huzzahne t\"}";
    public static final String INPUT = "?=one:two;test=Blah %0.r<o,huzzah> %1.t<w>";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}