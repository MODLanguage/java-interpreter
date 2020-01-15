package uk.modl.extractors;

import io.vavr.collection.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import uk.modl.model.Pair;
import uk.modl.utils.Util;
import uk.modl.visitor.ModlVisitorBase;

@Getter
public class StarLoadExtractor extends ModlVisitorBase {

    private List<LoadSet> loadSets = List.empty();
    private boolean immutable = false;

    @Override
    public void accept(final Pair pair) {
        final String key = pair.key;

        if (loadSets.size() > 0 && immutable) {
            throw new RuntimeException("Interpreter Error: Cannot load multiple files after *LOAD instruction");
        } else {
            final String lowerCase = key.toLowerCase();
            if (lowerCase.equals("*l") || lowerCase.equals("*load")) {

                immutable |= (key.equals("*L") || key.equals("*LOAD"));

                final List<FileSpec> specs = Util.getFilenames.apply(pair.value)
                        .map(this::normalize);

                final LoadSet loadSet = new LoadSet(pair, specs);
                loadSets = loadSets.append(loadSet);
            }
        }
    }

    /**
     * Remove graves etc.
     *
     * @param text the String to be normalized
     * @return the normalized String
     */
    private FileSpec normalize(@NonNull final String text) {
        String normalized = text;

        if (normalized.length() > 1 && normalized.startsWith("`") && normalized.endsWith("`")) {
            // Remove graves
            normalized = normalized.substring(1, normalized.length() - 1);
        }

        if (normalized.length() > 1 && normalized.startsWith("\"") && normalized.endsWith("\"")) {
            // Remove quotes
            normalized = normalized.substring(1, normalized.length() - 1);
        }

        final boolean forceLoad = (normalized.endsWith("!"));
        if (forceLoad) {
            // Remove the trailing ! character
            normalized = normalized.substring(0, normalized.length() - 1);
        }

        if (!normalized.endsWith(".modl") && !normalized.endsWith(".txt")) {
            // Add a file extension
            normalized += ".modl";
        }

        return new FileSpec(normalized, forceLoad);
    }

    /**
     * A Pair can represent several files to be loaded.
     */
    @RequiredArgsConstructor
    public static class LoadSet {
        public final Pair pair;
        public final List<FileSpec> fileSet;
    }

    /**
     * A file can be immutable or forced load, and has a filename
     */
    @RequiredArgsConstructor
    public static class FileSpec {
        public final String filename;
        public final boolean forceLoad;
    }
}
