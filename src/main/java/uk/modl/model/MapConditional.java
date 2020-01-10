package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@ToString
@EqualsAndHashCode
public class MapConditional implements MapItem {
    public final List<ConditionTest> tests;
    public final List<MapConditionalReturn> returns;

    public MapConditional(final List<ConditionTest> tests, final List<MapConditionalReturn> returns) {
        this.tests = Collections.unmodifiableList(tests);
        this.returns = Collections.unmodifiableList(returns);
    }
}
