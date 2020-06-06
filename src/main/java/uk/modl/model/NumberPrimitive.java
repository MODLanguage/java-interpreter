package uk.modl.model;

import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import org.apache.commons.lang3.math.NumberUtils;
import uk.modl.utils.IDSource;

@Value(staticConstructor = "of")
public class NumberPrimitive implements Primitive {

    @ToString.Exclude
    long id;

    @NonNull
    String value;

    public static NumberPrimitive of(final String value) {
        return NumberPrimitive.of(IDSource.nextId(), value);
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
