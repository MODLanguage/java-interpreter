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
public class ValueConditionalReturn implements Parent, Child {

    @ToString.Exclude
    long id;

    @EqualsAndHashCode.Exclude
    @NonNull
    Vector<ValueItem> items;

    public static ValueConditionalReturn of(final Ancestry ancestry, final Parent parent, final Vector<ValueItem> items) {
        final ValueConditionalReturn child = ValueConditionalReturn.of(IDSource.nextId(), items);
        ancestry.add(parent, child);
        return child;
    }

    public ValueConditionalReturn with(final Ancestry ancestry, final Vector<ValueItem> items) {
        final ValueConditionalReturn child = ValueConditionalReturn.of(id, items);
        ancestry.replaceChild(this, child);
        return child;
    }

}
