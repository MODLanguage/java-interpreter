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
import uk.modl.interpreter.Interpreter;
import uk.modl.modlObject.ModlObject;
import uk.modl.parser.printers.JsonPrinter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ParserTest2 extends TestCase {
        final public static List<Object[]> expected =  Arrays.asList(new Object[][]{
//                {"_test[\n" +
//                        "  numbers[\n" +
//                        "    1;2;3;4;5\n" +
//                        "]\n" +
//                        "  letters[\n" +
//                        "    a;b;c;d\n" +
//                        "  ]\n" +
//                        "]\n" +
//                        "\n" +
//                        "a=%test[0[0]]",
//                        "{\"a\":1}"},
//                {"_test[\n" +
//                        "  [\n" +
//                        "    1;2;3;4;5\n" +
//                        "]\n" +
//                        "  [\n" +
//                        "    a;b;c;d\n" +
//                        "  ]\n" +
//                        "]\n" +
//                        "\n" +
//                        "a=%test[0[0]]",
//                        "{\"a\":1}"},
                {/* From Trello variable methods card */
                        "  _testing = quick-test of John's variable_methods\n" +
                                "upcase_example = %testing.u\n" +
                                "downcase_example = %testing.d\n" +
                                "initcap_example = %testing.i\n" +
                                "sentence_example = %testing.s\n" +
                                "url_encode_example = %testing.ue",
                        "[\n" +
                                "  {\n" +
                                "    \"upcase_example\" : \"QUICK-TEST OF JOHN'S VARIABLE_METHODS\"\n" +
                                "  },\n" +
                                "  {\n" +
                                "    \"downcase_example\" : \"quick-test of john's variable_methods\"\n" +
                                "  },\n" +
                                "  {\n" +
                                "    \"initcap_example\" : \"Quick-test Of John's Variable_methods\"\n" +
                                "  },\n" +
                                "  {\n" +
                                "    \"sentence_example\" :  \"Quick-test of John's variable_methods\"\n" +
                                "  },\n" +
                                "  {\n" +
                                "    \"url_encode_example\" : \"quick-test+of+John%27s+variable_methods\"\n" +
                                "  }\n" +
                                "]",
                        "_testing=quick-test of John's variable_methods;upcase_example=%testing.u;downcase_example=%testing.d;initcap_example=%testing.i;sentence_example=%testing.s;url_encode_example=%testing.ue"},
                {"*VERSION=1\n" +
                        "\"test\"=1","{\n" +
                        "  \"test\" : 1\n" +
                        "}",
                "*VERSION=1;test=1"},
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
                                "}",
                        "*c(*i=g;*n=glossary;*s=map);*c(*i=t;*n=title;*s=str);*c(*i=d;*n=GlossDiv;*s=map);*c(*i=l;*n=GlossList;*s=map);*c(*i=e;*n=GlossEntry;*s=map;*a[[i;s;gt;a;ab;gd;gs]]);*c(*i=i;*n=ID;*s=str);*c(*i=s;*n=SortAs;*s=str);*c(*i=gt;*n=GlossTerm;*s=str);*c(*i=a;*n=Acronym;*s=str);*c(*i=ab;*n=Abbrev;*s=str);*c(*i=gd;*n=GlossDef;*s=map;*a=[p]:[p;sa]);*c(*i=p;*n=para;*s=str);*c(*i=sa;*n=SeeAlso;*s=arr);*c(*i=gs;*n=GlossSee;*s=str);g(?=SGML:markup:language;t=example glossary;d(t=S;l(e(i=%0;s=%0;gt=Standard Generalized %1.s %2.s;a=%0;ab=ISO 8879~:1986;gd=A meta-%1 %2, used to create %1 %2s such as DocBook.:[GML;XML];gs=%1))))"
                },



                {"_test=1~:2\n" +
                        "\n" +
                        "result={\n" +
                        "  test=1~:2?\n" +
                        "      yes\n" +
                        "  /?\n" +
                        "     no\n" +
                        "}", "{ \"result\": \"yes\" }",
                        "_test=1~:2;result={test=1~:2?yes/?no}"},
                {"_test=\"http://www.tesco.com\"\n" +
                        "\n" +
                        "result={\n" +
                        "  test=\"http://www.tesco.com\"?\n" +
                        "      yes\n" +
                        "  /?\n" +
                        "     no\n" +
                        "}", "{\n" +
                        "  \"result\" : \"yes\"\n" +
                        "}",
                "_test=http~://www.tesco.com;result={test=\"http://www.tesco.com\"?yes/?no}"},
                {"_test=\"http://www.tesco.com\"\n" +
                        "\n" +
                        "result={\n" +
                        "  test=\"http://www.tesco.com\"?\n" +
                        "      yes\n" +
                        "  /?\n" +
                        "     no\n" +
                        "}", "{\n" +
                        "  \"result\" : \"yes\"\n" +
                        "}",
                "_test=http~://www.tesco.com;result={test=\"http://www.tesco.com\"?yes/?no}"},
                {"_branch=\"alex.\";_root=d;namespace=`%branch`blah.`%root`",
                        "{\"namespace\":\"alex.blah.d\"}",
                        "_branch=alex.;_root=d;namespace=`%branch`blah.`%root`"
                },

                {"namespace=`%branch`blah.`%root`",
                        "{\"namespace\":\"%branchblah.%root\"}"
                },
                {"_root=tesco.com\n" +
                        "_branch=direct.\n" +
                        "namespace1=`%branch`numrecord.`%root`\n" +
                        "namespace2=`%branch`_`%root`.numq.net",
                        "[ {\n" +
                                "  \"namespace1\" : \"direct.numrecord.tesco.com\"\n" +
                                "}, {\n" +
                                "  \"namespace2\" : \"direct._tesco.com.numq.net\"\n" +
                                "} ]\n",
                        "_root=tesco.com;_branch=direct.;namespace1=`%branch`numrecord.`%root`;namespace2=`%branch`_`%root`.numq.net"},
                {"_branch=\"\"\n" +
                        "_root=\"\"\n" +
                        "namespace=`%branch`numrecord.`%root`",
                        "{\n" +
                                "  \"namespace\" : \"numrecord.\"\n" +
                                "}",
                        "_branch=\"\";_root=\"\";namespace=`%branch`numrecord.`%root`"},
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
                        "list=[tel;fb]:[yt;tw]",
//                        "list=[[tel;fb];[yt;tw]]",
//                        "list=(tel:fb):(yt:tw)",
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
                                "}",
                        "*c(*i=desc;*n=description;*s=str);*c(*i=val;*n=value;*s=str);*c(*i=media1;*n=media1;*s=map;*a[[desc;val]]);*c(*i=media2;*n=media2;*s=map;*a[[desc;val]]);*c(*i=list;*n=list;*s=map;*a[[media1;media2]]);list=[tel;fb]:[yt;tw]"},
                {"*class(\n" +
                        "  *id=p\n" +
                        "  *name=person\n" +
                        "  *superclass=map\n" +
                        ")\n" +
                        "\n" +
                        "p(name=John Smith;dob=01/01/2000)","{\n" +
                        "    \"person\": {\n" +
                        "        \"name\": \"John Smith\",\n" +
                        "        \"dob\": \"01/01/2000\"\n" +
                        "    }\n" +
                        "}",
                        "*c(*i=p;*n=person;*s=map);p(name=John Smith;dob=01/01/2000)"},
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
                                "}",
                        "*c(*i=m;*n=message;*s=map;*a[[direction;date_time;message]];method=sms);m=in:2018-03-22:hi"},
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
                                "}",
                        "*c(*i=car;*n=car;*s=map);car(make=Bentley)"},
                {"*class(\n" +
                        "  *id=p\n" +
                        "  *name=person\n" +
                        ")\n" +
                        "\n" +
                        "p(name=John Smith;dob=01/01/2001)","{\n" +
                        "    \"person\": {\n" +
                        "        \"name\": \"John Smith\",\n" +
                        "        \"dob\": \"01/01/2001\"\n" +
                        "    }\n" +
                        "}",
                        "*c(*i=p;*n=person);p(name=John Smith;dob=01/01/2001)"},
                {"?=[one;two]\n" +
                        "test=Blah `%0.s` %1.s",
                        "{\"test\":\"Blah One Two\"}",
                        "?=one:two;test=Blah `%0.s` %1.s"},
                {"?=one:two\n" +
                        "test=Blah `%0.r(o,huzzah)` `%1.t(w)`",
                        "{\"test\":\"Blah huzzahne t\"}",
                        "?=one:two;test=Blah `%0.r(o,huzzah)` `%1.t(w)`"},
                {"_test=\"123\"\n" +
                        "object(\n" +
                        "  print_test = %test.test\n" +
                        ")", "{\"object\":{\"print_test\":\"123.test\"}}",
                        "_test=\"123\";object(print_test=%test.test)"},
                {"_var = NotThisOne;_var=`%var`blah;out=`%var`deblah",
                        "{\n" +
                                "  \"out\" : \"NotThisOneblahdeblah\"\n" +
                                "}",
                        "_var=NotThisOne;_var=`%var`blah;out=`%var`deblah"},
                {"_var = NotThisOne;_var=blah;out=`%var`deblah",
                        "{\n" +
                                "  \"out\" : \"blahdeblah\"\n" +
                                "}",
                        "_var=NotThisOne;_var=blah;out=`%var`deblah"},
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
                        "}",
                        "test(map(array[]);array[map();array=1:2:3])"},
                {"{\n" +
                        "01?\n" +
                        "  test=1\n" +
                        "}", "{\n" +
                        "  \"test\":1\n" +
                        "}",
                        "{01?test=1}"},
                {"test=()", "{\n" +
                        "  \"test\":{}\n" +
                        "}",
                        "test()"},
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
                                "} ]",
                        "_co=at;_l=de;{co=at?country=Austria;language={l=fr?French/l=de?German/?Other}/?country=Other}"},
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
                                "]",
                        "true2=01;true1=01;false2=00;false1=00;null2=000;null1=000"},
                {"?=[a;b;c;d]:[1;2;3;4;5]\n" +
                        "test=%1[0]",
                        "{\n" +
                                "    \"test\": 1\n" +
                                "}",
                "?=[a;b;c;d]:[1;2;3;4;5];test=%1[0]"},
                {"_person(  \n" +
                        "  name(\n" +
                        "    first=John\n" +
                        "    last=Smith\n" +
                        "  )\n" +
                        ")\n" +
                        "say=%person[name[first]]",
                        "{\n" +
                                "    \"say\": \"John\"\n" +
                                "}",
                "_person(name(first=John;last=Smith));say=%person[name[first]]"},
                {"_C=gb\n" +
                        "_COUNTRIES(\n" +
                        "  us=United States\n" +
                        "  gb=United Kingdom\n" +
                        "  de=Germany\n" +
                        ")\n" +
                        "\n" +
                        "country_name = %COUNTRIES[%C]", "{\n" +
                        "  \"country_name\" : \"United Kingdom\"\n" +
                        "}",
                "_C=gb;_COUNTRIES(us=United States;gb=United Kingdom;de=Germany);country_name=%COUNTRIES[%C]"},
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
                        "car=Bentley:{C=ru?ContinentalRussia GT/?Continental GT}",
                        "{\n" +
                                "  \"car\" : {\n" +
                                "    \"m\" : \"Bentley\",\n" +
                                "    \"md\" : \"Continental GT\"\n" +
                                "  }\n" +
                                "}",
                "*c(*i=car;*n=car;*s=map;*a=[m]:[m;md]);_C=gb;car=Bentley:{C=ru?ContinentalRussia GT/?Continental GT}"},
//                {"_person(  \n" +
//                        "  name(\n" +
//                        "    first=John\n" +
//                        "    last=Smith\n" +
//                        "  )\n" +
//                        ")\n" +
//                        "say=Hi %person[name[first]]",
//                "{\n" +
//                        "    \"say\": \"Hi John\"\n" +
//                        "}"},
//                {"_person(  \n" +
//                        "  name(\n" +
//                        "    first=John\n" +
//                        "    last=Smith\n" +
//                        "  )\n" +
//                        ")\n" +
//                        "say=\"Hi, my name is %person[name[first]] %person[name[last]]\"",
//                "{\n" +
//                        "  \"say\" : \"Hi, my name is John Smith\"\n" +
//                        "}"},
                {"_person(  \n" +
                        "  name(\n" +
                        "    first=\"John\"\n" +
                        "  )\n" +
                        ")\n" +
                        "a=%person[name[first]]",
                        "{\"a\":\"John\"}",
                "_person(name(first=John));a=%person[name[first]]"},

                {"?=[a;b;c;d]:[1;2;3;4;5]\n" +
                        "test=%1",
                "{\n" +
                        "    \"test\": [1,2,3,4,5]\n" +
                        "}",
                "?=[a;b;c;d]:[1;2;3;4;5];test=%1"},
                {"_test=123\n" +
                        "print=%_test",
                "{\n" +
                        "    \"print\": 123\n" +
                        "}",
                "_test=123;print=%_test"},
                {"\n" +
                        "_test=abc\n" +
                        "\n" +
                        "{\n" +
                        "  test?\n" +
                        "    result=test is defined\n" +
                        "  /?\n" +
                        "    result=test is not defined\n" +
                        "}",
                        "{\n" +
                                "  \"result\" : \"test is defined\"\n" +
                                "}\n",
                "_test=abc;{test?result=test is defined/?result=test is not defined}"},
                {"{\n" +
                        "  true?\n" +
                        "    result=true\n" +
                        "}\n",
                        "{\n" +
                                "  \"result\" : true\n" +
                                "}\n",
                "{01?result=01}"},
                {"_test=true\n" +
                        "\n" +
                        "{\n" +
                        "  test?\n" +
                        "    result=%test\n" +
                        "}\n",
                        "{\n" +
                                "  \"result\" : true\n" +
                                "}\n",
                "_test=01;{test?result=%test}"},
                {"_test=false\n" +
                        "\n" +
                        "{\n" +
                        "  test?\n" +
                        "    result=result is true\n" +
                        "  /?\n" +
                        "    result=result is false\n" +
                        "}",
                "{\n" +
                        "  \"result\" : \"result is false\"\n" +
                        "}",
                "_test=00;{test?result=result is true/?result=result is false}"},
                {"{\n" +
                        "  test?\n" +
                        "    result=test is defined\n" +
                        "  /?\n" +
                        "    result=test is not defined\n" +
                        "}\n",
                "{\n" +
                        "  \"result\" : \"test is not defined\"\n" +
                        "}\n",
                "{test?result=test is defined/?result=test is not defined}"},
                {"{\n" +
                        "  !test?\n" +
                        "    result=test is not defined\n" +
                        "  /?\n" +
                        "    result=test is defined\n" +
                        "}\n",
                "{\n" +
                        "  \"result\" : \"test is not defined\"\n" +
                        "}",
                "{!test?result=test is not defined/?result=test is defined}"},
                {"_colour = green\n" +
                        "_test = { colour=green? true /? false } \n" +
                        "\n" +
                        "{\n" +
                        "  !test?\n" +
                        "    result=it’s not green\n" +
                        "  /?\n" +
                        "    result=it’s green\n" +
                        "}",
                "{\n" +
                        "  \"result\" : \"it’s green\"\n" +
                        "}",
                "_colour=green;_test={colour=green?01/?00};{!test?result=it’s not green/?result=it’s green}"},

                {"_test=1\n" +
                        "result={\n" +
                        "  %test=1?\n" +
                        "    yes\n" +
                        "  /?\n" +
                        "    no\n" +
                        "}",
                        "{ \"result\": \"yes\" }",
                "_test=1;result={%test=1?yes/?no}"},
                {"_test=1\n" +
                        "result={\n" +
                        "  test=1?\n" +
                        "    yes\n" +
                        "  /?\n" +
                        "    no\n" +
                        "}",
                        "{ \"result\": \"yes\" }",
                "_test=1;result={test=1?yes/?no}"},
                {"_test=1\n" +
                        "result={\n" +
                        "  _test=1?\n" +
                        "    yes\n" +
                        "  /?\n" +
                        "    no\n" +
                        "}",
                        "{ \"result\": \"yes\" }",
                "_test=1;result={_test=1?yes/?no}"},
                {"_test=1\n" +
                        "result={\n" +
                        "  %_test=1?\n" +
                        "    yes\n" +
                        "  /?\n" +
                        "    no\n" +
                        "}",
                        "{ \"result\": \"yes\" }",
                "_test=1;result={%_test=1?yes/?no}"},
                {"?[[a;b;c];[one;two;three]];letters=%0;numbers=%1",
                        " [ {\n" +
                                " \"letters\" : [ \"a\", \"b\", \"c\" ]\n" +
                                "}, {\n" +
                                " \"numbers\" : [ \"one\", \"two\", \"three\" ]\n" +
                                "} ]",
                "?=[a;b;c]:[one;two;three];letters=%0;numbers=%1"},
                {"?=[a;b;c]:[one;two;three];letters=%0;numbers=%1", "[{\n" +
                        "    \"letters\": [ \"a\", \"b\", \"c\"]},\n" +
                        "    {\"numbers\": [ \"one\", \"two\", \"three\"]\n" +
                        "}]",
                "?=[a;b;c]:[one;two;three];letters=%0;numbers=%1"},
                {"?[a;b;c];letters=%0", " {\n" +
                        " \"letters\" : \"a\"\n" +
                        "}",
                "?=a:b:c;letters=%0"},
                {"?=[a;b;c]:[one;two;three];letters=%0;numbers=%1",
                        "[ {\n" +
                                "  \"letters\" : [ \"a\", \"b\", \"c\" ]\n" +
                                "}, {\n" +
                                "  \"numbers\" : [ \"one\", \"two\", \"three\" ]\n" +
                                "} ]"},
//                {"test=(zero:one):(a:b)",
                {"test=[zero;one]:[a;b]",
                        "{\n" +
                                "  \"test\" : [ \n" +
                                "    [\"zero\", \"one\" ],\n" +
                                "    [\"a\", \"b\" ]\n" +
                                "  ]\n" +
                                "}"},
                {"test=[zero;one]:[a;b]",
                        "{\n" +
                                "  \"test\" : [ \n" +
                                "    [\"zero\", \"one\" ],\n" +
                                "    [\"a\", \"b\" ]\n" +
                                "  ]\n" +
                                "}"},
                {"?[zero;one;two]\n" +
                        "first_var=%0\n" +
                        "second_var=%1\n" +
                        "third_var=%2",
                        " [{\n" +
                                " \"first_var\" : \"zero\"\n" +
                                "},\n" +
                                "{\n" +
                                " \"second_var\" : \"one\"\n" +
                                "},\n" +
                                "{\n" +
                                " \"third_var\" : \"two\"\n" +
                                "}\n" +
                                "]",
                "?=zero:one:two;first_var=%0;second_var=%1;third_var=%2"},
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
                                "}",
                "_C=gb;o({C=gb?test1=123};test2=456)"},

                {"{\n" +
                        "true?\n" +
                        "  test=1\n" +
                        "}", "{\n" +
                        "  \"test\":1\n" +
                        "}",
                "{01?test=1}"},
                {"_test[a;b;c];alex=%test", "{\n" +
                        "  \"alex\" : [ \"a\", \"b\", \"c\" ]\n" +
                        "}",
                "_test=a:b:c;alex=%test",},
                {"_test[a;b;c];alex=%test[0]", "{\n" +
                        "  \"alex\" : \"a\"\n" +
                        "}",
                "_test=a:b:c;alex=%test[0]"},
                {"?[a;b;c];alex=%0", "{\n" +
                        "  \"alex\" :  \"a\"\n" +
                        "}",
                "?=a:b:c;alex=%0"},
                {"_bool=true\n" +
                        "{\n" +
                        "%bool?\n" +
                        "  test=1\n" +
                        "}", "{\n" +
                        "  \"test\":1\n" +
                        "}",
                "_bool=01;{%bool?test=1}"},
                {"_co = gb\n" +
                        "test = {\n" +
                        "  co = gb?\n" +
                        "    UK\n" +
                        "  /?\n" +
                        "    Other\n" +
                        "}",
                        "{\"test\" : \"UK\" }",
                "_co=gb;test={co=gb?UK/?Other}"},
                {"?=0:1:2\n" +
                        "result={\n" +
                        "%1>1?\n" +
                        "  yes\n" +
                        "/?\n" +
                        "  no\n" +
                        "}", "{\n" +
                        "  \"result\":\"no\"\n" +
                        "}",
                "?=0:1:2;result={%1>1?yes/?no}"},
                {"_test_vars(\n" +
                        "  one = 1\n" +
                        "  two = 2\n" +
                        ")\n" +
                        "\n" +
                        "first_number = %test_vars[one]", "{\n" +
                        "  \"first_number\" : 1\n" +
                        "}",
                "_test_vars(one=1;two=2);first_number=%test_vars[one]"},
                {"_C=gb\n" +
                        "_COUNTRIES[\n" +
                        "  United States\n" +
                        "  United Kingdom\n" +
                        "  Germany\n" +
                        "]\n" +
                        "\n" +
                        "country_name = %COUNTRIES[0]",
                        "{ \"country_name\" : \"United States\" }",
                "_C=gb;_COUNTRIES=United States:United Kingdom:Germany;country_name=%COUNTRIES[0]"},

                {"(_C=gb\n" +
                        "{C=gb?test1=123}\n" +
                        "test2=456)",
                        "{\n" +
                                "  \"test1\" : 123,\n" +
                                "  \"test2\" : 456\n" +
                                "}",
                "(_C=gb;{C=gb?test1=123};test2=456)"},
                {"alex=1.2345", "{\"alex\":1.2345}"},
                {"?=zero:one:two\n" +
                        "discount=%30",
                        "{ \n" +
                                "  \"discount\": \"%30\"\n" +
                                "}",
                "?=zero:one:two;discount=%30"},


                // TODO ESCAPES!!!
//                {"_test=1\\\\:2\n" +
//                        "result=%test", "{\n" +
//                        "    \"result\": \"1:2\"\n" +
//                        "}\n"},
//                {"_test=1\\\\:2\n" +
//                        "\n" +
//                        "result={\n" +
//                        "  test=1\\\\:2?\n" +
//                        "      yes\n" +
//                        "  /?\n" +
//                        "     no\n" +
//                        "}", "{ \"result\": \"yes\" }"},
//                {"test=1~~:2",
//                "{\n" +
//                        "   \"test\": [\n" +
//                        "\t\"1~”,\n" +
//                        "\t“2\"\n" +
//                        "   ]\n" +
//                        "}"},
                {"{\n" +
                        "true?\n" +
                        "  test=1\n" +
                        "}", "{\n" +
                        "  \"test\":1\n" +
                        "}",
                "{01?test=1}"},
                {"{\n" +
                        "TRUE?\n" +
                        "  test=1\n" +
                        "}", "{\n" +
                        "  \"test\":1\n" +
                        "}",
                "{01?test=1}"},
                {"_test=abcdefg\n" +
                        "result={\n" +
                        "  {test!=a*}?\n" +
                        "    in\n" +
                        "  /?\n" +
                        "    out\n" +
                        "}", "{\n" +
                        "    \"result\": \"out\"\n" +
                        "}",
                "_test=abcdefg;result={{test!=a*}?in/?out}"},
                {"test=[]", "{\n" +
                        "  \"test\":[]\n" +
                        "}",
                "test[]"},
                {"test(\n" +
                        "  empty_array=[]\n" +
                        "  empty_map=()\n" +
                        ")\n", "{\n" +
                        "  \"test\": {\n" +
                        "    \"empty_array\":[],\n" +
                        "    \"empty_map\":{}\n" +
                        "  }\n" +
                        "}",
                "test(empty_array[];empty_map())"},
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
                        "}",
                "_num1=2;_num2=1000;result={num1>num2?num1 is bigger/?num1 is not bigger}"},
                {"?=0:1:2\n" +
                        "zero=%0\n" +
                        "one=%1\n" +
                        "two=%2", "[\n" +
                        "    {\n" +
                        "        \"zero\": 0\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"one\": 1\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"two\": 2\n" +
                        "    }\n" +
                        "]",
                "?=0:1:2;zero=%0;one=%1;two=%2"},
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
                        "]",
                "?=a:b:c;zero=%0;one=%1;two=%2"},
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
                        "}",
                "_num1=5;_num2=2;result={num1>num2?num1 is bigger/?num1 is not bigger}"},
                {"?=\"A\":B:C\n" +
                        "first_letter=%0","{ \"first_letter\" : \"A\" }",
                "?=A:B:C;first_letter=%0"},
                {"test=100%",
                        "{\"test\":\"100%\"}"},
                {"test=`test`",
                        "{\n" +
                                "  \"test\" : \"test\"\n" +
                                "}"},
                {"test=\\u0021",
                        "{\n" +
                                "  \"test\" : \"!\"\n" +
                                "}"},
//                {"test=(zero:one)",
//                        "{\n" +
//                                "  \"test\" : [ \"zero\", \"one\" ]\n" +
//                                "}"},
                {"test=[zero;one]",
                        "{\n" +
                                "  \"test\" : [ \"zero\", \"one\" ]\n" +
                                "}",
                "test=zero:one"},
                {"_C=fr\n" +
                        "{C=gb?test1=123}\n" +
                        "test2=456",
                        "{\n" +
                                "  \"test2\" : 456\n" +
                                "}",
                "_C=fr;{C=gb?test1=123};test2=456"},
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
                                "}",
                "_C=ca;_L=en;{C=ca?n=Tesco Canada;{L=fr?s=Chaque Petite Contribution}}"},
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
                        "}","",
                        "_L=en;{C=ca?o(n=Tesco Canada;s={L=fr?Chaque Petite Contribution/?Every Little Helps})}"},
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
                                "}",
                "_letter=a;{letter=a?word=Apple/letter=b?word=Bee/?word=Other}"},
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
                                "}",
                "_int=1;{int=1?number=one/int=2?number=two/int=3?number=three/?number=other}"},
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
                                "}",
                "_number=one;{number=one?int=1/number=two?int=2/number=three?int=3}"},
                {"_co=gb\n" +
                        "{\n" +
                        "co=gb?\n" +
                        "  country = United Kingdom\n" +
                        "/?\n" +
                        "  country = Other\n" +
                        "}",
                        "{\n" +
                                "  \"country\" : \"United Kingdom\"\n" +
                                "}",
                "_co=gb;{co=gb?country=United Kingdom/?country=Other}"},
                {"_co=gb\n" +
                        "{\n" +
                        "co=gb?\n" +
                        "  country = United Kingdom\n" +
                        "/?\n" +
                        "  country = Other\n" +
                        "}",
                        "{\n" +
                                "  \"country\" : \"United Kingdom\"\n" +
                                "}",
                "_co=gb;{co=gb?country=United Kingdom/?country=Other}"},
                {"_co = gb\n" +
                        "test = {\n" +
                        "  co = gb?\n" +
                        "    test=123\n" +
                        "  /?\n" +
                        "    test\n" +
                        "}",
                        "{\"test\": { \"test\" : 123 }}",
                "_co=gb;test={co=gb?test=123/?test}"},
                {"_co = fr\n" +
                        "test = {\n" +
                        "  co = gb?\n" +
                        "    test=123\n" +
                        "  /?\n" +
                        "    test\n" +
                        "}",
                        "{\"test\": \"test\"}",
                "_co=fr;test={co=gb?test=123/?test}"},
                {"_COUNTRY=gb\n" +
                        "country=The country is %COUNTRY",
                        "{\n" +
                                "  \"country\" : \"The country is gb\"\n" +
                                "}",
                "_COUNTRY=gb;country=The country is %COUNTRY"},
                {"COUNTRY=gb\n" +
                        "country=The country is %COUNTRY",
                        "[ {\n" +
                                "  \"COUNTRY\" : \"gb\"\n" +
                                "}, {\n" +
                                "  \"country\" : \"The country is gb\"\n" +
                                "} ]",
                "COUNTRY=gb;country=The country is %COUNTRY"},
                {"_co=gb\n" +
                        "country=The country is %co",
                        "{\n" +
                                "  \"country\" : \"The country is gb\"\n" +
                                "}",
                "_co=gb;country=The country is %co"},
                {"_test = 123\n" +
                        "_test2 = abc",
                        "",
                "_test=123;_test2=abc"},
                {"_co=gb\n" +
                        "test=123",
                        "{ \"test\" : 123 }",
                "_co=gb;test=123"},
                {"*method(\n" +
                        "  ## The method can be called by it's ID or name\n" +
                        "  *id=cn\n" +
                        "  *name=company_name\n" +
                        "  ## The value of the object that the method is called on is transformed using the following methods:\n" +
                        "  *transform=`replace(-, ).trim(.).initcap`\n" +
                        ")\n" +
                        "\n" +
                        "_domain = smiths-limited.com\n" +
                        "friendly_name = %domain.cn ## The value \"Smiths Limited\" is assigned to the key \"friendly_name\"",
                        "{\n" +
                                "    \"friendly_name\": \"Smiths Limited\"\n" +
                                "}",
                        "*m(*i=cn;*n=company_name;*transform=`replace(-, ).trim(.).initcap`);_domain=smiths-limited.com;friendly_name=%domain.cn"},
                {"*method(\n" +
                        "  *id=rt\n" +
                        "  *name=remove_two\n" +
                        "  *transform=`replace(two,)`\n" +
                        ")\n" +
                        "\n" +
                        "_numbers = one two three\n" +
                        "name = %numbers.rt",
                        "{ \"name\": \"one  three\" }",
                        "*m(*i=rt;*n=remove_two;*transform=`replace(two,)`);_numbers=one two three;name=%numbers.rt"}
        });

        @Test
        public void testParser() throws IOException {
                // Take a load of test MODL strings and make sure uk.modl.parser generates correct output

                for (Object test : expected) {
                        String input = (String)((Object[])test)[0];
                        String expected = (String)((Object[])test)[1];

                        System.out.println("Input : " + input);
                        System.out.println("Expected : " + expected);

                        ModlObject modlObject = Interpreter.interpret(input);
                        String output = JsonPrinter.printModl(modlObject);
                        System.out.println("Output : " + output);
                        assertEquals(expected.replace(" ", "").replace("\n", ""),
                                output.replace(" ", "").replace("\n", ""));
                }

        }

        @Test
        public void testPrintingModlValue() throws Exception {
                ModlObject object = Interpreter.interpret("n=(a=1\nb=2)");
                String output = JsonPrinter.printModl(object.get("n"));
                System.out.println(output);
                assertEquals("{\n" +
                        "  \"a\" : 1,\n" +
                        "  \"b\" : 2\n" +
                        "}", output);
        }

        // TODO Run some test inputs which should generate errors (e.g. re-defining uppercase OBJECTNAME)
}