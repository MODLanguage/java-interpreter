package uk.modl.model;

public class Pair implements Structure, MapItem, ValueItem, ArrayItem {
    public final String key;
    public final PairValue value;

    public Pair(final String key, final PairValue value) {
        this.key = key;
        this.value = value;
    }
}
