package uk.modl.interpreter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.modlObject.ModlObject;
import uk.modl.parser.printers.JsonPrinter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by alex on 11/03/2019.
 */
public class GrammarTestRunnerTest extends TestCase {

    private List<String> errors = new ArrayList<>();

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testBaseTest() throws Exception {
        // TODO Go through grammar_tests/base_tests.json making sure all expected stuff is correct
        try (InputStream fileStream = new FileInputStream("../grammar/tests/base_tests.json")) {
            List<TestInput> list = mapper.readValue(fileStream, new TypeReference<LinkedList<TestInput>>() {
            });
            int testNumber = 1;
            int
                    startFromTestNumber =
                    0;// Use this to skip tests manually to make it easier for debugging a specific test.
            for (TestInput testInput : list) {
                if (testNumber >= startFromTestNumber) {
                    System.out.println("Running test number: " + testNumber);
                    if (testInput.input.equals("DELETED")) {
                        testNumber++;
                        continue;
                    }
                    try {
                        checkValidTestInput(testInput);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
                testNumber++;
            }

        } finally {
            if (errors.size() > 0) {

                System.out.println("--------------- Errors ----------------");
                for (final String error : errors) {
                    System.out.println(error);
                }

                Assert.fail("Errors found.");
            }
        }

    }

    private void checkValidTestInput(final TestInput testInput) throws IOException {
        System.out.println("Input : " + testInput.input);
        System.out.println("Minimised : " + testInput.minimised_modl);
        System.out.println("Expected : " + testInput.expected_output);

        try {
            ModlObject modlObject = Interpreter.interpret(testInput.input);
            String output = JsonPrinter.printModl(modlObject);
            System.out.println("Output : " + output);
            final String expected = testInput.expected_output.replace(" ", "")
                    .replace("\n", "")
                    .replace("\r", "");
            final String actual = output.replace(" ", "")
                    .replace("\n", "")
                    .replace("\r", "");

            if (!expected.equals(actual)) {
                errors.add("Test: " + testInput.id + "\nExpected: " + testInput.expected_output + "\n" + "Actual  : " + output + "\n");
            }
        } catch (final Exception e) {
            e.printStackTrace();
            errors.add("Test: " + testInput.id + "\nExpected: " + testInput.expected_output + "\n" + "Actual  : " + e.getMessage() + "\n");
        }
    }

    @Test
    public void testErrorTest() throws Exception {
        // Go through grammar_test/error_tests.json making sure all tests raise an error of some kind
        try (InputStream fileStream = new FileInputStream("../grammar/tests/error_tests.json")) {
            List<TestInput> list = mapper.readValue(fileStream, new TypeReference<LinkedList<TestInput>>() {
            });
            int testNumber = 1;
            int
                    startFromTestNumber =
                    0;// Use this to skip tests manually to make it easier for debugging a specific test.
            for (TestInput testInput : list) {
                if (testNumber >= startFromTestNumber) {
                    System.out.println("Running test number: " + testNumber);
                    checkInValidTestInput(testInput);
                }
                testNumber++;
            }
        } finally {
            if (errors.size() > 0) {

                System.out.println("--------------- Errors ----------------");
                for (final String error : errors) {
                    System.out.println(error);
                }

                Assert.fail("Errors found.");
            }
        }

    }

    private void checkInValidTestInput(final TestInput testInput) {
        System.out.println("Failing Input : " + testInput.input);
        try {
            ModlObject modlObject = Interpreter.interpret(testInput.input);
            fail("Expected error");
        } catch (Exception e) {
            if (!testInput.expected_output.equals(e.getMessage())) {
                errors.add("Test: " + testInput.id + "\nExpected: " + testInput.expected_output + "\n" + "Actual  : " + e.getMessage() + "\n");
            }
        }
    }

    public static class TestInput {
        String input;
        String minimised_modl;
        String expected_output;
        String[] tested_features;
        int id;

        public TestInput() {
        }

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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    // TODO Add Tony's new test cases to the base_tests or error_tests json files so they are available on all platforms
}
