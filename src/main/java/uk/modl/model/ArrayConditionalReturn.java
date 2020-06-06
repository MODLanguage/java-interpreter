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
public class ArrayConditionalReturn implements Parent, Child {

    @ToString.Exclude
    long id;

    @EqualsAndHashCode.Exclude
    @NonNull
    Vector<ArrayItem> items;

    public static ArrayConditionalReturn of(final Vector<ArrayItem> items) {
        return ArrayConditionalReturn.of(IDSource.nextId(), items);
    }

    public ArrayConditionalReturn with(final Vector<ArrayItem> items) {
        return ArrayConditionalReturn.of(id, items);
    }

}
