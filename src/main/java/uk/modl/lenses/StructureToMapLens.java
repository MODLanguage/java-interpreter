package uk.modl.lenses;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import uk.modl.model.Map;
import uk.modl.model.Structure;

public class StructureToMapLens implements Lens<Structure, Map> {
    /**
     * Applies this function to no arguments and returns the result.
     *
     * @return the result of function application
     */
    @Override
    public Map get(final Structure m) {
        return ((Map) m);
    }

    @Override
    public Tuple2<Structure, Map> set(final Structure s, final Map m) {
        return Tuple.of(s, m);
    }

}
