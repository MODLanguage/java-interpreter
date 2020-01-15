package uk.modl.transforms;

import io.vavr.Function1;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import uk.modl.extractors.StarLoadExtractor;
import uk.modl.interpreter.Interpreter;
import uk.modl.model.Modl;
import uk.modl.model.Pair;
import uk.modl.utils.Util;

public class StarLoadTransform implements Function1<Modl, Modl> {
    private static final Interpreter interpreter = new Interpreter();
    /**
     * Function to extract filenames and pairs from a Modl object.
     */
    private static Function1<Modl, List<Tuple2<List<String>, Pair>>> extractFilenamesAndPairs = (m) -> {
        final StarLoadExtractor starLoadExtractor = new StarLoadExtractor();
        m.visit(starLoadExtractor);

        return starLoadExtractor.getFilenamePairs();
    };
    /**
     * Function to convert filenames and pairs to Either Strings/Modl-objects and Pairs.
     */
    private static Function1<List<Tuple2<List<String>, Pair>>, List<Tuple2<List<Modl>, Pair>>> convertFilesToModlObjectsAndPairs = (list) -> {

        List<Tuple2<List<Modl>, Pair>> result = List.empty();

        for (final Tuple2<List<String>, Pair> tuple : list) {

            // Each tuple has a list of filenames
            final List<String> filenames = tuple._1;

            // Map the filenames to the contents of the files, or Error
            final List<String> contents = filenames.map(Util.getFileContents);

            // Interpret each MODL string from each file
            final List<Modl> modlObjects = contents
                    .map(interpreter);

            result = result.append(Tuple.of(modlObjects, tuple._2));

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

        return modl;// TODO: Return the modified Modl object rather than the input object.
    }

}
