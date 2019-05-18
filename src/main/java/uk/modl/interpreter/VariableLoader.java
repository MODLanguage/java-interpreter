/*
MIT License

Copyright (c) 2018 NUM Technology Ltd

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation
the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of
the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package uk.modl.interpreter;

import uk.modl.modlObject.ModlObject;
import uk.modl.modlObject.ModlValue;
import uk.modl.parser.RawModlObject;

import java.util.Map;

/**
 * Created by alex on 20/09/2018.
 */
class VariableLoader {

    static void loadConfigNumberedVariables(ModlValue value,
                                            Map<Integer, ModlValue> numberedVariables) { // , int paramNum) {
        if (value != null) {
            if (value instanceof ModlObject.Array) {
                for (ModlValue val : ((ModlObject.Array) value).getValues()) {
                    addConfigNumberedVariable(val, numberedVariables);
                }
                return;
            }
            addConfigNumberedVariable(value, numberedVariables);
        }
    }

    private static void addConfigNumberedVariable(ModlValue val, Map<Integer, ModlValue> numberedVariables) {
        int paramNum = numberedVariables.size();
        numberedVariables.put(paramNum, val);
    }

    static void loadConfigVar(String key, RawModlObject.Pair pair, Map<String, ModlValue> variables) {
        ModlValue v = pair.getModlValue();
        variables.put(key, v);
    }


}
