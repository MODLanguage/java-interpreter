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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.model.Modl;
import uk.modl.transforms.TransformationContext;
import uk.modl.utils.TestUtils;

import java.net.MalformedURLException;
import java.net.URL;

public class InterpreterTest {

    public static final String EXPECTED = "{\"delta\":{\"test1\":\"test2\",\"y\":\"yankee\",\"x\":\"xray\",\"w\":\"whisky\",\"v\":\"victor\"},\"e\":1,\"f\":2.2,\"g\":null,\"h\":true,\"j\":false}";

    public static final String INPUT = "*c(*i=a;*n=alpha;*s=map;v=victor);*c(*i=b;*n=bravo;*s=a;w=whisky);*c(*i=c;*n=charlie;*s=b;x=xray);*c(*i=d;*n=delta;*s=c;y=yankee);d(test1=test2);e=1;f=2.2;g=000;h=01;j=00";

    @Test
    public void parseOk() throws MalformedURLException {
        TestUtils.runTest(INPUT, EXPECTED);
    }

    @Test(expected = RuntimeException.class)
    public void parseBad() throws MalformedURLException {
        new Interpreter().apply(TransformationContext.baseCtx(new URL("file:///")), "xxx");
    }

    @Test
    public void testInterpreterConvenienceMethods_1() throws MalformedURLException {
        final Interpreter interpreter = new Interpreter();
        final Modl modl = interpreter.interpret("a=b", new URL("file:///"));
        Assert.assertNotNull(modl);
        Assert.assertEquals(1, modl.getStructures()
                .size());
    }

    @Test
    public void testInterpreterConvenienceMethods_2() throws MalformedURLException {
        final Interpreter interpreter = new Interpreter();
        final JsonNode jsonNode = interpreter.interpretToJsonObject("a=b", new URL("file:///"));
        Assert.assertNotNull(jsonNode);
        Assert.assertEquals("{\"a\":\"b\"}", jsonNode.toString());
    }

    @Test
    public void testInterpreterConvenienceMethods_3() throws JsonProcessingException, MalformedURLException {
        final Interpreter interpreter = new Interpreter();
        final String json = interpreter.interpretToJsonString("a=b", new URL("file:///"));
        Assert.assertNotNull(json);
        Assert.assertEquals("{\"a\":\"b\"}", json);
    }

    @Test
    public void testInterpreterConvenienceMethods_4() throws JsonProcessingException, MalformedURLException {
        final Interpreter interpreter = new Interpreter();
        final String json = interpreter.interpretToPrettyJsonString("a=b", new URL("file:///"));
        Assert.assertNotNull(json);
        Assert.assertEquals("{\n  \"a\" : \"b\"\n}", json);
    }

}