package org.example.backend.service.history;

import org.example.backend.entity.Action;
import org.example.backend.entity.Type;
import org.example.backend.entity.User;

public interface HistoryService {
    void createHistory(Type targetType, Long targetId, String name, Enum<?> action, User by
    );}
