package uk.modl.model;

import lombok.NonNull;
import lombok.Value;
import uk.modl.visitor.ModlVisitor;

@Value
public class Pair implements Structure, MapItem, ValueItem, ArrayItem {

    @NonNull
    String key;

    @NonNull
    PairValue value;

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
    }

    @Override
    public Number numericValue() {
        return value.numericValue();
    }

}
