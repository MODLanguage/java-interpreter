package uk.modl.model;

import lombok.EqualsAndHashCode;
import uk.modl.visitor.ModlVisitor;

@EqualsAndHashCode
public class NullPrimitive implements Primitive {
    public static final NullPrimitive instance = new NullPrimitive();

    private NullPrimitive() {

    }

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
    }

    @Override
    public String toString() {
        return "000";
    }
}
