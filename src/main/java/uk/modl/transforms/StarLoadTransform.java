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
import uk.modl.model.Modl;
import uk.modl.model.Pair;
import uk.modl.model.Structure;
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

        // Each tuple in this list holds the original Pair with the `*load` statements and the set of Modl objects
        // loaded using the filename[s] specified in the file list - there can be 1 or several.
        final List<Tuple2<List<Modl>, Pair>> loadedModlObjects = loadModlObjects
                .apply(modl);

        final StarLoadMutator starLoadMutator = new StarLoadMutator(loadedModlObjects, modl);
        modl.visit(starLoadMutator);
        return starLoadMutator.getModl();
    }

}

/**
 * Build a new copy of the Modl object with some pairs replaced
 */
@AllArgsConstructor
class StarLoadMutator extends ModlVisitorBase {
    private final List<Tuple2<List<Modl>, Pair>> loadedModlObjects;

    @Getter
    private Modl modl;

    @Override
    public void accept(final Pair pair) {
        final Option<Tuple2<List<Modl>, Pair>> maybeFoundPair = loadedModlObjects.find(tuple2 -> pair.equals(tuple2._2));

        // Create a new Modl object with the updated pair.
        modl = maybeFoundPair.map(p -> replace(modl, p))
                .getOrElse(modl);
    }

    /**
     * Replace any *load commands with their contents
     *
     * @param modl        the current Modl object
     * @param replacement the pair to be replaced and the set of Modl objects loaded from the files.
     * @return a new Modl object with the relevant changes, sharing existing objects where possible
     */
    private Modl replace(final Modl modl, final Tuple2<List<Modl>, Pair> replacement) {

        //
        // TODO: This only handles *loads at the top level in a MODL file. Needs to be more general to handle them anywhere in the file, e.g. nested in maps, conditionals etc.
        //

        final List<Structure> newStructures = List.ofAll(modl.structures.flatMap(structure -> {
            if (structure.equals(replacement._2)) {
                return replacement._1.flatMap(m -> m.structures);
            } else {
                return List.of(structure);
            }
        }));

        return new Modl(newStructures);
    }

}