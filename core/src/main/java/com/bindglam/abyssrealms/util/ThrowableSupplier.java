package com.bindglam.abyssrealms.util;

import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

public interface ThrowableSupplier<T> {
    T get() throws Exception;

    default @Nullable T getOrLog(@Nullable T defaultValue) {
        try {
            return get();
        } catch (Exception e) {
            Logger.error(e);
            return defaultValue;
        }
    }

    default @Nullable T getOrLog() {
        return getOrLog(null);
    }
}
