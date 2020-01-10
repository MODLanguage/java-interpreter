package uk.modl.model;

import io.vavr.collection.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@ToString
@EqualsAndHashCode
public class Modl {
    public final List<Structure> structures;

    public Modl(final List<Structure> structures) {
        this.structures = structures;
    }
}
