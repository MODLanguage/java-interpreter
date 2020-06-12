package uk.modl.ancestry;

import io.vavr.control.Option;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.model.Pair;
import uk.modl.model.StringPrimitive;
import uk.modl.model.TruePrimitive;
import uk.modl.utils.IDSource;

public class AncestryTest {

    @Test
    public void testReplaceNode_1() {
        IDSource.reset();
        final Ancestry a = new Ancestry();

        final MockPC[] pc = getMockPCS(6);

        a.add(pc[1], pc[2]);
        a.add(pc[1], pc[3]);
        a.add(pc[1], pc[4]);

//        a.dump();

        a.replaceChild(pc[4], pc[5]);

//        System.out.println();

//        a.dump();
    }

    private MockPC[] getMockPCS(final int n) {
        final MockPC[] pc = new MockPC[n];
        for (int i = 0; i < n; i++) {
            pc[i] = new MockPC();
        }
        return pc;
    }

    @Test
    public void testReplaceNode_2() {
        IDSource.reset();
        final Ancestry a = new Ancestry();

        final MockPC[] pc = getMockPCS(9);

        a.add(pc[1], pc[2]);
        a.add(pc[1], pc[3]);
        a.add(pc[2], pc[4]);
        a.add(pc[2], pc[5]);
        a.add(pc[5], pc[6]);
        a.add(pc[5], pc[7]);

//        a.dump();

        a.replaceChild(pc[2], pc[8]);

//        System.out.println();

//        a.dump();
    }

    @Test
    public void testReplaceNode_3() {
        IDSource.reset();
        final Ancestry a = new Ancestry();

        final MockPC[] pc = getMockPCS(15);

        a.add(pc[0], pc[1]);
        a.add(pc[1], pc[2]);
        a.add(pc[1], pc[3]);
        a.add(pc[2], pc[4]);
        a.add(pc[2], pc[5]);
        a.add(pc[5], pc[6]);
        a.add(pc[5], pc[7]);

        a.add(pc[8], pc[9]);
        a.add(pc[8], pc[10]);
        a.add(pc[9], pc[11]);
        a.add(pc[9], pc[12]);
        a.add(pc[10], pc[13]);
        a.add(pc[10], pc[14]);

//        a.dump();

        a.replaceChild(pc[2], pc[8]);

//        System.out.println();

//        a.dump();

        a.replaceChild(pc[10], pc[9]);

//        System.out.println();

//        a.dump();
    }

    @Test
    public void testReplaceNodeUsingFind_1() {
        IDSource.reset();
        final Ancestry a = new Ancestry();

        final Pair root = Pair.of(a, null, "root", TruePrimitive.instance);
        final Pair one = Pair.of(a, root, "one", TruePrimitive.instance);
        final Pair two = Pair.of(a, root, "two", TruePrimitive.instance);
        final Pair three = Pair.of(a, one, "three", TruePrimitive.instance);
        final Pair four = Pair.of(a, one, "four", TruePrimitive.instance);
        final Pair five = Pair.of(a, four, "five", TruePrimitive.instance);
        final Pair six = Pair.of(a, four, "six", TruePrimitive.instance);

//        a.dump();

        final Option<Pair> maybeOne = a.findByKey(five, "one");
        Assert.assertTrue(maybeOne.isDefined());
        Assert.assertEquals(one, maybeOne.get());

        final Option<Pair> maybeFour = a.findByKey(five, "four");
        Assert.assertTrue(maybeFour.isDefined());
        Assert.assertEquals(four, maybeFour.get());

        a.replaceChild(maybeOne.get(), maybeFour.get());
//        System.out.println();
//        a.dump();

        a.replaceChild(root, six);
//        System.out.println();
//        a.dump();
    }

    @Test
    public void testChangeParent() {
        IDSource.reset();
        final Ancestry a = new Ancestry();

        final Pair root = Pair.of(a, null, "root", TruePrimitive.instance);
        final Pair one = Pair.of(a, root, "one", TruePrimitive.instance);
        final Pair two = Pair.of(a, root, "two", TruePrimitive.instance);
        final Pair three = Pair.of(a, one, "three", TruePrimitive.instance);
        final Pair four = Pair.of(a, one, "four", TruePrimitive.instance);
        final Pair five = Pair.of(a, four, "five", TruePrimitive.instance);
        final Pair six = Pair.of(a, four, "six", TruePrimitive.instance);

//        a.dump();

        a.add(root, six);

//        System.out.println();
//        a.dump();
    }

    @Test
    public void testReplaceNodeWithDuplicatedIds() {
        IDSource.reset();
        final Ancestry a = new Ancestry();

        final Pair root = Pair.of(a, null, "root", TruePrimitive.instance);
        final Pair one = Pair.of(a, root, "one", TruePrimitive.instance);
        final Pair two = Pair.of(a, root, "two", TruePrimitive.instance);
        final Pair three = Pair.of(a, one, "three", TruePrimitive.instance);
        final Pair four = Pair.of(a, one, "four", TruePrimitive.instance);
        final Pair five = Pair.of(a, four, "five", TruePrimitive.instance);
        final Pair six = Pair.of(a, four, "six", TruePrimitive.instance);

        a.dump();

        final StringPrimitive s = StringPrimitive.of(a, four, "Child of 4");
        final StringPrimitive s2 = StringPrimitive.of(a, four, "Another Child of 4");

        System.out.println();
        a.dump();

        final Pair four_2 = four.with(a, "four_2", s);

        System.out.println();
        a.dump();
    }

}