package uk.modl.interpreter;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.model.Modl;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

public class NestedStarLoadTest1 {

    public static final String EXPECTED1 = "{\"the_number\":3}";
    public static final String EXPECTED2 = "{\"the_number\":1}";
    public static final String INPUT1 = "*l=../grammar/tests/1:../grammar/tests/2:../grammar/tests/3;the_number=%number";
    public static final String INPUT2 = "*l=../grammar/tests/1:../grammar/tests/2:../grammar/tests/3:../grammar/tests/1;the_number=%number";
    private static Interpreter interpreter = new Interpreter();
    private static JacksonJsonNodeTransformer jsonTransformer = new JacksonJsonNodeTransformer();

    @Test
    public void test1() {
        final Modl modl = interpreter.apply(INPUT1);
        Assert.assertNotNull(modl);
        final JsonNode jsonResult = jsonTransformer.apply(modl);

        final String mapResult = Util.jsonNodeToString.apply(jsonResult);
        Assert.assertNotNull(mapResult);
        Assert.assertEquals(EXPECTED1, mapResult);
    }

    @Test
    public void test2() {
        final Modl modl = interpreter.apply(INPUT2);
        Assert.assertNotNull(modl);
        final JsonNode jsonResult = jsonTransformer.apply(modl);

        final String mapResult = Util.jsonNodeToString.apply(jsonResult);
        Assert.assertNotNull(mapResult);
        Assert.assertEquals(EXPECTED2, mapResult);
    }
}