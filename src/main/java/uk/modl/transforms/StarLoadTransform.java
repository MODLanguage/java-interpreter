package uk.modl.transforms;

import io.vavr.Function1;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.control.Either;
import uk.modl.error.Error;
import uk.modl.extractors.StarLoadExtractor;
import uk.modl.interpreter.Interpreter;
import uk.modl.model.Modl;
import uk.modl.model.Pair;
import uk.modl.utils.Util;

public class StarLoadTransform implements Function1<Either<Error, Modl>, Either<Error, Modl>> {
    private static Interpreter interpreter = new Interpreter();

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
    private static Function1<List<Tuple2<List<String>, Pair>>, List<Tuple2<Either<Error, Modl>, Pair>>> convertFilesToModlObjectsAndPairs = (list) -> List.ofAll(
            list.map(tuple -> {
                final List<String> filenames = tuple._1;

                final String allFileContents = filenames.map(Util.getFileContents)
                        .map(Either::get)
                        .mkString(";");

                final Either<Error, Modl> filesAsModl = interpreter.apply(allFileContents);

                return Tuple.of(filesAsModl, tuple._2);
            }));

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param modl argument 1
     * @return the result of function application
     */
    @Override
    public Either<Error, Modl> apply(final Either<Error, Modl> modl) {
        final Either<Error, List<Tuple2<Either<Error, Modl>, Pair>>> modlObjectsAndPairs = modl.map(extractFilenamesAndPairs)
                .map(convertFilesToModlObjectsAndPairs);

        return modl;
    }

}
