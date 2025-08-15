package org.example.backend.service.tag;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.example.backend.entity.Tag;
import org.example.backend.repository.TagRepository;
import org.example.backend.utils.MessageUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final MessageUtils messageUtils;

    public Tag findById(long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(messageUtils.getMessage("tag.not.found")));
    }
}
