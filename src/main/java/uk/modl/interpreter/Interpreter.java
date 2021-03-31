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
import com.fasterxml.jackson.databind.node.TextNode;

import lombok.NonNull;
import uk.modl.model.Modl;

/**
 * Interpret a MODL String
 *
 * @author tonywalmsley
 */
public class Interpreter {

  /**
   * Interprets to json string
   * 
   * @param s
   * @returns
   */
  public String interpretToJsonString(@NonNull final String s) {
    final JsonNode jsonObject = this.interpretToJsonObject(s);
    return jsonObject.toString();// TODO: make JSON string
  }

  /**
   * Interprets to pretty json string
   * 
   * @param s
   * @returns
   */
  public String interpretToPrettyJsonString(@NonNull final String s) {
    final JsonNode jsonObject = this.interpretToJsonObject(s);
    return jsonObject.toString();// TODO - pretty-print
  }

  /**
   * Interprets to json object
   * 
   * @param s
   * @returns to json object
   */
  public JsonNode interpretToJsonObject(@NonNull final String s) {
    final Modl modl = this.interpret(s);
    return modlToJson(modl);
  }

  /**
   * TODO:
   * 
   * @param modl
   * @return
   */
  private JsonNode modlToJson(@NonNull final Modl modl) {
    return new TextNode(modl.toString());
  }

  /**
   * Interprets interpreter
   * 
   * @param s
   * @returns
   */
  private Modl interpret(@NonNull final String s) {
    return new Parser().parse(s);
  }

}
