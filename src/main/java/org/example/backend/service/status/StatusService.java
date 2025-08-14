package org.example.backend.service.status;

import org.example.backend.dto.status.AddStatusRequest;
import org.example.backend.dto.status.StatusResponse;

import java.util.List;

public interface StatusService {
    StatusResponse createStatus(AddStatusRequest request);
    List<StatusResponse> getAllStatuses();
    StatusResponse getStatusById(Integer id);
    StatusResponse updateStatus(Integer id, AddStatusRequest request);
    void deleteStatus(Integer id);
}
