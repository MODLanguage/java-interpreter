package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.Value;
import uk.modl.visitor.ModlVisitor;

@Value
public class Condition implements ConditionOrConditionGroupInterface {
    ValueItem lhs;
    Operator op;
    @NonNull
    Vector<ValueItem> values;
    boolean shouldNegate;

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
        values.forEach(s -> s.visit(visitor));
    }

}
