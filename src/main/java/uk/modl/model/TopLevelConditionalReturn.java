package uk.modl.model;

import java.util.Collections;
import java.util.List;

public class TopLevelConditionalReturn {
    public final List<Structure> structures;

    public TopLevelConditionalReturn(final List<Structure> structures) {
        this.structures = Collections.unmodifiableList(structures);
    }
}
