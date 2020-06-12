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

public class ClassExpansionTest3 {

    public static final String EXPECTED = "{\"list\":[{\"description\":\"tel\",\"value\":\"fb\"},{\"description\":\"yt\",\"value\":\"tw\"}]}";

    public static final String INPUT = "*class(\n" +
            " *id=desc;\n" +
            " *name=description;\n" +
            " *superclass=str\n" +
            ");\n" +
            "\n" +
            "*class(\n" +
            " *id=val;\n" +
            " *name=value;\n" +
            " *superclass=str\n" +
            ");\n" +
            "\n" +
            "*class(\n" +
            " *id=media1;\n" +
            " *name=media1;\n" +
            " *assign=[\n" +
            "   [desc;val]\n" +
            " ]\n" +
            ");\n" +
            "\n" +
            "*class(\n" +
            " *id=media2;\n" +
            " *name=media2;\n" +
            " *assign=[\n" +
            "   [desc;val]\n" +
            " ]\n" +
            ");\n" +
            "*class(\n" +
            " *id=list;\n" +
            " *name=list;\n" +
            " *assign[\n" +
            "   [media1;media2]\n" +
            " ]\n" +
            "); list=[tel;fb]:[yt;tw]";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}