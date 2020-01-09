package uk.modl.model;

import java.util.Collections;
import java.util.List;

public class Map implements PairValue, Structure, ValueItem, ArrayItem {
    public final List<MapItem> mapItems;

    public Map(final List<MapItem> mapItems) {
        this.mapItems = Collections.unmodifiableList(mapItems);
    }
}
