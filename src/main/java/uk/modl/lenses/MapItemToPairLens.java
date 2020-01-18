package uk.modl.lenses;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import uk.modl.model.MapItem;
import uk.modl.model.Pair;

public class MapItemToPairLens implements Lens<MapItem, Pair> {
    /**
     * Applies this function to no arguments and returns the result.
     *
     * @return the result of function application
     */
    @Override
    public Pair get(final MapItem m) {
        return ((Pair) m);
    }

    @Override
    public Tuple2<MapItem, Pair> set(final MapItem s, final Pair m) {
        return Tuple.of(s, m);
    }

}
