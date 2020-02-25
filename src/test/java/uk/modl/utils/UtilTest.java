package uk.modl.utils;

import org.junit.Assert;
import org.junit.Test;
import uk.modl.parser.errors.InterpreterError;

public class UtilTest {

    public static final String TEST_TEXT = "Some test text.";

    @Test(expected = InterpreterError.class)
    public void replacerTest_1() {
        final String result = Util.replacer("", TEST_TEXT);
        Assert.assertEquals(TEST_TEXT, result);
    }

    @Test
    public void replacerTest_2() {
        final String result = Util.replacer("r<test,more>", TEST_TEXT);
        Assert.assertEquals("Some more text.", result);
    }

    @Test
    public void replacerTest_3() {
        final String result = Util.replacer("r<test,``>", TEST_TEXT);
        Assert.assertEquals("Some  text.", result);
    }

    @Test
    public void trimmerTest_1() {
        final String result = Util.trimmer("t<test>", TEST_TEXT);
        Assert.assertEquals("Some ", result);

    }
}