package uk.modl.model;

import io.vavr.collection.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class ValueConditional implements ValueItem {
    public final List<ConditionTest> tests;
    public final List<ValueConditionalReturn> returns;

    public ValueConditional(final List<ConditionTest> tests, final List<ValueConditionalReturn> returns) {
        this.tests = tests;
        this.returns = returns;
    }
}
