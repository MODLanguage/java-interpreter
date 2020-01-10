package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@ToString
@EqualsAndHashCode
public class MapConditionalReturn {
    public final List<MapItem> items;

    public MapConditionalReturn(final List<MapItem> items) {
        this.items = Collections.unmodifiableList(items);
    }
}
