package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import uk.modl.visitor.ModlVisitor;

@ToString
@EqualsAndHashCode
public class ValueConditional implements ValueItem {
    public final Vector<ConditionTest> tests;
    public final Vector<ValueConditionalReturn> returns;

    public ValueConditional(final Vector<ConditionTest> tests, final Vector<ValueConditionalReturn> returns) {
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
