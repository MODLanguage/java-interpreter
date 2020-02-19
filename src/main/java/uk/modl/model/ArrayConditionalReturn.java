package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import uk.modl.visitor.ModlVisitable;
import uk.modl.visitor.ModlVisitor;

@ToString
@EqualsAndHashCode
public class ArrayConditionalReturn implements ModlVisitable {
    public final Vector<ArrayItem> items;

    public ArrayConditionalReturn(final Vector<ArrayItem> items) {
        this.items = items;
    }

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
        items.forEach(s -> s.visit(visitor));
    }

}
