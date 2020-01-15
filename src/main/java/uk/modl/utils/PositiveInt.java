package uk.modl.utils;

public class PositiveInt {
    public final int value;

    public PositiveInt(final int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Value is negative");
        }
        this.value = value;
    }
}
