package uk.modl.lenses;

import io.vavr.collection.List;
import uk.modl.model.Modl;
import uk.modl.model.Structure;

public class ModlLens implements Lens<Modl, List<Structure>, Modl, List<Structure>> {
    @Override
    public List<Structure> getAFromS(final Modl modl) {
        return modl.structures;
    }

    @Override
    public List<Structure> getBFromA(final List<Structure> structures) {
        return structures;
    }

    @Override
    public Modl getTFromB(final Modl modl, final List<Structure> structures) {
        return new Modl(structures);
    }

    @Override
    public List<Structure> getBFromT(final Modl modl) {
        return modl.structures;
    }

    @Override
    public List<Structure> getAFromB(final List<Structure> structures) {
        return structures;
    }

    @Override
    public Modl getSFromA(final Modl modl, final List<Structure> structures) {
        return new Modl(structures);
    }

    @Override
    public Modl getTFromS(final Modl modl) {
        return modl;
    }

    @Override
    public Modl getSFromT(final Modl modl) {
        return modl;
    }
}
