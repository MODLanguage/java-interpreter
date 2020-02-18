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
     * Methods defined by a *method instruction
     */
    @Getter
    private List<StarMethodTransform.MethodInstruction> methods = List.empty();

    /**
     * Add files loaded by a *load instruction
     *
     * @param filenames a List of String filenames
     */
    public void addFilesLoaded(final List<String> filenames) {
        filesLoaded = filenames.appendAll(filesLoaded);
    }

    /**
     * Add a Method defined by a *method instruction
     *
     * @param mi a StarMethodTransform.MethodInstruction
     */
    public void addMethodInstruction(final StarMethodTransform.MethodInstruction mi) {
        methods = methods.append(mi);
    }
}
