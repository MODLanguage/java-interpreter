package uk.modl.lenses;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import uk.modl.model.Map;
import uk.modl.model.MapItem;

public class MapLens implements Lens<Map, List<MapItem>> {
    /**
     * Applies this function to no arguments and returns the result.
     *
     * @return the result of function application
     */
    @Override
    public List<MapItem> get(final Map m) {
        return m.mapItems;
    }

    @Override
    public Tuple2<Map, List<MapItem>> set(final Map m, final List<MapItem> mapItems) {
        if (mapItems != null) {
            return Tuple.of(new Map(mapItems), m.mapItems);
        } else {
            return Tuple.of(new Map(List.empty()), m.mapItems);
        }
    }

}
