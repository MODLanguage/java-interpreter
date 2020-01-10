package uk.modl.model;

import io.vavr.collection.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class ArrayConditional implements ArrayItem {
    public final List<ConditionTest> tests;
    public final List<ArrayConditionalReturn> returns;

    public ArrayConditional(final List<ConditionTest> tests, final List<ArrayConditionalReturn> returns) {
        this.tests = tests;
        this.returns = returns;
    }
}
