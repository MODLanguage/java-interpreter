package uk.modl.extractors;

import io.vavr.collection.Vector;
import lombok.Getter;
import lombok.Value;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import uk.modl.model.Pair;
import uk.modl.utils.Util;
import uk.modl.visitor.ModlVisitorBase;

@Getter
public class StarLoadExtractor extends ModlVisitorBase {

    private Vector<LoadSet> loadSets = Vector.empty();

    /**
     * Check whether the key represents a LOAD instruction
     *
     * @param key the String to check
     * @return true if the key represents a LOAD instruction
     */
    public static boolean isLoadInstruction(final String key) {
        return StringUtils.equalsAnyIgnoreCase(key, "*l", "*load");
    }

    public void accept(final Pair pair) {
        final String key = pair.getKey();

        if (isLoadInstruction(key)) {

            val specs = Util.getFilenames.apply(pair.getValue())
                    .map(Util::normalize);

            loadSets = loadSets.append(new LoadSet(pair, specs));
        }
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
