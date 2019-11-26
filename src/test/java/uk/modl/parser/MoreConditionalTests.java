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

import org.junit.Test;
import uk.modl.interpreter.Interpreter;
import uk.modl.modlObject.ModlObject;
import uk.modl.parser.printers.JsonPrinter;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author twalmsley
 */
public class MoreConditionalTests {
    private final static String[] expected = {
            "_test1=one;_one=two;{test1=`one`?result=match/?result=nomatch}",
            "_test1=one;_one=two;{test1=one?result=match/?result=nomatch}",

            "_test1=a;_one=a; {test1=one? result=match/?result=nomatch}",
            "_test1=a;_one=a; {test1=`one`? result=match/?result=nomatch}",
            "_test1=a;_one=a; {test1='one'? result=match/?result=nomatch}",
            "_test1=a;_one=a; {test1=\"one\"? result=match/?result=nomatch}",

            "_test1=a;_one=b; {test1=one? result=match/?result=nomatch}",
            "_test1=a;_one=b; {test1=`one`? result=match/?result=nomatch}",
            "_test1=a;_one=b; {test1='one'? result=match/?result=nomatch}",
            "_test1=a;_one=b; {test1=\"one\"? result=match/?result=nomatch}",

            "_test1=one; {test1=one? result=match/?result=nomatch}",
            "_test1=one; {test1=`one`? result=match/?result=nomatch}",
            "_test1=one; {test1='one'? result=match/?result=nomatch}",
            "_test1=one; {test1=\"one\"? result=match/?result=nomatch}",

            "_test1=x;_one=one; {test1=one? result=match/?result=nomatch}",
            "_test1=x;_one=one; {test1=`one`? result=match/?result=nomatch}",
            "_test1=x;_one=one; {test1='one'? result=match/?result=nomatch}",
            "_test1=x;_one=one; {test1=\"one\"? result=match/?result=nomatch}",

            "_test1=a;_one=a; {test1=a? result=match/?result=nomatch}",
            "_test1=a;_one=a; {test1=`a`? result=match/?result=nomatch}",
            "_test1=a;_one=a; {test1='a'? result=match/?result=nomatch}",
            "_test1=a;_one=a; {test1=\"a\"? result=match/?result=nomatch}",

            "_test1=a;_one=a; {one=a? result=match/?result=nomatch}",
            "_test1=a;_one=a; {one=`a`? result=match/?result=nomatch}",
            "_test1=a;_one=a; {one='a'? result=match/?result=nomatch}",
            "_test1=a;_one=a; {one=\"a\"? result=match/?result=nomatch}",

    };

    @Test
    public void test_00() {
        Arrays.stream(expected)
                .forEach(this::singleCase);
    }

    private void singleCase(final String test) {

        System.out.print(test);
        System.out.print("\t\t\t\t");

        try {
            final ModlObject interpreted = Interpreter.interpret(test);
            final String output = JsonPrinter.printModl(interpreted, false);
            System.out.println(output);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}