package org.example.backend.mapper;

import org.example.backend.constant.Type;
import org.example.backend.dto.tag.CreateTagRequest;
import org.example.backend.dto.tag.TagResponse;
import org.example.backend.dto.tag.UpdateTagRequest;
import org.example.backend.entity.Tag;
import org.example.backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {
    public TagResponse toResponse(Tag tag) {
        TagResponse response = new TagResponse();
        response.setId(tag.getId());
        response.setName(tag.getName());
        if (tag.getDescription() != null) response.setDescription(tag.getDescription());
        response.setType(tag.getType().name());
        return response;
    }

    public Tag toEntityForCreate(CreateTagRequest request, User user) {
        Tag tag = new Tag();
        tag.setName(request.getName());
        if (request.getDescription() != null) tag.setDescription(request.getDescription());
        tag.setCreatedBy(user);
        tag.setType(Type.valueOf(request.getType()));
        return tag;
    }

    public void toEntityForUpdate(UpdateTagRequest request, Tag tag) {
        tag.setName(request.getName());
        if (request.getDescription() != null) request.setDescription(request.getDescription());
    }
}
