package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class NumberPrimitive implements Primitive {
    public final String value;

    public NumberPrimitive(final String value) {
        this.value = value;
    }
}
