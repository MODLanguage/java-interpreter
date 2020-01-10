package uk.modl.model;

import io.vavr.collection.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Condition implements ConditionOrConditionGroupInterface {
    public final String lhs;
    public final Operator op;
    public final List<ValueItem> values;

    public Condition(final String lhs, final Operator op, final List<ValueItem> values) {
        this.lhs = lhs;
        this.op = op;
        this.values = values;
    }
}
