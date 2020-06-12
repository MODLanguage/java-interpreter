/*
 * MIT License
 *
 * Copyright (c) 2020 NUM Technology Ltd
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

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
