package uk.modl.interpreter;

import io.vavr.Function2;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Vector;
import uk.modl.extractors.StarLoadExtractor;
import uk.modl.model.*;
import uk.modl.parser.errors.InterpreterError;
import uk.modl.transforms.*;

import java.util.Objects;

/**
 * Interpreter for a Modl object
 */
public class InterpreterVisitor implements Function2<TransformationContext, Modl, Tuple2<TransformationContext, Modl>> {

    public static final Vector<String> VALID_INSTRUCTIONS = Vector.of(
            "*class",
            "*c",
            "*method",
            "*m",
            "*version",
            "*v",
            "*id",
            "*i",
            "*name",
            "*n",
            "*assign",
            "*a",
            "*superclass",
            "*s",
            "*load",
            "*l",
            "*t",
            "*transform"
    );

    private final StarLoadTransform starLoadTransform;

    private final StarClassTransform starClassTransform;

    private final StarMethodTransform starMethodTransform;

    private final ReferencesTransform referencesTransform;

    private final ConditionalsTransform conditionalsTransform;

    private final ClassExpansionTransform classExpansionTransform;

    private final PercentStarInstructionTransform percentStarInstructionTransform;


    /**
     * Constructor
     */
    public InterpreterVisitor() {
        starLoadTransform = new StarLoadTransform();
        starClassTransform = new StarClassTransform();
        starMethodTransform = new StarMethodTransform();
        referencesTransform = new ReferencesTransform();
        conditionalsTransform = new ConditionalsTransform(starLoadTransform, referencesTransform);
        classExpansionTransform = new ClassExpansionTransform();
        percentStarInstructionTransform = new PercentStarInstructionTransform();
    }

    /**
     * Parse Structures
     *
     * @param s a Structure
     * @return a Structure
     */
    private Tuple2<TransformationContext, Structure> visitStructure(final TransformationContext ctx, final Structure s) {

        if (s instanceof Map) {
            return visitMap(ctx, (Map) s);
        }
        if (s instanceof Array) {
            return visitArray(ctx, (Array) s);
        }
        if (s instanceof Pair) {
            return visitPair(ctx, (Pair) s);
        }
        if (s instanceof TopLevelConditional) {
            return visitTopLevelConditional(ctx, (TopLevelConditional) s);
        }
        return Tuple.of(ctx, s);
    }

    /**
     * Parse a TopLevelConditional
     *
     * @param tlc the TopLevelConditional
     * @return a TopLevelConditional
     */
    private Tuple2<TransformationContext, Structure> visitTopLevelConditional(final TransformationContext ctx, final TopLevelConditional tlc) {

        TransformationContext newCtx = ctx;
        Vector<ConditionTest> tests = Vector.empty();

        for (final ConditionTest test : tlc.getTests()) {
            ctx.getAncestry()
                    .add(tlc, test);

            final Tuple2<TransformationContext, ConditionTest> result = visitConditionTest(newCtx, test);
            newCtx = result._1;
            tests = tests.append(result._2);
        }

        Vector<TopLevelConditionalReturn> returns = Vector.empty();

        for (final TopLevelConditionalReturn aReturn : tlc.getReturns()) {
            ctx.getAncestry()
                    .add(tlc, aReturn);

            final Tuple2<TransformationContext, TopLevelConditionalReturn> result = visitTopLevelConditionalReturn(newCtx, aReturn);
            newCtx = result._1;
            returns = returns.append(result._2);
        }

        return conditionalsTransform.apply(newCtx, tlc.with(tests, returns));
    }

    /**
     * Parse a MapConditional
     *
     * @param mc the MapConditional
     * @return a MapConditional
     */
    private Tuple2<TransformationContext, MapConditional> visitMapConditional(final TransformationContext ctx, final MapConditional mc) {

        TransformationContext newCtx = ctx;
        Vector<ConditionTest> tests = Vector.empty();

        for (final ConditionTest test : mc.getTests()) {
            ctx.getAncestry()
                    .add(mc, test);

            final Tuple2<TransformationContext, ConditionTest> result = visitConditionTest(newCtx, test);
            newCtx = result._1;
            tests = tests.append(result._2);
        }

        Vector<MapConditionalReturn> returns = Vector.empty();

        for (final MapConditionalReturn aReturn : mc.getReturns()) {
            ctx.getAncestry()
                    .add(mc, aReturn);

            final Tuple2<TransformationContext, MapConditionalReturn> result = visitMapConditionalReturn(newCtx, aReturn);
            newCtx = result._1;
            returns = returns.append(result._2);
        }

        final MapConditional mapConditional = mc.with(tests, returns);
        final MapConditional evaluated = conditionalsTransform.apply(newCtx, mapConditional);
        return Tuple.of(newCtx, evaluated);
    }

    /**
     * Parse a MapConditionalReturn
     *
     * @param mcr the MapConditionalReturn
     * @return a MapConditionalReturn
     */
    private Tuple2<TransformationContext, MapConditionalReturn> visitMapConditionalReturn(final TransformationContext ctx, final MapConditionalReturn mcr) {

        TransformationContext newCtx = ctx;
        Vector<MapItem> items = Vector.empty();

        for (final MapItem item : mcr.getItems()) {
            ctx.getAncestry()
                    .add(mcr, item);

            if (item instanceof Pair && StarLoadExtractor.isLoadInstruction(((Pair) item).getKey())) {
                final ValueItem refsResult = referencesTransform.apply(newCtx, (ValueItem) item);
                items = items.append((MapItem) refsResult);
            } else {
                final Tuple2<TransformationContext, MapItem> result = visitMapItem(newCtx, item);
                newCtx = result._1;
                items = items.append(result._2);
            }
        }

        return Tuple.of(newCtx, mcr.with(items));
    }

    /**
     * Parse a TopLevelConditionalReturn
     *
     * @param tlcr the TopLevelConditionalReturn
     * @return a TopLevelConditionalReturn
     */
    private Tuple2<TransformationContext, TopLevelConditionalReturn> visitTopLevelConditionalReturn(final TransformationContext ctx, final TopLevelConditionalReturn tlcr) {

        TransformationContext newCtx = ctx;
        Vector<Structure> structures = Vector.empty();

        for (final Structure structure : tlcr.getStructures()) {
            ctx.getAncestry()
                    .add(tlcr, structure);

            if (structure instanceof Pair && StarLoadExtractor.isLoadInstruction(((Pair) structure).getKey())) {
                final Tuple2<TransformationContext, Structure> refsResult = referencesTransform.apply(newCtx, structure);
                structures = structures.append(refsResult._2);
                newCtx = refsResult._1;
            } else {
                if (structure instanceof TopLevelConditional) {
                    final Tuple2<TransformationContext, Structure> visitResult = visitTopLevelConditional(newCtx, (TopLevelConditional) structure);
                    newCtx = visitResult._1;

                    structures = structures.append(visitResult._2);
                } else if (structure instanceof Pair && ((Pair) structure).getValue() instanceof ValueConditional) {
                    final Pair p = (Pair) structure;

                    final Tuple2<TransformationContext, ValueConditional> visitResult = visitValueConditional(newCtx, (ValueConditional) p.getValue());
                    newCtx = visitResult._1;


                    structures = structures.append(p.with(visitResult._2));
                } else {
                    structures = structures.append(structure);
                }
            }
        }

        return Tuple.of(newCtx, tlcr.with(structures));
    }

    /**
     * Parse a ConditionTest
     *
     * @param ct the ConditionTest
     * @return a ConditionTest
     */
    private Tuple2<TransformationContext, ConditionTest> visitConditionTest(final TransformationContext ctx, final ConditionTest ct) {
        TransformationContext newCtx = ctx;
        Vector<Tuple2<ConditionOrConditionGroupInterface, String>> newConditions = Vector.empty();

        for (final Tuple2<ConditionOrConditionGroupInterface, String> c : ct.getConditions()) {
            ctx.getAncestry()
                    .add(ct, c._1);

            if (c._1 instanceof Condition) {
                final Tuple2<TransformationContext, Condition> result = visitCondition(newCtx, (Condition) c._1);
                newCtx = result._1;
                newConditions = newConditions.append(c.update1(result._2));
            } else {
                final Tuple2<TransformationContext, ConditionGroup> result = visitConditionGroup(newCtx, (ConditionGroup) c._1);
                newCtx = result._1;
                newConditions = newConditions.append(c.update1(result._2));
            }
        }

        return Tuple.of(newCtx, ct.with(newConditions));
    }

    /**
     * Parse a ConditionGroup
     *
     * @param cg the ConditionGroup
     * @return a ConditionGroup
     */
    private Tuple2<TransformationContext, ConditionGroup> visitConditionGroup(final TransformationContext ctx, final ConditionGroup cg) {

        TransformationContext newCtx = ctx;
        Vector<Tuple2<ConditionTest, String>> subConditionList = Vector.empty();

        for (final Tuple2<ConditionTest, String> subCond : cg.getSubConditionList()) {
            ctx.getAncestry()
                    .add(cg, subCond._1);

            final Tuple2<TransformationContext, ConditionTest> result = visitConditionTest(newCtx, subCond._1);
            newCtx = result._1;
            subConditionList = subConditionList.append(subCond.update1(result._2));
        }

        return Tuple.of(newCtx, cg.with(subConditionList, cg.isShouldNegate()));
    }

    /**
     * Parse a Condition
     *
     * @param c the Condition
     * @return a Condition
     */
    private Tuple2<TransformationContext, Condition> visitCondition(final TransformationContext ctx, final Condition c) {
        final Condition c2 = referencesTransform.apply(ctx, c);

        TransformationContext newCtx = ctx;

        ctx.getAncestry()
                .add(c2, c2.getLhs());

        final Tuple2<TransformationContext, ValueItem> result = visitValue(newCtx, c2.getLhs());
        newCtx = result._1;
        final Primitive newLhs = (Primitive) result._2;

        Vector<ValueItem> values = Vector.empty();

        for (final ValueItem value : c2.getValues()) {
            ctx.getAncestry()
                    .add(c2, value);

            final Tuple2<TransformationContext, ValueItem> valueResult = visitValue(newCtx, value);
            newCtx = valueResult._1;
            values = values.append(valueResult._2);
        }

        return Tuple.of(newCtx, c.with(newLhs, c2.getOp(), values, c2.isShouldNegate()));
    }

    /**
     * Parse ModlArrays
     *
     * @param arr the Array
     * @return a list of ArrayItems
     */
    private Tuple2<TransformationContext, Structure> visitArray(final TransformationContext ctx, final Array arr) {

        TransformationContext newCtx = ctx;
        Vector<ArrayItem> items = Vector.empty();

        for (final ArrayItem arrayItem : arr.getArrayItems()) {
            ctx.getAncestry()
                    .add(arr, arrayItem);

            final Tuple2<TransformationContext, ArrayItem> result = visitArrayItem(newCtx, arrayItem);
            newCtx = result._1;
            items = items.append(result._2);
        }

        return Tuple.of(newCtx, arr.with(items));
    }

    /**
     * Parse ArrayItems
     *
     * @param ai the ArrayItem
     * @return a list of ArrayItems
     */
    private Tuple2<TransformationContext, ArrayItem> visitArrayItem(final TransformationContext ctx, final ArrayItem ai) {

        if (ai instanceof ArrayConditional) {
            final Tuple2<TransformationContext, ArrayConditional> result = visitArrayConditional(ctx, (ArrayConditional) ai);
            return Tuple.of(result._1, result._2);
        }
        return visitArrayValueItem(ctx, ai);
    }

    /**
     * Parse ArrayValue
     *
     * @param ai the ArrayItem
     * @return an ArrayItem
     */
    private Tuple2<TransformationContext, ArrayItem> visitArrayValueItem(final TransformationContext ctx, final ArrayItem ai) {

        if (ai instanceof Array) {
            final Tuple2<TransformationContext, Structure> result = visitArray(ctx, (Array) ai);
            return Tuple.of(result._1, (ArrayItem) result._2);
        }
        if (ai instanceof Map) {
            final Tuple2<TransformationContext, Structure> result = visitMap(ctx, (Map) ai);
            return Tuple.of(result._1, (ArrayItem) result._2);
        }
        if (ai instanceof Pair) {
            final Tuple2<TransformationContext, Structure> result = visitPair(ctx, (Pair) ai);
            return Tuple.of(result._1, (ArrayItem) result._2);
        }
        if (ai instanceof Primitive) {
            return Tuple.of(ctx, (ArrayItem) visitPrimitive(ctx, (Primitive) ai));
        }
        return null;
    }

    /**
     * Parse ArrayConditionalContext
     *
     * @param ac the ArrayConditional
     * @return an ArrayItem
     */
    private Tuple2<TransformationContext, ArrayConditional> visitArrayConditional(final TransformationContext ctx, final ArrayConditional ac) {

        TransformationContext newCtx = ctx;
        Vector<ConditionTest> tests = Vector.empty();

        for (final ConditionTest test : ac.getTests()) {
            ctx.getAncestry()
                    .add(ac, test);

            final Tuple2<TransformationContext, ConditionTest> result = visitConditionTest(newCtx, test);
            newCtx = result._1;
            tests = tests.append(result._2);
        }

        Vector<ArrayConditionalReturn> returns = Vector.empty();

        for (final ArrayConditionalReturn aReturn : ac.getReturns()) {
            ctx.getAncestry()
                    .add(ac, aReturn);

            final Tuple2<TransformationContext, ArrayConditionalReturn> result = visitArrayConditionalReturn(newCtx, aReturn);
            newCtx = result._1;
            returns = returns.append(result._2);
        }

        final ArrayConditional arrayConditional = ac.with(tests, returns);
        final ArrayConditional evaluated = conditionalsTransform.apply(newCtx, arrayConditional);
        return Tuple.of(newCtx, evaluated);
    }

    /**
     * Parse ArrayConditionalReturn
     *
     * @param acr the ArrayConditionalReturn
     * @return an ArrayConditionalReturn
     */
    private Tuple2<TransformationContext, ArrayConditionalReturn> visitArrayConditionalReturn(final TransformationContext ctx, final ArrayConditionalReturn acr) {

        TransformationContext newCtx = ctx;
        Vector<ArrayItem> items = Vector.empty();
        for (final ArrayItem item : acr.getItems()) {
            ctx.getAncestry()
                    .add(acr, item);
            if (item instanceof Pair && StarLoadExtractor.isLoadInstruction(((Pair) item).getKey())) {
                ctx.getAncestry()
                        .add(acr, item);
                final ValueItem refsResult = referencesTransform.apply(newCtx, (ValueItem) item);
                items = items.append((ArrayItem) refsResult);
            } else {
                final Tuple2<TransformationContext, ArrayItem> result = visitArrayItem(newCtx, item);
                newCtx = result._1;
                items = items.append(result._2);
            }
        }

        return Tuple.of(newCtx, acr.with(items));
    }

    /**
     * Parse ModlMaps
     *
     * @param map the Map
     * @return a Map
     */
    private Tuple2<TransformationContext, Structure> visitMap(final TransformationContext ctx, final Map map) {

        Vector<MapItem> items = Vector.empty();
        TransformationContext newCtx = ctx;
        for (final MapItem mapItem : map.getMapItems()) {
            ctx.getAncestry()
                    .add(map, mapItem);

            final Tuple2<TransformationContext, MapItem> result = visitMapItem(newCtx, mapItem);
            items = items.append(result._2);
            newCtx = result._1;
        }

        return Tuple.of(newCtx, map.with(items));
    }

    /**
     * Parse MapItems
     *
     * @param mi the MapItem
     * @return a MapItem
     */
    private Tuple2<TransformationContext, MapItem> visitMapItem(final TransformationContext ctx, final MapItem mi) {

        if (mi instanceof Pair) {
            final Tuple2<TransformationContext, Structure> result = visitPair(ctx, (Pair) mi);
            return Tuple.of(result._1, (MapItem) result._2);
        } else {
            final Tuple2<TransformationContext, MapConditional> mapConditional = visitMapConditional(ctx, (MapConditional) mi);
            return Tuple.of(mapConditional._1, mapConditional._2);
        }
    }

    /**
     * Parse a ModlPair
     *
     * @param p the Pair
     * @return a Pair
     */
    private Tuple2<TransformationContext, Structure> visitPair(final TransformationContext ctx, final Pair p) {

        if (p.getKey()
                .startsWith("*") && !VALID_INSTRUCTIONS
                .contains(p.getKey()
                        .toLowerCase())) {
            throw new RuntimeException("Invalid keyword: " + p.getKey());
        }
        TransformationContext newCtx = ctx;
        if (p.getKey()
                .equals("*VERSION") || p.getKey()
                .equals("*V")) {
            try {
                final int version = p.getValue()
                        .numericValue()
                        .intValue();

                if (version <= 0) {
                    throw new RuntimeException("Invalid MODL version: " + p.getValue()
                            .toString());
                }

                newCtx = newCtx.withVersion(version);
            } catch (final NumberFormatException e) {
                throw new RuntimeException("Invalid MODL version: " + p.getValue()
                        .toString());
            }
        }
        ctx.getAncestry()
                .add(p, p.getValue());

        // Special handling for *load instructions.
        if (StarLoadExtractor.isLoadInstruction(p.getKey())) {

            final ValueItem result = referencesTransform.apply(newCtx, (ValueItem) p.getValue());
            final Tuple2<TransformationContext, Structure> structureWithLoadedFiles = starLoadTransform.apply(newCtx, p.with(result));
            ctx.getAncestry()
                    .add(p, structureWithLoadedFiles._2);

            return Tuple.of(structureWithLoadedFiles._1, structureWithLoadedFiles._2);
        }

        final Tuple2<TransformationContext, Structure> structureWithExpandedClasses = starClassTransform.apply(newCtx, p);
        final Tuple2<TransformationContext, Structure> structureWithAppliedMethods = starMethodTransform.apply(structureWithExpandedClasses._1, structureWithExpandedClasses._2);
        final Tuple2<TransformationContext, Structure> contextAndStructure = referencesTransform.apply(structureWithAppliedMethods._1, structureWithAppliedMethods._2);
        final Structure structure = percentStarInstructionTransform.apply(contextAndStructure._1, contextAndStructure._2);

        newCtx = contextAndStructure._1;

        if (structure == null) {
            return Tuple.of(newCtx, null);
        }

        PairValue value;

        if (structure instanceof Pair) {
            final Pair pair = (Pair) structure;
            value = pair.getValue();
        } else {
            value = (PairValue) structure;
        }

        if (value instanceof Array) {
            final Tuple2<TransformationContext, Structure> result = visitArray(newCtx, (Array) value);
            newCtx = result._1;
            value = (PairValue) result._2;
        } else if (value instanceof Map) {
            final Tuple2<TransformationContext, Structure> result = visitMap(newCtx, (Map) value);
            newCtx = result._1;
            value = (PairValue) result._2;
        } else if (value instanceof ValueItem) {
            final Tuple2<TransformationContext, ValueItem> result = visitValueItem(newCtx, (ValueItem) value);
            newCtx = result._1;
            value = result._2;
        }

        if (structure instanceof Pair) {
            final Pair newPair = p.with(value);
            ctx.getAncestry()
                    .add(newPair, value);

            return Tuple.of(newCtx, newPair);
        }
        ctx.getAncestry()
                .add(p, structure);
        return Tuple.of(newCtx, structure);
    }

    /**
     * Parse ValueItems
     *
     * @param vi the ValueItem
     * @return a ValueItem
     */
    private Tuple2<TransformationContext, ValueItem> visitValueItem(final TransformationContext ctx, final ValueItem vi) {

        if (vi instanceof ValueConditional) {
            final Tuple2<TransformationContext, ValueConditional> result = visitValueConditional(ctx, (ValueConditional) vi);
            return Tuple.of(result._1, result._2);
        }
        if (vi instanceof Map) {
            final Tuple2<TransformationContext, Structure> result = visitMap(ctx, (Map) vi);
            return Tuple.of(result._1, (ValueItem) result._2);
        }
        if (vi instanceof Array) {
            final Tuple2<TransformationContext, Structure> result = visitArray(ctx, (Array) vi);
            return Tuple.of(result._1, (ValueItem) result._2);
        }
        if (vi instanceof Pair) {
            final Tuple2<TransformationContext, Structure> result = visitPair(ctx, (Pair) vi);
            return Tuple.of(result._1, (ValueItem) result._2);
        }
        if (vi instanceof Primitive) {
            final Primitive primitive = (Primitive) visitPrimitive(ctx, (Primitive) vi);
            return Tuple.of(ctx, primitive);
        }
        return Tuple.of(ctx, vi);
    }

    /**
     * Parse ValueConditional
     *
     * @param vc the ValueConditional
     * @return a ValueItem
     */
    private Tuple2<TransformationContext, ValueConditional> visitValueConditional(final TransformationContext ctx, final ValueConditional vc) {

        TransformationContext newCtx = ctx;
        Vector<ConditionTest> tests = Vector.empty();

        for (final ConditionTest test : vc.getTests()) {
            ctx.getAncestry()
                    .add(vc, test);

            final Tuple2<TransformationContext, ConditionTest> result = visitConditionTest(newCtx, test);
            newCtx = result._1;
            tests = tests.append(result._2);
        }

        Vector<ValueConditionalReturn> returns = Vector.empty();

        for (final ValueConditionalReturn aReturn : vc.getReturns()) {
            ctx.getAncestry()
                    .add(vc, aReturn);

            final Tuple2<TransformationContext, ValueConditionalReturn> result = visitValueConditionReturn(newCtx, aReturn);
            returns = returns.append(result._2);
            newCtx = result._1;
        }

        final ValueConditional valueConditional = vc.with(tests, returns);
        final ValueConditional evaluated = conditionalsTransform.apply(newCtx, valueConditional);
        newCtx.getAncestry()
                .replaceChild(vc, evaluated);
        return Tuple.of(newCtx, evaluated);
    }

    /**
     * Parse ValueConditionalReturn
     *
     * @param vcr the ValueConditionalReturn
     * @return a ValueConditionalReturn
     */
    private Tuple2<TransformationContext, ValueConditionalReturn> visitValueConditionReturn(final TransformationContext ctx, final ValueConditionalReturn vcr) {

        TransformationContext newCtx = ctx;
        Vector<ValueItem> items = Vector.empty();

        for (final ValueItem item : vcr.getItems()) {
            ctx.getAncestry()
                    .add(vcr, item);

            if (item instanceof Pair && StarLoadExtractor.isLoadInstruction(((Pair) item).getKey())) {
                final ValueItem refsResult = referencesTransform.apply(newCtx, item);
                items = items.append(refsResult);
            } else {
                final Tuple2<TransformationContext, ValueItem> result = visitValueItem(newCtx, item);
                newCtx = result._1;
                items = items.append(result._2);
            }
        }

        return Tuple.of(newCtx, vcr.with(items));
    }

    /**
     * Parse Values
     *
     * @param vi the ValueItem
     * @return a Value
     */
    private Tuple2<TransformationContext, ValueItem> visitValue(final TransformationContext ctx, final ValueItem vi) {

        if (vi instanceof StringPrimitive) {
            final ValueItem newValueItem = referencesTransform.apply(ctx, vi);
            return visitValueItem(ctx, newValueItem);
        }

        if (vi instanceof Array) {
            final Tuple2<TransformationContext, Structure> result = visitArray(ctx, (Array) vi);
            return Tuple.of(result._1, (ValueItem) result._2);
        }
        if (vi instanceof Map) {
            final Tuple2<TransformationContext, Structure> result = visitMap(ctx, (Map) vi);
            return Tuple.of(result._1, (ValueItem) result._2);
        }
        if (vi instanceof Pair) {
            final Tuple2<TransformationContext, Structure> result = visitPair(ctx, (Pair) vi);
            return Tuple.of(result._1, (ValueItem) result._2);
        }
        return Tuple.of(ctx, vi);
    }

    /**
     * Parse Primitive
     *
     * @param prim the Primitive
     * @return a ValueItem
     */
    private ValueItem visitPrimitive(final TransformationContext ctx, final Primitive prim) {
        final ValueItem dereferenced = referencesTransform.apply(ctx, prim);
        if (dereferenced instanceof StringPrimitive) {
            final StringPrimitive stringPrimitive = (StringPrimitive) dereferenced;
            return stringPrimitive.with(LiteralsTransform.replacellLiteralRefs(stringPrimitive.getValue()));
        }
        return dereferenced;
    }

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param modl argument 1
     * @return the result of function application
     */
    @Override
    public Tuple2<TransformationContext, Modl> apply(final TransformationContext ctx, final Modl modl) {

        TransformationContext newCtx = ctx;

        try {
            Vector<Structure> visitedStructures = Vector.empty();
            {
                for (final Structure structure : modl.getStructures()) {
                    ctx.getAncestry()
                            .add(modl, structure);

                    final Tuple2<TransformationContext, Structure> result = visitStructure(newCtx, structure);
                    newCtx = result._1;
                    visitedStructures = visitedStructures.append(result._2);
                }
            }

            final TransformationContext finalNewCtx = newCtx;

            final Vector<Structure> resultStructures = visitedStructures.map(structure -> classExpansionTransform.apply(finalNewCtx, structure));

//            finalNewCtx.getAncestry()
//                    .dump();// TODO: Delete this

            return Tuple.of(finalNewCtx, modl.with(resultStructures.filter(Objects::nonNull)));
        } catch (final InterpreterError e) {
            throw e;
        } catch (final RuntimeException e) {
            if (newCtx.getVersion() > 1) {
                throw new InterpreterError("Interpreter Error: " + e.getMessage() + " - MODL Version 1 interpreter cannot process this MODL Version " + newCtx.getVersion() + " file.");
            }
            throw new InterpreterError("Interpreter Error: " + e.getMessage());
        }
    }

}
