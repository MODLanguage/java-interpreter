package uk.modl.ancestry;

import lombok.Value;
import uk.modl.utils.IDSource;

@Value
public class MockPC implements Parent, Child {

    long id;

    public MockPC() {
        this.id = IDSource.nextId();
    }

}
