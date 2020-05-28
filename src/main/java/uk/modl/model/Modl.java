package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.Value;
import uk.modl.visitor.ModlVisitable;
import uk.modl.visitor.ModlVisitor;


@Value
public class Modl implements ModlVisitable {

    @NonNull
    Vector<Structure> structures;

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
        structures.forEach(s -> s.visit(visitor));
    }

}
