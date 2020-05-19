package uk.modl.interpreter;

import lombok.val;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

public class ClassExpansionTest2 {

    public static final String EXPECTED = "{\"GlossDef\":{\"para\":\"A meta-markup language, used to create markup languages such as DocBook.\",\"SeeAlso\":[\"GML\",\"XML\"]}}";

    public static final String INPUT = "*class(\n" +
            " *id=gd;\n" +
            " *name=GlossDef;\n" +
            " *assign=[\n" +
            "   [p];\n" +
            "   [p;sa]\n" +
            " ]\n" +
            ");\n" +
            "*class(\n" +
            " *id=p;\n" +
            " *name=para;\n" +
            " *superclass=str\n" +
            ");\n" +
            "*class(\n" +
            " *id=sa;\n" +
            " *name=SeeAlso;\n" +
            " *superclass=arr\n" +
            ");\n" +
            " ?=[SGML;markup;language];\n" +
            "gd=A meta-%1 %2, used to create %1 %2%s such as DocBook.:[GML;XML];\n";

    @Test
    public void parseOk() {
        val f = new Interpreter().andThen(new JacksonJsonNodeTransformer())
                .andThen(Util.jsonNodeToString);

        Assert.assertEquals(EXPECTED, f.apply(INPUT));
    }

}