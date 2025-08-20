package org.example.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.backend.dto.status.CreateStatusRequest;
import org.example.backend.dto.status.StatusResponse;
import org.example.backend.service.status.StatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/statuses")
@RequiredArgsConstructor
public class StatusController {
    private final StatusService statusService;

    @PostMapping
    public ResponseEntity<StatusResponse> createStatus(@Valid @RequestBody CreateStatusRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(statusService.createStatus(request));
    }

    @GetMapping
    public ResponseEntity<List<StatusResponse>> getAllStatuses() {
        return ResponseEntity.ok(statusService.getAllStatuses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatusResponse> getStatusById(@PathVariable Integer id) {
        return ResponseEntity.ok(statusService.getStatusById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StatusResponse> updateStatus(@PathVariable Integer id, @Valid @RequestBody CreateStatusRequest request) {
        return ResponseEntity.ok(statusService.updateStatus(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatus(@PathVariable Integer id) {
        statusService.deleteStatus(id);
        return ResponseEntity.noContent().build();
    }
}
