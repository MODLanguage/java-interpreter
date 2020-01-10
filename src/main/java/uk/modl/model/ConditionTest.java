package uk.modl.model;

import io.vavr.Tuple2;
import io.vavr.collection.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class ConditionTest {
    // The String in the immutable pair is an optional operator: & or |
    public final List<Tuple2<ConditionOrConditionGroupInterface, String>> conditions;

    public ConditionTest(final List<Tuple2<ConditionOrConditionGroupInterface, String>> conditions) {
        this.conditions = conditions;
    }
}
