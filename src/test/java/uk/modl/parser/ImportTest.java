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
import uk.modl.interpreter.Interpreter;
import uk.modl.interpreter.ModlObject;
import uk.modl.parser.printers.JsonPrinter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ImportTest extends TestCase {
    final static List<Object[]> expected =  Arrays.asList(new Object[][]{
            {"## country\n" +
                    "_c = us\n" +
                    "## language\n" +
                    "_l = en\n" +
                    "\n" +
                    "*I=import_config.modl\n" +
                    "\n" +
                    "country = %c\n" +
                    "language = %l\n" +
                    "time_zone = %tz",
                    "[\n" +
                            "  {\"country\": \"us\"},\n" +
                            "  {\"language\": \"en\"},\n" +
                            "  {\"time_zone\": \"EST\"}\n" +
                            "]"},
            {"*I=a:b:c\n" +
                    "var=%var",
                    "{\n" +
                            " \"var\": \"abc\"\n" +
                            "}"},
            {"*I=1:2:3\n" +
                    "the_number=%number",
                    "{\n" +
                            " \"the_number\": 3\n" +
                            "}"},
            {"*I=1:2:3:1\n" +
                    "the_number=%number",
            "{\n" +
                    " \"the_number\": 1\n" +
                    "}"},
            {"*I[1;2;3;1]\n" +
                    "the_number=%number",
            "{\n" +
                    " \"the_number\": 1\n" +
                    "}"},
            {"_T=demo\n*I=`%T`_config", ""},
            {"*I=demo_config\n" +
                    "*class(\n" +
                    "  *id=m\n" +
                    "  *name=message\n" +
                    "  *superclass=map\n" +
                    "  *assign=[\n" +
                    "    [direction;date_time;message]\n" +
                    "  ]\n" +
                    "  method=sms\n" +
                    ")\n" +
                    "\n" +
                    "m=out:2018-03-22 15\\:25:Hi\n" +
                    "m=in:2018-03-22 15\\:26:Hello, how are you?\n" +
                    "m=out:2018-03-22 15\\:25:Hi, good thanks\n" +
                    "m=out:2018-03-22 15\\:26:How about you?\n" +
                    "m=in:2018-03-22 15\\:26:Yes, fine thanks. What are you up to?\n" +
                    "m=out:2018-03-22 15\\:25:Just testing out MODL\n" +
                    "m=in:2018-03-22 15\\:26:Cool!",
                    "[ {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"out\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:25\",\n" +
                            "    \"message\" : \"Hi\",\n" +
                            "    \"method\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"in\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:26\",\n" +
                            "    \"message\" : \"Hello, how are you?\",\n" +
                            "    \"method\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"out\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:25\",\n" +
                            "    \"message\" : \"Hi, good thanks\",\n" +
                            "    \"method\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"out\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:26\",\n" +
                            "    \"message\" : \"How about you?\",\n" +
                            "    \"method\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"in\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:26\",\n" +
                            "    \"message\" : \"Yes, fine thanks. What are you up to?\",\n" +
                            "    \"method\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"out\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:25\",\n" +
                            "    \"message\" : \"Just testing out MODL\",\n" +
                            "    \"method\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"in\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:26\",\n" +
                            "    \"message\" : \"Cool!\",\n" +
                            "    \"method\" : \"sms\"\n" +
                            "  }\n" +
                            "} ]\n"},
            {"*I=src/test/test_import_dir/test_import.txt\n" +
                    "*class(\n" +
                    "  *id=m\n" +
                    "  *name=message\n" +
                    "  *superclass=map\n" +
                    "  *assign=[\n" +
                    "    [direction;date_time;message]\n" +
                    "  ]\n" +
                    "  method=sms\n" +
                    ")\n" +
                    "\n" +
                    "m=out:2018-03-22 15\\:25:Hi\n" +
                    "m=in:2018-03-22 15\\:26:Hello, how are you?\n" +
                    "m=out:2018-03-22 15\\:25:Hi, good thanks\n" +
                    "m=out:2018-03-22 15\\:26:How about you?\n" +
                    "m=in:2018-03-22 15\\:26:Yes, fine thanks. What are you up to?\n" +
                    "m=out:2018-03-22 15\\:25:Just testing out MODL\n" +
                    "m=in:2018-03-22 15\\:26:Cool!",
                    "[ {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"out\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:25\",\n" +
                            "    \"message\" : \"Hi\",\n" +
                            "    \"method\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"in\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:26\",\n" +
                            "    \"message\" : \"Hello, how are you?\",\n" +
                            "    \"method\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"out\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:25\",\n" +
                            "    \"message\" : \"Hi, good thanks\",\n" +
                            "    \"method\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"out\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:26\",\n" +
                            "    \"message\" : \"How about you?\",\n" +
                            "    \"method\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"in\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:26\",\n" +
                            "    \"message\" : \"Yes, fine thanks. What are you up to?\",\n" +
                            "    \"method\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"out\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:25\",\n" +
                            "    \"message\" : \"Just testing out MODL\",\n" +
                            "    \"method\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"in\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:26\",\n" +
                            "    \"message\" : \"Cool!\",\n" +
                            "    \"method\" : \"sms\"\n" +
                            "  }\n" +
                            "} ]\n"},
            {"*I=\"http://config.modl.uk/demo/message-thread.txt\"\n" +
                    "*class(\n" +
                    "  *id=m\n" +
                    "  *name=message\n" +
                    "  *superclass=map\n" +
                    "  *assign=[\n" +
                    "    [direction;date_time;message]\n" +
                    "  ]\n" +
                    "  method=sms\n" +
                    ")\n" +
                    "\n" +
                    "m=out:2018-03-22 15\\:25:Hi\n" +
                    "m=in:2018-03-22 15\\:26:Hello, how are you?\n" +
                    "m=out:2018-03-22 15\\:25:Hi, good thanks\n" +
                    "m=out:2018-03-22 15\\:26:How about you?\n" +
                    "m=in:2018-03-22 15\\:26:Yes, fine thanks. What are you up to?\n" +
                    "m=out:2018-03-22 15\\:25:Just testing out MODL\n" +
                    "m=in:2018-03-22 15\\:26:Cool!",
                    "[ {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"out\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:25\",\n" +
                            "    \"message\" : \"Hi\",\n" +
                            "    \"method\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"in\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:26\",\n" +
                            "    \"message\" : \"Hello, how are you?\",\n" +
                            "    \"method\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"out\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:25\",\n" +
                            "    \"message\" : \"Hi, good thanks\",\n" +
                            "    \"method\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"out\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:26\",\n" +
                            "    \"message\" : \"How about you?\",\n" +
                            "    \"method\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"in\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:26\",\n" +
                            "    \"message\" : \"Yes, fine thanks. What are you up to?\",\n" +
                            "    \"method\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"out\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:25\",\n" +
                            "    \"message\" : \"Just testing out MODL\",\n" +
                            "    \"method\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"in\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:26\",\n" +
                            "    \"message\" : \"Cool!\",\n" +
                            "    \"method\" : \"sms\"\n" +
                            "  }\n" +
                            "} ]\n"},
    });

    @Test
    public void testParser() throws IOException {
        // Take a load of test MODL strings and make sure uk.modl.parser generates correct output

        for (Object test : expected) {
            String input = (String)((Object[])test)[0];
            String expected = (String)((Object[])test)[1];

            System.out.println("Input : " + input);
            System.out.println("Expected : " + expected);

            ModlObject modlObject = Interpreter.interpret(input);
            String output = JsonPrinter.printModl(modlObject);
            System.out.println("Output : " + output);
            assertEquals(expected.replace(" ", "").replace("\n", ""),
                    output.replace(" ", "").replace("\n", ""));
        }

    }

    // TODO Run some test inputs which should generate errors (e.g. re-defininng uppercase OBJECTNAME)
}