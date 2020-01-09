package uk.modl.model;

import java.util.Collections;
import java.util.List;

public class ValueConditional implements ValueItem {
    public final List<ConditionTest> tests;
    public final List<ValueConditionalReturn> returns;

    public ValueConditional(final List<ConditionTest> tests, final List<ValueConditionalReturn> returns) {
        this.tests = Collections.unmodifiableList(tests);
        this.returns = Collections.unmodifiableList(returns);
    }
}
