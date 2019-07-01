package uk.modl.interpreter;

public class Util {
    private Util() {
    }

    /**
     * Remove graves if both are at the extremeties of the String.
     *
     * @param s the String to be degraved.
     * @return s or the degraved String.
     */
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
