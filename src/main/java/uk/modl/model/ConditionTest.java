package uk.modl.model;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Collections;
import java.util.List;

public class ConditionTest {
    // The String in the immutable pair is an optional operator: & or |
    public final List<ImmutablePair<ConditionOrConditionGroupInterface, String>> conditions;

    public ConditionTest(final List<ImmutablePair<ConditionOrConditionGroupInterface, String>> conditions) {
        this.conditions = Collections.unmodifiableList(conditions);
    }
}
