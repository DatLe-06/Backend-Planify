package org.example.backend.service.history;

import lombok.AllArgsConstructor;
import org.example.backend.entity.History;
import org.example.backend.entity.HistoryDetail;
import org.example.backend.mapper.HistoryDetailMapper;
import org.example.backend.repository.HistoryDetailRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@AllArgsConstructor
public class HistoryDetailService {
    private final HistoryDetailMapper historyDetailMapper;
    private HistoryDetailRepository historyDetailRepository;

    public void createHistoryDetail(Map<String, String[]> changes, History history) {
        Set<HistoryDetail> historyDetails = new HashSet<>();
        for (Map.Entry<String, String[]> entry : changes.entrySet()) {
            historyDetails.add(historyDetailMapper.toEntity(entry.getKey(), entry.getValue(), history));
        }
        historyDetailRepository.saveAll(historyDetails);
    }
}
