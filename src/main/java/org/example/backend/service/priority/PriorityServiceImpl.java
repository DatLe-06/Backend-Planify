package org.example.backend.service.priority;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.example.backend.constant.Direction;
import org.example.backend.constant.Type;
import org.example.backend.dto.priority.CreatePriorityRequest;
import org.example.backend.dto.priority.PriorityResponse;
import org.example.backend.dto.priority.UpdatePriorityRequest;
import org.example.backend.entity.Priority;
import org.example.backend.entity.User;
import org.example.backend.mapper.PriorityMapper;
import org.example.backend.repository.PriorityRepository;
import org.example.backend.service.user.CustomUserDetailsService;
import org.example.backend.utils.MessageUtils;
import org.example.backend.validation.PermissionValidate;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PriorityServiceImpl implements PriorityService {
    private final CustomUserDetailsService customUserDetailsService;
    private final PermissionValidate permissionValidate;
    private PriorityRepository priorityRepository;
    private MessageUtils messageUtils;
    private PriorityMapper priorityMapper;

    @Override
    public Priority findById(Integer id) {
        return priorityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(messageUtils.getMessage("priority.not.found")));
    }

    @Override
    public PriorityResponse create(CreatePriorityRequest request) {
        User user = customUserDetailsService.getCurrentUser();

        if (priorityRepository.existsByNameAndTypeAndCreatedBy(request.getName(), Type.valueOf(request.getType()), user)) {
            throw new IllegalArgumentException(messageUtils.getMessage("priority.already.exist"));
        }
        int maxOrder = priorityRepository.findMaxOrderPriority(user.getId()) == null ? 0 : priorityRepository.findMaxOrderPriority(user.getId());

        Priority priority = priorityMapper.toEntityForCreate(request, user, maxOrder);
        return priorityMapper.toResponse(priorityRepository.save(priority));
    }

    @Override
    public PriorityResponse update(Integer id, UpdatePriorityRequest request) {
        User user = customUserDetailsService.getCurrentUser();
        Priority priority = priorityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(messageUtils.getMessage("priority.not.found")));

        if (priorityRepository.existsByNameAndTypeAndCreatedByAndIdNot(request.getName(), priority.getType(), user, priority.getId())) {
            throw new IllegalArgumentException(messageUtils.getMessage("priority.already.exist"));
        }

        priorityMapper.toEntityForUpdate(priority, request);
        permissionValidate.canManagePriority(priority, user);
        return priorityMapper.toResponse(priorityRepository.save(priority));
    }

    @Override
    public void delete(Integer id) {
        User user = customUserDetailsService.getCurrentUser();
        Priority priority = priorityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(messageUtils.getMessage("priority.not.found")));
        permissionValidate.canManagePriority(priority, user);
        priorityRepository.deleteById(id);
    }

    @Override
    public Set<PriorityResponse> getAllPriorities(Type type, Direction direction) {
        User user = customUserDetailsService.getCurrentUser();
        List<Priority> priorities;
        if (direction.equals(Direction.ASC)) {
            priorities = priorityRepository.getAllByCreatedByAndTypeOrderByOrderPriorityAscNameAsc(user, type);
        } else {
            priorities = priorityRepository.getAllByCreatedByAndTypeOrderByOrderPriorityDescNameAsc(user, type);
        }
        return priorities.stream().map(priorityMapper::toResponse).collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
