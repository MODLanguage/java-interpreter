package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ComplexReferenceTest9 {

    public static final String EXPECTED = "{\"a\":{\"b\":{\"c\":{\"d\":{\"e\":{\"f\":1}}}}},\"testing\":1}";
    public static final String INPUT = "a(b(c(d(e(f=1)))));testing=%a.b.c.d.e.f";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}