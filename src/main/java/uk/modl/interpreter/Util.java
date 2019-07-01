package uk.modl.interpreter;

public class Util {
    private Util() {
    }

    public static String degrave(final String s) {
        if (s != null) {
            if (s.startsWith("`") && s.endsWith("`")) {
                return s.substring(1, s.length() - 1);
            }
            return s;
        }
        return null;
    }
}
