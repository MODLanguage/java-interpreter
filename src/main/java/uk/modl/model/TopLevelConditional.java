package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.Value;
import uk.modl.utils.IDSource;

@Value(staticConstructor = "of")
public class TopLevelConditional implements Structure {

    long id;

    @NonNull
    Vector<ConditionTest> tests;

    @NonNull
    Vector<TopLevelConditionalReturn> returns;

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
