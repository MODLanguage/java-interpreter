package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import uk.modl.visitor.ModlVisitor;

@ToString
@EqualsAndHashCode
public class ArrayConditional implements ArrayItem {
    public final Vector<ConditionTest> tests;
    public final Vector<ArrayConditionalReturn> returns;
    public final Vector<ArrayItem> result;

    public ArrayConditional(final Vector<ConditionTest> tests, final Vector<ArrayConditionalReturn> returns) {
        this(tests, returns, Vector.empty());
    }

    public ArrayConditional(final Vector<ConditionTest> tests, final Vector<ArrayConditionalReturn> returns, final Vector<ArrayItem> result) {
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

    public ArrayConditional setResult(final Vector<ArrayItem> result) {
        return new ArrayConditional(tests, returns, result);
    }
}
