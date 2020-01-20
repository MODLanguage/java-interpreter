package uk.modl.lenses;

import io.vavr.Tuple2;
import io.vavr.collection.List;
import org.junit.Test;
import uk.modl.model.*;
import uk.modl.parser.Parser;
import uk.modl.utils.PositiveInt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ModlLensTest {
    private static Parser parser = new Parser();

    @Test
    public void testGet() {
        final Modl modl = parser.apply("a=b;c=d;e=f");
        assertNotNull(modl);

        final ModlLens ml = new ModlLens();
        final PairInStructureListLens sll0 = new PairInStructureListLens(new PositiveInt(0));
        final PairInStructureListLens sll1 = new PairInStructureListLens(new PositiveInt(1));
        final PairInStructureListLens sll2 = new PairInStructureListLens(new PositiveInt(2));

        final Lens<Modl, Pair, Modl, Pair> lens0 = ml.then(sll0);
        final Lens<Modl, Pair, Modl, Pair> lens1 = ml.then(sll1);
        final Lens<Modl, Pair, Modl, Pair> lens2 = ml.then(sll2);

        final Structure s0 = lens0.getAFromS(modl);
        final Structure s1 = lens1.getAFromS(modl);
        final Structure s2 = lens2.getAFromS(modl);

        assertEquals(s0, modl.structures.get(0));
        assertEquals(s1, modl.structures.get(1));
        assertEquals(s2, modl.structures.get(2));

    }

    @Test
    public void testSetToValidValue() {
        final Modl modl = parser.apply("a=b;c=d;e=f;g=h");
        final Modl expected = parser.apply("a=b;g=h;e=f;g=h");

        assertNotNull(modl);
        assertNotNull(expected);

        final ModlLens ml = new ModlLens();
        final PairInStructureListLens sll1 = new PairInStructureListLens(new PositiveInt(1));

        final Lens<Modl, Pair, Modl, Pair> lens1 = ml.then(sll1);

        final Tuple2<Modl, Pair> result = lens1.set(modl, (Pair) modl.structures.get(3));

        assertEquals(expected, result._1);
        assertEquals(modl.structures.get(1), result._2);
    }

    @Test
    public void testSetToNullValue() {
        final Modl modl = parser.apply("a=b;c=d;e=f;g=h");
        final Modl expected = parser.apply("a=b;e=f;g=h");

        assertNotNull(modl);
        assertNotNull(expected);

        final ModlLens ml = new ModlLens();
        final PairInStructureListLens sll1 = new PairInStructureListLens(new PositiveInt(1));

        final Lens<Modl, Pair, Modl, Pair> lens1 = ml.then(sll1);

        final Tuple2<Modl, Pair> result = lens1.set(modl, null);

        assertEquals(expected, result._1);
        assertEquals(modl.structures.get(1), result._2);
    }

    @Test
    public void testUpdateDeepValue() {
        final Modl modl = parser.apply("x(a=b;y(c=d;z(e=f);g=h))");
        final Modl expected = parser.apply("x(a=b;y(c=d;z(e=changed);g=h))");

        assertNotNull(modl);
        assertNotNull(expected);

        final ModlLens ml = new ModlLens();
        final Lens<List<Structure>, Pair, List<Structure>, Pair> firstStructureAsPair = new PairInStructureListLens(new PositiveInt(0));
        final Lens<Pair, Map, Pair, Map> pairValueAsMap = new MapInPairLens();
        final Lens<Map, List<MapItem>, Map, List<MapItem>> mapItemsInMap = new MapLens();
        final Lens<List<MapItem>, Pair, List<MapItem>, Pair> secondMapItemAsPair = new MapItemListLens(new PositiveInt(1));
        final Lens<List<MapItem>, Pair, List<MapItem>, Pair> firstMapItemAsPair = new MapItemListLens(new PositiveInt(0));
        final Lens<Pair, PairValue, Pair, PairValue> pairValue = new PairValueLens();


        final Lens<Modl, PairValue, Modl, PairValue> lensOnPairEF = ml.then(firstStructureAsPair)
                .then(pairValueAsMap)
                .then(mapItemsInMap)
                .then(secondMapItemAsPair)
                .then(pairValueAsMap)
                .then(mapItemsInMap)
                .then(secondMapItemAsPair)
                .then(pairValueAsMap)
                .then(mapItemsInMap)
                .then(firstMapItemAsPair)
                .then(pairValue);

        final Tuple2<Modl, PairValue> result = lensOnPairEF.set(modl, new StringPrimitive("changed"));

        assertEquals(expected, result._1);
        assertEquals(new StringPrimitive("f"), result._2);

        final Tuple2<Modl, PairValue> result2 = lensOnPairEF.set(result._1, new StringPrimitive("f"));

        assertEquals(modl, result2._1);
        assertEquals(new StringPrimitive("changed"), result2._2);

        assertEquals(modl, result2._1);
    }
}