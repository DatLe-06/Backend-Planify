package org.example.backend.mapper;

import org.example.backend.constant.Type;
import org.example.backend.dto.history.detail.HistoryDetailResponse;
import org.example.backend.dto.history.HistoryResponse;
import org.example.backend.entity.History;
import org.example.backend.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

@Component
public class HistoryMapper {
    public HistoryResponse toResponse(History history, Set<HistoryDetailResponse> historyDetails) {
        HistoryResponse response = new HistoryResponse();
        response.setId(history.getId());
        response.setEntityId(history.getEntityId());
        response.setNameEntity(history.getName());
        response.setAction(history.getAction().name());
        response.setChangedAt(history.getChangedAt());
        response.setHistoryDetails(historyDetails);
        response.setNameCreator(history.getChangedBy().getUsername());
        return response;
    }

    public History toEntity(Long targetId, String name, Type type, Enum<?> action, User changeBy) {
        History history = new History();
        history.setEntityId(targetId);
        history.setName(name);
        history.setType(type);
        history.setAction(action);
        history.setChangedAt(LocalDateTime.now());
        history.setChangedBy(changeBy);
        return history;
    }
}
