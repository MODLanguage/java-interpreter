package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@ToString
@EqualsAndHashCode
public class ArrayConditional implements ArrayItem {
    public final List<ConditionTest> tests;
    public final List<ArrayConditionalReturn> returns;

    public ArrayConditional(final List<ConditionTest> tests, final List<ArrayConditionalReturn> returns) {
        this.tests = Collections.unmodifiableList(tests);
        this.returns = Collections.unmodifiableList(returns);
    }
}
