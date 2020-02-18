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

package uk.modl.interpreter;

import io.vavr.Function1;
import io.vavr.control.Option;
import uk.modl.model.Modl;
import uk.modl.parser.Parser;
import uk.modl.transforms.TransformationContext;

/**
 * Interpret a MODL String
 *
 * @author tonywalmsley
 */
public class Interpreter implements Function1<String, Modl> {

    private final Parser parser = new Parser();
    private final InterpreterVisitor interpreterVisitor = new InterpreterVisitor();

    /**
     * Interpreter entry point
     *
     * @param input a String, which should be a MODL String, but could be any value.
     * @return Either an Error or a Modl object.
     */
    @Override
    public Modl apply(final String input) {
        // Apply the function and return the result.
        return Option.of(input)
                .map(parser)
                .map(interpreterVisitor)
                .getOrElseThrow(() -> new RuntimeException("Cannot parse null input"));
    }

    public void setCtx(final TransformationContext ctx) {
        interpreterVisitor.setCtx(ctx);
    }
}
