package uk.modl.interpreter;

import lombok.val;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

public class ConditionalTest10 {

    public static final String EXPECTED = "{\"result\":\"num1 is bigger\"}";
    public static final String INPUT = "_num1=5;_num2=2;result={num1>%num2?num1 is bigger/?num1 is not bigger}";

    @Test
    public void parseOk() {
        val f = new Interpreter().andThen(new JacksonJsonNodeTransformer())
                .andThen(Util.jsonNodeToString);

        Assert.assertEquals(EXPECTED, f.apply(INPUT));
    }

}