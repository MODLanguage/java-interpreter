package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ConditionalTest12 {

    public static final String EXPECTED = "{\"result\":\"it's green\"}";

    public static final String INPUT = "_colour = green;\n" +
            "_test = { colour=green? true /? false }; \n" +
            "\n" +
            "{\n" +
            " !test?\n" +
            "   result=it's not green\n" +
            " /?\n" +
            "   result=it's green\n" +
            "}";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}