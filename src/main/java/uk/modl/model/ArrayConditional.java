package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.Value;
import uk.modl.utils.IDSource;

@Value(staticConstructor = "of")
public class ArrayConditional implements ArrayItem {

    long id;

    @NonNull
    Vector<ConditionTest> tests;

    @NonNull
    Vector<ArrayConditionalReturn> returns;

    @NonNull
    Vector<ArrayItem> result;

    public static ArrayConditional of(final Vector<ConditionTest> tests, final Vector<ArrayConditionalReturn> returns, final Vector<ArrayItem> result) {
        return ArrayConditional.of(IDSource.nextId(), tests, returns, result);
    }

    public ArrayConditional with(final Vector<ConditionTest> tests, final Vector<ArrayConditionalReturn> returns) {
        return ArrayConditional.of(id, tests, returns, result);
    }

    public ArrayConditional with(final Vector<ArrayItem> result) {
        return ArrayConditional.of(id, tests, returns, result);
    }

}
