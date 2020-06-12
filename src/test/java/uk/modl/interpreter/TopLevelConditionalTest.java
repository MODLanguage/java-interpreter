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

public class TopLevelConditionalTest {

    public static final String EXPECTED_1 = "{\"result\":\"match\"}";

    public static final String EXPECTED_2 = "{\"result\":\"nomatch\"}";

    public static final String EXPECTED_3 = "{\"result\":\"match\"}";

    public static final String EXPECTED_4 = "{\"result\":\"nomatch\"}";

    public static final String INPUT_1 = "_test1=one;_one=two;{test1=`one`?result=match/?result=nomatch}";

    public static final String INPUT_2 = "_test1=one;_one=two;{test1!=`one`?result=match/?result=nomatch}";

    public static final String INPUT_3 = "_test1=one;_one=two;{test1=\"one\"?result=match/?result=nomatch}";

    public static final String INPUT_4 = "_test1=one;_one=two;{test1=two?result=match/?result=nomatch}";

    @Test
    public void success_1() {
        TestUtils.runTest(INPUT_1, EXPECTED_1);
    }

    @Test
    public void success_2() {
        TestUtils.runTest(INPUT_2, EXPECTED_2);
    }

    @Test
    public void success_3() {
        TestUtils.runTest(INPUT_3, EXPECTED_3);
    }

    @Test
    public void success_4() {
        TestUtils.runTest(INPUT_4, EXPECTED_4);
    }

}