package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.Value;
import uk.modl.visitor.ModlVisitable;
import uk.modl.visitor.ModlVisitor;

@Value
public class ValueConditionalReturn implements ModlVisitable {
    @NonNull
    Vector<ValueItem> items;

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
        items.forEach(s -> s.visit(visitor));
    }
}
