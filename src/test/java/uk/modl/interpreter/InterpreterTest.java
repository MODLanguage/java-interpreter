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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.interpreter.model.Modl;

public class InterpreterTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test(expected = RuntimeException.class)
    public void parseBad() {
        Interpreter.interpretToJsonString("x;xx");
    }

    @Test
    public void testInterpreterConvenienceMethods_1() {
        final Modl modl = Interpreter.interpret("a=b");
        Assert.assertNotNull(modl);
        Assert.assertEquals(1, modl.getStructures()
                .get()
                .size());
    }

    @Test
    public void testInterpreterConvenienceMethods_2() throws JsonProcessingException {
        final JsonNode jsonNode = Interpreter.interpretToJsonObject("a=b");
        Assert.assertNotNull(jsonNode);
        Assert.assertEquals("{\"a\":\"b\"}", mapper.writeValueAsString(jsonNode));
    }

    @Test
    public void testInterpreterConvenienceMethods_3() {
        final String json = Interpreter.interpretToJsonString("a=b");
        Assert.assertNotNull(json);
        Assert.assertEquals("{\"a\":\"b\"}", json);
    }

    @Test
    public void testInterpreterConvenienceMethods_4() {
        final String json = Interpreter.interpretToPrettyJsonString("a=b");
        Assert.assertNotNull(json);
        Assert.assertEquals("{\n  \"a\" : \"b\"\n}", json);
    }

    @Test
    public void testParseLongModl() {
        final String json = Interpreter.interpretToPrettyJsonString("@n=1;@p=1;o(n=Go-Vac Cleaning Solutions;"
                + "s=Cleaning Company;c[t(v=+447460 390527);fb=Go-vac-Cleaning-Solutions-108934108269782/;"
                + "in=govaccleaningsolutions;li=company/go-vac-cleaning-solutions;a(al[104 Candleford Court;"
                + "Buckingham];pz=MK18 1GD;co=GB)])");
        Assert.assertNotNull(json);
        Assert.assertEquals("{\n"
                + "  \"@n\" : 1,\n"
                + "  \"@p\" : 1,\n"
                + "  \"o\" : {\n"
                + "    \"n\" : \"Go-Vac Cleaning Solutions\",\n"
                + "    \"s\" : \"Cleaning Company\",\n"
                + "    \"c\" : [ {\n"
                + "      \"t\" : {\n"
                + "        \"v\" : \"+447460 390527\"\n"
                + "      }\n"
                + "    }, {\n"
                + "      \"fb\" : \"Go-vac-Cleaning-Solutions-108934108269782/\"\n"
                + "    }, {\n"
                + "      \"in\" : \"govaccleaningsolutions\"\n"
                + "    }, {\n"
                + "      \"li\" : \"company/go-vac-cleaning-solutions\"\n"
                + "    }, {\n"
                + "      \"a\" : {\n"
                + "        \"al\" : [ \"104 Candleford Court\", \"Buckingham\" ],\n"
                + "        \"pz\" : \"MK18 1GD\",\n"
                + "        \"co\" : \"GB\"\n"
                + "      }\n"
                + "    } ]\n"
                + "  }\n"
                + "}", json);
    }

}
