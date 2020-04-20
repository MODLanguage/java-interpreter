package uk.modl.interpreter;

import lombok.val;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

public class InterpreterTest {

    public static final String EXPECTED = "{\"delta\":{\"test1\":\"test2\",\"y\":\"yankee\",\"x\":\"xray\",\"w\":\"whisky\",\"v\":\"victor\"},\"e\":1,\"f\":2.2,\"g\":null,\"h\":true,\"j\":false}";
    public static final String INPUT = "*c(*i=a;*n=alpha;*s=map;v=victor);*c(*i=b;*n=bravo;*s=a;w=whisky);*c(*i=c;*n=charlie;*s=b;x=xray);*c(*i=d;*n=delta;*s=c;y=yankee);d(test1=test2);e=1;f=2.2;g=000;h=01;j=00";

    @Test
    public void parseOk() {
        val f = new Interpreter().andThen(new JacksonJsonNodeTransformer())
                .andThen(Util.jsonNodeToString);

        Assert.assertEquals(EXPECTED, f.apply(INPUT));
    }

    @Test(expected = RuntimeException.class)
    public void parseBad() {
        new Interpreter().apply("xxx");
    }
}