package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class StringPrimitive implements Primitive {
    public final String value;

    public StringPrimitive(final String value) {
        this.value = value;
    }
}
