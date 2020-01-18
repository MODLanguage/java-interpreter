package uk.modl.lenses;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import uk.modl.model.Modl;
import uk.modl.model.Structure;

public class ModlLens implements Lens<Modl, List<Structure>> {
    /**
     * Applies this function to no arguments and returns the result.
     *
     * @return the result of function application
     */
    @Override
    public List<Structure> get(final Modl m) {
        return m.structures;
    }

    @Override
    public Tuple2<Modl, List<Structure>> set(final Modl modl, final List<Structure> structures) {
        if (structures != null) {
            return Tuple.of(new Modl(structures), modl.structures);
        } else {
            return Tuple.of(new Modl(List.empty()), modl.structures);
        }
    }

}
