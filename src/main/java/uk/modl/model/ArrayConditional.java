package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.Value;
import lombok.With;
import uk.modl.visitor.ModlVisitor;

@Value
@With
public class ArrayConditional implements ArrayItem {

    @NonNull
    Vector<ConditionTest> tests;

    @NonNull
    Vector<ArrayConditionalReturn> returns;

    @NonNull
    Vector<ArrayItem> result;

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
        tests.forEach(s -> s.visit(visitor));
        returns.forEach(s -> s.visit(visitor));
    }

}
