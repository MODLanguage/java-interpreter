package uk.modl.lenses;

import io.vavr.collection.List;
import lombok.RequiredArgsConstructor;
import uk.modl.model.MapItem;
import uk.modl.model.Pair;
import uk.modl.utils.PositiveInt;

@RequiredArgsConstructor
public class MapItemListLens implements Lens<List<MapItem>, Pair, List<MapItem>, Pair> {
    private final PositiveInt i;

    @Override
    public Pair getAFromS(final List<MapItem> mapItems) {
        return (Pair) mapItems.get(i.value);
    }

    @Override
    public List<MapItem> getTFromB(final List<MapItem> mapItems, final Pair mapItem) {
        return mapItems.update(i.value, mapItem);
    }

    @Override
    public Pair getBFromT(final List<MapItem> mapItems) {
        return (Pair) mapItems.get(i.value);
    }

    @Override
    public List<MapItem> getSFromA(final List<MapItem> mapItems, final Pair mapItem) {
        return mapItems.update(i.value, mapItem);
    }
}
