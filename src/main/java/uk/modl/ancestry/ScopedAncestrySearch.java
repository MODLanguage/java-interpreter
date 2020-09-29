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

import io.vavr.control.Option;
import lombok.val;
import uk.modl.model.Array;
import uk.modl.model.Modl;
import uk.modl.model.Pair;
import uk.modl.model.Structure;

import java.util.stream.Collectors;

/**
 * Search the ancestry for objects in scope above a given child, but only those earlier in the tree then the subtree
 * containing the child. This prevents forward references.
 */
public class ScopedAncestrySearch {

    private final java.util.Map<Child, Parent> ancestors;

    public ScopedAncestrySearch(final java.util.Map<Child, Parent> ancestors) {
        this.ancestors = ancestors;
    }

    public Option<Pair> findByKey(final Child child, final String key) {
        val parent = ancestors.get(child);

        if (parent != null) {
            val result = findByKey(0, parent, child, key);
            if (result.isDefined() && !result.get()
                    .equals(child)) {
                return result;
            }
        }

        // Check the root objects but filter out the current object.
        return checkRootModlObjects(child, key, parent);
    }

    private Option<Pair> checkRootModlObjects(final Child child, final String key, final Parent parent) {
        val roots = ancestors.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == null)
                .map(java.util.Map.Entry::getKey)
                .collect(Collectors.toList());

        Pair found = null;
        for (val root : roots) {
            if (root instanceof Modl) {
                val modl = (Modl) root;

                found = findByKeyInModl(child, key, parent, found, modl);
            }
        }
        return Option.of(found);
    }

    private Pair findByKeyInModl(final Child child, final String key, final Parent parent, Pair found, final Modl modl) {
        for (val s : modl.getStructures()) {
            if (sameAsParentOrChild(child, parent, s)) {
                break;
            }
            if (pairWithMatchingKey(key, s)) {
                found = (Pair) s;
            }
        }
        return found;
    }

    private boolean pairWithMatchingKey(final String key, final Object o) {
        return o instanceof Pair && ((Pair) o).getKey()
                .equals(key);
    }

    private boolean sameAsParentOrChild(final Child child, final Parent parent, final Structure s) {
        return s != null && (s.equals(parent) || s.equals(child));
    }

    private Option<Pair> findByKey(final int depth, final Parent parent, final Child child, final String key) {
        if (matchingKeyButNotSelf(depth, child, key)) {
            return Option.of((Pair) child);
        }

        final Option<Pair> found;

        if (child instanceof Array) {
            found = findByKeyInArray(parent, (Array) child, key);
        } else if (child instanceof uk.modl.model.Map) {
            found = findByKeyInMap(parent, (uk.modl.model.Map) child, key);
        } else if (child instanceof Modl) {
            found = findByKeyInModl((Modl) child, key);
        } else {
            found = Option.none();
        }

        if (found.isEmpty()) {
            return lookHigherUpTheTree(depth, parent, child, key);
        }
        return found;
    }

    private Option<Pair> lookHigherUpTheTree(final int depth, final Parent parent, final Child child, final String key) {
        //noinspection SuspiciousMethodCalls
        val grandParent = ancestors.get(parent);

        if (grandParent != null) {
            return findByKey(depth + 1, grandParent, (Child) parent, key);
        } else {
            return checkRootModlObjects(child, key, parent);
        }
    }

    private boolean matchingKeyButNotSelf(final int depth, final Child child, final String key) {
        return depth > 0 && child instanceof Pair && ((Pair) child).getKey()
                .equals(key);
    }

    private Option<Pair> findByKeyInModl(final Modl modl, final String key) {
        Pair found = null;
        for (val s : modl.getStructures()) {
            if (pairWithMatchingKey(key, s)) {
                found = (Pair) s;
            }
        }
        return Option.of(found);
    }

    private Option<Pair> findByKeyInMap(final Parent parent, final uk.modl.model.Map map, final String key) {
        Pair found = null;
        for (val mapItem : map.getMapItems()) {
            if (mapItem.equals(parent)) {
                break;
            }
            if (pairWithMatchingKey(key, mapItem)) {
                found = (Pair) mapItem;
            }
        }
        return Option.of(found);
    }

    private Option<Pair> findByKeyInArray(final Parent parent, final Array array, final String key) {
        Pair found = null;
        for (val arrayItem : array.getArrayItems()) {
            if (arrayItem.equals(parent)) {
                break;
            }
            if (pairWithMatchingKey(key, arrayItem)) {
                found = (Pair) arrayItem;
            }
        }
        return Option.of(found);
    }


}
