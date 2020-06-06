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
public class Modl implements Parent, Child {

    @ToString.Exclude
    long id;

    @EqualsAndHashCode.Exclude
    @NonNull
    Vector<Structure> structures;

    public static Modl of(final Vector<Structure> structures) {
        return Modl.of(IDSource.nextId(), structures);
    }

    public Modl with(final Vector<Structure> structures) {
        return Modl.of(id, structures);
    }

}
