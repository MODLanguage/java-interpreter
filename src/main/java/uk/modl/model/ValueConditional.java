package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.Value;
import lombok.With;
import uk.modl.parser.errors.InterpreterError;
import uk.modl.visitor.ModlVisitor;

@Value
@With
public class ValueConditional implements ValueItem {

    @NonNull
    Vector<ConditionTest> tests;

    @NonNull
    Vector<ValueConditionalReturn> returns;

    @NonNull
    Vector<ValueItem> result;

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
        tests.forEach(s -> s.visit(visitor));
        returns.forEach(s -> s.visit(visitor));
    }

    @Override
    public Number numericValue() {
        throw new InterpreterError("Cannot convert a conditional to a numeric value.");
    }

}
