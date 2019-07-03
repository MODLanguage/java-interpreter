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

import org.junit.Assert;
import org.junit.Test;
import uk.modl.interpreter.Interpreter;
import uk.modl.modlObject.ModlObject;
import uk.modl.parser.printers.JsonPrinter;

import java.io.IOException;

/**
 * @author twalmsley
 */
public class ScratchTest {
    private final static String[][] expected = {
            {
                    "_C=us; _L=en; _Q=numexample.com; _D=numexample.com; _DV=iPhone 7; _TZ=GMT; _GPS=53.473997,-2.237334; *load=\"https://s3.eu-west-2.amazonaws.com/modules.num.uk/1/rcf.txt!\"; o=NUM Example Co:Example Strapline:[t=Call us:441270123456;fb=examplefacebook;tw=exampletwitter;in=exampleinstagram]",
                    "{\n" +
                            "    \"organisation\": {\n" +
                            "        \"name\": \"NUM Example Co\",\n" +
                            "        \"slogan\": \"Example Strapline\",\n" +
                            "        \"contacts\": [\n" +
                            "            {\n" +
                            "                \"telephone\": {\n" +
                            "                    \"description\": \"Call us\",\n" +
                            "                    \"value\": \"441270123456\",\n" +
                            "                    \"object_type\": \"media\",\n" +
                            "                    \"object_display_name\": \"Telephone\",\n" +
                            "                    \"description_default\": \"Call\",\n" +
                            "                    \"prefix\": \"tel://\",\n" +
                            "                    \"media_type\": \"core\"\n" +
                            "                }\n" +
                            "            },\n" +
                            "            {\n" +
                            "                \"facebook\": {\n" +
                            "                    \"value\": \"examplefacebook\",\n" +
                            "                    \"object_type\": \"media\",\n" +
                            "                    \"object_display_name\": \"Facebook\",\n" +
                            "                    \"description_default\": \"View Facebook profile\",\n" +
                            "                    \"prefix\": \"https://www.facebook.com/\",\n" +
                            "                    \"media_type\": \"3p\"\n" +
                            "                }\n" +
                            "            },\n" +
                            "            {\n" +
                            "                \"twitter\": {\n" +
                            "                    \"value\": \"exampletwitter\",\n" +
                            "                    \"object_type\": \"media\",\n" +
                            "                    \"object_display_name\": \"Twitter\",\n" +
                            "                    \"description_default\": \"View Twitter profile\",\n" +
                            "                    \"prefix\": \"https://www.twitter.com/\",\n" +
                            "                    \"media_type\": \"3p\",\n" +
                            "                    \"value_prefix\": \"@\"\n" +
                            "                }\n" +
                            "            },\n" +
                            "            {\n" +
                            "                \"instagram\": {\n" +
                            "                    \"value\": \"exampleinstagram\",\n" +
                            "                    \"object_type\": \"media\",\n" +
                            "                    \"object_display_name\": \"Instagram\",\n" +
                            "                    \"description_default\": \"View Instagram profile\",\n" +
                            "                    \"prefix\": \"https://www.instagram.com/\",\n" +
                            "                    \"media_type\": \"3p\"\n" +
                            "                }\n" +
                            "            }\n" +
                            "        ],\n" +
                            "        \"object_type\": \"entity\",\n" +
                            "        \"object_display_name\": \"Organization\",\n" +
                            "        \"description_default\": \"View Organization\"\n" +
                            "    }\n" +
                            "}"
            }
    };

    /**
     * @throws IOException on test failure
     */
    @Test
    public void test_00() throws IOException {
        singleCase(expected[0]);
    }

    /**
     * @param test The current test case
     * @throws IOException on test failure
     */
    private void singleCase(final String[] test) throws IOException {
        String input = test[0];
        String expected = test[1];

        System.out.println("Input : " + input);
        System.out.println("Expected : " + expected);

        ModlObject interpreted = Interpreter.interpret(input);
        String output = JsonPrinter.printModl(interpreted);
        System.out.println("Output : " + output);
        Assert.assertEquals(expected
                        .replace(" ", "")
                        .replace("\n", "")
                        .replace("\r", ""),
                output
                        .replace(" ", "")
                        .replace("\n", "")
                        .replace("\r", ""));
    }
}