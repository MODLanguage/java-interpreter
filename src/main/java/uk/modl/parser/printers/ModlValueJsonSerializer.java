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
import uk.modl.modlObject.ModlObject;
import uk.modl.modlObject.ModlValue;

import java.io.IOException;

/**
 * Created by alex on 08/11/2018.
 */
public class ModlValueJsonSerializer extends JsonSerializer<ModlValue> {

    @Override
    public void serialize(ModlValue value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        serializeInternal(value, gen ,serializers, false);
    }

    public static void serializeStructure(ModlObject.Structure structure, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        serializeInternal(structure, gen, serializers);
    }

    private static void serializeInternal(ModlObject.Structure structure, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        serializeInternal(structure, gen, serializers, true);
    }

    public static void serializeInternal(ModlObject.Structure structure, JsonGenerator gen, SerializerProvider serializers, boolean startObject) throws IOException {
        if (structure == null) {
            return;
        }
        if (structure instanceof ModlObject.Array) {
            serializeInternal((ModlObject.Array)structure, gen, serializers);
        }
        if (structure instanceof ModlObject.Pair) {
            serializeInternal((ModlObject.Pair)structure, gen, serializers, startObject);
        }
        if (structure instanceof ModlObject.Map) {
            serializeInternal((ModlObject.Map)structure, gen, serializers);
        }
    }

    private static void serializeInternal(ModlObject.Map map, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (map == null) {
            return;
        }
        if (map.getPairs() != null) {
            gen.writeStartObject();
            for (ModlObject.Pair pair : map.getPairs()) {
                serializeInternal(pair, gen, serializers, false);
            }
            gen.writeEndObject();
        }
        else {
            gen.writeStartObject();
            gen.writeEndObject();
        }
    }

    public static void serializeInternal(ModlValue value, JsonGenerator gen, SerializerProvider serializers, boolean startObject) throws IOException {
        if (value == null) {
            return;
        }
        if (value instanceof ModlObject.Pair) {
            serializeInternal((ModlObject.Pair)value, gen, serializers, startObject);
        }
        if (value instanceof ModlObject.Map) {
            serializeInternal((ModlObject.Map)value, gen, serializers);
        }
        if (value instanceof ModlObject.Array) {
            serializeInternal((ModlObject.Array)value, gen, serializers);
        }
        if (value instanceof ModlObject.Number) {
            serializeInternal((ModlObject.Number)value, gen, serializers);
        }
        if (value instanceof ModlObject.True) {
            serializeInternal((ModlObject.True)value, gen, serializers);
        }
        if (value instanceof ModlObject.False) {
            serializeInternal((ModlObject.False)value, gen, serializers);
        }
        if (value instanceof ModlObject.Null) {
            serializeInternal((ModlObject.Null)value, gen, serializers);
        }
        if (value instanceof ModlObject.String) {
            serializeInternal((ModlObject.String)value, gen, serializers);
        }
    }

    private static void serializeInternal(ModlObject.Pair pair, JsonGenerator gen, SerializerProvider serializers, boolean startObject) throws IOException {
        if (pair == null) {
            return;
        }
        if (startObject) {
            gen.writeStartObject();
        }
        gen.writeFieldName(pair.getKey().string);
        boolean newStartObject = startObject;
        if (pair.getModlValue() instanceof ModlObject.Pair && !startObject) {
            newStartObject = true;
        }
        serializeInternal(pair.getModlValue(), gen, serializers, newStartObject);
        if (startObject) {
            gen.writeEndObject();
        }
    }

    private static void serializeInternal(ModlObject.Array array, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (array == null) {
            return;
        }
        if (array.getValues() != null) {
            gen.writeStartArray();
            for (ModlValue value : array.getValues()) {
                boolean startObject = false;
                if (value instanceof ModlObject.Pair) {
                    startObject = true;
                }
                serializeInternal(value, gen, serializers, startObject);
            }
            gen.writeEndArray();
        } else {
            gen.writeStartArray();
            gen.writeEndArray();
        }
    }

    private static void serializeInternal(ModlObject.Number number, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (number == null) {
            return;
        }
        // TODO Will this work?
        gen.writeNumber(number.number);
    }

    private static void serializeInternal(ModlObject.True trueVal, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (trueVal == null) {
            return;
        }
        gen.writeBoolean(true);
    }

    private static void serializeInternal(ModlObject.False falseVal, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (falseVal == null) {
            return;
        }
        gen.writeBoolean(false);
    }

    private static void serializeInternal(ModlObject.Null nullVal, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (nullVal == null) {
            return;
        }
        gen.writeNull();
    }

    private static void serializeInternal(ModlObject.String string, JsonGenerator gen, SerializerProvider serializers) throws IOException {
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
