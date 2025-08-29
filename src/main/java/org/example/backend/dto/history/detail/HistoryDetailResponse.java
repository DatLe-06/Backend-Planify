package org.example.backend.dto.history.detail;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HistoryDetailResponse {
    private Long id;
    private String fieldName;
    private String oldValue;
    private String newValue;
}
