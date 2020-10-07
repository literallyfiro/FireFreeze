package me.onlyfire.firefreeze.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MapUtil {

    public static <T, E> T getKeyFromValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

}
