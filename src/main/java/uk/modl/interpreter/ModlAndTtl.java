package uk.modl.interpreter;

import uk.modl.parser.RawModlObject;

/**
 * Created by alex on 20/09/2018.
 */
public class ModlAndTtl {
    public final long endTime;
    public final RawModlObject rawModlObject;
    public ModlAndTtl(long endTime, RawModlObject rawModlObject) {
        this.endTime = endTime;
        this.rawModlObject = rawModlObject;
    }
}
