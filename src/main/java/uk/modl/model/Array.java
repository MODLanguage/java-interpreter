package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import uk.modl.visitor.ModlVisitor;


@ToString
@EqualsAndHashCode
public class Array implements PairValue, Structure, ValueItem, ArrayItem {
    public final Vector<ArrayItem> arrayItems;

    public Array(final Vector<ArrayItem> arrayItems) {
        this.arrayItems = arrayItems;
    }

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
        arrayItems.forEach(s -> s.visit(visitor));
    }
}
