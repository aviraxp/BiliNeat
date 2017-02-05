package me.iacn.bilineat.util;

import java.lang.reflect.Field;

/**
 * Created by iAcn on 2017/2/5
 * Emali iAcn0301@foxmail.com
 */

public class ReflectUtils {

    public static <T> T getObjectField(Object obj, String fieldName, Class<T> clazz) {
        T value = null;

        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            value = (T) field.get(obj);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (value == null) throw new NullPointerException("Field not found");

        return value;
    }
}