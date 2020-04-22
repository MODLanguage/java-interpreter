package uk.modl.transforms;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import uk.modl.model.*;
import uk.modl.parser.errors.InterpreterError;
import uk.modl.utils.Util;

@RequiredArgsConstructor
public class ConditionalsTransform {
    /**
     * The context for this invocation of the interpreter
     */
    @NonNull
    @Setter
    private TransformationContext ctx;

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param tlc argument 1
     * @return the result of function application
     */
    public TopLevelConditional apply(final TopLevelConditional tlc) {
        if (tlc.getTests()
                .size() == 1) {
            if (evaluate(tlc.getTests()
                    .get(0))) {
                final Vector<Structure> structures = tlc.getReturns()
                        .get(0)
                        .getStructures()
                        .map(this::handleNestedTopLevelConditionals);

                return tlc.withResult(structures);
            } else {
                if (tlc.getReturns()
                        .size() > 1) {
                    final Vector<Structure> structures = tlc.getReturns()
                            .get(1)
                            .getStructures()
                            .map(this::handleNestedTopLevelConditionals);

                    return tlc.withResult(structures);
                }
            }
        } else {
            int i = 0;
            for (final ConditionTest test : tlc.getTests()) {
                if (evaluate(test)) {
                    final Vector<Structure> structures = tlc.getReturns()
                            .get(i)
                            .getStructures()
                            .map(this::handleNestedTopLevelConditionals);

                    return tlc.withResult(structures);
                }
                i += 1;
            }
        }
        return tlc;
    }

    private Structure handleNestedTopLevelConditionals(final Structure structure) {
        if (structure instanceof TopLevelConditional) {
            return apply((TopLevelConditional) structure);
        }
        return structure;
    }

    private boolean evaluate(final ConditionTest test) {
        final Vector<Tuple2<Boolean, String>> partial = test.getConditions()
                .map(this::evaluate);
        return evaluate(partial);
    }

    private Tuple2<Boolean, String> evaluate(final Tuple2<ConditionOrConditionGroupInterface, String> tuple) {
        // TODO
        if (tuple._1 instanceof Condition) {
            return Tuple.of(evaluate((Condition) tuple._1), tuple._2);
        } else {
            return Tuple.of(evaluate((ConditionGroup) tuple._1), tuple._2);
        }
    }

    private boolean evaluate(final ConditionGroup cg) {

        final Vector<Tuple2<Boolean, String>> partial = cg.getSubConditionList()
                .map(sc -> Tuple.of(evaluate(sc._1), sc._2));

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

    private boolean evaluate(final Condition c) {
        if (c.getOp() instanceof GreaterThanOperator) {
            return (c.isShouldNegate()) != Util.greaterThanAll(c.getLhs(), c.getValues());
        }
        if (c.getOp() instanceof GreaterThanOrEqualsOperator) {
            return (c.isShouldNegate()) != Util.greaterThanOrEqualToAll(c.getLhs(), c.getValues());
        }
        if (c.getOp() instanceof LessThanOperator) {
            return (c.isShouldNegate()) != Util.lessThanAll(c.getLhs(), c.getValues());
        }
        if (c.getOp() instanceof LessThanOrEqualsOperator) {
            return (c.isShouldNegate()) != Util.lessThanOrEqualToAll(c.getLhs(), c.getValues());
        }

        final int count = countMatches(c);
        if (c.getOp() instanceof EqualsOperator) {
            return (c.isShouldNegate()) != (count > 0);
        }
        if (c.getOp() instanceof NotEqualsOperator) {
            return (c.isShouldNegate()) != (count == 0);
        }
        return c.isShouldNegate();
    }

    private int countMatches(final Condition c) {
        return c.getValues()
                .map(Object::toString)
                .count(v -> {
                    if (!v.contains("*")) {
                        return v.equals(c.getLhs()
                                .toString());
                    } else {
                        final String regexStr = v.replaceAll("\\*", ".*");
                        return c.getLhs()
                                .toString()
                                .matches(regexStr);
                    }
                });
    }

    public ValueConditional apply(final ValueConditional vc) {
        if (vc.getTests()
                .size() == 1) {
            if (evaluate(vc.getTests()
                    .get(0))) {
                if (vc.getReturns()
                        .size() == 0) {
                    return vc.withResult(Vector.of(TruePrimitive.instance));
                }
                final Vector<ValueItem> items = vc.getReturns()
                        .get(0)
                        .getItems()
                        .map(this::handleNestedValueConditionals);

                return vc.withResult(items);
            } else {
                if (vc.getReturns()
                        .size() == 0) {
                    return vc.withResult(Vector.of(FalsePrimitive.instance));
                }
                final Vector<ValueItem> items = vc.getReturns()
                        .get(1)
                        .getItems()
                        .map(this::handleNestedValueConditionals);

                return vc.withResult(items);
            }
        } else {
            int i = 0;
            for (final ConditionTest test : vc.getTests()) {
                if (evaluate(test)) {
                    final Vector<ValueItem> items = vc.getReturns()
                            .get(i)
                            .getItems()
                            .map(this::handleNestedValueConditionals);

                    return vc.withResult(items);
                }
                i += 1;
            }
        }
        return vc;
    }

    private ValueItem handleNestedValueConditionals(final ValueItem vi) {
        if (vi instanceof ValueConditional) {
            return apply((ValueConditional) vi);
        }
        return vi;
    }

    private ArrayItem handleNestedArrayConditionals(final ArrayItem vi) {
        if (vi instanceof ArrayConditional) {
            return apply((ArrayConditional) vi);
        }
        return vi;
    }

    private MapItem handleNestedMapConditionals(final MapItem vi) {
        if (vi instanceof MapConditional) {
            return apply((MapConditional) vi);
        }
        return vi;
    }

    public ArrayConditional apply(final ArrayConditional ac) {
        if (ac.getTests()
                .size() == 1) {
            if (evaluate(ac.getTests()
                    .get(0))) {
                if (ac.getReturns()
                        .size() == 0) {
                    return ac.withResult(Vector.of(TruePrimitive.instance));
                }
                final Vector<ArrayItem> items = ac.getReturns()
                        .get(0)
                        .getItems()
                        .map(this::handleNestedArrayConditionals);

                return ac.withResult(items);
            } else {
                if (ac.getReturns()
                        .size() == 0) {
                    return ac.withResult(Vector.of(FalsePrimitive.instance));
                }
                final Vector<ArrayItem> items = ac.getReturns()
                        .get(1)
                        .getItems()
                        .map(this::handleNestedArrayConditionals);

                return ac.withResult(items);
            }
        } else {
            int i = 0;
            for (final ConditionTest test : ac.getTests()) {
                if (evaluate(test)) {
                    final Vector<ArrayItem> items = ac.getReturns()
                            .get(i)
                            .getItems()
                            .map(this::handleNestedArrayConditionals);

                    return ac.withResult(items);
                }
                i += 1;
            }
        }
        return ac;
    }

    public MapConditional apply(final MapConditional mc) {
        if (mc.getTests()
                .size() == 1) {
            if (evaluate(mc.getTests()
                    .get(0))) {
                if (mc.getReturns()
                        .size() == 0) {
                    return mc.withResult(Vector.of((MapItem) TruePrimitive.instance));
                }
                final Vector<MapItem> items = mc.getReturns()
                        .get(0)
                        .getItems()
                        .map(this::handleNestedMapConditionals);

                return mc.withResult(items);
            } else {
                if (mc.getReturns()
                        .size() == 0) {
                    return mc.withResult(Vector.of((MapItem) FalsePrimitive.instance));
                }
                final Vector<MapItem> items = mc.getReturns()
                        .get(1)
                        .getItems()
                        .map(this::handleNestedMapConditionals);

                return mc.withResult(items);
            }
        } else {
            int i = 0;
            for (final ConditionTest test : mc.getTests()) {
                if (evaluate(test)) {
                    final Vector<MapItem> items = mc.getReturns()
                            .get(i)
                            .getItems()
                            .map(this::handleNestedMapConditionals);

                    return mc.withResult(items);
                }
                i += 1;
            }
        }
        return mc;
    }
}
