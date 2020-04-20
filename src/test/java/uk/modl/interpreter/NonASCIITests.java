package uk.modl.interpreter;

import lombok.val;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

public class NonASCIITests {

    public static final String EXPECTED = "{\"å∫ç∂´´©\":\"œ∑´´¥\"}";
    public static final String INPUT = "å∫ç∂´´©=œ∑´´¥";

    @Test
    public void parseOk() {
        val f = new Interpreter().andThen(new JacksonJsonNodeTransformer())
                .andThen(Util.jsonNodeToString);

        Assert.assertEquals(EXPECTED, f.apply(INPUT));
    }

}