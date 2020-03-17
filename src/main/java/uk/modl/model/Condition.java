package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import uk.modl.visitor.ModlVisitor;

@ToString
@EqualsAndHashCode
public class Condition implements ConditionOrConditionGroupInterface {
    public final ValueItem lhs;
    public final Operator op;
    public final Vector<ValueItem> values;
    public final boolean shouldNegate;

    public Condition(final ValueItem lhs, final Operator op, final Vector<ValueItem> values, final boolean shouldNegate) {
        this.lhs = lhs;
        this.op = op;
        this.values = values;
        this.shouldNegate = shouldNegate;
    }

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
        values.forEach(s -> s.visit(visitor));
    }

}
