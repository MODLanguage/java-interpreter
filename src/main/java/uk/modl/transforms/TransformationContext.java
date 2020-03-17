package uk.modl.transforms;

import io.vavr.collection.*;
import io.vavr.control.Option;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * Stores context needed by other parts of the interpreter
 */
public class TransformationContext {

    /**
     * Files loaded by a *load instruction
     */
    @Getter
    private Vector<String> filesLoaded = Vector.empty();

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
     * Classes indexed by *id
     */
    @Getter
    private Map<String, StarClassTransform.ClassInstruction> classesById = HashMap.empty();

    /**
     * Classes indexed by *name
     */
    @Getter
    private Map<String, StarClassTransform.ClassInstruction> classesByName = HashMap.empty();

    /**
     * Add files loaded by a *load instruction
     *
     * @param filenames a List of String filenames
     */
    public void addFilesLoaded(final Vector<String> filenames) {
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
        classesById = classesById.put(ci.id, ci);
        if (StringUtils.isNotEmpty(ci.name)) {
            classesByName = classesByName.put(ci.name, ci);
        }
    }

    /**
     * Do we have a class with the given name or id?
     *
     * @param idOrName the name or id String
     * @return true if we have a class with the supplied name or id
     */
    public boolean hasClass(final String idOrName) {
        return classesById.containsKey(idOrName) || classesByName.containsKey(idOrName);
    }

    /**
     * Get a class by name or id if we have one.
     *
     * @param idOrName the id or name String
     * @return an Option of a StarClassTransform.ClassInstruction
     */
    public Option<StarClassTransform.ClassInstruction> getClassByNameOrId(final String idOrName) {
        final Option<StarClassTransform.ClassInstruction> clss = classesById.get(idOrName);
        return (clss.isDefined()) ? clss : classesByName.get(idOrName);
    }
}
