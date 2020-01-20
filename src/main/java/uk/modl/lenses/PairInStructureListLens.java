package uk.modl.lenses;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import lombok.RequiredArgsConstructor;
import uk.modl.model.Pair;
import uk.modl.model.Structure;
import uk.modl.utils.PositiveInt;

@RequiredArgsConstructor
public class PairInStructureListLens implements Lens<List<Structure>, Pair, List<Structure>, Pair> {
    private final PositiveInt i;

    @Override
    public Pair getAFromS(final List<Structure> structures) {
        return (Pair) structures.get(i.value);
    }

    @Override
    public List<Structure> getTFromB(final List<Structure> structures, final Pair structure) {
        return structures.update(i.value, structure);
    }

    @Override
    public Pair getBFromT(final List<Structure> structures) {
        return (Pair) structures.get(i.value);
    }

    @Override
    public List<Structure> getSFromA(final List<Structure> structures, final Pair structure) {
        return structures.update(i.value, structure);
    }

    @Override
    public Tuple2<List<Structure>, Pair> set(final List<Structure> structures, final Pair structure) {
        if (structure == null) {
            return Tuple.of(structures.removeAt(i.value), getBFromA(getAFromS(structures)));
        } else {
            return Lens.super.set(structures, structure);
        }
    }
}
