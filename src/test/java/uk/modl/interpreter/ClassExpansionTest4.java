package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ClassExpansionTest4 {

    public static final String EXPECTED = "{\"message\":{\"direction\":\"in\",\"date_time\":\"2018-03-22\",\"message\":\"hi\",\"method\":\"sms\"}}";

    public static final String INPUT = "*c(\n" +
            " *i=m;\n" +
            " *n=message;\n" +
            " *S=map;\n" +
            " *a=[\n" +
            "   [direction;date_time;message]\n" +
            " ];\n" +
            " method=sms\n" +
            ");\n" +
            "m=in:2018-03-22:hi";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}