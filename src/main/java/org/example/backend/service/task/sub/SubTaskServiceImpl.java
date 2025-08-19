package org.example.backend.service.task.sub;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.task.sub.AddSubTaskRequest;
import org.example.backend.dto.task.sub.SubTaskResponse;
import org.example.backend.entity.SubTask;
import org.example.backend.entity.Task;
import org.example.backend.repository.SubTaskRepository;
import org.example.backend.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubTaskServiceImpl implements SubTaskService{
    private final SubTaskRepository subTaskRepository;
    private final TaskRepository taskRepository;

    @Override
    public SubTaskResponse createSubTask(AddSubTaskRequest request) {
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        SubTask subTask = new SubTask();
        subTask.setTitle(request.getTitle());
        subTask.setCompleted(request.isCompleted());
        subTask.setTask(task);

        subTask = subTaskRepository.save(subTask);

        return mapToResponse(subTask);
    }

    @Override
    public List<SubTaskResponse> getAllSubTasks() {
        return subTaskRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public SubTaskResponse getSubTaskById(Long id) {
        SubTask subTask = subTaskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SubTask not found"));
        return mapToResponse(subTask);
    }

    @Override
    public SubTaskResponse updateSubTask(Long id, AddSubTaskRequest request) {
        SubTask subTask = subTaskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SubTask not found"));

        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        subTask.setTitle(request.getTitle());
        subTask.setCompleted(request.isCompleted());
        subTask.setTask(task);

        subTask = subTaskRepository.save(subTask);

        return mapToResponse(subTask);
    }

    @Override
    public void deleteSubTask(Long id) {
        if (!subTaskRepository.existsById(id)) {
            throw new RuntimeException("SubTask not found");
        }
        subTaskRepository.deleteById(id);
    }

    @Override
    public List<SubTask> getAllSubTasksByTaskId(Long taskId) {
        return subTaskRepository.findAllByTask_Id(taskId);
    }

    private SubTaskResponse mapToResponse(SubTask subTask) {
        return new SubTaskResponse(
                subTask.getId(),
                subTask.getTitle(),
                subTask.isCompleted(),
                subTask.getTask() != null ? subTask.getTask().getId() : null
        );
    }
}

