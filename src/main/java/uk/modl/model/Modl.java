package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.Value;
import uk.modl.utils.IDSource;

@Value(staticConstructor = "of")
public class Modl {

    long id;

    @NonNull
    Vector<Structure> structures;

    public static Modl of(final Vector<Structure> structures) {
        return Modl.of(IDSource.nextId(), structures);
    }

    public Modl with(final Vector<Structure> structures) {
        return Modl.of(id, structures);
    }

}
