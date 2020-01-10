package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import uk.modl.visitor.ModlVisitor;

@ToString
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
    public String text() {
        return value;
    }
}
