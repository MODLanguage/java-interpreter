package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class NestedStarLoadTest1 {

    public static final String EXPECTED1 = "{\"the_number\":3}";
    public static final String EXPECTED2 = "{\"the_number\":1}";
    public static final String INPUT1 = "*l=../grammar/tests/1:../grammar/tests/2:../grammar/tests/3;the_number=%number";
    public static final String INPUT2 = "*l=../grammar/tests/1:../grammar/tests/2:../grammar/tests/3:../grammar/tests/1;the_number=%number";

    @Test
    public void test1() {
        TestUtils.runTest(INPUT1, EXPECTED1);
    }

    @Test
    public void test2() {
        TestUtils.runTest(INPUT2, EXPECTED2);
    }
}