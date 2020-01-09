package uk.modl.model;

import java.util.Collections;
import java.util.List;

public class MapConditionalReturn {
    public final List<MapItem> items;

    public MapConditionalReturn(final List<MapItem> items) {
        this.items = Collections.unmodifiableList(items);
    }
}
