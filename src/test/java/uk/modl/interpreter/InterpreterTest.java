package uk.modl.interpreter;

import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.control.Either;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.model.Modl;
import uk.modl.transforms.JacksonTransformer;
import uk.modl.utils.Util;

public class InterpreterTest {

    public static final String EXPECTED = "[{\"*c\":{\"*i\":\"a\",\"*n\":\"alpha\",\"*s\":\"map\",\"v\":\"victor\"}},{\"*c\":{\"*i\":\"b\",\"*n\":\"bravo\",\"*s\":\"a\",\"w\":\"whisky\"}},{\"*c\":{\"*i\":\"c\",\"*n\":\"charlie\",\"*s\":\"b\",\"x\":\"xray\"}},{\"*c\":{\"*i\":\"d\",\"*n\":\"delta\",\"*s\":\"c\",\"y\":\"yankee\"}},{\"d\":{\"test1\":\"test2\"}},{\"e\":1.0},{\"f\":2.2},{\"g\":null},{\"h\":true},{\"j\":false}]";
    public static final String INPUT = "*c(*i=a;*n=alpha;*s=map;v=victor);*c(*i=b;*n=bravo;*s=a;w=whisky);*c(*i=c;*n=charlie;*s=b;x=xray);*c(*i=d;*n=delta;*s=c;y=yankee);d(test1=test2);e=1;f=2.2;g=000;h=01;j=00";
    private static Interpreter interpreter = new Interpreter();
    private static JacksonTransformer jsonTransformer = new JacksonTransformer();

    @Test
    public void parseOk() {
        final Modl modl = interpreter.apply(INPUT);
        Assert.assertNotNull(modl);
        final Either<Exception, JsonNode> jsonResult = jsonTransformer.apply(modl);

        final Either<Exception, String> mapResult = jsonResult.map(Util.jsonNodeToString);
        Assert.assertTrue(mapResult.isRight());
        System.out.println(mapResult.get());
        Assert.assertEquals(EXPECTED, mapResult.get());
    }

    @Test(expected = RuntimeException.class)
    public void parseBad() {
        interpreter.apply("xxx");
    }
}