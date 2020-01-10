package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import uk.modl.visitor.ModlVisitor;

@ToString
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
    public String text() {
        return "000";
    }
}
