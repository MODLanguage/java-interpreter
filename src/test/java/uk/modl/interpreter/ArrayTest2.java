package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ArrayTest2 {

    public static final String EXPECTED = "{\"o\":[1,2,null,4,5]}";

    public static final String INPUT = "o=1:2::4:5";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}