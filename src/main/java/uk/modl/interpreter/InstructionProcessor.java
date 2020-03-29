package uk.modl.interpreter;

import uk.modl.modlObject.ModlObject;
import uk.modl.modlObject.ModlValue;

import java.util.List;
import java.util.Map;

/**
 * Methods for processing instructions such as %*class, %*id, etc.
 */
class InstructionProcessor {
    private InstructionProcessor() {
    }

    static ModlValue processLoadInstruction(final List<String> filenames) {
        ModlObject.Array result = new ModlObject.Array();

        for (final String filename : filenames) {
            result.addValue(new ModlObject.String(filename));
        }
        return result;
    }

    static ModlValue processClassInstruction(final Map<String, Map<String, Object>> classes, final StringTransformer stringTransformer) {
        final ModlObject.Array result = new ModlObject.Array();

        for (final String key : classes.keySet()) {
            // Create a map of the class details
            final ModlObject.Map map = new ModlObject.Map();
            final ModlObject.String pairKey = new ModlObject.String(key);
            final ModlObject.Map classDetails = new ModlObject.Map();

            final Map<String, Object> klass = classes.get(key);

            addClassField(classDetails, klass, "name", "*name", stringTransformer);
            addClassField(classDetails, klass, "superclass", "*superclass", stringTransformer);
            addClassParams(classDetails, klass);
            addClassField(classDetails, klass, "expect", "*expect", stringTransformer);

            for (final String k : klass.keySet()) {
                if (!k.startsWith("*")) {
                    addClassField(classDetails, klass, k, k, stringTransformer);
                }
            }
            final ModlObject.Pair pair = new ModlObject.Pair(pairKey, classDetails);
            map.addPair(pair);
            result.addValue(map);
        }
        return result;
    }

    private static void addClassField(final ModlObject.Map classDetails,
                                      final Map<String, Object> klass,
                                      final String fieldName,
                                      final String keyName,
                                      final StringTransformer stringTransformer) {
        final Object nameObject = klass.get(keyName);
        final ModlObject.String classKeyObject = new ModlObject.String(fieldName);
        if (nameObject != null) {
            if (nameObject instanceof String) {
                final String value = (String) nameObject;
                final ModlObject.String classValueObject = new ModlObject.String(stringTransformer.transformString(value)
                        .toString());
                classDetails.addPair(new ModlObject.Pair(classKeyObject, classValueObject));
            } else if (nameObject instanceof ModlObject.String) {
                final String value = ((ModlObject.String) nameObject).string;

                ModlObject.String classValueObject = new ModlObject.String(stringTransformer.transformString(value)
                        .toString());
                classDetails.addPair(new ModlObject.Pair(classKeyObject, classValueObject));
            } else if (nameObject instanceof ModlObject.Array) {
                final List<ModlValue> values = ((ModlObject.Array) nameObject).getValues();
                final ModlObject.Array array = new ModlObject.Array();
                for (final ModlValue v : values) {
                    array.addValue(v);
                }
                classDetails.addPair(new ModlObject.Pair(classKeyObject, array));
            } else {
                final String value = "Unknown field type for class " + nameObject.getClass()
                        .getName();
                ModlObject.String classValueObject = new ModlObject.String(value);
                classDetails.addPair(new ModlObject.Pair(classKeyObject, classValueObject));
            }
        } else if (fieldName.equals("superclass")) {
            classDetails.addPair(new ModlObject.Pair(classKeyObject, new ModlObject.Null()));
        }
    }

    private static void addClassParams(ModlObject.Map classDetails,
                                       Map<String, Object> klass) {

        final ModlObject.String classKeyObject = new ModlObject.String("assign");

        final ModlObject.Array paramsArray = new ModlObject.Array();

        for (final String key : klass.keySet()) {
            if (key.startsWith("*param")) {
                final Object nameObject = klass.get(key);

                if (nameObject instanceof List) {
                    @SuppressWarnings("unchecked") final List<ModlObject> values = (List<ModlObject>) nameObject;
                    final ModlObject.Array array = new ModlObject.Array();
                    for (final ModlValue v : values) {
                        array.addValue(v);
                    }
                    paramsArray.addValue(array);
                } else {
                    final String value = "Unknown field type for class " + nameObject.getClass()
                            .getName();
                    throw new RuntimeException(value);
                }
            }
        }

        if (paramsArray.getModlValues()
                .size() > 0) {
            classDetails.addPair(new ModlObject.Pair(classKeyObject, paramsArray));
        }
    }

    static ModlValue processMethodInstruction(List<VariableMethodLoader.MethodDescriptor> methodList) {
        final ModlObject.Array result = new ModlObject.Array();
        for (final VariableMethodLoader.MethodDescriptor mthd : methodList) {
            // Create a map of the transform details
            final ModlObject.Map map = new ModlObject.Map();
            final ModlObject.String pairKey = new ModlObject.String(mthd.id);
            final ModlObject.Map methodDetails = new ModlObject.Map();

            methodDetails.addPair(new ModlObject.Pair(new ModlObject.String("name"), new ModlObject.String(mthd.name)));
            methodDetails.addPair(new ModlObject.Pair(new ModlObject.String("transform"),
                    new ModlObject.String(mthd.transform)));

            final ModlObject.Pair pair = new ModlObject.Pair(pairKey, methodDetails);
            map.addPair(pair);
            result.addValue(map);
        }
        return result;
    }
}
