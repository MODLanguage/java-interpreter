package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

public class ClassExpansionTest {

    public static final String EXPECTED = "{\"glossary\":{\"title\":\"example glossary\",\"GlossDiv\":{\"title\":\"S\",\"GlossList\":{\"GlossEntry\":{\"ID\":\"SGML\",\"SortAs\":\"SGML\",\"GlossTerm\":\"Standard Generalized Markup Language\",\"Acronym\":\"SGML\",\"Abbrev\":\"ISO 8879:1986\",\"GlossDef\":{\"para\":\"A meta-markup language, used to create markup languages such as DocBook.\",\"SeeAlso\":[\"GML\",\"XML\"]},\"GlossSee\":\"markup\"}}}}}";

    public static final String INPUT = "*class(\n" +
            " *id=g;\n" +
            " *name=glossary;\n" +
            " *superclass=map\n" +
            ");\n" +
            "*class(\n" +
            " *id=t;\n" +
            " *name=title;\n" +
            " *superclass=str\n" +
            ");\n" +
            "*class(\n" +
            " *id=d;\n" +
            " *name=GlossDiv;\n" +
            " *superclass=map\n" +
            ");\n" +
            "*class(\n" +
            " *id=l;\n" +
            " *name=GlossList;\n" +
            " *superclass=map\n" +
            ");\n" +
            "*class(\n" +
            " *id=e;\n" +
            " *name=GlossEntry;\n" +
            " *superclass=map;\n" +
            " *assign[\n" +
            "   [i;s;gt;a;ab;gd;gs]\n" +
            " ]\n" +
            ");\n" +
            "*class(\n" +
            " *id=i;\n" +
            " *name=ID;\n" +
            " *superclass=str\n" +
            ");\n" +
            "*class(\n" +
            " *id=s;\n" +
            " *name=SortAs;\n" +
            " *superclass=str\n" +
            ");\n" +
            "*class(\n" +
            " *id=gt;\n" +
            " *name=GlossTerm;\n" +
            " *superclass=str\n" +
            ");\n" +
            "*class(\n" +
            " *id=a;\n" +
            " *name=Acronym;\n" +
            " *superclass=str\n" +
            ");\n" +
            "*class(\n" +
            " *id=ab;\n" +
            " *name=Abbrev;\n" +
            " *superclass=str\n" +
            ");\n" +
            "*class(\n" +
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
            "*class(\n" +
            " *id=gs;\n" +
            " *name=GlossSee;\n" +
            " *superclass=str\n" +
            ");\n" +
            "\n" +
            "g(\n" +
            " ?=[SGML;markup;language];\n" +
            " t=example glossary;\n" +
            " d(\n" +
            "   t=S;\n" +
            "   l(\n" +
            "     e(\n" +
            "       i=%0;\n" +
            "       s=%0;\n" +
            "       gt=Standard Generalized %1.s %2.s;\n" +
            "       a=%0;\n" +
            "       ab=ISO 8879\\:1986;\n" +
            "       gd=A meta-%1 %2, used to create %1 %2%s such as DocBook.:[GML;XML];\n" +
            "       gs=%1\n" +
            "     )\n" +
            "   )\n" +
            " )\n" +
            ")";

    @Test
    public void parseOk() {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}