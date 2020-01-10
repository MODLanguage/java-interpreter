package uk.modl.model;

import io.vavr.collection.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class ArrayConditionalReturn {
    public final List<ArrayItem> items;

    public ArrayConditionalReturn(final List<ArrayItem> items) {
        this.items = items;
    }
}
