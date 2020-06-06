package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import uk.modl.ancestry.Child;
import uk.modl.ancestry.Parent;
import uk.modl.utils.IDSource;

@Value(staticConstructor = "of")
public class MapConditional implements MapItem, Parent, Child {

    @ToString.Exclude
    long id;

    @EqualsAndHashCode.Exclude
    @NonNull
    Vector<ConditionTest> tests;

    @EqualsAndHashCode.Exclude
    @NonNull
    Vector<MapConditionalReturn> returns;

    @EqualsAndHashCode.Exclude
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
