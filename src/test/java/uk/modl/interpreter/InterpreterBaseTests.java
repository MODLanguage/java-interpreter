package uk.modl.interpreter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import uk.modl.model.Modl;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.fail;

@Log4j2
public class InterpreterBaseTests {

    private static final JacksonJsonNodeTransformer jsonTransformer = new JacksonJsonNodeTransformer();
    private final List<String> errors = new ArrayList<>();
    private final ObjectMapper mapper = new ObjectMapper();
    /**
     * Read a set of tests from a file
     */
    private final Function<String, List<TestInput>> load = (filename) -> {
        try (final InputStream fileStream = new FileInputStream(filename)) {
            return mapper.readValue(fileStream, new TypeReference<LinkedList<TestInput>>() {
            });
        } catch (IOException e) {
            log.error("Error accessing file: {}", filename, e);
        }
        return Collections.emptyList();
    };

    @Test
    public void testBaseTest() {

        final List<TestInput> list = load.apply("../grammar/tests/base_tests.json");
        int testNumber = 1;
        final int startFromTestNumber = 0;// Use this to skip tests manually to make it easier for debugging a specific test.
        for (final TestInput testInput : list) {
            if (testNumber >= startFromTestNumber && !testInput.input.equals("DELETED")) {
                checkValidTestInput(testInput);
            }
            testNumber++;
        }

        if (errors.size() > 0) {
            log.info("--------------- Errors ----------------");
            for (final String error : errors) {
                log.info(error);
            }
            fail("Errors found.");
        }
    }

    private void checkValidTestInput(final TestInput testInput) {
        try {
            final Modl modl = new Interpreter().apply(testInput.input);
            if (modl != null) {
                final JsonNode jsonResult = jsonTransformer.apply(modl);

                final String output = Util.jsonNodeToString.apply(jsonResult);
                if (output != null) {

                    final String expected = testInput.expected_output.replace(" ", "")
                            .replace("\n", "")
                            .replace("\r", "");
                    final String actual = output.replace(" ", "")
                            .replace("\n", "")
                            .replace("\r", "");

                    if (!expected.equals(actual)) {
                        log.info("Running test number: " + testInput.id);
                        log.info("Input : " + testInput.input);
                        log.info("Minimised : " + testInput.minimised_modl);
                        log.info("Expected : " + testInput.expected_output);
                        log.info("Actual : " + output);

                        errors.add("Test: " + testInput.id + "\nExpected: " + testInput.expected_output + "\n" + "Actual  : " + output + "\n");
                    } else {
                        log.info("Test: " + testInput.id + " - no errors\n");
                    }

                } else {
                    errors.add("Test: " + testInput.id + "\nExpected: " + testInput.expected_output + "\n" + "Actual  : null\n");
                }

            } else {
                log.error("Test: " + testInput.id + " - no result\n");

            }
        } catch (final Exception e) {
            log.error("Test: " + testInput.id + " - exception: " + e);
            errors.add("Test: " + testInput.id + "\nExpected: " + testInput.expected_output + "\n" + "Actual  : " + e.getMessage() + "\n");
        }
    }

    @Test
    public void testErrorTest() {
        // Go through grammar_test/error_tests.json making sure all tests raise an error of some kind
        final List<TestInput> list = load.apply("../grammar/tests/error_tests.json");
        int testNumber = 1;
        final int startFromTestNumber = 0;// Use this to skip tests manually to make it easier for debugging a specific test.
        for (final TestInput testInput : list) {
            if (testNumber >= startFromTestNumber) {
                log.info("Running test number: " + testNumber);
                checkInValidTestInput(testInput);
            }
            testNumber++;
        }
        if (errors.size() > 0) {
            log.info("--------------- Errors ----------------");
            for (final String error : errors) {
                log.info(error);
            }
            fail("Errors found.");
        }
    }

    private void checkInValidTestInput(final TestInput testInput) {
        log.info("Failing Input : " + testInput.input);
        try {
            new Interpreter().apply(testInput.input);
            fail("Expected error");
        } catch (Exception e) {
            if (!testInput.expected_output.equals(e.getMessage())) {
                errors.add("Test: " + testInput.id + "\nExpected: " + testInput.expected_output + "\n" + "Actual  : " + e.getMessage() + "\n");
            }
        }
    }

    @Data
    public static class TestInput {
        public String input;
        public String minimised_modl;
        public String expected_output;
        public String[] tested_features;
        public int id;
    }
}