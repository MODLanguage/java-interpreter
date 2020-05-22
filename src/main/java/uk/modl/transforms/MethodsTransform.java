package uk.modl.transforms;

import io.vavr.Function2;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import uk.modl.utils.Util;

@RequiredArgsConstructor
public class MethodsTransform implements Function2<String, String, String> {


    /**
     * The context for this invocation of the interpreter
     */
    @NonNull
    @Setter
    private TransformationContext ctx;

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param s argument 1
     * @return the result of function application
     */
    @Override
    public String apply(final String method, final String s) {
        if (s != null) {
            return runMethods(method, s);
        }
        return null;
    }

    private String runMethods(final String method, final String s) {
        final Option<StarMethodTransform.MethodInstruction> maybeMethod = ctx.getMethodByNameOrId(method);
        if (maybeMethod.isDefined()) {
            final StarMethodTransform.MethodInstruction mi = maybeMethod.get();

            final Vector<String> methods = Util.toMethodList(mi.getTransform());
            final String[] refList = methods.toJavaArray(String[]::new);
            return Util.handleMethodsAndTrailingPathComponents(refList, s);
        }
        return s;
    }

}
