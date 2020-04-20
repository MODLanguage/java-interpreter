package uk.modl.interpreter;

import lombok.val;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

public class ArrayConditionalTest1 {

    public static final String EXPECTED = "{\"country\":\"gb\",\"x\":[\"this\"]}";
    public static final String INPUT = "country=gb;x=[{ country=gb ? this /country=us? that }]";

    @Test
    public void parseOk() {
        val f = new Interpreter().andThen(new JacksonJsonNodeTransformer())
                .andThen(Util.jsonNodeToString);

        Assert.assertEquals(EXPECTED, f.apply(INPUT));
    }

}