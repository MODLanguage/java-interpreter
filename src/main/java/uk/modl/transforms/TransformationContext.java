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

import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.LinkedHashSet;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import lombok.Value;
import lombok.With;
import lombok.val;
import uk.modl.ancestry.Ancestry;
import uk.modl.model.ArrayItem;
import uk.modl.parser.errors.RecursiveFileLoadException;
import uk.modl.utils.Tree;

/**
 * Stores context needed by other parts of the interpreter
 */
@Value(staticConstructor = "of")
@With
public class TransformationContext {

  public static final int INTERPRETER_VERSION = 1;

  public static final boolean STAR_LOAD_IMMUTABLE = false;

  public static final boolean STAR_CLASS_IMMUTABLE = false;

  /**
   * The supplied timeout.
   */
  long timeout;

  /**
   * The URL of the MODL that is being processed.
   */
  Option<URL> url;

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
   * Files loaded by a *load instruction stored by nesting level.
   */
  Tree<String> filesLoaded;

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

  /**
   * The positiokn in the *load tree.
   */
  Tree.Node<String> starLoadNestingNode;

  /**
   * @param uri                 the URI of the MODL
   * @param timeoutMilliseconds the number of seconds the caller is prepared to
   *                            wait for a result.
   * @return TransformationContext
   */
  public static TransformationContext baseCtx(final URL uri, final long timeoutMilliseconds) {
    val maybeUri = Option.of(uri);
    final Tree<String> filesLoadedTree = new Tree<>("");
    return TransformationContext.of(timeoutMilliseconds, maybeUri, new Ancestry(), INTERPRETER_VERSION,
        STAR_LOAD_IMMUTABLE, STAR_CLASS_IMMUTABLE, Vector.empty(), filesLoadedTree, LinkedHashSet.empty(),
        LinkedHashMap.empty(), LinkedHashMap.empty(), LinkedHashSet.empty(), LinkedHashMap.empty(),
        LinkedHashMap.empty(), filesLoadedTree.getRoot());
  }

  /**
   * Add files loaded by a *load instruction
   *
   * @param filenames a List of String filenames
   * @return TransformationContext
   * @throws RecursiveFileLoadException if a file has already been loaded at a
   *                                    higher nesting level.
   */
  public TransformationContext addFilesLoaded(final Vector<String> filenames) {

    checkRecursiveLoad(starLoadNestingNode, filenames);
    return this;
  }

  private void checkRecursiveLoad(final Tree.Node<String> parentNode, final Vector<String> filenames) {
    if (parentNode != null) {

      final String nodeData = parentNode.getNodeData();
      filenames.forEach(file -> {
        if (nodeData.equals(file)) {
          throw new RecursiveFileLoadException("Recursive file load detected for: " + file);
        }
      });

      checkRecursiveLoad(parentNode.getParent(), filenames);
    }
  }

  /**
   * 
   * @return Vector of String filenames for all the files loaded up to now.
   */
  public Vector<String> getAllFilesLoaded() {
    return Vector.ofAll(filesLoaded.getAllNodes());
  }

  public TransformationContext addNestingLevel(final String filename) {
    final Tree.Node<String> node = new Tree.Node<>(filename, starLoadNestingNode, new ArrayList<>());
    starLoadNestingNode.addChild(node);
    return this.withStarLoadNestingNode(node);
  }

  /**
   * Add a Method defined by a *method instruction
   *
   * @param mi a StarMethodTransform.MethodInstruction
   * @return TransformationContext
   */
  public TransformationContext addMethodInstruction(final StarMethodTransform.MethodInstruction mi) {
    if (isDuplicateId(mi)) {
      throw new RuntimeException("Duplicate method name or id: " + mi.getId());
    }

    if (isDuplicateName(mi)) {
      throw new RuntimeException("Duplicate method name or id: " + mi.getName());
    }

    val updatedMethods = methods.add(mi);
    val updatedMethodsById = methodsById.put(mi.getId(), mi);

    val updatedMethodsByName = (StringUtils.isNotEmpty(mi.getName())) ? methodsByName.put(mi.getName(), mi)
        : methodsByName;

    return this.withMethods(updatedMethods).withMethodsById(updatedMethodsById).withMethodsByName(updatedMethodsByName);
  }

  private boolean isDuplicateName(final StarMethodTransform.MethodInstruction mi) {
    return methodsById.containsKey(mi.getName()) || methodsByName.containsKey(mi.getName());
  }

  private boolean isDuplicateId(final StarMethodTransform.MethodInstruction mi) {
    return methodsById.containsKey(mi.getId()) || methodsByName.containsKey(mi.getId());
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
    if (isAlreadyDefined(ci)) {
      throw new RuntimeException(
          "Class name or id already defined - cannot redefine: " + ci.getId() + ", " + ci.getName());
    }

    val updatedClasses = classes.add(ci);
    val updatedClassesById = classesById.put(ci.getId(), ci);

    val updatedClassesByName = (StringUtils.isNotEmpty(ci.getName())) ? classesByName.put(ci.getName(), ci)
        : classesByName;

    return this.withClasses(updatedClasses).withClassesById(updatedClassesById).withClassesByName(updatedClassesByName);
  }

  private boolean isAlreadyDefined(final StarClassTransform.ClassInstruction ci) {
    return classesById.containsKey(ci.getId()) || classesByName.containsKey(ci.getId())
        || classesById.containsKey(ci.getName()) || classesByName.containsKey(ci.getName());
  }

  /**
   * Get a ClassInstruction by name or id if we have one.
   *
   * @param idOrName the id or name String
   * @return an Option of a StarClassTransform.ClassInstruction
   */
  public Option<StarClassTransform.ClassInstruction> getClassByNameOrId(final String idOrName) {
    return classesById.get(idOrName).orElse(() -> classesByName.get(idOrName));
  }

  /**
   * Get a MethodInstruction by name or id if we have one.
   *
   * @param idOrName the id or name String
   * @return an Option of a StarClassTransform.ClassInstruction
   */
  public Option<StarMethodTransform.MethodInstruction> getMethodByNameOrId(final String idOrName) {
    return methodsById.get(idOrName).orElse(() -> methodsByName.get(idOrName));
  }

}
