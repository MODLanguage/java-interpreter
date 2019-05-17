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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by alex on 11/03/2019.
 */
public class ScratchTest extends TestCase {

    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testBaseTest() throws Exception {
        try (InputStream fileStream = new FileInputStream("grammar_tests/base_tests.json")) {
            List<TestInput> list = mapper.readValue(fileStream, new TypeReference<LinkedList<TestInput>>() {
            });
            int testNumber = 1;
            int targetTest = 33;
            for (TestInput testInput : list) {
                for (String feature : testInput.tested_features) {
                    if (feature.equals("object_ref")) {
                        if (testNumber >= targetTest) {
                            System.out.println("Running test number: " + testNumber);
                            checkValidTestInput(testInput);
                        }
                        testNumber++;
                    }
                }
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

    private void checkInValidTestInput(String testInput) {
        System.out.println("Failing Input : " + testInput);
        try {
            ModlObject modlObject = Interpreter.interpret(testInput);
            fail("Expected error");
        } catch (Exception e) {
            // OK
        }
    }

    public static class TestInput {
        String input;
        String minimised_modl;
        String expected_output;
        List<String> tested_features = new ArrayList<>();
        public TestInput() {
        }

        public List<String> getTested_features() {
            return tested_features;
        }

        public void setTested_features(List<String> tested_features) {
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

}
