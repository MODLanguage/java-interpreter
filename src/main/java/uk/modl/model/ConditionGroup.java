package uk.modl.model;

import io.vavr.Tuple2;
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
public class ConditionGroup implements ConditionOrConditionGroupInterface, Parent, Child {

    @ToString.Exclude
    long id;

    @EqualsAndHashCode.Exclude
    @NonNull
    Vector<Tuple2<ConditionTest, String>> subConditionList;

    @EqualsAndHashCode.Exclude
    boolean shouldNegate;

    public static ConditionGroup of(final Ancestry ancestry, final Parent parent, final Vector<Tuple2<ConditionTest, String>> subConditionList, final boolean shouldNegate) {
        final ConditionGroup child = ConditionGroup.of(IDSource.nextId(), subConditionList, shouldNegate);
        ancestry.add(parent, child);
        return child;
    }

    public ConditionGroup with(final Ancestry ancestry, final Vector<Tuple2<ConditionTest, String>> subConditionList, final boolean shouldNegate) {
        final ConditionGroup child = ConditionGroup.of(id, subConditionList, shouldNegate);
        ancestry.replaceChild(this, child);
        return child;
    }

}
