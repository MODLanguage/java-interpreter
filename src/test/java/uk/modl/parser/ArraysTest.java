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

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.parser.printers.JsonPrinter;

import java.io.IOException;

/**
 * @author twalmsley
 * <p>
 * I run the tests individually rather than in a loop to make it easier to see which ones fail.
 * This way the early failures don't mask later ones.
 */
public class ArraysTest {
    private final static String[][] expected = {{
            // Normal NB array
            "o=1:2:3:4:5", "{\n" + "  \"o\" : [ 1, 2, 3, 4, 5 ]\n" + "}"
    }, {
            // Missing elements
            "o=1:2::4:5", "{\n" + "  \"o\" : [ 1, 2, null, 4, 5 ]\n" + "}"
    }, {
            // Missing elements at the end (should fail, and does)
            "o=1:2::4:5::", "{\n" + " ParseCancellationException\n" + "}"
    }, {
            // Normal array
            "o=[1;2;3;4;5]", "{\n" + "  \"o\" : [ 1, 2, 3, 4, 5 ]\n" + "}"
    }, {
            // Missing elelemts
            "o=[1;2;;4;5]", "{\n" + "  \"o\" : [ 1, 2, null, 4, 5 ]\n" + "}"
    }, {
            // Missing elements at the end (should fail, and does)
            "o=[1;2;;4;5;;]", "{\n" + "  ParseCancellationException\n" + "}"
    }, {
            // Normal array with newlines
            "o=[1\n2\n3\n4\n5]", "{\n" + "  \"o\" : [ 1, 2, 3, 4, 5 ]\n" + "}"
    }, {
            // Multiple newlines
            "o=[1\n2\n\n3\n4\n5]", "{\n" + "  \"o\" : [ 1, 2, 3, 4, 5 ]\n" + "}"
    }, {
            // Missing separator - no semicolon or newline
            "o=[1 2 3 4 5]", "{\n  \"o\" : [ \"1 2 3 4 5\" ]\n" + "}"
    }, {
            // Finish on a semicolon
            "o=[1;2;3;4;5;]", "{\n" + "  ParseCancellationException\n" + "}"
    }, {
            // Finish on a colon
            "o=1:2:3:4:5:", "{\n" + "  ParseCancellationException\n" + "}"
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
     * @throws IOException on test failure
     */
    @Test
    public void test_01() throws IOException {
        singleCase(expected[1]);
    }

    /**
     * @throws IOException on test failure
     */
    @Test
    public void test_02() throws IOException {
        try {
            singleCase(expected[2]);
            Assert.fail("Expected and exception");
        } catch (ParseCancellationException e) {
            System.out.println("Success: ParseCancellationException received");
        }
    }

    /**
     * @throws IOException on test failure
     */
    @Test
    public void test_03() throws IOException {
        singleCase(expected[3]);
    }

    /**
     * @throws IOException on test failure
     */
    @Test
    public void test_04() throws IOException {
        singleCase(expected[4]);
    }

    /**
     * @throws IOException on test failure
     */
    @Test
    public void test_05() throws IOException {
        try {
            singleCase(expected[5]);
            Assert.fail("Expected and exception");
        } catch (ParseCancellationException e) {
            System.out.println("Success: ParseCancellationException received");
        }
    }

    /**
     * @throws IOException on test failure
     */
    @Test
    public void test_06() throws IOException {
        singleCase(expected[6]);
    }

    /**
     * @throws IOException on test failure
     */
    @Test
    public void test_07() throws IOException {
        singleCase(expected[7]);
    }

    /**
     * @throws IOException on test failure
     */
    @Test
    public void test_08() throws IOException {
        singleCase(expected[8]);
    }

    /**
     * @throws IOException on test failure
     */
    @Test
    public void test_09() throws IOException {
        try {
            singleCase(expected[9]);
            Assert.fail("Expected and exception");
        } catch (ParseCancellationException e) {
            System.out.println("Success: ParseCancellationException received");
        }
    }

    /**
     * @throws IOException on test failure
     */
    @Test
    public void test_10() throws IOException {
        try {
            singleCase(expected[10]);
            Assert.fail("Expected and exception");
        } catch (ParseCancellationException e) {
            System.out.println("Success: ParseCancellationException received as expected");
        }
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

        RawModlObject rawModlObject = ModlObjectCreator.processModlParsed(input);
        String output = JsonPrinter.printModl(rawModlObject);
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