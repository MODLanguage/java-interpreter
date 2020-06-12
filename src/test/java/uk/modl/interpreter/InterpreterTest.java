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
import uk.modl.transforms.TransformationContext;
import uk.modl.utils.TestUtils;

public class InterpreterTest {

    public static final String EXPECTED = "{\"delta\":{\"test1\":\"test2\",\"y\":\"yankee\",\"x\":\"xray\",\"w\":\"whisky\",\"v\":\"victor\"},\"e\":1,\"f\":2.2,\"g\":null,\"h\":true,\"j\":false}";

    public static final String INPUT = "*c(*i=a;*n=alpha;*s=map;v=victor);*c(*i=b;*n=bravo;*s=a;w=whisky);*c(*i=c;*n=charlie;*s=b;x=xray);*c(*i=d;*n=delta;*s=c;y=yankee);d(test1=test2);e=1;f=2.2;g=000;h=01;j=00";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

    @Test(expected = RuntimeException.class)
    public void parseBad() {
        new Interpreter().apply(TransformationContext.emptyCtx(), "xxx");
    }
}