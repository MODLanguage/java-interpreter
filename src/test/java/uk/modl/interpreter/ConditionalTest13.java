package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ConditionalTest13 {

    public static final String EXPECTED = "{\"result\":\"test is not defined\"}";

    public static final String INPUT = "{\n" +
            " !test?\n" +
            "   result=test is not defined\n" +
            " /?\n" +
            "   result=test is defined\n" +
            "}";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}