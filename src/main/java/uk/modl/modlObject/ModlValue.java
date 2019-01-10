package uk.modl.modlObject;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import uk.modl.parser.printers.ModlObjectJsonSerializer;
import uk.modl.parser.printers.ModlValueJsonSerializer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by alex on 05/11/2018.
 */
@JsonSerialize(using = ModlValueJsonSerializer.class)
public abstract class ModlValue {
    public ModlValue get(String name) { throw new UnsupportedOperationException(); }

    public ModlValue get(Integer index) { throw new UnsupportedOperationException(); }

    public List<String> getKeys() { throw new UnsupportedOperationException(); }

    public Object getValue() { throw new UnsupportedOperationException(); }

    public List<? extends ModlValue> getModlValues() { return new LinkedList<>(); };

    public boolean isModlObject() { return false; }
    public boolean isStructure() { return false; }
    public boolean isArray() { return false; }
    public boolean isMap() { return false; }
    public boolean isPair() { return false; }
    public boolean isTerminal() { return false; }
    public boolean isString() { return false; }
    public boolean isNumber() { return false; }
    public boolean isFalse() { return false; }
    public boolean isTrue() { return false; }
    public boolean isNull() { return false; }
}
