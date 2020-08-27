/*
 * MIT License
 *
 * Copyright (c) 2020 NUM Technology Ltd
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package uk.modl.interpreter;

import org.junit.Test;
import uk.modl.utils.TestUtils;

import java.net.MalformedURLException;

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
    public void parseOk() throws MalformedURLException {
        TestUtils.runTest(INPUT, EXPECTED);
    }

}