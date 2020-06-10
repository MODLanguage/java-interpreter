package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import org.apache.commons.lang3.math.NumberUtils;
import uk.modl.ancestry.Ancestry;
import uk.modl.ancestry.Child;
import uk.modl.ancestry.Parent;
import uk.modl.utils.IDSource;

@Value(staticConstructor = "of")
public class StringPrimitive implements Primitive, Child {

    @ToString.Exclude
    long id;

    @EqualsAndHashCode.Exclude
    String value;

    @Override
    public String toString() {
        return value;
    }

    @Override
    public Number numericValue() {
        return NumberUtils.createNumber(value);
    }

    public static StringPrimitive of(final Ancestry ancestry, final Parent parent, final String value) {
        final StringPrimitive child = StringPrimitive.of(IDSource.nextId(), value);
        ancestry.add(parent, child);
        return child;
    }

    public StringPrimitive with(final Ancestry ancestry, final String value) {
        final StringPrimitive child = StringPrimitive.of(id, value);
        ancestry.replaceChild(this, child);
        return child;
    }

}
