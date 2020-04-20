package uk.modl.interpreter;

import lombok.val;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

public class ComplexConditionalReferenceTest1 {

    public static final String EXPECTED = "{\"test\":\"yes\"}";
    public static final String INPUT = "_array[1;2];_array_item=%array.0;test={array_item=%array.0?yes/?no}";

    @Test
    public void parseOk() {
        val f = new Interpreter().andThen(new JacksonJsonNodeTransformer())
                .andThen(Util.jsonNodeToString);

        Assert.assertEquals(EXPECTED, f.apply(INPUT));
    }

}