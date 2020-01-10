package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@ToString
@EqualsAndHashCode
public class NegatedCondition implements ConditionOrConditionGroupInterface {
    public final Operator op;
    public final List<ValueItem> values;

    public NegatedCondition(final Operator op, final List<ValueItem> values) {
        this.op = op;
        this.values = Collections.unmodifiableList(values);
    }
}
