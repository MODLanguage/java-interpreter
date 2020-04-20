package uk.modl.interpreter;

import lombok.val;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

public class SimpleReferenceTest2 {

    public static final String EXPECTED = "{\"out\":\"NotThisOneblahdeblah\"}";
    public static final String INPUT = "_var = NotThisOne;_var=%var%blah;out=%var%deblah";

    @Test
    public void parseOk() {
        val f = new Interpreter().andThen(new JacksonJsonNodeTransformer())
                .andThen(Util.jsonNodeToString);

        Assert.assertEquals(EXPECTED, f.apply(INPUT));
    }

}