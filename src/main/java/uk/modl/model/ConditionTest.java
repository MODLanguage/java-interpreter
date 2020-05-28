package uk.modl.model;

import io.vavr.Tuple2;
import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.Value;
import uk.modl.visitor.ModlVisitable;
import uk.modl.visitor.ModlVisitor;

@Value
public class ConditionTest implements ModlVisitable {

    // The String in the immutable pair is an optional operator: & or |
    @NonNull
    Vector<Tuple2<ConditionOrConditionGroupInterface, String>> conditions;

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
        conditions.forEach(s -> s._1.visit(visitor));
    }

}
