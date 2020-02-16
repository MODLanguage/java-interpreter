package uk.modl.interpreter;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.model.Modl;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

public class SimpleConditionalReferenceTest {

    public static final String EXPECTED = "{\"msg\":true}";
    public static final String INPUT = "_var1=Hello;_var2=World;msg={%var1!=%var2?}";
    private static Interpreter interpreter = new Interpreter();
    private static JacksonJsonNodeTransformer jsonTransformer = new JacksonJsonNodeTransformer();

    @Test
    public void parseOk() {
        final Modl modl = interpreter.apply(INPUT);
        Assert.assertNotNull(modl);
        final JsonNode jsonResult = jsonTransformer.apply(modl);

        final String mapResult = Util.jsonNodeToString.apply(jsonResult);
        Assert.assertNotNull(mapResult);
        Assert.assertEquals(EXPECTED, mapResult);
    }

}