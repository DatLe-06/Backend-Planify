package org.example.backend.service.tag;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.example.backend.constant.Type;
import org.example.backend.dto.tag.CreateTagRequest;
import org.example.backend.dto.tag.TagResponse;
import org.example.backend.dto.tag.UpdateTagRequest;
import org.example.backend.entity.Tag;
import org.example.backend.entity.User;
import org.example.backend.mapper.TagMapper;
import org.example.backend.repository.TagRepository;
import org.example.backend.service.user.CustomUserDetailsService;
import org.example.backend.utils.MessageUtils;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TagServiceImpl implements TagService{
    private final TagRepository tagRepository;
    private final MessageUtils messageUtils;
    private final CustomUserDetailsService customUserDetailsService;
    private final TagMapper tagMapper;

    @Override
    public Tag findById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(messageUtils.getMessage("tag.not.found")));
    }

    @Override
    public Set<Tag> getAllByIds(Set<Long> ids) {
        return new HashSet<>(tagRepository.findAllById(ids));
    }

    @Override
    public TagResponse create(CreateTagRequest request) {
        User user = customUserDetailsService.getCurrentUser();

        if (tagRepository.existsByNameAndTypeAndCreatedBy(request.getName(), Type.valueOf(request.getType()), user)) {
            throw new IllegalArgumentException(messageUtils.getMessage("tag.already.exist"));
        }

        Tag tag = tagMapper.toEntityForCreate(request, user);
        return tagMapper.toResponse(tagRepository.save(tag));
    }

    @Override
    public TagResponse update(Long id, UpdateTagRequest request) {
        User user = customUserDetailsService.getCurrentUser();
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(messageUtils.getMessage("tag.not.found")));

        if (tagRepository.existsByNameAndTypeAndCreatedByAndIdNot(request.getName(), tag.getType(), user, tag.getId())) {
            throw new IllegalArgumentException(messageUtils.getMessage("tag.already.exist"));
        }

        tagMapper.toEntityForUpdate(request, tag);
        return tagMapper.toResponse(tagRepository.save(tag));
    }

    @Override
    public void delete(Long id) {
        if (!tagRepository.existsById(id)) throw new EntityNotFoundException(messageUtils.getMessage("tag.not.found"));
        tagRepository.deleteById(id);
    }

    @Override
    public Set<TagResponse> getAllTags(Type type) {
        User user = customUserDetailsService.getCurrentUser();
        return tagRepository.findAllByCreatedByAndType(user, type)
                .stream().map(tagMapper::toResponse).collect(Collectors.toSet());
    }
}
