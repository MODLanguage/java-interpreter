package uk.modl.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.Function1;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Vector;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import uk.modl.extractors.StarLoadExtractor;
import uk.modl.model.Array;
import uk.modl.model.PairValue;
import uk.modl.model.Primitive;
import uk.modl.model.ValueItem;
import uk.modl.parser.errors.InterpreterError;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class Util {

    public final String GRAVE = "`";

    public final String DOUBLEQUOTE = "\"";

    /**
     * A Regex to match the parameters of a MODL replace method
     */
    private final Pattern replacerPattern = Pattern.compile("^r<(.*),(.*)>$");

    /**
     * A Regex to match the parameters of a MODL trim method
     */
    private final Pattern trimmerPattern = Pattern.compile("^t<(.*)>$");

    /**
     * Map a filename to Either an Error or the file contents as a String
     */
    public Function1<StarLoadExtractor.FileSpec, Tuple2<StarLoadExtractor.FileSpec, String>> getFileContents = (spec) -> {
        try {
            if (spec.getFilename()
                    .startsWith("http")) {
                final URL url = new URL(spec.getFilename());
                return Tuple.of(spec, new Scanner(url.openStream(), StandardCharsets.UTF_8.name()).useDelimiter("\\A")
                        .next());
            } else if (Files.exists(Paths.get(spec.getFilename()))) {
                return Tuple.of(spec, String.join("", Files.readAllLines(Paths.get(spec.getFilename()))));
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
    public Function1<PairValue, Vector<String>> getFilenames = (pairValue) -> {
        if (pairValue instanceof Primitive) {
            final Primitive pv = (Primitive) pairValue;
            return Vector.of(pv.toString());
        }
        if (pairValue instanceof Array) {
            final Array a = (Array) pairValue;
            return Vector.ofAll(a.getArrayItems()
                    .map(Objects::toString));
        }
        return Vector.empty();
    };

    /**
     * Render a JSON Node to a String.
     */
    public Function1<JsonNode, String> jsonNodeToString = (jsonNode -> {
        try {
            return new ObjectMapper().writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    });

    /**
     * Remove single quotes from a String
     *
     * @param text the possibly quoted String
     * @return an unquoted String
     */
    public String unquote(final String text) {
        return StringUtils.removeEnd(StringUtils.removeStart(StringUtils.removeEnd(StringUtils.removeStart(text, GRAVE), GRAVE), DOUBLEQUOTE), DOUBLEQUOTE);
    }

    /**
     * Replace parts of a String
     *
     * @param spec a spec of the form: r&lt;this,that&gt;
     * @param s    the String to be processed
     * @return the updated String
     */
    public String replacer(final String spec, final String s) {
        final Matcher matcher = replacerPattern.matcher(spec);

        if (matcher.find()) {
            final String text = matcher.group(1);
            final String newText = Util.unquote(matcher.group(2));

            return s.replace(text, newText);
        } else {
            throw new InterpreterError("Invalid method: " + spec);
        }
    }

    /**
     * Trim a String
     *
     * @param spec a spec of the form: t&lt;this&gt;
     * @param s    the String to be processed
     * @return the updated String
     */
    public String trimmer(final String spec, final String s) {
        final Matcher matcher = trimmerPattern.matcher(spec);

        if (matcher.find()) {
            final String text = matcher.group(1);
            final int i = s.indexOf(text);
            if (i > -1) {
                return s.substring(0, i);
            }
        } else {
            throw new InterpreterError("Invalid method: " + spec);
        }
        return s;
    }

    public boolean greaterThanAll(final ValueItem lhs, final Vector<ValueItem> values) {
        return !values.find(v -> {
            final double v2 = toDouble(v.toString());
            return toDouble(lhs.toString()) <= v2;
        })
                .isDefined();
    }

    private double toDouble(final String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            throw new InterpreterError("Invalid numeric value: " + e.getMessage());
        }
    }

    public boolean greaterThanOrEqualToAll(final ValueItem lhs, final Vector<ValueItem> values) {
        return !values.find(v -> {
            final double v2 = toDouble(v.toString());
            return toDouble(lhs.toString()) < v2;
        })
                .isDefined();
    }

    public boolean lessThanAll(final ValueItem lhs, final Vector<ValueItem> values) {
        return !values.find(v -> {
            final double v2 = toDouble(v.toString());
            return toDouble(lhs.toString()) >= v2;
        })
                .isDefined();
    }

    public boolean lessThanOrEqualToAll(final ValueItem lhs, final Vector<ValueItem> values) {
        return !values.find(v -> {
            final double v2 = toDouble(v.toString());
            return toDouble(lhs.toString()) > v2;
        })
                .isDefined();
    }

    public boolean truthy(final PairValue value) {
        if (value != null) {
            final String s = value.toString();
            return !s.equalsIgnoreCase("null") && !s.equalsIgnoreCase("000") && !s.equalsIgnoreCase("00") && !s.equalsIgnoreCase("false");
        }
        return false;
    }

}
