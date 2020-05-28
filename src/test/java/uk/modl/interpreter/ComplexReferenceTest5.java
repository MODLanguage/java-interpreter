package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ComplexReferenceTest5 {

    public static final String EXPECTED = "{\"name\":\"пример\",\"department\":\"обслуживание клиентов\"}";
    public static final String INPUT = "?=`e1afmkfd`:` -7sbcecqbdsccxfizhcp6b8ah`;(name=%0.p;department=%1.p)";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}