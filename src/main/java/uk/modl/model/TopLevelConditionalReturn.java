package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import uk.modl.ancestry.Child;
import uk.modl.ancestry.Parent;
import uk.modl.utils.IDSource;


@Value(staticConstructor = "of")
public class TopLevelConditionalReturn implements Structure, Parent, Child {

    @ToString.Exclude
    long id;

    @EqualsAndHashCode.Exclude
    @NonNull
    Vector<Structure> structures;

    public static TopLevelConditionalReturn of(final Vector<Structure> structures) {
        return TopLevelConditionalReturn.of(IDSource.nextId(), structures);
    }

    public TopLevelConditionalReturn with(final Vector<Structure> structures) {
        return TopLevelConditionalReturn.of(id, structures);
    }

}
