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

import io.vavr.Function2;
import io.vavr.Tuple2;
import io.vavr.control.Option;
import lombok.NonNull;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import uk.modl.model.Modl;
import uk.modl.parser.Parser;
import uk.modl.parser.errors.InterpreterError;
import uk.modl.transforms.TransformationContext;

/**
 * Interpret a MODL String
 *
 * @author tonywalmsley
 */
public class Interpreter implements Function2<TransformationContext, String, Tuple2<TransformationContext, Modl>> {

    private final Parser parser = new Parser();

    private final InterpreterVisitor interpreterVisitor = new InterpreterVisitor();

    /**
     * Interpreter entry point
     *
     * @param ctx   TransformationContext
     * @param input a String, which should be a MODL String, but could be any value.
     * @return Either an Error or a Modl object.
     */
    @Override
    public Tuple2<TransformationContext, Modl> apply(final TransformationContext ctx, @NonNull final String input) {
        // Apply the function and return the result.
        return Option.of(input)
                .map(s -> {
                    try {
                        return parser.apply(s, ctx.getAncestry());
                    } catch (final InterpreterError e) {
                        throw e;
                    } catch (final ParseCancellationException e) {
                        throw new InterpreterError("Parser Error: " + e.getMessage());
                    } catch (final RuntimeException e) {
                        throw new InterpreterError("Interpreter Error: " + e.getMessage());
                    }
                })
                .map(modl -> apply(ctx, modl))
                .get();
    }

    /**
     * Interpreter entry point2 - reprocessing of Modl objects that were cached
     *
     * @param ctx  TransformationContext
     * @param modl a Modl object.
     * @return Either an Error or a Modl object.
     */
    public Tuple2<TransformationContext, Modl> apply(final TransformationContext ctx, @NonNull final Modl modl) {
        // Apply the function and return the result.
        return Option.of(modl)
                .map(m -> interpreterVisitor.apply(ctx, m))
                .get();
    }

}
