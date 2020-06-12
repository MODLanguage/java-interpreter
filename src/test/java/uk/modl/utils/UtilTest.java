/*
 * MIT License
 *
 * Copyright (c) 2020 NUM Technology Ltd
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package uk.modl.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class UtilTest {

    public static final String TEST_TEXT = "Some test text.";

    @Test(expected = RuntimeException.class)
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

    @Test
    public void testUnwrapLiteral_1() {
        assertNull(Util.unwrapLiteral(null));
        assertEquals("", Util.unwrapLiteral(""));
        assertEquals("test", Util.unwrapLiteral("test"));
        assertEquals("%test", Util.unwrapLiteral("%test"));
        assertEquals("`%test", Util.unwrapLiteral("`%test"));
        assertEquals("`%test", Util.unwrapLiteral("%`%test"));
        assertEquals("%test", Util.unwrapLiteral("%`%test`"));

        assertEquals("%test%", Util.unwrapLiteral("%`%test%`"));
        assertEquals("test%", Util.unwrapLiteral("%`test%`"));
        assertEquals("%hello %test world", Util.unwrapLiteral("%`%hello %test world`"));
    }

}