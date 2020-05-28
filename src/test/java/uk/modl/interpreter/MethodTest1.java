package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class MethodTest1 {

    public static final String EXPECTED = "{\"friendly_name\":\"Smiths Limited\"}";

    public static final String INPUT = "*method(\n" +
            " *id=cn;\n" +
            " *name=company_name;\n" +
            " *transform=replace<-, >.trim<.>.initcap\n" +
            ");\n" +
            "\n" +
            "_domain = smiths-limited.com;\n" +
            "friendly_name = %domain.cn";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}