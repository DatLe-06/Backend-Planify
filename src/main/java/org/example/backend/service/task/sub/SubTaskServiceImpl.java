package org.example.backend.service.task.sub;

import lombok.AllArgsConstructor;
import org.example.backend.dto.task.sub.CreateSubTaskRequest;
import org.example.backend.dto.task.sub.SubTaskResponse;
import org.example.backend.dto.task.sub.UpdateSubTaskRequest;
import org.example.backend.entity.SubTask;
import org.example.backend.entity.Task;
import org.example.backend.exception.custom.TaskNotFoundException;
import org.example.backend.mapper.SubTaskMapper;
import org.example.backend.repository.SubTaskRepository;
import org.example.backend.repository.TaskRepository;
import org.example.backend.service.user.CustomUserDetailsService;
import org.example.backend.utils.MessageUtils;
import org.example.backend.validation.PermissionValidate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubTaskServiceImpl implements SubTaskService {
    private SubTaskRepository subTaskRepository;
    private TaskRepository taskRepository;
    private SubTaskMapper subTaskMapper;
    private MessageUtils messageUtils;
    private CustomUserDetailsService customUserDetailsService;
    private PermissionValidate permissionValidate;

    @Override
    public SubTaskResponse createSubTask(CreateSubTaskRequest request) {
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new TaskNotFoundException(messageUtils.getMessage("task.not.found")));
        permissionValidate.canManageSubTask(customUserDetailsService.getCurrentUser(), task.getPlan());

        SubTask subTask = subTaskMapper.toEntityForCreate(request, task);
        subTaskRepository.save(subTask);
        return subTaskMapper.toResponse(subTask);
    }

    @Override
    public SubTaskResponse updateSubTask(Long id, UpdateSubTaskRequest request) {
        Task task = taskRepository.findById(request.getTaskId()).orElseThrow(() -> new TaskNotFoundException(messageUtils.getMessage("task.not.found")));
        permissionValidate.canManageSubTask(customUserDetailsService.getCurrentUser(), task.getPlan());
        SubTask subTask = subTaskRepository.findById(id).orElseThrow(() -> new RuntimeException("SubTask not found"));

        subTaskMapper.toEntityForUpdate(request, subTask);
        subTaskRepository.save(subTask);
        return subTaskMapper.toResponse(subTask);
    }

    @Override
    public void deleteSubTask(Long id) {
        SubTask subTask = subTaskRepository.findById(id).orElseThrow(() -> new RuntimeException("SubTask not found"));
        Task task = taskRepository.findById(subTask.getTask().getId()).orElseThrow(() -> new TaskNotFoundException(messageUtils.getMessage("task.not.found")));
        permissionValidate.canManageSubTask(customUserDetailsService.getCurrentUser(), task.getPlan());
        subTaskRepository.deleteById(id);
    }

    @Override
    public List<SubTaskResponse> getAllSubTasksByTaskId(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException(messageUtils.getMessage("task.not.found")));
        permissionValidate.accessTaskOrSubTask(customUserDetailsService.getCurrentUser(), task.getPlan());
        return subTaskRepository.findAllByTask_IdOrderByCreatedAtDesc(taskId)
                .stream().map(subTaskMapper::toResponse)
                .collect(Collectors.toList());
    }
}

