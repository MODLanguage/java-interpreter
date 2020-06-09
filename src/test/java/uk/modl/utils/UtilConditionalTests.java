package uk.modl.utils;

import io.vavr.collection.Vector;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.model.NumberPrimitive;
import uk.modl.model.ValueItem;

public class UtilConditionalTests {


    @Test
    public void isGreaterThanAll() {
        final Vector<ValueItem> values = Vector.of(NumberPrimitive.of("10"), NumberPrimitive.of("20"), NumberPrimitive.of("99"), NumberPrimitive.of("-1"), NumberPrimitive.of("0"));
        final boolean result = Util.greaterThanAll(NumberPrimitive.of("100"), values);
        Assert.assertTrue(result);
    }

    @Test
    public void isNotGreaterThanAll() {
        final Vector<ValueItem> values = Vector.of(NumberPrimitive.of("10"), NumberPrimitive.of("100"), NumberPrimitive.of("99"), NumberPrimitive.of("-1"), NumberPrimitive.of("0"));
        final boolean result = Util.greaterThanAll(NumberPrimitive.of("100"), values);
        Assert.assertFalse(result);
    }

    @Test
    public void isGreaterThanOrEqualToAll() {
        final Vector<ValueItem> values = Vector.of(NumberPrimitive.of("10"), NumberPrimitive.of("20"), NumberPrimitive.of("100"), NumberPrimitive.of("-1"), NumberPrimitive.of("0"));
        final boolean result = Util.greaterThanOrEqualToAll(NumberPrimitive.of("100"), values);
        Assert.assertTrue(result);
    }

    @Test
    public void isNotGreaterThanOrEqualToAll() {
        final Vector<ValueItem> values = Vector.of(NumberPrimitive.of("10"), NumberPrimitive.of("101"), NumberPrimitive.of("100"), NumberPrimitive.of("-1"), NumberPrimitive.of("0"));
        final boolean result = Util.greaterThanOrEqualToAll(NumberPrimitive.of("100"), values);
        Assert.assertFalse(result);
    }

    @Test
    public void isLessThanAll() {
        final Vector<ValueItem> values = Vector.of(NumberPrimitive.of("10"), NumberPrimitive.of("20"), NumberPrimitive.of("99"), NumberPrimitive.of("-1"), NumberPrimitive.of("0"));
        final boolean result = Util.lessThanAll(NumberPrimitive.of("-2"), values);
        Assert.assertTrue(result);
    }

    @Test
    public void isNotLessThanAll() {
        final Vector<ValueItem> values = Vector.of(NumberPrimitive.of("10"), NumberPrimitive.of("20"), NumberPrimitive.of("99"), NumberPrimitive.of("-1"), NumberPrimitive.of("0"));
        final boolean result = Util.lessThanAll(NumberPrimitive.of("-1"), values);
        Assert.assertFalse(result);
    }

    @Test
    public void isLessThanOrEqualToAll() {
        final Vector<ValueItem> values = Vector.of(NumberPrimitive.of("10"), NumberPrimitive.of("20"), NumberPrimitive.of("99"), NumberPrimitive.of("-1"), NumberPrimitive.of("0"));
        final boolean result = Util.lessThanOrEqualToAll(NumberPrimitive.of("-1"), values);
        Assert.assertTrue(result);
    }

    @Test
    public void isNotLessThanOrEqualToAll() {
        final Vector<ValueItem> values = Vector.of(NumberPrimitive.of("10"), NumberPrimitive.of("20"), NumberPrimitive.of("99"), NumberPrimitive.of("-1"), NumberPrimitive.of("0"));
        final boolean result = Util.lessThanOrEqualToAll(NumberPrimitive.of("0"), values);
        Assert.assertFalse(result);
    }

}