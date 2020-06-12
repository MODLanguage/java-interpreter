package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ArrayTest3 {

    public static final String EXPECTED = "{\"x\":[1,null,null,null,null,null,null,[2,null,null,null,null,null,null,null,null,3],null,null,null,null,null,null,null,null,null,[4,null,null,null,null,null,null,null,null,5]]}";

    public static final String INPUT = "x=[1;;;;;;;2:::::::::3;;;;;;;;;;4:::::::::5]";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}