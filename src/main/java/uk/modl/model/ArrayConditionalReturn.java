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
public class ArrayConditionalReturn implements Parent, Child {

    @ToString.Exclude
    long id;

    @EqualsAndHashCode.Exclude
    @NonNull
    Vector<ArrayItem> items;

    public static ArrayConditionalReturn of(final Ancestry ancestry, final Parent parent, final Vector<ArrayItem> items) {
        final ArrayConditionalReturn child = ArrayConditionalReturn.of(IDSource.nextId(), items);
        ancestry.add(parent, child);
        return child;
    }

    public ArrayConditionalReturn with(final Ancestry ancestry, final Vector<ArrayItem> items) {
        final ArrayConditionalReturn child = ArrayConditionalReturn.of(id, items);
        ancestry.replaceChild(this, child);
        return child;
    }

}
