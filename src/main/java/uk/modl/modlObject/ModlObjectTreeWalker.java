package uk.modl.modlObject;

import uk.modl.parser.RawModlObject;

public class ModlObjectTreeWalker {

    private ModlObject modlObject;

    public ModlObjectTreeWalker(final ModlObject modlObject) {
        this.modlObject = modlObject;
    }

    public void walk(final Visitor visitor) {
        for (final ModlObject.Structure structure : modlObject.getStructures()) {
            visitor.visit(structure);
            walk(structure, visitor);
        }
    }

    public void walk(final RawModlObject.ConditionTest conditionTest, final Visitor visitor) {
        conditionTest.getSubConditionMap()
                .forEach((k, v) -> {
                    walk(k, visitor);
                    visitor.visit(v);
                });
    }

    public void walk(final RawModlObject.Condition condition, final Visitor visitor) {
        visitor.visit(condition);
        condition.getValues()
                .forEach(modlValue -> {
                    walk(modlValue, visitor);
                });
    }

    public void walk(final RawModlObject.ConditionGroup conditionGroup, final Visitor visitor) {
        conditionGroup.getConditionsTestList()
                .forEach(visitor::visit);
    }

    public void walk(final RawModlObject.SubCondition subCondition, final Visitor visitor) {
        if (subCondition instanceof RawModlObject.Condition) {
            walk((RawModlObject.Condition) subCondition, visitor);
        }
        if (subCondition instanceof RawModlObject.ConditionGroup) {
            walk((RawModlObject.ConditionGroup) subCondition, visitor);
        }
    }

    public void walk(final ModlValue structure, final Visitor visitor) {

        if (structure instanceof ModlObject.Map) {
            final ModlObject.Map map = (ModlObject.Map) structure;
            for (final ModlObject.Pair pair : map.getPairs()) {
                visitor.visit(pair);
                walk(pair, visitor);
            }
        }

        if (structure instanceof ModlObject.Pair) {
            final ModlObject.Pair pair = (ModlObject.Pair) structure;
            visitor.visit(pair.getKey());
            walk(pair.getModlValue(), visitor);
        }

        if (structure instanceof ModlObject.Array) {
            final ModlObject.Array array = (ModlObject.Array) structure;
            for (final ModlValue value : array.values) {
                visitor.visit(value);
                walk(value, visitor);
            }
        }

        if (structure instanceof ModlObject.Number) {
            final ModlObject.Number number = (ModlObject.Number) structure;
            visitor.visit(number);
        }

        if (structure instanceof ModlObject.String) {
            final ModlObject.String str = (ModlObject.String) structure;
            visitor.visit(str);
        }

        if (structure instanceof ModlObject.True) {
            final ModlObject.True t = (ModlObject.True) structure;
            visitor.visit(t);
        }

        if (structure instanceof ModlObject.False) {
            final ModlObject.False f = (ModlObject.False) structure;
            visitor.visit(f);
        }

        if (structure instanceof ModlObject.Null) {
            final ModlObject.Null n = (ModlObject.Null) structure;
            visitor.visit(n);
        }

        if (structure instanceof RawModlObject.ValueConditional) {
            final RawModlObject.ValueConditional n = (RawModlObject.ValueConditional) structure;
            n.getConditionals()
                    .forEach((key, value) -> {
                        walk(key, visitor);
                        walk(value, visitor);
                    });
        }

        if (structure instanceof RawModlObject.ArrayConditional) {
            final RawModlObject.ArrayConditional n = (RawModlObject.ArrayConditional) structure;
            n.getConditionals()
                    .forEach((k, v) -> {
                        walk(k, visitor);
                        walk(v, visitor);
                    });
        }

        if (structure instanceof RawModlObject.ArrayConditionalReturn) {
            final RawModlObject.ArrayConditionalReturn n = (RawModlObject.ArrayConditionalReturn) structure;
            n.getValues()
                    .forEach(v -> walk(v, visitor));
        }

        if (structure instanceof RawModlObject.SubCondition) {
            final RawModlObject.SubCondition n = (RawModlObject.SubCondition) structure;
            walk(n, visitor);
        }

        if (structure instanceof RawModlObject.MapConditional) {
            final RawModlObject.MapConditional n = (RawModlObject.MapConditional) structure;
            n.getConditionals()
                    .forEach((k, v) -> {
                        walk(k, visitor);
                        v.getPairs()
                                .forEach(p -> walk(p, visitor));
                    });
        }

        if (structure instanceof RawModlObject.TopLevelConditional) {
            final RawModlObject.TopLevelConditional n = (RawModlObject.TopLevelConditional) structure;
            n.getConditionals()
                    .forEach((k, v) -> {
                        walk(k, visitor);
                        walk(v, visitor);
                    });
        }

        if (structure instanceof RawModlObject.TopLevelConditionalReturn) {
            final RawModlObject.TopLevelConditionalReturn n = (RawModlObject.TopLevelConditionalReturn) structure;
            n.getModlValues()
                    .forEach(v -> walk(v, visitor));
        }

        if (structure instanceof RawModlObject.ValueConditionalReturn) {
            final RawModlObject.ValueConditionalReturn n = (RawModlObject.ValueConditionalReturn) structure;
            n.getValues()
                    .forEach(v -> walk(v, visitor));
        }
    }

    public interface Visitor {
        void visit(final Object v);
    }
}
