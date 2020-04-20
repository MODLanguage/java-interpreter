package uk.modl.interpreter;

import lombok.val;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

public class ComplexReferenceTest5 {

    public static final String EXPECTED = "{\"name\":\"пример\",\"department\":\"обслуживание клиентов\"}";
    public static final String INPUT = "?=`e1afmkfd`:` -7sbcecqbdsccxfizhcp6b8ah`;(name=%0.p;department=%1.p)";

    @Test
    public void parseOk() {
        val f = new Interpreter().andThen(new JacksonJsonNodeTransformer())
                .andThen(Util.jsonNodeToString);

        Assert.assertEquals(EXPECTED, f.apply(INPUT));
    }

}