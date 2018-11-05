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
 * Created by alex on 13/09/2018.
 */
public class ModlObjectJsonSerializer extends JsonSerializer<ModlObject> {
    @Override
    public void serialize(ModlObject modlObject, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (modlObject.getStructures().size() > 1) {
            gen.writeStartArray();
        }
        for (ModlObject.Structure structure : modlObject.getStructures()) {
            serialize(structure, gen, serializers);
        }
        if (modlObject.getStructures().size() > 1) {
            gen.writeEndArray();
        }
    }

    public void serialize(ModlObject.Structure structure, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        serialize(structure, gen, serializers, true);
    }

    public void serialize(ModlObject.Structure structure, JsonGenerator gen, SerializerProvider serializers, boolean startObject) throws IOException {
        if (structure == null) {
            return;
        }
        if (structure instanceof ModlObject.Array) {
            serialize((ModlObject.Array)structure, gen, serializers);
        }
        if (structure instanceof ModlObject.Pair) {
            serialize((ModlObject.Pair)structure, gen, serializers, startObject);
        }
        if (structure instanceof ModlObject.Map) {
            serialize((ModlObject.Map)structure, gen, serializers);
        }
    }

    private void serialize(ModlObject.Map map, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (map == null) {
            return;
        }
        if (map.getPairs() != null) {
            gen.writeStartObject();
            for (ModlObject.Pair pair : map.getPairs()) {
                serialize(pair, gen, serializers, false);
            }
            gen.writeEndObject();
        }
        else {
            gen.writeStartObject();
            gen.writeEndObject();
        }
    }

    public void serialize(ModlValue value, JsonGenerator gen, SerializerProvider serializers, boolean startObject) throws IOException {
        if (value == null) {
            return;
        }
        if (value instanceof ModlObject.Pair) {
            serialize((ModlObject.Pair)value, gen, serializers, startObject);
        }
        if (value instanceof ModlObject.Map) {
            serialize((ModlObject.Map)value, gen, serializers);
        }
        if (value instanceof ModlObject.Array) {
            serialize((ModlObject.Array)value, gen, serializers);
        }
        if (value instanceof ModlObject.Number) {
            serialize((ModlObject.Number)value, gen, serializers);
        }
        if (value instanceof ModlObject.True) {
            serialize((ModlObject.True)value, gen, serializers);
        }
        if (value instanceof ModlObject.False) {
            serialize((ModlObject.False)value, gen, serializers);
        }
        if (value instanceof ModlObject.Null) {
            serialize((ModlObject.Null)value, gen, serializers);
        }
        if (value instanceof ModlObject.String) {
            serialize((ModlObject.String)value, gen, serializers);
        }
    }

    private void serialize(ModlObject.Pair pair, JsonGenerator gen, SerializerProvider serializers, boolean startObject) throws IOException {
        if (pair == null) {
            return;
        }
        if (startObject) {
            gen.writeStartObject();
        }
        gen.writeFieldName(pair.getKey().string);
        serialize(pair.getModlValue(), gen, serializers, startObject);
        if (startObject) {
            gen.writeEndObject();
        }
    }

    private void serialize(ModlObject.Array array, JsonGenerator gen, SerializerProvider serializers) throws IOException {
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
                serialize(value, gen, serializers, startObject);
            }
            gen.writeEndArray();
        } else {
            gen.writeStartArray();
            gen.writeEndArray();
        }
    }

    private void serialize(ModlObject.Number number, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (number == null) {
            return;
        }
        // TODO Will this work?
        gen.writeNumber(number.number);
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
