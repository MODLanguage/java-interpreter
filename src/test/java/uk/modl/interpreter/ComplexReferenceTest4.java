package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ComplexReferenceTest4 {

    public static final String EXPECTED = "{\"name\":\"пример\",\"department\":\"обслуживание клиентов\"}";
    public static final String INPUT = "(name=%`e1afmkfd`.p;department=%` -7sbcecqbdsccxfizhcp6b8ah`.p)";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}