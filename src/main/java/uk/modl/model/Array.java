package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.Value;
import uk.modl.visitor.ModlVisitor;


@Value
public class Array implements PairValue, Structure, ValueItem, ArrayItem {

    @NonNull
    Vector<ArrayItem> arrayItems;

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
        arrayItems.forEach(s -> s.visit(visitor));
    }

    @Override
    public Number numericValue() {
        throw new RuntimeException("Cannot convert an array to a numeric value.");
    }

}
