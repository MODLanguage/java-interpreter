package uk.modl.model;

import io.vavr.collection.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class NegatedCondition implements ConditionOrConditionGroupInterface {
    public final Operator op;
    public final List<ValueItem> values;

    public NegatedCondition(final Operator op, final List<ValueItem> values) {
        this.op = op;
        this.values = values;
    }
}
