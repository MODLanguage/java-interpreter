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
import lombok.RequiredArgsConstructor;

/**
 * A Simple stream of Tokens that supports peeking and pushback.
 */
@RequiredArgsConstructor
public class TokenStream {
    @NonNull
    private final LinkedList<Token> tokens;

    /**
     * Consume the next Token if there is one.
     *
     * @return a Token or null
     */
    public Token next() {
        if (tokens.isEmpty()) {
            return null;
        }
        return tokens.remove(0);
    }

    /**
     * Get the next Token if there is one without consuming it.
     *
     * @return a Token or null
     */
    public Token peek() {
        if (tokens.isEmpty()) {
            return null;
        }
        return tokens.get(0);
    }

    /**
     * Return an unconsumed Token.
     *
     * @param t a Token - this should be the most recent token returned from next()
     */
    public void pushback(@NonNull final Token t) {
        tokens.add(0, t);
    }

    /**
     * Check whether there are any more tokens.
     *
     * @return true if the stream is empty
     */
    public boolean isEmpty() {
        return tokens.isEmpty();
    }
}
