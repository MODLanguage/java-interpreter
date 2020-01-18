package uk.modl.lenses;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import lombok.RequiredArgsConstructor;
import uk.modl.model.MapItem;
import uk.modl.utils.PositiveInt;

@RequiredArgsConstructor
public class MapItemListLens implements Lens<List<MapItem>, MapItem> {
    private final PositiveInt i;

    @Override
    public Tuple2<List<MapItem>, MapItem> set(final List<MapItem> mapItems, final MapItem mapItem) {
        if (i.value < mapItems.size()) {
            final MapItem replaced = mapItems.get(i.value);
            if (mapItem != null) {
                final List<MapItem> result = mapItems.update(i.value, mapItem);
                return Tuple.of(result, replaced);
            } else {
                final List<MapItem> result = mapItems.remove(replaced);
                return Tuple.of(result, replaced);
            }
        }

        return Tuple.of(mapItems, mapItem);
    }

    @Override
    public MapItem get(final List<MapItem> mapItems) {
        if (mapItems.size() <= i.value) {
            return null;
        }
        return mapItems.get(i.value);
    }
}
