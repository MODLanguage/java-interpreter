package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import uk.modl.visitor.ModlVisitor;

@ToString
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
    public String text() {
        return "01";
    }
}
