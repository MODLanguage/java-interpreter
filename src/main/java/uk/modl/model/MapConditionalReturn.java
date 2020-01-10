package uk.modl.model;

import io.vavr.collection.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class MapConditionalReturn {
    public final List<MapItem> items;

    public MapConditionalReturn(final List<MapItem> items) {
        this.items = items;
    }
}
