package uk.modl.transforms;

import io.vavr.collection.Vector;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.model.*;

public class ClassExpansionTransformTest2 {
    @Test
    public void testAssigns() {
        final TransformationContext ctx = new TransformationContext();
        final Vector<ArrayItem> assign = Vector.of(new Array(Vector.of(new StringPrimitive("first"), new StringPrimitive("second"), new StringPrimitive("third"))));
        final Vector<Pair> pairs = Vector.of(new Pair("item1", new StringPrimitive("value1")), new Pair("item2", new StringPrimitive("value2")), new Pair("item3", new StringPrimitive("value3")));

        final StarClassTransform.ClassInstruction classInstruction = new StarClassTransform.ClassInstruction("id", "name", "map", assign, pairs);
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