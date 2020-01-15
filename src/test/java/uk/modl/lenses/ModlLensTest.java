package uk.modl.lenses;

import io.vavr.Tuple2;
import org.junit.Test;
import uk.modl.model.Modl;
import uk.modl.model.Structure;
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
        final StructureListLens sll0 = new StructureListLens(new PositiveInt(0));
        final StructureListLens sll1 = new StructureListLens(new PositiveInt(1));
        final StructureListLens sll2 = new StructureListLens(new PositiveInt(2));

        final Lens<Modl, Structure> lens0 = ml.andThenLens(sll0);
        final Lens<Modl, Structure> lens1 = ml.andThenLens(sll1);
        final Lens<Modl, Structure> lens2 = ml.andThenLens(sll2);

        final Structure s0 = lens0.get(modl);
        final Structure s1 = lens1.get(modl);
        final Structure s2 = lens2.get(modl);

        assertEquals(s0, modl.structures.get(0));
        assertEquals(s1, modl.structures.get(1));
        assertEquals(s2, modl.structures.get(2));

    }

    @Test
    public void testSet() {
        final Modl modl = parser.apply("a=b;c=d;e=f;g=h");
        final Modl expected = parser.apply("a=b;g=h;e=f;g=h");

        assertNotNull(modl);
        assertNotNull(expected);

        final ModlLens ml = new ModlLens();
        final StructureListLens sll1 = new StructureListLens(new PositiveInt(1));

        final Lens<Modl, Structure> lens1 = ml.andThenLens(sll1);

        final Tuple2<Modl, Structure> result = lens1.set(modl, modl.structures.get(3));

        assertEquals(expected, result._1);
        assertEquals(modl.structures.get(1), result._2);
    }
}