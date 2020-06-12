package uk.modl.ancestry;

import io.vavr.control.Option;
import uk.modl.model.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Ancestry {

    private final Map<Child, Parent> ancestors;

    public Ancestry() {
        ancestors = new LinkedHashMap<>();
    }

    public void add(final Parent parent, final Child child) {
        if (child != null) {
            ancestors.put(child, parent);
        }
    }

    /**
     * Dump the tree as a graphviz list of nodes - needs embedding in "digraph g {...}"
     */
    public void dump() {
        ancestors.entrySet()
                .stream()
                .map(e -> {
                    final Child child = e.getKey();
                    final Parent parent = e.getValue();


                    final long childId = child.getId();
                    final String childHash = Integer.toHexString(System.identityHashCode(child));
                    String childString = "" + childId + ":" + child.getClass()
                            .getSimpleName() + "\\n" + childHash;

                    if (child instanceof Pair) {
                        childString += ":\\n" + ((Pair) child).getKey();
                    }

                    if (child instanceof Primitive) {
                        childString += ":\\n" + child.toString();
                    }

                    if (parent != null) {
                        final long parentId = parent.getId();
                        final String parentHash = Integer.toHexString(System.identityHashCode(parent));
                        String parentString = "" + parentId + ":" + parent.getClass()
                                .getSimpleName() + "\\n" + parentHash;

                        if (parent instanceof Pair) {
                            parentString += ":\\n" + ((Pair) parent).getKey();
                        }

                        if (parent instanceof Primitive) {
                            parentString += ":\\n" + parent.toString();
                        }

                        return "\"" + parentHash + "\"[label=\"" + parentString + "\"]\n\"" + childHash + "\"[label=\"" + childString + "\"]\n\"" + childHash + "\" -> \"" + parentHash + "\"";
                    } else {
                        return "missing[label=\"missing\"]\n\"" + childHash + "\"[label=\"" + childString + "\"]\n\"" + childHash + "\" -> missing";
                    }
                })
                .forEach(System.out::println);
    }

    public Option<Pair> findByKey(final Child child, final String key) {
        final Option<Pair> result = findByKey(0, child, key);
        if (result.isDefined() && !result.get()
                .equals(child)) {
            return result;
        }

        // Check the root objects but filter out the current object.
        final List<Child> roots = ancestors.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == null)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        Pair found = null;
        for (final Child root : roots) {
            if (root instanceof Modl) {
                final Modl modl = (Modl) root;
                for (final Structure s : modl.getStructures()) {
                    if (s instanceof Pair && ((Pair) s).getKey()
                            .equals(key) && !s.equals(child)) {
                        found = (Pair) s;
                    }
                }
            }
        }
        if (found != null) {
            return Option.of(found);
        }
        return Option.none();
    }

    private Option<Pair> findByKey(final int depth, final Child child, final String key) {
        if (depth > 0 && child instanceof Pair && ((Pair) child).getKey()
                .equals(key)) {
            return Option.of((Pair) child);
        }

        if (child instanceof Array) {
            Pair found = null;
            final Array array = (Array) child;
            for (final ArrayItem arrayItem : array.getArrayItems()) {
                if (arrayItem instanceof Pair && ((Pair) arrayItem).getKey()
                        .equals(key)) {
                    found = (Pair) arrayItem;
                }
            }
            if (found != null) {
                return Option.of(found);
            }
        }
        if (child instanceof uk.modl.model.Map) {
            Pair found = null;
            final uk.modl.model.Map map = (uk.modl.model.Map) child;
            for (final MapItem mapItem : map.getMapItems()) {
                if (mapItem instanceof Pair && ((Pair) mapItem).getKey()
                        .equals(key)) {
                    found = (Pair) mapItem;
                }
            }
            if (found != null) {
                return Option.of(found);
            }
        }
        if (child instanceof Modl) {
            Pair found = null;
            final Modl modl = (Modl) child;
            for (final Structure s : modl.getStructures()) {
                if (s instanceof Pair && ((Pair) s).getKey()
                        .equals(key)) {
                    found = (Pair) s;
                }
            }
            if (found != null) {
                return Option.of(found);
            }
        }

        final Parent parent = ancestors.get(child);
        if (parent != null) {
            return findByKey(depth + 1, (Child) parent, key);
        } else {
            final List<Child> roots = ancestors.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue() == null)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            Pair found = null;
            for (final Child root : roots) {
                if (root instanceof Modl) {
                    final Modl modl = (Modl) root;
                    for (final Structure s : modl.getStructures()) {
                        if (s instanceof Pair && ((Pair) s).getKey()
                                .equals(key)) {
                            found = (Pair) s;
                        }
                    }
                }
            }
            if (found != null) {
                return Option.of(found);
            }

        }
        return Option.none();
    }

    public void replaceChild(final Child oldChild, final Child newChild) {
        if (oldChild != null && newChild != null) {
            final Parent parent = ancestors.remove(oldChild);

            final List<Child> childrenOfOldChild = ancestors.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue() != null && entry.getValue()
                            .equals(oldChild))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());


            for (final Child childOfOldChild : childrenOfOldChild) {
                ancestors.remove(childOfOldChild);
                ancestors.put(childOfOldChild, (Parent) newChild);
            }

            ancestors.put(newChild, parent);
        }
    }

    /**
     * Remove an entire sub-tree
     *
     * @param parent the first node to be removed, along with its children
     */
    private void prune(final Parent parent) {
        final List<Child> referencesToOldChild = ancestors.entrySet()
                .stream()
                .filter(entry -> entry.getValue() != null && entry.getValue()
                        .equals(parent))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        referencesToOldChild.forEach(ancestors::remove);

        referencesToOldChild.forEach(child -> prune((Parent) child));
    }

    public void replaceSubTree(final Child oldChild, final Child newChild) {
        if (oldChild != null && newChild != null) {
            final Parent parent = ancestors.remove(oldChild);

            prune((Parent) oldChild);

            ancestors.put(newChild, parent);
        }
    }

}
