package uk.modl.lenses;

import uk.modl.model.Map;
import uk.modl.model.Pair;

public class MapInPairLens implements Lens<Pair, Map, Pair, Map> {
    @Override
    public Map getAFromS(final Pair pair) {
        return (Map) pair.value;
    }

    @Override
    public Map getBFromA(final Map map) {
        return map;
    }

    @Override
    public Pair getTFromB(final Pair pair, final Map map) {
        return new Pair(pair.key, map);
    }

    @Override
    public Map getBFromT(final Pair pair) {
        return (Map) pair.value;
    }

    @Override
    public Map getAFromB(final Map map) {
        return map;
    }

    @Override
    public Pair getSFromA(final Pair pair, final Map map) {
        return new Pair(pair.key, map);
    }

    @Override
    public Pair getTFromS(final Pair pair) {
        return pair;
    }

    @Override
    public Pair getSFromT(final Pair pair) {
        return pair;
    }
}
