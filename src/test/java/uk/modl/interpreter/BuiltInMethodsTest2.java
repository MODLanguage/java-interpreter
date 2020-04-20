package uk.modl.interpreter;

import lombok.val;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

public class BuiltInMethodsTest2 {

    public static final String EXPECTED = "{\"test\":\"Blah huzzahne t\"}";
    public static final String INPUT = "?=one:two;test=Blah %0.r<o,huzzah> %1.t<w>";

    @Test
    public void parseOk() {
        val f = new Interpreter().andThen(new JacksonJsonNodeTransformer())
                .andThen(Util.jsonNodeToString);

        Assert.assertEquals(EXPECTED, f.apply(INPUT));
    }

}