package org.example.backend.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.backend.constant.Action;

import java.util.HashMap;
import java.util.Map;

@Converter
public class ActionConverter implements AttributeConverter<Enum<?>, String> {
    private static final Map<String, Enum<?>> STRING_TO_ENUM = new HashMap<>();

    static {
        for (Action.Task e : Action.Task.values()) {
            STRING_TO_ENUM.put("TASK_" + e.name(), e);
        }
        for (Action.Plan e : Action.Plan.values()) {
            STRING_TO_ENUM.put("PLAN_" + e.name(), e);
        }
        for (Action.Tag e : Action.Tag.values()) {
            STRING_TO_ENUM.put("TAG_" + e.name(), e);
        }
        for (Action.Member e : Action.Member.values()) {
            STRING_TO_ENUM.put("MEMBER_" + e.name(), e);
        }
    }

    @Override
    public String convertToDatabaseColumn(Enum<?> attribute) {
        if (attribute == null) return null;
        if (attribute instanceof Action.Task) return "TASK_" + attribute.name();
        if (attribute instanceof Action.Plan) return "PLAN_" + attribute.name();
        if (attribute instanceof Action.Tag) return "TAG_" + attribute.name();
        if (attribute instanceof Action.Member) return "MEMBER_" + attribute.name();
        throw new IllegalArgumentException("Unknown enum type: " + attribute.getClass());
    }

    @Override
    public Enum<?> convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        Enum<?> e = STRING_TO_ENUM.get(dbData);
        if (e == null) throw new IllegalArgumentException("Unknown database value: " + dbData);
        return e;
    }
}
