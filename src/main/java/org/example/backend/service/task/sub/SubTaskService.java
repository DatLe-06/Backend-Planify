package org.example.backend.service.task.sub;

import org.example.backend.dto.task.sub.AddSubTaskRequest;
import org.example.backend.dto.task.sub.SubTaskResponse;

import java.util.List;

public interface SubTaskService {
    SubTaskResponse createSubTask(AddSubTaskRequest request);
    List<SubTaskResponse> getAllSubTasks();
    SubTaskResponse getSubTaskById(Long id);
    SubTaskResponse updateSubTask(Long id, AddSubTaskRequest request);
    void deleteSubTask(Long id);
}

