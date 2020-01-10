package uk.modl.model;

import io.vavr.collection.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@ToString
@EqualsAndHashCode
public class Array implements PairValue, Structure, ValueItem, ArrayItem {
    public final List<ArrayItem> arrayItems;

    public Array(final List<ArrayItem> arrayItems) {
        this.arrayItems = arrayItems;
    }
}
