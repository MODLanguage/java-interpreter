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

public class ConditionalTest extends TestCase {
    final static List<Object[]> expected =  Arrays.asList(new Object[][] {
            {"_number=42\n" +
                    "{\n" +
                    "  !{number>41}?\n" +
                    "    support_number=441270123456\n" +
                    "  /?\n" +
                    "    support_number=International Clients:14161234567\n" +
                    "}",
                    "{\"support_number\":[\"International Clients\",14161234567]}"},
            {"_country=gb\n" +
                    "{\n" +
                    "  !{country=us|gb|au}?\n" +
                    "    support_number=441270123456\n" +
                    "  /?\n" +
                    "    support_number=International Clients:14161234567\n" +
                    "}",
                    "{\"support_number\":[\"International Clients\",14161234567]}"},
            {"_test=gb\n" +
                    "result={test=gb|au?No/?Yes}", "{ \"result\": \"No\" }"},
            {"_input=\"hi apple ios\"\n" +
                    "{\n" +
                    "  {input=*apple*ios*}?\n" +
                    "    support_number=441270123456\n" +
                    "  /?\n" +
                    "    support_number=International Clients:14161234567\n" +
                    "}",
                    "{\"support_number\":441270123456}"},
            {"_input=\"An iOS string\"\n" +
                    "{\n" +
                    "  {input=*iOS*}?\n" +
                    "    support_number=441270123456\n" +
                    "  /?\n" +
                    "    support_number=International Clients:14161234567\n" +
                    "}",
                    "{\"support_number\":441270123456}"},
            {"_input=\"An iOS string\"\n" +
                    "{\n" +
                    "  !{input=iOS*}?\n" +
                    "    support_number=441270123456\n" +
                    "  /?\n" +
                    "    support_number=International Clients:14161234567\n" +
                    "}",
                    "{\"support_number\":441270123456}"},
            {"_number=42\n" +
                    "{\n" +
                    "  {number>41}?\n" +
                    "    support_number=441270123456\n" +
                    "  /?\n" +
                    "    support_number=International Clients:14161234567\n" +
                    "}",
                    "{\"support_number\":441270123456}"},
            { "_co=ca\n" +
                    "{\n" +
                    "  co = fr?\n" +
                    "    support_number=14161234567\n" +
                    "  /?\n" +
                    "    support_number=441270123456\n" +
                    "}",
                    "{\n" +
                            " \"support_number\" : 441270123456\n" +
                            "}" },
            {"_country=gb\n" +
                    "{\n" +
                    "  country=us|gb|au?\n" +
                    "    support_number=441270123456\n" +
                    "  /?\n" +
                    "    support_number=International Clients:14161234567\n" +
                    "}",
                    "{\"support_number\":441270123456}"},
            { "_co=ca\n" +
                    "_l=fr\n" +
                    "{\n" +
                    "  { co = ca & l = fr } | co = fr?\n" +
                    "    support_number=14161234567\n" +
                    "  /?\n" +
                    "    support_number=441270123456\n" +
                    "}",
                    "{\n" +
                            " \"support_number\" : 14161234567\n" +
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

            ModlObject modlObject = Interpreter.interpret(input);
            String output = JsonPrinter.printModl(modlObject);
            System.out.println("Output : " + output);
            assertEquals(expected.replace(" ", "").replace("\n", "").replace("\r", ""),
                    output.replace(" ", "").replace("\n", "").replace("\r", ""));
        }
    }
}