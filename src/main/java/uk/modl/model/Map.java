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
public class Map implements PairValue, Structure, ValueItem, ArrayItem, Parent, Child {

    @ToString.Exclude
    long id;

    @EqualsAndHashCode.Exclude
    @NonNull
    Vector<MapItem> mapItems;

    @Override
    public Number numericValue() {
        throw new RuntimeException("Cannot convert a map to a numeric value.");
    }

    public static Map of(final Ancestry ancestry, final Parent parent, final Vector<MapItem> mapItems) {
        final Map child = Map.of(IDSource.nextId(), mapItems);
        ancestry.add(parent, child);
        return child;
    }

    public Map with(final Ancestry ancestry, final Vector<MapItem> mapItems) {
        final Map child = Map.of(id, mapItems);
        ancestry.replaceChild(this, child);
        return child;
    }

}
