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

package uk.modl.extractors;

import io.vavr.collection.Vector;
import lombok.Getter;
import lombok.Value;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import uk.modl.model.Array;
import uk.modl.model.Pair;
import uk.modl.model.PairValue;
import uk.modl.model.Primitive;
import uk.modl.utils.Util;

import java.util.Objects;

@Getter
public class StarLoadExtractor {

    private Vector<LoadSet> loadSets = Vector.empty();

    /**
     * Check whether the key represents a LOAD instruction
     *
     * @param key the String to check
     * @return true if the key represents a LOAD instruction
     */
    public static boolean isLoadInstruction(final String key) {
        return StringUtils.equalsAnyIgnoreCase(key, "*l", "*load");
    }

    public StarLoadExtractor accept(final Pair pair) {
        final String key = pair.getKey();

        if (isLoadInstruction(key)) {

            val specs = getFilenames(pair.getValue())
                    .map(Util::normalize);

            loadSets = loadSets.append(new LoadSet(pair, specs));
        }
        return this;
    }

    /**
     * Map a PairValue to a list of Strings - for use as file names.
     * This is only applicable to *load MODL instructions
     *
     * @param pairValue PairValue
     * @return Vector of String
     */
    public Vector<String> getFilenames(final PairValue pairValue) {
        if (pairValue instanceof Primitive) {
            final Primitive pv = (Primitive) pairValue;
            return Vector.of(pv.toString());
        } else if (pairValue instanceof Array) {
            final Array a = (Array) pairValue;
            return Vector.ofAll(a.getArrayItems()
                    .map(Objects::toString));
        }
        return Vector.empty();
    }

    /**
     * A Pair can represent several files to be loaded.
     */
    @Value(staticConstructor = "of")
    public static class LoadSet {

        Pair pair;

        Vector<FileSpec> fileSet;

    }

    /**
     * A file can be immutable or forced load, and has a filename
     */
    @Value(staticConstructor = "of")
    public static class FileSpec {

        String filename;

        boolean forceLoad;

    }

}
