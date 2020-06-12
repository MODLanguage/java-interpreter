package uk.modl.transforms;

import io.vavr.collection.Vector;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import uk.modl.utils.Util;

@RequiredArgsConstructor
public class MethodsTransform {


    /**
     * Applies this function to one argument and returns the result.
     *
     * @param s argument 1
     * @return the result of function application
     */
    public String apply(final TransformationContext ctx, final String method, final String s) {
        if (s != null) {
            return runMethods(ctx, method, s);
        }
        return null;
    }

    private String runMethods(final TransformationContext ctx, final String method, final String s) {
        final Option<StarMethodTransform.MethodInstruction> maybeMethod = ctx.getMethodByNameOrId(method);

        return maybeMethod.map(mi -> {
            final Vector<String> methods = Util.toMethodList(mi.getTransform());

            final String[] refList = methods.toJavaArray(String[]::new);

            return Util.handleMethodsAndTrailingPathComponents(refList, s);
        })
                .getOrElse(s);
    }

}
