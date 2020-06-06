package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import uk.modl.ancestry.Child;
import uk.modl.ancestry.Parent;
import uk.modl.utils.IDSource;

@Value(staticConstructor = "of")
public class MapConditionalReturn implements Parent, Child {

    @ToString.Exclude
    long id;

    @EqualsAndHashCode.Exclude
    @NonNull
    Vector<MapItem> items;

    public static MapConditionalReturn of(final Vector<MapItem> items) {
        return MapConditionalReturn.of(IDSource.nextId(), items);
    }

    public MapConditionalReturn with(final Vector<MapItem> items) {
        return MapConditionalReturn.of(id, items);
    }

}
