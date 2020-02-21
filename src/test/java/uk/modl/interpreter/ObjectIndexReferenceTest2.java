package uk.modl.interpreter;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.model.Modl;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

public class ObjectIndexReferenceTest2 {

    public static final String EXPECTED = "{\"test1\":\"one\",\"test2\":\"two\",\"test3\":\"three\",\"test4\":\"%3\",\"test5\":\"%4\",\"test6\":\"%5\",\"test7\":\"%6\",\"test8\":\"%7\",\"test9\":\"%8\"}";
    public static final String INPUT = "?=one:two:three;test1=%0;test2=%1;test3=%2;test4=%3;test5=%4;test6=%5;test7=%6;test8=%7;test9=%8";
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