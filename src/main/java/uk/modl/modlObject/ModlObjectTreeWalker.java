package uk.modl.modlObject;

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
            visitor.visit(pair.getValue());
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
    }

    public interface Visitor {
        void visit(final Object v);
    }
}
