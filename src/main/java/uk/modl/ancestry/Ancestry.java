package uk.modl.ancestry;

import io.vavr.control.Option;
import uk.modl.model.*;

import java.util.HashMap;
import java.util.Map;

public class Ancestry {

    private final Map<Child, Parent> ancestors = new HashMap<>();

    public void add(final Parent parent, final Child child) {
        ancestors.put(child, parent);
    }

    /**
     * TODO: Delete this
     */
    public void dump() {
        ancestors.entrySet()
                .stream()
                .map(e -> {
                    final Child child = e.getKey();
                    final Parent parent = e.getValue();


                    final long childId = child.getId();
                    String childString = "" + childId + ":" + child.getClass()
                            .getSimpleName();

                    if (child instanceof Pair) {
                        childString += ":\\n" + ((Pair) child).getKey();
                    }

                    if (child instanceof Primitive) {
                        childString += ":\\n" + child.toString();
                    }

                    if (parent != null) {
                        final long parentId = parent.getId();
                        String parentString = "" + parentId + ":" + parent.getClass()
                                .getSimpleName();

                        if (parent instanceof Pair) {
                            parentString += ":\\n" + ((Pair) parent).getKey();
                        }

                        if (parent instanceof Primitive) {
                            parentString += ":\\n" + parent.toString();
                        }

                        return "" + parentId + "[label=\"" + parentString + "\"]\n" + childId + "[label=\"" + childString + "\"]\n" + parentId + " -> " + childId;
                    } else {
                        return "missing[label=\"missing\"]\n" + childId + "[label=\"" + childString + "\"]\nmissing -> " + childId;
                    }
                })
                .forEach(System.out::println);
    }

    public Option<Pair> findByKey(final Child child, final String key) {
        if (child instanceof Pair && ((Pair) child).getKey()
                .equals(key)) {
            return Option.of((Pair) child);
        }

        if (child instanceof Array) {
            final Array array = (Array) child;
            for (final ArrayItem arrayItem : array.getArrayItems()) {
                if (arrayItem instanceof Pair && ((Pair) arrayItem).getKey()
                        .equals(key)) {
                    return Option.of((Pair) arrayItem);
                }
            }
        }
        if (child instanceof uk.modl.model.Map) {
            final uk.modl.model.Map map = (uk.modl.model.Map) child;
            for (final MapItem mapItem : map.getMapItems()) {
                if (mapItem instanceof Pair && ((Pair) mapItem).getKey()
                        .equals(key)) {
                    return Option.of((Pair) mapItem);
                }
            }
        }
        if (child instanceof Modl) {
            final Modl modl = (Modl) child;
            for (final Structure s : modl.getStructures()) {
                if (s instanceof Pair && ((Pair) s).getKey()
                        .equals(key)) {
                    return Option.of((Pair) s);
                }
            }
        }

        final Parent parent = ancestors.get(child);
        if (parent != null) {
            return findByKey((Child) parent, key);
        }
        return Option.none();
    }

    public void replaceChild(final Child oldChild, final Child newChild) {
        final Parent parent = ancestors.remove(oldChild);
        ancestors.put(newChild, parent);
    }

}
