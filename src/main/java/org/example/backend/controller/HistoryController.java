package org.example.backend.controller;

import lombok.AllArgsConstructor;
import org.example.backend.constant.Type;
import org.example.backend.dto.history.HistoryResponse;
import org.example.backend.service.history.HistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/histories")
@AllArgsConstructor
public class HistoryController {
    private HistoryService historyService;

    @GetMapping
    public ResponseEntity<Page<HistoryResponse>> getHistoriesByTargetId(
            @RequestParam Long id, @RequestParam Type type, Pageable pageable) {
        return ResponseEntity.ok(historyService.getAllHistoryByTargetId(id,type, pageable ));
    }
}
