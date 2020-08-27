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

package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

import java.net.MalformedURLException;

public class ObjectIndexReferenceTest2 {

    public static final String EXPECTED = "{\"test1\":\"one\",\"test2\":\"two\",\"test3\":\"three\",\"test4\":\"%3\",\"test5\":\"%4\",\"test6\":\"%5\",\"test7\":\"%6\",\"test8\":\"%7\",\"test9\":\"%8\"}";

    public static final String INPUT = "?=one:two:three;test1=%0;test2=%1;test3=%2;test4=%3;test5=%4;test6=%5;test7=%6;test8=%7;test9=%8";

    @Test
    public void parseOk() throws MalformedURLException {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}