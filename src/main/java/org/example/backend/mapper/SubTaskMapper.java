package org.example.backend.mapper;

import org.example.backend.dto.task.sub.CreateSubTaskRequest;
import org.example.backend.dto.task.sub.SubTaskResponse;
import org.example.backend.dto.task.sub.UpdateSubTaskRequest;
import org.example.backend.entity.SubTask;
import org.example.backend.entity.Task;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SubTaskMapper {
    public SubTaskResponse toResponse(SubTask subTask) {
        return new SubTaskResponse(
                subTask.getId(),
                subTask.getTitle(),
                subTask.isCompleted(),
                subTask.getTask().getId()
        );
    }

    public SubTask toEntityForCreate(CreateSubTaskRequest request, Task task) {
        SubTask subTask = new SubTask();
        subTask.setTitle(request.getTitle());
        subTask.setCompleted(false);
        subTask.setCreatedAt(LocalDateTime.now());
        subTask.setTask(task);
        return subTask;
    }

    public void toEntityForUpdate(UpdateSubTaskRequest request, SubTask subTask) {
        subTask.setTitle(request.getTitle());
        subTask.setCompleted(request.isCompleted());
    }
}
