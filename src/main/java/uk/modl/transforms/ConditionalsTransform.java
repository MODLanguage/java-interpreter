package uk.modl.transforms;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import uk.modl.model.*;
import uk.modl.parser.errors.InterpreterError;

@RequiredArgsConstructor
public class ConditionalsTransform {
    /**
     * The context for this invocation of the interpreter
     */
    @NonNull
    private TransformationContext ctx;

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param tlc argument 1
     * @return the result of function application
     */
    public TopLevelConditional apply(final TopLevelConditional tlc) {
        if (tlc.tests.size() == 1) {
            if (evaluate(tlc.tests.get(0))) {
                final Vector<Structure> structures = tlc.returns.get(0).structures.map(this::handleNestedTopLevelConditionals);

                return tlc.setResult(structures);
            } else {
                final Vector<Structure> structures = tlc.returns.get(1).structures.map(this::handleNestedTopLevelConditionals);

                return tlc.setResult(structures);
            }
        } else {
            int i = 0;
            for (final ConditionTest test : tlc.tests) {
                if (evaluate(test)) {
                    final Vector<Structure> structures = tlc.returns.get(i).structures.map(this::handleNestedTopLevelConditionals);

                    return tlc.setResult(structures);
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
        final Vector<Tuple2<Boolean, String>> partial = test.conditions.map(this::evaluate);
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

        final Vector<Tuple2<Boolean, String>> partial = cg.subConditionList.map(sc -> Tuple.of(evaluate(sc._1), sc._2));

        return evaluate(partial);
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
        final int count = countMatches(c);
        if (c.op instanceof EqualsOperator) {
            return count > 0;
        }
        if (c.op instanceof NotEqualsOperator) {
            return (count == 0);
        }
        return false;
    }

    private int countMatches(final Condition c) {
        return c.values.count(v -> v.toString()
                .equals(c.lhs.toString()));
    }

    /**
     * @param ctx the TransformationContext
     */
    public void seCtx(final TransformationContext ctx) {
        this.ctx = ctx;
    }

    public ValueConditional apply(final ValueConditional vc) {
        if (vc.tests.size() == 1) {
            if (evaluate(vc.tests.get(0))) {
                if (vc.returns.size() == 0) {
                    return vc.setResult(Vector.of(TruePrimitive.instance));
                }
                final Vector<ValueItem> items = vc.returns.get(0).items.map(this::handleNestedValueConditionals);

                return vc.setResult(items);
            } else {
                if (vc.returns.size() == 0) {
                    return vc.setResult(Vector.of(FalsePrimitive.instance));
                }
                final Vector<ValueItem> items = vc.returns.get(1).items.map(this::handleNestedValueConditionals);

                return vc.setResult(items);
            }
        } else {
            int i = 0;
            for (final ConditionTest test : vc.tests) {
                if (evaluate(test)) {
                    final Vector<ValueItem> items = vc.returns.get(i).items.map(this::handleNestedValueConditionals);

                    return vc.setResult(items);
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


    public ArrayConditional apply(final ArrayConditional ac) {
        if (ac.tests.size() == 1) {
            if (evaluate(ac.tests.get(0))) {
                if (ac.returns.size() == 0) {
                    return ac.setResult(Vector.of(TruePrimitive.instance));
                }
                final Vector<ArrayItem> items = ac.returns.get(0).items.map(this::handleNestedArrayConditionals);

                return ac.setResult(items);
            } else {
                if (ac.returns.size() == 0) {
                    return ac.setResult(Vector.of(FalsePrimitive.instance));
                }
                final Vector<ArrayItem> items = ac.returns.get(1).items.map(this::handleNestedArrayConditionals);

                return ac.setResult(items);
            }
        } else {
            int i = 0;
            for (final ConditionTest test : ac.tests) {
                if (evaluate(test)) {
                    final Vector<ArrayItem> items = ac.returns.get(i).items.map(this::handleNestedArrayConditionals);

                    return ac.setResult(items);
                }
                i += 1;
            }
        }
        return ac;
    }
}
