package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import uk.modl.ancestry.Ancestry;
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

    public static MapConditionalReturn of(final Ancestry ancestry, final Parent parent, final Vector<MapItem> items) {
        final MapConditionalReturn child = MapConditionalReturn.of(IDSource.nextId(), items);
        ancestry.add(parent, child);
        return child;
    }

    public MapConditionalReturn with(final Ancestry ancestry, final Vector<MapItem> items) {
        final MapConditionalReturn child = MapConditionalReturn.of(id, items);
        ancestry.replaceChild(this, child);
        return child;
    }

}
