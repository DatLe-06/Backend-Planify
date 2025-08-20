package org.example.backend.service.status;

import org.example.backend.dto.status.CreateStatusRequest;
import org.example.backend.dto.status.StatusResponse;
import org.example.backend.entity.Status;

import java.util.List;

public interface StatusService {
    Status findById(Integer id);
    StatusResponse createStatus(CreateStatusRequest request);
    List<StatusResponse> getAllStatuses();
    StatusResponse getStatusById(Integer id);
    StatusResponse updateStatus(Integer id, CreateStatusRequest request);
    void deleteStatus(Integer id);
}
