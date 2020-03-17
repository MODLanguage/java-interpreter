package uk.modl.model;

import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.math.NumberUtils;
import uk.modl.visitor.ModlVisitor;

@EqualsAndHashCode
public class StringPrimitive implements Primitive {
    public final String value;

    public StringPrimitive(final String value) {
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

    @Override
    public Number numericValue() {
        return NumberUtils.createNumber(value);
    }
}
