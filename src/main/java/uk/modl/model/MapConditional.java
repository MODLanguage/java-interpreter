package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import uk.modl.visitor.ModlVisitor;

@ToString
@EqualsAndHashCode
public class MapConditional implements MapItem {
    public final Vector<ConditionTest> tests;
    public final Vector<MapConditionalReturn> returns;
    public final Vector<MapItem> result;

    public MapConditional(final Vector<ConditionTest> tests, final Vector<MapConditionalReturn> returns) {
        this(tests, returns, Vector.empty());
    }

    public MapConditional(final Vector<ConditionTest> tests, final Vector<MapConditionalReturn> returns, final Vector<MapItem> result) {
        this.tests = tests;
        this.returns = returns;
        this.result = result;
    }

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
        tests.forEach(s -> s.visit(visitor));
        returns.forEach(s -> s.visit(visitor));
    }

    public MapConditional setResult(final Vector<MapItem> result) {
        return new MapConditional(tests, returns, result);
    }
}
