package uk.modl.model;

import io.vavr.collection.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Map implements PairValue, Structure, ValueItem, ArrayItem {
    public final List<MapItem> mapItems;

    public Map(final List<MapItem> mapItems) {
        this.mapItems = mapItems;
    }
}
