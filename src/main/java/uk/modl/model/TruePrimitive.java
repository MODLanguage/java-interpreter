package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class TruePrimitive implements Primitive {
    public static final TruePrimitive instance = new TruePrimitive();

    private TruePrimitive() {

    }
}
