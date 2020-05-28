package uk.modl.transforms;

import io.vavr.collection.List;
import io.vavr.collection.Vector;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.model.*;

public class ClassExpansionTransformTest1 {
    @Test
    public void test() {
        final TransformationContext ctx = TransformationContext.emptyCtx();
        final ClassExpansionTransform transform = new ClassExpansionTransform();

        // Create the class
        final Vector<ArrayItem> assign = Vector.of(new Array(Vector.of(new StringPrimitive("one"), new StringPrimitive("two"), new StringPrimitive("three"))));
        final StarClassTransform.ClassInstruction classInstruction = StarClassTransform.ClassInstruction.of("test", null, null, assign, Vector.empty());
        ctx.addClassInstruction(classInstruction);

        // Create the Pair to be transformed by the class
        final Map pairValue = new Map(Vector.of(new Pair("one", new StringPrimitive("1")), new Pair("two", new StringPrimitive("2")), new Pair("three", new StringPrimitive("3"))));
        final Pair testPair = new Pair("test", pairValue);

        // Transform the pair
        final Pair updatedPair = (Pair) transform.apply(ctx, testPair);

        // Check the result
        Assert.assertNotNull(updatedPair);
        Assert.assertEquals("test", updatedPair.getKey());
        Assert.assertTrue(updatedPair.getValue() instanceof Map);

        final Map m = (Map) updatedPair.getValue();

        Assert.assertEquals(3, m.getMapItems()
                .size());

        final List<String> expectedKeys = List.of("one", "two", "three");
        final List<String> expectedValues = List.of("1", "2", "3");

        m.getMapItems()
                .forEach(mapItem -> {
                    Assert.assertTrue(mapItem instanceof Pair);
                    final Pair p = (Pair) mapItem;
                    Assert.assertTrue(expectedKeys.contains(p.getKey()));
                    Assert.assertTrue(p.getValue() instanceof StringPrimitive);
                    Assert.assertTrue(expectedValues.contains(p.getValue()
                            .toString()));
                });
    }
}