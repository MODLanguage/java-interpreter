package uk.modl.interpreter;

import lombok.val;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

public class ComplexReferenceTest4 {

    public static final String EXPECTED = "{\"name\":\"пример\",\"department\":\"обслуживание клиентов\"}";
    public static final String INPUT = "(name=%`e1afmkfd`.p;department=%` -7sbcecqbdsccxfizhcp6b8ah`.p)";

    @Test
    public void parseOk() {
        val f = new Interpreter().andThen(new JacksonJsonNodeTransformer())
                .andThen(Util.jsonNodeToString);

        Assert.assertEquals(EXPECTED, f.apply(INPUT));
    }

}