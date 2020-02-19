package uk.modl.model;

import io.vavr.Tuple2;
import io.vavr.collection.Vector;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import uk.modl.visitor.ModlVisitable;
import uk.modl.visitor.ModlVisitor;

@ToString
@EqualsAndHashCode
public class ConditionTest implements ModlVisitable {
    // The String in the immutable pair is an optional operator: & or |
    public final Vector<Tuple2<ConditionOrConditionGroupInterface, String>> conditions;

    public ConditionTest(final Vector<Tuple2<ConditionOrConditionGroupInterface, String>> conditions) {
        this.conditions = conditions;
    }

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
        conditions.forEach(s -> s._1.visit(visitor));
    }

}
