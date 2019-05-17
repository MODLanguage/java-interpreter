package uk.modl.interpreter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.junit.Test;
import uk.modl.modlObject.ModlObject;
import uk.modl.parser.printers.JsonPrinter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by alex on 11/03/2019.
 */
public class GrammarTestRunnerTest extends TestCase {

    ObjectMapper mapper = new ObjectMapper();

    public static class TestInput {
        public TestInput() {
        }

        String input;
        String minimised_modl;
        String expected_output;
        String [] tested_features;

        public String[] getTested_features() {
            return tested_features;
        }

        public void setTested_features(String[] tested_features) {
            this.tested_features = tested_features;
        }

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }

        public String getMinimised_modl() {
            return minimised_modl;
        }

        public void setMinimised_modl(String minised_modl) {
            this.minimised_modl = minised_modl;
        }

        public String getExpected_output() {
            return expected_output;
        }

        public void setExpected_output(String expected_output) {
            this.expected_output = expected_output;
        }
    }

    @Test
    public void testBaseTest() throws Exception {
        // TODO Go through grammar_tests/base_tests.json making sure all expected stuff is correct
        try (InputStream fileStream = new FileInputStream("grammar_tests/base_tests.json")) {
            List<TestInput> list = mapper.readValue(fileStream, new TypeReference<LinkedList<TestInput>>() {
            });
            int testNumber = 1;
            int startFromTestNumber = 71;
            for (TestInput testInput : list) {
                if (testNumber >= startFromTestNumber) {
                    System.out.println("Running test number: " + testNumber);
                    checkValidTestInput(testInput);
                }
                testNumber++;
            }
        }

    }

    private void checkValidTestInput(TestInput testInput) throws IOException {
        System.out.println("Input : " + testInput.input);
        System.out.println("Minimised : " + testInput.minimised_modl);
        System.out.println("Expected : " + testInput.expected_output);

        ModlObject modlObject = Interpreter.interpret(testInput.input);
        String output = JsonPrinter.printModl(modlObject);
        System.out.println("Output : " + output);
        assertEquals(testInput.expected_output.replace(" ", "").replace("\n", "").replace("\r", ""),
                output.replace(" ", "").replace("\n", "").replace("\r", ""));

    }

    @Test
    public void testErrorTest() throws Exception {
        // Go through grammar_test/error_tests.json making sure all tests raise an error of some kind
        try (InputStream fileStream = new FileInputStream("grammar_tests/error_tests.json")) {
            List<String> list = mapper.readValue(fileStream, new TypeReference<LinkedList<String>>() {
            });
            for (String testInput : list) {
                checkInValidTestInput(testInput);
            }
        }

    }

    private void checkInValidTestInput(String testInput) {
        System.out.println("Failing Input : " + testInput);
        try {
            ModlObject modlObject = Interpreter.interpret(testInput);
            fail("Expected error");
        } catch (Exception e) {
            // OK
        }
    }

    // TODO Add Tony's new test cases to the base_tests or error_tests json files so they are available on all platforms
}
