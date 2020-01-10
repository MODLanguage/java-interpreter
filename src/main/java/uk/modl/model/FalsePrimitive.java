package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class FalsePrimitive implements Primitive {
    public static final FalsePrimitive instance = new FalsePrimitive();

    private FalsePrimitive() {

    }
}
