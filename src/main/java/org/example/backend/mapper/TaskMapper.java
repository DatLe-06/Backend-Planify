package org.example.backend.mapper;

import org.example.backend.dto.task.CreateTaskRequest;
import org.example.backend.dto.task.TaskResponse;
import org.example.backend.dto.task.UpdateTaskRequest;
import org.example.backend.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TaskMapper {
    public TaskResponse toResponse(Task task, List<SubTask> subTasks) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStartDate(task.getStartDate());
        response.setEndDate(task.getEndDate());
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdateAt(task.getUpdatedAt());

        response.setStatusName(task.getStatus().getName());
        response.setPriorityName(task.getPriority().getName());
        response.setPlanName(task.getPlan().getTitle());
        response.setOwnerName(task.getCreatedBy().getUsername());
        if (!subTasks.isEmpty()) {
            long completed = subTasks.stream().filter(SubTask::isCompleted).count();
            response.setProgress(completed / (double) subTasks.size());
        }
        if (!task.getMembers().isEmpty()) {
            response.setMemberNames(task.getMembers().stream().map(User::getUsername).collect(Collectors.toSet()));
        }
        if (!task.getTags().isEmpty()) {
            response.setTagNames(task.getTags().stream().map(Tag::getName).collect(Collectors.toSet()));
        }
        return response;
    }

    public void toEntityForCreate(
            CreateTaskRequest request, Status status, Priority priority, Set<User> members,
            Plan plan, Set<Tag> tags, User createdBy, Task task) {
        task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());

        task.setStartDate(request.getStartDate());
        task.setEndDate(request.getEndDate());
        task.setStatus(status);
        task.setPriority(priority);
        if (!members.isEmpty()) task.setMembers(members);
        if (!tags.isEmpty()) task.setTags(tags);
        task.setPlan(plan);
        task.setCreatedBy(createdBy);
    }

    public void toEntityForUpdate(
            UpdateTaskRequest request, Status status,
            Priority priority, Set<User> members,
            Set<Tag> tags, Task task) {
        task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getStartDate() != null) task.setStartDate(request.getStartDate());
        if (request.getEndDate() != null) task.setEndDate(request.getEndDate());
        if (status != null) task.setStatus(status);
        if (priority != null) task.setPriority(priority);
        if (!members.isEmpty()) task.setMembers(members);
        if (!tags.isEmpty()) task.setTags(tags);
    }
}
