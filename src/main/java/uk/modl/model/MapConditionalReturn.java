package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.Value;
import uk.modl.visitor.ModlVisitable;
import uk.modl.visitor.ModlVisitor;

@Value
public class MapConditionalReturn implements ModlVisitable {
    @NonNull
    Vector<MapItem> items;

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
        items.forEach(s -> s.visit(visitor));
    }
}
