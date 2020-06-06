package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import uk.modl.utils.IDSource;

@Value(staticConstructor = "of")
public class ValueConditionalReturn {

    @ToString.Exclude
    long id;

    @NonNull
    Vector<ValueItem> items;

    public static ValueConditionalReturn of(final Vector<ValueItem> items) {
        return ValueConditionalReturn.of(IDSource.nextId(), items);
    }

    public ValueConditionalReturn with(final Vector<ValueItem> items) {
        return ValueConditionalReturn.of(id, items);
    }

}
