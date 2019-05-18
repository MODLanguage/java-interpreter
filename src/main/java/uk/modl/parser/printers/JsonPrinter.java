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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.modl.modlObject.ModlObject;
import uk.modl.modlObject.ModlValue;
import uk.modl.parser.RawModlObject;

public class JsonPrinter {
    private static ObjectMapper mapper = new ObjectMapper();

    public static String printModl(RawModlObject modl) throws JsonProcessingException {
        return printModl(modl, true);
    }

    public static String printModl(ModlObject modl) throws JsonProcessingException {
        return printModl(modl, true);
    }

    public static String printModl(ModlValue modlValue) throws JsonProcessingException {
        return printModl(modlValue, true);
    }

    private static String printModl(ModlValue modlValue, boolean pretty) throws JsonProcessingException {
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        if (pretty) {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(modlValue);
        }

        return mapper.writeValueAsString(modlValue);
    }

    public static String printModl(RawModlObject modl, boolean pretty) throws JsonProcessingException {
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        if (pretty) {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(modl);
        }

        return mapper.writeValueAsString(modl);
    }

    public static String printModl(ModlObject modl, boolean pretty) throws JsonProcessingException {
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        ModlObject toConvert = modl;
        if (modl.getStructures().isEmpty()) {
            toConvert = null;
        }
        if (pretty) {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(toConvert);
        }

        return mapper.writeValueAsString(toConvert);
    }

}
