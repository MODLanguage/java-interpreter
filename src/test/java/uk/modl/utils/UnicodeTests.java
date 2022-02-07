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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UnicodeTests {

    @Test
    public void testUnicodeSlashes() {
        assertEquals(StringEscapeReplacer.replace("\\u0022b\\u0022"), "\"b\"");
    }

    @Test
    public void testUnicodeTildes() {
        assertEquals(StringEscapeReplacer.replace("~u0022b~u0022"), "\"b\"");
    }

    @Test
    public void testReplaceUnicode() {
        assertEquals(StringEscapeReplacer.replace("~u022b~u022b"), "ȫȫ");
    }

    @Test
    public void testReplaceUnicodeUtf16() {
        assertEquals(StringEscapeReplacer.replace(
                        "~u00ae~ud83d~ude00~ud83d~udc41~ufe0f~u200d~ud83d~udde8~ufe0f0hello~u01c5~ud83d~udd25"),
                "®\uD83D\uDE00\uD83D\uDC41️\u200D\uD83D\uDDE8️0helloǅ\uD83D\uDD25");
    }

    @Test
    public void testReplaceUnicodeUtf16_2() {
        assertEquals(StringEscapeReplacer.replace("~ua3300"), "ꌰ0");
    }

    @Test
    public void testIgnoreEscapedSequences() {
        assertEquals(StringEscapeReplacer.replace("\\\\~u00ae~ud83d~ude00~ud83d~udc41~ufe0f~u200d~ud83d~udde8"
                + "~ufe0f0hello~u01c5\\\\~ud83d\\\\~udd250000"), "~u00ae\uD83D\uDE00\uD83D\uDC41️\u200D\uD83D\uDDE8"
                + "️0helloǅ~ud83d~udd250000");
    }

}
