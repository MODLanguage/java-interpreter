package uk.modl.interpreter;

import lombok.val;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

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
        val f = new Interpreter().andThen(new JacksonJsonNodeTransformer())
                .andThen(Util.jsonNodeToString);

        Assert.assertEquals(EXPECTED, f.apply(INPUT));

    }

}