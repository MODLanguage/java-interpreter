package uk.modl.interpreter;

import uk.modl.parser.ModlObjectCreator;
import uk.modl.parser.RawModlObject;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by alex on 20/09/2018.
 */
public class FileLoader {
    private static Map<String, ModlAndTtl> cache = new HashMap<>();

    public static RawModlObject loadFile(String location) {
        String contents = null;
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
                if (cache.get(location).endTime >= (System.currentTimeMillis() / 1000)) {
                    return cache.get(location).rawModlObject;
                } else {
                    cache.remove(location);
                }
            }
        } else {
            cache.remove(location);
        }
        if (location.startsWith("http:")) {
            // Load from URI
            try {
                contents = new Scanner(new URL(location).openStream(), "UTF-8").useDelimiter("\\A").next();
            } catch (IOException e) {
                throw new RuntimeException(("Could not make URI : " + location + ", error : " + e));
            }
        } else {
            try {
                contents = new String(Files.readAllBytes(Paths.get(location)));
            } catch (IOException e) {
                throw new RuntimeException(("Could not open file : " + location + ", error : " + e));
            }
        }
        RawModlObject rawModlObject = null;
        try {
            rawModlObject = ModlObjectCreator.processModlParsed(contents);
        } catch (IOException e) {
            throw new RuntimeException("Could not interpret " + location + ", error : " + e);
        }
        long endTime = (System.currentTimeMillis() / 1000) + 3600;
        ModlAndTtl modlAndTtl = new ModlAndTtl(endTime, rawModlObject);
        cache.put(location, modlAndTtl);
        return rawModlObject;
    }
}
