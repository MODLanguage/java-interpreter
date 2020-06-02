package uk.modl.extractors;

import io.vavr.collection.Vector;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import uk.modl.model.Pair;
import uk.modl.utils.Util;
import uk.modl.visitor.ModlVisitorBase;

@Getter
public class StarLoadExtractor extends ModlVisitorBase {

    private Vector<LoadSet> loadSets = Vector.empty();

    private boolean immutable = false;

    /**
     * Check whether the key represents a LOAD instruction
     *
     * @param key the String to check
     * @return true if the key represents a LOAD instruction
     */
    public static boolean isLoadInstruction(final String key) {
        return StringUtils.equalsAnyIgnoreCase(key, "*l", "*load");
    }

    /**
     * Check whether the key represents an immutable LOAD instruction
     *
     * @param key the String to check
     * @return true if the key represents an immutable LOAD instruction
     */
    private static boolean isImmutableLoadInstruction(final String key) {
        return StringUtils.equalsAny(key, "*L", "*LOAD");
    }

    @Override
    public void accept(final Pair pair) {
        final String key = pair.getKey();

        if (loadSets.size() > 0 && immutable) {
            throw new RuntimeException("Cannot load multiple files after *LOAD instruction");
        }

        if (isLoadInstruction(key)) {

            immutable |= isImmutableLoadInstruction(key);

            val specs = Util.getFilenames.apply(pair.getValue())
                    .map(this::normalize);

            loadSets = loadSets.append(new LoadSet(pair, specs));
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
            normalized = StringUtils.unwrap(normalized, "`");
        }

        if (normalized.length() > 1 && normalized.startsWith("\"") && normalized.endsWith("\"")) {
            // Remove quotes
            normalized = StringUtils.unwrap(normalized, "\"");
        }

        final boolean forceLoad = (normalized.endsWith("!"));
        normalized = StringUtils.removeEnd(normalized, "!");

        if (!normalized.endsWith(".modl") && !normalized.endsWith(".txt")) {
            // Add a file extension
            normalized += ".modl";
        }

        normalized = normalized.replace("~://", "://");

        return new FileSpec(normalized, forceLoad);
    }

    /**
     * A Pair can represent several files to be loaded.
     */
    @Value
    public static class LoadSet {

        Pair pair;

        Vector<FileSpec> fileSet;

    }

    /**
     * A file can be immutable or forced load, and has a filename
     */
    @Value
    public static class FileSpec {

        String filename;

        boolean forceLoad;

    }

}
