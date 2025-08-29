package org.example.backend.service.task;

import org.example.backend.dto.task.CreateTaskRequest;
import org.example.backend.dto.task.TaskResponse;
import org.example.backend.dto.task.UpdateTaskRequest;

public interface TaskService {
    TaskResponse create(CreateTaskRequest request);
    TaskResponse update(Long id, UpdateTaskRequest request);
    void hardDelete(Long id);
    TaskResponse getById(Long id);
    void softDelete(Long id);
    TaskResponse restore(Long id);
}

