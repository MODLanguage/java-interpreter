package uk.modl.model;

import io.vavr.collection.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import uk.modl.visitor.ModlVisitor;

@ToString
@EqualsAndHashCode
public class ValueConditional implements ValueItem {
    public final List<ConditionTest> tests;
    public final List<ValueConditionalReturn> returns;

    public ValueConditional(final List<ConditionTest> tests, final List<ValueConditionalReturn> returns) {
        this.tests = tests;
        this.returns = returns;
    }

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
        tests.forEach(s -> s.visit(visitor));
        returns.forEach(s -> s.visit(visitor));
    }

    @Override
    public String text() {
        return toString();//TODO: Not the best idea
    }
}
