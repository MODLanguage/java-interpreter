/*
MIT License

Copyright (c) 2018 NUM Technology Ltd

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation
the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of
the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package uk.modl.parser;

import junit.framework.TestCase;
import org.junit.Test;
import uk.modl.parser.printers.JsonPrinter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ParserTest extends TestCase {
    final public static List<Object[]> expected =  Arrays.asList(new Object[][] {
            { "o=[test;test;t=Customer Service:44123]", "{\n" +
                    "  \"o\" : [ \"test\", \"test\", {\n" +
                    "    \"t\" : [ \"Customer Service\", 44123 ]\n" +
                    "  } ]\n" +
                    "}" ,
                    "o[test;test;t=Customer Service:44123]"},
            { "test[number=1;number=2;number=3]", "{\n" +
                    "  \"test\": [\n" +
                    "    {\n" +
                    "      \"number\": 1\n" +
                    "    }\n" +
                    "    ,\n" +
                    "    {\n" +
                    "      \"number\": 2\n" +
                    "    }\n" +
                    "    ,\n" +
                    "    {\n" +
                    "      \"number\": 3\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}" },
                { "test(one=1;two=2;three=3)", "{\n" +
                    " \"test\" : {" +
                    "  \"one\": 1,\n" +
                    "  \"two\": 2,\n" +
                    "  \"three\": 3\n" +
                    "}}" },
            { "test=test", "{\n" +
                    "  \"test\": \"test\"\n" +
                    "}" },
            { "one=1;two=2;three=3", "[\n" +
                    "  {\n" +
                    "    \"one\": 1\n" +
                    "  }\n" +
                    "  ,\n" +
                    "  {\n" +
                    "    \"two\": 2\n" +
                    "  }\n" +
                    "  ,\n" +
                    "  {\n" +
                    "    \"three\": 3\n" +
                    "  }\n" +
                    "]" },
            { "[o(n=test);o(n=test2)]", "[ {\n" +
                    "  \"o\" : {\n" +
                    "    \"n\" : \"test\"\n" +
                    "  }\n" +
                    "}, {\n" +
                    "  \"o\" : {\n" +
                    "    \"n\" : \"test2\"\n" +
                    "  }\n" +
                    "} ]" },
            { "R=0\nnumber=1;number=2;number=3",
                    " [ {\n" +
                    "  \"R\" : 0\n" +
                    "}, {\n" +
                    "    \"number\": 1\n" +
                    "  }\n" +
                    "  ,\n" +
                    "  {\n" +
                    "    \"number\": 2\n" +
                    "  }\n" +
                    "  ,\n" +
                    "  {\n" +
                    "    \"number\": 3\n" +
                    "  }\n" +
                    "]",
            "R=0;number=1;number=2;number=3"},
            { "test=(one=1)", "{\n" +
                    "  \"test\": {\n" +
                    "    \"one\": 1\n" +
                    "  }\n" +
                    "}",
            "test(one=1)"},
            { "test(one=1)", "{\n" +
                    "  \"test\": {\n" +
                    "    \"one\": 1\n" +
                    "  }\n" +
                    "}" },
            { "test=[1;2;3]", "{\n" +
                    "  \"test\": [\n" +
                    "    1,\n" +
                    "    2,\n" +
                    "    3\n" +
                    "  ]\n" +
                    "}" ,
            "test=1:2:3"},
            { "test[1;2;3]", "{\n" +
                    "  \"test\": [\n" +
                    "    1,\n" +
                    "    2,\n" +
                    "    3\n" +
                    "  ]\n" +
                    "}" ,
            "test=1:2:3"},
            { "o(n=Tesco;s=Every Little Helps)", "{\n" +
                    "  \"o\" : {\n" +
                    "    \"n\" : \"Tesco\",\n" +
                    "    \"s\" : \"Every Little Helps\"\n" +
                    "  }\n" +
                    "}" },
            { "o(n=test)", "{\n" +
                    "  \"o\" : {\n" +
                    "    \"n\" : \"test\"\n" +
                    "  }\n" +
                    "}",
            "o(n=test)"},
            { "o(n=test);\n", "{\n" +
                    "  \"o\" : {\n" +
                    "    \"n\" : \"test\"\n" +
                    "  }\n" +
                    "}",
            "o(n=test)"},
            { "o(\n" +
                    "n=test\n" +
                    ")", "{\n" +
                    "  \"o\" : {\n" +
                    "    \"n\" : \"test\"\n" +
                    "  }\n" +
                    "}",
            "o(n=test)"},
            { "o(n=test);", "{\n" +
                    "  \"o\" : {\n" +
                    "    \"n\" : \"test\"\n" +
                    "  }\n" +
                    "}" ,
            "o(n=test)"},
            { "o(n=test)\n" +
                    "o(n=test2)", "[ {\n" +
                    "  \"o\" : {\n" +
                    "    \"n\" : \"test\"\n" +
                    "  }\n" +
                    "}, {\n" +
                    "  \"o\" : {\n" +
                    "    \"n\" : \"test2\"\n" +
                    "  }\n" +
                    "} ]",
            "o(n=test);o(n=test2)"},
            { "o(n=test);o(n=test2)", "[ {\n" +
                    "  \"o\" : {\n" +
                    "    \"n\" : \"test\"\n" +
                    "  }\n" +
                    "}, {\n" +
                    "  \"o\" : {\n" +
                    "    \"n\" : \"test2\"\n" +
                    "  }\n" +
                    "} ]" },
            { "o=[test;test]", "{\n" +
                    "  \"o\" : [ \"test\", \"test\" ]\n" +
                    "}" ,
            "o=test:test"},
            { "o=test", "{\n" +
                    "  \"o\" : \"test\"\n" +
                    "}" },
            { "o=[1;2]", "{\n" +
                    "  \"o\" : [ 1, 2 ]\n" +
                    "}",
            "o=1:2"},
            { "o=[test1;test2]", "{\n" +
                    "  \"o\" : [ \"test1\", \"test2\" ]\n" +
                    "}" ,
            "o=test1:test2"},
            { "o(t=test1;t2=test2)", "{\n" +
                    "  \"o\" : {\n" +
                    "    \"t\" : \"test1\",\n" +
                    "    \"t2\" : \"test2\"\n" +
                    "  }\n" +
                    "}" },
            { "o(t(a=test;b=test2);t2(c=test;d=test2))", "{\n" +
                    "  \"o\" : {\n" +
                    "    \"t\" : {\n" +
                    "      \"a\" : \"test\",\n" +
                    "      \"b\" : \"test2\"\n" +
                    "    },\n" +
                    "    \"t2\" : {\n" +
                    "      \"c\" : \"test\",\n" +
                    "      \"d\" : \"test2\"\n" +
                    "    }\n" +
                    "  }\n" +
                    "}" }
    });

    @Test
    public void testParser() throws IOException {
        // Take a load of test MODL strings and make sure uk.modl.parser generates correct output

        for (Object test : expected) {
            String input = (String)((Object[])test)[0];
            String expected = (String)((Object[])test)[1];

            System.out.println("Input : " + input);
            System.out.println("Expected : " + expected);

            RawModlObject rawModlObject = ModlObjectCreator.processModlParsed(input);
            String output = JsonPrinter.printModl(rawModlObject);
            System.out.println("Output : " + output);
            assertEquals(expected.replace(" ", "").replace("\n", "").replace("\r", ""),
                    output.replace(" ", "").replace("\n", "").replace("\r", ""));
        }
    }
}