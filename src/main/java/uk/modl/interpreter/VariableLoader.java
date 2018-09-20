package uk.modl.interpreter;

import uk.modl.parser.RawModlObject;

import java.util.List;
import java.util.Map;

/**
 * Created by alex on 20/09/2018.
 */
public class VariableLoader {

    public static void loadConfigNumberedVariables(List<RawModlObject.Value> valueItems, Map<Integer, ModlObject.Value> numberedVariables) { // , int paramNum) {
        if (valueItems != null) {
            for (RawModlObject.Value val : valueItems) {
                addConfigNumberedVariable(val, numberedVariables);
            }
        }
    }

    private static int addConfigNumberedVariable(RawModlObject.Value val, Map<Integer, ModlObject.Value> numberedVariables) {
        int paramNum = numberedVariables.size();
        numberedVariables.put(paramNum, val);
        return paramNum;
    }

    private static void loadConfigVariable(List<RawModlObject.Pair> mapItems, Map<String, ModlObject.Value> variables) {
        // Load in the variable
        for (RawModlObject.Pair mapItem : mapItems) {
            loadConfigVar(mapItem.getKey().string, mapItem, variables);
        }
    }

    public static void loadConfigVar(String key, RawModlObject.Pair pair, Map<String, ModlObject.Value> variables) {
        ModlObject.Value v = pair.getValues().get(0);
        variables.put(key, v);
    }


}
