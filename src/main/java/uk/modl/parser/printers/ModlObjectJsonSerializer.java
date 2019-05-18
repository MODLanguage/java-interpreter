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
            //            serialize(structure, gen, serializers);
            ModlValueJsonSerializer.serializeStructure(structure, gen, serializers);
        }
        if (modlObject.getStructures().size() > 1) {
            gen.writeEndArray();
        }
    }

}
