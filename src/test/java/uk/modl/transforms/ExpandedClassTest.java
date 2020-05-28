package uk.modl.transforms;

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
                new Array(Vector.of(new StringPrimitive("a"), new StringPrimitive("b"))),
                new Array(Vector.of(new StringPrimitive("a"), new StringPrimitive("b"), new StringPrimitive("c")))
        );

        final Vector<Pair> pairs = Vector.of(
                new Pair("p1", new StringPrimitive("p1_value")),
                new Pair("p2", new StringPrimitive("p2_value")),
                new Pair("p3", new StringPrimitive("p3_value"))
        );
        final StarClassTransform.ClassInstruction ci = of("test1", "name", "super", assign, pairs);

        final Vector<ArrayItem> assign2 = Vector.of(
                new Array(Vector.of(new StringPrimitive("d"), new StringPrimitive("e"))),
                new Array(Vector.of(new StringPrimitive("d"), new StringPrimitive("e"), new StringPrimitive("f")))
        );

        final Vector<Pair> pairs2 = Vector.of(
                new Pair("p4", new StringPrimitive("p4_value")),
                new Pair("p5", new StringPrimitive("p5_value")),
                new Pair("p6", new StringPrimitive("p6_value"))
        );
        final StarClassTransform.ClassInstruction superclass = of("super", "super", "str", assign2, pairs2);

        final TransformationContext ctx = TransformationContext.emptyCtx();

        ctx.addClassInstruction(superclass);

        final ExpandedClass expandedClass = ExpandedClass.of(ctx, ci, "str");
        assertEquals("ClassExpansionTransform.ExpandedClass(id=test1, name=name, superclass=str, assigns=Vector(Vector(a, b), Vector(a, b, c), Vector(d, e), Vector(d, e, f)), pairs=Vector(Pair(key=p1, value=p1_value), Pair(key=p2, value=p2_value), Pair(key=p3, value=p3_value), Pair(key=p4, value=p4_value), Pair(key=p5, value=p5_value), Pair(key=p6, value=p6_value)))", expandedClass.toString());
    }

    @Test
    public void testMinimalClassInstrcution() {
        final Vector<ArrayItem> assign = Vector.empty();

        final Vector<Pair> pairs = Vector.empty();
        final StarClassTransform.ClassInstruction ci = of("test2", null, null, assign, pairs);


        final TransformationContext ctx = TransformationContext.emptyCtx();

        final ExpandedClass expandedClass = ExpandedClass.of(ctx, ci, null);
        assertEquals("ClassExpansionTransform.ExpandedClass(id=test2, name=test2, superclass=null, assigns=Vector(), pairs=Vector())", expandedClass.toString());
    }

}