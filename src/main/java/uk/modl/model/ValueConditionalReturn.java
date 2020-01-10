package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@ToString
@EqualsAndHashCode
public class ValueConditionalReturn {
    public final List<ValueItem> items;

    public ValueConditionalReturn(final List<ValueItem> items) {
        this.items = Collections.unmodifiableList(items);

    }
}
