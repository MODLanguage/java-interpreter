package uk.modl.model;

import java.util.Collections;
import java.util.List;

public class Array implements PairValue, Structure, ValueItem, ArrayItem {
    public final List<ArrayItem> arrayItems;

    public Array(final List<ArrayItem> arrayItems) {
        this.arrayItems = Collections.unmodifiableList(arrayItems);
    }
}
