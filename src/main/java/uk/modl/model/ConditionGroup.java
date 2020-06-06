package uk.modl.model;

import io.vavr.Tuple2;
import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import uk.modl.utils.IDSource;

@Value(staticConstructor = "of")
public class ConditionGroup implements ConditionOrConditionGroupInterface {

    @ToString.Exclude
    long id;

    @NonNull
    Vector<Tuple2<ConditionTest, String>> subConditionList;

    boolean shouldNegate;

    public static ConditionGroup of(final Vector<Tuple2<ConditionTest, String>> subConditionList, final boolean shouldNegate) {
        return ConditionGroup.of(IDSource.nextId(), subConditionList, shouldNegate);
    }

    public ConditionGroup with(final Vector<Tuple2<ConditionTest, String>> subConditionList, final boolean shouldNegate) {
        return ConditionGroup.of(id, subConditionList, shouldNegate);
    }

}
