package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import uk.modl.visitor.ModlVisitor;

@ToString
@EqualsAndHashCode
public class Map implements PairValue, Structure, ValueItem, ArrayItem {
    public final Vector<MapItem> mapItems;

    public Map(final Vector<MapItem> mapItems) {
        this.mapItems = mapItems;
    }

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
        mapItems.forEach(s -> s.visit(visitor));
    }
}
