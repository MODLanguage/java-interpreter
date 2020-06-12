package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ComplexReferenceTest7 {

    public static final String EXPECTED = "{\"country_name\":\"United Kingdom\"}";
    public static final String INPUT = "_C=gb;_COUNTRIES(us=United States;gb=United Kingdom;de=Germany);country_name=%COUNTRIES.%C";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}