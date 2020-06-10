package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import org.apache.commons.lang3.math.NumberUtils;
import uk.modl.ancestry.Ancestry;
import uk.modl.ancestry.Child;
import uk.modl.ancestry.Parent;
import uk.modl.utils.IDSource;

@Value(staticConstructor = "of")
public class NumberPrimitive implements Primitive, Child {

    @ToString.Exclude
    long id;

    @EqualsAndHashCode.Exclude
    @NonNull
    String value;

    public static NumberPrimitive of(final Ancestry ancestry, final Parent parent, final String value) {
        final NumberPrimitive child = NumberPrimitive.of(IDSource.nextId(), value);
        ancestry.add(parent, child);
        return child;
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
