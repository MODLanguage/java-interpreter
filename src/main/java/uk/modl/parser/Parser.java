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

package uk.modl.parser;

import lombok.extern.log4j.Log4j2;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import uk.modl.ancestry.Ancestry;
import uk.modl.model.Modl;
import uk.modl.parser.antlr.MODLLexer;
import uk.modl.parser.antlr.MODLParser;
import uk.modl.parser.errors.ThrowingErrorListener;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Class to parse MODL Strings to Modl trees.
 */
@Log4j2
public class Parser {

    /**
     * Parse a MODL String to a Modl object
     *
     * @param input               the MODL String
     * @param ancestry            Ancestry
     * @param timeoutMilliseconds the number of seconds the caller is prepared to wait for a result.
     * @return Either a Throwable or a Modl object
     */
    public Modl apply(final String input, final Ancestry ancestry, final long timeoutMilliseconds) {
        try {
            final ExecutorService executorService = Executors.newSingleThreadExecutor();
            // Antlr boilerplate
            final InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
            final MODLLexer lexer = new MODLLexer(CharStreams.fromStream(stream, StandardCharsets.UTF_8));
            final CommonTokenStream tokens = new CommonTokenStream(lexer);
            final MODLParser parser = new MODLParser(tokens);
            parser.setBuildParseTree(true);

            lexer.addErrorListener(ThrowingErrorListener.INSTANCE);
            parser.addErrorListener(ThrowingErrorListener.INSTANCE);

            /*
             * Warning: Nasty threading code follows
             *
             * The reason for this is that the MODL grammar can take a very long time to process if deeply nested,
             * and ANTLR4 cannot be interrupted, hence it is run in a separate thread. We have to use the Thread.stop()
             * method because otherwise the thread does not terminate and cannot be killed any other way.
             *
             * This issue may be resolved by grammar changes in future.
             *
             */

            // This is to capture the executor thread that will process the MODL so we can call its `stop()` method
            // if there is a timeout.
            final AtomicReference<Thread> taskThread = new AtomicReference<>();

            // Execute the parser in a Future
            final Future<MODLParser.ModlContext> future = executorService.submit(() -> {
                taskThread.set(Thread.currentThread());
                return parser.modl();
            });

            final MODLParser.ModlContext modlCtx;

            try {
                // Get the result
                modlCtx = future.get(timeoutMilliseconds, TimeUnit.MILLISECONDS);
            } finally {
                // Force shutdown and kill the thread.
                executorService.shutdownNow();
                try {
                    taskThread.get()
                            .stop();// Try to kill the thread.
                } catch (final Throwable e) {
                    // Ignore ThreadDeath
                }
            }

            if (modlCtx != null) {
                // The String has been parsed by Antlr, now its our turn
                return new ModlParsedVisitor(modlCtx, ancestry).getModl();
            }

            throw new TimeoutException();
        } catch (final IOException | InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof ParseCancellationException) {
                throw (ParseCancellationException) e.getCause();
            }
            throw new RuntimeException(e);
        }
    }

}
