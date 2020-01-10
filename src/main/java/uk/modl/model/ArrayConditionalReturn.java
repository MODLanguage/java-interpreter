package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@ToString
@EqualsAndHashCode
public class ArrayConditionalReturn {
    public final List<ArrayItem> items;

    public ArrayConditionalReturn(final List<ArrayItem> items) {
        this.items = Collections.unmodifiableList(items);
    }
}
