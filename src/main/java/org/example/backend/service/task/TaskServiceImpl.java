package org.example.backend.service.task;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.task.AddTaskRequest;
import org.example.backend.dto.task.TaskResponse;
import org.example.backend.dto.task.UpdateTaskRequest;
import org.example.backend.entity.*;
import org.example.backend.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{
    private final TaskRepository taskRepository;
    private final StatusRepository statusRepository;
    private final PriorityRepository priorityRepository;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final TagRepository tagRepository;
    private final SubTaskRepository subTaskRepository;

    @Override
    public TaskResponse createTask(AddTaskRequest request) {
        Task task = new Task();
        task.setContent(request.getContent());
        task.setStartDate(request.getStartDate());
        task.setEndDate(request.getEndDate());
        task.setCreatedAt(LocalDateTime.now());

        // Set Status
        if (request.getStatusId() != null) {
            Status status = statusRepository.findById(request.getStatusId())
                    .orElseThrow(() -> new RuntimeException("Status not found"));
            task.setStatus(status);
        }

        // Set Priority
        if (request.getPriorityId() != null) {
            Priority priority = priorityRepository.findById(request.getPriorityId())
                    .orElseThrow(() -> new RuntimeException("Priority not found"));
            task.setPriority(priority);
        }

        // Set Members
        if (request.getMemberIds() != null) {
            Set<User> members = new HashSet<>(userRepository.findAllById(request.getMemberIds()));
            task.setMembers(members);
        }

        // Set Project
        if (request.getProjectId() != null) {
            Plan plan = planRepository.findById(request.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found"));
            task.setPlan(plan);
        }

        // Set Tags
        if (request.getTagIds() != null) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(request.getTagIds()));
            task.setTags(tags);
        }

        return mapToResponse(taskRepository.save(task));
    }

    @Override
    public TaskResponse updateTask(Long id, UpdateTaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setContent(request.getContent());
        task.setStartDate(request.getStartDate());
        task.setEndDate(request.getEndDate());
        task.setUpdatedAt(LocalDateTime.now());

        // Set Status
        if (request.getStatusId() != null) {
            Status status = statusRepository.findById(request.getStatusId())
                    .orElseThrow(() -> new RuntimeException("Status not found"));
            task.setStatus(status);
        }

        // Set Priority
        if (request.getPriorityId() != null) {
            Priority priority = priorityRepository.findById(request.getPriorityId())
                    .orElseThrow(() -> new RuntimeException("Priority not found"));
            task.setPriority(priority);
        }

        // Set Members
        if (request.getMemberIds() != null) {
            Set<User> members = new HashSet<>(userRepository.findAllById(request.getMemberIds()));
            task.setMembers(members);
        }

        // Set Project
        if (request.getProjectId() != null) {
            Plan plan = planRepository.findById(request.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found"));
            task.setPlan(plan);
        }

        // Set Tags
        if (request.getTagIds() != null) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(request.getTagIds()));
            task.setTags(tags);
        }

        return mapToResponse(taskRepository.save(task));
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
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

    private TaskResponse mapToResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setContent(task.getContent());

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
        response.setProjectName(task.getPlan() != null ? task.getPlan().getName() : null);
        response.setTagNames(task.getTags().stream().map(Tag::getName).collect(Collectors.toSet()));
        return response;
    }
}

