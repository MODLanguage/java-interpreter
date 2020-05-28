package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ConditionalTest14 {

    public static final String EXPECTED = "{\"result\":\"no\"}";

    public static final String INPUT = "?=0:1:2;\nresult={\n%1>1?\n yes\n/?\n no\n}";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}