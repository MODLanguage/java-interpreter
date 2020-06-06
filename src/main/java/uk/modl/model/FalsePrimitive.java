package uk.modl.model;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class FalsePrimitive implements Primitive {

    public static final FalsePrimitive instance = new FalsePrimitive();

    private FalsePrimitive() {

    }

    @Override
    public String toString() {
        return "00";
    }

    @Override
    public Number numericValue() {
        return 0;
    }

}
