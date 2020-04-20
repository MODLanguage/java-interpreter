package uk.modl.interpreter;

import lombok.val;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

public class ComplexReferenceTest9 {

    public static final String EXPECTED = "{\"a\":{\"b\":{\"c\":{\"d\":{\"e\":{\"f\":1}}}}},\"testing\":1}";
    public static final String INPUT = "a(b(c(d(e(f=1)))));testing=%a.b.c.d.e.f";

    @Test
    public void parseOk() {
        val f = new Interpreter().andThen(new JacksonJsonNodeTransformer())
                .andThen(Util.jsonNodeToString);

        Assert.assertEquals(EXPECTED, f.apply(INPUT));
    }

}