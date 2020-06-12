package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class SimpleReferenceTest1 {

    public static final String EXPECTED = "{\"msg\":\"Hello World\"}";
    public static final String INPUT = "_var1=Hello;_var2=World;msg=%var1 %var2";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}