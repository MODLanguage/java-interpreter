package uk.modl.model;

import io.vavr.collection.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import uk.modl.visitor.ModlVisitor;

@ToString
@EqualsAndHashCode
public class MapConditional implements MapItem {
    public final List<ConditionTest> tests;
    public final List<MapConditionalReturn> returns;

    public MapConditional(final List<ConditionTest> tests, final List<MapConditionalReturn> returns) {
        this.tests = tests;
        this.returns = returns;
    }

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
        tests.forEach(s -> s.visit(visitor));
        returns.forEach(s -> s.visit(visitor));
    }
}
