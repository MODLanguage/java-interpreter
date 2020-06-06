package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import uk.modl.utils.IDSource;

@Value(staticConstructor = "of")
public class MapConditional implements MapItem {

    @ToString.Exclude
    long id;

    @NonNull
    Vector<ConditionTest> tests;

    @NonNull
    Vector<MapConditionalReturn> returns;

    @NonNull
    Vector<MapItem> result;

    public static MapConditional of(final Vector<ConditionTest> tests, final Vector<MapConditionalReturn> returns, final Vector<MapItem> result) {
        return MapConditional.of(IDSource.nextId(), tests, returns, result);
    }

    public MapConditional with(final Vector<ConditionTest> tests, final Vector<MapConditionalReturn> returns) {
        return MapConditional.of(id, tests, returns, result);
    }

    public MapConditional with(final Vector<MapItem> result) {
        return MapConditional.of(id, tests, returns, result);
    }

}
