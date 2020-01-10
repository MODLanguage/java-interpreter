package uk.modl.model;

import io.vavr.collection.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.tuple.ImmutablePair;

@ToString
@EqualsAndHashCode
public class ConditionTest {
    // The String in the immutable pair is an optional operator: & or |
    public final List<ImmutablePair<ConditionOrConditionGroupInterface, String>> conditions;

    public ConditionTest(final List<ImmutablePair<ConditionOrConditionGroupInterface, String>> conditions) {
        this.conditions = conditions;
    }
}
