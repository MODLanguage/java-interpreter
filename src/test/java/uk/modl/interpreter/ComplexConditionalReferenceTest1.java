package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ComplexConditionalReferenceTest1 {

    public static final String EXPECTED = "{\"test\":\"yes\"}";
    public static final String INPUT = "_array[1;2];_array_item=%array.0;test={array_item=%array.0?yes/?no}";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}