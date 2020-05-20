package uk.modl.transforms;

import io.vavr.Function1;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import uk.modl.model.*;
import uk.modl.parser.errors.InterpreterError;
import uk.modl.utils.SupertypeInference;

@RequiredArgsConstructor
public class ClassExpansionTransform implements Function1<Structure, Structure> {

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
    public Structure apply(final Structure s) {
        if (s instanceof Pair) {
            final Pair p = (Pair) s;
            if (!p.getKey()
                    .startsWith("*")) {
                return processRecursive(p);
            }
        }

        if (s != null) {
            return processRecursive(s);
        }
        return null;
    }

    private Structure processRecursive(final Structure structure) {

        // Make sure we only process Maps
        if (structure instanceof Array) {
            final Array arr = (Array) structure;
            final Vector<ArrayItem> arrayItems = arr.getArrayItems()
                    .filter(ai -> ai instanceof Map)
                    .map(ai -> processStructure((Structure) ai))
                    .map(ai -> (ArrayItem) ai);
            return new Array(arrayItems);
        } else if (structure instanceof Map) {
            return processStructure(structure);
        } else if (structure instanceof Pair) {
            return processStructure(structure);
        }
        return structure;
    }

    private Structure processStructure(final Structure structure) {
        if (structure instanceof Map) {
            final Map map = (Map) structure;

            final Vector<MapItem> mapItems = map.getMapItems()
                    .map(mi -> (Pair) mi)
                    .map(pair -> {
                        final String key = pair.getKey();
                        final Option<StarClassTransform.ClassInstruction> maybeClass = ctx.getClassByNameOrId(key);

                        if (maybeClass.isDefined()) {
                            return processClass(key, pair.getValue());
                        }
                        return pair.getValue();
                    })
                    .map(o -> (Structure) o)
                    .map(this::processRecursive)
                    .map(s -> (MapItem) s);
            return new Map(mapItems);
        }
        if (structure instanceof Pair) {
            final Pair pair = (Pair) structure;

            final String key = pair.getKey();
            final Option<StarClassTransform.ClassInstruction> maybeClass = ctx.getClassByNameOrId(key);

            if (maybeClass.isDefined()) {
                return processClass(key, pair.getValue());
            }
            if (pair.getValue() instanceof Structure) {
                return processRecursive((Structure) pair.getValue());
            }
        }
        return structure;
    }

    private Structure processClass(final String key, final PairValue value) {
        final Option<StarClassTransform.ClassInstruction> maybeClass = ctx.getClassByNameOrId(key);

        if (maybeClass.isDefined()) {
            final StarClassTransform.ClassInstruction ci = maybeClass.get();
            if (!key.equals(ci.getId()) && !(value instanceof Array || value instanceof Map)) {
                return new Pair(key, value);
            }

            if ((key.equals(ci.getName()) || key.equals(ci.getId())) && !(value instanceof Array)) {
                final Structure newValue = transformToClass(ci, value);
                if (newValue instanceof Array && ((Array) newValue).getArrayItems()
                        .length() == 1) {

                    final String newKey = StringUtils.isEmpty(ci.getName()) ? ci.getId() : ci.getName();
                    return new Pair(newKey, (PairValue) ((Array) newValue).getArrayItems()
                            .get(0));
                } else if (newValue instanceof Pair) {
                    return newValue;
                } else {
                    return new Pair(ci.getName(), (PairValue) newValue);
                }
            } else {
                final Structure tmpValue = transformToClass(ci, value);
                final Structure newValue = (value instanceof Map && ((Map) tmpValue).getMapItems()
                        .isEmpty()) ? (Structure) value : tmpValue;

                return accept(new Pair(key, (PairValue) newValue), ci);
            }
        } else {
            throw new InterpreterError("Class not found: " + key);
        }
    }

    private Structure transformToClass(final StarClassTransform.ClassInstruction ci, final Object value) {
        final boolean ignoreAssign = !(value instanceof Array) || ci.getAssign()
                .isEmpty();

        Vector<ArrayItem> newValue = Vector.empty();

        Object v = processNestedClasses(value);

        Array keys = null;
        if (!ignoreAssign) {
            if (v instanceof Array) {
                final Vector<ArrayItem> arrayItems = toArrayUsingAssign((Array) v, ci);
                v = new Array(arrayItems);
            } else if (!(v instanceof Map)) {
                keys = (Array) ci.getAssign()
                        .find(arr -> ((Array) arr).getArrayItems()
                                .size() == 1)
                        .getOrElseThrow(() -> new InterpreterError("No matching *assign value of length: " + 1));
            } else {
                throw new InterpreterError("cannot use '*assign' to populate a map: " + v);
            }
        }
        if (keys == null) {
            if (v instanceof Map) {
                final Map m = (Map) v;
                newValue = newValue.appendAll(m.getMapItems()
                        .map(ai -> (ArrayItem) ai));
            } else if (v instanceof Array && ((Array) v).getArrayItems()
                    .length() > 0) {

                newValue = ((Array) v).getArrayItems();

            } else if (v instanceof StringPrimitive) {
                final String newKey = StringUtils.isEmpty(ci.getName()) ? ci.getId() : ci.getName();
                return new Pair(newKey, (PairValue) value);
            }
            newValue = newValue
                    .map(item -> {
                        if (item instanceof Pair) {
                            final Pair pair = (Pair) item;
                            if (pair.getValue() instanceof Structure) {
                                return (ArrayItem) processStructure((Structure) pair.getValue());
                            } else {
                                return pair;
                            }
                        } else if (item instanceof StringPrimitive) {
                            return item;
                        } else {
                            final Map map = (Map) item;
                            return (ArrayItem) processStructure(map);

                        }
                    });
            final Structure newStructure = (Structure) processNestedClasses(new Array(newValue));

            return (Structure) inherit(ci.getId(), (PairValue) newStructure);
        } else if (value instanceof StringPrimitive) {
            newValue = newValue.append(new Pair(keys.getArrayItems()
                    .get(0)
                    .toString(), (StringPrimitive) value));
        } else {
            @NonNull final Vector<ArrayItem> arrayItems = keys.getArrayItems();
            Vector<ArrayItem> newArrayItems = Vector.empty();

            for (int i = 0; i < arrayItems.size(); i++) {
                final String key = arrayItems.get(i)
                        .toString();

                assert v instanceof Array;
                final ArrayItem arrayItem = ((Array) v).getArrayItems()
                        .get(i);
                if (arrayItem instanceof Array || arrayItem instanceof Map) {
                    final Structure pv = (Structure) arrayItem;

                    final Structure structure = processStructure(pv);

                    final Option<StarClassTransform.ClassInstruction> classByNameOrId = ctx.getClassByNameOrId(key);
                    if (classByNameOrId.isDefined()) {
                        final StarClassTransform.ClassInstruction classForeKey = classByNameOrId.get();

                        final String newKey = StringUtils.isEmpty(classForeKey.getName()) ? classForeKey.getId() : classForeKey.getName();
                        if (v instanceof Array) {
                            newArrayItems = newArrayItems.append(new Pair(newKey, (PairValue) replacePairInArray((Array) v, i, structure)));
                        } else if (v instanceof Map) {
                            newArrayItems = newArrayItems.append((ArrayItem) replacePairInMap((Map) v, key, newKey, structure));
                        }
                    } else {
                        if (v instanceof Array) {
                            newArrayItems = newArrayItems.append(new Pair(key, (PairValue) replacePairInArray((Array) v, i, pv)));
                        } else if (v instanceof Map) {
                            newArrayItems = newArrayItems.append((ArrayItem) replacePairInMap((Map) v, key, key, pv));
                        }
                    }
                } else {
                    newArrayItems = newArrayItems.append((ArrayItem) processRecursive(new Pair(key, (PairValue) arrayItem)));
                }
            }
            return new Array(newArrayItems);
        }
        return new Array(newValue);
    }

    private Structure replacePairInMap(final Map map, final String oldKey, final String newKey, final Structure newStructure) {
        final Vector<MapItem> pairs = map.getMapItems()
                .map(arrayItem -> (Pair) arrayItem)
                .map(pair -> {
                    if (pair.getKey()
                            .equals(oldKey)) {
                        return new Pair(newKey, (PairValue) newStructure);
                    }
                    return pair;
                });
        return new Map(pairs);
    }

    private Structure replacePairInArray(final Array array, final int index, final Structure newStructure) {
        return new Array(array.getArrayItems()
                .update(index, (ArrayItem) newStructure));
    }

    private Object processNestedClasses(final Object structure) {
        if (structure instanceof Map) {
            final Vector<MapItem> arrayItems = ((Map) structure).getMapItems()
                    .map(mapItem -> {
                        final Pair pair = (Pair) mapItem;
                        final Option<StarClassTransform.ClassInstruction> maybeClass = ctx.getClassByNameOrId(pair.getKey());
                        final PairValue newValue = pair.getValue();

                        if (maybeClass.isDefined()) {
                            final StarClassTransform.ClassInstruction classInstruction = maybeClass.get();
                            final String newKey = StringUtils.isEmpty(classInstruction.getName()) ? classInstruction.getId() : classInstruction.getName();
                            if (newValue instanceof StringPrimitive || newValue instanceof NumberPrimitive) {
                                return new Pair(newKey, newValue);
                            }

                            final Structure s = processClass(newKey, newValue);
                            return (Pair) s;
                        }
                        return mapItem;
                    });
            return new Map(arrayItems);
        } else if (structure instanceof Pair) {
            final Pair pair = (Pair) structure;
            final Option<StarClassTransform.ClassInstruction> maybeClass = ctx.getClassByNameOrId(pair.getKey());
            final PairValue newValue = pair.getValue();

            if (maybeClass.isDefined()) {
                final StarClassTransform.ClassInstruction classInstruction = maybeClass.get();
                final String newKey = StringUtils.isEmpty(classInstruction.getName()) ? classInstruction.getId() : classInstruction.getName();
                if (newValue instanceof StringPrimitive || newValue instanceof NumberPrimitive) {
                    return new Pair(newKey, newValue);
                }

                return processClass(newKey, newValue);
            }
            return structure;
        } else if (structure instanceof Array) {
            final Vector<ArrayItem> arrayItems = ((Array) structure).getArrayItems()
                    .map(this::processNestedClasses)
                    .map(s -> (ArrayItem) s);
            if (arrayItems.nonEmpty()) {

                final Vector<MapItem> mapItems = arrayItems.filter(ai -> ai instanceof MapItem)
                        .map(mi -> (MapItem) mi);
                if (mapItems.size() > 0) {
                    return new Map(mapItems);
                }
                return new Array(arrayItems);
            } else {
                return structure;
            }
        } else {
            return structure;
        }
    }


    /**
     * Expand a Pair if it matches a class
     *
     * @param p  the Pair
     * @param ci the class to transform to
     * @return the expanded Pair
     */
    private Structure accept(final Pair p, final StarClassTransform.ClassInstruction ci) {
        final String newKey = StringUtils.isEmpty(ci.getName()) ? ci.getId() : ci.getName();
        PairValue pairValue;

        var type = SupertypeInference.inferType(ctx, ci, p.getValue());

        switch (type) {
            case "str":
                pairValue = new StringPrimitive(p.getValue()
                        .toString());
                break;
            case "num":
            case "bool":
                pairValue = new NumberPrimitive(p.getValue()
                        .numericValue()
                        .toString());
                break;
            case "arr":
                if (p.getValue() instanceof Array) {
                    pairValue = new Array(((Array) p.getValue()).getArrayItems());
                } else if (p.getValue() instanceof Map) {
                    return (Structure) p.getValue();
                } else {
                    final Vector<ArrayItem> mapItems = Vector.of((ArrayItem) p.getValue());
                    pairValue = new Array(mapItems);
                }
                break;
            case "map":
                if (p.getValue() instanceof Array) {
                    throw new InterpreterError("Cannot convert array to map: " + p.getValue());
                } else if (p.getValue() instanceof Map) {
                    pairValue = inherit(ci.getName(), p.getValue());
                    pairValue = inherit(ci.getName(), pairValue);
                } else {
                    pairValue = new Map(Vector.of(new Pair("value", p.getValue())));
                    pairValue = inherit(ci.getName(), pairValue);
                }
                break;
            default:
                return p;
        }
        return new Pair(newKey, pairValue);
    }

    private Vector<ArrayItem> toArrayUsingAssign(final Array array, final StarClassTransform.ClassInstruction ci) {
        // Find the correct assign statement
        final Option<ArrayItem> maybeAssignArray = ci.getAssign()
                .find(arr -> ((Array) arr).getArrayItems()
                        .size() == array.getArrayItems()
                        .size());

        if (maybeAssignArray.isDefined()) {
            final Array assignArray = (Array) maybeAssignArray.get();
            return array.getArrayItems()
                    .zipWith(assignArray.getArrayItems(), (item, assign) -> {
                        if (item instanceof PairValue) {
                            return (ArrayItem) processNestedClasses(new Pair(assign.toString(), (PairValue) item));
                        } else if (item instanceof ArrayConditional) {
                            return (ArrayItem) processNestedClasses(new Pair(assign.toString(), new Array(((ArrayConditional) item).getResult())));
                        } else {
                            throw new NullPointerException("NEED TO HANDLE : " + item.getClass()
                                    .toString());// TODO: Fix this
                        }
                    });
        }
        return array.getArrayItems();
    }

    private PairValue inherit(final String superclass, final PairValue value) {
        // TODO:
        final Option<StarClassTransform.ClassInstruction> maybeClass = ctx.getClassByNameOrId(superclass);

        if (value instanceof Map) {
            final Vector<MapItem> mapItems =
                    maybeClass.map(this::recurseAddPairs)
                            .getOrElse(Vector.empty())
                            .foldLeft(((Map) value).getMapItems(), Vector::append);

            return new Map(mapItems);
        }
        return value;
    }

    private Vector<Pair> recurseAddPairs(final StarClassTransform.ClassInstruction ci) {
        final Option<StarClassTransform.ClassInstruction> superclass = ctx.getClassByNameOrId(ci.getSuperclass());
        final Vector<Pair> items = (superclass.isDefined()) ? recurseAddPairs(superclass.get()) : Vector.empty();
        return ci.getPairs()
                .appendAll(items);
    }

}
