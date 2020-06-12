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
            System.out.println("Usage: java -cp [path-to-jar]/interpreter-0.0.17.jar Interpret <modl-file-name> [modl-file-name] ...");
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
