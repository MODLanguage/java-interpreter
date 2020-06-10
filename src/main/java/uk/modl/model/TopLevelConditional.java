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
public class TopLevelConditional implements Structure, Parent, Child {

    @ToString.Exclude
    long id;

    @EqualsAndHashCode.Exclude
    @NonNull
    Vector<ConditionTest> tests;

    @EqualsAndHashCode.Exclude
    @NonNull
    Vector<TopLevelConditionalReturn> returns;

    @EqualsAndHashCode.Exclude
    @NonNull
    Vector<Structure> result;

    public static TopLevelConditional of(final Ancestry ancestry, final Parent parent, final Vector<ConditionTest> tests, final Vector<TopLevelConditionalReturn> returns, final Vector<Structure> result) {
        final TopLevelConditional child = TopLevelConditional.of(IDSource.nextId(), tests, returns, result);
        ancestry.add(parent, child);
        return child;
    }

    public TopLevelConditional with(final Ancestry ancestry, final Vector<ConditionTest> tests, final Vector<TopLevelConditionalReturn> returns) {
        final TopLevelConditional child = TopLevelConditional.of(id, tests, returns, result);
        ancestry.replaceChild(this, child);
        return child;
    }

    public TopLevelConditional with(final Ancestry ancestry, final Vector<Structure> result) {
        final TopLevelConditional child = TopLevelConditional.of(id, tests, returns, result);
        ancestry.replaceChild(this, child);
        return child;
    }

}
