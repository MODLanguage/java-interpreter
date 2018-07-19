/*
MIT License

Copyright (c) 2018 NUM Technology Ltd

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation
the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of
the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package uk.modl.parser;

import junit.framework.TestCase;
import org.junit.Test;
import uk.modl.config.ConfigInterpreter;
import uk.modl.parser.printers.JsonPrinter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ParserTest2 extends TestCase {
        final static List<Object[]> expected =  Arrays.asList(new Object[][]{
//                {"_bool=true\n" +
//                        "{\n" +
//                        "bool?\n" +
//                        "  test=1\n" +
//                        "}", "{\n" +
//                        "  \"test\":1\n" +
//                        "}"},
//                {"_test=1\\\\:2\n" +
//                        "\n" +
//                        "result={\n" +
//                        "  test=1\\\\:2?\n" +
//                        "      yes\n" +
//                        "  /?\n" +
//                        "     no\n" +
//                        "}", "{\n" +
//                        "    \"result\": \"yes”\n" +
//                        "}"},
                {"_test=1\\\\:2\n" +
                        "result=%test", "{\n" +
                        "    \"result\": \"1:2\"\n" +
                        "}\n"},
//                {"_test=\"http://www.tesco.com\"\n" +
//                        "\n" +
//                        "result={\n" +
//                        "  test=\"http://www.tesco.com\"?\n" +
//                        "      yes\n" +
//                        "  /?\n" +
//                        "     no\n" +
//                        "}", "{\n" +
//                        "    \"result\": \"yes”\n" +
//                        "}"},
                {"_test=abcdefg\n" +
                        "result={\n" +
                        "  {test!=a*}?\n" +
                        "    in\n" +
                        "  /?\n" +
                        "    out\n" +
                        "}", "{\n" +
                        "    \"result\": \"out\"\n" +
                        "}"},
                {"_co = gb\n" +
                        "test = {\n" +
                        "  co = gb?\n" +
                        "    UK\n" +
                        "  /?\n" +
                        "    Other\n" +
                        "}",
                        "{\"test\" : \"UK\" }"},
                {"?=0:1:2\n" +
                        "result={\n" +
                        "%1>1?\n" +
                        "  yes\n" +
                        "/?\n" +
                        "  no\n" +
                        "}", "{\n" +
                        "  \"result\":\"no\"\n" +
                        "}"},
                {"test=()", "{\n" +
                        "  \"test\":{}\n" +
                        "}"},
                {"test=[]", "{\n" +
                        "  \"test\":[]\n" +
                        "}"},
                {"test(\n" +
                        "  empty_array=[]\n" +
                        "  empty_map=()\n" +
                        ")\n", "{\n" +
                        "  \"test\": {\n" +
                        "    \"empty_array\":[],\n" +
                        "    \"empty_map\":{}\n" +
                        "  }\n" +
                        "}"},
                {"test(\n" +
                        "  map(\n" +
                        "    array[]\n" +
                        "  )\n" +
                        "  array[\n" +
                        "    map()\n" +
                        "    array[1;2;3]\n" +
                        "  ]\n" +
                        ")\n", "{\n" +
                        "  \"test\": {\n" +
                        "    \"map\": {\n" +
                        "      \"array\": []\n" +
                        "    },\n" +
                        "    \"array\": [{\n" +
                        "        \"map\": {}\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"array\": [1, 2, 3]\n" +
                        "      }\n" +
                        "    ]\n" +
                        "  }\n" +
                        "}"},
                {"_num1 = 2\n" +
                        "_num2 = 1000\n" +
                        "\n" +
                        "result={\n" +
                        "  num1>num2?\n" +
                        "    num1 is bigger\n" +
                        "  /?\n" +
                        "    num1 is not bigger\n" +
                        "}\n", "{\n" +
                        "    \"result\": \"num1 is not bigger\"\n" +
                        "}"},
                {"?=0:1:2\n" +
                        "zero=%0\n" +
                        "one=%1\n" +
                        "two=%2", "[\n" +
                        "    {\n" +
                        "        \"zero\": \"0\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"one\": \"1\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"two\": \"2\"\n" +
                        "    }\n" +
                        "]"},
                {"?=a:b:c\n" +
                        "zero=%0\n" +
                        "one=%1\n" +
                        "two=%2\n", "[\n" +
                        "    {\n" +
                        "        \"zero\": \"a\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"one\": \"b\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"two\": \"c\"\n" +
                        "    }\n" +
                        "]"},
                {"_num1 = 5\n" +
                        "_num2 = 2\n" +
                        "\n" +
                        "result={\n" +
                        "  num1>num2?\n" +
                        "    num1 is bigger\n" +
                        "  /?\n" +
                        "    num1 is not bigger\n" +
                        "}", "{\n" +
                        "    \"result\": \"num1 is bigger\"\n" +
                        "}"},
                {"*class(\n" +
                        "  *id=p\n" +
                        "  *name=person\n" +
                        "  *superclass=map\n" +
                        ")\n" +
                        "\n" +
                        "p(name=Elliott Brown;dob=16/11/1983)","{\n" +
                        "    \"person\": {\n" +
                        "        \"name\": \"Elliott Brown\",\n" +
                        "        \"dob\": \"16/11/1983\"\n" +
                        "    }\n" +
                        "}"},
                {"*class(\n" +
                        "  *id=p\n" +
                        "  *name=person\n" +
                        ")\n" +
                        "\n" +
                        "p(name=Elliott Brown;dob=16/11/1983)","{\n" +
                        "    \"person\": {\n" +
                        "        \"name\": \"Elliott Brown\",\n" +
                        "        \"dob\": \"16/11/1983\"\n" +
                        "    }\n" +
                        "}"},
                {"*class(\n" +
                        "  *id=p\n" +
                        "  *name=person\n" +
                        "  *assign=[\n" +
                        "     [name;dob]\n" +
                        "   ]\n" +
                        ")\n" +
                        "\n" +
                        "p=Elliott Brown:16/11/1983","{\n" +
                        "    \"person\": {\n" +
                        "        \"name\": \"Elliott Brown\",\n" +
                        "        \"dob\": \"16/11/1983\"\n" +
                        "    }\n" +
                        "}"},
                {"\"test\"=1","{\n" +
                        "  \"test\" : 1\n" +
                        "}"},
                {"?=\"A\":B:C\n" +
                        "first_letter=%0","{ \"first_letter\" : \"A\" }"},
                {"_test=123\n" +
                        "object(\n" +
                        "  print_test = %test.test\n" +
                        ")", "{\"object\":{\"print_test\":\"123.test\"}}"},
                {"test=100%",
                        "{\"test\":\"100%\"}"},
                {"*c(\n" +
                        "  *i=m\n" +
                        "  *n=message\n" +
                        "  *s=map\n" +
                        "  *a=[\n" +
                        "    [direction;date_time;message]\n" +
                        "  ]\n" +
                        "  method=sms\n" +
                        ")\n" +
                        "m=in:2018-03-22:hi",
                        "{\n" +
                                "  \"message\" : {\n" +
                                "    \"direction\" : \"in\",\n" +
                                "    \"date_time\" : \"2018-03-22\",\n" +
                                "    \"message\" : \"hi\",\n" +
                                "    \"method\" : \"sms\"\n" +
                                "  }\n" +
                                "}"},
                {"test=`test`",
                "{\n" +
                        "  \"test\" : \"test\"\n" +
                        "}"},
                {"test=\\u0021",
                "{\n" +
                        "  \"test\" : \"!\"\n" +
                        "}"},
                {"?=zero:one:two\n" +
                        "discount=%30",
                "{ \n" +
                        "  \"discount\": \"%30\"\n" +
                        "}"},
                {"?[zero;one;two]\n" +
                        "first_var=%0\n" +
                        "second_var=%1\n" +
                        "third_var=%2",
                "[ {\n" +
                        "  \"first_var\" : \"zero\"\n" +
                        "}, {\n" +
                        "  \"second_var\" : \"one\"\n" +
                        "}, {\n" +
                        "  \"third_var\" : \"two\"\n" +
                        "} ]"},
                {"?=[one;two]\n" +
                        "test=Blah `%0.r(o,huzzah)` `%1.t(w)`",
                        "{\"test\":\"Blah huzzahne t\"}"},
                {"?=[one;two]\n" +
                        "test=Blah `%0.s` %1.s",
                        "{\"test\":\"Blah One Two\"}"},
                {"*class(\n" +
                        "  *id=g\n" +
                        "  *name=glossary\n" +
                        "  *superclass=map\n" +
                        ")\n" +
                        "*class(\n" +
                        "  *id=t\n" +
                        "  *name=title\n" +
                        "  *superclass=str\n" +
                        ")\n" +
                        "*class(\n" +
                        "  *id=d\n" +
                        "  *name=GlossDiv\n" +
                        "  *superclass=map\n" +
                        ")\n" +
                        "*class(\n" +
                        "  *id=l\n" +
                        "  *name=GlossList\n" +
                        "  *superclass=map\n" +
                        ")\n" +
                        "*class(\n" +
                        "  *id=e\n" +
                        "  *name=GlossEntry\n" +
                        "  *superclass=map\n" +
                        "  *assign[\n" +
                        "    [i;s;gt;a;ab;gd;gs]\n" +
                        "  ]\n" +
                        ")\n" +
                        "*class(\n" +
                        "  *id=i\n" +
                        "  *name=ID\n" +
                        "  *superclass=str\n" +
                        ")\n" +
                        "*class(\n" +
                        "  *id=s\n" +
                        "  *name=SortAs\n" +
                        "  *superclass=str\n" +
                        ")\n" +
                        "*class(\n" +
                        "  *id=gt\n" +
                        "  *name=GlossTerm\n" +
                        "  *superclass=str\n" +
                        ")\n" +
                        "*class(\n" +
                        "  *id=a\n" +
                        "  *name=Acronym\n" +
                        "  *superclass=str\n" +
                        ")\n" +
                        "*class(\n" +
                        "  *id=ab\n" +
                        "  *name=Abbrev\n" +
                        "  *superclass=str\n" +
                        ")\n" +
                        "*class(\n" +
                        "  *id=gd\n" +
                        "  *name=GlossDef\n" +
                        "  *superclass=map\n" +
                        "  *assign=[\n" +
                        "    [p]\n" +
                        "    [p;sa]\n" +
                        "  ]\n" +
                        ")\n" +
                        "*class(\n" +
                        "  *id=p\n" +
                        "  *name=para\n" +
                        "  *superclass=str\n" +
                        ")\n" +
                        "*class(\n" +
                        "  *id=sa\n" +
                        "  *name=SeeAlso\n" +
                        "  *superclass=arr\n" +
                        ")\n" +
                        "*class(\n" +
                        "  *id=gs\n" +
                        "  *name=GlossSee\n" +
                        "  *superclass=str\n" +
                        ")\n" +
                        "\n" +
                        "g(\n" +
                        "  ?=[SGML;markup;language]\n" +
                        "  t=example glossary\n" +
                        "  d(\n" +
                        "    t=S\n" +
                        "    l(\n" +
                        "      e(\n" +
                        "        i=%0\n" +
                        "        s=%0\n" +
                        "        gt=Standard Generalized %1.s %2.s\n" +
                        "        a=%0\n" +
                        "        ab=ISO 8879\\:1986\n" +
                        "        gd=A meta-%1 %2, used to create %1 %2s such as DocBook.\n" +
                        "          :[GML;XML]\n" +
                        "        gs=%1\n" +
                        "      )\n" +
                        "    )\n" +
                        "  )\n" +
                        ")\n",
                        "{\n" +
                                "  \"glossary\":{\n" +
                                "    \"title\":\"example glossary\",\n" +
                                "    \"GlossDiv\":{\n" +
                                "      \"title\":\"S\",\n" +
                                "      \"GlossList\":{\n" +
                                "        \"GlossEntry\":{\n" +
                                "          \"ID\":\"SGML\",\n" +
                                "          \"SortAs\":\"SGML\",\n" +
                                "          \"GlossTerm\":\"Standard Generalized Markup Language\",\n" +
                                "          \"Acronym\":\"SGML\",\n" +
                                "          \"Abbrev\":\"ISO 8879:1986\",\n" +
                                "          \"GlossDef\":{\n" +
                                "            \"para\":\"A meta-markup language, used to create markup languages such as DocBook.\",\n" +
                                "            \"SeeAlso\":[\"GML\",\"XML\"]\n" +
                                "          },\n" +
                                "          \"GlossSee\":\"markup\"\n" +
                                "        }\n" +
                                "      }\n" +
                                "    }\n" +
                                "  }\n" +
                                "}"
                },
                {"*class(\n" +
                        "  *id=desc\n" +
                        "  *name=description\n" +
                        "  *superclass=str\n" +
                        ")\n" +
                        "\n" +
                        "*class(\n" +
                        "  *id=val\n" +
                        "  *name=value\n" +
                        "  *superclass=str\n" +
                        ")\n" +
                        "\n" +
                        "*class(\n" +
                        "  *id=media1\n" +
                        "  *name=media1\n" +
                        "  *superclass=map\n" +
                        "  *assign=[\n" +
                        "    [desc;val]\n" +
                        "  ]\n" +
                        ")\n" +
                        "\n" +
                        "*class(\n" +
                        "  *id=media2\n" +
                        "  *name=media2\n" +
                        "  *superclass=map\n" +
                        "  *assign=[\n" +
                        "    [desc;val]\n" +
                        "  ]\n" +
                        ")\n" +
                        "*class(\n" +
                        "  *id=list\n" +
                        "  *name=list\n" +
                        "  *superclass=map\n" +
                        "  *assign[\n" +
                        "    [media1;media2]\n" +
                        "  ]\n" +
                        ")\n" +
                        "\n" +
                        "\n" +
                        "list=(tel:fb):(yt:tw)",
                        "{\n" +
                                "  \"list\" : {\n" +
                                "    \"media1\" : {\n" +
                                "      \"description\" : \"tel\",\n" +
                                "      \"value\" : \"fb\"\n" +
                                "    },\n" +
                                "    \"media2\" : {\n" +
                                "      \"description\" : \"yt\",\n" +
                                "      \"value\" : \"tw\"\n" +
                                "    }\n" +
                                "  }\n" +
                                "}"},
                {"test=(zero:one):(a:b)",
                "{\n" +
                        "  \"test\" : [ \n" +
                        "    [\"zero\", \"one\" ],\n" +
                        "    [\"a\", \"b\" ]\n" +
                        "  ]\n" +
                        "}"},
                {"test=(zero:one)",
                        "{\n" +
                                "  \"test\" : [ \"zero\", \"one\" ]\n" +
                                "}"},
                {"*class(\n" +
                        "  *id=car\n" +
                        "  *name=car\n" +
                        "  *superclass=map\n" +
                        "  *assign=[\n" +
                        "    [m]\n" +
                        "    [m;md]\n" +
                        "  ]\n" +
                        ")\n" +
                        "\n" +
                        "_C=gb\n" +
                        "\n" +
                        "car=Bentley:{C=ru?Continentalski GT/?Continental GT}",
                "{\n" +
                        "  \"car\" : {\n" +
                        "    \"m\" : \"Bentley\",\n" +
                        "    \"md\" : \"Continental GT\"\n" +
                        "  }\n" +
                        "}"},
                {"*class(\n" +
                        "  *id=car\n" +
                        "  *name=car\n" +
                        "  *superclass=map\n" +
                        ")\n" +
                        "\n" +
                        "car(\n" +
                        "  make=Bentley\n" +
                        ")",
                "{\n" +
                        "  \"car\" : {\n" +
                        "    \"make\" : \"Bentley\"\n" +
                        "  }\n" +
                        "}"},
                {/* nested conditionals */
                        "_co=at\n" +
                                " _l=de\n" +
                                "{\n" +
                                "  co=at?\n" +
                                "    country=Austria\n" +
                                "    language={\n" +
                                "      l=fr?\n" +
                                "        French\n" +
                                "      /l=de?\n" +
                                "        German\n" +
                                "      /?\n" +
                                "        Other\n" +
                                "    }\n" +
                                "  /?\n" +
                                "    country=Other\n" +
                                "}",
                        "[ {\n" +
                                "  \"country\" : \"Austria\"\n" +
                                "}, {\n" +
                                "  \"language\" : \"German\"\n" +
                                "} ]"},
                {"_C=fr\n" +
                        "{C=gb?test1=123}\n" +
                        "test2=456",
                        "{\n" +
                                "  \"test2\" : 456\n" +
                                "}"},
                {"(_C=gb\n" +
                        "{C=gb?test1=123}\n" +
                        "test2=456)",
                        "{\n" +
                                "  \"test1\" : 123,\n" +
                                "  \"test2\" : 456\n" +
                                "}"},
                {"_C=ca\n" +
                        "_L=en\n" +
                        "{\n" +
                        "  C=ca?\n" +
                        "   n=Tesco Canada\n" +
                        "   {L=fr?\n" +
                        "     s=Chaque Petite Contribution\n" +
                        "   }\n" +
                        "}",
                        "{\n" +
                                "  \"n\" : \"Tesco Canada\"\n" +
                                "}"},
                {"_L=en\n" +
                        "{\n" +
                        "  C=ca?\n" +
                        "     o(\n" +
                        "       n=Tesco Canada\n" +
                        "       s={L=fr?\n" +
                        "         Chaque Petite Contribution\n" +
                        "       /?\n" +
                        "         Every Little Helps\n" +
                        "       }\n" +
                        "     )\n" +
                        "}",""},
                {"_C=gb\n" +
                        "o(\n" +
                        " {C=gb?test1=123}\n" +
                        " test2=456\n" +
                        ")",
                        "{\n" +
                                "  \"o\" : {\n" +
                                "    \"test1\" : 123,\n" +
                                "    \"test2\" : 456\n" +
                                "  }\n" +
                                "}"},
                {"_letter=a\n" +
                        "{\n" +
                        "  letter=a?\n" +
                        "    word=Apple\n" +
                        "  /letter=b?\n" +
                        "    word=Bee\n" +
                        "  /?\n" +
                        "    word=Other\n" +
                        "}",
                        "{\n" +
                                "  \"word\" : \"Apple\"\n" +
                                "}"},
                {"_int=1\n" +
                        "{\n" +
                        "  int=1?\n" +
                        "    number=one\n" +
                        "  /int=2?\n" +
                        "    number=two\n" +
                        "  /int=3?\n" +
                        "    number=three\n" +
                        "  /?\n" +
                        "    number=other\n" +
                        "}",
                        "{\n" +
                                "  \"number\" : \"one\"\n" +
                                "}"},
                {"_number=one\n" +
                        "{\n" +
                        "  number=one?\n" +
                        "    int=1\n" +
                        "  /number=two?\n" +
                        "    int=2\n" +
                        "  /number=three?\n" +
                        "    int=3\n" +
                        "}",
                        "{\n" +
                                "  \"int\" : 1\n" +
                                "}"},
                {"_co=gb\n" +
                        "{\n" +
                        "co=gb?\n" +
                        "  country = United Kingdom\n" +
                        "/?\n" +
                        "  country = Other\n" +
                        "}",
                        "{\n" +
                                "  \"country\" : \"United Kingdom\"\n" +
                                "}"},
                {"_co=gb\n" +
                        "{\n" +
                        "co=gb?\n" +
                        "  country = United Kingdom\n" +
                        "/?\n" +
                        "  country = Other\n" +
                        "}",
                        "{\n" +
                                "  \"country\" : \"United Kingdom\"\n" +
                                "}"},
                {"_co = gb\n" +
                        "test = {\n" +
                        "  co = gb?\n" +
                        "    test=123\n" +
                        "  /?\n" +
                        "    test\n" +
                        "}",
                        "{\"test\": { \"test\" : 123 }}"},
                {"_co = fr\n" +
                        "test = {\n" +
                        "  co = gb?\n" +
                        "    test=123\n" +
                        "  /?\n" +
                        "    test\n" +
                        "}",
                        "{\"test\": \"test\"}"},
                {"_COUNTRY=gb\n" +
                        "country=The country is %COUNTRY",
                        "{\n" +
                                "  \"country\" : \"The country is gb\"\n" +
                                "}"},
                {"COUNTRY=gb\n" +
                        "country=The country is %COUNTRY",
                        "[ {\n" +
                                "  \"COUNTRY\" : \"gb\"\n" +
                                "}, {\n" +
                                "  \"country\" : \"The country is gb\"\n" +
                                "} ]"},
                {"_co=gb\n" +
                        "country=The country is %co",
                        "{\n" +
                                "  \"country\" : \"The country is gb\"\n" +
                                "}"},
                {"_test = 123\n" +
                        "_test2 = abc",
                        ""},
                {"_co=gb\n" +
                        "test=123",
                        "{ \"test\" : 123 }"},
                {"true2 = 01\n" +
                        "true1 = true\n" +
                        "false2 = 00\n" +
                        "false1 = false\n" +
                        "null2 = 000\n" +
                        "null1 = null",
                        "[\n" +
                                "  { \"true2\" : true },\n" +
                                "  { \"true1\" : true },\n" +
                                "  { \"false2\" : false },\n" +
                                "  { \"false1\" : false },\n" +
                                "  { \"null2\" : null },\n" +
                                "  { \"null1\" : null }\n" +
                                "]"},
                {"?=[zero;one;two]\n" +
                        "zero=%0\n" +
                        "one=%1\n" +
                        "two=%2",
                        "[\n" +
                                "  {\n" +
                                "    \"zero\" : \"zero\"\n" +
                                "  },\n" +
                                "  {\n" +
                                "    \"one\" : \"one\"\n" +
                                "  },\n" +
                                "  {\n" +
                                "    \"two\" : \"two\"\n" +
                                "  }\n" +
                                "]"},
                {/* From Trello variable methods card */
                        "*vm(\n" +
                                "  testing = quick-test of Elliott's variable_methods\n" +
                                ")\n" +
                                "upcase_example = %testing.u\n" +
                                "downcase_example = %testing.d\n" +
                                "initcap_example = %testing.i\n" +
                                "sentence_example = %testing.s\n" +
                                "url_encode_example = %testing.ue",
                        "[\n" +
                                "  {\n" +
                                "    \"upcase_example\" : \"QUICK-TEST OF ELLIOTT'S VARIABLE_METHODS\"\n" +
                                "  },\n" +
                                "  {\n" +
                                "    \"downcase_example\" : \"quick-test of elliott's variable_methods\"\n" +
                                "  },\n" +
                                "  {\n" +
                                "    \"initcap_example\" : \"Quick-test Of Elliott's Variable_methods\"\n" +
                                "  },\n" +
                                "  {\n" +
                                "    \"sentence_example\" :  \"Quick-test of Elliott's variable_methods\"\n" +
                                "  },\n" +
                                "  {\n" +
                                "    \"url_encode_example\" : \"quick-test+of+Elliott%27s+variable_methods\"\n" +
                                "  }\n" +
                                "]"}
        });

        @Test
        public void testParser() throws IOException {
                // Take a load of test MODL strings and make sure uk.modl.parser generates correct output

                for (Object test : expected) {
                        String input = (String)((Object[])test)[0];
                        String expected = (String)((Object[])test)[1];

                        System.out.println("Input : " + input);
                        System.out.println("Expected : " + expected);

                        ModlObject modlObject = ConfigInterpreter.interpret(input);
                        String output = JsonPrinter.printModl(modlObject);
                        System.out.println("Output : " + output);
                        assertEquals(expected.replace(" ", "").replace("\n", ""),
                                output.replace(" ", "").replace("\n", ""));
                }

        }

        // TODO Run some test inputs which should generate errors (e.g. re-defining uppercase OBJECTNAME)
}