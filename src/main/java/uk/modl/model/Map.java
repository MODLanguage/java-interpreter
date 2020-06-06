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

    public static Map of(final Vector<MapItem> mapItems) {
        return Map.of(IDSource.nextId(), mapItems);
    }

    public Map with(final Vector<MapItem> mapItems) {
        return Map.of(id, mapItems);
    }

}
