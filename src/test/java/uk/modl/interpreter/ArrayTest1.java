package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ArrayTest1 {

    public static final String EXPECTED = "[\"one\",\"two\",\"three\"]";

    public static final String INPUT = "[one;two;three]";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}