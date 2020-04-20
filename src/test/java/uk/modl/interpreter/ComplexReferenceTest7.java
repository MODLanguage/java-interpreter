package uk.modl.interpreter;

import lombok.val;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

public class ComplexReferenceTest7 {

    public static final String EXPECTED = "{\"country_name\":\"United Kingdom\"}";
    public static final String INPUT = "_C=gb;_COUNTRIES(us=United States;gb=United Kingdom;de=Germany);country_name=%COUNTRIES.%C";

    @Test
    public void parseOk() {
        val f = new Interpreter().andThen(new JacksonJsonNodeTransformer())
                .andThen(Util.jsonNodeToString);

        Assert.assertEquals(EXPECTED, f.apply(INPUT));
    }

}