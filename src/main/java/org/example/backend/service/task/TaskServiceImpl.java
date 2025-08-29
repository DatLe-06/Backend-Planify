package org.example.backend.service.task;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.backend.constant.Action;
import org.example.backend.constant.Type;
import org.example.backend.dto.task.CreateTaskRequest;
import org.example.backend.dto.task.TaskRequest;
import org.example.backend.dto.task.TaskResponse;
import org.example.backend.dto.task.UpdateTaskRequest;
import org.example.backend.entity.*;
import org.example.backend.exception.custom.TaskNotFoundException;
import org.example.backend.mapper.TaskMapper;
import org.example.backend.repository.TaskRepository;
import org.example.backend.service.history.HistoryService;
import org.example.backend.service.plan.PlanService;
import org.example.backend.service.priority.PriorityServiceImpl;
import org.example.backend.service.status.StatusService;
import org.example.backend.service.tag.TagServiceImpl;
import org.example.backend.service.task.sub.SubTaskService;
import org.example.backend.service.user.CustomUserDetailsService;
import org.example.backend.utils.MessageUtils;
import org.example.backend.utils.ReflectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final CustomUserDetailsService userService;
    private final StatusService statusService;
    private final PriorityServiceImpl priorityService;
    private final TagServiceImpl tagServiceImpl;
    private final SubTaskService subTaskService;
    private final HistoryService historyService;
    private final MessageUtils messageUtils;
    private final TaskMapper taskMapper;
    private final PlanService planService;

    @Transactional
    @Override
    public TaskResponse create(CreateTaskRequest request) {
        User creator = userService.getCurrentUser();
        Task task = new Task();
        validateAndMapEntity(request, task, creator);
        Task createdTask = taskRepository.save(task);

        historyService.createHistory(createdTask.getId(), task.getTitle(), Type.TASK, Action.Task.CREATE, creator, Collections.emptyMap());
        return taskMapper.toResponse(task, subTaskService.getAllSubTasksByTaskId(task.getId()));
    }

    @Transactional
    @Override
    public TaskResponse update(Long id, UpdateTaskRequest request) {
        Task task = findById(id);
        Task oldTask = new Task(task);
        User creator = userService.getCurrentUser();

        validateAndMapEntity(request, task, creator);
        Map<String, String[]> changes = null;
        try {
            changes = ReflectionUtils.detectChanges(oldTask, task);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        if (changes.isEmpty()) throw new IllegalArgumentException(messageUtils.getMessage("task.nothing.change"));

        Task updatedTask = taskRepository.save(task);
        historyService.createHistory(id, task.getTitle(), Type.TASK, Action.Task.UPDATE, creator, changes);
        return taskMapper.toResponse(updatedTask, subTaskService.getAllSubTasksByTaskId(updatedTask.getId()));
    }

    @Transactional
    @Override
    public TaskResponse restore(Long id) {
        User creator = userService.getCurrentUser();
        taskRepository.restore(id);

        Task task = findById(id);
        historyService.createHistory(task.getId(), task.getTitle(), Type.TASK, Action.Task.RESTORE, creator, Collections.emptyMap());
        return taskMapper.toResponse(task, subTaskService.getAllSubTasksByTaskId(task.getId()));
    }

    @Transactional
    @Override
    public void softDelete(Long id) {
        Task task = findById(id);
        User creator = userService.getCurrentUser();
        historyService.createHistory(task.getId(), task.getTitle(), Type.TASK, Action.Task.SOFT_DELETE, creator, Collections.emptyMap());
        taskRepository.delete(task);
    }

    @Transactional
    @Override
    public void hardDelete(Long id) {
        Task task = taskRepository.findTaskSoftDeletedById(id)
                .orElseThrow(() -> new TaskNotFoundException(messageUtils.getMessage("task.not.found")));
        User creator = userService.getCurrentUser();
        historyService.createHistory(task.getId(), task.getTitle(), Type.TASK, Action.Task.HARD_DELETE, creator, Collections.emptyMap());
        taskRepository.hardDelete(task.getId());
    }

    @Override
    public TaskResponse getById(Long id) {
        Task task = findById(id);
        return taskMapper.toResponse(task, subTaskService.getAllSubTasksByTaskId(task.getId()));
    }

    private Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(messageUtils.getMessage("task.not.found")));
    }

    private void validateAndMapEntity(TaskRequest request, Task task, User creator) {
        Plan plan = null;
        if (request instanceof CreateTaskRequest) plan = planService.findById(((CreateTaskRequest) request).getPlanId());
        else plan = task.getPlan();
        Status status = statusService.findById(request.getStatusId());
        Priority priority = priorityService.findById(request.getPriorityId());
        Set<User> members = request.getMemberIds() != null ? userService.getAllByIds(request.getMemberIds()) : null;
        Set<Tag> tags = request.getTagIds() != null ? tagServiceImpl.getAllByIds(request.getTagIds()) : null;

        taskMapper.toEntity(request, status, priority, members, plan, tags, creator, task);
    }
}

