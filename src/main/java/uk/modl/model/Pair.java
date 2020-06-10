package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import uk.modl.ancestry.Ancestry;
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

    public static Pair of(final Ancestry ancestry, final Parent parent, final String key, final PairValue value) {
        final Pair child = Pair.of(IDSource.nextId(), key, value);
        ancestry.add(parent, child);
        return child;
    }

    @Override
    public Number numericValue() {
        return value.numericValue();
    }

    public Pair with(final Ancestry ancestry, final PairValue v) {
        final Pair child = Pair.of(id, key, v);
        ancestry.replaceChild(this, child);
        return child;
    }

    public Pair with(final Ancestry ancestry, final String key, final PairValue value) {
        final Pair child = Pair.of(id, key, value);
        ancestry.replaceChild(this, child);
        return child;
    }

}
