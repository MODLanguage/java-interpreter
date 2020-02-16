package uk.modl.interpreter;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.model.Modl;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

public class ObjectIndexReferenceTest {

    public static final String EXPECTED = "[{\"letters\":[\"a\",\"b\",\"c\"]},{\"letters2\":\"defdef\"}]";
    public static final String INPUT = "?[[a;b;c];d;e;f];letters=%0;letters2=%1%%2%%3%%1%%2%%3%";
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