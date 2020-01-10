package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@ToString
@EqualsAndHashCode
public class TopLevelConditionalReturn {
    public final List<Structure> structures;

    public TopLevelConditionalReturn(final List<Structure> structures) {
        this.structures = Collections.unmodifiableList(structures);
    }
}
