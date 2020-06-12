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
public class Array implements PairValue, Structure, ValueItem, ArrayItem, Parent, Child {

    @ToString.Exclude
    long id;

    @EqualsAndHashCode.Exclude
    @NonNull
    Vector<ArrayItem> arrayItems;

    public static Array of(final Ancestry ancestry, final Parent parent, final Vector<ArrayItem> arrayItems) {
        final Array child = Array.of(IDSource.nextId(), arrayItems);
        ancestry.add(parent, child);
        return child;
    }

    @Override
    public Number numericValue() {
        throw new RuntimeException("Cannot convert an array to a numeric value.");
    }

    public Array with(final Ancestry ancestry, final Vector<ArrayItem> arrayItems) {
        final Array child = Array.of(id, arrayItems);
        ancestry.replaceChild(this, child);
        return child;
    }

}
