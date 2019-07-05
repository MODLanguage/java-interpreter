package uk.modl.parser;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.parser.printers.JsonPrinter;

import java.io.IOException;

/**
 * Test GitHub grammar issue#5
 */
public class NewlinesBeforeSemicolonsTest {
    private final static String[][] expected = {{
            "a=1:2:3\n" +
                    ";\n" +
                    "b=4:5:6\n" +
                    ";\n", "[ {\n" +
            "  \"a\" : [ 1, 2, 3 ]\n" +
            "}, {\n" +
            "  \"b\" : [ 4, 5, 6 ]\n" +
            "} ]"
    }, {
            "a=1:2:3\n" +
                    ";\n" +
                    "b=4:5:6\n" +
                    ";;\n", "ParseCancellationException"
    }, {
            "a=1:2:3\n" +
                    ";\n" +
                    "b=4:5:6\n" +
                    ";\n;\n", "ParseCancellationException"
    }, {
            "a=1:2:3\n" +
                    ";;\n" +
                    "b=4:5:6", "ParseCancellationException"
    }, {
            "a=1:2:3\n" +
                    ";\n;\n" +
                    "b=4:5:6", "ParseCancellationException"
    }, {
            "a=1:2:3;\n;\n" +
                    "b=4:5:6", "ParseCancellationException"
    }, {
            "a=1:2:3;;\n" +
                    "b=4:5:6", "ParseCancellationException"
    }, {
            "a=1:2:3;\n" +
                    "b=4:5:6", "[ {\n" +
            "  \"a\" : [ 1, 2, 3 ]\n" +
            "}, {\n" +
            "  \"b\" : [ 4, 5, 6 ]\n" +
            "} ]"
    }, {
            "a=1:2:3;\n" +
                    "b=4:5:6", "[ {\n" +
            "  \"a\" : [ 1, 2, 3 ]\n" +
            "}, {\n" +
            "  \"b\" : [ 4, 5, 6 ]\n" +
            "} ]"
    }, {
            "a=1:2:3;b=4:5:6", "[ {\n" +
            "  \"a\" : [ 1, 2, 3 ]\n" +
            "}, {\n" +
            "  \"b\" : [ 4, 5, 6 ]\n" +
            "} ]"
    }, {
            "a=1:2:3;b=4:5:6;", "[ {\n" +
            "  \"a\" : [ 1, 2, 3 ]\n" +
            "}, {\n" +
            "  \"b\" : [ 4, 5, 6 ]\n" +
            "} ]"
    }, {
            "a=1:2:3;b=4:5:6\n\n;", "[ {\n" +
            "  \"a\" : [ 1, 2, 3 ]\n" +
            "}, {\n" +
            "  \"b\" : [ 4, 5, 6 ]\n" +
            "} ]"
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
        try {
            singleCase(expected[1]);
            Assert.fail("Expected a Parser Error");
        } catch (Exception e) {
            if (!e.getMessage()
                    .startsWith("Parser Error:")) {
                Assert.fail("Expected a Parser Error");
            }
        }
    }

    /**
     * @throws IOException on test failure
     */
    @Test
    public void test_02() throws IOException {
        try {
            singleCase(expected[2]);
            Assert.fail("Expected a ParseCancellationException");
        } catch (Exception e) {
            if (!e.getMessage()
                    .startsWith("Parser Error:")) {
                Assert.fail("Expected a Parser Error");
            }
        }
    }

    /**
     * @throws IOException on test failure
     */
    @Test
    public void test_03() throws IOException {
        try {
            singleCase(expected[3]);
            Assert.fail("Expected a ParseCancellationException");
        } catch (Exception e) {
            if (!e.getMessage()
                    .startsWith("Parser Error:")) {
                Assert.fail("Expected a Parser Error");
            }
        }
    }

    /**
     * @throws IOException on test failure
     */
    @Test
    public void test_04() throws IOException {
        try {
            singleCase(expected[4]);
            Assert.fail("Expected a ParseCancellationException");
        } catch (Exception e) {
            if (!e.getMessage()
                    .startsWith("Parser Error:")) {
                Assert.fail("Expected a Parser Error");
            }
        }
    }

    /**
     * @throws IOException on test failure
     */
    @Test
    public void test_05() throws IOException {
        try {
            singleCase(expected[5]);
            Assert.fail("Expected a ParseCancellationException");
        } catch (Exception e) {
            if (!e.getMessage()
                    .startsWith("Parser Error:")) {
                Assert.fail("Expected a Parser Error");
            }
        }
    }

    /**
     * @throws IOException on test failure
     */
    @Test
    public void test_06() throws IOException {
        try {
            singleCase(expected[6]);
            Assert.fail("Expected a ParseCancellationException");
        } catch (Exception e) {
            if (!e.getMessage()
                    .startsWith("Parser Error:")) {
                Assert.fail("Expected a Parser Error");
            }
        }
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
        singleCase(expected[9]);
    }

    /**
     * @throws IOException on test failure
     */
    @Test
    public void test_10() throws IOException {
        singleCase(expected[10]);
    }

    /**
     * @throws IOException on test failure
     */
    @Test
    public void test_11() throws IOException {
        singleCase(expected[11]);
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