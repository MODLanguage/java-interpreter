package uk.modl.interpreter;

import lombok.val;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

public class ConditionalTest15 {

    public static final String EXPECTED = "{\"country\":\"us\",\"language\":\"en\",\"time_zone\":\"EST\"}";

    public static final String INPUT = "## country\n" +
            "_c = us;\n" +
            "## language\n" +
            "_l = en;\n" +
            "\n" +
            "*L=../grammar/tests/import_config.modl;\n" +
            "\n" +
            "country = %c;\n" +
            "language = %l;\n" +
            "time_zone = %tz";

    @Test
    public void parseOk() {
        val f = new Interpreter().andThen(new JacksonJsonNodeTransformer())
                .andThen(Util.jsonNodeToString);

        Assert.assertEquals(EXPECTED, f.apply(INPUT));
    }

}