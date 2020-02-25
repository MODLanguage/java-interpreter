package uk.modl.interpreter;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.model.Modl;
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
    private static JacksonJsonNodeTransformer jsonTransformer = new JacksonJsonNodeTransformer();

    @Test
    public void success_1() {
        final Modl modl = new Interpreter().apply(INPUT_1);
        Assert.assertNotNull(modl);
        final JsonNode jsonResult = jsonTransformer.apply(modl);

        final String mapResult = Util.jsonNodeToString.apply(jsonResult);
        Assert.assertNotNull(mapResult);
        Assert.assertEquals(EXPECTED_1, mapResult);
    }

    @Test
    public void success_2() {
        final Modl modl = new Interpreter().apply(INPUT_2);
        Assert.assertNotNull(modl);
        final JsonNode jsonResult = jsonTransformer.apply(modl);

        final String mapResult = Util.jsonNodeToString.apply(jsonResult);
        Assert.assertNotNull(mapResult);
        Assert.assertEquals(EXPECTED_2, mapResult);
    }

    @Test
    public void success_3() {
        final Modl modl = new Interpreter().apply(INPUT_3);
        Assert.assertNotNull(modl);
        final JsonNode jsonResult = jsonTransformer.apply(modl);

        final String mapResult = Util.jsonNodeToString.apply(jsonResult);
        Assert.assertNotNull(mapResult);
        Assert.assertEquals(EXPECTED_3, mapResult);
    }

    @Test
    public void success_4() {
        final Modl modl = new Interpreter().apply(INPUT_4);
        Assert.assertNotNull(modl);
        final JsonNode jsonResult = jsonTransformer.apply(modl);

        final String mapResult = Util.jsonNodeToString.apply(jsonResult);
        Assert.assertNotNull(mapResult);
        Assert.assertEquals(EXPECTED_4, mapResult);
    }

}