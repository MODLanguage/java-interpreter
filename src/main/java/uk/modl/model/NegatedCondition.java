package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import uk.modl.visitor.ModlVisitor;

@ToString
@EqualsAndHashCode
public class NegatedCondition implements ConditionOrConditionGroupInterface {
    public final Operator op;
    public final Vector<ValueItem> values;

    public NegatedCondition(final Operator op, final Vector<ValueItem> values) {
        this.op = op;
        this.values = values;
    }

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
        values.forEach(s -> s.visit(visitor));
    }
}
