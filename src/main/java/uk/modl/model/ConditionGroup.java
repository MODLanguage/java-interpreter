package uk.modl.model;

import io.vavr.Tuple2;
import io.vavr.collection.Vector;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import uk.modl.visitor.ModlVisitor;

@ToString
@EqualsAndHashCode
public class ConditionGroup implements ConditionOrConditionGroupInterface {
    public final Vector<Tuple2<ConditionTest, String>> subConditionList;
    public final boolean shouldNegate;

    public ConditionGroup(final Vector<Tuple2<ConditionTest, String>> subConditionList, final boolean shouldNegate) {
        this.subConditionList = subConditionList;
        this.shouldNegate = shouldNegate;
    }

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
        subConditionList.forEach(s -> s._1.visit(visitor));
    }

}
