package uk.modl.transforms;

import io.vavr.collection.HashMap;
import io.vavr.collection.Vector;
import org.junit.Test;
import uk.modl.model.Array;
import uk.modl.model.ArrayItem;
import uk.modl.model.Pair;
import uk.modl.model.StringPrimitive;
import uk.modl.transforms.ClassExpansionTransform.ExpandedClass;

import static org.junit.Assert.*;
import static uk.modl.transforms.StarClassTransform.ClassInstruction.*;

public class ExpandedClassTest {

    @Test
    public void testSuperclassStructure() {
        final Vector<ArrayItem> assign = Vector.of(
                Array.of(Vector.of(StringPrimitive.of("a"), StringPrimitive.of("b"))),
                Array.of(Vector.of(StringPrimitive.of("a"), StringPrimitive.of("b"), StringPrimitive.of("c")))
        );

        final HashMap<String, Pair> pairs = HashMap.of(
                "p1", Pair.of("p1", StringPrimitive.of("p1_value")),
                "p2", Pair.of("p2", StringPrimitive.of("p2_value")),
                "p3", Pair.of("p3", StringPrimitive.of("p3_value"))
        );
        final StarClassTransform.ClassInstruction ci = of("test1", "name", "super", assign, pairs);

        final Vector<ArrayItem> assign2 = Vector.of(
                Array.of(Vector.of(StringPrimitive.of("d"), StringPrimitive.of("e"))),
                Array.of(Vector.of(StringPrimitive.of("d"), StringPrimitive.of("e"), StringPrimitive.of("f")))
        );

        final HashMap<String, Pair> pairs2 = HashMap.of(
                "p4", Pair.of("p4", StringPrimitive.of("p4_value")),
                "p5", Pair.of("p5", StringPrimitive.of("p5_value")),
                "p6", Pair.of("p6", StringPrimitive.of("p6_value"))
        );
        final StarClassTransform.ClassInstruction superclass = of("super", "super", "str", assign2, pairs2);

        final TransformationContext ctx = TransformationContext.emptyCtx()
                .addClassInstruction(superclass);

        final ExpandedClass expandedClass = ExpandedClass.of(ctx, ci, "str");
        assertEquals("ClassExpansionTransform.ExpandedClass(id=test1, name=name, superclass=str, assigns=Vector(Vector(a, b), Vector(a, b, c), Vector(d, e), Vector(d, e, f)), pairs=Vector(Pair(key=p1, value=p1_value), Pair(key=p2, value=p2_value), Pair(key=p3, value=p3_value), Pair(key=p4, value=p4_value), Pair(key=p5, value=p5_value), Pair(key=p6, value=p6_value)))", expandedClass.toString());
    }

    @Test
    public void testMinimalClassInstruction() {
        final Vector<ArrayItem> assign = Vector.empty();

        final HashMap<String, Pair> pairs = HashMap.empty();
        final StarClassTransform.ClassInstruction ci = of("test2", null, null, assign, pairs);


        final TransformationContext ctx = TransformationContext.emptyCtx();

        final ExpandedClass expandedClass = ExpandedClass.of(ctx, ci, null);
        assertEquals("ClassExpansionTransform.ExpandedClass(id=test2, name=test2, superclass=null, assigns=Vector(), pairs=Vector())", expandedClass.toString());
    }

}