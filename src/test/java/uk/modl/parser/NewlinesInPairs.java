package uk.modl.parser;

import org.junit.Assert;
import org.junit.Test;
import uk.modl.parser.printers.JsonPrinter;

import java.io.IOException;

/**
 * Test GitHub grammar issue#5
 */
public class NewlinesInPairs {
    private final static String[][] expected = {{
            "a = \n" +
                    "     b =\n" +
                    "     c\n" +
                    "d=e", "[ {\n" +
            "  \"a\" : {\n" +
            "    \"b\" : \"c\"\n" +
            "  }\n" +
            "}, {\n" +
            "  \"d\" : \"e\"\n" +
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