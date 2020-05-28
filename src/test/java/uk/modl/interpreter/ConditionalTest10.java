package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ConditionalTest10 {

    public static final String EXPECTED = "{\"result\":\"num1 is bigger\"}";
    public static final String INPUT = "_num1=5;_num2=2;result={num1>%num2?num1 is bigger/?num1 is not bigger}";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}