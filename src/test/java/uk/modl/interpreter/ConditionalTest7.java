package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ConditionalTest7 {

    public static final String EXPECTED = "{\"result\":\"yes\"}";
    public static final String INPUT = "_test=1;result={%_test=1?yes/?no}";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}