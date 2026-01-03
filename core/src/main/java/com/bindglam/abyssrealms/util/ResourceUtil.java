package com.bindglam.abyssrealms.util;

import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.io.*;

public final class ResourceUtil {
    private ResourceUtil() {
    }

    public static @Nullable String readResource(ResourceKey key, ClassLoader classLoader) {
        InputStream inputStream = key.getResource(classLoader);
        if(inputStream == null) {
            Logger.error("The resource is not found.");
            return null;
        }

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.readAllAsString();
        } catch (IOException e) {
            Logger.error(e);
            return null;
        }
    }

    public static @Nullable String readFile(File file) {
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return reader.readAllAsString();
        } catch (IOException e) {
            Logger.error(e);
            return null;
        }
    }
}
