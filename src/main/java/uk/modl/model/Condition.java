package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import uk.modl.ancestry.Ancestry;
import uk.modl.ancestry.Child;
import uk.modl.ancestry.Parent;
import uk.modl.utils.IDSource;

@Value(staticConstructor = "of")
public class Condition implements ConditionOrConditionGroupInterface, Parent, Child {

    @ToString.Exclude
    long id;

    @EqualsAndHashCode.Exclude
    Primitive lhs;

    @EqualsAndHashCode.Exclude
    Operator op;

    @EqualsAndHashCode.Exclude
    @NonNull
    Vector<ValueItem> values;

    @EqualsAndHashCode.Exclude
    boolean shouldNegate;

    public static Condition of(final Ancestry ancestry, final Parent parent, final Primitive lhs, final Operator op, final Vector<ValueItem> values, final boolean shouldNegate) {
        final Condition child = Condition.of(IDSource.nextId(), lhs, op, values, shouldNegate);
        ancestry.add(parent, child);
        return child;
    }

    public Condition with(final Ancestry ancestry, final Primitive lhs, final Operator op, final Vector<ValueItem> values, final boolean shouldNegate) {
        final Condition child = Condition.of(id, lhs, op, values, shouldNegate);
        ancestry.replaceChild(this, child);
        return child;
    }

}
