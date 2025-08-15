package org.example.backend.service.task;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.backend.dto.task.AddTaskRequest;
import org.example.backend.dto.task.TaskRequest;
import org.example.backend.dto.task.TaskResponse;
import org.example.backend.dto.task.UpdateTaskRequest;
import org.example.backend.entity.*;
import org.example.backend.repository.*;
import org.example.backend.service.CustomUserDetailsService;
import org.example.backend.service.history.HistoryService;
import org.example.backend.service.plan.PlanService;
import org.example.backend.service.priority.PriorityService;
import org.example.backend.service.status.StatusService;
import org.example.backend.service.tag.TagService;
import org.example.backend.utils.MessageUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{
    private final TaskRepository taskRepository;
    private final StatusService statusService;
    private final PriorityService priorityService;
    private final CustomUserDetailsService userDetailsService;
    private final PlanService planService;
    private final TagService tagService;
    private final SubTaskRepository subTaskRepository;
    private final MessageUtils messageUtils;
    private final HistoryService historyService;

    @Override
    public TaskResponse create(AddTaskRequest request) {
        Task task = new Task();
        validateAndSet(request, task);
        task.setCreatedAt(LocalDateTime.now());
        task.setPlan(planService.findById(request.getPlanId()));

        User user = userDetailsService.currentUser();
        task.setCreatedBy(user);
        historyService.createHistory(Type.TASK, task.getId(), task.getTitle(), Action.Task.CREATE, user);
        return mapToResponse(taskRepository.save(task));
    }

    @Override
    public TaskResponse updateTask(Long id, UpdateTaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(messageUtils.getMessage("task.not.found")));
        validateAndSet(request, task);
        User user = userDetailsService.currentUser();
        historyService.createHistory(Type.TASK, task.getId(), task.getTitle(), Action.Task.UPDATE, user);
        return mapToResponse(taskRepository.save(task));
    }

    @Override
    public String deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(messageUtils.getMessage("task.not.found")));
        User user = userDetailsService.currentUser();
        History history = historyService.createHistory(Type.TASK, task.getId(), task.getTitle(), Action.Task.DELETE, user);
        taskRepository.delete(task);
        return messageUtils.getMessage("delete.task.success", history.getName());
    }

    @Override
    public TaskResponse getTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return mapToResponse(task);
    }

    @Override
    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    private void validateAndSet(TaskRequest request, Task task) {
        task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        task.setStartDate(request.getStartDate());
        task.setEndDate(request.getEndDate());
        task.setUpdatedAt(LocalDateTime.now());

        Status status = statusService.findById(request.getStatusId());
        task.setStatus(status);

        Priority priority = priorityService.findById(request.getPriorityId());
        task.setPriority(priority);

        if (request.getMemberIds() != null) {
            Set<User> members = request.getMemberIds().stream().map(userDetailsService::getById).collect(Collectors.toSet());
            task.setMembers(members);
        }

        if (request.getTagIds() != null) {
            Set<Tag> tags = request.getTagIds().stream().map(tagService::findById).collect(Collectors.toSet());
            task.setTags(tags);
        }
    }

    private TaskResponse mapToResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setContent(task.getDescription());

        List<SubTask> subTasks = subTaskRepository.findAllByTask_Id(task.getId());
        if (subTasks != null && !subTasks.isEmpty() ) {
            long completed = subTasks.stream().filter(SubTask::isCompleted).count();
            response.setProgress(completed / (double) subTasks.size());
        }

        response.setStartDate(task.getStartDate());
        response.setEndDate(task.getEndDate());
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdateAt(task.getUpdatedAt());
        response.setStatusName(task.getStatus() != null ? task.getStatus().getName() : null);
        response.setPriorityName(task.getPriority() != null ? task.getPriority().getName() : null);
        response.setMemberNames(task.getMembers().stream().map(User::getUsername).collect(Collectors.toSet()));
        response.setProjectName(task.getPlan() != null ? task.getPlan().getTitle() : null);
        response.setTagNames(task.getTags().stream().map(Tag::getName).collect(Collectors.toSet()));
        return response;
    }
}

