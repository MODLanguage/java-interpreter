package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import uk.modl.utils.IDSource;

@Value(staticConstructor = "of")
public class MapConditionalReturn {

    @ToString.Exclude
    long id;

    @NonNull
    Vector<MapItem> items;

    public static MapConditionalReturn of(final Vector<MapItem> items) {
        return MapConditionalReturn.of(IDSource.nextId(), items);
    }

    public MapConditionalReturn with(final Vector<MapItem> items) {
        return MapConditionalReturn.of(id, items);
    }

}
