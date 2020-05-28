package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ClassesListTest {

    public static final String EXPECTED = "{\"classes\":[{\"m\":{\"name\":\"message\",\"superclass\":\"map\",\"assign\":[[\"direction\",\"date_time\",\"message\"]],\"method\":\"sms\"}},{\"a\":{\"name\":\"alpha\",\"superclass\":\"map\",\"v\":\"victor\"}},{\"b\":{\"name\":\"bravo\",\"superclass\":\"alpha\",\"w\":\"whisky\"}},{\"c\":{\"name\":\"charlie\",\"superclass\":\"bravo\",\"x\":\"xray\"}},{\"d\":{\"name\":\"delta\",\"superclass\":\"charlie\",\"y\":\"yankee\"}}]}";
    public static final String INPUT = "*L=\"https://www.modl.uk/tests/message-thread.txt\";*class(*id=a;*name=alpha;*superclass=map;v=victor);*class(*id=b;*name=bravo;*superclass=alpha;w=whisky);*class(*id=c;*name=charlie;*superclass=bravo;x=xray);*class(*id=d;*name=delta;*superclass=charlie;y=yankee);classes=%*class";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}