package uk.modl.utils;

public class IDSource {

    private static long ids = 0L;

    public static long nextId() {
        return ids++;
    }

    public static void reset() {
        ids = 0L;
    }

}
