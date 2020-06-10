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