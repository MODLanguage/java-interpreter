package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import uk.modl.ancestry.Child;
import uk.modl.ancestry.Parent;
import uk.modl.utils.IDSource;

@Value(staticConstructor = "of")
public class Condition implements ConditionOrConditionGroupInterface, Parent, Child {

    @ToString.Exclude
    long id;

    @EqualsAndHashCode.Exclude
    StringPrimitive lhs;

    @EqualsAndHashCode.Exclude
    Operator op;

    @EqualsAndHashCode.Exclude
    @NonNull
    Vector<ValueItem> values;

    @EqualsAndHashCode.Exclude
    boolean shouldNegate;

    public static Condition of(final StringPrimitive lhs, final Operator op, final Vector<ValueItem> values, final boolean shouldNegate) {
        return Condition.of(IDSource.nextId(), lhs, op, values, shouldNegate);
    }

    public Condition with(final StringPrimitive lhs, final Operator op, final Vector<ValueItem> values, final boolean shouldNegate) {
        return Condition.of(id, lhs, op, values, shouldNegate);
    }

}
