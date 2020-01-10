package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class NullPrimitive implements Primitive {
    public static final NullPrimitive instance = new NullPrimitive();

    private NullPrimitive() {

    }
}
