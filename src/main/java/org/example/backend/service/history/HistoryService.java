package org.example.backend.service.history;

import org.example.backend.entity.Action;
import org.example.backend.entity.History;
import org.example.backend.entity.Type;
import org.example.backend.entity.User;

public interface HistoryService {
    History createHistory(Type targetType, Long targetId, String name, Enum<?> action, User by);
}
