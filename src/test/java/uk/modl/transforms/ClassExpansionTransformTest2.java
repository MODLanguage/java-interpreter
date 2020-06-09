package uk.modl.transforms;

import io.vavr.collection.HashMap;
import io.vavr.collection.Vector;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.model.Map;
import uk.modl.model.Pair;
import uk.modl.model.StringPrimitive;

public class ClassExpansionTransformTest2 {

    @Test
    public void testAssigns() {
        final TransformationContext ctx = addClasses(TransformationContext.emptyCtx());

        final ClassExpansionTransform transform = new ClassExpansionTransform();

        final Pair updatedPair = (Pair) transform.apply(ctx, Pair.of("d", Map.of(Vector.of(Pair.of("test1", StringPrimitive.of("test2"))))));
        Assert.assertNotNull(updatedPair);
        Assert.assertEquals("delta", updatedPair.getKey());
        Assert.assertTrue(updatedPair.getValue() instanceof Map);

        final Map m = (Map) updatedPair.getValue();

        Assert.assertEquals(5, m.getMapItems()
                .size());
    }

    private TransformationContext addClasses(TransformationContext ctx) {
        {
            final io.vavr.collection.Map<String, Pair> pairs = HashMap.of("v", Pair.of("v", StringPrimitive.of("victor")));

            final StarClassTransform.ClassInstruction classInstruction = StarClassTransform.ClassInstruction.of("a", "alpha", "map", Vector.empty(), pairs);
            ctx = ctx.addClassInstruction(classInstruction);
        }
        {
            final io.vavr.collection.Map<String, Pair> pairs = HashMap.of("w", Pair.of("w", StringPrimitive.of("whisky")));

            final StarClassTransform.ClassInstruction classInstruction = StarClassTransform.ClassInstruction.of("b", "bravo", "alpha", Vector.empty(), pairs);
            ctx = ctx.addClassInstruction(classInstruction);
        }
        {
            final io.vavr.collection.Map<String, Pair> pairs = HashMap.of("x", Pair.of("x", StringPrimitive.of("xray")));

            final StarClassTransform.ClassInstruction classInstruction = StarClassTransform.ClassInstruction.of("c", "charlie", "bravo", Vector.empty(), pairs);
            ctx = ctx.addClassInstruction(classInstruction);
        }
        {
            final io.vavr.collection.Map<String, Pair> pairs = HashMap.of("y", Pair.of("y", StringPrimitive.of("yankee")));

            final StarClassTransform.ClassInstruction classInstruction = StarClassTransform.ClassInstruction.of("d", "delta", "charlie", Vector.empty(), pairs);
            ctx = ctx.addClassInstruction(classInstruction);
        }
        return ctx;
    }

}