package uk.modl.model;

import java.util.Collections;
import java.util.List;

public class Modl {
    public final List<Structure> structures;

    public Modl(final List<Structure> structures) {
        this.structures = Collections.unmodifiableList(structures);
    }
}
