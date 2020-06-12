package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ArrayConditionalTest1 {

    public static final String EXPECTED = "{\"country\":\"gb\",\"x\":[\"this\"]}";
    public static final String INPUT = "country=gb;x=[{ country=gb ? this /country=us? that }]";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}