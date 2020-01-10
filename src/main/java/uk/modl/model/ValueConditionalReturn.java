package uk.modl.model;

import io.vavr.collection.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class ValueConditionalReturn {
    public final List<ValueItem> items;

    public ValueConditionalReturn(final List<ValueItem> items) {
        this.items = items;

    }
}
