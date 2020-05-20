package uk.modl.transforms;

import io.vavr.collection.Vector;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.model.Map;
import uk.modl.model.Pair;
import uk.modl.model.StringPrimitive;

public class ClassExpansionTransformTest2 {
    @Test
    public void testAssigns() {
        final TransformationContext ctx = new TransformationContext();
        addClasses(ctx);

        final ClassExpansionTransform transform = new ClassExpansionTransform(ctx);

        final Pair updatedPair = (Pair) transform.apply(new Pair("d", new Map(Vector.of(new Pair("test1", new StringPrimitive("test2"))))));
        Assert.assertNotNull(updatedPair);
        Assert.assertEquals("delta", updatedPair.getKey());
        Assert.assertTrue(updatedPair.getValue() instanceof Map);

        final Map m = (Map) updatedPair.getValue();

        Assert.assertEquals(5, m.getMapItems()
                .size());
    }

    private void addClasses(final TransformationContext ctx) {
        {
            final Vector<Pair> pairs = Vector.of(new Pair("v", new StringPrimitive("victor")));

            final StarClassTransform.ClassInstruction classInstruction = StarClassTransform.ClassInstruction.of("a", "alpha", "map", null, pairs);
            ctx.addClassInstruction(classInstruction);
        }
        {
            final Vector<Pair> pairs = Vector.of(new Pair("w", new StringPrimitive("whisky")));

            final StarClassTransform.ClassInstruction classInstruction = StarClassTransform.ClassInstruction.of("b", "bravo", "alpha", null, pairs);
            ctx.addClassInstruction(classInstruction);
        }
        {
            final Vector<Pair> pairs = Vector.of(new Pair("x", new StringPrimitive("xray")));

            final StarClassTransform.ClassInstruction classInstruction = StarClassTransform.ClassInstruction.of("c", "charlie", "bravo", null, pairs);
            ctx.addClassInstruction(classInstruction);
        }
        {
            final Vector<Pair> pairs = Vector.of(new Pair("y", new StringPrimitive("yankee")));

            final StarClassTransform.ClassInstruction classInstruction = StarClassTransform.ClassInstruction.of("d", "delta", "charlie", null, pairs);
            ctx.addClassInstruction(classInstruction);
        }
    }
}