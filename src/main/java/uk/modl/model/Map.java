package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import uk.modl.utils.IDSource;

@Value(staticConstructor = "of")
public class Map implements PairValue, Structure, ValueItem, ArrayItem {

    @ToString.Exclude
    long id;

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
