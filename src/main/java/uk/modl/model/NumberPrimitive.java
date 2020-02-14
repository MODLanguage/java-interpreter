package uk.modl.model;

import lombok.EqualsAndHashCode;
import uk.modl.visitor.ModlVisitor;

@EqualsAndHashCode
public class NumberPrimitive implements Primitive {
    public final String value;

    public NumberPrimitive(final String value) {
        this.value = value;
    }

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
    }

    @Override
    public String toString() {
        return value;
    }

    public double numericValue() {
        return Double.parseDouble(value);
    }
}
