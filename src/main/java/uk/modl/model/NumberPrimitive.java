package uk.modl.model;

import lombok.NonNull;
import lombok.Value;
import org.apache.commons.lang3.math.NumberUtils;
import uk.modl.visitor.ModlVisitor;

@Value
public class NumberPrimitive implements Primitive {
    @NonNull
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
