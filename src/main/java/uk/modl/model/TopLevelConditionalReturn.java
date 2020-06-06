package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.Value;
import uk.modl.utils.IDSource;


@Value(staticConstructor = "of")
public class TopLevelConditionalReturn implements Structure {

    long id;

    @NonNull
    Vector<Structure> structures;

    public static TopLevelConditionalReturn of(final Vector<Structure> structures) {
        return TopLevelConditionalReturn.of(IDSource.nextId(), structures);
    }

    public TopLevelConditionalReturn with(final Vector<Structure> structures) {
        return TopLevelConditionalReturn.of(id, structures);
    }

}
