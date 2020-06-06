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
public class Array implements PairValue, Structure, ValueItem, ArrayItem, Parent, Child {

    @ToString.Exclude
    long id;

    @EqualsAndHashCode.Exclude
    @NonNull
    Vector<ArrayItem> arrayItems;

    @Override
    public Number numericValue() {
        throw new RuntimeException("Cannot convert an array to a numeric value.");
    }

    public static Array of(final Vector<ArrayItem> arrayItems) {
        return Array.of(IDSource.nextId(), arrayItems);
    }

    public Array with(final Vector<ArrayItem> arrayItems) {
        return Array.of(id, arrayItems);
    }

}
