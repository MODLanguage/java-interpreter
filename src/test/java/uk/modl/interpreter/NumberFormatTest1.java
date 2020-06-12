package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class NumberFormatTest1 {

    public static final String EXPECTED = "{\"print\":\"20180921 08:20 2\"}";

    public static final String INPUT = "_var=2;\n*L=\"http://modl.uk/tests/testing.txt!\";\nprint=%update_date";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}