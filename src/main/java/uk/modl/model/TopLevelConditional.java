package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import uk.modl.visitor.ModlVisitor;

@ToString
@EqualsAndHashCode
public class TopLevelConditional implements Structure {
    public final Vector<ConditionTest> tests;
    public final Vector<TopLevelConditionalReturn> returns;
    public final Vector<Structure> result;

    public TopLevelConditional(final Vector<ConditionTest> tests, final Vector<TopLevelConditionalReturn> returns) {
        this(tests, returns, Vector.empty());
    }

    public TopLevelConditional(final Vector<ConditionTest> tests, final Vector<TopLevelConditionalReturn> returns, final Vector<Structure> structures) {
        this.tests = tests;
        this.returns = returns;
        this.result = structures;
    }

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
        tests.forEach(s -> s.visit(visitor));
        returns.forEach(s -> s.visit(visitor));
    }

    public TopLevelConditional setResult(final Vector<Structure> structures) {
        return new TopLevelConditional(tests, returns, structures);
    }
}
