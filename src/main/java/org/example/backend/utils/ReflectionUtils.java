package org.example.backend.utils;

import jakarta.persistence.Column;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ReflectionUtils {
    public static Map<String, String[]> detectChanges(Object oldObj, Object newObj) throws IllegalAccessException {
        Map<String, String[]> changes = new HashMap<>();

        for (Field field : oldObj.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            Object oldVal = field.get(oldObj);
            Object newVal = field.get(newObj);

            if (!Objects.equals(oldVal, newVal)) {
                Column column = field.getAnnotation(Column.class);
                String fieldName = (column != null) ? column.name() : field.getName();

                changes.put(fieldName, new String[]{String.valueOf(oldVal), String.valueOf(newVal)});
            }
        }

        return changes;
    }
}
