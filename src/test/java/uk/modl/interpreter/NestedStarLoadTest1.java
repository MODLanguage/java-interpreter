package uk.modl.interpreter;

import lombok.val;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

public class NestedStarLoadTest1 {

    public static final String EXPECTED1 = "{\"the_number\":3}";
    public static final String EXPECTED2 = "{\"the_number\":1}";
    public static final String INPUT1 = "*l=../grammar/tests/1:../grammar/tests/2:../grammar/tests/3;the_number=%number";
    public static final String INPUT2 = "*l=../grammar/tests/1:../grammar/tests/2:../grammar/tests/3:../grammar/tests/1;the_number=%number";

    @Test
    public void test1() {
        val f = new Interpreter().andThen(new JacksonJsonNodeTransformer())
                .andThen(Util.jsonNodeToString);

        Assert.assertEquals(EXPECTED1, f.apply(INPUT1));
    }

    @Test
    public void test2() {
        val f = new Interpreter().andThen(new JacksonJsonNodeTransformer())
                .andThen(Util.jsonNodeToString);

        Assert.assertEquals(EXPECTED2, f.apply(INPUT2));
    }
}