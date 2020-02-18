package uk.modl.transforms;

import io.vavr.Function1;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.modl.extractors.StarLoadExtractor;
import uk.modl.interpreter.Interpreter;
import uk.modl.model.*;
import uk.modl.utils.SimpleCache;
import uk.modl.utils.Util;
import uk.modl.visitor.ModlVisitorBase;

public class StarLoadTransform implements Function1<Structure, Structure> {
    private static final Interpreter interpreter = new Interpreter();
    private static SimpleCache<String, Modl> cache = new SimpleCache<>();

    /**
     * Function to extract filenames and pairs from a Modl object.
     */
    private static Function1<Pair, List<StarLoadExtractor.LoadSet>> extractFilenamesAndPairs = (m) -> {
        final StarLoadExtractor starLoadExtractor = new StarLoadExtractor();
        m.visit(starLoadExtractor);
        return starLoadExtractor.getLoadSets();
    };

    /**
     * Function to convert filenames and pairs to Either Strings/Modl-objects and Pairs.
     */
    private static Function1<List<StarLoadExtractor.LoadSet>, List<Tuple2<List<Modl>, Pair>>> convertFilesToModlObjectsAndPairs = (list) -> {

        List<Tuple2<List<Modl>, Pair>> result = List.empty();

        for (final StarLoadExtractor.LoadSet loadSet : list) {

            // Each tuple has a list of filenames
            final List<StarLoadExtractor.FileSpec> filenames = loadSet.fileSet;

            // Partition the file specs into cache hits and those that are either cache misses or force-reloads.
            final Tuple2<List<StarLoadExtractor.FileSpec>, List<StarLoadExtractor.FileSpec>> partition = filenames.partition(spec -> cache.contains(spec.filename) && !spec.forceLoad);
            final List<StarLoadExtractor.FileSpec> cacheHits = partition._1;
            final List<StarLoadExtractor.FileSpec> cacheMisses = partition._2;

            // Map the filenames to the contents of the files, or Error
            final List<Tuple2<StarLoadExtractor.FileSpec, String>> contents = cacheMisses.map(Util.getFileContents);

            //
            // TODO: If any load returns an error AND we have a cached copy then use the cached copy for up to 7 days.
            //

            // Interpret each MODL string from each file
            final List<Tuple2<StarLoadExtractor.FileSpec, Modl>> modlObjects = contents
                    .map(filenameAndContents -> Tuple.of(filenameAndContents._1, interpreter.apply(filenameAndContents._2)));

            // Add the cache hits
            final List<Tuple2<StarLoadExtractor.FileSpec, Modl>> cachedModlObjects = cacheHits.map(spec -> Tuple.of(spec, cache.get(spec.filename)));

            result = result.append(Tuple.of(modlObjects.map(t -> t._2), loadSet.pair));
            result = result.append(Tuple.of(cachedModlObjects.map(t -> t._2), loadSet.pair));

            // Add the cache misses to the cache for next time
            modlObjects.forEach(t -> cache.put(t._1.filename, t._2));
        }

        return result;
    };

    /**
     * Load and interpret the Modl objects in the files specified by the *load statements
     */
    private static final Function1<Pair, List<Tuple2<List<Modl>, Pair>>> loadModlObjects = extractFilenamesAndPairs
            .andThen(convertFilesToModlObjectsAndPairs);

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param structure argument 1
     * @return the result of function application
     */
    @Override
    public Structure apply(final Structure structure) {

        if (structure instanceof Pair) {
            final Pair p = (Pair) structure;
            // Each tuple in this list holds the original Pair with the `*load` statements and the set of Modl objects
            // loaded using the filename[s] specified in the file list - there can be 1 or several.
            final List<Tuple2<List<Modl>, Pair>> loadedModlObjects = loadModlObjects
                    .apply(p);

            final StarLoadMutator starLoadMutator = new StarLoadMutator(loadedModlObjects, p);
            p.visit(starLoadMutator);
            return starLoadMutator.getPair();
        }
        return structure;
    }

    /**
     * Build a new copy of the Modl object with some pairs replaced
     */
    @AllArgsConstructor
    private static class StarLoadMutator extends ModlVisitorBase {
        private final List<Tuple2<List<Modl>, Pair>> loadedModlObjects;

        @Getter
        private Pair pair;

        @Override
        public void accept(final Pair pair) {
            final Option<Tuple2<List<Modl>, Pair>> maybeFoundPair = loadedModlObjects.find(tuple2 -> pair.equals(tuple2._2));

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
        private Pair replace(final Pair p, final Tuple2<List<Modl>, Pair> replacement) {

            if (p.equals(replacement._2)) {

                final List<ArrayItem> arrayItems = replacement._1.flatMap(m -> m.structures.map(structure -> (ArrayItem) structure));
                return new Pair(p.key, new Array(arrayItems));
            } else {
                return p;
            }

        }

    }
}

