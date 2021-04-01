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

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import uk.modl.interpreter.Interpreter;

@Log4j2
public class Interpret {
  private static final Interpreter interpreter = new Interpreter();

  public static void main(final String... args) {
    if (args.length == 0) {
      log.info("Usage: java -jar ./build/libs/interpreter-<version>.jar <modl-file-name> [modl-file-name] ...");
    }

    final int sum = Arrays.stream(args).mapToInt(filename -> {
      try {
        final List<String> allLines = Files.readAllLines(Paths.get(filename));

        final String modlString = String.join("\n", allLines);

        final String result = interpreter.interpretToPrettyJsonString(modlString);

        log.info(result);

      } catch (final Exception e) {
        log.error(e.getMessage());
        return 1;
      }
      return 0;
    }).sum();
    System.exit(sum);
  }

}
