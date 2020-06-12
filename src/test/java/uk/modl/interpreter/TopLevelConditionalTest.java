package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class TopLevelConditionalTest {

    public static final String EXPECTED_1 = "{\"result\":\"match\"}";

    public static final String EXPECTED_2 = "{\"result\":\"nomatch\"}";

    public static final String EXPECTED_3 = "{\"result\":\"match\"}";

    public static final String EXPECTED_4 = "{\"result\":\"nomatch\"}";

    public static final String INPUT_1 = "_test1=one;_one=two;{test1=`one`?result=match/?result=nomatch}";

    public static final String INPUT_2 = "_test1=one;_one=two;{test1!=`one`?result=match/?result=nomatch}";

    public static final String INPUT_3 = "_test1=one;_one=two;{test1=\"one\"?result=match/?result=nomatch}";

    public static final String INPUT_4 = "_test1=one;_one=two;{test1=two?result=match/?result=nomatch}";

    @Test
    public void success_1() {
        TestUtils.runTest(INPUT_1, EXPECTED_1);
    }

    @Test
    public void success_2() {
        TestUtils.runTest(INPUT_2, EXPECTED_2);
    }

    @Test
    public void success_3() {
        TestUtils.runTest(INPUT_3, EXPECTED_3);
    }

    @Test
    public void success_4() {
        TestUtils.runTest(INPUT_4, EXPECTED_4);
    }

}