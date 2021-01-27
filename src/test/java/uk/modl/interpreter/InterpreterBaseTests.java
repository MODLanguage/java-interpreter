/*
 * MIT License
 *
 * Copyright (c) 2020 NUM Technology Ltd
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package uk.modl.interpreter;

import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import io.vavr.Tuple2;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import uk.modl.model.Modl;
import uk.modl.transforms.JacksonJsonNodeTransform;
import uk.modl.transforms.TransformationContext;
import uk.modl.utils.Util;

@Log4j2
public class InterpreterBaseTests {

  public static final int TIMEOUT_SECONDS = 10000;

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
    final int startFromTestNumber = 0;// Use this to skip tests manually to make it easier for debugging a specific
                                      // test.
    for (final TestInput testInput : list) {
      if (testNumber >= startFromTestNumber && !testInput.input.equals("DELETED")) {
        checkValidTestInput(testInput);
      }
      testNumber++;
    }

    if (errors.size() > 0) {
      log.info("--------------- Errors ----------------");
      for (final String error : errors) {
        log.error(error);
      }
      fail("Errors found.");
    }
  }

  private void checkValidTestInput(final TestInput testInput) {
    try {
      final TransformationContext ctx = TransformationContext.baseCtx(null, TIMEOUT_SECONDS);
      final Tuple2<TransformationContext, Modl> interpreted = new Interpreter().apply(ctx, testInput.input);
      final JacksonJsonNodeTransform jsonTransformer = new JacksonJsonNodeTransform(ctx);

      if (interpreted._2 != null) {
        final JsonNode jsonResult = jsonTransformer.apply(interpreted._2);

        final String output = Util.jsonNodeToString(jsonResult);
        if (output != null) {

          final String expected = testInput.expected_output.replace(" ", "").replace("\n", "").replace("\r", "");
          final String actual = output.replace(" ", "").replace("\n", "").replace("\r", "");

          if (!expected.equals(actual)) {
            log.info("Running test number: " + testInput.id);
            log.info("Input : " + testInput.input);
            log.info("Minimised : " + testInput.minimised_modl);
            log.info("Expected : " + testInput.expected_output);
            log.info("Actual : " + output);

            errors.add("Test: " + testInput.id + "\nExpected: " + testInput.expected_output + "\n" + "Actual  : "
                + output + "\n");
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
      log.error("Test: " + testInput.id + " - exception: " + e, e);
      errors.add("Test: " + testInput.id + "\nExpected: " + testInput.expected_output + "\n" + "Actual  : "
          + e.getMessage() + "\n");
    }
  }

  @Test
  public void testErrorTest() {
    // Go through grammar_test/error_tests.json making sure all tests raise an error
    // of some kind
    final List<TestInput> list = load.apply("../grammar/tests/error_tests.json");
    int testNumber = 1;
    final int startFromTestNumber = 0;// Use this to skip tests manually to make it easier for debugging a specific
                                      // test.
    for (final TestInput testInput : list) {
      if (testNumber >= startFromTestNumber) {
        checkInvalidTestInput(testInput);
      }
      testNumber++;
    }
    if (errors.size() > 0) {
      log.info("--------------- Errors ----------------");
      for (final String error : errors) {
        log.error(error);
      }
      fail("Errors found.");
    }
  }

  private void checkInvalidTestInput(final TestInput testInput) {
    try {
      final TransformationContext ctx = TransformationContext.baseCtx(null, TIMEOUT_SECONDS);
      final Tuple2<TransformationContext, Modl> interpreted = new Interpreter().apply(ctx, testInput.input);
      final JacksonJsonNodeTransform jsonTransformer = new JacksonJsonNodeTransform(ctx);
      if (interpreted._2 != null) {
        final JsonNode jsonResult = jsonTransformer.apply(interpreted._2);

        final String output = Util.jsonNodeToString(jsonResult);
        if (output != null) {

          final String expected = testInput.expected_output.replace(" ", "").replace("\n", "").replace("\r", "");
          final String actual = output.replace(" ", "").replace("\n", "").replace("\r", "");

          if (!expected.equals(actual)) {
            log.info("Running test number: " + testInput.id);
            log.info("Input : " + testInput.input);
            log.info("Minimised : " + testInput.minimised_modl);
            log.info("Expected : " + testInput.expected_output);

            errors.add("Test: " + testInput.id + "\nExpected: " + testInput.expected_output + "\n" + "Actual  : "
                + output + "\n");
          } else {
            log.info("Test: " + testInput.id + " - no errors\n");
          }

        } else {
          errors.add("Test: " + testInput.id + "\nExpected: " + testInput.expected_output + "\n" + "Actual  : null\n");
        }

      } else {
        log.error("Test: " + testInput.id + " - no result\n");

      }
      fail("Expected error");
    } catch (final Exception e) {
      if (!testInput.expected_output.equals(e.getMessage())) {
        errors.add("Test: " + testInput.id + "\nExpected: " + testInput.expected_output + "\n" + "Actual  : "
            + e.getMessage() + "\n");
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