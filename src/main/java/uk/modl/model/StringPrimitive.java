package uk.modl.model;

import lombok.Value;
import org.apache.commons.lang3.math.NumberUtils;
import uk.modl.utils.IDSource;

@Value(staticConstructor = "of")
public class StringPrimitive implements Primitive {

    long id;

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
