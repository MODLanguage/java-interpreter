package uk.modl.model;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class NullPrimitive implements Primitive {

    public static final NullPrimitive instance = new NullPrimitive();

    private NullPrimitive() {

    }

    @Override
    public String toString() {
        return "000";
    }

    @Override
    public Number numericValue() {
        return null;
    }

}
