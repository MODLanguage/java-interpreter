package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ConditionalTest8 {

    public static final String EXPECTED = "{\"o\":{\"test1\":123,\"test2\":456}}";
    public static final String INPUT = "_C=gb;o({C=gb?test1=123};test2=456)";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}