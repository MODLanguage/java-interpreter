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

        /* Suggested order of processing
        pipeline = starLoadTransform
                .andThen(starClassTransform)
                .andThen(starMethodTransform)
                .andThen(referencesTransform)
                .andThen(conditionalsTransform);

         */
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

        final TopLevelConditional result = conditionalsTransform.apply(tlc);

        final Vector<ConditionTest> tests = result.tests
                .map(this::visitConditionTest);

        final Vector<TopLevelConditionalReturn> returns = result.returns
                .map(this::visitTopLevelConditionalReturn);

        return new TopLevelConditional(tests, returns);
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

        return new MapConditional(tests, returns);
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
        // TODO
        return ct;
    }

    /**
     * Parse a ConditionGroup
     *
     * @param cg the ConditionGroup
     * @return a ConditionGroup
     */
    private ConditionGroup visitConditionGroup(final ConditionGroup cg) {
        final Vector<Tuple2<ConditionTest, String>> subConditionList = handleConditionGroup(cg.subConditionList);
        return new ConditionGroup(subConditionList);
    }

    /**
     * Convert a ConditionGroup context to a list of subconditions
     *
     * @param list the context
     * @return a list of subconditions
     */
    private Vector<Tuple2<ConditionTest, String>> handleConditionGroup(final Vector<Tuple2<ConditionTest, String>> list) {
        // TODO
        return list;
    }

    /**
     * Parse a ConditionGroup
     *
     * @param ncg the NegatedConditionGroup
     * @return a ConditionGroup
     */
    private NegatedConditionGroup visitNegatedConditionGroup(final NegatedConditionGroup ncg) {

        final Vector<Tuple2<ConditionTest, String>> subConditionList = handleConditionGroup(ncg.subConditionList);
        return new NegatedConditionGroup(subConditionList);
    }

    /**
     * Parse a Condition
     *
     * @param c the Condition
     * @return a Condition
     */
    private Condition visitCondition(final Condition c) {

        final Vector<ValueItem> values = c.values
                .map(this::visitValue);

        final String lhs = c.lhs;// TODO

        return new Condition(lhs, c.op, values);
    }

    /**
     * Parse a Condition
     *
     * @param nc the NegatedCondition
     * @return a Condition
     */
    private NegatedCondition visitNegatedCondition(final NegatedCondition nc) {

        final Vector<ValueItem> values = nc.values
                .map(this::visitValue);

        return new NegatedCondition(nc.op, values);
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

        return new ArrayConditional(tests, returns);
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

        final Pair pair = referencesTransform.apply(starMethodTransform.apply(starClassTransform.apply(starLoadTransform.apply(p))));
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
        }
        return visitValue(vi);
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

        return new ValueConditional(tests, returns);
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

        return (vi instanceof Array) ?
                visitArray((Array) vi) :
                (vi instanceof Map) ?
                        visitMap((Map) vi) :
                        (vi instanceof Pair) ?
                                visitPair((Pair) vi) :
                                visitPrimitive((Primitive) vi);
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
        starLoadTransform.seCtx(ctx);
        starClassTransform.seCtx(ctx);
        starMethodTransform.seCtx(ctx);
        referencesTransform.seCtx(ctx);
        conditionalsTransform.seCtx(ctx);

    }
}
