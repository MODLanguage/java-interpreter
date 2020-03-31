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
                    "*L=\"https://modules.num.uk/3/rcf.txt\";\n" +
                            "_n=1;i[(n=NUM Logo;t=logo;v[`https://www.logos.uk/num/num.uk-100.png`:image/png:100:100;`https://www.logos.uk/num/num.uk-250.png`:image/png:250:250;`https://www.logos.uk/num/num.uk-500.png`:image/png:500:500]);(n=NUM Strapline;t=splash;v[`https://www.logos.uk/num/num.uk-strapline.png`:image/png:250:250;`https://www.logos.uk/num/num.uk-strapline-500.png`:image/png:500:500])]",
                    "{\n" +
                            "  \"images\" : [ {\n" +
                            "    \"name\" : \"NUM Logo\",\n" +
                            "    \"type\" : \"logo\",\n" +
                            "    \"variants\" : [ {\n" +
                            "      \"url\" : \"https://www.logos.uk/num/num.uk-100.png\",\n" +
                            "      \"mime\" : \"image/png\",\n" +
                            "      \"width\" : 100,\n" +
                            "      \"height\" : 100\n" +
                            "    }, {\n" +
                            "      \"url\" : \"https://www.logos.uk/num/num.uk-250.png\",\n" +
                            "      \"mime\" : \"image/png\",\n" +
                            "      \"width\" : 250,\n" +
                            "      \"height\" : 250\n" +
                            "    }, {\n" +
                            "      \"url\" : \"https://www.logos.uk/num/num.uk-500.png\",\n" +
                            "      \"mime\" : \"image/png\",\n" +
                            "      \"width\" : 500,\n" +
                            "      \"height\" : 500\n" +
                            "    } ]\n" +
                            "  }, {\n" +
                            "    \"name\" : \"NUM Strapline\",\n" +
                            "    \"type\" : \"splash\",\n" +
                            "    \"variants\" : [ {\n" +
                            "      \"url\" : \"https://www.logos.uk/num/num.uk-strapline.png\",\n" +
                            "      \"mime\" : \"image/png\",\n" +
                            "      \"width\" : 250,\n" +
                            "      \"height\" : 250\n" +
                            "    }, {\n" +
                            "      \"url\" : \"https://www.logos.uk/num/num.uk-strapline-500.png\",\n" +
                            "      \"mime\" : \"image/png\",\n" +
                            "      \"width\" : 500,\n" +
                            "      \"height\" : 500\n" +
                            "    } ]\n" +
                            "  } ]\n" +
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