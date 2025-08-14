package org.example.backend.service.status;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.backend.dto.status.AddStatusRequest;
import org.example.backend.dto.status.StatusResponse;
import org.example.backend.entity.Status;
import org.example.backend.entity.User;
import org.example.backend.repository.StatusRepository;
import org.example.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {
    private final StatusRepository statusRepository;
    private final UserRepository userRepository;

    @Override
    public StatusResponse createStatus(AddStatusRequest request) {
        if (statusRepository.existsByNameAndCreatedBy_Id(request.getName(), request.getUserId())) {
            throw new IllegalArgumentException("Status name already exists for this user");
        }

        User creator = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Status status = new Status();
        status.setName(request.getName());
        status.setDescription(request.getDescription());
        status.setCreatedBy(creator);

        Status saved = statusRepository.save(status);
        return mapToResponse(saved);
    }

    @Override
    public List<StatusResponse> getAllStatuses() {
        return statusRepository.findAll()
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public StatusResponse getStatusById(Integer id) {
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Status not found"));
        return mapToResponse(status);    }

    @Override
    public StatusResponse updateStatus(Integer id, AddStatusRequest request) {
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Status not found"));

        if (!status.getName().equals(request.getName()) &&
                statusRepository.existsByNameAndCreatedBy_Id(request.getName(), request.getUserId())) {
            throw new IllegalArgumentException("Status name already exists for this user");
        }

        User creator = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        status.setName(request.getName());
        status.setDescription(request.getDescription());
        status.setCreatedBy(creator);

        Status updated = statusRepository.save(status);
        return mapToResponse(updated);
    }

    @Override
    public void deleteStatus(Integer id) {
        if (!statusRepository.existsById(id)) {
            throw new EntityNotFoundException("Status not found");
        }
        statusRepository.deleteById(id);
    }

    private StatusResponse mapToResponse(Status status) {
        return new StatusResponse(
                status.getId(),
                status.getName(),
                status.getDescription(),
                status.getCreatedBy().getId(),
                status.getCreatedBy().getUsername()
        );
    }
}

