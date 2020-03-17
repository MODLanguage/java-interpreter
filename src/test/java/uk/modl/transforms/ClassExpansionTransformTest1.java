package uk.modl.transforms;

import io.vavr.collection.Vector;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.model.Map;
import uk.modl.model.Pair;

public class ClassExpansionTransformTest1 {
    @Test
    public void test() {
        final TransformationContext ctx = new TransformationContext();

        final StarClassTransform.ClassInstruction classInstruction = new StarClassTransform.ClassInstruction("id", "name", "map", null, null);
        ctx.addClassInstruction(classInstruction);

        final ClassExpansionTransform transform = new ClassExpansionTransform(ctx);

        final Pair updatedPair = transform.apply(new Pair("id", new Map(Vector.empty())));
        Assert.assertNotNull(updatedPair);
        Assert.assertEquals("name", updatedPair.key);
        Assert.assertTrue(updatedPair.value instanceof Map);

        final Map m = (Map) updatedPair.value;

        Assert.assertEquals(6, m.mapItems.size());
    }
}