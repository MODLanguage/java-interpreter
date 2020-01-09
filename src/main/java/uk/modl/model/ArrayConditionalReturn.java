package uk.modl.model;

import java.util.Collections;
import java.util.List;

public class ArrayConditionalReturn {
    public final List<ArrayItem> items;

    public ArrayConditionalReturn(final List<ArrayItem> items) {
        this.items = Collections.unmodifiableList(items);
    }
}
