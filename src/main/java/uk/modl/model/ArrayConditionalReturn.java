package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.Value;
import uk.modl.visitor.ModlVisitable;
import uk.modl.visitor.ModlVisitor;

@Value
public class ArrayConditionalReturn implements ModlVisitable {
    @NonNull
    Vector<ArrayItem> items;

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
        items.forEach(s -> s.visit(visitor));
    }

}
