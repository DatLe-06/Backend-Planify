package org.example.backend.service.history;

import lombok.AllArgsConstructor;
import org.example.backend.entity.History;
import org.example.backend.entity.Type;
import org.example.backend.entity.User;
import org.example.backend.repository.HistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class HistoryServiceImpl implements HistoryService{
    private final HistoryRepository historyRepository;

    @Override
    public History createHistory(Type targetType, Long targetId, String name, Enum<?> action, User by) {
        History history = new History();
        if (targetType.equals(Type.TASK)) history.setTaskId(targetId);
        else history.setPlanId(targetId);

        history.setName(name);
        history.setAction(action);
        history.setChangedBy(by);
        history.setChangedAt(LocalDateTime.now());
        return historyRepository.save(history);
    }
}
