package uk.modl.model;

import io.vavr.Tuple2;
import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.Value;
import uk.modl.visitor.ModlVisitor;

@Value
public class ConditionGroup implements ConditionOrConditionGroupInterface {
    @NonNull
    Vector<Tuple2<ConditionTest, String>> subConditionList;
    boolean shouldNegate;

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
        subConditionList.forEach(s -> s._1.visit(visitor));
    }

}
