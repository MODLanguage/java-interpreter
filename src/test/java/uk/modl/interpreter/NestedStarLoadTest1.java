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

public class NestedStarLoadTest1 {

    public static final String EXPECTED1 = "{\"the_number\":3}";

    public static final String EXPECTED2 = "{\"the_number\":1}";

    public static final String INPUT1 = "*l=../grammar/tests/1:../grammar/tests/2:../grammar/tests/3;the_number=%number";

    public static final String INPUT2 = "*l=../grammar/tests/1:../grammar/tests/2:../grammar/tests/3:../grammar/tests/1;the_number=%number";

    @Test
    public void test1() {
        TestUtils.runTest(INPUT1, EXPECTED1);
    }

    @Test
    public void test2() {
        TestUtils.runTest(INPUT2, EXPECTED2);
    }
}