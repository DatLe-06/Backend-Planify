package org.example.backend.service.tag;

import org.example.backend.constant.Type;
import org.example.backend.dto.tag.CreateTagRequest;
import org.example.backend.dto.tag.TagResponse;
import org.example.backend.dto.tag.UpdateTagRequest;
import org.example.backend.entity.Tag;

import java.util.Set;

public interface TagService {
    Tag findById(Long id);
    Set<Tag> getAllByIds(Set<Long> ids);
    TagResponse create(CreateTagRequest request);
    TagResponse update(Long id, UpdateTagRequest request);
    void delete(Long id);
    Set<TagResponse> getAllTags(Type type);
}
