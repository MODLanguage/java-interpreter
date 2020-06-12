package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ClassExpansionTest3 {

    public static final String EXPECTED = "{\"list\":[{\"description\":\"tel\",\"value\":\"fb\"},{\"description\":\"yt\",\"value\":\"tw\"}]}";

    public static final String INPUT = "*class(\n" +
            " *id=desc;\n" +
            " *name=description;\n" +
            " *superclass=str\n" +
            ");\n" +
            "\n" +
            "*class(\n" +
            " *id=val;\n" +
            " *name=value;\n" +
            " *superclass=str\n" +
            ");\n" +
            "\n" +
            "*class(\n" +
            " *id=media1;\n" +
            " *name=media1;\n" +
            " *assign=[\n" +
            "   [desc;val]\n" +
            " ]\n" +
            ");\n" +
            "\n" +
            "*class(\n" +
            " *id=media2;\n" +
            " *name=media2;\n" +
            " *assign=[\n" +
            "   [desc;val]\n" +
            " ]\n" +
            ");\n" +
            "*class(\n" +
            " *id=list;\n" +
            " *name=list;\n" +
            " *assign[\n" +
            "   [media1;media2]\n" +
            " ]\n" +
            "); list=[tel;fb]:[yt;tw]";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}