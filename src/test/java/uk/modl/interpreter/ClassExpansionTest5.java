package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ClassExpansionTest5 {

    public static final String EXPECTED = "{\"alpha\":{\"value\":1,\"x\":\"test\"}}";

    public static final String INPUT = "*class(*id=a;*name=alpha;x=\"test\");a=1";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}