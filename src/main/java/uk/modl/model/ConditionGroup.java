package uk.modl.model;

import io.vavr.Tuple2;
import io.vavr.collection.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class ConditionGroup implements ConditionOrConditionGroupInterface {
    public final List<Tuple2<ConditionTest, String>> subConditionList;

    public ConditionGroup(final List<Tuple2<ConditionTest, String>> subConditionList) {
        this.subConditionList = subConditionList;
    }
}
