/*
 * MIT License
 *
 * Copyright (c) 2020 NUM Technology Ltd
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package uk.modl.interpreter;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.NonNull;
import uk.modl.interpreter.model.Modl;
import uk.modl.interpreter.parser.Parser;

/**
 * Interpret a MODL String
 *
 * @author tonywalmsley
 */
public class Interpreter {

    /**
     * Interprets to json string
     *
     * @param s String
     * @return String
     */
    public String interpretToJsonString(@NonNull final String s) {
        final JsonNode jsonObject = this.interpretToJsonObject(s);
        return JsonToString.convert(jsonObject);
    }

    /**
     * Interprets to pretty json string
     *
     * @param s String
     * @return String
     */
    public String interpretToPrettyJsonString(@NonNull final String s) {
        final JsonNode jsonObject = this.interpretToJsonObject(s);
        return JsonToString.convertPretty(jsonObject);
    }

    /**
     * Interprets to json object
     *
     * @param s String
     * @return JsonNode
     */
    public JsonNode interpretToJsonObject(@NonNull final String s) {
        final Modl modl = this.interpret(s);
        return ModlToJson.convert(modl);
    }

    /**
     * Interprets to Modl object
     *
     * @param s String
     * @return Modl
     */
    public Modl interpret(@NonNull final String s) {
        return new Parser().parseModl(s);
    }

}
