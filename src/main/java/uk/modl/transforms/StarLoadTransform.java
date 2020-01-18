package uk.modl.transforms;

import io.vavr.Function1;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import uk.modl.extractors.StarLoadExtractor;
import uk.modl.interpreter.Interpreter;
import uk.modl.model.Modl;
import uk.modl.model.Pair;
import uk.modl.utils.SimpleCache;
import uk.modl.utils.Util;
import uk.modl.visitor.ModlVisitorBase;

public class StarLoadTransform implements Function1<Modl, Modl> {
    private static final Interpreter interpreter = new Interpreter();
    private static SimpleCache<String, Modl> cache = new SimpleCache<>();

    /**
     * Function to extract filenames and pairs from a Modl object.
     */
    private static Function1<Modl, List<StarLoadExtractor.LoadSet>> extractFilenamesAndPairs = (m) -> {
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
    private static final Function1<Modl, List<Tuple2<List<Modl>, Pair>>> loadModlObjects = extractFilenamesAndPairs
            .andThen(convertFilesToModlObjectsAndPairs);

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param modl argument 1
     * @return the result of function application
     */
    @Override
    public Modl apply(final Modl modl) {
        final List<Tuple2<List<Modl>, Pair>> loadedModlObjects = loadModlObjects
                .apply(modl);

        final StarLoadMutator starLoadMutator = new StarLoadMutator(loadedModlObjects);
        modl.visit(starLoadMutator);
        return starLoadMutator.getModl();
    }

}

/**
 * TODO: Build a new copy of the Modl object with some pairs replaced
 */
@RequiredArgsConstructor
class StarLoadMutator extends ModlVisitorBase {
    private final List<Tuple2<List<Modl>, Pair>> loadedModlObjects;

    @Getter
    private Modl modl;
}