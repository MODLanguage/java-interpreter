/*
 * MIT License
 *
 * Copyright (c) 2020 NUM Technology Ltd
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package uk.modl.transforms;

import io.vavr.collection.*;
import io.vavr.control.Option;
import lombok.Value;
import lombok.With;
import org.apache.commons.lang3.StringUtils;
import uk.modl.ancestry.Ancestry;
import uk.modl.model.ArrayItem;

/**
 * Stores context needed by other parts of the interpreter
 */
@Value(staticConstructor = "of")
@With
public class TransformationContext {

    public static final int VERSION = 1;

    public static final boolean STAR_LOAD_IMMUTABLE = false;

    public static final boolean STAR_CLASS_IMMUTABLE = false;

    /**
     * This is a mutable object to keep track of child->parent relations.
     */
    Ancestry ancestry;

    /**
     * Interpreter version
     */
    int version;

    /**
     * StarLoadImmutable
     */
    boolean starLoadImmutable;

    /**
     * StarClassImmutable
     */
    boolean starClassImmutable;

    /**
     * The Modl Object Index
     */
    Vector<ArrayItem> objectIndex;

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
        return new TransformationContext(new Ancestry(), VERSION, STAR_LOAD_IMMUTABLE, STAR_CLASS_IMMUTABLE, Vector.empty(), Vector.empty(), LinkedHashSet.empty(), LinkedHashMap.empty(), LinkedHashMap.empty(), LinkedHashSet.empty(), LinkedHashMap.empty(), LinkedHashMap.empty());
    }

    /**
     * Add files loaded by a *load instruction
     *
     * @param filenames a List of String filenames
     * @return TransformationContext
     */
    public TransformationContext addFilesLoaded(final Vector<String> filenames) {
        return this.withFilesLoaded(filenames.appendAll(filesLoaded));
    }

    /**
     * Add a Method defined by a *method instruction
     *
     * @param mi a StarMethodTransform.MethodInstruction
     * @return TransformationContext
     */
    public TransformationContext addMethodInstruction(final StarMethodTransform.MethodInstruction mi) {
        if (methodsById.containsKey(mi.getId()) || methodsByName.containsKey(mi.getId())) {
            throw new RuntimeException("Duplicate method name or id: " + mi.getId());
        }

        if (methodsById.containsKey(mi.getName()) || methodsByName.containsKey(mi.getName())) {
            throw new RuntimeException("Duplicate method name or id: " + mi.getName());
        }

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
     * @return TransformationContext
     */
    public TransformationContext addClassInstruction(final StarClassTransform.ClassInstruction ci) {
        if (starClassImmutable) {
            throw new RuntimeException("Already defined *class as final.");
        }
        if (classesById.containsKey(ci.getId()) || classesByName.containsKey(ci.getId()) || classesById.containsKey(ci.getName()) || classesByName.containsKey(ci.getName())) {
            throw new RuntimeException("Class name or id already defined - cannot redefine: " + ci.getId() + ", " + ci.getName());
        }

        final Set<StarClassTransform.ClassInstruction> updatedClasses = classes.add(ci);
        final Map<String, StarClassTransform.ClassInstruction> updatedClassesById = classesById.put(ci.getId(), ci);

        final Map<String, StarClassTransform.ClassInstruction> updatedClassesByName = (StringUtils.isNotEmpty(ci.getName())) ? classesByName.put(ci.getName(), ci) : classesByName;

        return this.withClasses(updatedClasses)
                .withClassesById(updatedClassesById)
                .withClassesByName(updatedClassesByName);
    }

    /**
     * Get a ClassInstruction by name or id if we have one.
     *
     * @param idOrName the id or name String
     * @return an Option of a StarClassTransform.ClassInstruction
     */
    public Option<StarClassTransform.ClassInstruction> getClassByNameOrId(final String idOrName) {
        return classesById.get(idOrName)
                .orElse(() -> classesByName.get(idOrName));
    }

    /**
     * Get a MethodInstruction by name or id if we have one.
     *
     * @param idOrName the id or name String
     * @return an Option of a StarClassTransform.ClassInstruction
     */
    public Option<StarMethodTransform.MethodInstruction> getMethodByNameOrId(final String idOrName) {
        return methodsById.get(idOrName)
                .orElse(() -> methodsByName.get(idOrName));
    }

}
