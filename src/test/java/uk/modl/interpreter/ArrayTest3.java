package uk.modl.interpreter;

import lombok.val;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

public class ArrayTest3 {

    public static final String EXPECTED = "{\"x\":[1,null,null,null,null,null,null,[2,null,null,null,null,null,null,null,null,3],null,null,null,null,null,null,null,null,null,[4,null,null,null,null,null,null,null,null,5]]}";

    public static final String INPUT = "x=[1;;;;;;;2:::::::::3;;;;;;;;;;4:::::::::5]";

    @Test
    public void parseOk() {
        val f = new Interpreter().andThen(new JacksonJsonNodeTransformer())
                .andThen(Util.jsonNodeToString);

        Assert.assertEquals(EXPECTED, f.apply(INPUT));
    }

}