package com.restaurant.collect;

import com.restaurant.base.HasValue;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public class MoreCollections {
    public static boolean isNullOrEmpty(Collection collection) {
        return null == collection || collection.isEmpty();
    }

    private static final Map<Class<? extends Enum>, Map<?, ? extends Enum>> enumByValueCache = newHashMap();
    public static <E extends Enum<E> & HasValue<T>, T> Map<T, E> enumsByValue(final Class<E> enumType) {
        if (!enumByValueCache.containsKey(enumType)) {
            final Map<T, E> enumByValueMap = newHashMap();
            final EnumSet<E> es = EnumSet.allOf(enumType);
            for (E enumValue : es) {
                enumByValueMap.put(enumValue.getValue(), enumValue);
            }
            enumByValueCache.put(enumType, enumByValueMap);
        }
        return (Map<T, E>) enumByValueCache.get(enumType);
    }
}
