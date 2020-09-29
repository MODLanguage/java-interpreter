/*
 * MIT License
 *
 * Copyright (c) 2020 NUM Technology Ltd
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package uk.modl.transforms;

import lombok.RequiredArgsConstructor;
import lombok.val;
import uk.modl.utils.Util;

@RequiredArgsConstructor
public class MethodsTransform {


    /**
     * Applies this function to one argument and returns the result.
     *
     * @param ctx    TransformationContext
     * @param method String
     * @param s      String
     * @return the result of function application
     */
    public String apply(final TransformationContext ctx, final String method, final String s) {
        if (s != null) {
            return runMethods(ctx, method, s);
        }
        return null;
    }

    private String runMethods(final TransformationContext ctx, final String method, final String s) {
        val maybeMethod = ctx.getMethodByNameOrId(method);

        return maybeMethod.map(mi -> {
            val methods = Util.toMethodList(mi.getTransform());

            val refList = methods.toJavaArray(String[]::new);

            return Util.handleMethodsAndTrailingPathComponents(refList, s);
        })
                .getOrElse(s);
    }

}
