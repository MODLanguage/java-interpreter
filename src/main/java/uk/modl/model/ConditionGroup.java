package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Collections;
import java.util.List;

@ToString
@EqualsAndHashCode
public class ConditionGroup implements ConditionOrConditionGroupInterface {
    public final List<ImmutablePair<ConditionTest, String>> subConditionList;

    public ConditionGroup(final List<ImmutablePair<ConditionTest, String>> subConditionList) {
        this.subConditionList = Collections.unmodifiableList(subConditionList);
    }
}
