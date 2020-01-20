package uk.modl.lenses;

import uk.modl.model.Pair;
import uk.modl.model.PairValue;

public class PairValueLens implements Lens<Pair, PairValue, Pair, PairValue> {
    @Override
    public PairValue getAFromS(final Pair pair) {
        return pair.value;
    }

    @Override
    public Pair getTFromB(final Pair pair, final PairValue pairValue) {
        return new Pair(pair.key, pairValue);
    }

    @Override
    public PairValue getBFromT(final Pair pair) {
        return pair.value;
    }

    @Override
    public Pair getSFromA(final Pair pair, final PairValue pairValue) {
        return new Pair(pair.key, pairValue);
    }
}
