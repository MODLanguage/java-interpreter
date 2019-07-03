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

package uk.modl.interpreter;

import uk.modl.parser.ModlObjectCreator;
import uk.modl.parser.RawModlObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by alex on 20/09/2018.
 */
class FileLoader {
    private static Map<String, ModlCacheEntry> cache = new HashMap<>();

    static RawModlObject loadFile(final List<String> filesLoaded, String location) {
        String contents;
        boolean forceReload = false;
        if (location.endsWith("!")) {
            forceReload = true;
            location = location.substring(0, location.length() - 1);
        }
        // If no .modl or no .txt at end, then add .modl
        if (!location.endsWith(".modl") && !location.endsWith(".txt")) {
            location = location + ".modl";
        }
        if (!forceReload) {
            if (cache.get(location) != null) {
                if (!cache.get(location)
                        .expired()) {
                    filesLoaded.add(location);
                    return cache.get(location).rawModlObject;
                } else {
                    // Keep it because we might need to use it if our network connection is down.
                }
            }
        }

        if (location.startsWith("http")) {
            // Load from URI
            try {
                contents =
                        new Scanner(new URL(location).openStream(), StandardCharsets.UTF_8.name()).useDelimiter("\\A")
                                .next();
            } catch (IOException e) {
                // Maybe its still in the cache...
                if (cache.get(location) != null) {
                    return cache.get(location).rawModlObject;
                }

                // No, so throw an exception
                throw new RuntimeException(("Could not make URI : " + location + ", error : " + e));
            }
        } else {
            try {
                contents = new String(Files.readAllBytes(Paths.get(location)));
            } catch (IOException e) {
                // Maybe its still in the cache...
                if (cache.get(location) != null) {
                    return cache.get(location).rawModlObject;
                }

                // No, so throw an exception
                throw new RuntimeException(("Could not open file : " + location + ", error : " + e));
            }
        }
        RawModlObject rawModlObject;
        try {
            rawModlObject = ModlObjectCreator.processModlParsed(contents);
        } catch (IOException e) {
            throw new RuntimeException("Could not interpret " + location + ", error : " + e);
        }
        long endTime = (System.currentTimeMillis() / 1000) + 3600;
        ModlCacheEntry modlCacheEntry = new ModlCacheEntry(endTime, rawModlObject);
        cache.put(location, modlCacheEntry);
        filesLoaded.add(location);
        return rawModlObject;
    }
}
