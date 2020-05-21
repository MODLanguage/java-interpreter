package uk.modl.interpreter;

import lombok.val;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

public class ConditionalTest12 {

    public static final String EXPECTED = "{\"result\":\"it's green\"}";

    public static final String INPUT = "_colour = green;\n" +
            "_test = { colour=green? true /? false }; \n" +
            "\n" +
            "{\n" +
            " !test?\n" +
            "   result=it's not green\n" +
            " /?\n" +
            "   result=it's green\n" +
            "}";

    @Test
    public void parseOk() {
        val f = new Interpreter().andThen(new JacksonJsonNodeTransformer())
                .andThen(Util.jsonNodeToString);

        Assert.assertEquals(EXPECTED, f.apply(INPUT));
    }

}