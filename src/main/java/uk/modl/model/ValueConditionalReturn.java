package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import uk.modl.visitor.ModlVisitable;
import uk.modl.visitor.ModlVisitor;

@ToString
@EqualsAndHashCode
public class ValueConditionalReturn implements ModlVisitable {
    public final Vector<ValueItem> items;

    public ValueConditionalReturn(final Vector<ValueItem> items) {
        this.items = items;

    }

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
        items.forEach(s -> s.visit(visitor));
    }
}
