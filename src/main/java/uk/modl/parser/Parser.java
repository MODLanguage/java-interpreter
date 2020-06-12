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

import lombok.extern.log4j.Log4j2;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import uk.modl.ancestry.Ancestry;
import uk.modl.model.Modl;
import uk.modl.parser.antlr.MODLLexer;
import uk.modl.parser.antlr.MODLParser;
import uk.modl.parser.errors.ThrowingErrorListener;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Class to parse MODL Strings to Modl trees.
 */
@Log4j2
public class Parser {

    /**
     * Parse a MODL String to a Modl object
     *
     * @param input    the MODL String
     * @param ancestry Ancestry
     * @return Either a Throwable or a Modl object
     */
    public Modl apply(final String input, final Ancestry ancestry) {
        try {
            // Antlr boilerplate
            final InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
            final MODLLexer lexer = new MODLLexer(CharStreams.fromStream(stream, StandardCharsets.UTF_8));
            final CommonTokenStream tokens = new CommonTokenStream(lexer);
            final MODLParser parser = new MODLParser(tokens);
            parser.setBuildParseTree(true);

            lexer.addErrorListener(ThrowingErrorListener.INSTANCE);
            parser.addErrorListener(ThrowingErrorListener.INSTANCE);

            final MODLParser.ModlContext modlCtx = parser.modl();

            // The String has been parsed by Antlr, now its our turn
            return new ModlParsedVisitor(modlCtx, ancestry).getModl();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

}
