package uk.modl.model;

import io.vavr.collection.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class MapConditional implements MapItem {
    public final List<ConditionTest> tests;
    public final List<MapConditionalReturn> returns;

    public MapConditional(final List<ConditionTest> tests, final List<MapConditionalReturn> returns) {
        this.tests = tests;
        this.returns = returns;
    }
}
