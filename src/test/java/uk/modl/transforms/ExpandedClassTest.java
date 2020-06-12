package uk.modl.transforms;

import io.vavr.collection.HashMap;
import io.vavr.collection.Vector;
import org.junit.Test;
import uk.modl.ancestry.Ancestry;
import uk.modl.ancestry.Parent;
import uk.modl.model.Array;
import uk.modl.model.ArrayItem;
import uk.modl.model.Pair;
import uk.modl.model.StringPrimitive;
import uk.modl.transforms.ClassExpansionTransform.ExpandedClass;

import static org.junit.Assert.*;
import static uk.modl.transforms.StarClassTransform.ClassInstruction.*;

public class ExpandedClassTest {

    final Ancestry ancestry = new Ancestry();

    final Parent parent = () -> 0;


    @Test
    public void testSuperclassStructure() {
        final Vector<ArrayItem> assign = Vector.of(
                Array.of(ancestry, parent, Vector.of(StringPrimitive.of(ancestry, parent, "a"), StringPrimitive.of(ancestry, parent, "b"))),
                Array.of(ancestry, parent, Vector.of(StringPrimitive.of(ancestry, parent, "a"), StringPrimitive.of(ancestry, parent, "b"), StringPrimitive.of(ancestry, parent, "c")))
        );

        final HashMap<String, Pair> pairs = HashMap.of(
                "p1", Pair.of(ancestry, parent, "p1", StringPrimitive.of(ancestry, parent, "p1_value")),
                "p2", Pair.of(ancestry, parent, "p2", StringPrimitive.of(ancestry, parent, "p2_value")),
                "p3", Pair.of(ancestry, parent, "p3", StringPrimitive.of(ancestry, parent, "p3_value"))
        );
        final StarClassTransform.ClassInstruction ci = of("test1", "name", "super", assign, pairs);

        final Vector<ArrayItem> assign2 = Vector.of(
                Array.of(ancestry, parent, Vector.of(StringPrimitive.of(ancestry, parent, "d"), StringPrimitive.of(ancestry, parent, "e"))),
                Array.of(ancestry, parent, Vector.of(StringPrimitive.of(ancestry, parent, "d"), StringPrimitive.of(ancestry, parent, "e"), StringPrimitive.of(ancestry, parent, "f")))
        );

        final HashMap<String, Pair> pairs2 = HashMap.of(
                "p4", Pair.of(ancestry, parent, "p4", StringPrimitive.of(ancestry, parent, "p4_value")),
                "p5", Pair.of(ancestry, parent, "p5", StringPrimitive.of(ancestry, parent, "p5_value")),
                "p6", Pair.of(ancestry, parent, "p6", StringPrimitive.of(ancestry, parent, "p6_value"))
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