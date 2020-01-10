package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@ToString
@EqualsAndHashCode
public class TopLevelConditional implements Structure {
    public final List<ConditionTest> tests;
    public final List<TopLevelConditionalReturn> returns;

    public TopLevelConditional(final List<ConditionTest> tests, final List<TopLevelConditionalReturn> returns) {
        this.tests = Collections.unmodifiableList(tests);
        this.returns = Collections.unmodifiableList(returns);
    }
}
