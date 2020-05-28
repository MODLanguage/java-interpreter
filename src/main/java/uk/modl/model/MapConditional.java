package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.Value;
import lombok.With;
import uk.modl.visitor.ModlVisitor;

@Value
@With
public class MapConditional implements MapItem {

    @NonNull
    Vector<ConditionTest> tests;

    @NonNull
    Vector<MapConditionalReturn> returns;

    @NonNull
    Vector<MapItem> result;

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
        tests.forEach(s -> s.visit(visitor));
        returns.forEach(s -> s.visit(visitor));
    }

}
