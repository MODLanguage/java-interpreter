package uk.modl.model;

import io.vavr.collection.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import uk.modl.visitor.ModlVisitable;
import uk.modl.visitor.ModlVisitor;


@ToString
@EqualsAndHashCode
public class TopLevelConditionalReturn implements ModlVisitable {
    public final List<Structure> structures;

    public TopLevelConditionalReturn(final List<Structure> structures) {
        this.structures = structures;
    }

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
        structures.forEach(s -> s.visit(visitor));
    }
}
