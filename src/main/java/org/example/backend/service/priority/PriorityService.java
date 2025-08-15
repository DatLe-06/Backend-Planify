package org.example.backend.service.priority;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.example.backend.entity.Priority;
import org.example.backend.repository.PriorityRepository;
import org.example.backend.utils.MessageUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PriorityService {
    private final PriorityRepository priorityRepository;
    private final MessageUtils messageUtils;

    public Priority findById(int id) {
        return priorityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(messageUtils.getMessage("priority.not.found")));
    } 
}
