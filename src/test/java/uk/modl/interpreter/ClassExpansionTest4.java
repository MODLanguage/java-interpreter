package uk.modl.interpreter;

import lombok.val;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

public class ClassExpansionTest4 {

    public static final String EXPECTED = "{\"message\":{\"direction\":\"in\",\"date_time\":\"2018-03-22\",\"message\":\"hi\",\"method\":\"sms\"}}";

    public static final String INPUT = "*c(\n" +
            " *i=m;\n" +
            " *n=message;\n" +
            " *S=map;\n" +
            " *a=[\n" +
            "   [direction;date_time;message]\n" +
            " ];\n" +
            " method=sms\n" +
            ");\n" +
            "m=in:2018-03-22:hi";

    @Test
    public void parseOk() {
        val f = new Interpreter().andThen(new JacksonJsonNodeTransformer())
                .andThen(Util.jsonNodeToString);

        Assert.assertEquals(EXPECTED, f.apply(INPUT));

    }

}