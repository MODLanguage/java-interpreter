package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@ToString
@EqualsAndHashCode
public class Array implements PairValue, Structure, ValueItem, ArrayItem {
    public final List<ArrayItem> arrayItems;

    public Array(final List<ArrayItem> arrayItems) {
        this.arrayItems = Collections.unmodifiableList(arrayItems);
    }
}
