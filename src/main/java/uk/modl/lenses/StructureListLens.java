package uk.modl.lenses;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import lombok.RequiredArgsConstructor;
import uk.modl.model.Structure;
import uk.modl.utils.PositiveInt;

@RequiredArgsConstructor
public class StructureListLens implements Lens<List<Structure>, Structure> {
    private final PositiveInt i;

    @Override
    public Tuple2<List<Structure>, Structure> set(final List<Structure> structures, final Structure structure) {
        if (i.value < structures.size()) {
            final Structure replaced = structures.get(i.value);
            if (structure != null) {
                final List<Structure> result = structures.update(i.value, structure);
                return Tuple.of(result, replaced);
            } else {
                final List<Structure> result = structures.remove(replaced);
                return Tuple.of(result, replaced);
            }
        }

        return Tuple.of(structures, structure);
    }

    @Override
    public Structure get(final List<Structure> structures) {
        if (structures.size() <= i.value) {
            return null;
        }
        return structures.get(i.value);
    }
}
