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

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

public class RelativeStarLoadTest1 {

    public static final String EXPECTED = "[ {\n" +
            "  \"message\" : {\n" +
            "    \"direction\" : \"a\",\n" +
            "    \"date_time\" : \"b\",\n" +
            "    \"message\" : \"c\",\n" +
            "    \"method\" : \"sms\"\n" +
            "  }\n" +
            "}, {\n" +
            "  \"message\" : {\n" +
            "    \"direction\" : \"x\",\n" +
            "    \"date_time\" : \"y\",\n" +
            "    \"message\" : \"z\",\n" +
            "    \"method\" : \"sms\"\n" +
            "  }\n" +
            "} ]";

    @Test
    public void parseOk() throws IOException {

        final URL url = Paths.get("src/test/resources/modl/RelativeStarLoadTest1.modl")
                .toUri()
                .toURL();
        final Interpreter interpreter = new Interpreter();
        final String json = interpreter.interpretToPrettyJsonString(url, 10000);
        Assert.assertEquals(EXPECTED, json);
    }

}