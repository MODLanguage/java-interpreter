package uk.modl.transforms;

import io.vavr.collection.LinkedHashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;
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
    private Set<StarMethodTransform.MethodInstruction> methods = LinkedHashSet.empty();

    /**
     * Classes defined by a *class instruction
     */
    @Getter
    private Set<StarClassTransform.ClassInstruction> classes = LinkedHashSet.empty();

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
        methods = methods.add(mi);
    }

    /**
     * Add a class defined by a *class instruction
     *
     * @param ci a StarClassTransform.ClassInstruction
     */
    public void addClassInstruction(final StarClassTransform.ClassInstruction ci) {
        classes = classes.add(ci);
    }
}
