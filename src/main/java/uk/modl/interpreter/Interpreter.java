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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.Tuple2;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import lombok.NonNull;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.commons.io.IOUtils;
import uk.modl.model.Modl;
import uk.modl.model.Pair;
import uk.modl.model.Structure;
import uk.modl.parser.Parser;
import uk.modl.parser.errors.InterpreterError;
import uk.modl.transforms.JacksonJsonNodeTransform;
import uk.modl.transforms.TransformationContext;
import uk.modl.utils.Util;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Interpret a MODL String
 *
 * @author tonywalmsley
 */
public class Interpreter {

    private final Parser parser = new Parser();

    private final InterpreterVisitor interpreterVisitor = new InterpreterVisitor();

    /**
     * Interpret a String into a Modl object.
     *
     * @param modlString String
     * @return Modl
     */
    public Modl interpret(final String modlString) {
        final TransformationContext ctx = TransformationContext.baseCtx(null);
        final Tuple2<TransformationContext, Modl> interpreted = apply(ctx, modlString);
        return interpreted._2;
    }

    /**
     * Interpret a String into a Modl object.
     *
     * @param url URL
     * @return Modl
     */
    public Modl interpret(final URL url) throws IOException {
        final String modlString = IOUtils.toString(url, StandardCharsets.UTF_8);

        final TransformationContext ctx = TransformationContext.baseCtx(url);
        final Tuple2<TransformationContext, Modl> interpreted = apply(ctx, modlString);
        return interpreted._2;
    }

    /**
     * Interpret a String into a Modl object.
     *
     * @param modlString String
     * @param url        URL
     * @return Modl
     */
    public Modl interpret(final String modlString, final URL url) {
        final TransformationContext ctx = TransformationContext.baseCtx(url);
        final Tuple2<TransformationContext, Modl> interpreted = apply(ctx, modlString);
        return interpreted._2;
    }

    /**
     * Interpret a String into a JsonNode object.
     *
     * @param modlString String
     * @return JsonNode
     */
    public JsonNode interpretToJsonObject(final String modlString) {
        final TransformationContext ctx = TransformationContext.baseCtx(null);
        final Tuple2<TransformationContext, Modl> interpreted = apply(ctx, modlString);
        return new JacksonJsonNodeTransform(ctx).apply(interpreted._2);
    }

    /**
     * Interpret a String into a JsonNode object.
     *
     * @param url URL
     * @return JsonNode
     */
    public JsonNode interpretToJsonObject(final URL url) throws IOException {
        final String modlString = IOUtils.toString(url, StandardCharsets.UTF_8);
        final TransformationContext ctx = TransformationContext.baseCtx(url);
        final Tuple2<TransformationContext, Modl> interpreted = apply(ctx, modlString);
        return new JacksonJsonNodeTransform(ctx).apply(interpreted._2);
    }

    /**
     * Interpret a String into a JsonNode object.
     *
     * @param modlString String
     * @param url        URL
     * @return JsonNode
     */
    public JsonNode interpretToJsonObject(final String modlString, final URL url) {
        final TransformationContext ctx = TransformationContext.baseCtx(url);
        final Tuple2<TransformationContext, Modl> interpreted = apply(ctx, modlString);
        return new JacksonJsonNodeTransform(ctx).apply(interpreted._2);
    }

    /**
     * Interpret a String into a JsonNode object.
     *
     * @param modlString String
     * @return String
     * @throws JsonProcessingException on error
     */
    public String interpretToJsonString(final String modlString, final URL url) throws JsonProcessingException {
        final JsonNode jsonNode = interpretToJsonObject(modlString, url);
        return new ObjectMapper().writeValueAsString(jsonNode);
    }

    /**
     * Interpret a String into a JsonNode object.
     *
     * @param modlString String
     * @return String
     * @throws JsonProcessingException on error
     */
    public String interpretToPrettyJsonString(final String modlString) throws JsonProcessingException {
        final JsonNode jsonNode = interpretToJsonObject(modlString, null);
        return new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(jsonNode);
    }

    /**
     * Interpret a String into a JsonNode object.
     *
     * @param url URL
     * @return String
     * @throws JsonProcessingException on error
     */
    public String interpretToPrettyJsonString(final URL url) throws IOException {
        final String modlString = IOUtils.toString(url, StandardCharsets.UTF_8);
        final JsonNode jsonNode = interpretToJsonObject(modlString, url);
        return new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(jsonNode);
    }

    /**
     * Interpret a String into a JsonNode object.
     *
     * @param modlString String
     * @param url        URL
     * @return String
     * @throws JsonProcessingException on error
     */
    public String interpretToPrettyJsonString(final String modlString, final URL url) throws JsonProcessingException {
        final JsonNode jsonNode = interpretToJsonObject(modlString, url);
        return new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(jsonNode);
    }

    /**
     * Interpreter entry point
     *
     * @param ctx   TransformationContext
     * @param input a String, which should be a MODL String, but could be any value.
     * @return Either an Error or a Modl object.
     */
    public Tuple2<TransformationContext, Modl> apply(final TransformationContext ctx, @NonNull final String input) {
        // Apply the function and return the result.
        return Option.of(input)
                .map(s -> parse(ctx, s))
                .map(modl -> apply(ctx, modl))
                .get();
    }

    /**
     * Initial parsing of a MODL string - no interpretation
     *
     * @param ctx        TransformationContext
     * @param modlString a String, which should be a MODL String, but could be any value.
     * @return a Modl object
     */
    public Modl parse(final TransformationContext ctx, @NonNull final String modlString) {
        try {
            final Modl modl = parser.apply(modlString, ctx.getAncestry());

            // Check that the top level has all Pairs if it has any at all.
            final Vector<Structure> filtered = modl.getStructures()
                    .filter(Util::shouldAppearInOutput);
            final int numberOfTopLevelPairs = filtered.count(s -> s instanceof Pair);
            if (numberOfTopLevelPairs > 0 && numberOfTopLevelPairs < filtered.size()) {
                throw new InterpreterError("Interpreter Error: Mixed top-level types are not allowed.");
            }

            return modl;
        } catch (final InterpreterError e) {
            throw e;
        } catch (final ParseCancellationException e) {
            throw new InterpreterError("Parser Error: " + e.getMessage());
        } catch (final RuntimeException e) {
            throw new InterpreterError("Interpreter Error: " + e.getMessage());
        }
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
