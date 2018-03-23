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

package uk.modl.parser.printers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import uk.modl.parser.ModlObject;

import java.io.IOException;

public class ModlJsonSerializer extends JsonSerializer<ModlObject> {
    @Override
    public void serialize(ModlObject modlObject, JsonGenerator gen, SerializerProvider serializers) throws IOException {
//        boolean closeObject = false;
//        if (modlObject.getValues().size() == 1 && modlObject.getValues().get(0).getPair() != null) {
//            gen.writeStartObject();
//            closeObject = true;
//        }
        if (modlObject.getStructures().size() > 1) {
            gen.writeStartArray();
        }
        for (ModlObject.Structure structure : modlObject.getStructures()) {
            serialize(structure, gen, serializers);
        }
        if (modlObject.getStructures().size() > 1) {
            gen.writeEndArray();
        }
//        if (closeObject) {
//            gen.writeEndObject();
//        }
    }

    public void serialize(ModlObject.Structure structure, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        serialize(structure, gen, serializers, true);
    }
    public void serialize(ModlObject.Structure structure, JsonGenerator gen, SerializerProvider serializers, boolean startObject) throws IOException {
        if (structure == null) {
            return;
        }
        serialize(structure.getArray(), gen, serializers);
        serialize(structure.getPair(), gen, serializers, startObject);
        serialize(structure.getMap(), gen, serializers);

    }

    public void serialize(ModlObject.Value value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        serialize(value, gen, serializers, true);
    }
    public void serialize(ModlObject.Value value, JsonGenerator gen, SerializerProvider serializers, boolean startObject) throws IOException {
        if (value == null) {
            return;
        }
        if (value.getPair() != null) {
            serialize(value.getPair(), gen, serializers, startObject);
        }
        if (value.getMap() != null) {
            serialize(value.getMap(), gen, serializers);
        }
        if (value.getArray() != null) {
            serialize(value.getArray(), gen, serializers);
        }
        serialize(value.getQuoted(), gen, serializers);
        serialize(value.getNumber(), gen, serializers);
        serialize(value.getTrueVal(), gen, serializers);
        serialize(value.getFalseVal(), gen, serializers);
        serialize(value.getNullVal(), gen, serializers);
        serialize(value.getString(), gen, serializers);
    }

    private void serialize(ModlObject.Map map, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (map == null) {
            return;
        }
        if (map.getMapItems() != null) {
            gen.writeStartObject();
            for (ModlObject.MapItem mapItem : map.getMapItems()) {
                serialize(mapItem, gen, serializers, false);
            }
            gen.writeEndObject();
        }
    }

    private void serialize(ModlObject.MapItem mapItem, JsonGenerator gen, SerializerProvider serializers, boolean startObject) throws IOException {
        if (mapItem == null) {
            return;
        }
        if (mapItem.getPair() != null) {
                serialize(mapItem.getPair(), gen, serializers, false);
        }
    }

    private void serialize(ModlObject.Array array, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (array == null) {
            return;
        }
        if (array.getArrayItems() != null) {
            gen.writeStartArray();
            for (ModlObject.ArrayItem arrayItem : array.getArrayItems()) {
                serialize(arrayItem, gen, serializers, false);
            }
            gen.writeEndArray();
        }
    }

    private void serialize(ModlObject.ArrayItem arrayItem, JsonGenerator gen, SerializerProvider serializers, boolean startObject) throws IOException {
        if (arrayItem == null) {
            return;
        }
        if (arrayItem.getValue() != null) {
                serialize(arrayItem.getValue(), gen, serializers);
        }
    }

    private void serialize(ModlObject.Pair pair, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        serialize(pair, gen, serializers, true);
    }

    private void serialize(ModlObject.Pair pair, JsonGenerator gen, SerializerProvider serializers, boolean startObject) throws IOException {
        if (pair == null) {
            return;
        }
        if (startObject) {
            gen.writeStartObject();
        }
            gen.writeFieldName(pair.getKey());
            if (pair.getValueItems() != null) {
                if (pair.getValueItems().size() > 1) {
                    gen.writeStartArray();
                }
                for (ModlObject.ValueItem value : pair.getValueItems()) {
                    serialize(value, gen, serializers);
                }
                if (pair.getValueItems().size() > 1) {
                    gen.writeEndArray();
                }
            } else if (pair.getArray() != null) {
                serialize(pair.getArray(), gen, serializers);
            } else if (pair.getMap() != null) {
                serialize(pair.getMap(), gen, serializers);
            }
        if (startObject) {
            gen.writeEndObject();
        }
    }


    private void serialize(ModlObject.ValueItem valueItem, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            serialize(valueItem, gen, serializers, true);
    }
    private void serialize(ModlObject.ValueItem valueItem, JsonGenerator gen, SerializerProvider serializers, boolean startObject) throws IOException {
        if (valueItem == null) {
            return;
        }
//        if (values.size() > 1 || values.get(0).getStructure() != null) {
//            gen.writeStartObject();
//        }
//        for (ModlObject.Value value : values) {
//            serialize(valueItem.getValue(), gen, serializers, false);
        if (valueItem.getValue() != null) {
            serialize(valueItem.getValue(), gen, serializers, true);
        }
        if (valueItem.getValueItems() != null) {
            if (startObject && valueItem.getValueItems().size() > 1) {
                gen.writeStartArray();
            }
            for (ModlObject.ValueItem vi : valueItem.getValueItems()) {
//                serialize(vi, gen, serializers, false);
                serialize(vi, gen, serializers, true);
            }
            if (startObject && valueItem.getValueItems().size() > 1) {
                gen.writeEndArray();
            }
        }
//        }
//        if (values.size() > 1 || values.get(0).getStructure() != null) {
//            gen.writeEndObject();
//        }
    }

    private void serialize(ModlObject.Quoted quoted, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (quoted == null) {
            return;
        }
        gen.writeString(quoted.string);
    }

    private void serialize(ModlObject.Number number, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (number == null) {
            return;
        }
        // TODO Will this work?
        gen.writeNumber(number.string);
    }

    private void serialize(ModlObject.True trueVal, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (trueVal == null) {
            return;
        }
        gen.writeBoolean(true);
    }

    private void serialize(ModlObject.False falseVal, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (falseVal == null) {
            return;
        }
        gen.writeBoolean(false);
    }

    private void serialize(ModlObject.Null nullVal, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (nullVal == null) {
            return;
        }
        gen.writeNull();
    }

    private void serialize(ModlObject.String string, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (string == null) {
            return;
        }
        if (string.string != null) {
            gen.writeString(string.string);
        } else {
            gen.writeString("");
        }
    }

}
