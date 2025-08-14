package org.example.backend.service.task;

import org.example.backend.dto.task.AddTaskRequest;
import org.example.backend.dto.task.TaskResponse;
import org.example.backend.dto.task.UpdateTaskRequest;

import java.util.List;

public interface TaskService {
    TaskResponse createTask(AddTaskRequest request);
    TaskResponse updateTask(Long id, UpdateTaskRequest request);
    void deleteTask(Long id);
    TaskResponse getTask(Long id);
    List<TaskResponse> getAllTasks();
}

