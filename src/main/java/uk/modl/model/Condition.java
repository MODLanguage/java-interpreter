package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@ToString
@EqualsAndHashCode
public class Condition implements ConditionOrConditionGroupInterface {
    public final String lhs;
    public final Operator op;
    public final List<ValueItem> values;

    public Condition(final String lhs, final Operator op, final List<ValueItem> values) {
        this.lhs = lhs;
        this.op = op;
        this.values = Collections.unmodifiableList(values);
    }
}
