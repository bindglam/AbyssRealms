package com.bindglam.abyssrealms.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;

public record ResourceKey(String group, String value) {
    public String getPath() {
        return "assets/" + group + "/" + value;
    }

    @Override
    public @NotNull String toString() {
        return this.getPath();
    }

    public @Nullable InputStream getResource(ClassLoader classLoader) {
        return classLoader.getResourceAsStream(this.getPath());
    }
}
