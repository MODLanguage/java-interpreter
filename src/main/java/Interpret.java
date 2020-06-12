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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.Tuple2;
import uk.modl.interpreter.Interpreter;
import uk.modl.model.Modl;
import uk.modl.transforms.JacksonJsonNodeTransform;
import uk.modl.transforms.TransformationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Interpret {

    public static void main(final String... args) {
        if (args.length == 0) {
            System.out.println("Usage: java -cp ./build/libs/interpreter-0.1.1.jar Interpret <modl-file-name> [modl-file-name] ...");
        }

        Arrays.stream(args)
                .forEach(filename -> {
                    System.out.println("Processing file: " + filename);
                    try {
                        final Path path = Paths.get(filename);
                        final String modlString = String.join("", Files.readAllLines(path));


                        final Interpreter interpreter = new Interpreter();
                        final TransformationContext ctx = TransformationContext.emptyCtx();
                        final Tuple2<TransformationContext, Modl> interpreted = interpreter.apply(ctx, modlString);
                        final JsonNode json = new JacksonJsonNodeTransform(ctx).apply(interpreted._2);


                        final String result = new ObjectMapper().writerWithDefaultPrettyPrinter()
                                .writeValueAsString(json);

                        System.out.println(result);

                    } catch (final IOException e) {
                        System.err.println(e.getMessage());
                    }
                    System.out.println("Finished file: " + filename);
                    System.out.println();
                });
    }

}
