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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import org.junit.Assert;
import org.junit.Test;

import uk.modl.model.Modl;

public class InterpreterTest {

  @Test(expected = RuntimeException.class)
  public void parseBad() throws MalformedURLException {
    new Interpreter().interpretToJsonString("xxx");
  }

  @Test
  public void testInterpreterConvenienceMethods_1() throws MalformedURLException {
    final Interpreter interpreter = new Interpreter();
    final Modl modl = interpreter.interpret("a=b");
    Assert.assertNotNull(modl);
    Assert.assertEquals(1, modl.getStructures().size());
  }

  @Test
  public void testInterpreterConvenienceMethods_2() throws MalformedURLException {
    final Interpreter interpreter = new Interpreter();
    final JsonNode jsonNode = interpreter.interpretToJsonObject("a=b");
    Assert.assertNotNull(jsonNode);
    Assert.assertEquals("{\"a\":\"b\"}", jsonNode.toString());
  }

  @Test
  public void testInterpreterConvenienceMethods_3() throws JsonProcessingException, MalformedURLException {
    final Interpreter interpreter = new Interpreter();
    final String json = interpreter.interpretToJsonString("a=b");
    Assert.assertNotNull(json);
    Assert.assertEquals("{\"a\":\"b\"}", json);
  }

  @Test
  public void testInterpreterConvenienceMethods_4() throws JsonProcessingException, MalformedURLException {
    final Interpreter interpreter = new Interpreter();
    final String json = interpreter.interpretToPrettyJsonString("a=b");
    Assert.assertNotNull(json);
    Assert.assertEquals("{\n  \"a\" : \"b\"\n}", json);
  }

}