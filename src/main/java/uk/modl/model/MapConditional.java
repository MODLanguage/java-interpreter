package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import uk.modl.ancestry.Ancestry;
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

    public static MapConditional of(final Ancestry ancestry, final Parent parent, final Vector<ConditionTest> tests, final Vector<MapConditionalReturn> returns, final Vector<MapItem> result) {
        final MapConditional child = MapConditional.of(IDSource.nextId(), tests, returns, result);
        ancestry.add(parent, child);
        return child;
    }

    public MapConditional with(final Ancestry ancestry, final Vector<ConditionTest> tests, final Vector<MapConditionalReturn> returns) {
        final MapConditional child = MapConditional.of(id, tests, returns, result);
        ancestry.replaceChild(this, child);
        return child;
    }

    public MapConditional with(final Ancestry ancestry, final Vector<MapItem> result) {
        final MapConditional child = MapConditional.of(id, tests, returns, result);
        ancestry.replaceChild(this, child);
        return child;
    }

}
