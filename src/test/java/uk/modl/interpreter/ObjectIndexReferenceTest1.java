package uk.modl.interpreter;

import lombok.val;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

public class ObjectIndexReferenceTest1 {

    public static final String EXPECTED = "{\"letters\":[\"a\",\"b\",\"c\"],\"letters2\":\"defdef\"}";
    public static final String INPUT = "?[[a;b;c];d;e;f];letters=%0;letters2=%1%%2%%3%%1%%2%%3%";

    @Test
    public void parseOk() {
        val f = new Interpreter().andThen(new JacksonJsonNodeTransformer())
                .andThen(Util.jsonNodeToString);

        Assert.assertEquals(EXPECTED, f.apply(INPUT));
    }

}