package uk.modl.model;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Collections;
import java.util.List;

public class NegatedConditionGroup implements ConditionOrConditionGroupInterface {
    public final List<ImmutablePair<ConditionTest, String>> subConditionList;

    public NegatedConditionGroup(final List<ImmutablePair<ConditionTest, String>> subConditionList) {
        this.subConditionList = Collections.unmodifiableList(subConditionList);
    }
}
