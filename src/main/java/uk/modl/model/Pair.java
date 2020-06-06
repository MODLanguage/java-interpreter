package uk.modl.model;

import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import uk.modl.utils.IDSource;

@Value(staticConstructor = "of")
public class Pair implements Structure, MapItem, ValueItem, ArrayItem {

    @ToString.Exclude
    long id;

    @NonNull
    String key;

    @NonNull
    PairValue value;

    public static Pair of(final String key, final PairValue value) {
        return Pair.of(IDSource.nextId(), key, value);
    }

    @Override
    public Number numericValue() {
        return value.numericValue();
    }

    public Pair with(final PairValue v) {
        return Pair.of(id, key, v);
    }

    public Pair with(final String key, final PairValue value) {
        return Pair.of(id, key, value);
    }

}
