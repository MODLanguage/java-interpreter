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
public class Array implements PairValue, Structure, ValueItem, ArrayItem, Parent, Child {

    @ToString.Exclude
    long id;

    @EqualsAndHashCode.Exclude
    @NonNull
    Vector<ArrayItem> arrayItems;

    public static Array of(final Ancestry ancestry, final Parent parent, final Vector<ArrayItem> arrayItems) {
        final Array child = Array.of(IDSource.nextId(), arrayItems);
        ancestry.add(parent, child);
        return child;
    }

    @Override
    public Number numericValue() {
        throw new RuntimeException("Cannot convert an array to a numeric value.");
    }

    public Array with(final Ancestry ancestry, final Vector<ArrayItem> arrayItems) {
        final Array child = Array.of(id, arrayItems);
        ancestry.replaceChild(this, child);
        return child;
    }

}
