package org.example.backend.repository;

import org.example.backend.constant.Type;
import org.example.backend.entity.Tag;
import org.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsByNameAndTypeAndCreatedBy(String name, Type type, User createdBy);
    Set<Tag> findAllByCreatedByAndType(User createdBy, Type type);
    boolean existsByNameAndTypeAndCreatedByAndIdNot(String name, Type type, User createdBy, Long id);
}
