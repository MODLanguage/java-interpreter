package uk.modl.extractors;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import lombok.Getter;
import lombok.NonNull;
import uk.modl.model.Pair;
import uk.modl.utils.Util;
import uk.modl.visitor.ModlVisitorBase;

@Getter
public class StarLoadExtractor extends ModlVisitorBase {

    private List<Tuple2<List<String>, Pair>> filenamePairs = List.empty();
    private boolean immutable;

    @Override
    public void accept(final Pair pair) {
        final String key = pair.key;

        immutable |= (key.equals("*L") || key.equals("*LOAD"));

        if (filenamePairs.size() > 0 && immutable) {
            throw new RuntimeException("Interpreter Error: Cannot load multiple files after *LOAD instruction");
        } else {
            final String lowerCase = key.toLowerCase();
            if (lowerCase.equals("*l") || lowerCase.equals("*load")) {
                final List<String> normalizedFileNames = Util.getFilenames.apply(pair.value)
                        .map(this::normalize);
                filenamePairs = List.of(Tuple.of(normalizedFileNames, pair));
            }
        }
    }

    /**
     * Remove graves etc.
     *
     * @param text the String to be normalized
     * @return the normalized String
     */
    private String normalize(@NonNull final String text) {
        String normalized = text;

        if (normalized != null && normalized.length() > 1 && normalized.startsWith("`") && normalized.endsWith("`")) {
            normalized = normalized.substring(1, normalized.length() - 1);
        }

        if (normalized != null && normalized.length() > 1 && normalized.startsWith("\"") && normalized.endsWith("\"")) {
            normalized = normalized.substring(1, normalized.length() - 1);
        }

        if (normalized != null && !normalized.endsWith(".modl") && !normalized.endsWith(".txt")) {
            normalized += ".modl";
        }

        return normalized;
    }
}
