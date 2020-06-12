package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ConditionalTest1 {

    public static final String EXPECTED = "{\"a\":1,\"b\":2,\"c\":true}";
    public static final String INPUT = "a=1;b=2;c={{a=1}|{b=2}?true/?false}";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}