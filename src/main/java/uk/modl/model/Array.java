package uk.modl.model;

import io.vavr.collection.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import uk.modl.visitor.ModlVisitor;


@ToString
@EqualsAndHashCode
public class Array implements PairValue, Structure, ValueItem, ArrayItem {
    public final List<ArrayItem> arrayItems;

    public Array(final List<ArrayItem> arrayItems) {
        this.arrayItems = arrayItems;
    }

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
        arrayItems.forEach(s -> s.visit(visitor));
    }

    @Override
    public String text() {
        return toString();//TODO: Not the best idea
    }
}
