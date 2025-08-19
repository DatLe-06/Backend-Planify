package org.example.backend.service.task.sub;

import org.example.backend.dto.task.sub.CreateSubTaskRequest;
import org.example.backend.dto.task.sub.SubTaskResponse;
import org.example.backend.dto.task.sub.UpdateSubTaskRequest;

import java.util.List;

public interface SubTaskService {
    SubTaskResponse createSubTask(CreateSubTaskRequest request);
    SubTaskResponse updateSubTask(Long id, UpdateSubTaskRequest request);
    void deleteSubTask(Long id);
    List<SubTaskResponse> getAllSubTasksByTaskId(Long taskId);
}

