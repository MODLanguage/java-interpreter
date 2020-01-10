package uk.modl.visitor;

import uk.modl.model.*;

public interface ModlVisitor {
    void accept(final Modl modl);

    void accept(final Pair pair);

    void accept(final TopLevelConditional topLevelConditional);

    void accept(final Array array);

    void accept(final ArrayConditional arrayConditional);

    void accept(final ArrayConditionalReturn arrayConditionalReturn);

    void accept(final Condition condition);

    void accept(final ConditionGroup conditionGroup);

    void accept(final ConditionTest conditionTest);

    void accept(final Map map);

    void accept(final MapConditional mapConditional);

    void accept(final MapConditionalReturn mapConditionalReturn);

    void accept(final NegatedCondition negatedCondition);

    void accept(final NegatedConditionGroup negatedConditionGroup);

    void accept(final TopLevelConditionalReturn topLevelConditionalReturn);

    void accept(final ValueConditional valueConditional);

    void accept(final ValueConditionalReturn valueConditionalReturn);

    void accept(final NullPrimitive nullPrimitive);

    void accept(final FalsePrimitive falsePrimitive);

    void accept(final TruePrimitive truePrimitive);

    void accept(final NumberPrimitive numberPrimitive);

    void accept(final StringPrimitive stringPrimitive);
}
