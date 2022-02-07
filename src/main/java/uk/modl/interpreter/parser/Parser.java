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

package uk.modl.interpreter.parser;

import static io.vavr.API.Left;
import static io.vavr.API.Right;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.vavr.control.Either;
import lombok.NonNull;
import uk.modl.interpreter.model.Modl;
import uk.modl.interpreter.model.ModlArray;
import uk.modl.interpreter.model.ModlBoolNull;
import uk.modl.interpreter.model.ModlFloat;
import uk.modl.interpreter.model.ModlInteger;
import uk.modl.interpreter.model.ModlMap;
import uk.modl.interpreter.model.ModlPair;
import uk.modl.interpreter.model.ModlPrimitive;
import uk.modl.interpreter.model.ModlQuoted;
import uk.modl.interpreter.model.ModlString;
import uk.modl.interpreter.model.ModlStructure;
import uk.modl.interpreter.model.ModlValue;
import uk.modl.interpreter.tokeniser.Token;
import uk.modl.interpreter.tokeniser.TokenStream;
import uk.modl.interpreter.tokeniser.TokenType;
import uk.modl.interpreter.tokeniser.Tokeniser;

public class Parser {
    public Modl parseModl(@NonNull final String s) {
        final LinkedList<Token> tokens = Tokeniser.tokenise(s);
        final Either<ModlPrimitive, List<ModlStructure>> parsed = parse(new TokenStream(tokens));
        return new Modl(parsed);
    }

    private Either<ModlPrimitive, List<ModlStructure>> parse(@NonNull final TokenStream s) {
        final ModlPrimitive rootPrimitive = parsePrimitive(s);
        if (rootPrimitive != null) {
            return Left(rootPrimitive);
        }
        return Right(parseStructures(s));
    }

    private ModlPrimitive parsePrimitive(@NonNull final TokenStream s) {
        ModlPrimitive result;
        final Token tok = s.next();
        switch (tok.getType()) {
            case LPAREN:
            case RPAREN:
            case LBRACKET:
            case RBRACKET:
            case EQUALS: {
                if (s.isEmpty()) {
                    throw new ParserException(String.format("Unexpected token: %s", tok));
                }
                s.pushback(tok);
                return null;
            }
            case NULL: {
                result = ModlBoolNull.MODL_NULL;
                break;
            }
            case TRUE: {
                result = ModlBoolNull.MODL_TRUE;
                break;
            }
            case FALSE: {
                result = ModlBoolNull.MODL_FALSE;
                break;
            }
            case QUOTED: {
                result = new ModlQuoted((String) tok.getValue());
                break;
            }
            case STRING: {
                result = new ModlString((String) tok.getValue());
                break;
            }
            case INTEGER: {
                result = new ModlInteger((Integer) tok.getValue());
                break;
            }
            case FLOAT: {
                result = new ModlFloat((Float) tok.getValue());
                break;
            }
            default: {
                throw new ParserException(String.format("Unknown token type in: %s", tok));
            }
        }
        final Token peek = s.peek();
        if (peek != null && peek.getType() == TokenType.STRUCT_SEP) {
            throw new ParserException("Only one primitive is allowed at the root.");
        }
        if (
                peek != null &&
                        (peek.getType() == TokenType.LPAREN || peek.getType() == TokenType.LBRACKET
                                || peek.getType() == TokenType.EQUALS)
        ) {
            // Its not a primitive
            s.pushback(tok);
            return null;
        } else if (peek != null) {
            throw new ParserException(String.format("Unexpected token: %s", peek));
        }
        return result;
    }

    private List<ModlStructure> parseStructures(@NonNull final TokenStream s) {
        final List<ModlStructure> result = new ArrayList<>();
        while (!s.isEmpty()) {
            result.add((ModlStructure) parseModlValue(s));
            final Token maybeStructSep = s.next();
            if (maybeStructSep != null) {
                if (maybeStructSep.getType() != TokenType.STRUCT_SEP) {
                    throw new ParserException(String.format("Expected ';' near %s", maybeStructSep));
                }
            }
        }
        return result;
    }

    private ModlMap parseModlMap(@NonNull final TokenStream s) {
        final Token firstToken = s.next();
        // Its a map
        final List<ModlPair> mapEntries = new ArrayList<>();
        while (!s.isEmpty()) {
            Token peek = s.peek();
            if (peek != null && peek.getType() == TokenType.RPAREN) {
                // Consume the peeked token and break
                s.next();
                break;
            }
            final ModlValue mp = parseModlValue(s);
            mapEntries.add((ModlPair) mp);

            peek = s.peek();
            if (peek != null) {
                if (peek.getType() == TokenType.RPAREN) {
                    // Consume the peeked token
                    s.next();
                    break;
                }
                if (peek.getType() == TokenType.STRUCT_SEP) {
                    // Consume the peeked token and continue
                    s.next();
                    peek = s.peek();
                    if (peek != null && peek.getType() == TokenType.RPAREN) {
                        throw new ParserException(String.format("Unexpected ; before ] at %s", peek));
                    }
                }
            } else {
                throw new ParserException(String.format("Expected ')' near %s", firstToken));
            }
        }
        return new ModlMap(mapEntries);
    }

    private ModlArray parseModlArray(@NonNull final TokenStream s) {
        final Token firstToken = s.next();
        // Its an array
        final List<ModlValue> arrayEntries = new ArrayList<>();
        while (!s.isEmpty()) {
            Token peek = s.peek();
            if (peek != null && peek.getType() == TokenType.RBRACKET) {
                // Consume the peeked token and break
                s.next();
                break;
            }
            final ModlValue ms = parseModlValue(s);
            arrayEntries.add(ms);

            peek = s.peek();
            if (peek != null) {
                if (peek.getType() == TokenType.RBRACKET) {
                    // Consume the peeked token and break
                    s.next();
                    break;
                }
                if (peek.getType() == TokenType.STRUCT_SEP) {
                    // Consume the peeked token and continue
                    s.next();
                    peek = s.peek();
                    if (peek != null && peek.getType() == TokenType.RBRACKET) {
                        throw new ParserException(String.format("Unexpected ; before ] at %s", peek));
                    }
                }
            } else {
                throw new ParserException(String.format("Expected ']' near %s", firstToken));
            }
        }
        return new ModlArray(arrayEntries);
    }

    private ModlValue parseModlValue(@NonNull final TokenStream s) {
        final Token firstToken = s.next();

        if (firstToken.getType() == TokenType.LBRACKET) {
            s.pushback(firstToken);
            return parseModlArray(s);
        } else if (firstToken.getType() == TokenType.LPAREN) {
            s.pushback(firstToken);
            return parseModlMap(s);
        } else if (firstToken.getType() == TokenType.STRING || firstToken.getType() == TokenType.QUOTED) {
            final Token peek = s.peek();

            final String key = (String) firstToken.getValue();
            if (peek != null && peek.getType() == TokenType.EQUALS) {
                // its a pair
                // Consume the = token
                s.next();
                return new ModlPair(key, (ModlStructure) parsePairValue(s));
            }

            if (peek != null && (peek.getType() == TokenType.LBRACKET || peek.getType() == TokenType.LPAREN)) {
                // Its still a pair
                return new ModlPair(key, (ModlStructure) parsePairValue(s));
            }

            if (peek == null || peek.getType() == TokenType.STRUCT_SEP || peek.getType() == TokenType.RPAREN
                    || peek.getType() == TokenType.RBRACKET) {
                // Its simply a string or quoted string
                if (firstToken.getType() == TokenType.STRING) {
                    return new ModlString((String) firstToken.getValue());
                } else {
                    return new ModlQuoted((String) firstToken.getValue());
                }
            }

            throw new ParserException(String.format("Unexpected token: '%s'", firstToken));
        } else if (firstToken.getType() == TokenType.INTEGER) {
            return new ModlInteger((Integer) firstToken.getValue());
        } else if (firstToken.getType() == TokenType.FLOAT) {
            return new ModlFloat((Float) firstToken.getValue());
        } else if (firstToken.getType() == TokenType.NULL) {
            return ModlBoolNull.MODL_NULL;
        } else if (firstToken.getType() == TokenType.TRUE) {
            return ModlBoolNull.MODL_TRUE;
        } else if (firstToken.getType() == TokenType.FALSE) {
            return ModlBoolNull.MODL_FALSE;
        } else {
            s.pushback(firstToken);
            final ModlPrimitive maybePrimitive = parsePrimitive(s);
            if (maybePrimitive != null) {
                return maybePrimitive;
            }
        }
        throw new ParserException(String.format("Unexpected token: '%s'", firstToken));
    }

    private ModlValue parsePairValue(@NonNull final TokenStream s) {
        final Token firstToken = s.next();

        if (firstToken.getType() == TokenType.LBRACKET) {
            s.pushback(firstToken);
            return parseModlArray(s);
        } else if (firstToken.getType() == TokenType.LPAREN) {
            s.pushback(firstToken);
            return parseModlMap(s);
        } else if (firstToken.getType() == TokenType.STRING || firstToken.getType() == TokenType.QUOTED) {
            final Token peek = s.peek();

            if (peek != null && peek.getType() == TokenType.EQUALS) {
                throw new ParserException(String.format("Unexpected token: '%s'", firstToken));
            }

            if (peek != null && (peek.getType() == TokenType.LBRACKET || peek.getType() == TokenType.LPAREN)) {
                // Its still a pair
                throw new ParserException(String.format("Unexpected token: '%s'", firstToken));
            }

            if (peek == null || peek.getType() == TokenType.STRUCT_SEP || peek.getType() == TokenType.RPAREN
                    || peek.getType() == TokenType.RBRACKET) {
                // Its simply a string or quoted string
                if (firstToken.getType() == TokenType.STRING) {
                    return new ModlString((String) firstToken.getValue());
                } else {
                    return new ModlQuoted((String) firstToken.getValue());
                }
            }

            throw new ParserException(String.format("Unexpected token: '%s'", firstToken));
        } else if (firstToken.getType() == TokenType.INTEGER) {
            return new ModlInteger((Integer) firstToken.getValue());
        } else if (firstToken.getType() == TokenType.FLOAT) {
            return new ModlFloat((Float) firstToken.getValue());
        } else if (firstToken.getType() == TokenType.NULL) {
            return ModlBoolNull.MODL_NULL;
        } else if (firstToken.getType() == TokenType.TRUE) {
            return ModlBoolNull.MODL_TRUE;
        } else if (firstToken.getType() == TokenType.FALSE) {
            return ModlBoolNull.MODL_FALSE;
        } else {
            s.pushback(firstToken);
            final ModlPrimitive maybePrimitive = parsePrimitive(s);
            if (maybePrimitive != null) {
                return maybePrimitive;
            }
        }

        throw new ParserException(String.format("Unexpected token: '%s'", firstToken));
    }
}
