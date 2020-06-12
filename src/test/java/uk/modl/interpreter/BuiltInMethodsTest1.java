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

public class BuiltInMethodsTest1 {

    public static final String EXPECTED = "{\"upcase_example\":\"QUICK-TEST OF JOHN'S VARIABLE_METHODS\",\"downcase_example\":\"quick-test of john's variable_methods\",\"initcap_example\":\"Quick-test Of John's Variable_methods\",\"sentence_example\":\"Quick-test of John's variable_methods\",\"url_encode_example\":\"QUICK-TEST+OF+JOHN%27S+VARIABLE_METHODS\"}";

    public static final String INPUT = "_testing = quick-test of John's variable_methods;upcase_example = %testing.u;downcase_example = %testing.d;initcap_example = %testing.i;sentence_example = %testing.s;url_encode_example = %testing.u.e";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}