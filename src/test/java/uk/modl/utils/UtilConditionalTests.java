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

package uk.modl.utils;

import io.vavr.collection.Vector;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.ancestry.Ancestry;
import uk.modl.ancestry.Parent;
import uk.modl.model.NumberPrimitive;
import uk.modl.model.ValueItem;

public class UtilConditionalTests {

    final Ancestry ancestry = new Ancestry();

    final Parent parent = () -> 0;

    @Test
    public void isGreaterThanAll() {
        final Vector<ValueItem> values = Vector.of(NumberPrimitive.of(ancestry, parent, "10"), NumberPrimitive.of(ancestry, parent, "20"), NumberPrimitive.of(ancestry, parent, "99"), NumberPrimitive.of(ancestry, parent, "-1"), NumberPrimitive.of(ancestry, parent, "0"));
        final boolean result = Util.greaterThanAll(NumberPrimitive.of(ancestry, parent, "100"), values);
        Assert.assertTrue(result);
    }

    @Test
    public void isNotGreaterThanAll() {
        final Vector<ValueItem> values = Vector.of(NumberPrimitive.of(ancestry, parent, "10"), NumberPrimitive.of(ancestry, parent, "100"), NumberPrimitive.of(ancestry, parent, "99"), NumberPrimitive.of(ancestry, parent, "-1"), NumberPrimitive.of(ancestry, parent, "0"));
        final boolean result = Util.greaterThanAll(NumberPrimitive.of(ancestry, parent, "100"), values);
        Assert.assertFalse(result);
    }

    @Test
    public void isGreaterThanOrEqualToAll() {
        final Vector<ValueItem> values = Vector.of(NumberPrimitive.of(ancestry, parent, "10"), NumberPrimitive.of(ancestry, parent, "20"), NumberPrimitive.of(ancestry, parent, "100"), NumberPrimitive.of(ancestry, parent, "-1"), NumberPrimitive.of(ancestry, parent, "0"));
        final boolean result = Util.greaterThanOrEqualToAll(NumberPrimitive.of(ancestry, parent, "100"), values);
        Assert.assertTrue(result);
    }

    @Test
    public void isNotGreaterThanOrEqualToAll() {
        final Vector<ValueItem> values = Vector.of(NumberPrimitive.of(ancestry, parent, "10"), NumberPrimitive.of(ancestry, parent, "101"), NumberPrimitive.of(ancestry, parent, "100"), NumberPrimitive.of(ancestry, parent, "-1"), NumberPrimitive.of(ancestry, parent, "0"));
        final boolean result = Util.greaterThanOrEqualToAll(NumberPrimitive.of(ancestry, parent, "100"), values);
        Assert.assertFalse(result);
    }

    @Test
    public void isLessThanAll() {
        final Vector<ValueItem> values = Vector.of(NumberPrimitive.of(ancestry, parent, "10"), NumberPrimitive.of(ancestry, parent, "20"), NumberPrimitive.of(ancestry, parent, "99"), NumberPrimitive.of(ancestry, parent, "-1"), NumberPrimitive.of(ancestry, parent, "0"));
        final boolean result = Util.lessThanAll(NumberPrimitive.of(ancestry, parent, "-2"), values);
        Assert.assertTrue(result);
    }

    @Test
    public void isNotLessThanAll() {
        final Vector<ValueItem> values = Vector.of(NumberPrimitive.of(ancestry, parent, "10"), NumberPrimitive.of(ancestry, parent, "20"), NumberPrimitive.of(ancestry, parent, "99"), NumberPrimitive.of(ancestry, parent, "-1"), NumberPrimitive.of(ancestry, parent, "0"));
        final boolean result = Util.lessThanAll(NumberPrimitive.of(ancestry, parent, "-1"), values);
        Assert.assertFalse(result);
    }

    @Test
    public void isLessThanOrEqualToAll() {
        final Vector<ValueItem> values = Vector.of(NumberPrimitive.of(ancestry, parent, "10"), NumberPrimitive.of(ancestry, parent, "20"), NumberPrimitive.of(ancestry, parent, "99"), NumberPrimitive.of(ancestry, parent, "-1"), NumberPrimitive.of(ancestry, parent, "0"));
        final boolean result = Util.lessThanOrEqualToAll(NumberPrimitive.of(ancestry, parent, "-1"), values);
        Assert.assertTrue(result);
    }

    @Test
    public void isNotLessThanOrEqualToAll() {
        final Vector<ValueItem> values = Vector.of(NumberPrimitive.of(ancestry, parent, "10"), NumberPrimitive.of(ancestry, parent, "20"), NumberPrimitive.of(ancestry, parent, "99"), NumberPrimitive.of(ancestry, parent, "-1"), NumberPrimitive.of(ancestry, parent, "0"));
        final boolean result = Util.lessThanOrEqualToAll(NumberPrimitive.of(ancestry, parent, "0"), values);
        Assert.assertFalse(result);
    }

}