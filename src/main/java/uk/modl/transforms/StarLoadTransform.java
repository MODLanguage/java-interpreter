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

package uk.modl.transforms;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import uk.modl.extractors.StarLoadExtractor;
import uk.modl.interpreter.Interpreter;
import uk.modl.model.*;
import uk.modl.parser.errors.StarLoadException;
import uk.modl.utils.SimpleCache;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

@RequiredArgsConstructor
public class StarLoadTransform {

    private static final SimpleCache<String, Modl> cache = new SimpleCache<>();

    /**
     * Function to extract filenames and pairs from a Modl object.
     */
    private static Vector<StarLoadExtractor.LoadSet> extractFilenamesAndPairs(final Pair p) {
        return new StarLoadExtractor().accept(p)
                .getLoadSets();
    }

    /**
     * Function to convert filenames and pairs to Either Strings/Modl-objects and Pairs.
     */
    private Tuple2<TransformationContext, Vector<Tuple3<Vector<String>, Vector<Modl>, Pair>>> convertFilesToModlObjectsAndPairs(final TransformationContext ctx, final Vector<StarLoadExtractor.LoadSet> list) {

        TransformationContext newCtx = ctx;

        Vector<Tuple3<Vector<String>, Vector<Modl>, Pair>> result = Vector.empty();

        for (final StarLoadExtractor.LoadSet loadSet : list) {

            if (newCtx.isStarLoadImmutable()) {
                throw new RuntimeException("Cannot load multiple files after *LOAD instruction");
            }
            if (loadSet.getPair()
                    .getKey()
                    .startsWith("*L")) {
                newCtx = newCtx.withStarLoadImmutable(true);
            }

            // Each tuple has a list of filenames
            final Vector<StarLoadExtractor.FileSpec> filenames = loadSet.getFileSet();

            for (final StarLoadExtractor.FileSpec spec : filenames) {
                // Interpret each MODL string from each file
                final Interpreter interpreter = new Interpreter();
                try {

                    if (cache.contains(spec.getFilename()) && !spec.isForceLoad()) {
                        // Its cached and not force-loaded
                        final Tuple2<StarLoadExtractor.FileSpec, Modl> cachedModl = Tuple.of(spec, cache.get(spec.getFilename()));

                        // Re-interpret the cached Modl objects to extract classes, methods etc. for the current context
                        final Tuple2<TransformationContext, Modl> interpreted = interpreter.apply(newCtx, cachedModl._2);
                        newCtx = interpreted._1;

                        result = result.append(Tuple.of(Vector.of(cachedModl._1.getFilename()), Vector.of(cachedModl._2), loadSet.getPair()));

                        // Add the cache misses to the cache for next time
                        cache.put(cachedModl._1.getFilename(), cachedModl._2);
                    } else {
                        // Its either not cached or not force-loaded
                        // Map the filenames to the contents of the files, or Error
                        final Tuple2<StarLoadExtractor.FileSpec, String> contents = getFileContents(spec);
                        final Tuple2<TransformationContext, Modl> interpreted = interpreter.apply(newCtx, contents._2);
                        newCtx = interpreted._1;

                        result = result.append(Tuple.of(Vector.of(contents._1.getFilename()), Vector.of(interpreted._2), loadSet.getPair()));
                    }
                } catch (final StarLoadException e) {
                    //
                    // If any load returns an error AND we have a cached copy then use the cached copy for up to 7 days.
                    //
                    if (cache.contains(spec.getFilename())) {
                        final Tuple2<StarLoadExtractor.FileSpec, Modl> cachedModl = Tuple.of(spec, cache.get(spec.getFilename()));

                        // Re-interpret the cached Modl objects to extract classes, methods etc. for the current context
                        final Tuple2<TransformationContext, Modl> interpreted = interpreter.apply(newCtx, cachedModl._2);
                        newCtx = interpreted._1;

                        result = result.append(Tuple.of(Vector.of(cachedModl._1.getFilename()), Vector.of(cachedModl._2), loadSet.getPair()));
                    } else {
                        throw e;
                    }
                }
            }
        }

        return Tuple.of(newCtx, result);
    }

    /**
     * Map a filename to Either an Error or the file contents as a String
     */
    public Tuple2<StarLoadExtractor.FileSpec, String> getFileContents(final StarLoadExtractor.FileSpec spec) {
        try {
            if (spec.getFilename()
                    .startsWith("http")) {

                // Load over the net
                final URL url = new URL(spec.getFilename());
                return Tuple.of(spec, new Scanner(url.openStream(), StandardCharsets.UTF_8.name()).useDelimiter("\\A")
                        .next());
            } else if (Files.exists(Paths.get(spec.getFilename()))) {

                // Load local file
                return Tuple.of(spec, String.join("", Files.readAllLines(Paths.get(spec.getFilename()))));
            }
        } catch (final Exception e) {
            throw new StarLoadException("Could not load resource: " + e.getMessage());
        }
        throw new StarLoadException("Could not load resource: " + spec.getFilename());
    }

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param s argument 1
     * @return the result of function application
     */
    public Tuple2<TransformationContext, Structure> apply(final TransformationContext ctx, final Structure s) {

        TransformationContext newCtx = ctx;

        if (s instanceof Pair) {
            final Pair rawPair = (Pair) s;
            if (StarLoadExtractor.isLoadInstruction(rawPair.getKey())) {
                final Tuple2<TransformationContext, Structure> refsResult = new ReferencesTransform().apply(newCtx, (Structure) rawPair);
                newCtx = refsResult._1;
                final Pair p = (Pair) refsResult._2;

                // Each tuple in this list holds the original Pair with the `*load` statements and the set of Modl objects
                // loaded using the filename[s] specified in the file list - there can be 1 or several.
                final Tuple2<TransformationContext, Vector<Tuple3<Vector<String>, Vector<Modl>, Pair>>> result = convertFilesToModlObjectsAndPairs(newCtx, extractFilenamesAndPairs(p));

                // Record which files were loaded - for use in a `%*load` reference
                newCtx = result._1.addFilesLoaded(result._2.flatMap(tuple -> tuple._1));


                return Tuple.of(newCtx, new StarLoadMutator(result._2).accept(p, ctx));
            }
        }
        return Tuple.of(newCtx, s);
    }

    /**
     * Build a new copy of the Modl object with some pairs replaced
     */
    @AllArgsConstructor
    private static class StarLoadMutator {

        private final Vector<Tuple3<Vector<String>, Vector<Modl>, Pair>> loadedModlObjects;

        public Pair accept(final Pair pair, final TransformationContext ctx) {
            // Find the last matching loaded object (last because the earlier ones might be overridden by later ones)
            final Option<Tuple3<Vector<String>, Vector<Modl>, Pair>> maybeFoundPair = loadedModlObjects.findLast(tuple3 -> pair.equals(tuple3._3));

            // Create a new Modl object with the updated pair.
            return maybeFoundPair.map(p -> replace(pair, p, ctx))
                    .getOrElse(pair);
        }

        /**
         * Replace any *load commands with their contents
         *
         * @param p           the current Modl object
         * @param replacement the pair to be replaced and the set of Modl objects loaded from the files.
         * @param ctx         TransformationContext
         * @return a new Modl object with the relevant changes, sharing existing objects where possible
         */
        private Pair replace(final Pair p, final Tuple3<Vector<String>, Vector<Modl>, Pair> replacement, final TransformationContext ctx) {

            if (p.equals(replacement._3)) {

                final Vector<ArrayItem> arrayItems = replacement._2.flatMap(m -> m.getStructures()
                        .filter(structure -> structure instanceof ArrayItem)
                        .map(structure -> (ArrayItem) structure));

                return p.with(ctx.getAncestry(), Array.of(ctx.getAncestry(), p, arrayItems));
            } else {
                return p;
            }
        }

    }

}

