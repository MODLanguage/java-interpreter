package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

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
        TestUtils.runTest(INPUT, EXPECTED);
    }

}