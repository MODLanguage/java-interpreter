package uk.modl.interpreter;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class StringTransformerTest {
    @Test
    public void canHandleRefsEndingInPercent() {
        final List<String> result = StringTransformer.getPercentPartsFromString("%ref%");
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("%ref%", result.get(0));
    }
}
