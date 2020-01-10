package uk.modl.interpreter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Either;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import uk.modl.error.Error;
import uk.modl.model.Modl;

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

    private static Interpreter interpreter = new Interpreter();
    private List<String> errors = new ArrayList<>();
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * Read a set of tests from a file
     *
     * @param filename
     * @return
     */
    private Function<String, List<TestInput>> load = (filename) -> {
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
                log.info("Running test number: " + testNumber);
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
        log.info("Input : " + testInput.input);
        log.info("Minimised : " + testInput.minimised_modl);
        log.info("Expected : " + testInput.expected_output);

        try {
            final Either<Error, Modl> maybeModlObject = interpreter.apply(testInput.input);
            if (maybeModlObject.isRight()) {
                log.info("Test: " + testInput.id + " - no errors\n");
            } else {
                log.error("Test: " + testInput.id + " - no result\n");

            }
        } catch (final Exception e) {
            log.error(e);
            errors.add("Test: " + testInput.id + "\nExpected: " + testInput.expected_output + "\n" + "Actual  : " + e.getMessage() + "\n");
        }
    }

    @Test
    public void testErrorTest() throws Exception {
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
            interpreter.apply(testInput.input);
            fail("Expected error");
        } catch (Exception e) {
            if (!testInput.expected_output.equals(e.getMessage())) {
                errors.add("Test: " + testInput.id + "\nExpected: " + testInput.expected_output + "\n" + "Actual  : " + e.getMessage() + "\n");
            }
        }
    }

    public static class TestInput {
        public String input;
        public String minimised_modl;
        public String expected_output;
        public String[] tested_features;
        public int id;

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
}