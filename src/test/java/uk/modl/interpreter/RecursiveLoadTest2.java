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

import java.net.MalformedURLException;

import org.junit.Test;

import uk.modl.utils.TestUtils;

public class RecursiveLoadTest2 {

  final String INPUT_1 = "*l=../grammar/tests/1:../grammar/tests/2:../grammar/tests/3:../grammar/tests/1;\nfiles=%*load";

  final String EXPECT_1 = "{\"files\":[\"../grammar/tests/1.modl\",\"../grammar/tests/2.modl\",\"../grammar/tests/3.modl\",\"../grammar/tests/1.modl\"]}";

  @Test
  public void parseOk() throws MalformedURLException {
    System.out.println("Input:");
    System.out.println(INPUT_1);
    System.out.println("Expecting:");
    System.out.println(EXPECT_1);
    TestUtils.runTest(INPUT_1, EXPECT_1);
  }

}
