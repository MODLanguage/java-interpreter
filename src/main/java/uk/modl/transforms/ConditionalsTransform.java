package uk.modl.transforms;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import uk.modl.model.*;
import uk.modl.parser.errors.InterpreterError;
import uk.modl.utils.Util;

@RequiredArgsConstructor
public class ConditionalsTransform {

    private final StarLoadTransform starLoadTransform;

    private final ReferencesTransform referencesTransform;

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param tlc argument 1
     * @return the result of function application
     */
    public Tuple2<TransformationContext, Structure> apply(final TransformationContext ctx, final TopLevelConditional tlc) {
        if (tlc.getTests()
                .size() == 1) {
            if (evaluate(ctx, tlc.getTests()
                    .get(0))) {
                @NonNull final Vector<Structure> vector = tlc.getReturns()
                        .get(0)
                        .getStructures();

                return getToplevelConditionalResult(ctx, tlc, vector);
            } else {
                if (tlc.getReturns()
                        .size() > 1) {
                    @NonNull final Vector<Structure> vector = tlc.getReturns()
                            .get(1)
                            .getStructures();

                    return getToplevelConditionalResult(ctx, tlc, vector);
                }
            }
        } else {
            int i = 0;
            TransformationContext newCtx = ctx;
            Vector<Structure> structures = Vector.empty();

            for (final ConditionTest test : tlc.getTests()) {
                if (evaluate(ctx, test)) {

                    @NonNull final Vector<Structure> vector = tlc.getReturns()
                            .get(i)
                            .getStructures();

                    for (final Structure structure : vector) {
                        final Tuple2<TransformationContext, Structure> result = handleNestedTopLevelConditionals(newCtx, structure);
                        newCtx = result._1;
                        structures = structures.append(result._2);
                    }

                    newCtx = saveResultInContext(ctx, structures);

                }
                i += 1;
            }
            return Tuple.of(newCtx, tlc.withResult(structures));
        }
        return Tuple.of(ctx, tlc);
    }

    private Tuple2<TransformationContext, Structure> getToplevelConditionalResult(final TransformationContext ctx, final TopLevelConditional tlc, final @NonNull Vector<Structure> vector) {
        TransformationContext newCtx = ctx;
        Vector<Structure> structures = Vector.empty();

        for (final Structure structure : vector) {
            final Tuple2<TransformationContext, Structure> result = handleNestedTopLevelConditionals(newCtx, structure);
            newCtx = result._1;
            structures = structures.append(result._2);
        }

        newCtx = saveResultInContext(ctx, structures);

        return Tuple.of(newCtx, tlc.withResult(structures));
    }

    private TransformationContext saveResultInContext(final TransformationContext ctx, final Vector<Structure> structures) {

        TransformationContext newCtx = ctx;
        for (final Structure structure : structures) {

            final Tuple2<TransformationContext, Structure> loadResult = starLoadTransform.apply(newCtx, structure);
            newCtx = loadResult._1;
            final Structure newStructure = loadResult._2;

            if (newStructure instanceof Pair) {
                final Tuple2<TransformationContext, Structure> refsResult = referencesTransform.apply(newCtx, newStructure);
                newCtx = refsResult._1;

                final Pair pair = (Pair) refsResult._2;
                @NonNull final String key = pair.getKey();
                newCtx = newCtx.addPair(key, pair);

                if (key.startsWith("_")) {
                    newCtx = newCtx.addPair(key.substring(1), pair);
                }
            }
        }

        return newCtx;

    }

    private Tuple2<TransformationContext, Structure> handleNestedTopLevelConditionals(final TransformationContext ctx, final Structure structure) {
        if (structure instanceof TopLevelConditional) {
            return apply(ctx, (TopLevelConditional) structure);
        }
        if (structure instanceof Pair) {
            final Pair p = (Pair) structure;
            @NonNull final PairValue value = p.getValue();

            if (value instanceof ValueConditional) {
                final ValueConditional result = apply(ctx, (ValueConditional) value);
                final Pair resultPair = new Pair(p.getKey(), result);
                final TransformationContext newCtx = saveResultInContext(ctx, Vector.of(resultPair));
                return Tuple.of(newCtx, resultPair);
            }
        }
        return Tuple.of(ctx, structure);
    }

    private boolean evaluate(final TransformationContext ctx, final ConditionTest test) {
        final Vector<Tuple2<Boolean, String>> partial = test.getConditions()
                .map(cond -> evaluate(ctx, cond));
        return evaluate(partial);
    }

    private Tuple2<Boolean, String> evaluate(final TransformationContext ctx, final Tuple2<ConditionOrConditionGroupInterface, String> tuple) {
        if (tuple._1 instanceof Condition) {
            return Tuple.of(evaluate(ctx, (Condition) tuple._1), tuple._2);
        } else {
            return Tuple.of(evaluate(ctx, (ConditionGroup) tuple._1), tuple._2);
        }
    }

    private boolean evaluate(final TransformationContext ctx, final ConditionGroup cg) {

        final Vector<Tuple2<Boolean, String>> partial = cg.getSubConditionList()
                .map(sc -> Tuple.of(evaluate(ctx, sc._1), sc._2));

        return (cg.isShouldNegate()) != evaluate(partial);
    }

    private boolean evaluate(final Vector<Tuple2<Boolean, String>> partial) {
        boolean first = true;
        boolean result = false;
        String lastOp = null;
        for (Tuple2<Boolean, String> partialItem : partial) {
            if (first) {
                first = false;
                result = partialItem._1;
            } else {
                switch (lastOp) {
                    case "&":
                        result = result && partialItem._1;
                        break;
                    case "|":
                        result = result || partialItem._1;
                        break;
                    default:
                        throw new InterpreterError("Invalid operation in conditional: " + lastOp);
                }
            }
            lastOp = partialItem._2;
        }
        return result;
    }

    private boolean evaluate(final TransformationContext ctx, final Condition c) {
        final Operator op = c.getOp();
        final boolean shouldNegate = c.isShouldNegate();

        final ValueItem lhs = c.getLhs();

        @NonNull final Vector<ValueItem> values = c.getValues();

        if (op == null && lhs == null || lhs.toString() == null) {
            // Is the RHS true?
            return shouldNegate != values.map(v -> {
                if (v instanceof TruePrimitive) {
                    return true;
                }
                if (v instanceof FalsePrimitive || v instanceof NullPrimitive) {
                    return false;
                }
                final String key = v.toString();
                final String hiddenKey = "_" + key;
                if (v instanceof StringPrimitive && (ctx.getPairs()
                        .containsKey(key) || ctx.getPairs()
                        .containsKey(hiddenKey))) {

                    final Pair pair = ctx.getPairs()
                            .get(key)
                            .getOrElse(() -> ctx.getPairs()
                                    .get(hiddenKey)
                                    .get());

                    @NonNull final PairValue pairValue = pair.getValue();
                    if (pairValue instanceof TruePrimitive) {
                        return true;
                    }
                    if (pairValue instanceof FalsePrimitive || pairValue instanceof NullPrimitive) {
                        return false;
                    }
                    if (pairValue instanceof ValueConditional) {
                        final ValueConditional vc = (ValueConditional) pairValue;
                        return vc.getResult()
                                .map(vcResult -> {
                                    if (vcResult instanceof TruePrimitive) {
                                        return true;
                                    }
                                    if (vcResult instanceof FalsePrimitive || vcResult instanceof NullPrimitive) {
                                        return false;
                                    }
                                    return false;
                                })
                                .getOrElse(false);
                    }
                    return true;
                }
                return false;
            })
                    .getOrElse(false);
        }
        if (op instanceof GreaterThanOperator) {
            return shouldNegate != Util.greaterThanAll(lhs, values);
        }
        if (op instanceof GreaterThanOrEqualsOperator) {
            return shouldNegate != Util.greaterThanOrEqualToAll(lhs, values);
        }
        if (op instanceof LessThanOperator) {
            return shouldNegate != Util.lessThanAll(lhs, values);
        }
        if (op instanceof LessThanOrEqualsOperator) {
            return shouldNegate != Util.lessThanOrEqualToAll(lhs, values);
        }

        final int count = countMatches(c, lhs);
        if (op instanceof EqualsOperator) {
            return shouldNegate != (count > 0);
        }
        if (op instanceof NotEqualsOperator) {
            return shouldNegate != (count == 0);
        }
        return shouldNegate;
    }

    private int countMatches(final Condition c, final ValueItem lhs) {
        return c.getValues()
                .count(v -> {
                    if (!v.toString()
                            .contains("*")) {
                        return v.equals(lhs);
                    } else {
                        final String regexStr = v.toString()
                                .replaceAll("\\*", ".*");
                        return c.getLhs()
                                .toString()
                                .matches(regexStr);
                    }
                });
    }

    public ValueConditional apply(final TransformationContext ctx, final ValueConditional vc) {
        if (vc.getTests()
                .size() == 1) {
            if (evaluate(ctx, vc.getTests()
                    .get(0))) {
                if (vc.getReturns()
                        .size() == 0) {
                    return vc.withResult(Vector.of(TruePrimitive.instance));
                }
                final Vector<ValueItem> items = vc.getReturns()
                        .get(0)
                        .getItems()
                        .map(nested -> handleNestedValueConditionals(ctx, nested));

                return vc.withResult(items);
            } else {
                if (vc.getReturns()
                        .size() == 0) {
                    return vc.withResult(Vector.of(FalsePrimitive.instance));
                }
                final Vector<ValueItem> items = vc.getReturns()
                        .get(1)
                        .getItems()
                        .map(nested -> handleNestedValueConditionals(ctx, nested));

                return vc.withResult(items);
            }
        } else {
            int i = 0;
            for (final ConditionTest test : vc.getTests()) {
                if (evaluate(ctx, test)) {
                    final Vector<ValueItem> items = vc.getReturns()
                            .get(i)
                            .getItems()
                            .map(nested -> handleNestedValueConditionals(ctx, nested));

                    return vc.withResult(items);
                }
                i += 1;
            }
        }
        return vc;
    }

    private ValueItem handleNestedValueConditionals(final TransformationContext ctx, final ValueItem vi) {
        if (vi instanceof ValueConditional) {
            return apply(ctx, (ValueConditional) vi);
        }
        return vi;
    }

    private ArrayItem handleNestedArrayConditionals(final TransformationContext ctx, final ArrayItem vi) {
        if (vi instanceof ArrayConditional) {
            return apply(ctx, (ArrayConditional) vi);
        }
        return vi;
    }

    private MapItem handleNestedMapConditionals(final TransformationContext ctx, final MapItem vi) {
        if (vi instanceof MapConditional) {
            return apply(ctx, (MapConditional) vi);
        }
        return vi;
    }

    public ArrayConditional apply(final TransformationContext ctx, final ArrayConditional ac) {
        if (ac.getTests()
                .size() == 1) {
            if (evaluate(ctx, ac.getTests()
                    .get(0))) {
                if (ac.getReturns()
                        .size() == 0) {
                    return ac.withResult(Vector.of(TruePrimitive.instance));
                }
                final Vector<ArrayItem> items = ac.getReturns()
                        .get(0)
                        .getItems()
                        .map(nested -> handleNestedArrayConditionals(ctx, nested));

                return ac.withResult(items);
            } else {
                if (ac.getReturns()
                        .size() == 0) {
                    return ac.withResult(Vector.of(FalsePrimitive.instance));
                }
                final Vector<ArrayItem> items = ac.getReturns()
                        .get(1)
                        .getItems()
                        .map(nested -> handleNestedArrayConditionals(ctx, nested));

                return ac.withResult(items);
            }
        } else {
            int i = 0;
            for (final ConditionTest test : ac.getTests()) {
                if (evaluate(ctx, test)) {
                    final Vector<ArrayItem> items = ac.getReturns()
                            .get(i)
                            .getItems()
                            .map(nested -> handleNestedArrayConditionals(ctx, nested));

                    return ac.withResult(items);
                }
                i += 1;
            }
        }
        return ac;
    }

    public MapConditional apply(final TransformationContext ctx, final MapConditional mc) {
        if (mc.getTests()
                .size() == 1) {
            if (evaluate(ctx, mc.getTests()
                    .get(0))) {
                if (mc.getReturns()
                        .size() == 0) {
                    return mc.withResult(Vector.of((MapItem) TruePrimitive.instance));
                }
                final Vector<MapItem> items = mc.getReturns()
                        .get(0)
                        .getItems()
                        .map(nested -> handleNestedMapConditionals(ctx, nested));

                return mc.withResult(items);
            } else {
                if (mc.getReturns()
                        .size() == 0) {
                    return mc.withResult(Vector.of((MapItem) FalsePrimitive.instance));
                }
                final Vector<MapItem> items = mc.getReturns()
                        .get(1)
                        .getItems()
                        .map(nested -> handleNestedMapConditionals(ctx, nested));

                return mc.withResult(items);
            }
        } else {
            int i = 0;
            for (final ConditionTest test : mc.getTests()) {
                if (evaluate(ctx, test)) {
                    final Vector<MapItem> items = mc.getReturns()
                            .get(i)
                            .getItems()
                            .map(nested -> handleNestedMapConditionals(ctx, nested));

                    return mc.withResult(items);
                }
                i += 1;
            }
        }
        return mc;
    }

}
