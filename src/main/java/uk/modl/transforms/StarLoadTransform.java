package uk.modl.transforms;

import io.vavr.Function1;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import uk.modl.extractors.StarLoadExtractor;
import uk.modl.interpreter.Interpreter;
import uk.modl.model.*;
import uk.modl.utils.SimpleCache;
import uk.modl.utils.Util;

@RequiredArgsConstructor
public class StarLoadTransform implements Function1<Structure, Structure> {

    private static final SimpleCache<String, Modl> cache = new SimpleCache<>();

    /**
     * Function to extract filenames and pairs from a Modl object.
     */
    private static final Function1<Pair, Vector<StarLoadExtractor.LoadSet>> extractFilenamesAndPairs = (m) -> {
        final StarLoadExtractor starLoadExtractor = new StarLoadExtractor();
        m.visit(starLoadExtractor);
        return starLoadExtractor.getLoadSets();
    };

    /**
     * The context for this invocation of the interpreter
     */
    @NonNull
    @Setter
    private TransformationContext ctx;

    /**
     * Function to convert filenames and pairs to Either Strings/Modl-objects and Pairs.
     */
    private Vector<Tuple3<Vector<String>, Vector<Modl>, Pair>> convertFilesToModlObjectsAndPairs(final Vector<StarLoadExtractor.LoadSet> list) {

        Vector<Tuple3<Vector<String>, Vector<Modl>, Pair>> result = Vector.empty();

        for (final StarLoadExtractor.LoadSet loadSet : list) {

            // Each tuple has a list of filenames
            final Vector<StarLoadExtractor.FileSpec> filenames = loadSet.getFileSet();

            for (final StarLoadExtractor.FileSpec spec : filenames) {
                // Interpret each MODL string from each file
                final Interpreter interpreter = new Interpreter();
                interpreter.setCtx(ctx);

                if (cache.contains(spec.getFilename()) && !spec.isForceLoad()) {
                    // Its cached and not force-loaded
                    final Tuple2<StarLoadExtractor.FileSpec, Modl> cachedModl = Tuple.of(spec, cache.get(spec.getFilename()));

                    // Re-interpret the cached Modl objects to extract classes, methods etc. for the current context
                    interpreter.apply(cachedModl._2);

                    result = result.append(Tuple.of(Vector.of(cachedModl._1.getFilename()), Vector.of(cachedModl._2), loadSet.getPair()));

                    // Add the cache misses to the cache for next time
                    cache.put(cachedModl._1.getFilename(), cachedModl._2);
                } else {
                    // Its either not cached or not force-loaded
                    // Map the filenames to the contents of the files, or Error
                    final Tuple2<StarLoadExtractor.FileSpec, String> contents = Util.getFileContents.apply(spec);
                    if (contents != null) {
                        final Modl modl = interpreter.apply(contents._2);

                        result = result.append(Tuple.of(Vector.of(contents._1.getFilename()), Vector.of(modl), loadSet.getPair()));
                    }
                }
            }

            //
            // TODO: If any load returns an error AND we have a cached copy then use the cached copy for up to 7 days.
            //

        }

        return result;
    }

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param s argument 1
     * @return the result of function application
     */
    public Structure apply(final Structure s) {

        if (s instanceof Pair) {
            final Pair p = (Pair) s;
            if (StarLoadExtractor.isLoadInstruction(p.getKey())) {
                // Each tuple in this list holds the original Pair with the `*load` statements and the set of Modl objects
                // loaded using the filename[s] specified in the file list - there can be 1 or several.
                final Vector<Tuple3<Vector<String>, Vector<Modl>, Pair>> loadedModlObjects = convertFilesToModlObjectsAndPairs(extractFilenamesAndPairs
                        .apply(p));

                // Record which files were loaded - for use in a `%*load` reference
                ctx.addFilesLoaded(loadedModlObjects.flatMap(tuple -> tuple._1));


                return new StarLoadMutator(loadedModlObjects).accept(p);
            }
        }
        return s;
    }

    /**
     * Build a new copy of the Modl object with some pairs replaced
     */
    @AllArgsConstructor
    private static class StarLoadMutator {

        private final Vector<Tuple3<Vector<String>, Vector<Modl>, Pair>> loadedModlObjects;

        public Pair accept(final Pair pair) {
            // Find the last matching loaded object (last because the earlier ones might be overridden by later ones)
            final Option<Tuple3<Vector<String>, Vector<Modl>, Pair>> maybeFoundPair = loadedModlObjects.findLast(tuple3 -> pair.equals(tuple3._3));

            // Create a new Modl object with the updated pair.
            return maybeFoundPair.map(p -> replace(pair, p))
                    .getOrElse(pair);
        }

        /**
         * Replace any *load commands with their contents
         *
         * @param p           the current Modl object
         * @param replacement the pair to be replaced and the set of Modl objects loaded from the files.
         * @return a new Modl object with the relevant changes, sharing existing objects where possible
         */
        private Pair replace(final Pair p, final Tuple3<Vector<String>, Vector<Modl>, Pair> replacement) {

            if (p.equals(replacement._3)) {

                final Vector<ArrayItem> arrayItems = replacement._2.flatMap(m -> m.getStructures()
                        .map(structure -> (ArrayItem) structure));

                return new Pair(p.getKey(), new Array(arrayItems));
            } else {
                return p;
            }
        }

    }

}

