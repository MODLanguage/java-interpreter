package uk.modl.model;

import lombok.EqualsAndHashCode;
import uk.modl.visitor.ModlVisitor;

@EqualsAndHashCode
public class TruePrimitive implements Primitive {

    public static final TruePrimitive instance = new TruePrimitive();

    private TruePrimitive() {

    }

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
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
