package uk.modl.model;

import io.vavr.collection.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@ToString
@EqualsAndHashCode
public class TopLevelConditionalReturn {
    public final List<Structure> structures;

    public TopLevelConditionalReturn(final List<Structure> structures) {
        this.structures = structures;
    }
}
