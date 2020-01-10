package uk.modl.visitor;

import uk.modl.model.*;

/**
 * Extend and override as necessary
 */
public abstract class ModlVisitorBase implements ModlVisitor {

    @Override
    public void accept(final Modl modl) {

    }

    @Override
    public void accept(final Pair pair) {
    }

    @Override
    public void accept(final TopLevelConditional topLevelConditional) {

    }

    @Override
    public void accept(final Array array) {

    }

    @Override
    public void accept(final ArrayConditional arrayConditional) {

    }

    @Override
    public void accept(final ArrayConditionalReturn arrayConditionalReturn) {

    }

    @Override
    public void accept(final Condition condition) {

    }

    @Override
    public void accept(final ConditionGroup conditionGroup) {

    }

    @Override
    public void accept(final ConditionTest conditionTest) {

    }

    @Override
    public void accept(final Map map) {

    }

    @Override
    public void accept(final MapConditional mapConditional) {

    }

    @Override
    public void accept(final MapConditionalReturn mapConditionalReturn) {

    }

    @Override
    public void accept(final NegatedCondition negatedCondition) {

    }

    @Override
    public void accept(final NegatedConditionGroup negatedConditionGroup) {

    }

    @Override
    public void accept(final TopLevelConditionalReturn topLevelConditionalReturn) {

    }

    @Override
    public void accept(final ValueConditional valueConditional) {

    }

    @Override
    public void accept(final ValueConditionalReturn valueConditionalReturn) {

    }

    @Override
    public void accept(final NullPrimitive nullPrimitive) {

    }

    @Override
    public void accept(final FalsePrimitive falsePrimitive) {

    }

    @Override
    public void accept(final TruePrimitive truePrimitive) {

    }

    @Override
    public void accept(final NumberPrimitive numberPrimitive) {

    }

    @Override
    public void accept(final StringPrimitive stringPrimitive) {

    }
}
