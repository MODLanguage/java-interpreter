package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ObjectIndexReferenceTest1 {

    public static final String EXPECTED = "{\"letters\":[\"a\",\"b\",\"c\"],\"letters2\":\"defdef\"}";
    public static final String INPUT = "?[[a;b;c];d;e;f];letters=%0;letters2=%1%%2%%3%%1%%2%%3%";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}