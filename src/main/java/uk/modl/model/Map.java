package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.Value;
import uk.modl.visitor.ModlVisitor;

@Value
public class Map implements PairValue, Structure, ValueItem, ArrayItem {

    @NonNull
    Vector<MapItem> mapItems;

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
        mapItems.forEach(s -> s.visit(visitor));
    }

    @Override
    public Number numericValue() {
        throw new RuntimeException("Cannot convert a map to a numeric value.");
    }

}
