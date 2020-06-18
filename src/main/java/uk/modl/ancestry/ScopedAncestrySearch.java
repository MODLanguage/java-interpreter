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
import uk.modl.model.*;

import java.util.List;
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
        final Parent parent = ancestors.get(child);

        if (parent != null) {
            final Option<Pair> result = findByKey(0, parent, child, key);
            if (result.isDefined() && !result.get()
                    .equals(child)) {
                return result;
            }
        }

        // Check the root objects but filter out the current object.
        return checkRootModlObjects(child, key, parent);
    }

    private Option<Pair> checkRootModlObjects(final Child child, final String key, final Parent parent) {
        final List<Child> roots = ancestors.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == null)
                .map(java.util.Map.Entry::getKey)
                .collect(Collectors.toList());

        Pair found = null;
        for (final Child root : roots) {
            if (root instanceof Modl) {
                final Modl modl = (Modl) root;

                for (final Structure s : modl.getStructures()) {
                    if (s != null && (s.equals(parent) || s.equals(child))) {
                        break;
                    }
                    if (s instanceof Pair && ((Pair) s).getKey()
                            .equals(key)) {
                        found = (Pair) s;
                    }
                }
            }
        }
        return Option.of(found);
    }

    private Option<Pair> findByKey(final int depth, final Parent parent, final Child child, final String key) {
        if (depth > 0 && child instanceof Pair && ((Pair) child).getKey()
                .equals(key)) {
            return Option.of((Pair) child);
        }

        if (child instanceof Array) {
            final Option<Pair> found = findByKeyInArray(parent, (Array) child, key);
            if (found.isDefined()) {
                return found;
            }
        }
        if (child instanceof uk.modl.model.Map) {
            final Option<Pair> found = findByKeyInMap(parent, (uk.modl.model.Map) child, key);
            if (found.isDefined()) return found;
        }
        if (child instanceof Modl) {
            Option<Pair> found = findByKeyInModl((Modl) child, key);
            if (found.isDefined()) return found;
        }

        //noinspection SuspiciousMethodCalls
        final Parent grandParent = ancestors.get(parent);

        if (grandParent != null) {
            return findByKey(depth + 1, grandParent, (Child) parent, key);
        } else {
            return checkRootModlObjects(child, key, parent);
        }
    }

    private Option<Pair> findByKeyInModl(final Modl modl, final String key) {
        Pair found = null;
        for (final Structure s : modl.getStructures()) {
            if (s instanceof Pair && ((Pair) s).getKey()
                    .equals(key)) {
                found = (Pair) s;
            }
        }
        return Option.of(found);
    }

    private Option<Pair> findByKeyInMap(final Parent parent, final uk.modl.model.Map map, final String key) {
        Pair found = null;
        for (final MapItem mapItem : map.getMapItems()) {
            if (mapItem.equals(parent)) {
                break;
            }
            if (mapItem instanceof Pair && ((Pair) mapItem).getKey()
                    .equals(key)) {
                found = (Pair) mapItem;
            }
        }
        return Option.of(found);
    }

    private Option<Pair> findByKeyInArray(final Parent parent, final Array array, final String key) {
        Pair found = null;
        for (final ArrayItem arrayItem : array.getArrayItems()) {
            if (arrayItem.equals(parent)) {
                break;
            }
            if (arrayItem instanceof Pair && ((Pair) arrayItem).getKey()
                    .equals(key)) {
                found = (Pair) arrayItem;
            }
        }
        return Option.of(found);
    }


}
