package uk.modl.model;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class TruePrimitive implements Primitive {

    public static final TruePrimitive instance = new TruePrimitive();

    private TruePrimitive() {

    }

    @Override
    public String toString() {
        return "01";
    }

    @Override
    public Number numericValue() {
        return 1;
    }

}
