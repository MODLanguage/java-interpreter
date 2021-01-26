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

package uk.modl.ancestry;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.vavr.collection.Vector;
import io.vavr.control.Option;
import lombok.val;
import lombok.var;
import uk.modl.model.Array;
import uk.modl.model.Pair;
import uk.modl.model.Primitive;
import uk.modl.transforms.TransformationContext;

public class Ancestry {

  private static final String LABEL_PREFIX = "\"[label=\"";
  private static final String LABEL_SUFFIX = "\"]\n\"";

  private final Map<Child, Parent> ancestors;

  private final ScopedAncestrySearch ancestrySearch;

  public Ancestry() {
    ancestors = new LinkedHashMap<>();
    ancestrySearch = new ScopedAncestrySearch(ancestors);
  }

  public void add(final Parent parent, final Child child) {
    if (child != null) {
      ancestors.put(child, parent);
    }
  }

  public Option<Pair> findByKey(final Child child, final String key) {
    return ancestrySearch.findByKey(child, key);
  }

  /**
   * Dump the tree as a graphviz list of nodes - needs embedding in "digraph g
   * {...}"
   */
  public void dump() {
    ancestors.entrySet().stream().map(e -> {
      val child = e.getKey();
      val parent = e.getValue();

      val childId = child.getId();
      val childHash = Integer.toHexString(System.identityHashCode(child));
      var childString = "" + childId + ":" + child.getClass().getSimpleName() + "\\n" + childHash;

      if (child instanceof Pair) {
        childString += ":\\n" + ((Pair) child).getKey();
      }

      if (child instanceof Primitive) {
        childString += ":\\n" + child.toString();
      }

      if (parent != null) {
        val parentId = parent.getId();
        val parentHash = Integer.toHexString(System.identityHashCode(parent));
        var parentString = "" + parentId + ":" + parent.getClass().getSimpleName() + "\\n" + parentHash;

        if (parent instanceof Pair) {
          parentString += ":\\n" + ((Pair) parent).getKey();
        }

        if (parent instanceof Primitive) {
          parentString += ":\\n" + parent.toString();
        }

        return "\"" + parentHash + LABEL_PREFIX + parentString + LABEL_SUFFIX + childHash + LABEL_PREFIX + childString
            + LABEL_SUFFIX + childHash + "\" -> \"" + parentHash + "\"";
      } else {
        return "missing[label=\"missing\"]\n\"" + childHash + LABEL_PREFIX + childString + LABEL_SUFFIX + childHash
            + "\" -> missing";
      }
    }).forEach(System.out::println);
  }

  public Pair findReferencedPair(final TransformationContext ctx, final Child c, final String key) {
    val hiddenKey = (key.startsWith("_")) ? key : "_" + key;
    val unhiddenKey = (key.startsWith("_")) ? key.substring(1) : key;

    return ctx.getAncestry().findByKey(c, hiddenKey).orElse(() -> ctx.getAncestry().findByKey(c, unhiddenKey))
        .getOrElse((Pair) null);
  }

  public void replaceChild(final Child oldChild, final Child newChild) {
    if (oldChild != null && newChild != null) {
      val parent = ancestors.remove(oldChild);

      final List<Child> childrenOfOldChild = ancestors.entrySet().stream()
          .filter(entry -> entryValueMatchesChild(oldChild, entry)).map(Map.Entry::getKey).collect(Collectors.toList());

      for (val childOfOldChild : childrenOfOldChild) {
        ancestors.remove(childOfOldChild);
        ancestors.put(childOfOldChild, (Parent) newChild);
      }

      ancestors.put(newChild, parent);
    }
  }

  private boolean entryValueMatchesChild(final Child oldChild, final Map.Entry<Child, Parent> entry) {
    return entry.getValue() != null && entry.getValue().equals((Parent) oldChild);
  }

  /**
   * Remove an entire sub-tree
   *
   * @param parent the first node to be removed, along with its children
   */
  private void prune(final Parent parent) {
    val referencesToOldChild = ancestors.entrySet().stream()
        .filter(entry -> entry.getValue() != null && entry.getValue().equals(parent)).map(Map.Entry::getKey)
        .collect(Collectors.toList());

    referencesToOldChild.forEach(ancestors::remove);

    referencesToOldChild.forEach(child -> prune((Parent) child));
  }

  public void replaceSubTree(final Child oldChild, final Child newChild) {
    if (oldChild != null && newChild != null) {
      val parent = ancestors.remove(oldChild);

      prune((Parent) oldChild);

      ancestors.put(newChild, parent);
    }
  }

  public String pathTo(final Vector<String> path, final Parent parent) {
    if (parent == null) {
      return path.reverse().mkString("/");
    }

    final String element;
    if (parent instanceof Pair) {
      element = ((Pair) parent).getKey();
    } else if (parent instanceof Array) {
      element = "array";
    } else if (parent instanceof uk.modl.model.Map) {
      element = "map";
    } else {
      element = "object";
    }
    val newPath = path.append(element);
    final Parent grandparent = ancestors.get((Child) parent);
    return pathTo(newPath, grandparent);
  }

}
