package org.example.backend.service.task;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.backend.constant.Action;
import org.example.backend.constant.Type;
import org.example.backend.dto.task.CreateTaskRequest;
import org.example.backend.dto.task.TaskResponse;
import org.example.backend.dto.task.UpdateTaskRequest;
import org.example.backend.entity.*;
import org.example.backend.exception.custom.TaskNotFoundException;
import org.example.backend.mapper.TaskMapper;
import org.example.backend.repository.TaskRepository;
import org.example.backend.service.user.CustomUserDetailsService;
import org.example.backend.service.history.HistoryService;
import org.example.backend.service.plan.PlanService;
import org.example.backend.service.priority.PriorityServiceImpl;
import org.example.backend.service.status.StatusService;
import org.example.backend.service.tag.TagServiceImpl;
import org.example.backend.service.task.sub.SubTaskService;
import org.example.backend.utils.MessageUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final PlanService planService;
    private final CustomUserDetailsService userService;
    private final StatusService statusService;
    private final PriorityServiceImpl priorityService;
    private final TagServiceImpl tagServiceImpl;
    private final SubTaskService subTaskService;
    private final HistoryService historyService;
    private final MessageUtils messageUtils;
    private final TaskMapper taskMapper;

    @Transactional
    @Override
    public TaskResponse create(CreateTaskRequest request) {
        User currentUser = userService.getCurrentUser();
        Task task = new Task();

        Plan plan = planService.findById(request.getPlanId());
        Status status = statusService.findById(request.getStatusId());
        Priority priority = priorityService.findById(request.getPriorityId());
        Set<User> members = (request.getMemberIds() != null) ? userService.getAllByIds(request.getMemberIds()) : task.getMembers();
        Set<Tag> tags = (request.getTagIds() != null) ? tagServiceImpl.getAllByIds(request.getTagIds()) : task.getTags();

        taskMapper.toEntityForCreate(request, status, priority, members, plan, tags, currentUser, task);
        taskRepository.save(task);

        historyService.createHistory(Type.TASK, task.getId(), task.getTitle(), Action.Task.CREATE, currentUser);
        return taskMapper.toResponse(task, subTaskService.getAllSubTasksByTaskId(task.getId()));
    }

    @Transactional
    @Override
    public TaskResponse updateTask(Long id, UpdateTaskRequest request) {
        Task task = findById(id);
        User currentUser = userService.getCurrentUser();

        Status status = (request.getStatusId() != null) ? statusService.findById(request.getStatusId()) : task.getStatus();
        Priority priority = (request.getPriorityId() != null) ? priorityService.findById(request.getPriorityId()) : task.getPriority();
        Set<User> members = (request.getMemberIds() != null) ? userService.getAllByIds(request.getMemberIds()) : task.getMembers();
        Set<Tag> tags = (request.getTagIds() != null) ? tagServiceImpl.getAllByIds(request.getTagIds()) : task.getTags();

        taskMapper.toEntityForUpdate(request, status, priority, members, tags, task);
        taskRepository.save(task);

        historyService.createHistory(Type.TASK, task.getId(), task.getTitle(), Action.Task.UPDATE, currentUser);
        return taskMapper.toResponse(task, subTaskService.getAllSubTasksByTaskId(task.getId()));
    }

    @Transactional
    @Override
    public String deleteTask(Long id) {
        Task task = findById(id);
        User currentUser = userService.getCurrentUser();

        History history = historyService.createHistory(Type.TASK, task.getId(), task.getTitle(), Action.Task.DELETE, currentUser);
        taskRepository.delete(task);

        return messageUtils.getMessage("delete.task.success", history.getName());
    }

    @Override
    public TaskResponse getTask(Long id) {
        Task task = findById(id);
        return taskMapper.toResponse(task, subTaskService.getAllSubTasksByTaskId(task.getId()));
    }

    @Override
    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(task -> taskMapper.toResponse(task, subTaskService.getAllSubTasksByTaskId(task.getId())))
                .toList();
    }

    private Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(messageUtils.getMessage("task.not.found")));
    }
}

