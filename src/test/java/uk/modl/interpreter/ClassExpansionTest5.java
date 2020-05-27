package uk.modl.interpreter;

import lombok.val;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

public class ClassExpansionTest5 {

    public static final String EXPECTED = "{\"alpha\":{\"value\":1,\"x\":\"test\"}}";

    public static final String INPUT = "*class(*id=a;*name=alpha;x=\"test\");a=1";

    @Test
    public void parseOk() {
        val f = new Interpreter().andThen(new JacksonJsonNodeTransformer())
                .andThen(Util.jsonNodeToString);

        Assert.assertEquals(EXPECTED, f.apply(INPUT));
    }

}