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

/**
 * A command-line tool to invoke the Interpreter
 */
@Log4j2
public class Interpret {
    /**
     * Program entry point
     *
     * @param args an array of file names containing MODL strings.
     */
    public static void main(final String... args) {
        if (args.length == 0) {
            log.info("Usage: java -jar ./build/libs/interpreter-<version>.jar <modl-file-name> [modl-file-name] ...");
        }

        /*
         * Read and interpret each file, and sum the status codes - 0 means all
         * succeeded.
         */
        final int sum = Arrays.stream(args)
                .mapToInt(filename -> {
                    try {
                        // Read the lines from the file
                        final List<String> allLines = Files.readAllLines(Paths.get(filename));

                        // Join the lines
                        final String modlString = String.join("\n", allLines);

                        // Interpret to formatted JSON
                        final String result = Interpreter.interpretToPrettyJsonString(modlString);

                        // Log the result.
                        log.info(result);

                    } catch (final Exception e) {
                        log.error(e.getMessage());
                        return 1;
                    }
                    return 0;
                })
                .sum();

        // `sum` is the count of the files that failed interpretation.
        System.exit(sum);
    }

}
