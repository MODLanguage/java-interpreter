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

package uk.modl.interpreter.tokeniser;

import java.util.LinkedList;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * Class to convert a MODL string into a list of MODL grammar tokens.
 */
@UtilityClass
public class Tokeniser {
    /**
     * Convert a MODL String to a list of MODL grammar tokens.
     *
     * @param s a MODL String
     * @return a LinkedList of Tokens
     */
    public static LinkedList<Token> tokenise(@NonNull final String s) {
        return new Context(s).parse();
    }
}
