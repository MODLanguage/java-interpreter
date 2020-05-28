package uk.modl.utils;

import org.junit.Test;
import uk.modl.parser.errors.InterpreterError;

import static org.junit.Assert.*;

public class UtilTest {

    public static final String TEST_TEXT = "Some test text.";

    @Test(expected = InterpreterError.class)
    public void replacerTest_1() {
        final String result = Util.replacer("", TEST_TEXT);
        assertEquals(TEST_TEXT, result);
    }

    @Test
    public void replacerTest_2() {
        final String result = Util.replacer("r<test,more>", TEST_TEXT);
        assertEquals("Some more text.", result);
    }

    @Test
    public void replacerTest_3() {
        final String result = Util.replacer("r<test,``>", TEST_TEXT);
        assertEquals("Some  text.", result);
    }

    @Test
    public void trimmerTest_1() {
        final String result = Util.trimmer("t<test>", TEST_TEXT);
        assertEquals("Some ", result);

    }

    @Test
    public void unquoteTest() {
        final String result = Util.unquote("`a string including a quote from Winston: \"We'll fight them on the beaches\"`");
        assertEquals("a string including a quote from Winston: \"We'll fight them on the beaches\"", result);
    }

}