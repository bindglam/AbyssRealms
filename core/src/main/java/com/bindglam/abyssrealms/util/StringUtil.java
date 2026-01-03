package com.bindglam.abyssrealms.util;

import java.util.Iterator;

public final class StringUtil {
    private StringUtil() {
    }

    public static String combine(Iterable<?> iterable, String prefix, String splitter, String subfix) {
        StringBuilder result = new StringBuilder(prefix);

        Iterator<?> iterator = iterable.iterator();
        while(iterator.hasNext()) {
            result.append(iterator.next().toString());

            if(iterator.hasNext())
                result.append(splitter);
        }

        result.append(subfix);

        return result.toString();
    }
}
