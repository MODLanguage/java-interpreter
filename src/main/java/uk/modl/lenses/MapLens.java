package uk.modl.lenses;

import io.vavr.collection.List;
import uk.modl.model.Map;
import uk.modl.model.MapItem;

public class MapLens implements Lens<Map, List<MapItem>, Map, List<MapItem>> {
    @Override
    public List<MapItem> getAFromS(final Map map) {
        return map.mapItems;
    }

    @Override
    public Map getTFromB(final Map map, final List<MapItem> mapItems) {
        return new Map(mapItems);
    }

    @Override
    public List<MapItem> getBFromT(final Map map) {
        return map.mapItems;
    }

    @Override
    public Map getSFromA(final Map map, final List<MapItem> mapItems) {
        return new Map(mapItems);
    }
}
