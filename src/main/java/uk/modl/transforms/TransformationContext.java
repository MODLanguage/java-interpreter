package uk.modl.transforms;

import io.vavr.collection.*;
import io.vavr.control.Option;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import uk.modl.model.Pair;

/**
 * Stores context needed by other parts of the interpreter
 */
public class TransformationContext {

    /**
     * Possible targets of references
     */
    @Getter
    private Map<String, Pair> pairs = HashMap.empty();
    /**
     * The Modl Object Index
     */
    @Getter
    private uk.modl.model.Array objectIndex;

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
        classesById = classesById.put(ci.getId(), ci);
        if (StringUtils.isNotEmpty(ci.getName())) {
            classesByName = classesByName.put(ci.getName(), ci);
        }
    }

    /**
     * Get a class by name or id if we have one.
     *
     * @param idOrName the id or name String
     * @return an Option of a StarClassTransform.ClassInstruction
     */
    public Option<StarClassTransform.ClassInstruction> getClassByNameOrId(final String idOrName) {
        return classesById.get(idOrName)
                .orElse(classesByName.get(idOrName));
    }

    public void setObjectIndex(final uk.modl.model.Array objectIndex) {
        this.objectIndex = objectIndex;
    }

    public void addPair(final String key, final Pair p) {
        pairs = pairs.put(key, p);
    }
}
