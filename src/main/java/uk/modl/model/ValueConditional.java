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
public class ValueConditional implements ValueItem, Parent, Child {

    @ToString.Exclude
    long id;

    @EqualsAndHashCode.Exclude
    @NonNull
    Vector<ConditionTest> tests;

    @EqualsAndHashCode.Exclude
    @NonNull
    Vector<ValueConditionalReturn> returns;

    @EqualsAndHashCode.Exclude
    @NonNull
    Vector<ValueItem> result;

    public static ValueConditional of(final Ancestry ancestry, final Parent parent, final Vector<ConditionTest> tests, final Vector<ValueConditionalReturn> returns, final Vector<ValueItem> result) {
        final ValueConditional child = ValueConditional.of(IDSource.nextId(), tests, returns, result);
        ancestry.add(parent, child);
        return child;
    }

    @Override
    public Number numericValue() {
        throw new RuntimeException("Cannot convert a conditional to a numeric value.");
    }

    public ValueConditional with(final Ancestry ancestry, final Vector<ConditionTest> tests, final Vector<ValueConditionalReturn> returns) {
        final ValueConditional child = ValueConditional.of(id, tests, returns, result);
        ancestry.replaceChild(this, child);
        return child;
    }

    public ValueConditional with(final Ancestry ancestry, final Vector<ValueItem> result) {
        final ValueConditional child = ValueConditional.of(id, tests, returns, result);
        ancestry.replaceChild(this, child);
        return child;
    }

}
