package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.Value;
import uk.modl.utils.IDSource;

@Value(staticConstructor = "of")
public class ArrayConditionalReturn {

    long id;

    @NonNull
    Vector<ArrayItem> items;

    public static ArrayConditionalReturn of(final Vector<ArrayItem> items) {
        return ArrayConditionalReturn.of(IDSource.nextId(), items);
    }

    public ArrayConditionalReturn with(final Vector<ArrayItem> items) {
        return ArrayConditionalReturn.of(id, items);
    }

}
