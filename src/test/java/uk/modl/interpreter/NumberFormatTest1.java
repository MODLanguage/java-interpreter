package uk.modl.interpreter;

import lombok.val;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

public class NumberFormatTest1 {

    public static final String EXPECTED = "{\"print\":\"20180921 08:20 2\"}";

    public static final String INPUT = "_var=2;\n*L=\"http://modl.uk/tests/testing.txt!\";\nprint=%update_date";

    @Test
    public void parseOk() {
        val f = new Interpreter().andThen(new JacksonJsonNodeTransformer())
                .andThen(Util.jsonNodeToString);

        Assert.assertEquals(EXPECTED, f.apply(INPUT));
    }

}