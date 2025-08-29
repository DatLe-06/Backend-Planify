package org.example.backend.service.history;

import lombok.AllArgsConstructor;
import org.example.backend.constant.Type;
import org.example.backend.dto.history.detail.HistoryDetailResponse;
import org.example.backend.dto.history.HistoryResponse;
import org.example.backend.entity.History;
import org.example.backend.entity.HistoryDetail;
import org.example.backend.entity.User;
import org.example.backend.mapper.HistoryDetailMapper;
import org.example.backend.mapper.HistoryMapper;
import org.example.backend.repository.HistoryDetailRepository;
import org.example.backend.repository.HistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HistoryService{
    private final HistoryDetailRepository historyDetailRepository;
    private final HistoryDetailMapper historyDetailMapper;
    private HistoryRepository historyRepository;
    private HistoryMapper historyMapper;
    private HistoryDetailService historyDetailService;

    public void createHistory(Long targetId, String name, Type type, Enum<?> action, User changeBy, Map<String, String[]> changes) {
        History history = historyRepository.save(historyMapper.toEntity(targetId, name, type, action, changeBy));
        historyDetailService.createHistoryDetail(changes, history);
    }

    public Page<HistoryResponse> getAllHistoryByTargetId(Long targetId, Type type, Pageable pageable) {
        Page<History> historiesPage = historyRepository.findAllByIdAndTypeOrderByChangedAtDesc(targetId, type, pageable);
        return historiesPage.map(history -> {
            Set<HistoryDetail> historyDetails = historyDetailRepository.findAllByHistoryId(history.getId());
            Set<HistoryDetailResponse> historyDetailResponses = historyDetails.stream()
                    .map(historyDetailMapper::toResponse)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            return historyMapper.toResponse(history, historyDetailResponses);
        });
    }
}
