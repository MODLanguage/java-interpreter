package uk.modl.utils;

import io.vavr.collection.Vector;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.model.NumberPrimitive;
import uk.modl.model.ValueItem;

public class UtilConditionalTests {


    @Test
    public void isGreaterThanAll() {
        final Vector<ValueItem> values = Vector.of(new NumberPrimitive("10"), new NumberPrimitive("20"), new NumberPrimitive("99"), new NumberPrimitive("-1"), new NumberPrimitive("0"));
        final boolean result = Util.greaterThanAll(new NumberPrimitive("100"), values);
        Assert.assertTrue(result);
    }

    @Test
    public void isNotGreaterThanAll() {
        final Vector<ValueItem> values = Vector.of(new NumberPrimitive("10"), new NumberPrimitive("100"), new NumberPrimitive("99"), new NumberPrimitive("-1"), new NumberPrimitive("0"));
        final boolean result = Util.greaterThanAll(new NumberPrimitive("100"), values);
        Assert.assertFalse(result);
    }

    @Test
    public void isGreaterThanOrEqualToAll() {
        final Vector<ValueItem> values = Vector.of(new NumberPrimitive("10"), new NumberPrimitive("20"), new NumberPrimitive("100"), new NumberPrimitive("-1"), new NumberPrimitive("0"));
        final boolean result = Util.greaterThanOrEqualToAll(new NumberPrimitive("100"), values);
        Assert.assertTrue(result);
    }

    @Test
    public void isNotGreaterThanOrEqualToAll() {
        final Vector<ValueItem> values = Vector.of(new NumberPrimitive("10"), new NumberPrimitive("101"), new NumberPrimitive("100"), new NumberPrimitive("-1"), new NumberPrimitive("0"));
        final boolean result = Util.greaterThanOrEqualToAll(new NumberPrimitive("100"), values);
        Assert.assertFalse(result);
    }

    @Test
    public void isLessThanAll() {
        final Vector<ValueItem> values = Vector.of(new NumberPrimitive("10"), new NumberPrimitive("20"), new NumberPrimitive("99"), new NumberPrimitive("-1"), new NumberPrimitive("0"));
        final boolean result = Util.lessThanAll(new NumberPrimitive("-2"), values);
        Assert.assertTrue(result);
    }

    @Test
    public void isNotLessThanAll() {
        final Vector<ValueItem> values = Vector.of(new NumberPrimitive("10"), new NumberPrimitive("20"), new NumberPrimitive("99"), new NumberPrimitive("-1"), new NumberPrimitive("0"));
        final boolean result = Util.lessThanAll(new NumberPrimitive("-1"), values);
        Assert.assertFalse(result);
    }

    @Test
    public void isLessThanOrEqualToAll() {
        final Vector<ValueItem> values = Vector.of(new NumberPrimitive("10"), new NumberPrimitive("20"), new NumberPrimitive("99"), new NumberPrimitive("-1"), new NumberPrimitive("0"));
        final boolean result = Util.lessThanOrEqualToAll(new NumberPrimitive("-1"), values);
        Assert.assertTrue(result);
    }

    @Test
    public void isNotLessThanOrEqualToAll() {
        final Vector<ValueItem> values = Vector.of(new NumberPrimitive("10"), new NumberPrimitive("20"), new NumberPrimitive("99"), new NumberPrimitive("-1"), new NumberPrimitive("0"));
        final boolean result = Util.lessThanOrEqualToAll(new NumberPrimitive("0"), values);
        Assert.assertFalse(result);
    }

}