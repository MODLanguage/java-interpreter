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
public class ValueConditionalReturn implements Parent, Child {

    @ToString.Exclude
    long id;

    @EqualsAndHashCode.Exclude
    @NonNull
    Vector<ValueItem> items;

    public static ValueConditionalReturn of(final Vector<ValueItem> items) {
        return ValueConditionalReturn.of(IDSource.nextId(), items);
    }

    public ValueConditionalReturn with(final Vector<ValueItem> items) {
        return ValueConditionalReturn.of(id, items);
    }

}
