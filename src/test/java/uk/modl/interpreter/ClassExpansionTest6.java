package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ClassExpansionTest6 {

    public static final String EXPECTED = "{\"alpha\":[\"b\"]}";

    public static final String INPUT = "*class(*id=a;*name=alpha;*superclass=arr);a=b";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}