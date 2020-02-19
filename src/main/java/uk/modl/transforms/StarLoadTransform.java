package uk.modl.transforms;

import io.vavr.Function1;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import uk.modl.extractors.StarLoadExtractor;
import uk.modl.interpreter.Interpreter;
import uk.modl.model.Array;
import uk.modl.model.ArrayItem;
import uk.modl.model.Modl;
import uk.modl.model.Pair;
import uk.modl.utils.SimpleCache;
import uk.modl.utils.Util;
import uk.modl.visitor.ModlVisitorBase;

import java.util.Objects;

@RequiredArgsConstructor
public class StarLoadTransform {
    private static SimpleCache<String, Modl> cache = new SimpleCache<>();
    /**
     * Function to extract filenames and pairs from a Modl object.
     */
    private static Function1<Pair, Vector<StarLoadExtractor.LoadSet>> extractFilenamesAndPairs = (m) -> {
        final StarLoadExtractor starLoadExtractor = new StarLoadExtractor();
        m.visit(starLoadExtractor);
        return starLoadExtractor.getLoadSets();
    };

    /**
     * The context for this invocation of the interpreter
     */
    @NonNull
    private TransformationContext ctx;

    /**
     * Function to convert filenames and pairs to Either Strings/Modl-objects and Pairs.
     */
    private Vector<Tuple3<Vector<String>, Vector<Modl>, Pair>> convertFilesToModlObjectsAndPairs(final Vector<StarLoadExtractor.LoadSet> list) {

        Vector<Tuple3<Vector<String>, Vector<Modl>, Pair>> result = Vector.empty();

        for (final StarLoadExtractor.LoadSet loadSet : list) {

            // Each tuple has a list of filenames
            final Vector<StarLoadExtractor.FileSpec> filenames = loadSet.fileSet;

            // Partition the file specs into cache hits and those that are either cache misses or force-reloads.
            final Tuple2<Vector<StarLoadExtractor.FileSpec>, Vector<StarLoadExtractor.FileSpec>> partition = filenames.partition(spec -> cache.contains(spec.filename) && !spec.forceLoad);
            final Vector<StarLoadExtractor.FileSpec> cacheHits = partition._1;
            final Vector<StarLoadExtractor.FileSpec> cacheMisses = partition._2;

            // Map the filenames to the contents of the files, or Error
            final Vector<Tuple2<StarLoadExtractor.FileSpec, String>> contents = cacheMisses.map(Util.getFileContents)
                    .filter(Objects::nonNull);

            //
            // TODO: If any load returns an error AND we have a cached copy then use the cached copy for up to 7 days.
            //

            // Interpret each MODL string from each file
            final Interpreter interpreter = new Interpreter();
            interpreter.setCtx(ctx);
            final Vector<Tuple2<StarLoadExtractor.FileSpec, Modl>> modlObjects = contents
                    .map(filenameAndContents -> {
                        final Modl modl = interpreter.apply(filenameAndContents._2);
                        return Tuple.of(filenameAndContents._1, modl);
                    });

            // Add the cache hits
            final Vector<Tuple2<StarLoadExtractor.FileSpec, Modl>> cachedModlObjects = cacheHits.map(spec -> Tuple.of(spec, cache.get(spec.filename)));

            // Re-interpret the cached Modl objects to extract classes, methods etc. for the current context
            cachedModlObjects.map(t -> t._2)
                    .forEach(interpreter::apply);

            final Vector<String> modlObjectFilenames = modlObjects.map(t -> t._1.filename);
            final Vector<Modl> modlObjectList = modlObjects.map(t -> t._2);

            result = result.append(Tuple.of(modlObjectFilenames, modlObjectList, loadSet.pair));

            final Vector<String> cachedModlObjectFilenames = cachedModlObjects.map(t -> t._1.filename);
            final Vector<Modl> cachedModlObjectList = cachedModlObjects.map(t -> t._2);

            result = result.append(Tuple.of(cachedModlObjectFilenames, cachedModlObjectList, loadSet.pair));

            // Add the cache misses to the cache for next time
            modlObjects.forEach(t -> cache.put(t._1.filename, t._2));
        }

        return result;
    }

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param p argument 1
     * @return the result of function application
     */
    public Pair apply(final Pair p) {

        if (StarLoadExtractor.isLoadInstruction(p.key)) {
            // Each tuple in this list holds the original Pair with the `*load` statements and the set of Modl objects
            // loaded using the filename[s] specified in the file list - there can be 1 or several.
            final Vector<Tuple3<Vector<String>, Vector<Modl>, Pair>> loadedModlObjects = convertFilesToModlObjectsAndPairs(extractFilenamesAndPairs
                    .apply(p));

            // Record which files were loaded - for use in a `%*load` reference
            ctx.addFilesLoaded(loadedModlObjects.flatMap(tuple -> tuple._1));


            final StarLoadMutator starLoadMutator = new StarLoadMutator(loadedModlObjects, p);
            p.visit(starLoadMutator);
            return starLoadMutator.getPair();
        }
        return p;
    }

    public void seCtx(final TransformationContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Build a new copy of the Modl object with some pairs replaced
     */
    @AllArgsConstructor
    private static class StarLoadMutator extends ModlVisitorBase {
        private final Vector<Tuple3<Vector<String>, Vector<Modl>, Pair>> loadedModlObjects;

        @Getter
        private Pair pair;

        @Override
        public void accept(final Pair pair) {
            final Option<Tuple3<Vector<String>, Vector<Modl>, Pair>> maybeFoundPair = loadedModlObjects.find(tuple3 -> pair.equals(tuple3._3));

            // Create a new Modl object with the updated pair.
            this.pair = maybeFoundPair.map(p -> replace(pair, p))
                    .getOrElse(this.pair);
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

                final Vector<ArrayItem> arrayItems = replacement._2.flatMap(m -> m.structures.map(structure -> (ArrayItem) structure));
                return new Pair(p.key, new Array(arrayItems));
            } else {
                return p;
            }

        }

    }
}

