package uk.modl.transforms;

import io.vavr.collection.List;
import lombok.Getter;

/**
 * Stores context needed by other parts of the interpreter
 */
public class TransformationContext {

    /**
     * Files loaded by a *load instruction
     */
    @Getter
    private List<String> filesLoaded = List.empty();

    /**
     * Add files loaded by a *load instruction
     *
     * @param filenames a List of String filenames
     */
    public void addFilesLoaded(final List<String> filenames) {
        filesLoaded = filenames.appendAll(filesLoaded);
    }
}
