package uk.modl.model;

import io.vavr.collection.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import uk.modl.visitor.ModlVisitor;

@ToString
@EqualsAndHashCode
public class Map implements PairValue, Structure, ValueItem, ArrayItem {
    public final List<MapItem> mapItems;

    public Map(final List<MapItem> mapItems) {
        this.mapItems = mapItems;
    }

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
        mapItems.forEach(s -> s.visit(visitor));
    }

    @Override
    public String text() {
        return toString();//TODO: Not the best idea
    }
}
