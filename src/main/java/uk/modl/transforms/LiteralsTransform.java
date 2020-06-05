package uk.modl.transforms;

import uk.modl.model.StringPrimitive;
import uk.modl.model.ValueItem;
import uk.modl.utils.Util;

import java.util.regex.Matcher;

public class LiteralsTransform {

    public static ValueItem replacellLiteralRefs(final String s) {
        if (s == null) {
            return null;
        }
        final Matcher matcher = ReferencesTransform.literalsPattern.matcher(s);

        // Gather the match groups into a list of references
        String result = s;
        while (matcher.find()) {
            final String match = matcher.group();
            result = result.replace(match, Util.unwrapLiteral(match));
        }
        return new StringPrimitive(result);
    }

}
