package uk.modl.modlObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by alex on 05/11/2018.
 */
public interface ModlValue {
    default ModlValue get(String name) { throw new UnsupportedOperationException(); }

    default ModlValue get(Integer index) { throw new UnsupportedOperationException(); }

    default List<String> getKeys() { throw new UnsupportedOperationException(); }

    default Object getValue() { throw new UnsupportedOperationException(); }

    default List<? extends ModlValue> getModlValues() { return new LinkedList<>(); };

    default boolean isModlObject() { return false; }
    default boolean isStructure() { return false; }
    default boolean isArray() { return false; }
    default boolean isMap() { return false; }
    default boolean isPair() { return false; }
    default boolean isTerminal() { return false; }
    default boolean isString() { return false; }
    default boolean isNumber() { return false; }
    default boolean isFalse() { return false; }
    default boolean isTrue() { return false; }
    default boolean isNull() { return false; }
}
