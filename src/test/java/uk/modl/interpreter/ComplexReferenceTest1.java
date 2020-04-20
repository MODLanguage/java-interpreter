package uk.modl.interpreter;

import lombok.val;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

public class ComplexReferenceTest1 {

    public static final String EXPECTED = "{\"z\":true}";
    public static final String INPUT = "_x=a:b:c:d;_y=[1;2;3;4];z={%x.1=b | %y.2=1?}";

    @Test
    public void parseOk() {
        val f = new Interpreter().andThen(new JacksonJsonNodeTransformer())
                .andThen(Util.jsonNodeToString);

        Assert.assertEquals(EXPECTED, f.apply(INPUT));
    }

}