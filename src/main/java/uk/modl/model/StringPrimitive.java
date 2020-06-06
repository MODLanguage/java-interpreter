package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import org.apache.commons.lang3.math.NumberUtils;
import uk.modl.ancestry.Child;
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

    public static StringPrimitive of(final String value) {
        return StringPrimitive.of(IDSource.nextId(), value);
    }

    public StringPrimitive with(final String value) {
        return StringPrimitive.of(id, value);
    }

}
