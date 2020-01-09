package uk.modl.model;

import java.util.Collections;
import java.util.List;

public class TopLevelConditional implements Structure {
    public final List<ConditionTest> tests;
    public final List<TopLevelConditionalReturn> returns;

    public TopLevelConditional(final List<ConditionTest> tests, final List<TopLevelConditionalReturn> returns) {
        this.tests = Collections.unmodifiableList(tests);
        this.returns = Collections.unmodifiableList(returns);
    }
}
