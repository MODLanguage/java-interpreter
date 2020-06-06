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
public class ArrayConditional implements ArrayItem, Parent, Child {

    @ToString.Exclude
    long id;

    @EqualsAndHashCode.Exclude
    @NonNull
    Vector<ConditionTest> tests;

    @EqualsAndHashCode.Exclude
    @NonNull
    Vector<ArrayConditionalReturn> returns;

    @EqualsAndHashCode.Exclude
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
