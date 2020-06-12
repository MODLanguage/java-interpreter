package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class NonASCIITests {

    public static final String EXPECTED = "{\"å∫ç∂´´©\":\"œ∑´´¥\"}";
    public static final String INPUT = "å∫ç∂´´©=œ∑´´¥";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}