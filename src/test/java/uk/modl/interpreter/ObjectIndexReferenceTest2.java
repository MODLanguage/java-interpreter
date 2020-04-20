package uk.modl.interpreter;

import lombok.val;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

public class ObjectIndexReferenceTest2 {

    public static final String EXPECTED = "{\"test1\":\"one\",\"test2\":\"two\",\"test3\":\"three\",\"test4\":\"%3\",\"test5\":\"%4\",\"test6\":\"%5\",\"test7\":\"%6\",\"test8\":\"%7\",\"test9\":\"%8\"}";
    public static final String INPUT = "?=one:two:three;test1=%0;test2=%1;test3=%2;test4=%3;test5=%4;test6=%5;test7=%6;test8=%7;test9=%8";

    @Test
    public void parseOk() {
        val f = new Interpreter().andThen(new JacksonJsonNodeTransformer())
                .andThen(Util.jsonNodeToString);

        Assert.assertEquals(EXPECTED, f.apply(INPUT));
    }

}