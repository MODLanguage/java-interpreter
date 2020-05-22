package uk.modl.interpreter;

import lombok.val;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

public class MethodTest1 {

    public static final String EXPECTED = "{\"friendly_name\":\"Smiths Limited\"}";

    public static final String INPUT = "*method(\n" +
            " *id=cn;\n" +
            " *name=company_name;\n" +
            " *transform=replace<-, >.trim<.>.initcap\n" +
            ");\n" +
            "\n" +
            "_domain = smiths-limited.com;\n" +
            "friendly_name = %domain.cn";

    @Test
    public void parseOk() {
        val f = new Interpreter().andThen(new JacksonJsonNodeTransformer())
                .andThen(Util.jsonNodeToString);

        Assert.assertEquals(EXPECTED, f.apply(INPUT));
    }

}