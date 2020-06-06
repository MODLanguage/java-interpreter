package uk.modl.transforms;

import uk.modl.utils.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LiteralsTransform {

    public static final Pattern literalsPattern = Pattern.compile("(%`.+`)");

    public static String replacellLiteralRefs(final String s) {
        if (s == null) {
            return null;
        }
        final Matcher matcher = literalsPattern.matcher(s);

        // Gather the match groups into a list of references
        String result = s;
        while (matcher.find()) {
            final String match = matcher.group();
            result = result.replace(match, Util.unwrapLiteral(match));
        }
        return result;
    }

}
