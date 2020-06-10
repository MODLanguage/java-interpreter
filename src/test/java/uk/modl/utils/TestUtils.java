package uk.modl.utils;

import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.Tuple2;
import lombok.experimental.UtilityClass;
import org.junit.Assert;
import uk.modl.interpreter.Interpreter;
import uk.modl.model.Modl;
import uk.modl.transforms.JacksonJsonNodeTransform;
import uk.modl.transforms.TransformationContext;

@UtilityClass
public class TestUtils {

    public void runTest(final String input, final String expected) {
        final Interpreter interpreter = new Interpreter();
        final TransformationContext ctx = TransformationContext.emptyCtx();
        final Tuple2<TransformationContext, Modl> interpreted = interpreter.apply(ctx, input);
        final JsonNode json = new JacksonJsonNodeTransform(ctx).apply(interpreted._2);
        final String result = Util.jsonNodeToString.apply(json);

        Assert.assertEquals(expected, result);
    }

}
