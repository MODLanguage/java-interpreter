package uk.modl.transforms;

import io.vavr.collection.*;
import io.vavr.control.Option;
import lombok.Value;
import lombok.With;
import org.apache.commons.lang3.StringUtils;
import uk.modl.model.Array;
import uk.modl.model.Pair;

/**
 * Stores context needed by other parts of the interpreter
 */
@Value
@With
public class TransformationContext {

    /**
     * Possible targets of references
     */
    Map<String, Pair> pairs;

    /**
     * The Modl Object Index
     */
    uk.modl.model.Array objectIndex;

    /**
     * Files loaded by a *load instruction
     */
    Vector<String> filesLoaded;

    /**
     * Methods defined by a *method instruction
     */
    Set<StarMethodTransform.MethodInstruction> methods;

    /**
     * Methods indexed by *id
     */
    Map<String, StarMethodTransform.MethodInstruction> methodsById;

    /**
     * Methods indexed by *name
     */
    Map<String, StarMethodTransform.MethodInstruction> methodsByName;

    /**
     * Classes defined by a *class instruction
     */
    Set<StarClassTransform.ClassInstruction> classes;

    /**
     * Classes indexed by *id
     */
    Map<String, StarClassTransform.ClassInstruction> classesById;

    /**
     * Classes indexed by *name
     */
    Map<String, StarClassTransform.ClassInstruction> classesByName;

    public static TransformationContext emptyCtx() {
        return new TransformationContext(LinkedHashMap.empty(), new Array(Vector.empty()), Vector.empty(), LinkedHashSet.empty(), LinkedHashMap.empty(), LinkedHashMap.empty(), LinkedHashSet.empty(), LinkedHashMap.empty(), LinkedHashMap.empty());
    }

    /**
     * Add files loaded by a *load instruction
     *
     * @param filenames a List of String filenames
     */
    public TransformationContext addFilesLoaded(final Vector<String> filenames) {
        return this.withFilesLoaded(filenames.appendAll(filesLoaded));
    }

    /**
     * Add a Method defined by a *method instruction
     *
     * @param mi a StarMethodTransform.MethodInstruction
     */
    public TransformationContext addMethodInstruction(final StarMethodTransform.MethodInstruction mi) {
        final Set<StarMethodTransform.MethodInstruction> updatedMethods = methods.add(mi);
        final Map<String, StarMethodTransform.MethodInstruction> updatedMethodsById = methodsById.put(mi.getId(), mi);

        final Map<String, StarMethodTransform.MethodInstruction> updatedMethodsByName = (StringUtils.isNotEmpty(mi.getName())) ? methodsByName.put(mi.getName(), mi) : methodsByName;

        return this.withMethods(updatedMethods)
                .withMethodsById(updatedMethodsById)
                .withMethodsByName(updatedMethodsByName);
    }

    /**
     * Add a class defined by a *class instruction
     *
     * @param ci a StarClassTransform.ClassInstruction
     */
    public TransformationContext addClassInstruction(final StarClassTransform.ClassInstruction ci) {
        final Set<StarClassTransform.ClassInstruction> updatedClasses = classes.add(ci);
        final Map<String, StarClassTransform.ClassInstruction> updatedClassesById = classesById.put(ci.getId(), ci);

        final Map<String, StarClassTransform.ClassInstruction> updatedClassesByName = (StringUtils.isNotEmpty(ci.getName())) ? classesByName.put(ci.getName(), ci) : classesByName;

        return this.withClasses(updatedClasses)
                .withClassesById(updatedClassesById)
                .withClassesByName(updatedClassesByName);
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

    public TransformationContext addPair(final String key, final Pair p) {
        return this.withPairs(pairs.put(key, p));
    }

    public Option<StarMethodTransform.MethodInstruction> getMethodByNameOrId(final String idOrName) {
        return methodsById.get(idOrName)
                .orElse(methodsByName.get(idOrName));
    }

}
