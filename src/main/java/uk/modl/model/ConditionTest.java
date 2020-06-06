package uk.modl.model;

import io.vavr.Tuple2;
import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.Value;
import uk.modl.utils.IDSource;

@Value(staticConstructor = "of")
public class ConditionTest {

    long id;

    // The String in the immutable pair is an optional operator: & or |
    @NonNull
    Vector<Tuple2<ConditionOrConditionGroupInterface, String>> conditions;

    public static ConditionTest of(final Vector<Tuple2<ConditionOrConditionGroupInterface, String>> conditions) {
        return ConditionTest.of(IDSource.nextId(), conditions);
    }

    public ConditionTest with(final Vector<Tuple2<ConditionOrConditionGroupInterface, String>> conditions) {
        return ConditionTest.of(id, conditions);
    }

}
