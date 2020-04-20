package uk.modl.model;

import lombok.Value;
import org.apache.commons.lang3.math.NumberUtils;
import uk.modl.visitor.ModlVisitor;

@Value
public class StringPrimitive implements Primitive {
    String value;

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
