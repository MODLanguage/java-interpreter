package uk.modl.interpreter;

import lombok.val;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

public class TopLevelConditionalTest {

    public static final String EXPECTED_1 = "{\"result\":\"match\"}";
    public static final String EXPECTED_2 = "{\"result\":\"nomatch\"}";
    public static final String EXPECTED_3 = "{\"result\":\"match\"}";
    public static final String EXPECTED_4 = "{\"result\":\"nomatch\"}";
    public static final String INPUT_1 = "_test1=one;_one=two;{test1=`one`?result=match/?result=nomatch}";
    public static final String INPUT_2 = "_test1=one;_one=two;{test1!=`one`?result=match/?result=nomatch}";
    public static final String INPUT_3 = "_test1=one;_one=two;{test1=\"one\"?result=match/?result=nomatch}";
    public static final String INPUT_4 = "_test1=one;_one=two;{test1=two?result=match/?result=nomatch}";

    @Test
    public void success_1() {
        val f = new Interpreter().andThen(new JacksonJsonNodeTransformer())
                .andThen(Util.jsonNodeToString);

        Assert.assertEquals(EXPECTED_1, f.apply(INPUT_1));
    }

    @Test
    public void success_2() {
        val f = new Interpreter().andThen(new JacksonJsonNodeTransformer())
                .andThen(Util.jsonNodeToString);

        Assert.assertEquals(EXPECTED_2, f.apply(INPUT_2));
    }

    @Test
    public void success_3() {
        val f = new Interpreter().andThen(new JacksonJsonNodeTransformer())
                .andThen(Util.jsonNodeToString);

        Assert.assertEquals(EXPECTED_3, f.apply(INPUT_3));
    }

    @Test
    public void success_4() {
        val f = new Interpreter().andThen(new JacksonJsonNodeTransformer())
                .andThen(Util.jsonNodeToString);

        Assert.assertEquals(EXPECTED_4, f.apply(INPUT_4));
    }

}