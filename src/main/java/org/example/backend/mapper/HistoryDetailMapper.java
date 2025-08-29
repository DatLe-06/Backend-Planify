package org.example.backend.mapper;

import org.example.backend.dto.history.detail.HistoryDetailResponse;
import org.example.backend.entity.History;
import org.example.backend.entity.HistoryDetail;
import org.springframework.stereotype.Component;

@Component
public class HistoryDetailMapper {
    public HistoryDetailResponse toResponse(HistoryDetail historyDetail) {
        HistoryDetailResponse response = new HistoryDetailResponse();
        response.setFieldName(historyDetail.getFieldName());
        response.setOldValue(historyDetail.getOldValue());
        response.setNewValue(historyDetail.getNewValue());
        return response;
    }

    public HistoryDetail toEntity(String key, String[] value, History history) {
        HistoryDetail historyDetail = new HistoryDetail();
        historyDetail.setFieldName(key);
        historyDetail.setOldValue(value[0]);
        historyDetail.setNewValue(value[0]);
        historyDetail.setHistory(history);
        return historyDetail;
    }
}
