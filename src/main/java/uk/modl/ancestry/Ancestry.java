package uk.modl.ancestry;

import java.util.HashMap;
import java.util.Map;

public class Ancestry {

    private final Map<Child, Parent> ancestors = new HashMap<>();

    public void add(final Parent parent, final Child child) {
        ancestors.put(child, parent);
    }

}
