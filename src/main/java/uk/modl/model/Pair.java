package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import uk.modl.ancestry.Child;
import uk.modl.ancestry.Parent;
import uk.modl.utils.IDSource;

@Value(staticConstructor = "of")
public class Pair implements Structure, MapItem, ValueItem, ArrayItem, Parent, Child {

    @ToString.Exclude
    long id;

    @EqualsAndHashCode.Exclude
    @NonNull
    String key;

    @EqualsAndHashCode.Exclude
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
