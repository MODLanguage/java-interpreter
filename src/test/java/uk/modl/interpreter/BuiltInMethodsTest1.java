package uk.modl.interpreter;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.model.Modl;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

public class BuiltInMethodsTest1 {

    public static final String EXPECTED = "{\"upcase_example\":\"QUICK-TEST OF JOHN'S VARIABLE_METHODS\",\"downcase_example\":\"quick-test of john's variable_methods\",\"initcap_example\":\"Quick-test Of John's Variable_methods\",\"sentence_example\":\"Quick-test of John's variable_methods\",\"url_encode_example\":\"QUICK-TEST+OF+JOHN%27S+VARIABLE_METHODS\"}";
    public static final String INPUT = "_testing = quick-test of John's variable_methods;upcase_example = %testing.u;downcase_example = %testing.d;initcap_example = %testing.i;sentence_example = %testing.s;url_encode_example = %testing.u.e";
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