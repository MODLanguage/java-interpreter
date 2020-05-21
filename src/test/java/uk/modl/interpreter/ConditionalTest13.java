package uk.modl.interpreter;

import lombok.val;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

public class ConditionalTest13 {

    public static final String EXPECTED = "{\"result\":\"test is not defined\"}";

    public static final String INPUT = "{\n" +
            " !test?\n" +
            "   result=test is not defined\n" +
            " /?\n" +
            "   result=test is defined\n" +
            "}";

    @Test
    public void parseOk() {
        val f = new Interpreter().andThen(new JacksonJsonNodeTransformer())
                .andThen(Util.jsonNodeToString);

        Assert.assertEquals(EXPECTED, f.apply(INPUT));
    }

}