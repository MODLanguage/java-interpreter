package uk.modl.transforms;

import io.vavr.collection.HashMap;
import io.vavr.collection.Vector;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.ancestry.Ancestry;
import uk.modl.ancestry.Parent;
import uk.modl.model.Map;
import uk.modl.model.Pair;
import uk.modl.model.StringPrimitive;

public class ClassExpansionTransformTest2 {

    final Ancestry ancestry = new Ancestry();

    final Parent parent = () -> 0;


    @Test
    public void testAssigns() {
        final TransformationContext ctx = addClasses(TransformationContext.emptyCtx());

        final ClassExpansionTransform transform = new ClassExpansionTransform();

        final Pair updatedPair = (Pair) transform.apply(ctx, Pair.of(ancestry, parent, "d", Map.of(ancestry, parent, Vector.of(Pair.of(ancestry, parent, "test1", StringPrimitive.of(ancestry, parent, "test2"))))));
        Assert.assertNotNull(updatedPair);
        Assert.assertEquals("delta", updatedPair.getKey());
        Assert.assertTrue(updatedPair.getValue() instanceof Map);

        final Map m = (Map) updatedPair.getValue();

        Assert.assertEquals(5, m.getMapItems()
                .size());
    }

    private TransformationContext addClasses(TransformationContext ctx) {
        {
            final io.vavr.collection.Map<String, Pair> pairs = HashMap.of("v", Pair.of(ancestry, parent, "v", StringPrimitive.of(ancestry, parent, "victor")));

            final StarClassTransform.ClassInstruction classInstruction = StarClassTransform.ClassInstruction.of("a", "alpha", "map", Vector.empty(), pairs);
            ctx = ctx.addClassInstruction(classInstruction);
        }
        {
            final io.vavr.collection.Map<String, Pair> pairs = HashMap.of("w", Pair.of(ancestry, parent, "w", StringPrimitive.of(ancestry, parent, "whisky")));

            final StarClassTransform.ClassInstruction classInstruction = StarClassTransform.ClassInstruction.of("b", "bravo", "alpha", Vector.empty(), pairs);
            ctx = ctx.addClassInstruction(classInstruction);
        }
        {
            final io.vavr.collection.Map<String, Pair> pairs = HashMap.of("x", Pair.of(ancestry, parent, "x", StringPrimitive.of(ancestry, parent, "xray")));

            final StarClassTransform.ClassInstruction classInstruction = StarClassTransform.ClassInstruction.of("c", "charlie", "bravo", Vector.empty(), pairs);
            ctx = ctx.addClassInstruction(classInstruction);
        }
        {
            final io.vavr.collection.Map<String, Pair> pairs = HashMap.of("y", Pair.of(ancestry, parent, "y", StringPrimitive.of(ancestry, parent, "yankee")));

            final StarClassTransform.ClassInstruction classInstruction = StarClassTransform.ClassInstruction.of("d", "delta", "charlie", Vector.empty(), pairs);
            ctx = ctx.addClassInstruction(classInstruction);
        }
        return ctx;
    }

}