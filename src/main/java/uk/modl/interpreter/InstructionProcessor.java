package uk.modl.interpreter;

import uk.modl.modlObject.ModlObject;
import uk.modl.modlObject.ModlValue;
import uk.modl.parser.ModlParsed;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Methods for processing instructions such as %*class, %*id, etc.
 */
public class InstructionProcessor {
    private InstructionProcessor() {
    }

    public static ModlValue processLoadInstruction(final List<String> filenames) {
        final ModlObject modlObject = new ModlObject();
        ModlObject.Array result = modlObject.new Array();

        for (final String filename : filenames) {
            result.addValue(modlObject.new String(filename));
        }
        return result;
    }

    public static ModlValue processClassInstruction(final Map<String, Map<String, Object>> classes) {
        final ModlObject modlObject = new ModlObject();
        final ModlObject.Array result = modlObject.new Array();

        for (final String key : classes.keySet()) {
            // Create a map of the class details
            final ModlObject.Map map = modlObject.new Map();
            final ModlObject.String pairKey = modlObject.new String(key);
            final ModlObject.Map classDetails = modlObject.new Map();

            final Map<String, Object> klass = classes.get(key);

            addClassField(modlObject, classDetails, klass, "name", "*name");
            if (klass.containsKey("object_type")) {
                addClassField(modlObject, classDetails, klass, "superclass", "object_type");
            } else {
                addClassField(modlObject, classDetails, klass, "superclass", "*superclass");
            }
            addClassParams(modlObject, classDetails, klass, "assign");
            addClassField(modlObject, classDetails, klass, "expect", "*expect");

            for (final String k : klass.keySet()) {
                if (!k.startsWith("*")) {
                    addClassField(modlObject, classDetails, klass, k, k);
                }
            }
            final ModlObject.Pair pair = modlObject.new Pair(pairKey, classDetails);
            map.addPair(pair);
            result.addValue(map);
        }
        return result;
    }

    private static void addClassField(ModlObject modlObject, ModlObject.Map classDetails, Map<String, Object> klass, String fieldName, String keyName) {
        final Object nameObject = klass.get(keyName);
        if (nameObject != null) {
            final ModlObject.String classKeyObject = modlObject.new String(fieldName);
            if (nameObject instanceof String) {
                final String value = (String) nameObject;
                final ModlObject.String classValueObject = modlObject.new String(value);
                classDetails.addPair(modlObject.new Pair(classKeyObject, classValueObject));
            } else if (nameObject instanceof ModlObject.String) {
                final String value = ((ModlObject.String) nameObject).string;
                ModlObject.String classValueObject = modlObject.new String(value);
                classDetails.addPair(modlObject.new Pair(classKeyObject, classValueObject));
            } else if (nameObject instanceof ModlObject.Array) {
                final List<ModlValue> values = ((ModlObject.Array) nameObject).getValues();
                final ModlObject.Array array = modlObject.new Array();
                for (final ModlValue v : values) {
                    array.addValue(v);
                }
                classDetails.addPair(modlObject.new Pair(classKeyObject, array));
            } else {
                final String value = "Unknown field type for class " + nameObject.getClass().getName();
                ModlObject.String classValueObject = modlObject.new String(value);
                classDetails.addPair(modlObject.new Pair(classKeyObject, classValueObject));
            }
        }
    }

    private static void addClassParams(ModlObject modlObject, ModlObject.Map classDetails, Map<String, Object> klass, String fieldName) {

        final ModlObject.String classKeyObject = modlObject.new String(fieldName);

        final ModlObject.Array paramsArray = modlObject.new Array();

        for(final String key : klass.keySet()) {
            if(key.startsWith("*param")) {
                final Object nameObject = klass.get(key);

                if (nameObject instanceof List) {
                    final List<ModlObject> values = (List<ModlObject>) nameObject;
                    final ModlObject.Array array = modlObject.new Array();
                    for (final ModlValue v : values) {
                        array.addValue(v);
                    }
                    paramsArray.addValue(array);
                } else {
                    final String value = "Unknown field type for class " + nameObject.getClass().getName();
                }
            }
        }

        if (paramsArray.getModlValues().size() > 0) {
            classDetails.addPair(modlObject.new Pair(classKeyObject, paramsArray));
        }
    }

    public static ModlValue processMethodInstruction(List<VariableMethodLoader.MethodDescriptor> methodList) {
        final ModlObject modlObject = new ModlObject();
        final ModlObject.Array result = modlObject.new Array();
        for (final VariableMethodLoader.MethodDescriptor mthd : methodList) {
            // Create a map of the transform details
            final ModlObject.Map map = modlObject.new Map();
            final ModlObject.String pairKey = modlObject.new String(mthd.id);
            final ModlObject.Map methodDetails = modlObject.new Map();

            //methodDetails.addPair(modlObject.new Pair(modlObject.new String("id"), modlObject.new String(mthd.id)));
            methodDetails.addPair(modlObject.new Pair(modlObject.new String("name"), modlObject.new String(mthd.name)));
            methodDetails.addPair(modlObject.new Pair(modlObject.new String("transform"), modlObject.new String(mthd.transform)));

            final ModlObject.Pair pair = modlObject.new Pair(pairKey, methodDetails);
            map.addPair(pair);
            result.addValue(map);
        }
        return result;
    }
}
