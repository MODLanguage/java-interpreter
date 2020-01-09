package uk.modl.model;

import java.util.Collections;
import java.util.List;

public class ValueConditionalReturn {
    public final List<ValueItem> items;

    public ValueConditionalReturn(final List<ValueItem> items) {
        this.items = Collections.unmodifiableList(items);

    }
}
