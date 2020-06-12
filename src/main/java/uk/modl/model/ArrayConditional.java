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

    public static ArrayConditional of(final Ancestry ancestry, final Parent parent, final Vector<ConditionTest> tests, final Vector<ArrayConditionalReturn> returns, final Vector<ArrayItem> result) {
        final ArrayConditional child = ArrayConditional.of(IDSource.nextId(), tests, returns, result);
        ancestry.add(parent, child);
        return child;
    }

    public ArrayConditional with(final Ancestry ancestry, final Vector<ConditionTest> tests, final Vector<ArrayConditionalReturn> returns) {
        final ArrayConditional child = ArrayConditional.of(id, tests, returns, result);
        ancestry.replaceChild(this, child);
        return child;
    }

    public ArrayConditional with(final Ancestry ancestry, final Vector<ArrayItem> result) {
        final ArrayConditional child = ArrayConditional.of(id, tests, returns, result);
        ancestry.replaceChild(this, child);
        return child;
    }

}
