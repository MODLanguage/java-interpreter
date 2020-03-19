package uk.modl.interpreter;

import io.vavr.Function1;
import io.vavr.Tuple2;
import io.vavr.collection.Vector;
import uk.modl.model.*;
import uk.modl.transforms.*;

/**
 * Interpreter for a Modl object
 */
public class InterpreterVisitor implements Function1<Modl, Modl> {

    private final StarLoadTransform starLoadTransform;
    private final StarClassTransform starClassTransform;
    private final StarMethodTransform starMethodTransform;
    private final ReferencesTransform referencesTransform;
    private final ConditionalsTransform conditionalsTransform;
    private final ClassExpansionTransform classExpansionTransform;
    private final PercentStarInstructionTransform percentStarInstructionTransform;
    private final Function1<Pair, Pair> processPairs;

    /**
     * Constructor
     */
    public InterpreterVisitor() {
        final TransformationContext ctx = new TransformationContext();
        starLoadTransform = new StarLoadTransform(ctx);
        starClassTransform = new StarClassTransform(ctx);
        starMethodTransform = new StarMethodTransform(ctx);
        referencesTransform = new ReferencesTransform(ctx);
        conditionalsTransform = new ConditionalsTransform(ctx);
        classExpansionTransform = new ClassExpansionTransform(ctx);
        percentStarInstructionTransform = new PercentStarInstructionTransform(ctx);

        processPairs = percentStarInstructionTransform.compose(classExpansionTransform)
                .compose(referencesTransform)
                .compose(starMethodTransform)
                .compose(starClassTransform)
                .compose(starLoadTransform);
    }

    /**
     * Parse Structures
     *
     * @param s a Structure
     * @return a Structure
     */
    private Structure visitStructure(final Structure s) {

        if (s instanceof Map) {
            return visitMap((Map) s);
        } else if (s instanceof Array) {
            return visitArray((Array) s);
        } else if (s instanceof Pair) {
            return visitPair((Pair) s);
        } else if (s instanceof TopLevelConditional) {
            return visitTopLevelConditional((TopLevelConditional) s);
        }
        return s;
    }

    /**
     * Parse a TopLevelConditional
     *
     * @param tlc the TopLevelConditional
     * @return a TopLevelConditional
     */
    private TopLevelConditional visitTopLevelConditional(final TopLevelConditional tlc) {

        final Vector<ConditionTest> tests = tlc.tests
                .map(this::visitConditionTest);

        final Vector<TopLevelConditionalReturn> returns = tlc.returns
                .map(this::visitTopLevelConditionalReturn);

        final TopLevelConditional newTlc = new TopLevelConditional(tests, returns);

        return conditionalsTransform.apply(newTlc);
    }

    /**
     * Parse a MapConditional
     *
     * @param mc the MapConditional
     * @return a MapConditional
     */
    private MapConditional visitMapConditional(final MapConditional mc) {

        final Vector<ConditionTest> tests = mc.tests
                .map(this::visitConditionTest);

        final Vector<MapConditionalReturn> returns = mc.returns
                .map(this::visitMapConditionalReturn);

        final MapConditional mapConditional = new MapConditional(tests, returns);
        return conditionalsTransform.apply(mapConditional);

    }

    /**
     * Parse a MapConditionalReturn
     *
     * @param mcr the MapConditionalReturn
     * @return a MapConditionalReturn
     */
    private MapConditionalReturn visitMapConditionalReturn(final MapConditionalReturn mcr) {

        final Vector<MapItem> items = mcr.items
                .map(this::visitMapItem);
        return new MapConditionalReturn(items);
    }

    /**
     * Parse a TopLevelConditionalReturn
     *
     * @param tlcr the TopLevelConditionalReturn
     * @return a TopLevelConditionalReturn
     */
    private TopLevelConditionalReturn visitTopLevelConditionalReturn(final TopLevelConditionalReturn tlcr) {

        final Vector<Structure> structures = tlcr.structures
                .map(this::visitStructure);

        return new TopLevelConditionalReturn(structures);
    }

    /**
     * Parse a ConditionTest
     *
     * @param ct the ConditionTest
     * @return a ConditionTest
     */
    private ConditionTest visitConditionTest(final ConditionTest ct) {
        final Vector<Tuple2<ConditionOrConditionGroupInterface, String>> newConditions = ct.conditions.map(c -> {
            if (c._1 instanceof Condition) {
                return c.update1(visitCondition((Condition) c._1));
            } else {
                return c.update1(visitConditionGroup((ConditionGroup) c._1));
            }
        });
        return new ConditionTest(newConditions);
    }

    /**
     * Parse a ConditionGroup
     *
     * @param cg the ConditionGroup
     * @return a ConditionGroup
     */
    private ConditionGroup visitConditionGroup(final ConditionGroup cg) {
        final Vector<Tuple2<ConditionTest, String>> subConditionList = cg.subConditionList.map(t -> t.update1(visitConditionTest(t._1)));
        return new ConditionGroup(subConditionList, cg.shouldNegate);
    }

    /**
     * Parse a Condition
     *
     * @param c the Condition
     * @return a Condition
     */
    private Condition visitCondition(final Condition c) {
        final Condition c2 = referencesTransform.apply(c);

        final ValueItem newLhs = visitValue(c2.lhs);
        final Vector<ValueItem> values = c2.values
                .map(this::visitValue);

        return new Condition(newLhs, c2.op, values, c2.shouldNegate);
    }

    /**
     * Parse ModlArrays
     *
     * @param arr the Array
     * @return a list of ArrayItems
     */
    private Array visitArray(final Array arr) {

        final Vector<ArrayItem> items = arr.arrayItems
                .map(this::visitArrayItem);

        return new Array(items);
    }

    /**
     * Parse ArrayItems
     *
     * @param ai the ArrayItem
     * @return a list of ArrayItems
     */
    private ArrayItem visitArrayItem(final ArrayItem ai) {

        return (ai instanceof ArrayConditional) ?
                visitArrayConditional((ArrayConditional) ai) :
                visitArrayValueItem(ai);
    }

    /**
     * Parse ArrayValue
     *
     * @param ai the ArrayItem
     * @return an ArrayItem
     */
    private ArrayItem visitArrayValueItem(final ArrayItem ai) {

        return (ai instanceof Array) ?
                visitArray((Array) ai) :
                (ai instanceof Map) ?
                        visitMap((Map) ai) :
                        (ai instanceof Pair) ?
                                visitPair((Pair) ai) :
                                (ai instanceof Primitive) ?
                                        visitPrimitive((Primitive) ai) : null;
    }

    /**
     * Parse ArrayConditionalContext
     *
     * @param ac the ArrayConditional
     * @return an ArrayItem
     */
    private ArrayConditional visitArrayConditional(final ArrayConditional ac) {

        final Vector<ConditionTest> tests = ac.tests
                .map(this::visitConditionTest);

        final Vector<ArrayConditionalReturn> returns = ac.returns
                .map(this::visitArrayConditionalReturn);

        final ArrayConditional arrayConditional = new ArrayConditional(tests, returns);
        return conditionalsTransform.apply(arrayConditional);
    }

    /**
     * Parse ArrayConditionalReturn
     *
     * @param acr the ArrayConditionalReturn
     * @return an ArrayConditionalReturn
     */
    private ArrayConditionalReturn visitArrayConditionalReturn(final ArrayConditionalReturn acr) {

        final Vector<ArrayItem> items = acr.items
                .map(this::visitArrayItem);
        return new ArrayConditionalReturn(items);
    }

    /**
     * Parse ModlMaps
     *
     * @param map the Map
     * @return a Map
     */
    private Map visitMap(final Map map) {

        final Vector<MapItem> items = map.mapItems
                .map(this::visitMapItem);

        return new Map(items);
    }

    /**
     * Parse MapItems
     *
     * @param mi the MapItem
     * @return a MapItem
     */
    private MapItem visitMapItem(final MapItem mi) {

        return (mi instanceof Pair) ?
                visitPair((Pair) mi) :
                visitMapConditional((MapConditional) mi);
    }

    /**
     * Parse a ModlPair
     *
     * @param p the Pair
     * @return a Pair
     */
    private Pair visitPair(final Pair p) {

        final Pair pair = processPairs.apply(p);
        if (pair == null) {
            return null;
        }

        PairValue value = pair.value;

        if (value instanceof Array) {
            value = visitArray((Array) value);
        } else if (value instanceof Map) {
            value = visitMap((Map) value);
        } else if (value instanceof ValueItem) {
            value = visitValueItem((ValueItem) value);
        }
        return new Pair(p.key, value);
    }

    /**
     * Parse ValueItems
     *
     * @param vi the ValueItem
     * @return a ValueItem
     */
    private ValueItem visitValueItem(final ValueItem vi) {

        if (vi instanceof ValueConditional) {
            return visitValueConditional((ValueConditional) vi);
        } else if (vi instanceof Map) {
            return visitMap((Map) vi);
        } else if (vi instanceof Array) {
            return visitArray((Array) vi);
        } else if (vi instanceof Pair) {
            return visitPair((Pair) vi);
        } else if (vi instanceof Primitive) {
            return visitPrimitive((Primitive) vi);
        }
        return vi;
    }

    /**
     * Parse ValueConditional
     *
     * @param vc the ValueConditional
     * @return a ValueItem
     */
    private ValueConditional visitValueConditional(final ValueConditional vc) {

        final Vector<ConditionTest> tests = vc.tests
                .map(this::visitConditionTest);

        final Vector<ValueConditionalReturn> returns = vc.returns
                .map(this::visitValueConditionReturn);

        final ValueConditional valueConditional = new ValueConditional(tests, returns);
        return conditionalsTransform.apply(valueConditional);
    }

    /**
     * Parse ValueConditionalReturn
     *
     * @param vcr the ValueConditionalReturn
     * @return a ValueConditionalReturn
     */
    private ValueConditionalReturn visitValueConditionReturn(final ValueConditionalReturn vcr) {

        final Vector<ValueItem> items = vcr.items
                .map(this::visitValueItem);

        return new ValueConditionalReturn(items);
    }

    /**
     * Parse Values
     *
     * @param vi the ValueItem
     * @return a Value
     */
    private ValueItem visitValue(final ValueItem vi) {

        if (vi instanceof StringPrimitive) {
            final ValueItem newValueItem = referencesTransform.apply(vi);
            return visitValueItem(newValueItem);
        }

        return (vi instanceof Array) ?
                visitArray((Array) vi) :
                (vi instanceof Map) ?
                        visitMap((Map) vi) :
                        (vi instanceof Pair) ?
                                visitPair((Pair) vi) :
                                vi;
    }

    /**
     * Parse Primitive
     *
     * @param prim the Primitive
     * @return a ValueItem
     */
    private Primitive visitPrimitive(final Primitive prim) {
        return prim;
    }

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param modl argument 1
     * @return the result of function application
     */
    @Override
    public Modl apply(final Modl modl) {

        final Vector<Structure> structures = Vector.ofAll(modl.structures
                .map(this::visitStructure));

        return new Modl(structures);
    }

    public void setCtx(final TransformationContext ctx) {
        starLoadTransform.setCtx(ctx);
        starClassTransform.setCtx(ctx);
        starMethodTransform.setCtx(ctx);
        referencesTransform.setCtx(ctx);
        conditionalsTransform.setCtx(ctx);
        classExpansionTransform.setCtx(ctx);
        percentStarInstructionTransform.setCtx(ctx);
    }
}
