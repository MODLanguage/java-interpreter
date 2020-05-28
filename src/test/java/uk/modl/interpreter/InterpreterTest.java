package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.transforms.TransformationContext;
import uk.modl.utils.TestUtils;

public class InterpreterTest {

    public static final String EXPECTED = "{\"delta\":{\"test1\":\"test2\",\"y\":\"yankee\",\"x\":\"xray\",\"w\":\"whisky\",\"v\":\"victor\"},\"e\":1,\"f\":2.2,\"g\":null,\"h\":true,\"j\":false}";
    public static final String INPUT = "*c(*i=a;*n=alpha;*s=map;v=victor);*c(*i=b;*n=bravo;*s=a;w=whisky);*c(*i=c;*n=charlie;*s=b;x=xray);*c(*i=d;*n=delta;*s=c;y=yankee);d(test1=test2);e=1;f=2.2;g=000;h=01;j=00";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

    @Test(expected = RuntimeException.class)
    public void parseBad() {
        new Interpreter().apply(TransformationContext.emptyCtx(), "xxx");
    }
}