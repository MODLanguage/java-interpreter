package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class SimpleReferenceTest2 {

    public static final String EXPECTED = "{\"out\":\"NotThisOneblahdeblah\"}";
    public static final String INPUT = "_var = NotThisOne;_var=%var%blah;out=%var%deblah";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}