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

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import uk.modl.interpreter.Interpreter;
import uk.modl.transforms.JacksonJsonNodeTransform;
import uk.modl.transforms.TransformationContext;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Interpret {

    public static final int TIMEOUT_SECONDS = 10000;

    public static void main(final String... args) {
        if (args.length == 0) {
            System.out.println("Usage: java -cp ./build/libs/interpreter-<version>.jar <modl-file-name> [modl-file-name] ...");
        }

        final int sum = Arrays.stream(args)
                .mapToInt(filename -> {
                    try {
                        val path = Paths.get(filename);
                        val modlString = String.join("\n", Files.readAllLines(path));


                        val interpreter = new Interpreter();
                        val ctx = TransformationContext.baseCtx(new URL(path.toUri()
                                .toASCIIString()), TIMEOUT_SECONDS);
                        val interpreted = interpreter.apply(ctx, modlString);
                        val json = new JacksonJsonNodeTransform(ctx).apply(interpreted._2);


                        val result = new ObjectMapper().writerWithDefaultPrettyPrinter()
                                .writeValueAsString(json);

                        System.out.println(result);

                    } catch (final Exception e) {
                        System.err.println(e.getMessage());
                        return 1;
                    }
                    System.out.println();
                    return 0;
                })
                .sum();
        System.exit(sum);
    }

}
