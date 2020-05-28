package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ArrayOfArraysTest {

    public static final String EXPECTED = "{\"x\":[[\"a\",\"b\",\"c\"]]}";
    public static final String INPUT = "x=[[a;b;c]]";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}