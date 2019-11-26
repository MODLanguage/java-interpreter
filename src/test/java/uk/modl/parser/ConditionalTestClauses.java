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

import org.junit.Assert;
import org.junit.Test;
import uk.modl.interpreter.Interpreter;
import uk.modl.modlObject.ModlObject;
import uk.modl.parser.printers.JsonPrinter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author twalmsley
 */
public class ConditionalTestClauses {
    private final static String[] expected = {
            //
            // All of these tests should set `result=success`
            //
            "_abc=xyz;_def=pqr; {%`xyz`=%abc?    result=success /? result=fail}",// lhs explicitly literal
            "_abc=xyz;_def=pqr; {abc=xyz?        result=success /? result=fail}",// rhs implicitly literal
            "_abc=xyz;_def=pqr; {%`xyz`=%`xyz`?  result=success /? result=fail}",// lhs+rhs explicitly literal
            "_abc=xyz;_def=pqr; {%`xyz`=xyz?     result=success /? result=fail}",
            "_abc=xyz;_def=pqr; {abc=%`xyz`?     result=success /? result=fail}",
            "_abc=xyz;_def=pqr; {abc=%`XYZ`.d?   result=success /? result=fail}",
            "_abc=xyz;_def=pqr; {abc=%abc?       result=success /? result=fail}",

            // (Note the switching around of success and fail below here)

            "_abc=xyz;_def=pqr; {\"abc\"=%abc?   result=fail    /? result=success}",
            "_abc=xyz;_def=pqr; {'abc'=%abc?     result=fail    /? result=success}",
            "_abc=xyz;_def=pqr; {`abc`=%abc?     result=fail    /? result=success}",

            "_abc=xyz;_def=pqr; {abc=abc?        result=fail    /? result=success}",// rhs defaults to literal
            "_abc=xyz;_def=pqr; {abc=%`abc`?     result=fail    /? result=success}",
            "_abc=xyz;_def=pqr; {abc=`%abc`?     result=fail    /? result=success}",
            "_abc=xyz;_def=pqr; {abc=`%xyz`?     result=fail    /? result=success}",

            "{abc=xyz?                           result=fail    /? result=success}",// abc is undefined
            "_abc=xyz; {abc=%def?                result=fail    /? result=success}",// def is undefined
    };

    @Test
    public void test_00() {
        final List<String> failures = Arrays.stream(expected)
                .map(this::singleCase)
                .filter(x -> !"passed".equals(x))
                .collect(Collectors.toList());
        System.out.println(failures);
        Assert.assertEquals(0, failures.size());
    }

    private String singleCase(final String test) {

        //System.out.print(test);
        //System.out.print("\t\t\t\t");

        try {
            final ModlObject interpreted = Interpreter.interpret(test);
            final String output = JsonPrinter.printModl(interpreted, false);
            //System.out.println(output);
            if ("{\"result\":\"success\"}".equals(output)) {
                return "passed";
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return "\nTest '" + test + "' failed\n";
    }
}