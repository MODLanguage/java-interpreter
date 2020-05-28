package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ComplexReferenceTest2 {

    public static final String EXPECTED = "{\"z\":true}";
    public static final String INPUT = "_x=a:b:c:d;_y=[1;2;3;4];z={%x.1=b&%y.2=3?}";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}