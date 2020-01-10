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
import io.vavr.control.Either;
import io.vavr.control.Option;
import uk.modl.error.Error;
import uk.modl.model.Modl;
import uk.modl.parser.Parser;
import uk.modl.transforms.ReferencesTransform;
import uk.modl.transforms.StarClassTransform;
import uk.modl.transforms.StarLoadTransform;

/**
 * Interpret a MODL String
 *
 * @author tonywalmsley
 */
public class Interpreter implements Function1<String, Either<Error, Modl>> {

    private final Function1<String, Either<Error, Modl>> interpretFunction;

    /**
     * Constructor
     */
    public Interpreter() {
        final Parser parser = new Parser();
        final StarLoadTransform starLoadTransform = new StarLoadTransform();
        final StarClassTransform starClassTransform = new StarClassTransform();
        final ReferencesTransform referencesTransform = new ReferencesTransform();

        // Build the function to do the interpreting
        interpretFunction = parser.andThen(starLoadTransform)
                .andThen(starClassTransform)
                .andThen(referencesTransform);
    }

    /**
     * Interpreter entry point
     *
     * @param input a String, which should be a MODL String, but could be any value.
     * @return Either an Error or a Modl object.
     */
    public Either<Error, Modl> apply(final String input) {
        return Option.of(input)
                .map(s -> {
                    // Apply the function and return the result.
                    return interpretFunction.apply(input);
                })
                .getOrElse(Either.left(new Error("Cannot parse null input")));
    }

}
