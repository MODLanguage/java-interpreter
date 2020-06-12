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
public class ConditionTest implements Parent, Child {

    @ToString.Exclude
    long id;

    // The String in the immutable pair is an optional operator: & or |
    @EqualsAndHashCode.Exclude
    @NonNull
    Vector<Tuple2<ConditionOrConditionGroupInterface, String>> conditions;

    public static ConditionTest of(final Ancestry ancestry, final Parent parent, final Vector<Tuple2<ConditionOrConditionGroupInterface, String>> conditions) {
        final ConditionTest child = ConditionTest.of(IDSource.nextId(), conditions);
        ancestry.add(parent, child);
        return child;
    }

    public ConditionTest with(final Ancestry ancestry, final Vector<Tuple2<ConditionOrConditionGroupInterface, String>> conditions) {
        final ConditionTest child = ConditionTest.of(id, conditions);
        ancestry.replaceChild(this, child);
        return child;
    }

}
