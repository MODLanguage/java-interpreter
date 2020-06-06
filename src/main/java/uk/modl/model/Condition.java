package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.Value;
import uk.modl.utils.IDSource;

@Value(staticConstructor = "of")
public class Condition implements ConditionOrConditionGroupInterface {

    long id;

    StringPrimitive lhs;

    Operator op;

    @NonNull
    Vector<ValueItem> values;

    boolean shouldNegate;

    public static Condition of(final StringPrimitive lhs, final Operator op, final Vector<ValueItem> values, final boolean shouldNegate) {
        return Condition.of(IDSource.nextId(), lhs, op, values, shouldNegate);
    }

    public Condition with(final StringPrimitive lhs, final Operator op, final Vector<ValueItem> values, final boolean shouldNegate) {
        return Condition.of(id, lhs, op, values, shouldNegate);
    }

}
