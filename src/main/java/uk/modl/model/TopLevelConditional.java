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

    public static TopLevelConditional of(final Vector<ConditionTest> tests, final Vector<TopLevelConditionalReturn> returns, final Vector<Structure> result) {
        return TopLevelConditional.of(IDSource.nextId(), tests, returns, result);
    }

    public TopLevelConditional with(final Vector<ConditionTest> tests, final Vector<TopLevelConditionalReturn> returns) {
        return TopLevelConditional.of(id, tests, returns, result);
    }

    public TopLevelConditional with(final Vector<Structure> result) {
        return TopLevelConditional.of(id, tests, returns, result);
    }

}
