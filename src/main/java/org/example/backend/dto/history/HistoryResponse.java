package org.example.backend.dto.history;

import lombok.Getter;
import lombok.Setter;
import org.example.backend.dto.history.detail.HistoryDetailResponse;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
public class HistoryResponse {
    private Long id;
    private Long entityId;
    private String nameEntity;
    private String action;
    private Set<HistoryDetailResponse> historyDetails;
    private LocalDateTime changedAt;
    private String nameCreator;
}
