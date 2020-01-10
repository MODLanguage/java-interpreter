package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@ToString
@EqualsAndHashCode
public class ValueConditional implements ValueItem {
    public final List<ConditionTest> tests;
    public final List<ValueConditionalReturn> returns;

    public ValueConditional(final List<ConditionTest> tests, final List<ValueConditionalReturn> returns) {
        this.tests = Collections.unmodifiableList(tests);
        this.returns = Collections.unmodifiableList(returns);
    }
}
