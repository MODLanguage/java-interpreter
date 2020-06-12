package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ObjectIndexReferenceTest3 {

    public static final String EXPECTED = "{\"test\":\"Blah One Two\"}";
    public static final String INPUT = "?=one:two;test=Blah %0.s %1.s";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}