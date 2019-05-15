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
import uk.modl.modlObject.ModlObject;
import uk.modl.parser.printers.JsonPrinter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ImportTest extends TestCase {
    final public static List<Object[]> expected =  Arrays.asList(new Object[][]{
            {"*L=\"http://config.modl.uk/demo/message-thread.txt\"\n" +
                    "*class(\n" +
                    "  *id=m\n" +
                    "  *name=message\n" +
                    "  *superclass=map\n" +
                    "  *assign=[\n" +
                    "    [direction;date_time;message]\n" +
                    "  ]\n" +
                    "  transform=sms\n" +
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
                            "    \"transform\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"in\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:26\",\n" +
                            "    \"message\" : \"Hello, how are you?\",\n" +
                            "    \"transform\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"out\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:25\",\n" +
                            "    \"message\" : \"Hi, good thanks\",\n" +
                            "    \"transform\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"out\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:26\",\n" +
                            "    \"message\" : \"How about you?\",\n" +
                            "    \"transform\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"in\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:26\",\n" +
                            "    \"message\" : \"Yes, fine thanks. What are you up to?\",\n" +
                            "    \"transform\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"out\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:25\",\n" +
                            "    \"message\" : \"Just testing out MODL\",\n" +
                            "    \"transform\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"in\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:26\",\n" +
                            "    \"message\" : \"Cool!\",\n" +
                            "    \"transform\" : \"sms\"\n" +
                            "  }\n" +
                            "} ]\n",
            "*L=http~://config.modl.uk/demo/message-thread.txt;*c(*i=m;*n=message;*s=map;*a[[direction;date_time;message]];transform=sms);m=out:2018-03-22 15~:25:Hi;m=in:2018-03-22 15~:26:Hello, how are you?;m=out:2018-03-22 15~:25:Hi, good thanks;m=out:2018-03-22 15~:26:How about you?;m=in:2018-03-22 15~:26:Yes, fine thanks. What are you up to?;m=out:2018-03-22 15~:25:Just testing out MODL;m=in:2018-03-22 15~:26:Cool!"},
            {"_var=2\n" +
                    "*L=\"http://s3-eu-west-1.amazonaws.com/modltestfiles/testing.txt!\"\n" +
                    "print=%update_date\n",
                    "{\n" +
                            "    \"print\": \"20180921 08:20 2\"\n" +
                            "}",
            "_var=2;*L=http~://s3-eu-west-1.amazonaws.com/modltestfiles/testing.txt!;print=%update_date"},

            {"_T=grammar_tests/demo\n*L=`%T`_config", "", "_T=grammar_tests/demo;*L=`%T`_config"},
            {"*L=grammar_tests/1:grammar_tests/2:grammar_tests/3\n" +
                    "the_number=%number",
                    "{\n" +
                            " \"the_number\": 3\n" +
                            "}",
                    "*L=grammar_tests/1:grammar_tests/2:grammar_tests/3;the_number=%number"},
            {"*L=grammar_tests/1:grammar_tests/2:grammar_tests/3:grammar_tests/1\n" +
                    "the_number=%number",
                    "{\n" +
                            " \"the_number\": 1\n" +
                            "}",
                    "*L=grammar_tests/1:grammar_tests/2:grammar_tests/3:grammar_tests/1;the_number=%number"},
            {"*L[grammar_tests/1;grammar_tests/2;grammar_tests/3;grammar_tests/1]\n" +
                    "the_number=%number",
                    "{\n" +
                            " \"the_number\": 1\n" +
                            "}",
                    "*L=grammar_tests/1:grammar_tests/2:grammar_tests/3:grammar_tests/1;the_number=%number"},
            {"*L=grammar_tests/a:grammar_tests/b:grammar_tests/c\n" +
                    "var=%var",
                    "{\n" +
                            " \"var\": \"abc\"\n" +
                            "}",
            "*L=grammar_tests/a:grammar_tests/b:grammar_tests/c;var=%var"},

            {"*L=grammar_tests/demo_config\n" +
                    "*class(\n" +
                    "  *id=m\n" +
                    "  *name=message\n" +
                    "  *superclass=map\n" +
                    "  *assign=[\n" +
                    "    [direction;date_time;message]\n" +
                    "  ]\n" +
                    "  transform=sms\n" +
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
                            "    \"transform\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"in\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:26\",\n" +
                            "    \"message\" : \"Hello, how are you?\",\n" +
                            "    \"transform\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"out\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:25\",\n" +
                            "    \"message\" : \"Hi, good thanks\",\n" +
                            "    \"transform\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"out\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:26\",\n" +
                            "    \"message\" : \"How about you?\",\n" +
                            "    \"transform\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"in\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:26\",\n" +
                            "    \"message\" : \"Yes, fine thanks. What are you up to?\",\n" +
                            "    \"transform\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"out\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:25\",\n" +
                            "    \"message\" : \"Just testing out MODL\",\n" +
                            "    \"transform\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"in\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:26\",\n" +
                            "    \"message\" : \"Cool!\",\n" +
                            "    \"transform\" : \"sms\"\n" +
                            "  }\n" +
                            "} ]\n",
            "*L=grammar_tests/demo_config;*c(*i=m;*n=message;*s=map;*a[[direction;date_time;message]];transform=sms);m=out:2018-03-22 15~:25:Hi;m=in:2018-03-22 15~:26:Hello, how are you?;m=out:2018-03-22 15~:25:Hi, good thanks;m=out:2018-03-22 15~:26:How about you?;m=in:2018-03-22 15~:26:Yes, fine thanks. What are you up to?;m=out:2018-03-22 15~:25:Just testing out MODL;m=in:2018-03-22 15~:26:Cool!"},
            {"## country\n" +
                    "_c = us\n" +
                    "## language\n" +
                    "_l = en\n" +
                    "\n" +
                    "*L=grammar_tests/import_config.modl\n" +
                    "\n" +
                    "country = %c\n" +
                    "language = %l\n" +
                    "time_zone = %tz",
                    "[\n" +
                            "  {\"country\": \"us\"},\n" +
                            "  {\"language\": \"en\"},\n" +
                            "  {\"time_zone\": \"EST\"}\n" +
                            "]",
            "_c=us;_l=en;*L=grammar_tests/import_config.modl;country=%c;language=%l;time_zone=%tz"},
            {"*L=grammar_tests/test_import_dir/test_import.txt\n" +
                    "*class(\n" +
                    "  *id=m\n" +
                    "  *name=message\n" +
                    "  *superclass=map\n" +
                    "  *assign=[\n" +
                    "    [direction;date_time;message]\n" +
                    "  ]\n" +
                    "  transform=sms\n" +
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
                            "    \"transform\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"in\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:26\",\n" +
                            "    \"message\" : \"Hello, how are you?\",\n" +
                            "    \"transform\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"out\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:25\",\n" +
                            "    \"message\" : \"Hi, good thanks\",\n" +
                            "    \"transform\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"out\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:26\",\n" +
                            "    \"message\" : \"How about you?\",\n" +
                            "    \"transform\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"in\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:26\",\n" +
                            "    \"message\" : \"Yes, fine thanks. What are you up to?\",\n" +
                            "    \"transform\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"out\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:25\",\n" +
                            "    \"message\" : \"Just testing out MODL\",\n" +
                            "    \"transform\" : \"sms\"\n" +
                            "  }\n" +
                            "}, {\n" +
                            "  \"message\" : {\n" +
                            "    \"direction\" : \"in\",\n" +
                            "    \"date_time\" : \"2018-03-22 15:26\",\n" +
                            "    \"message\" : \"Cool!\",\n" +
                            "    \"transform\" : \"sms\"\n" +
                            "  }\n" +
                            "} ]\n",
            "*L=grammar_tests/test_import_dir/test_import.txt;*c(*i=m;*n=message;*s=map;*a[[direction;date_time;message]];transform=sms);m=out:2018-03-22 15~:25:Hi;m=in:2018-03-22 15~:26:Hello, how are you?;m=out:2018-03-22 15~:25:Hi, good thanks;m=out:2018-03-22 15~:26:How about you?;m=in:2018-03-22 15~:26:Yes, fine thanks. What are you up to?;m=out:2018-03-22 15~:25:Just testing out MODL;m=in:2018-03-22 15~:26:Cool!"}
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
            assertEquals(expected.replace(" ", "").replace("\n", "").replace("\r",""),
                    output.replace(" ", "").replace("\n", "").replace("\r",""));
        }

    }

    // TODO Run some test inputs which should generate errors (e.g. re-defininng uppercase OBJECTNAME)
}