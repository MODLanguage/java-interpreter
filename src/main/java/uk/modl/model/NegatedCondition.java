package uk.modl.model;

import java.util.Collections;
import java.util.List;

public class NegatedCondition implements ConditionOrConditionGroupInterface {
    public final Operator op;
    public final List<ValueItem> values;

    public NegatedCondition(final Operator op, final List<ValueItem> values) {
        this.op = op;
        this.values = Collections.unmodifiableList(values);
    }
}
