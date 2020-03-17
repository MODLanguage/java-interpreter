package uk.modl.model;

import lombok.EqualsAndHashCode;
import uk.modl.visitor.ModlVisitor;

@EqualsAndHashCode
public class FalsePrimitive implements Primitive {
    public static final FalsePrimitive instance = new FalsePrimitive();

    private FalsePrimitive() {

    }

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
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
