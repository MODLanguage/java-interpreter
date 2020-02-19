package uk.modl.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.Function1;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import uk.modl.extractors.StarLoadExtractor;
import uk.modl.model.Array;
import uk.modl.model.PairValue;
import uk.modl.model.Primitive;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;

public class Util {
    /**
     * Map a filename to Either an Error or the file contents as a String
     */
    public static Function1<StarLoadExtractor.FileSpec, Tuple2<StarLoadExtractor.FileSpec, String>> getFileContents = (spec) -> {
        try {
            if (spec.filename.startsWith("http")) {
                final URL url = new URL(spec.filename);
                return Tuple.of(spec, new Scanner(url.openStream(), StandardCharsets.UTF_8.name()).useDelimiter("\\A")
                        .next());
            } else if (Files.exists(Paths.get(spec.filename))) {
                return Tuple.of(spec, String.join("", Files.readAllLines(Paths.get(spec.filename))));
            }
            return null;
        } catch (final Exception e) {
            throw new RuntimeException("Could not interpret " + e.getMessage());
        }
    };

    /**
     * Map a PairValue to a list of Strings - for use as file names.
     * This is only applicable to *load MODL instructions
     */
    public static Function1<PairValue, List<String>> getFilenames = (pairValue) -> {
        if (pairValue instanceof Primitive) {
            final Primitive pv = (Primitive) pairValue;
            return List.of(pv.toString());
        }
        if (pairValue instanceof Array) {
            final Array a = (Array) pairValue;
            return List.ofAll(a.arrayItems.map(Objects::toString));
        }
        return List.empty();
    };

    /**
     * Render a JSON Node to a String.
     */
    public static Function1<JsonNode, String> jsonNodeToString = (jsonNode -> {
        try {
            return new ObjectMapper().writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    });

}
