package uk.modl.model;

import io.vavr.collection.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class TopLevelConditional implements Structure {
    public final List<ConditionTest> tests;
    public final List<TopLevelConditionalReturn> returns;

    public TopLevelConditional(final List<ConditionTest> tests, final List<TopLevelConditionalReturn> returns) {
        this.tests = tests;
        this.returns = returns;
    }
}
